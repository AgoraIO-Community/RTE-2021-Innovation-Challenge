package io.agora.rte

import android.util.Log
import androidx.annotation.NonNull
import io.agora.rtc.Constants.ERR_OK
import io.agora.rtc.IRtcChannelEventHandler
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcChannel
import io.agora.rtc.models.ChannelMediaOptions
import io.agora.rte.listener.RteChannelEventListener
import io.agora.rte.listener.RteStatisticsReportListener
import io.agora.rtm.*
import io.agora.rtm.RtmStatusCode.JoinChannelError.JOIN_CHANNEL_ERR_ALREADY_JOINED

internal class RteChannelImpl(
        channelId: String,
        private var eventListener: RteChannelEventListener?
) : IRteChannel {

    private val TAG = RteChannelImpl::class.java.simpleName
    internal var statisticsReportListener: RteStatisticsReportListener? = null
    private val rtmChannelListener = object : RtmChannelListener {
        override fun onAttributesUpdated(p0: MutableList<RtmChannelAttribute>?) {

        }

        /**收到频道内消息(包括频道内的群聊消息和各种房间配置、人员信息、流信息等)*/
        override fun onMessageReceived(p0: RtmMessage?, p1: RtmChannelMember?) {
            Log.e(TAG, "接收到频道${p1?.channelId}内的消息->${p0?.text}")
            eventListener?.onChannelMsgReceived(p0, p1)
        }

        override fun onMemberJoined(p0: RtmChannelMember?) {

        }

        override fun onMemberLeft(p0: RtmChannelMember?) {

        }

        override fun onMemberCountUpdated(p0: Int) {

        }

        override fun onFileMessageReceived(p0: RtmFileMessage?, p1: RtmChannelMember?) {
        }

        override fun onImageMessageReceived(p0: RtmImageMessage?, p1: RtmChannelMember?) {
        }
    }

    private val rtcChannelEventHandler = object : IRtcChannelEventHandler() {
        override fun onChannelError(rtcChannel: RtcChannel?, err: Int) {
            super.onChannelError(rtcChannel, err)
            Log.e("RteChannelImpl", "onChannelError->" + rtcChannel?.channelId() + ",err->" + err)
        }

        override fun onChannelWarning(rtcChannel: RtcChannel?, warn: Int) {
            super.onChannelWarning(rtcChannel, warn)
            Log.e("RteChannelImpl", "onChannelWarning->" + rtcChannel?.channelId() + ",warn->" + warn)
        }

        override fun onNetworkQuality(rtcChannel: RtcChannel?, uid: Int, txQuality: Int, rxQuality: Int) {
            super.onNetworkQuality(rtcChannel, uid, txQuality, rxQuality)
            eventListener?.onNetworkQuality(uid, txQuality, rxQuality)
        }

        override fun onClientRoleChanged(rtcChannel: RtcChannel?, oldRole: Int, newRole: Int) {
            super.onClientRoleChanged(rtcChannel, oldRole, newRole)
            Log.e("RteChannelImpl", rtcChannel?.channelId() + "," + oldRole + "," + newRole)
        }

        override fun onJoinChannelSuccess(rtcChannel: RtcChannel?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(rtcChannel, uid, elapsed)
            Log.e("RteChannelImpl", String.format("onJoinChannelSuccess channel $rtcChannel uid $uid"))
        }

        override fun onUserJoined(rtcChannel: RtcChannel?, uid: Int, elapsed: Int) {
            super.onUserJoined(rtcChannel, uid, elapsed)
            Log.e("RteChannelImpl", "onUserJoined->$uid")
        }

        override fun onRemoteVideoStateChanged(rtcChannel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(rtcChannel, uid, state, reason, elapsed)
            Log.e("RteChannelImpl", "onRemoteVideoStateChanged->$uid, state->$state, reason->$reason")
        }

        override fun onRemoteVideoStats(rtcChannel: RtcChannel?, stats: IRtcEngineEventHandler.RemoteVideoStats?) {
            super.onRemoteVideoStats(rtcChannel, stats)
//            Log.e("RteChannelImpl", "onRemoteVideoStats->${rtcChannel?.channelId()}, " +
//                    "receivedBitrate->${stats?.receivedBitrate}")
        }

//        override fun onSubscribeVideoStateChanged(rtcChannel: RtcChannel?, uid: Int, oldState: Int, newState: Int, elapseSinceLastState: Int) {
//            super.onSubscribeVideoStateChanged(rtcChannel, uid, oldState, newState, elapseSinceLastState)
//            Log.e("RteChannelImpl", "onSubscribeVideoStateChanged->$${rtcChannel?.channelId()}, " +
//                    "uid->$uid, oldState->$oldState, newState->$newState")
//        }

        override fun onRtcStats(rtcChannel: RtcChannel?, stats: IRtcEngineEventHandler.RtcStats?) {
            super.onRtcStats(rtcChannel, stats)
            statisticsReportListener?.onRtcStats(rtcChannel, stats)
        }

        override fun onVideoSizeChanged(rtcChannel: RtcChannel?, uid: Int, width: Int, height: Int, rotation: Int) {
            super.onVideoSizeChanged(rtcChannel, uid, width, height, rotation)
            statisticsReportListener?.onVideoSizeChanged(rtcChannel, uid, width, height, rotation)
        }
    }

    private val rtmChannel = RteEngineImpl.rtmClient.createChannel(channelId, rtmChannelListener)
    val rtcChannel: RtcChannel = RteEngineImpl.rtcEngine.createRtcChannel(channelId)

    init {
        rtcChannel.setRtcChannelEventHandler(rtcChannelEventHandler)
    }

    override fun join(rtcOptionalInfo: String, rtcToken: String, rtcUid: Long, mediaOptions: ChannelMediaOptions,
                      @NonNull callback: ResultCallback<Void>) {
        val uid = (rtcUid and 0xffffffffL)
        val rtcCode = rtcChannel.joinChannel(rtcToken, rtcOptionalInfo, uid.toInt(), mediaOptions)
        joinRtmChannel(rtcCode, callback)
    }

    private fun joinRtmChannel(rtcCode: Int, @NonNull callback: ResultCallback<Void>) {
        rtmChannel.join(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                if (rtcCode == ERR_OK) {
                    callback.onSuccess(p0)
                } else {
                    callback.onFailure(ErrorInfo(rtcCode))
                }
            }

            override fun onFailure(p0: ErrorInfo?) {
                if (p0?.errorCode == JOIN_CHANNEL_ERR_ALREADY_JOINED) {
                    callback.onSuccess(Unit as Void)
                } else {
                    callback.onFailure(p0)
                }
            }
        })
    }

    override fun leave() {
        rtmChannel.leave(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                Log.e("RteChannelImpl", "成功离开RTM频道")
            }

            override fun onFailure(p0: ErrorInfo?) {
                Log.e("RteChannelImpl", "离开RTM频道失败:${p0?.errorDescription}")
            }
        })
        rtcChannel.leaveChannel()
        eventListener = null
    }

    override fun release() {
        rtmChannel.release()
        rtcChannel.destroy()
    }
}
