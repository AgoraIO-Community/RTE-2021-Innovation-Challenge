package io.agora.education.api.message

import io.agora.education.api.user.data.EduBaseUserInfo

enum class EduActionType(val value: Int) {
    EduActionTypeApply(0),
    EduActionTypeInvitation(1),
    EduActionTypeAccept(2),
    EduActionTypeReject(3)
}

class EduActionMessage(
        val processUuid: String,
        val action: EduActionType,
        val fromUser: EduBaseUserInfo,
        val timeout: Long,
        var payload: Map<String, Any>?) {
}