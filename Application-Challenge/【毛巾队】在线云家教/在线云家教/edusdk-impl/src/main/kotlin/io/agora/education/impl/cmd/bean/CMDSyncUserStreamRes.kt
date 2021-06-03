package io.agora.education.impl.cmd.bean

import io.agora.education.api.room.data.Property

class CMDSyncUserStreamRes(
        val step: Int,
        val isFinished: Int,
        val count: Int,
        val total: Int,
        val nextId: String,
        val nextTs: Long,
        val list: MutableList<CMDSyncUserRes>?
) {
}


/**更新流数据，必定更新用户数据*/
class CMDSyncUserRes(
        val userName: String,
        val userUuid: String,
        val role: String,
        val muteChat: Int,
        val userProperties: Map<String, Any>,
        val updateTime: Long,
        /**标识此用户是新下线用户还是新上线用户(ValidState)*/
        val state: Int,
        /**这些流的所有者是Uuid代表的用户(因为用户可能有多路流，所以用集合)*/
        val streams: MutableList<CMDSyncStreamRes>?
) {
}


class CMDSyncStreamRes(
        val streamUuid: String,
        val streamName: String,
        val videoSourceType: Int,
        val audioSourceType: Int,
        val videoState: Int,
        val audioState: Int,
        val updateTime: Long,
        /**标识此流是新下线还是新上线(ValidState)*/
        val state: Int
) {

}