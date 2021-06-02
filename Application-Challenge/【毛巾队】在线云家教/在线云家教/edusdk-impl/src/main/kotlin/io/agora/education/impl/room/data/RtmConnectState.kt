package io.agora.education.impl.room.data

import io.agora.rtm.RtmStatusCode

class RtmConnectState(
        /**最近一次的RTM连接状态*/
        internal var lastConnectionState: Int = RtmStatusCode.ConnectionState.CONNECTION_STATE_DISCONNECTED
) {
    fun isReconnecting(): Boolean {
        return lastConnectionState == RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING
    }

    fun isConnected(): Boolean {
        return lastConnectionState == RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTED
    }
}