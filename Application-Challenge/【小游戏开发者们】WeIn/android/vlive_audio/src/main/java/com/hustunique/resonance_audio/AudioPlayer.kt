package com.hustunique.resonance_audio

import java.nio.ByteBuffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/16
 */
class AudioPlayer(
    channels: Int = AudioConfig.NUM_CHANNELS,
    samplesPreFrames: Int = AudioConfig.SAMPLES_NUM_PRE_FRAME,
    sampleRate: Int = AudioConfig.SAMPLE_RATE
) {
    companion object {
        init {
            System.loadLibrary("oboe")
            System.loadLibrary("vlive_audio")
        }
    }

    private val handler = nCreateEngine(channels, samplesPreFrames, sampleRate)

    fun startPlay() {
        nStartAudio(handler)
    }

    fun writeData(buffer: ByteBuffer, numFrames: Int) =
        nWriteData(handler, buffer, numFrames)


    fun stopPlay() {
        nStopAudio(handler)
    }

    fun release() {
        nReleaseEngine(handler)
    }

    private external fun nCreateEngine(channels: Int, samplesPreFrames: Int, sampleRate: Int): Long

    private external fun nReleaseEngine(handler: Long)

    private external fun nStartAudio(handler: Long)

    private external fun nStopAudio(handler: Long)

    private external fun nWriteData(handler: Long, buffer: ByteBuffer, numFrames: Int): Int
}