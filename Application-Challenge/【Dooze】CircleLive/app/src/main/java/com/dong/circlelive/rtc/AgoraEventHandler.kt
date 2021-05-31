package com.dong.circlelive.rtc

import com.dong.circlelive.Live
import com.dong.circlelive.base.Timber
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.models.UserInfo
import java.util.*

class AgoraEventHandler : IRtcEngineEventHandler() {
    private val handlers =
        ArrayList<LiveEventHandler>()

    fun addHandler(handlerLive: LiveEventHandler) {
        if (handlers.contains(handlerLive)) return
        handlers.add(handlerLive)
    }

    fun removeHandler(handlerLive: LiveEventHandler) {
        handlers.remove(handlerLive)
    }

    override fun onLocalVideoStats(stats: LocalVideoStats) {
        handlers.forEach {
            it.onLocalVideoStats(null, stats)
        }
    }

    override fun onLocalAudioStats(audioStats: LocalAudioStats?) {
        super.onLocalAudioStats(audioStats)
        handlers.forEach { it.onLocalAudioStats(audioStats) }
    }

    /**
    视频发布状态改变回调。

    自从
    v3.1.0。
    发布状态包括：

    PUB_STATE_IDLE(0)
    PUB_STATE_NO_PUBLISHED(1)
    PUB_STATE_PUBLISHING(2)
    PUB_STATE_PUBLISHED(3)
    参数
    channel	频道名。
    oldState	之前的发布状态。
    newState	当前的发布状态。
    elapseSinceLastState	两次状态变化时间间隔（毫秒）。
     */
    override fun onVideoPublishStateChanged(channel: String?, oldState: Int, newState: Int, elapseSinceLastState: Int) {
        super.onVideoPublishStateChanged(channel, oldState, newState, elapseSinceLastState)
        Timber.d(Live.TAG) { "onVideoPublishStateChanged channel = $channel old = $oldState  new = $newState" }
    }

    override fun onFirstLocalVideoFramePublished(elapsed: Int) {
        super.onFirstLocalVideoFramePublished(elapsed)
        Timber.v(Live.TAG) { "onFirstLocalVideoFramePublished elapsed = $elapsed" }
    }

    override fun onVideoSizeChanged(uid: Int, width: Int, height: Int, rotation: Int) {
        super.onVideoSizeChanged(uid, width, height, rotation)
        Timber.d(Live.TAG) { "onVideoSizeChanged uid = $uid ($width * $height) rotate = $rotation" }
    }

    /**
    本地视频状态发生改变回调。

    自从
    v2.4.1。
    本地视频的状态发生改变时，SDK 会触发该回调返回当前的本地视频状态；当状态为 LOCAL_VIDEO_STREAM_STATE_FAILED(3) 时，你可以在 error 参数中查看返回的错误信息。 该接口在本地视频出现故障时，方便你了解当前视频的状态以及出现故障的原因，方便排查问题。

    参数
    localVideoState	当前的本地视频状态：
    LOCAL_VIDEO_STREAM_STATE_STOPPED(0)：本地视频默认初始状态
    LOCAL_VIDEO_STREAM_STATE_CAPTURING(1)：本地视频采集设备启动成功
    LOCAL_VIDEO_STREAM_STATE_ENCODING(2)：本地视频首帧编码成功
    LOCAL_VIDEO_STREAM_STATE_FAILED(3)：本地视频启动失败
    error	本地视频出错原因：
    LOCAL_VIDEO_STREAM_ERROR_OK(0)：本地视频状态正常
    LOCAL_VIDEO_STREAM_ERROR_FAILURE(1)：出错原因不明确
    LOCAL_VIDEO_STREAM_ERROR_DEVICE_NO_PERMISSION(2)：没有权限启动本地视频采集设备
    LOCAL_VIDEO_STREAM_ERROR_DEVICE_BUSY(3)：本地视频采集设备正在使用中
    LOCAL_VIDEO_STREAM_ERROR_CAPTURE_FAILURE(4)：本地视频采集失败，建议检查采集设备是否正常工作
    Android 9 及以上版本，app 切后台一段时间后，系统收回相机权限
    Android 6 及以上版本，如果相机被第三方应用占用，且未被及时释放。如果一段时间后，相机被释放，则 SDK 会再次出发该回调，并报告 state 为 CAPTURING，error 为 ERROR_OK.
    LOCAL_VIDEO_STREAM_ERROR_ENCODE_FAILURE(5)：本地视频编码失败
     */
    override fun onLocalVideoStateChanged(localVideoState: Int, error: Int) {
        super.onLocalVideoStateChanged(localVideoState, error)
        Timber.d(Live.TAG) { "onLocalVideoStateChanged state = $localVideoState  error = $error  handlers = ${handlers.size}" }
        handlers.forEach { it.onLocalVideoStateChanged(localVideoState, error) }
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, esplase: Int) {
        Timber.d(Live.TAG) { "onRemoteVideoStateChanged o uid = $uid state = $state  reason = $reason" }
    }

