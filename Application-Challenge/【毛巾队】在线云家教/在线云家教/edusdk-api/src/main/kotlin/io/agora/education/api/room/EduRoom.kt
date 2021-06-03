package io.agora.education.api.room

import io.agora.education.api.EduCallback
import io.agora.education.api.board.EduBoard
import io.agora.education.api.record.EduRecord
import io.agora.education.api.room.data.EduRoomInfo
import io.agora.education.api.room.data.EduRoomStatus
import io.agora.education.api.room.data.RoomJoinOptions
import io.agora.education.api.room.listener.EduRoomEventListener
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.user.EduStudent
import io.agora.education.api.user.EduTeacher
import io.agora.education.api.user.EduUser
import io.agora.education.api.user.data.EduUserInfo

abstract class EduRoom(roomInfo: EduRoomInfo, roomStatus: EduRoomStatus) {

    companion object {
        fun create(roomInfo: EduRoomInfo, roomStatus: EduRoomStatus): EduRoom {
            val cla = Class.forName("io.agora.education.impl.room.EduRoomImpl")
            return cla.getConstructor(EduRoomInfo::class.java, EduRoomStatus::class.java)
                    .newInstance(roomInfo, roomStatus) as EduRoom
        }
    }

    var roomProperties: MutableMap<String, Any> = mutableMapOf()

    lateinit var board: EduBoard
    lateinit var record: EduRecord

    var eventListener: EduRoomEventListener? = null

    abstract fun joinClassroom(options: RoomJoinOptions, callback: EduCallback<EduStudent>)

    abstract fun getLocalUser(): EduUser

    abstract fun getRoomInfo(): EduRoomInfo

    abstract fun getRoomStatus(): EduRoomStatus

    abstract fun getStudentCount(): Int

    abstract fun getTeacherCount(): Int

    abstract fun getStudentList(): MutableList<EduUserInfo>

    abstract fun getTeacherList(): MutableList<EduUserInfo>

    abstract fun getFullStreamList(): MutableList<EduStreamInfo>

    abstract fun getFullUserList(): MutableList<EduUserInfo>

    abstract fun clearData()

    abstract fun leave()

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is EduRoom) {
            return false
        }
        return other.getRoomInfo().roomUuid.equals(this.getRoomInfo().roomUuid)
    }
}
