package com.hustunique.vlive.agora

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.hustunique.resonance_audio.AudioConfig
import com.hustunique.vlive.R
import com.hustunique.vlive.util.UserInfoManager
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.RtcEngineConfig
import io.agora.rtc.mediaio.IVideoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/27/21
 */
class AgoraModule(
    private val activity: ComponentActivity,
    private val mode: Int,
    private val onUserJoinedAction: (Int) -> Unit = {},
    private val onUserLeaveAction: (Int) -> Unit = {}
) {

    companion object {
        private const val TAG = "AgoraModule"
    }

    private var mRtcEngine: RtcEngine? = null

    var audioModule: AudioModule? = null
        private set

    private val audioFrameObserver = AgoraAudioFrameObserver({ array, numSamples, uid ->
        audioModule?.feedData(uid, numSamples, array)
    }, { array, numSamples ->
        audioModule?.getData(array, numSamples)
    })

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.i(TAG, "onUserJoined() called with: uid = $uid, elapsed = $elapsed")
            activity.lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.Main) {
                    onUserJoinedAction(uid)
                }
                if (audioModule == null) {
                    audioModule = AudioModule().apply {
                        init()
                    }
                }
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            activity.lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.Main) {
                    onUserLeaveAction(uid)
                }
            }
        }

        override fun onWarning(warn: Int) {
            super.onWarning(warn)
            Log.w(TAG, "onWarning: $warn")
        }

        override fun onError(err: Int) {
            super.onError(err)
            Log.e(TAG, "onError: $err")
        }


        override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
            super.onFirstLocalVideoFrame(width, height, elapsed)
            Log.i(
                TAG,
                "onFirstLocalVideoFrame() called with: width = $width, height = $height, elapsed = $elapsed"
            )
        }

        override fun onFirstLocalVideoFramePublished(elapsed: Int) {
            super.onFirstLocalVideoFramePublished(elapsed)
            Log.i(TAG, "onFirstLocalVideoFramePublished() called with: elapsed = $elapsed")

        }

        override fun onLocalVideoStat(sentBitrate: Int, sentFrameRate: Int) {
            super.onLocalVideoStat(sentBitrate, sentFrameRate)
            Log.i(
                TAG,
                "onLocalVideoStat() called with: sentBitrate = $sentBitrate, sentFrameRate = $sentFrameRate"
            )
        }

        override fun onLocalVideoStateChanged(localVideoState: Int, error: Int) {
            super.onLocalVideoStateChanged(localVideoState, error)
            Log.i(
                TAG,
                "onLocalVideoStateChanged() called with: localVideoState = $localVideoState, error = $error"
            )
        }
    }


    fun initAgora(videoSource: IVideoSource? = null) {
        initializeAgoraEngine()
        setupLocalVideo()
//        mRtcEngine?.setLocalVideoRenderer(rawVideoConsumer)
//        mRtcEngine?.setVideoSource(videoSource)

        setAudioObserver()
    }

    private fun setAudioObserver() {
        mRtcEngine?.run {
            setAudioProfile(
                Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO,
                Constants.AUDIO_SCENARIO_GAME_STREAMING
            )
            registerAudioFrameObserver(audioFrameObserver)
            setPlaybackAudioFrameParameters(
                AudioConfig.SAMPLE_RATE,
                AudioConfig.NUM_CHANNELS,
                Constants.RAW_AUDIO_FRAME_OP_MODE_WRITE_ONLY,
                960
            )
        }
    }

    fun setRemoteVideoRender(uid: Int, videoConsumer: AgoraRawVideoConsumer) {
        mRtcEngine?.setRemoteVideoRenderer(uid, videoConsumer)
    }


    private fun setupLocalVideo() {
        mRtcEngine?.apply {
            enableVideo()
            enableLocalVideo(mode == 0)
        }
    }

    fun joinChannel(channelId: String) {
        mRtcEngine?.joinChannel(
            null,
            channelId,
            "Extra Optional Data",
            UserInfoManager.uid.toIntOrNull() ?: 0
        )
    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun initializeAgoraEngine() {
        try {
//            val logConfig = LogConfig()
//            logConfig.level = Constants.LogLevel.getValue(Constants.LogLevel.LOG_LEVEL_INFO)
//            val ts: String = SimpleDateFormat("yyyyMMdd").format(Date())
//            logConfig.filePath = "/sdcard/$ts.log"
//            logConfig.fileSize = 2048 * 10

            val config = RtcEngineConfig()
            config.mAppId = activity.getString(R.string.agora_app_id)
            config.mEventHandler = mRtcEventHandler
            config.mContext = activity.applicationContext
//            config.mLogConfig = logConfig
            mRtcEngine =
                RtcEngine.create(config)
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))

            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                    e
                )
            )
        }
    }

    fun destroyAgora() {
        audioModule?.release()
        audioModule = null
        leaveChannel()
        RtcEngine.destroy()
    }

}