    override fun onFirstRemoteVideoFrame(uid: Int, width: Int, height: Int, esplase: Int) {
        Timber.d(Live.TAG) { "onFirstRemoteVideoFrame uid = $uid  $width * $height" }
    }

    /**
     * https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
     */
    override fun onApiCallExecuted(error: Int, api: String?, result: String?) {
        if (error != 0) {
            Timber.e(tag = Live.TAG) { "agora onApiCallExecuted error = $error api = $api $result" }
        }
    }

    // https://docs.agora.io/cn/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_warn_code.html
    override fun onWarning(warning: Int) {
        super.onWarning(warning)
        Timber.w(Live.TAG) { "agora waring code = $warning ${RtcEngine.getErrorDescription(warning)}" }
    }

    override fun onError(error: Int) {
        // https://docs.agora.io/cn/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
        Timber.e(tag = Live.TAG) { "agora onError code = $error ${RtcEngine.getErrorDescription(error)}" }
    }

    override fun onTokenPrivilegeWillExpire(channel: String?) {
        Timber.w(tag = Live.TAG) { "onTokenPrivilegeWillExpire channel = $channel" }
    }

    override fun onRequestToken() {
        Timber.w(tag = Live.TAG) { "onRequestToken" }
    }

    /**
     * state	当前的网络连接状态：
    CONNECTION_STATE_DISCONNECTED(1)：网络连接断开
    CONNECTION_STATE_CONNECTING(2)：建立网络连接中
    CONNECTION_STATE_CONNECTED(3)：网络已连接
    CONNECTION_STATE_RECONNECTING(4)：重新建立网络连接中
    CONNECTION_STATE_FAILED(5)：网络连接失败

    reason	引起当前网络连接状态发生改变的原因：
    CONNECTION_CHANGED_CONNECTING(0)：建立网络连接中
    CONNECTION_CHANGED_JOIN_SUCCESS(1)：成功加入频道
    CONNECTION_CHANGED_INTERRUPTED(2)：网络连接中断
    CONNECTION_CHANGED_BANNED_BY_SERVER(3)：网络连接被服务器禁止
    CONNECTION_CHANGED_JOIN_FAILED(4)：加入频道失败
    CONNECTION_CHANGED_LEAVE_CHANNEL(5)：离开频道
    CONNECTION_CHANGED_INVALID_APP_ID(6)：不是有效的 APP ID。请更换有效的 APP ID 重新加入频道
    CONNECTION_CHANGED_INVALID_CHANNEL_NAME(7)：不是有效的频道名。请更换有效的频道名重新加入频道
    CONNECTION_CHANGED_INVALID_TOKEN(8)：生成的 Token 无效。一般有以下原因：
    在控制台上启用了 App Certificate，但加入频道未使用 Token。当启用了 App Certificate，必须使用 Token
    在调用 joinChannel 加入频道时指定的 uid 与生成 Token 时传入的 uid 不一致
    CONNECTION_CHANGED_TOKEN_EXPIRED(9)：当前使用的 Token 过期，不再有效，需要重新在你的服务端申请生成 Token
    CONNECTION_CHANGED_REJECTED_BY_SERVER(10)：此用户被服务器禁止
    CONNECTION_CHANGED_SETTING_PROXY_SERVER(11)：由于设置了代理服务器，SDK 尝试重连
    CONNECTION_CHANGED_RENEW_TOKEN(12)：更新 Token 引起网络连接状态改变
    CONNECTION_CHANGED_CLIENT_IP_ADDRESS_CHANGED(13)：客户端 IP 地址变更，可能是由于网络类型，或网络运营商的 IP 或端口发生改变引起
    CONNECTION_CHANGED_KEEP_ALIVE_TIMEOUT(14)：SDK 和服务器连接保活超时，进入自动重连状态
     */
    override fun onConnectionStateChanged(state: Int, reason: Int) {
        Timber.i(tag = Live.TAG) { "agora onConnectionStateChanged state = $state reason = $reason  handlers = ${handlers.size}" }
    }

