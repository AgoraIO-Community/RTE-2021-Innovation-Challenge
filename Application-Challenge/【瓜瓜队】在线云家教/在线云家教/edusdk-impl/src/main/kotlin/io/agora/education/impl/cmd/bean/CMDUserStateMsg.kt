package io.agora.education.impl.cmd.bean

import io.agora.education.impl.room.data.response.EduUserRes

class CMDUserStateMsg(
        val userUuid: String,
        val userName: String,
        val role: String,
        val muteChat: Int,
        val updateTime: Long,
        val operator: EduUserRes
) {

}