package io.agora.rte.listener

import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmMessage

interface RteChannelEventListener {

    /**网络质量发生改变*/
    fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int)

    /**收到频道内消息(包括频道内的聊天消息和各种房间配置、人员信息、流信息等)*/
    fun onChannelMsgReceived(p0: RtmMessage?, p1: RtmChannelMember?)
}