package com.hustunique.vlive.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.hustunique.vlive.agora.AgoraMessageModule
import com.hustunique.vlive.agora.AgoraModule
import com.hustunique.vlive.data.Vector3
import com.hustunique.vlive.databinding.ActivitySceneBinding
import com.hustunique.vlive.filament.FilamentContext
import com.hustunique.vlive.filament.FilamentLocalController
import com.hustunique.vlive.filament.model_object.ActorModelObject
import com.hustunique.vlive.filament.model_object.SceneModelObject
import com.hustunique.vlive.filament.model_object.ScreenModelObject
import com.hustunique.vlive.filament.model_object.UnityCubeModelObject
import com.hustunique.vlive.local.GroupMemberManager
import com.hustunique.vlive.local.UnityObjectManager
import com.hustunique.vlive.local.VirtualCharacterPropertyProvider
import com.hustunique.vlive.opengl.GLRender
import com.hustunique.vlive.remote.Service
import com.hustunique.vlive.util.ToastUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SceneActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SceneActivity"

    }

    private val args by navArgs<SceneActivityArgs>()

    private val binding by lazy {
        ActivitySceneBinding.inflate(layoutInflater)
    }

    private val localController by lazy {
        FilamentLocalController(this)
    }
    private val characterPropertyProvider by lazy {
        if (args.mode != 0) {
            VirtualCharacterPropertyProvider(this, localController::onCharacterPropertyReady)
        } else null
    }

    private val viewModel by viewModels<SceneViewModel>()

    lateinit var screenModelObject: ScreenModelObject

    private lateinit var glRender: GLRender

    private lateinit var agoraModule: AgoraModule

    private val insetsController by lazy { ViewCompat.getWindowInsetsController(binding.root) }

    private val groupMemberManager by lazy {
        GroupMemberManager(
            binding.filamentView::addModelObject,
            binding.filamentView::removeModelObject
        )
    }

    private val agoraMessageModule by lazy {
        AgoraMessageModule(
            this,
            args.cid,
            groupMemberManager::onMatrix,
            groupMemberManager::rtmModeChoose
        )
    }

    private val unityBlockManager by lazy {
        UnityObjectManager(
            binding.filamentView::addModelObject,
            binding.filamentView::removeModelObject
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        insetsController?.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
        localController.onUpdate = agoraMessageModule::sendMessage

        glRender = GLRender().apply {
            init()
        }
        Log.i(TAG, "onCreate: vlive viewmodel $viewModel")

        agoraModule =
            AgoraModule(
                this,
                args.mode,
                groupMemberManager::rtcJoin,
                groupMemberManager::rtcQuit
            ).apply {
                initAgora()
                joinChannel(args.cid)
            }.also { groupMemberManager.agoraModule = it }
        agoraMessageModule.mode = args.mode
        groupMemberManager.onMemberUpdate = { viewModel.memberInfo.postValue(it) }

        if (args.cid == "UnityUser") {
            agoraMessageModule.onUnityMessage = unityBlockManager::onEvent
        }

        localController.onCameraUpdate = { pos, rotation ->
            agoraModule.audioModule?.run {
                setHeadPosRotation(pos, rotation)
            }
        }

        binding.filamentView.apply {
            filamentContext = FilamentContext(this, glRender.getEglContext())
            bindController(localController)

            addModelObject(SceneModelObject())
        }
        enterRoom()

        viewModel.eventData.observe(this) {
            Log.i(TAG, "Dispatch local control event: $it")
            localController.onEvent(it)
        }
    }

    private fun enterRoom() {
        lifecycleScope.launchWhenCreated {
            Service.channelJoin(args.cid, args.mode).apply {
                if (!successful) {
                    ToastUtil.makeShort("加入房间失败 $msg")
                    finish()
                    return@apply
                }
                data?.let {
                    it.pos.run {
                        localController.position = Vector3(get(0), get(1), get(2))
                    }
                    it.memberList.forEach {
                        groupMemberManager.rtmModeChoose(
                            it.mode,
                            it.uid.toIntOrNull() ?: 0,
                            it.name
                        )
                    }
                }
            }
        }
    }

    private fun leaveRoom() {
        GlobalScope.launch {
            Service.channelLeave(args.cid)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveRoom()
        agoraModule.destroyAgora()
        agoraMessageModule.release()
        glRender.release()
        localController.release()
        characterPropertyProvider?.destroy()
    }

    override fun onResume() {
        super.onResume()
        characterPropertyProvider?.resume()
    }

    override fun onPause() {
        super.onPause()
        characterPropertyProvider?.pause()
    }
}