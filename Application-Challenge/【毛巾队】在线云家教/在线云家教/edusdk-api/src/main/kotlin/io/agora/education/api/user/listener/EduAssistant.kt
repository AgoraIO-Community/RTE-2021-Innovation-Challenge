package io.agora.education.api.user.listener

import io.agora.education.api.EduCallback
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.user.EduUser
import io.agora.education.api.user.listener.EduStudentEventListener

interface EduAssistant : EduUser {
    fun setEventListener(eventListener: EduStudentEventListener)

    fun remoteStartStudentCamera(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopStudentCamera(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStartTeacherCamera(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopTeacherCamera(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStartStudentMicrophone(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopStudentMicrophone(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStartTeacherMicrophone(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)

    fun remoteStopTeacherMicrophone(eduStreamInfo: EduStreamInfo, callback: EduCallback<Unit>)
}
