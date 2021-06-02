package io.agora.education.api.room.data

open class EduRoomInfo(
        /**同一个appId下的房间唯一标示，同时也是rtc、rtm中的channelName*/
        var roomUuid: String,
        var roomName: String
) {
    companion object {
        fun create(roomType: Int, roomUuid: String, roomName: String): EduRoomInfo {
            val cla = Class.forName("io.agora.education.impl.room.data.EduRoomInfoImpl")
            return cla.getConstructor(Int::class.java, String::class.java, String::class.java)
                    .newInstance(roomType, roomUuid, roomName) as EduRoomInfo
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is EduRoomInfo) {
            return false
        }
        return (other.roomUuid == this.roomUuid && other.roomName == this.roomName)
    }
}
