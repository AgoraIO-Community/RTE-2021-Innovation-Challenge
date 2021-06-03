package io.agora.education.api.user.data

import io.agora.education.api.message.EduActionType

class EduStartActionConfig(
        val processUuid: String,
        val action: EduActionType,
        val toUser: EduBaseUserInfo,
        val timeout: Long,
        val payload: Map<String, Any>?
) {
}

class EduStopActionConfig(
        val processUuid: String,
        val action: EduActionType,
        var payload: Map<String, Any>?) {
}