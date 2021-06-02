package io.agora.rte

import android.content.Context
import android.util.Log
import io.agora.rtc.Constants
import io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
import io.agora.rtc.Constants.ERR_OK
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import io.agora.rte.data.RteAudioReverbPreset
import io.agora.rte.data.RteAudioVoiceChanger
import io.agora.rte.listener.*
import io.agora.rtm.*
import io.agora.rtm.RtmStatusCode.LoginError.LOGIN_ERR_ALREADY_LOGIN
import java.io.File

object RteEngineImpl : IRteEngine {
    internal lateinit var rtmClient: RtmClient
    internal lateinit var rtcEngine: RtcEngine
    private val channelMap = mutableMapOf<String, IRteChannel>()

    var eventListener: RteEngineEventListener? = null
    var mediaDeviceListener: RteMediaDeviceListener? = null
    var audioMixingListener: RteAudioMixingListener? = null
    var speakerReportListener: RteSpeakerReportListener? = null

    /**rtm登录成功的标志*/
    var rtmLoginSuccess = false

    private val rtmClientListener = object : RtmClientListener {
        override fun onTokenExpired() {
        }

        override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
        }

        /**RTE连接质量发生改变*/
        override fun onConnectionStateChanged(p0: Int, p1: Int) {
            eventListener?.onConnectionStateChanged(p0, p1)
        }

        /**收到私聊消息 peerMsg*/
        override fun onMessageReceived(p0: RtmMessage?, p1: String?) {
            eventListener?.onPeerMsgReceived(p0, p1)
        }

        override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        }

        override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        }

        override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {
        }

        override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {
        }
    }

    private val rtcEngineEventHandler = object : IRtcEngineEventHandler() {

        override fun onError(err: Int) {
            Log.e("RteEngineImpl", String.format("onError code %d message %s", err, RtcEngine.getErrorDescription(err)))
        }

        override fun onWarning(warn: Int) {
            super.onWarning(warn)
            Log.e("RteEngineImpl", String.format("onWarning code %d message %s", warn, RtcEngine.getErrorDescription(warn)));
        }

        override fun onClientRoleChanged(oldRole: Int, newRole: Int) {
            super.onClientRoleChanged(oldRole, newRole)
            Log.e("RteEngineImpl", "onClientRoleChanged, $oldRole, $newRole")
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            Log.e("RteEngineImpl", String.format("onJoinChannelSuccess channel %s uid %d", channel, uid))
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            Log.e("RteEngineImpl", "onUserJoined->$uid")
        }

//        override fun onSubscribeVideoStateChanged(channel: String?, uid: Int, oldState: Int, newState: Int, elapseSinceLastState: Int) {
//            super.onSubscribeVideoStateChanged(channel, uid, oldState, newState, elapseSinceLastState)
//            Log.e("RteEngineImpl", "onSubscribeVideoStateChanged->$uid, oldState->$oldState, newState->$newState")
//        }

        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)
            Log.e("RteEngineImpl", "onRemoteVideoStateChanged->$uid, state->$state, reason->$reason")
        }

        override fun onAudioRouteChanged(routing: Int) {
            super.onAudioRouteChanged(routing)
            mediaDeviceListener?.onAudioRouteChanged(routing)
        }

        override fun onAudioMixingFinished() {
            super.onAudioMixingFinished()
            audioMixingListener?.onAudioMixingFinished()
        }

        override fun onAudioMixingStateChanged(state: Int, errorCode: Int) {
            super.onAudioMixingStateChanged(state, errorCode)
            audioMixingListener?.onAudioMixingStateChanged(state, errorCode)
        }

        override fun onAudioVolumeIndication(speakers: Array<out AudioVolumeInfo>?, totalVolume: Int) {
            super.onAudioVolumeIndication(speakers, totalVolume)
            speakers?.let {
                /*本地用户独享的音量提示回调返回的 speakers 数组中： uid 为 0， volume 等于 totalVolume*/
                if (speakers?.size == 1 && speakers[0].uid == 0) {
                    speakerReportListener?.onAudioVolumeIndicationOfLocalSpeaker(speakers, totalVolume)
                } else {
                    speakerReportListener?.onAudioVolumeIndicationOfRemoteSpeaker(speakers, totalVolume)
                }
            }
        }
    }

    override fun init(context: Context, appId: String, logFileDir: String) {
        var path = logFileDir.plus(File.separatorChar).plus("agorartm.log")
        rtmClient = RtmClient.createInstance(context, appId, rtmClientListener)
        var code = rtmClient.setLogFile(path)
        path = logFileDir.plus(File.separatorChar).plus("agorasdk.log")
        rtcEngine = RtcEngine.create(context, appId, rtcEngineEventHandler)
        rtcEngine.setChannelProfile(CHANNEL_PROFILE_LIVE_BROADCASTING)
        code = rtcEngine.setLogFile(path)
        Log.e("RteEngineImpl", "init")
//        rtcEngine.setParameters("{\"rtc.log_filter\": 65535}")
        rtcEngine.setParameters("{\"che.video.h264ProfileNegotiated\": 66}")
        rtcEngine.setParameters("{\"che.video.web_h264_interop_enable\": true}")
    }

    override fun loginRtm(rtmUid: String, rtmToken: String, callback: RteCallback<Unit>) {
        /**rtm不能重复登录*/
//        if (!rtmLoginSuccess) {
        rtmClient.login(rtmToken, rtmUid, object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                rtmLoginSuccess = true
                callback.onSuccess(Unit)
            }

            override fun onFailure(p0: ErrorInfo?) {
                rtmLoginSuccess = false
                p0?.let {
                    if (p0.errorCode == LOGIN_ERR_ALREADY_LOGIN) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onFailure(p0.errorCode, p0.errorDescription)
                    }
                }
            }
        })
