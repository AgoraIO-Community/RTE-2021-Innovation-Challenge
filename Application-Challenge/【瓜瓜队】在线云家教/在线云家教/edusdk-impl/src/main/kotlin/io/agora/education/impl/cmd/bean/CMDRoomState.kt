package io.agora.education.impl.cmd.bean

import io.agora.education.impl.room.data.response.EduUserRes

/**rtm通知room状态发生改变时，返回数据的数据结构*/
class CMDRoomState(
        /**房间状态 1开始 0结束*/
        val state: Int,
        val startTime: Long,
        val operator: EduUserRes
) {
}