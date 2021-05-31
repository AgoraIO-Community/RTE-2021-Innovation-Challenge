package com.dong.circlelive.live.model

import android.graphics.Bitmap
import androidx.lifecycle.*
import cn.leancloud.AVObject
import cn.leancloud.AVUser
import com.dong.circlelive.*
import com.dong.circlelive.base.Timber
import com.dong.circlelive.gl.BitmapFilterGenerator
import com.dong.circlelive.model.uid
import com.dong.circlelive.rtc.*
import com.dong.circlelive.store.KEY_LIVE_FILTER_INTENSITY
import com.dong.circlelive.store.KEY_LIVE_FILTER_PATH
import com.dong.circlelive.store.KEY_SHOWED_FILTER_TIPS
import com.dong.circlelive.store.store
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcChannel
import io.agora.rtc.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by dooze on 2021/5/25  11:03 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LivingViewModel(val channelId: String) : ViewModel(), LiveEventHandler {


    val liveChannel = MutableLiveData<LiveChannel>()

    private var curChannel: RtcChannel? = null

    private var liveRtmChannel: LiveRtmChannel? = null

    val renderValue = MutableLiveData<LocalRender?>()

    val removeRender = MutableLiveData<Pair<Int, String>?>()

    val lutBitmaps = MutableLiveData<List<LutImage>?>()

    val livingCount = MutableLiveData(1)

    val showFilterTips = MutableLiveData(false)

    fun start(lifecycleOwner: LifecycleOwner) {
        launch {
            val user = AVUser.currentUser()
            val channelManager = Live.channelManager ?: return@launch
            val cameraSource = live.initCameraSource()
            cameraSource.manualStart()

            val tokenResult = Live.createRtcToken(channelId, user.objectId)
            if (tokenResult == null || tokenResult.result.isEmpty()) {
                withContext(Dispatchers.Main) {
                    toast(R.string.live_start_token_error)
                }
                Timber.e { "invalid token $tokenResult" }
                return@launch
            }

            live.setupSourceAndRender(user.uid).let { (source, render) ->
                render.onInitialize()
                render.onStart()
                source.preDisplayRender = render
                renderValue.value = render
            }

            val path = store.decodeString(KEY_LIVE_FILTER_PATH)
            if (!path.isNullOrEmpty()) {
                cameraSource.changeFilter(path)
            }

            cameraSource.changeIntensity(store.decodeFloat(KEY_LIVE_FILTER_INTENSITY, 1f))

            curChannel = channelManager.joinChannel(
                channelId,
                tokenResult.result,
                Constants.CLIENT_ROLE_BROADCASTER,
                ChannelEventHandler(this@LivingViewModel)
            )
            withContext(Dispatchers.IO) {
                liveChannel.postValue(LiveChannel(AVObject.createWithoutData("LiveChannel", channelId).fetch("owner")).also {
                    it.incLivingTimes()
                })

                liveRtmChannel = live.liveRtmChannels[channelId] ?: LiveRtmChannel(channelId, liveChannel.value?.name ?: "").also {
                    if (it.start() == null) {
                        it.sendMessage(LiveMessage.create("${user.username}开始Live", LiveMessageType.LIVING_START.value))
                    }
                }
            }

            if (!store.decodeBool(KEY_SHOWED_FILTER_TIPS)) {
                store.encode(KEY_SHOWED_FILTER_TIPS, true)
                showFilterTips.value = true
            }

        }
        live.liveUserInfoUpdate.observe(lifecycleOwner, Observer { (uid, userInfo) ->
            onUserInfoUpdated(uid, userInfo)
        })
    }

    fun transformFilters(bmp: Bitmap) {
        launch {
            try {
                withContext(Dispatchers.IO) {
                    val filterGenerator = BitmapFilterGenerator(bmp.width, bmp.height)
                    filterGenerator.setupSourceBitmap(bmp)
                    lutBitmaps.postValue(appContext.assets.list("lut")?.map {
                        val path = "lut/$it"
                        filterGenerator.changeLut(path)
                        LutImage(path, filterGenerator.render())
                    } ?: return@withContext)
                    filterGenerator.release()
                }

            } catch (t: Throwable) {
                Timber.e(t) { "transformFilters error" }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        destroy()
    }

    fun destroy() {
        lutBitmaps.value = null
        renderValue.value = null
        if (!subscriberChannels.containsKey(liveRtmChannel?.channelId)) {
            launch {
                liveRtmChannel?.leave()
            }
        }
        live.finishLive()
    }

    override fun onLeaveChannel(channel: RtcChannel?, stats: IRtcEngineEventHandler.RtcStats?) {

    }

    override fun onJoinChannelSuccess(channel: RtcChannel?, uid: Int, elapsed: Int) {
        Timber.d(Live.TAG) { "onJoinChannelSuccess" }
    }

    override fun onUserOffline(channel: RtcChannel?, uid: Int, reason: Int) {

    }

    override fun onUserJoined(channel: RtcChannel?, uid: Int, elapsed: Int) {
        Timber.d(Live.TAG) { "onUserJoined $uid " }
    }

    override fun onLocalVideoStats(channel: RtcChannel?, stats: IRtcEngineEventHandler.LocalVideoStats?) {

    }

    override fun onLocalAudioStats(audioStats: IRtcEngineEventHandler.LocalAudioStats?) {

    }

    override fun onRemoteVideoStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteVideoStats?) {

    }

    override fun onRemoteAudioStats(channel: RtcChannel, stats: IRtcEngineEventHandler.RemoteAudioStats) {

    }

    override fun onRemoteVideoStateChanged(channel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int) {
        Timber.d(Live.TAG) { "onRemoteVideoStateChanged $uid  -> $state" }
        launch {
            when (state) {
                Constants.REMOTE_VIDEO_STATE_DECODING -> {
                    livingCount.postValue((livingCount.value ?: 0) + 1)
                    val userId = live.uidMappingHelper?.toUserId(uid, UidMappingHelper.EVENT_REMOTE_RENDER)
                    if (!userId.isNullOrEmpty()) {
                        val render = live.obtainRemoteRender(userId)
                        if (channel == null) {
                            live.rtcEngine?.setRemoteVideoRenderer(uid, render)
                        } else {
                            channel.setRemoteRenderMode(uid, Constants.RENDER_MODE_HIDDEN, Constants.VIDEO_MIRROR_MODE_DISABLED)
                            channel.setRemoteVideoRenderer(uid, render)
                        }
                        renderValue.value = (render)
                    }
                }
                Constants.REMOTE_VIDEO_STATE_STOPPED -> {
                    livingCount.postValue((livingCount.value ?: 0) - 1)
                    val userId = live.uidMappingHelper?.toUserId(uid, UidMappingHelper.EVENT_REMOTE_STOP_RENDER)
                    if (!userId.isNullOrEmpty()) {
                        removeRender.value = (Pair(uid, userId))
                    }
                }
            }
        }
    }

    override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo) {
        Timber.d(Live.TAG) { "onUserInfoUpdated $uid  -> $userInfo" }
        launch {
            live.uidMappingHelper?.onUserInfoUpdated(uid, userInfo) { event, userId ->
                if (event.tag == UidMappingHelper.EVENT_REMOTE_STOP_RENDER) {
                    removeRender.postValue(Pair(uid, userId))
                } else if (event.tag == UidMappingHelper.EVENT_REMOTE_RENDER) {
                    val render = live.obtainRemoteRender(userId)
                    if (curChannel == null) {
                        live.rtcEngine?.setRemoteVideoRenderer(uid, render)
                    } else {
                        curChannel?.setRemoteRenderMode(uid, Constants.RENDER_MODE_HIDDEN, Constants.VIDEO_MIRROR_MODE_DISABLED)
                        curChannel?.setRemoteVideoRenderer(uid, render)
                    }
                    renderValue.value = (render)
                }
            }
        }
    }

    override fun onNetworkQuality(channel: RtcChannel?, uid: Int, txQuality: Int, rxQuality: Int) {

    }

    override fun onRequestToken(channel: RtcChannel?) {
        launch(Dispatchers.IO) {
            kotlin.runCatching {
                val user = AVUser.currentUser()
                val tokenResult = Live.createRtcToken(channelId, user.objectId)
                if (tokenResult == null || tokenResult.result.isEmpty()) {
                    Timber.e { "invalid token $tokenResult" }
                    return@launch
                }
                curChannel?.renewToken(tokenResult.result)
            }
        }
    }

    override fun onActiveSpeaker(uid: Int) {

    }

    override fun onAudioVolumeIndication(speakers: Array<out IRtcEngineEventHandler.AudioVolumeInfo>, totalVolume: Int) {

    }

    override fun onLocalAudioStateChanged(state: Int, error: Int) {

    }

    override fun onRemoteAudioStateChanged(rtcChannel: RtcChannel?, uid: Int, state: Int, reason: Int, elapsed: Int) {

    }

    override fun onLocalVideoStateChanged(localVideoState: Int, error: Int) {

    }

    override fun onConnectionLost(rtcChannel: RtcChannel?) {

    }

    override fun onConnectionStateChanged(channel: RtcChannel, state: Int, reason: Int) {

    }


}