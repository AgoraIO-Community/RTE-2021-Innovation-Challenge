package io.agora.education.api.user

import io.agora.education.api.EduCallback
import io.agora.education.api.room.data.Property
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.stream.data.ScreenStreamInitOptions
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.listener.EduTeacherEventListener

interface EduTeacher : EduUser {
    fun setEventListener(eventListener: EduTeacherEventListener)

    fun beginClass(callback: EduCallback<Unit>)

    fun endClass(callback: EduCallback<Unit>)

    fun allowStudentChat(isAllow: Boolean, callback: EduCallback<Unit>)

    fun allowRemoteStudentChat(isAllow: Boolean, remoteStudent: EduUserInfo, callback: EduCallback<Unit>)

    fun startShareScreen(options: ScreenStreamInitOptions, callback: EduCallback<EduStreamInfo>)

    fun stopShareScreen(callback: EduCallback<Unit>)

    fun remoteStartStudentCamera(remoteStream: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopStudentCamera(remoteStream: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStartStudentMicrophone(remoteStream: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopStudentMicrophone(remoteStream: EduStreamInfo, callback: EduCallback<Unit>)
}
