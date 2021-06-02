package io.agora.education.impl.room.data.response

import io.agora.education.api.room.data.EduRoomInfo
import io.agora.education.api.room.data.Property
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.impl.user.data.request.RoleMuteConfig

/**userUuid: 房间内用户唯一id，同时也是用户加入rtm的uid
 * userToken: 用户token*/
class EduEntryRes(val room: EduEntryRoomRes, val user: EduEntryUserRes) {
}

class EduEntryRoomRes(val roomInfo: EduRoomInfo, val roomState: EduEntryRoomStateRes,
                      val roomProperties: MutableMap<String, Any>?) {

}

class EduEntryRoomStateRes(val state: Int, val startTime: Long,
                           val muteChat: RoleMuteConfig?, val muteVideo: RoleMuteConfig?,
                           val muteAudio: RoleMuteConfig?) {

}

class EduEntryUserRes(val userUuid: String, val userName: String, val role: String,
                      val streamUuid: String, val userToken: String, val rtmToken: String,
                      val rtcToken: String, val muteChat: Int, val userProperties: MutableMap<String, Any>,
                      val streams: MutableList<EduEntryStreamRes>?, updateTime: Long, state: Int) {
}

class EduEntryStreamRes(
        var streamUuid: String,
        var streamName: String,
        var videoSourceType: Int,
        var audioSourceType: Int,
        var videoState: Int,
        var audioState: Int,
        var updateTime: Long,
        var rtcToken: String
) {
}