    override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo?) {
        super.onUserInfoUpdated(uid, userInfo)
        userInfo ?: return
        handlers.forEach {
            it.onUserInfoUpdated(uid, userInfo)
        }
    }

    override fun onActiveSpeaker(uid: Int) {
        super.onActiveSpeaker(uid)
        handlers.forEach {
            it.onActiveSpeaker(uid)
        }
    }

    /**
    本地音频状态发生改变回调。

    自从
    v2.9.0。
    本地音频的状态发生改变时（包括本地麦克风录制状态和音频编码状态），SDK 会触发该回调报告当前的本地音频状态。 在本地音频出现故障时，该回调可以帮助你了解当前音频的状态以及出现故障的原因，方便你排查问题。

    注解
    当状态为 LOCAL_AUDIO_STREAM_STATE_FAILED(3) 时，你可以在 error 参数中查看返回的错误信息。
    参数
    state	当前的本地音频状态：
    LOCAL_AUDIO_STREAM_STATE_STOPPED(0)：本地音频默认初始状态
    LOCAL_AUDIO_STREAM_STATE_CAPTURING(1)：本地音频录制设备启动成功
    LOCAL_AUDIO_STREAM_STATE_ENCODING(2)：本地音频首帧编码成功
    LOCAL_AUDIO_STREAM_STATE_FAILED(3)：本地音频启动失败
    error	本地音频出错原因：
    LOCAL_AUDIO_STREAM_ERROR_OK(0)：本地音频状态正常
    LOCAL_AUDIO_STREAM_ERROR_FAILURE(1)：本地音频出错原因不明确
    LOCAL_AUDIO_STREAM_ERROR_DEVICE_NO_PERMISSION(2)：没有权限启动本地音频录制设备
    LOCAL_AUDIO_STREAM_ERROR_DEVICE_BUSY(3)：本地音频录制设备已经在使用中
    LOCAL_AUDIO_STREAM_ERROR_CAPTURE_FAILURE(4)：本地音频录制失败，建议你检查录制设备是否正常工作
    LOCAL_AUDIO_STREAM_ERROR_ENCODE_FAILURE(5)：本地音频编码失败
     */
    override fun onLocalAudioStateChanged(state: Int, error: Int) {
        super.onLocalAudioStateChanged(state, error)
        Timber.v(Live.TAG) { "onLocalAudioStateChanged state = $state error = $error  handlers = ${handlers.size}" }
        handlers.forEach { it.onLocalAudioStateChanged(state, error) }
    }

    /**
    远端音频状态发生改变回调。

    自从
    v2.9.0。
    远端用户（通信场景）或主播（直播场景）音频状态发生改变时，SDK 会触发该回调向本地用户报告当前的远端音频流状态。

    注解
    当频道内的用户（通信场景）或主播（直播场景）的人数超过 17 时，该回调可能不准确。
    参数
    uid	发生音频状态改变的远端用户 ID
    state	远端音频流状态:
    REMOTE_AUDIO_STATE_STOPPED(0)：远端音频流默认初始状态。 在 REMOTE_AUDIO_REASON_LOCAL_MUTED(3)、REMOTE_AUDIO_REASON_REMOTE_MUTED(5) 或 REMOTE_AUDIO_REASON_REMOTE_OFFLINE(7) 的情况下，会报告该状态。
    REMOTE_AUDIO_STATE_STARTING(1)：本地用户已接收远端音频首包
    REMOTE_AUDIO_STATE_DECODING(2)：远端音频流正在解码，正常播放。在 REMOTE_AUDIO_REASON_NETWORK_RECOVERY(2)、 REMOTE_AUDIO_REASON_LOCAL_UNMUTED(4) 或 REMOTE_AUDIO_REASON_REMOTE_UNMUTED(6) 的情况下，会报告该状态
    REMOTE_AUDIO_STATE_FROZEN(3)：远端音频流卡顿。在 REMOTE_AUDIO_REASON_NETWORK_CONGESTION(1) 的情况下，会报告该状态
    REMOTE_AUDIO_STATE_FAILED(4)：远端音频流播放失败。在 REMOTE_AUDIO_REASON_INTERNAL(0) 的情况下，会报告该状态
    reason	远端音频流状态改变的具体原因：
    REMOTE_AUDIO_REASON_INTERNAL(0)：内部原因
    REMOTE_AUDIO_REASON_NETWORK_CONGESTION(1)：网络阻塞
    REMOTE_AUDIO_REASON_NETWORK_RECOVERY(2)：网络恢复正常
    REMOTE_AUDIO_REASON_LOCAL_MUTED(3)：本地用户停止接收远端音频流或本地用户禁用音频模块
    REMOTE_AUDIO_REASON_LOCAL_UNMUTED(4)：本地用户恢复接收远端音频流或本地用户启用音频模块
    REMOTE_AUDIO_REASON_REMOTE_MUTED(5)：远端用户停止发送音频流或远端用户禁用音频模块
    REMOTE_AUDIO_REASON_REMOTE_UNMUTED(6)：远端用户恢复发送音频流或远端用户启用音频模块
    REMOTE_AUDIO_REASON_REMOTE_OFFLINE(7)：远端用户离开频道
    elapsed	从本地用户调用 joinChannel 方法到发生本事件经历的时间，单位为 ms
     */
    override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
        Timber.v(Live.TAG) { "onRemoteAudioStateChanged uid = $uid state = $state reason = $reason  handlers = ${handlers.size}" }
        handlers.forEach { it.onRemoteAudioStateChanged(null, uid, state, reason, elapsed) }
    }

    /**
     * 提示频道内谁正在说话、说话者音量及本地用户是否在说话的回调。

    该回调提示频道内瞬时音量最高的几个用户（最多三个）的用户 ID、他们的音量及本地用户是否在说话。

    该回调默认禁用。可以通过启用说话者音量提示 enableAudioVolumeIndication 方法开启； 开启后，无论频道内是否有人说话，都会按方法中设置的时间间隔返回提示音量。

    每次触发，用户会收到两个独立的 onAudioVolumeIndication 回调，其中一个包含本地用户的音量信息，另一个包含远端所有用户的音量信息，详见下方参数描述。

    注解
    如需使用该回调 speakers 数组中的 vad 参数（即本地人声检测功能），请在 enableAudioVolumeIndication 方法中设置 report_vad 为 true。
    用户调用 muteLocalAudioStream 方法会对 SDK 行为产生影响：
    本地用户调用该方法后 SDK 即不再返回本地用户的音量提示回调。
    远端用户调用该方法后 20 秒，远端说话者的音量提示回调将不再包含该用户；如果所有远端用户调用该方法后 20 秒，SDK 即不再返回远端说话者的音量提示回调。
    参数
    speakers	每个说话者的用户 ID 和音量信息的数组，详见 AudioVolumeInfo。
    在本地用户的回调中，此数组中包含以下成员：
    uid = 0，表示本地用户；
    volume，等于 totalVolume，报告本地用户混音后的音量；
    vad，报告本地用户人声状态。
    channelId，表示当前说话者在哪个频道。
    在远端用户的回调中，此数组中包含以下成员：
    uid，表示每位说话者的用户 ID。
    volume，表示各说话者混音后的音量。
    vad 为 0，该参数对远端用户无效。
    channelId，表示当前说话者在哪个频道。 如果报告的 speakers 数组为空，则表示远端此时没有人说话。
    totalVolume	混音后的总音量（0~255）。
    在本地用户的回调中，totalVolume 为本地用户混音后的音量。
    在远端用户的回调中，totalVolume 为所有说话者混音后的总音量。
     */
    override fun onAudioVolumeIndication(speakers: Array<out AudioVolumeInfo>?, totalVolume: Int) {
        super.onAudioVolumeIndication(speakers, totalVolume)
        speakers ?: return
        handlers.forEach {
            it.onAudioVolumeIndication(speakers, totalVolume)
        }
        //Timber.v(Live.TAG_PARTY) { "onAudioVolumeIndication ${speakers.joinToString { it.toLogString() }}" }
    }

}

fun IRtcEngineEventHandler.AudioVolumeInfo.toLogString(): String {
    return "room = $channelId uid = $uid volume = $volume vad = $vad"
}
