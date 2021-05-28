package com.hustunique.vlive.filament

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.hustunique.vlive.filament.model_object.FilamentBaseModelObject
import java.nio.ByteBuffer
import java.nio.channels.Channels

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/2/21
 */
class FilamentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), LifecycleObserver, Choreographer.FrameCallback {

    companion object {
        private const val TAG = "FilamentView"
    }

    var filamentContext: FilamentContext? = null
        //FilamentContext(this)
        set(value) {
            field?.release()
            field = value
            if (value != null) {
                initContext()
            }
        }

    private val modelObjectList = mutableListOf<FilamentBaseModelObject>()

    private var controller: FilamentLocalController? = null

    private val choreographer = Choreographer.getInstance()

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        initContext()
    }

    private fun initContext() {
        filamentContext?.apply {
            val ibl = "default_env"
            setIndirectLight(readCompressedAsset("envs/$ibl/${ibl}_ibl.ktx"))
            setSkyBox(readCompressedAsset("envs/$ibl/${ibl}_skybox.ktx"))

            materialHolder.loadVideoMaterial(readUncompressedAsset("materials/lit.filamat"))
        }
    }

    override fun doFrame(frameTimeNanos: Long) {
        choreographer.postFrameCallbackDelayed(this, 32)
        filamentContext?.let { controller?.update(it.camera) }
        modelObjectList.forEach { it.update(frameTimeNanos) }

        filamentContext?.render(frameTimeNanos)
    }

    fun bindController(filamentCameraController: FilamentLocalController) {
        this.controller = filamentCameraController
    }

    fun addModelObject(obj: FilamentBaseModelObject) {
        obj.run {
            filamentContext?.let {
                bindToContext(it)
                asset = it.loadGlb(readCompressedAsset(resourcePath))
            }
        }
        modelObjectList.add(obj)
    }

    fun removeModelObject(obj: FilamentBaseModelObject) {
        modelObjectList.remove(obj)
        filamentContext?.removeObj(obj)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            controller?.onTouchEvent(it)
        }
        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        choreographer.postFrameCallback(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        choreographer.removeFrameCallback(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.i(TAG, "onDestroy: ")
        controller?.release()
        choreographer.removeFrameCallback(this)
        modelObjectList.forEach { it.destroy() }
        filamentContext?.release()
    }


    private fun readUncompressedAsset(@Suppress("SameParameterValue") assetName: String): ByteBuffer =
        context.assets.openFd(assetName).use { fd ->
            val input = fd.createInputStream()
            val dst = ByteBuffer.allocate(fd.length.toInt())

            val src = Channels.newChannel(input)
            src.read(dst)
            src.close()

            return dst.apply { rewind() }
        }


    private fun readCompressedAsset(assetName: String): ByteBuffer =
        context.assets.open(assetName).use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.wrap(bytes)
        }
}