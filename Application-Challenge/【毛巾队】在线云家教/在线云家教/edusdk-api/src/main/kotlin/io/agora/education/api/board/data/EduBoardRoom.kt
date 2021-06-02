package io.agora.education.api.board.data

data class EduBoardRoom(
        val boardFollow: Boolean,
        val boardOperators: List<EduBoardOperator>
)
