package io.agora.education.api.user

import io.agora.education.api.user.listener.EduStudentEventListener

interface EduStudent : EduUser {
    fun setEventListener(eventListener: EduStudentEventListener)
}
