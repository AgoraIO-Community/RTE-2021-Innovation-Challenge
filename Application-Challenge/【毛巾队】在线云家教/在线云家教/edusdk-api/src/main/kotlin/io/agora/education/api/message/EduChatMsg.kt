package io.agora.education.api.message

import io.agora.education.api.user.data.EduUserInfo

open class EduChatMsg(
        fromUser: EduUserInfo,
        message: String,
        val type: Int
) : EduMsg(fromUser, message)

enum class EduChatMsgType(var value: Int) {
    Text(1)
}
