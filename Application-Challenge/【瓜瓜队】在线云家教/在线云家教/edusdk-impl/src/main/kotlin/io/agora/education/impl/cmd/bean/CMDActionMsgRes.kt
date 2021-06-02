package io.agora.education.impl.cmd.bean

import io.agora.education.api.user.data.EduBaseUserInfo
import io.agora.education.impl.room.data.response.EduSnapshotRoomInfoRes

internal class CMDActionMsgRes(
        val processUuid: String,
        val action: Int,
        val timeout: Long,
        val fromUser: EduBaseUserInfo,
        val fromRoom: EduSnapshotRoomInfoRes,
        val payload: Map<String, Any>?
) {
}