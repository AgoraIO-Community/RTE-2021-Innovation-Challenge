package com.hustunique.vlive.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hustunique.vlive.data.Vector3
import com.hustunique.vlive.local.MemberInfo

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/25
 */
class SceneViewModel : ViewModel() {

    val eventData = MutableLiveData<BaseEvent>()

    val memberInfo = MutableLiveData<List<MemberInfo>>()

}

open class BaseEvent()

data class RockerEvent(
    val radians: Float,
    val progress: Float,
    val roll: Float,
) : BaseEvent()

data class ModeSwitchEvent(
    val rockerMode: Boolean
) : BaseEvent()

class ResetEvent() : BaseEvent()

data class FlyEvent(
    val pos: Vector3
) : BaseEvent()