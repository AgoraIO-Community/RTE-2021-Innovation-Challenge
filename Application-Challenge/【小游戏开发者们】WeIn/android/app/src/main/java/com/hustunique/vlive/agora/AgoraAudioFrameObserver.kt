package com.hustunique.vlive.agora

import android.util.Log
import io.agora.rtc.IAudioFrameObserver

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/16
 */
class AgoraAudioFrameObserver(
    private val dataCallback: (ByteArray, Int, Int) -> Unit,
    private val getData: (ByteArray, Int) -> Unit
) :
    IAudioFrameObserver {
    companion object {
        private const val TAG = "AgoraAudioFrameObserver"
    }

    override fun onRecordFrame(
        samples: ByteArray?,
        numOfSamples: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int
    ): Boolean = true.apply {
        Log.d(TAG, "onRecordFrame() called")
    }

    override fun onPlaybackFrame(
        samples: ByteArray?,
        numOfSamples: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int
    ): Boolean {
        Log.d(
            TAG,
            "onPlaybackFrame() called with: samples = $samples, numOfSamples = $numOfSamples, bytesPerSample = $bytesPerSample, channels = $channels, samplesPerSec = $samplesPerSec"
        )
        samples?.let {
            getData(it, numOfSamples)
        }
        return true
    }

    override fun onPlaybackFrameBeforeMixing(
        samples: ByteArray?,
        numOfSamples: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        uid: Int
    ): Boolean {
        Log.d(
            TAG,
            "onPlaybackFrameBeforeMixing() called with: samples = $samples, numOfSamples = $numOfSamples, bytesPerSample = $bytesPerSample, channels = $channels, samplesPerSec = $samplesPerSec, uid = $uid"
        )
        if (uid == 8086) {
            return true
        }
        samples?.let {
            dataCallback(it, numOfSamples, uid)
        }
        return true
    }

    override fun onMixedFrame(
        samples: ByteArray?,
        numOfSamples: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int
    ): Boolean = false.apply {
        Log.d(TAG, "onMixedFrame() called")
    }

    override fun isMultipleChannelFrameWanted(): Boolean = false

    override fun onPlaybackFrameBeforeMixingEx(
        samples: ByteArray?,
        numOfSamples: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        uid: Int,
        channelId: String?
    ): Boolean = false
}