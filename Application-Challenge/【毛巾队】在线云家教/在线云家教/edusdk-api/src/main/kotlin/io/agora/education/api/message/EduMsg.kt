package io.agora.education.api.message

import io.agora.education.api.user.data.EduUserInfo

open class EduMsg(
        val fromUser: EduUserInfo,
        var message: String
)
