package io.agora.education.impl.cmd.bean

import io.agora.education.api.user.data.EduBaseUserInfo

class CMDUserPropertyRes(
        val fromUser: EduBaseUserInfo,
        action: Int,
        changeProperties: MutableMap<String, Any>,
        cause: MutableMap<String, Any>?
) : CMDRoomPropertyRes(action, changeProperties, cause) {
}