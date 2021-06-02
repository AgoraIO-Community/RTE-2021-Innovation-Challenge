package io.agora.education.impl.board.data.response

internal data class BoardInfoRes(
        val boardId: String,
        val boardToken: String
)

internal data class BoardStatusRes(
        val followMode: Int
)

internal data class BoardRoomRes(
        val boardInfo: BoardInfoRes,
        val boardState: BoardStatusRes
)
