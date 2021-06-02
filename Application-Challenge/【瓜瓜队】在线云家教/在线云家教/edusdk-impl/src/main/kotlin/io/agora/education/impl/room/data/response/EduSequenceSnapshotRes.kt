package io.agora.education.impl.room.data.response

import io.agora.education.impl.cmd.bean.OnlineUserInfo
import io.agora.education.impl.user.data.request.RoleMuteConfig

internal class EduSequenceSnapshotRes(
        val sequence: Int,
        val snapshot: EduSnapshotRes
) {
}

internal class EduSnapshotRes(
        val room: EduSnapshotRoomRes,
        val users: MutableList<OnlineUserInfo>
) {}

internal class EduSnapshotRoomRes(
        val roomInfo: EduSnapshotRoomInfoRes,
        val roomState: EduSnapshotRoomStateRes,
        val roomProperties: MutableMap<String, Any>?
) {}

internal class EduSnapshotRoomInfoRes(
        val roomName: String,
        val roomUuid: String
) {}

internal class EduSnapshotRoomStateRes(
        val state: Int,
        val startTime: Long,
        val muteChat: RoleMuteConfig?,
        val muteVideo: RoleMuteConfig?,
        val muteAudio: RoleMuteConfig?
) {}

