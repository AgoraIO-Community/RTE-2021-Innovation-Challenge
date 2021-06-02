package io.agora.rte.listener

import io.agora.rtm.RtmMessage

interface RteEngineEventListener {

    /**RTE连接质量发生改变*/
    fun onConnectionStateChanged(p0: Int, p1: Int)

    /**收到点对点消息 peerMsg*/
    fun onPeerMsgReceived(p0: RtmMessage?, p1: String?)
}
