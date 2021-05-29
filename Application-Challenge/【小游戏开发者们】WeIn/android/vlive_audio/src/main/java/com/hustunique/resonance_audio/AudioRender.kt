package com.hustunique.resonance_audio

import java.nio.ByteBuffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/16
 */
class AudioRender(
    private val channels: Int = AudioConfig.NUM_CHANNELS,
    samplesPreFrames: Int = AudioConfig.SAMPLES_NUM_PRE_FRAME,
    sampleRate: Int = AudioConfig.SAMPLE_RATE
) {

    companion object {
        init {
            System.loadLibrary("ResonanceAudioShared")
            System.loadLibrary("vlive_audio")
        }
    }

    private val handler = nCreateRenderEngine(channels, samplesPreFrames, sampleRate)

    fun setHeadPos(x: Float, y: Float, z: Float) {
        nSetHeadPosition(handler, x, y, z)
    }

    fun setHeadRotation(x: Float, y: Float, z: Float, w: Float) {
        nSetHeadRotation(handler, x, y, z, w)
    }

    fun getOutput(buffer: ByteBuffer, numFrames: Int) =
        nGetOutputData(handler, buffer, channels, numFrames)


    fun createRenderSource(): Int {
        return nCreateSoundsSource(handler)
    }

    fun feedData(sourceId: Int, byteBuffer: ByteBuffer, numFrames: Int) {
        nSetInputData(handler, sourceId, byteBuffer, channels, numFrames)
    }

    fun feedData(sourceId: Int, byteArray: ByteArray, numFrames: Int) {
        nSetInputDataArray(handler, sourceId, byteArray, channels, numFrames)
    }

    fun setSourcePos(sourceId: Int, x: Float, y: Float, z: Float) {
        nSetSourcePosition(handler, sourceId, x, y, z)
    }

    fun setSourceRotation(sourceId: Int, x: Float, y: Float, z: Float, w: Float) {
        nSetSourceRotation(handler, sourceId, x, y, z, w)
    }

    fun releaseSource(sourceId: Int) {
        nReleaseSoundsSource(handler, sourceId)
    }

    fun release() {
        nReleaseRenderEngine(handler)
    }

    private external fun nCreateRenderEngine(
        numChannels: Int,
        framesPerBuffer: Int,
        sampleRateHZ: Int
    ): Long

    private external fun nReleaseRenderEngine(handler: Long)

    private external fun nCreateSoundsSource(handler: Long): Int

    private external fun nReleaseSoundsSource(handler: Long, sourceId: Int)

    private external fun nSetHeadPosition(
        handler: Long,
        x: Float,
        y: Float,
        z: Float
    )

    private external fun nSetHeadRotation(
        handler: Long,
        x: Float,
        y: Float,
        z: Float,
        w: Float
    )

    private external fun nSetSourcePosition(
        handler: Long,
        sourceId: Int,
        x: Float,
        y: Float,
        z: Float
    )

    private external fun nSetSourceRotation(
        handler: Long,
        sourceId: Int,
        x: Float,
        y: Float,
        z: Float,
        w: Float
    )

    private external fun nGetOutputData(
        handler: Long,
        buffer: ByteBuffer,
        numChannels: Int,
        numFrames: Int
    ): Boolean

    private external fun nSetInputData(
        handler: Long,
        sourceId: Int,
        buffer: ByteBuffer,
        numChannels: Int,
        numFrames: Int
    )

    private external fun nSetInputDataArray(
        handler: Long,
        sourceId: Int,
        array: ByteArray,
        numChannels: Int,
        numFrames: Int
    )

}