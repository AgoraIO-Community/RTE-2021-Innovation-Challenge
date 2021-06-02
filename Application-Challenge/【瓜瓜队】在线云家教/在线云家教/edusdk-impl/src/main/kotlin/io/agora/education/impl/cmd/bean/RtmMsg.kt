package io.agora.education.impl.cmd.bean

import io.agora.education.api.room.data.EduRoomInfo
import io.agora.education.impl.room.data.response.EduFromUserRes

/**rtm传送频道消息时，返回数据的数据结构*/
class RtmMsg(
        val fromUser: EduFromUserRes,
        val message: String,
        val type: Int?
) {
}