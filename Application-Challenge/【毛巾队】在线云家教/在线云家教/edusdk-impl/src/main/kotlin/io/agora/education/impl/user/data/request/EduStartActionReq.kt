package io.agora.education.impl.user.data.request

class EduStartActionReq(
        val action: Int,
        val toUserUuid: String,
        val fromUserUuid: String,
        val timeout: Long,
        var payload: Map<String, Any>?
) {
}