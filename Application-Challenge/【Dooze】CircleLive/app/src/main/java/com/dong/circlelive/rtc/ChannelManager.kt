package com.dong.circlelive.rtc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dong.circlelive.Live
import com.dong.circlelive.base.Timber
import io.agora.rtc.Constants
import io.agora.rtc.IRtcChannelEventHandler
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcChannel
import io.agora.rtc.models.ChannelMediaOptions

/**
 * Create by dooze on 2020/7/10  12:05 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ChannelManager(private var uid: String) {

    private val _publishLiveData = MutableLiveData<String?>()
    val publishingChannelData: LiveData<String?> = _publishLiveData

    private val channels = mutableMapOf<String, RtcChannel>()
    private val channelTokenMap = mutableMapOf<String, String>()

    /**
     * 当前正在发送推流的channel，APP只能同时又一个推流发送，否则会出错
     */
    var publishChannel: RtcChannel? = null
        private set(value) {
            field = value
            _publishLiveData.postValue(value?.channelId())
        }


    fun reInitUid(uid: String): Boolean {
        val changed = this.uid != uid
        this.uid = uid
        return changed
    }

    /**
     * 加入一个房间，并自动接收这个房间的音视频流，但是不会发布自己的流
     * @param channelId 房间的唯一标识
     * @param token 房间的鉴权token
     * @param role 用户角色，主播或者观众，目前默认是使用直播模式，如果只主播会默认发布视频
     */
    fun joinChannel(channelId: String, token: String, role: Int, eventHandler: ChannelEventHandler? = null): RtcChannel? {
        Timber.d(tag = TAG) { "joinChannel id = $channelId role = $role  uid = $uid" }
        if (!channels.containsKey(channelId)) {
            val rtcEngine = Live.rtcEngine ?: return null
            val channel = rtcEngine.createRtcChannel(channelId)
            if (role == Constants.CLIENT_ROLE_BROADCASTER) {
                publishChannel?.let { pubChannel ->
                    if (pubChannel.channelId() != channelId) {
                        if (pubChannel.unpublish() != 0) {
                            Timber.w(TAG) { "请确保你想要 unpublish 音视频流的频道 channelId，与当前正在 publish 音视频流的频道 channel ID 一致" }
                        }
                    }
                }
            }
            channelTokenMap[channelId] = token
            channels[channelId] = channel
            channel.setClientRole(role)
            channel.setRtcChannelEventHandler(eventHandler)
            channel.joinChannelWithUserAccount(token, uid, ChannelMediaOptions())
            if (role == Constants.CLIENT_ROLE_BROADCASTER) {
                val code = channel.publish()
                if (code != 0) {
                    throw RuntimeException("can't publish channel = $channelId error = $code")
                } else {
                    publishChannel = channel
                }
            }
            return channel
        } else {
            Timber.i(TAG) { "already join channel = $channelId" }
        }
        channelTokenMap[channelId] = token
        return channels[channelId]
    }

    fun leaveChannel(channelId: String) {
        val channel = channels.remove(channelId)
        invalidChannelToken(channelId)
        if (channel != null) {
            if (channel == publishChannel) {
                unpublish(channelId)
            }
            channel.leaveChannel()
            channel.destroy()
        } else {
            Timber.i(TAG) { "can't leave channel = $channelId because of not join" }
        }
    }


    fun unpublish(channelId: String) {
        if (channelId == publishChannel?.channelId()) {
            if (publishChannel?.unpublish() != 0) {
                Timber.w(TAG) { "请确保你想要 unpublish 音视频流的频道 channelId，与当前正在 publish 音视频流的频道 channel ID 一致" }
            } else {
                publishChannel = null
            }
        } else {
            Timber.i(TAG) { "channel = $channelId not published" }
        }
    }

    fun publishChannel(channelId: String) {
        val channel = channels[channelId]
        if (channel == null) {
            Timber.i(TAG) { "can't publish channel = $channelId because of not join" }
        } else {
            publishChannel?.let { pubChannel ->
                if (pubChannel.channelId() != channelId) {
                    if (pubChannel.unpublish() != 0) {
                        Timber.w(TAG) { "请确保你想要 unpublish 音视频流的频道 channelId，与当前正在 publish 音视频流的频道 channel ID 一致" }
                    }
                }
            }
            val code = channel.publish()
            if (code != 0) {
                throw RuntimeException("can't publish channel = $channelId error = $code")
            } else {
                publishChannel = channel
            }
        }
    }

    fun changeRole(channelId: String, role: Int) {
        if (channelId == publishChannel?.channelId()) {
            publishChannel?.setClientRole(role)
        } else {
            if (role == Constants.CLIENT_ROLE_BROADCASTER) {
                unpublish(channelId)
            }
            val channel = channels[channelId]
            channel?.setClientRole(role)
        }
    }

    fun leaveAllChannel() {
        val iterator = channels.values.iterator()
        while (iterator.hasNext()) {
            val channel = iterator.next()
            val channelId = channel.channelId()
            if (channel == publishChannel) {
                unpublish(channelId)
            }
            invalidChannelToken(channelId)
            channel.leaveChannel()
            channel.destroy()
        }
        channels.clear()
        publishChannel = null
    }

    fun isJoined(channelId: String): Boolean {
        return channels.containsKey(channelId)
    }

    fun isPublishing(channelId: String): Boolean {
        return publishChannel?.channelId() == channelId
    }

    fun isPublishing(): Boolean {
        return publishChannel != null
    }

    fun joinedChannel(): List<RtcChannel> {
        return channels.values.toList()
    }

    fun getChannel(channelId: String): RtcChannel? {
        return channels[channelId]
    }

    fun invalidChannelToken(channelId: String) {
        channelTokenMap.remove(channelId)
    }

    fun getChannelToken(channelId: String): String? = channelTokenMap[channelId]

    companion object {
        const val TAG = "Live_Channel"
    }
}


