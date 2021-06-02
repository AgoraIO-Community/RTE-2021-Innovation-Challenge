package io.agora.education.impl.cmd.bean

import io.agora.education.impl.room.data.response.EduBaseStreamRes
import io.agora.education.impl.room.data.response.EduStreamRes
import io.agora.education.impl.room.data.response.EduUserRes
import io.agora.education.impl.user.data.EduUserInfoImpl

/**人员进出时，RTM回调出来的数据结构*/
class RtmUserInOutMsg(val total: Int, val onlineUsers: MutableList<OnlineUserInfo>,
                      val offlineUsers: MutableList<OfflineUserInfo>) {
}

class OnlineUserInfo(userUuid: String, userName: String, role: String,
                     muteChat: Int, updateTime: Long?, state: Int,
                     val streamUuid: String, val streams: MutableList<EduBaseStreamRes>,
                     val userProperties: MutableMap<String, Any>)
    : EduUserRes(userUuid, userName, role, muteChat, updateTime, state)

class OfflineUserInfo(userUuid: String, userName: String, role: String,
                      muteChat: Int, updateTime: Long?, state: Int,
                      val operator: EduUserRes?, val streamUuid: String,
                      val streams: MutableList<EduBaseStreamRes>,
                      val userProperties: MutableMap<String, Any>)
    : EduUserRes(userUuid, userName, role, muteChat, updateTime, state) {

}