//        } else {
//            callback.onSuccess(Unit)
//        }
    }

    override fun logoutRtm() {
        rtmClient.logout(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                rtmLoginSuccess = false
//                rtmClient.release()
//                RtcEngine.destroy()
                Log.e("RteEngineImpl", "成功退出RTM")
            }

            override fun onFailure(p0: ErrorInfo?) {
                Log.e("RteEngineImpl", "退出RTM失败:${p0?.errorDescription}")
                if (p0?.errorCode == RtmStatusCode.LeaveChannelError.LEAVE_CHANNEL_ERR_USER_NOT_LOGGED_IN) {
                    rtmLoginSuccess = false
                }
            }
        })
    }

    override fun createChannel(channelId: String, eventListener: RteChannelEventListener): IRteChannel {
        val rteChannel = RteChannelImpl(channelId, eventListener)
        channelMap[channelId] = rteChannel
        return rteChannel
    }

    override fun enableLocalMedia(audio: Boolean, video: Boolean) {
        rtcEngine.enableLocalAudio(audio)
        rtcEngine.enableLocalVideo(video)
    }

    operator fun get(channelId: String): IRteChannel? {
        return channelMap[channelId]
    }

    override fun setClientRole(channelId: String, role: Int): Int {
        if (channelMap.isNotEmpty()) {
            val code = (channelMap[channelId] as RteChannelImpl).rtcChannel.setClientRole(role)
            if (code == 0) {
                Log.e("RteEngineImpl", "成功设置角色为:$role")
            }
            return code
        }
        return -1
    }

    override fun publish(channelId: String): Int {
        if (channelMap.isNotEmpty()) {
            return (channelMap[channelId] as RteChannelImpl).rtcChannel.publish()
        }
        return -1
    }

    override fun unpublish(channelId: String): Int {
        if (channelMap.isNotEmpty()) {
            return (channelMap[channelId] as RteChannelImpl).rtcChannel.unpublish()
        }
        return -1
    }

    override fun updateLocalStream(hasAudio: Boolean, hasVideo: Boolean) {
        rtcEngine.enableLocalAudio(hasAudio)
        rtcEngine.enableLocalVideo(hasVideo)
        rtcEngine.muteLocalAudioStream(!hasAudio)
        rtcEngine.muteLocalVideoStream(!hasVideo)
    }

    override fun muteRemoteStream(channelId: String, uid: Int, muteAudio: Boolean, muteVideo: Boolean): Int {
        if (channelMap.isNotEmpty()) {
            val channel = (channelMap[channelId] as RteChannelImpl).rtcChannel
            val code0 = channel.muteRemoteAudioStream(uid, muteAudio)
            val code1 = channel.muteRemoteVideoStream(uid, muteVideo)
            return if (code0 == ERR_OK && code1 == ERR_OK) ERR_OK else -1
        }
        return -1
    }

    override fun muteLocalStream(muteAudio: Boolean, muteVideo: Boolean): Int {
        val code0 = rtcEngine.muteLocalAudioStream(muteAudio)
        val code1 = rtcEngine.muteLocalVideoStream(muteVideo)
        return if (code0 == ERR_OK && code1 == ERR_OK) ERR_OK else -1
    }

    override fun setVideoEncoderConfiguration(config: VideoEncoderConfiguration): Int {
        return rtcEngine.setVideoEncoderConfiguration(config)
    }

    override fun enableVideo(): Int {
        return rtcEngine.enableVideo()
    }

    override fun enableAudio(): Int {
        return rtcEngine.enableAudio()
    }

    override fun switchCamera(): Int {
        return rtcEngine.switchCamera()
    }

    override fun setupLocalVideo(local: VideoCanvas): Int {
        return rtcEngine.setupLocalVideo(local)
    }

    override fun setupRemoteVideo(local: VideoCanvas): Int {
        return rtcEngine.setupRemoteVideo(local)
    }

    override fun startAudioMixing(filePath: String, loopback: Boolean, replace: Boolean, cycle: Int): Int {
        return rtcEngine.startAudioMixing(filePath, loopback, replace, cycle)
    }

    override fun setAudioMixingPosition(pos: Int): Int {
        return rtcEngine.setAudioMixingPosition(pos)
    }

    override fun pauseAudioMixing(): Int {
        return rtcEngine.pauseAudioMixing()
    }

    override fun resumeAudioMixing(): Int {
        return rtcEngine.resumeAudioMixing()
    }

    override fun stopAudioMixing(): Int {
        return rtcEngine.stopAudioMixing()
    }

    override fun getAudioMixingDuration(): Int {
        return rtcEngine.audioMixingDuration
    }

    override fun getAudioMixingCurrentPosition(): Int {
        return rtcEngine.audioMixingCurrentPosition
    }

    override fun setLocalVoiceChanger(voiceManager: RteAudioVoiceChanger): Int {
        return rtcEngine.setLocalVoiceChanger(voiceManager.value)
    }

    override fun setLocalVoiceReverbPreset(preset: RteAudioReverbPreset): Int {
        return rtcEngine.setLocalVoiceReverbPreset(preset.value)
    }

    override fun enableInEarMonitoring(enabled: Boolean): Int {
        return rtcEngine.enableInEarMonitoring(enabled)
    }

    override fun enableAudioVolumeIndication(interval: Int, smooth: Int, report_vad: Boolean) {
        rtcEngine.enableAudioVolumeIndication(interval, smooth, report_vad)
    }

    override fun setStatisticsReportListener(channelId: String, listener: RteStatisticsReportListener): Int {
        if (channelMap.isNotEmpty()) {
            val channel = channelMap[channelId] as RteChannelImpl
            channel.statisticsReportListener = listener
            return 0
        }
        return -1
    }

    override fun getError(code: Int): String {
        return RtcEngine.getErrorDescription(code)
    }

    override fun ok(): Int {
        return Constants.ERR_OK
    }
}
