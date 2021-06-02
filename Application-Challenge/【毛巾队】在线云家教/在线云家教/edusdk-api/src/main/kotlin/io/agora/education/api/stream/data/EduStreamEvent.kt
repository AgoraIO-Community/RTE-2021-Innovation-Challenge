package io.agora.education.api.stream.data

import io.agora.education.api.user.data.EduBaseUserInfo
import io.agora.education.api.user.data.EduUserInfo

data class EduStreamEvent(val modifiedStream: EduStreamInfo, val operatorUser: EduBaseUserInfo?) {

    fun copy(): EduStreamEvent
    {
        val eduStreamInfo = modifiedStream.copy()
        val eduStreamEvent = operatorUser?.copy()
        return EduStreamEvent(eduStreamInfo, eduStreamEvent)
    }
}