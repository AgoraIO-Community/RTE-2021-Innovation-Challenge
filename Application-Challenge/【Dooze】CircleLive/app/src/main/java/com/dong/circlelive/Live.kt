package com.dong.circlelive

import android.content.Context
import android.view.TextureView
import androidx.lifecycle.MutableLiveData
import cn.leancloud.AVUser
import com.dong.circlelive.base.Timber
import com.dong.circlelive.live.LiveToken
import com.dong.circlelive.live.TokenResult
import com.dong.circlelive.rtc.*
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.models.UserInfo
import io.agora.rtc.video.VideoCanvas
import io.agora.rtm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

/**
 * Create by dooze on 5/3/21  7:29 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class Live {

    var rtcEngine: RtcEngine? = null
        private set

    var uidMappingHelper: UidMappingHelper? = null

    var rtmClient: RtmClient? = null
        private set

    var channelManager: ChannelManager? = null
        private set

    var cameraSource: CameraSource? = null

    val liveUserInfoUpdate = MutableLiveData<Pair<Int, UserInfo>>()

    val liveRtmChannels = mutableMapOf<String, LiveRtmChannel>()

    private val renderMap = hashMapOf<String, LocalRender>()

    private fun init(appContext: Context) {
        val rtcEngine = RtcEngine.create(appContext, APP_ID, LiveEventHandler())
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        rtcEngine.setAudioProfile(
            Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY,
            Constants.AudioScenario.getValue(Constants.AudioScenario.GAME_STREAMING)
        )
        this.rtcEngine = rtcEngine

        val rtmClient = RtmClient.createInstance(appContext, APP_ID, object : RtmClientListener {
            override fun onConnectionStateChanged(p0: Int, p1: Int) {
                Timber.d { "trm connect state $p0  -> $p1" }
            }

            override fun onMessageReceived(p0: RtmMessage?, p1: String?) {
                Timber.d { "trm received  $p0  -> $p1" }
            }

            override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {
            }

            override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {
            }

            override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
            }

            override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
            }

            override fun onTokenExpired() {
            }

            override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
            }
        })
        this.rtmClient = rtmClient
        uidMappingHelper = UidMappingHelper()

    }

    fun initCameraSource(forceFrontCamera: Boolean? = null): CameraSource {
        var source = cameraSource
        if (source == null) {
            source = CameraSource(appContext, 1280, 720)
            source.init(forceFrontCamera)
            cameraSource = source
        }
        return source
    }

    fun obtainRemoteRender(userPublicId: String, releasePreRender: Boolean = false): LocalRender {
        val cacheRender = renderMap[userPublicId]
        if (cacheRender?.textureView != null) return cacheRender
        if (releasePreRender) {
            cacheRender?.release()
        }
        val textureView = TextureView(appContext)
        val render = LocalRender(textureView, userPublicId)
        render.init(null)
        render.setBufferType(MediaIO.BufferType.BYTE_ARRAY)
        render.setPixelFormat(MediaIO.PixelFormat.I420)
        renderMap[userPublicId] = render
        return render
    }

    fun setupSourceAndRender(uid: String): Pair<CameraSource, LocalRender> {
        val rtcEngine = requireNotNull(rtcEngine)
        rtcEngine.enableVideo()
        val source = requireNotNull(cameraSource)
        renderMap[uid]?.run {
            onDispose()
            release()
        }
        rtcEngine.setVideoSource(source)
        val textureView = TextureView(appContext)
        textureView.id = textureView.hashCode()
        val render = LocalRender(textureView, uid)
        render.init(source.eglContext)
        render.setBufferType(MediaIO.BufferType.TEXTURE)
        render.setPixelFormat(MediaIO.PixelFormat.TEXTURE_2D)
        rtcEngine.setLocalVideoRenderer(render)
        rtcEngine.setLocalRenderMode(VideoCanvas.RENDER_MODE_HIDDEN, Constants.VIDEO_MIRROR_MODE_DISABLED)

        val isVideoMute = false
        rtcEngine.enableLocalVideo(!isVideoMute)
        rtcEngine.muteLocalVideoStream(isVideoMute)

        val mute = false
        rtcEngine.enableLocalAudio(!mute)
        rtcEngine.muteLocalAudioStream(mute)

        renderMap[uid] = render
        return Pair(source, render)
    }

    fun finishLive() {
        channelManager?.leaveAllChannel()
        cameraSource?.release()
        renderMap.entries.forEach { it.value.release() }
        renderMap.clear()
        cameraSource = null
    }


    suspend fun loginRtm(userId: String) {
        val tokenResult = createRtmToken(userId, userId, 24 * 60 * 60) ?: throw RuntimeException("rtm token error")
        Live.loginRtmImp(tokenResult.result, userId)
    }

    fun initRtm() {
        val client = requireNotNull(rtmClient)
        client.rtmCallManager.setEventListener(object : RtmCallEventListener {
            override fun onLocalInvitationReceivedByPeer(p0: LocalInvitation?) {
                Timber.d(TAG) { "onLocalInvitationReceivedByPeer $p0" }
            }

            override fun onLocalInvitationAccepted(p0: LocalInvitation?, p1: String?) {
                Timber.d(TAG) { "onLocalInvitationAccepted $p0 $p1" }

            }

            override fun onLocalInvitationRefused(p0: LocalInvitation?, p1: String?) {
                Timber.d(TAG) { "onLocalInvitationRefused $p0 $p1" }
            }

            override fun onLocalInvitationCanceled(p0: LocalInvitation?) {
                Timber.d(TAG) { "onLocalInvitationCanceled $p0" }

            }

            override fun onLocalInvitationFailure(p0: LocalInvitation?, p1: Int) {
                Timber.d(TAG) { "onLocalInvitationFailure $p0 $p1" }

            }

            override fun onRemoteInvitationReceived(p0: RemoteInvitation?) {
                Timber.d(TAG) { "onRemoteInvitationReceived $p0" }

            }

            override fun onRemoteInvitationAccepted(p0: RemoteInvitation?) {
                Timber.d(TAG) { "onRemoteInvitationAccepted $p0" }

            }

            override fun onRemoteInvitationRefused(p0: RemoteInvitation?) {
                Timber.d(TAG) { "onRemoteInvitationRefused $p0" }

            }

            override fun onRemoteInvitationCanceled(p0: RemoteInvitation?) {
                Timber.d(TAG) { "onRemoteInvitationCanceled $p0" }

            }

            override fun onRemoteInvitationFailure(p0: RemoteInvitation?, p1: Int) {
                Timber.d(TAG) { "onRemoteInvitationFailure $p0 $p1" }

            }
        })

    }

    suspend fun makeCallInvite(userId: String) = suspendCancellableCoroutine<ErrorInfo?> { ucot ->
        requireNotNull(rtmClient?.rtmCallManager).apply {
            sendLocalInvitation(createLocalInvitation(userId), object : ResultCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    ucot.resume(null, null)
                }

                override fun onFailure(p0: ErrorInfo?) {
                    ucot.resume(p0, null)
                }
            })
        }
    }


    inner class LiveEventHandler : IRtcEngineEventHandler() {

        override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo) {
            super.onUserInfoUpdated(uid, userInfo)
            liveUserInfoUpdate.postValue(Pair(uid, userInfo))
        }
    }

    companion object {
        const val MAX_LIVING_USER = 9
        const val TAG = "DoLive"

        private const val APP_ID = "54a844b9550a430abe6365e7ee1f5d6f"
        lateinit var live: Live

        val rtcEngine: RtcEngine?
            get() = live.rtcEngine

        val channelManager: ChannelManager?
            get() = live.channelManager

        fun init(context: Context) {
            if (::live.isInitialized) return
            live = Live()
            live.init(context)
        }

        fun initUserInfo() {
            val user = AVUser.currentUser()
            rtcEngine?.apply {
                live.channelManager = ChannelManager(user.objectId)
                registerLocalUserAccount(APP_ID, user.objectId)
            }
        }
    }
}

val live: Live
    get() {
        return Live.live
    }


suspend fun Live.Companion.loginRtmImp(token: String, userId: String) = suspendCancellableCoroutine<ErrorInfo?> { ucot ->
    requireNotNull(live.rtmClient).login(token, userId, object : ResultCallback<Void> {
        override fun onSuccess(p0: Void?) {
            Timber.d { "rtm login success" }
            live.initRtm()
            ucot.resume(null, null)
        }

        override fun onFailure(p0: ErrorInfo?) {
            Timber.d { "rtm login error $p0" }
            ucot.resume(p0, null)
        }
    })
}

suspend fun Live.Companion.createRtcToken(channel: String, userId: String, expireSecond: Int = 360000): TokenResult? = withContext(Dispatchers.IO) {
    appContext.retrofit.create(LiveToken::class.java).createRtc(channel, userId, expireSecond).execute().body()
}


suspend fun Live.Companion.createRtmToken(channel: String, userId: String, expireSecond: Int = 360000): TokenResult? = withContext(Dispatchers.IO) {
    appContext.retrofit.create(LiveToken::class.java).createRtm(channel, userId, expireSecond).execute().body()
}