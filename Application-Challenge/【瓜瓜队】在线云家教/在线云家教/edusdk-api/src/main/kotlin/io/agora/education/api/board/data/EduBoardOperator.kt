package io.agora.education.api.board.data

import io.agora.education.api.user.data.EduUserInfo

data class EduBoardOperator(
        val isPublisher: Boolean,
        val user: EduUserInfo
)
