package com.hustunique.vlive.local

import android.util.Log
import android.util.SparseArray
import com.hustunique.vlive.agora.AgoraModule
import com.hustunique.vlive.filament.model_object.ActorModelObject
import com.hustunique.vlive.filament.model_object.FilamentBaseModelObject
import com.hustunique.vlive.filament.model_object.GreenActorModelObject
import com.hustunique.vlive.filament.model_object.ScreenModelObject
import com.hustunique.vlive.util.ThreadUtil
import com.hustunique.vlive.util.UserInfoManager
import com.hustunique.vlive.util.putIfAbsent

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/16
 */
class GroupMemberManager(
    private val addObj: (FilamentBaseModelObject) -> Unit,
    private val removeObj: (FilamentBaseModelObject) -> Unit
) {

    companion object {
        private const val TAG = "GroupMemberManager"
    }

    var agoraModule: AgoraModule? = null

    var onMemberUpdate: (List<MemberInfo>) -> Unit = {}

    private val memberInfo = SparseArray<MemberInfo>()

    private val memberInfoList = mutableListOf<MemberInfo>()

    private val onPut: (MemberInfo) -> Unit = {
        if (it.uid.toString() != UserInfoManager.uid) {
            memberInfoList.add(it)
            onMemberUpdate(memberInfoList)
        }
    }

    @Synchronized
    fun rtcJoin(uid: Int) {
        memberInfo.putIfAbsent(uid, MemberInfo(uid = uid), onPut)
        memberInfo.get(uid).apply { rtcJoined = true }
    }

    @Synchronized
    fun rtcQuit(uid: Int) {
        memberInfo.get(uid)?.apply {
            rtcJoined = false
            modelObject?.run(removeObj)
            memberInfoList.remove(this)
            onMemberUpdate(memberInfoList)
        }
        Log.i(TAG, "rtcQuit: removeObj $uid")
        memberInfo.remove(uid)
    }

    @Synchronized
    fun rtmModeChoose(mode: Int, uid: Int, uname: String) {
        Log.i(TAG, "rtmModeChoose() called with: video = $mode, uid = $uid")
        memberInfo.putIfAbsent(uid, MemberInfo(uid = uid), onPut)
        memberInfo.get(uid).apply {
            this.mode = mode
            userName = uname
        }
    }

    fun onMatrix(characterProperty: CharacterProperty, uid: Int) {
        memberInfo.get(uid)?.apply {
            ThreadUtil.execUi {
                if (rtcJoined && mode != null && modelObject == null) {
                    Log.i(TAG, "addModelObject: $uid")
                    modelObject =
                        when (mode) {
                            0 -> ScreenModelObject().apply {
                                agoraModule?.setRemoteVideoRender(
                                    uid,
                                    videoConsumer
                                )
                            }
                            1 -> ActorModelObject()
                            else -> GreenActorModelObject()
                        }.also(
                            addObj
                        )
                }
                modelObject?.run {
                    onProperty(characterProperty)
                    val (pos, quat) = getTransform()
                    agoraModule?.audioModule?.run {
                        setSourcePosRotation(uid, pos, quat)
                    }
                }
            }
        }
    }

}

data class MemberInfo(
    var userName: String = "",
    var uid: Int,
    var mode: Int? = null,
    var rtcJoined: Boolean = false,
    var modelObject: FilamentBaseModelObject? = null,
    var onMatrix: (CharacterProperty) -> Unit = {}
)