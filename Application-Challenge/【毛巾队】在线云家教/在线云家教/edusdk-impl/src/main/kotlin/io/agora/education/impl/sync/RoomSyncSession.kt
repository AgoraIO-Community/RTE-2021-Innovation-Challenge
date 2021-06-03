package io.agora.education.impl.sync

import io.agora.education.api.EduCallback
import io.agora.education.api.room.data.EduRoomInfo
import io.agora.education.api.room.data.EduRoomState
import io.agora.education.api.room.data.EduRoomStatus
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.user.EduUser
import io.agora.education.api.user.data.EduLocalUserInfo
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.impl.cmd.bean.CMDResponseBody
import java.util.*

internal open abstract class RoomSyncSession(val roomInfo: EduRoomInfo, val roomStatus: EduRoomStatus) {

    lateinit var localUser: EduUser

    /**本地缓存的人流数据*/
    val eduUserInfoList = Collections.synchronizedList(mutableListOf<EduUserInfo>())
    val eduStreamInfoList = Collections.synchronizedList(mutableListOf<EduStreamInfo>())

    abstract fun updateSequenceId(cmdResponseBody: CMDResponseBody<Any>): Pair<Int, Int>?

    abstract fun fetchLostSequence(callback: EduCallback<Unit>)

    abstract fun fetchLostSequence(nextId: Int, count: Int?, callback: EduCallback<Unit>)

    abstract fun fetchSnapshot(callback: EduCallback<Unit>)
}