package com.hustunique.vlive.agora

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.hustunique.vlive.R
import com.hustunique.vlive.databinding.ActivityAgoraBinding
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.RtcEngineConfig
import io.agora.rtc.video.VideoCanvas
import java.text.SimpleDateFormat
import java.util.*

class AgoraActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AgoraActivity"

    }

    private val binding by lazy {
        ActivityAgoraBinding.inflate(layoutInflater)
    }


    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {


        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initAgora()
    }

    private fun initAgora() {
        initializeAgoraEngine()
        setupLocalVideo()
        joinChannel()
    }

    private fun setupLocalVideo() {
        val surface = RtcEngine.CreateRendererView(this).apply {
            setZOrderMediaOverlay(true)
        }
        binding.localVideoViewContainer.addView(surface)
        mRtcEngine?.apply {
            enableVideo()
            setupLocalVideo(VideoCanvas(surface, VideoCanvas.RENDER_MODE_FIT, 0))
        }
    }

    private fun joinChannel() {
        mRtcEngine?.joinChannel(null, "hi", "Extra Optional Data", 0)
    }

    private fun setupRemoteVideo(uid: Int) {
        runOnUiThread {
            if (binding.remoteVideoViewContainer.childCount > 0) {
                return@runOnUiThread
            }
            val surface = RtcEngine.CreateRendererView(this)
            binding.remoteVideoViewContainer.addView(surface)
            mRtcEngine?.setupRemoteVideo(VideoCanvas(surface, VideoCanvas.RENDER_MODE_FIT, uid))
        }
    }

    private fun onRemoteUserLeft() {

    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun initializeAgoraEngine() {
        try {
            val logConfig = RtcEngineConfig.LogConfig()
            logConfig.level = Constants.LogLevel.getValue(Constants.LogLevel.LOG_LEVEL_INFO)
            val ts: String = SimpleDateFormat("yyyyMMdd").format(Date())
            logConfig.filePath = "/sdcard/$ts.log"
            logConfig.fileSize = 2048 * 10

            val config = RtcEngineConfig()
            config.mAppId = getString(R.string.agora_app_id)
            config.mEventHandler = mRtcEventHandler
            config.mContext = applicationContext
            config.mLogConfig = logConfig
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

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
    }
}