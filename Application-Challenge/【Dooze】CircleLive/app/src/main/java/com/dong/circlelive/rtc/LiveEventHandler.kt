package com.dong.circlelive.rtc

import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.IRtcEngineEventHandler.LocalVideoStats
import io.agora.rtc.IRtcEngineEventHandler.RtcStats
import io.agora.rtc.RtcChannel
import io.agora.rtc.models.UserInfo

interface LiveEventHandler {

    fun onLeaveChannel(channel: RtcChannel?, stats: RtcStats?)

    fun onJoinChannelSuccess(channel: RtcChannel?, uid: Int, elapsed: Int)

    fun onUserOffline(channel: RtcChannel?, uid: Int, reason: Int)

    fun onUserJoined(channel: RtcChannel?, uid: Int, elapsed: Int)

    fun onLocalVideoStats(channel: RtcChannel?, stats: LocalVideoStats?)

    fun onLocalAudioStats(audioStats: IRtcEngineEventHandler.LocalAudioStats?)

    fun onRemoteVideoStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteVideoStats?)

    fun onRemoteAudioStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteAudioStats)

    fun onRemoteVideoStateChanged(channel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int)

    fun onUserInfoUpdated(uid: Int, userInfo: UserInfo)

    fun onNetworkQuality(channel: RtcChannel?, uid: Int, txQuality: Int, rxQuality: Int)

    fun onRequestToken(channel: RtcChannel?)

    fun onActiveSpeaker(uid: Int)

    fun onAudioVolumeIndication(speakers: Array<out IRtcEngineEventHandler.AudioVolumeInfo>, totalVolume: Int)

    fun onLocalAudioStateChanged(state: Int, error: Int)

    fun onRemoteAudioStateChanged(rtcChannel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int)

    fun onLocalVideoStateChanged(localVideoState: Int, error: Int)

    fun onConnectionLost(rtcChannel: RtcChannel?)

    fun onConnectionStateChanged(channel: RtcChannel, state: Int, reason: Int)

}


interface LivingRoomHandler {

    fun onJoinSuccess(rtcChannel: RtcChannel?)

    fun onRequestToken(channel: RtcChannel?)

    fun onLocalVideoStateChanged(localVideoState: Int, error: Int)

    fun renderRemoteUser(channel: RtcChannel?, uid: Int, userId: String)

    fun removeRemoteRender(channel: RtcChannel?, uid: Int, userId: String)
}