class ChannelEventHandler(private val liveEventHandler: LiveEventHandler) : IRtcChannelEventHandler() {


    override fun onJoinChannelSuccess(channel: RtcChannel, uid: Int, elapsed: Int) {
        liveEventHandler.onJoinChannelSuccess(channel, uid, elapsed)
    }

    override fun onUserJoined(channel: RtcChannel, uid: Int, elapsed: Int) {
        liveEventHandler.onUserJoined(channel, uid, elapsed)
    }

    override fun onUserOffline(channel: RtcChannel, uid: Int, elapsed: Int) {
        liveEventHandler.onUserOffline(channel, uid, elapsed)
    }

    override fun onLeaveChannel(channel: RtcChannel, stats: IRtcEngineEventHandler.RtcStats?) {
        liveEventHandler.onLeaveChannel(channel, stats)
    }

    override fun onClientRoleChanged(channel: RtcChannel, oldRole: Int, newRole: Int) {
    }


    /**
     * state	State of the remote video:
    REMOTE_VIDEO_STATE_STOPPED(0)：远端视频默认初始状态。在 REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3)、REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5) 或 REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7) 的情况下，会报告该状态。
    REMOTE_VIDEO_STATE_STARTING(1)：本地用户已接收远端视频首包
    REMOTE_VIDEO_STATE_DECODING(2)：远端视频流正在解码，正常播放。在 REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY(2)、REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4)、REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6) 或 REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9) 的情况下，会报告该状态
    REMOTE_VIDEO_STATE_FROZEN(3)：远端视频流卡顿。在 REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1) 或 REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8) 的情况下，会报告该状态
    REMOTE_VIDEO_STATE_FAILED(4)：远端视频流播放失败。在 REMOTE_VIDEO_STATE_REASON_INTERNAL(0) 的情况下，会报告该状态。

    reason	远端视频流状态改变的具体原因：
    REMOTE_VIDEO_STATE_REASON_INTERNAL(0)：内部原因
    REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1)：网络阻塞
    REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY(2)：网络恢复正常
    REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3)：本地用户停止接收远端视频流或本地用户禁用视频模块
    REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4)：本地用户恢复接收远端视频流或本地用户启动视频模块
    REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5)：远端用户停止发送视频流或远端用户禁用视频模块
    REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6)：远端用户恢复发送视频流或远端用户启用视频模块
    REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7)：远端用户离开频道
    REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8)：远端视频流已回退为音频流
    REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9)：回退的远端音频流恢复为视频流
     */
    override fun onRemoteVideoStateChanged(channel: RtcChannel, uid: Int, state: Int, reason: Int, elapsed: Int) {
        Timber.v(Live.TAG) { "onRemoteVideoStateChanged channel = ${channel.channelId()} uid = $uid state = $state reason = $reason " }
        liveEventHandler.onRemoteVideoStateChanged(channel, uid, state, reason, elapsed)
    }

    override fun onRemoteAudioStateChanged(rtcChannel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteAudioStateChanged(rtcChannel, uid, state, reason, elapsed)
        Timber.v(Live.TAG) { "onRemoteAudioStateChanged channel = ${rtcChannel?.channelId()} uid = $uid state = $state reason = $reason " }
        liveEventHandler.onRemoteAudioStateChanged(rtcChannel, uid, state, reason, elapsed)
    }


    override fun onNetworkQuality(channel: RtcChannel, uid: Int, txQuality: Int, rxQuality: Int) {
        liveEventHandler.onNetworkQuality(channel, uid, txQuality, rxQuality)
    }

    override fun onRemoteVideoStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteVideoStats?) {
        liveEventHandler.onRemoteVideoStats(channel, stats)
    }


    override fun onRemoteAudioStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteAudioStats?) {
        stats ?: return
        liveEventHandler.onRemoteAudioStats(channel, stats)

    }

    override fun onTokenPrivilegeWillExpire(channel: RtcChannel, token: String?) {
        Timber.w(tag = Live.TAG) { "onTokenPrivilegeWillExpire channel = $channel" }
    }

    override fun onRequestToken(channel: RtcChannel) {
        Timber.w(tag = Live.TAG) { "onRequestToken channel = ${channel.channelId()}" }
        liveEventHandler.onRequestToken(channel)
    }

    override fun onConnectionStateChanged(channel: RtcChannel, state: Int, reason: Int) {
        Timber.i(tag = Live.TAG) { "agora onConnectionStateChanged channel = ${channel.channelId()} state = $state reason = $reason" }
        liveEventHandler.onConnectionStateChanged(channel, state, reason)
    }


    override fun onConnectionLost(p0: RtcChannel?) {
        super.onConnectionLost(p0)
        Timber.i(Live.TAG) { "onConnectionLost  ${p0?.channelId()}" }
        liveEventHandler.onConnectionLost(p0)
    }

    override fun onChannelError(rtcChannel: RtcChannel?, err: Int) {
        super.onChannelError(rtcChannel, err)
        Timber.e(Live.TAG) { "onChannelError  err = $err" }
    }
}