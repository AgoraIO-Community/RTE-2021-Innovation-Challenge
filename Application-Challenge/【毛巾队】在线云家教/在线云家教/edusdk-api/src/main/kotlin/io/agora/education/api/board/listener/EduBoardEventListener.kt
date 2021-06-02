package io.agora.education.api.board.listener

import io.agora.education.api.user.data.EduUserInfo

interface EduBoardEventListener {
    fun onFollowMode(enable: Boolean)

    fun onPermissionGranted(student: EduUserInfo)

    fun onPermissionRevoked(student: EduUserInfo)
}
