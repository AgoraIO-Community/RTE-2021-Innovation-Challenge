package io.agora.education.impl.board.data.response

internal data class BoardUserRes(
        val userUuid: String,
        val userName: String,
        val role: String,
        val grantPermission: Int
)
