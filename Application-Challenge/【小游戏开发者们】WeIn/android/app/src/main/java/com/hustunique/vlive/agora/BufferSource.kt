package com.hustunique.vlive.agora

import io.agora.rtc.mediaio.IVideoFrameConsumer
import io.agora.rtc.mediaio.IVideoSource
import io.agora.rtc.mediaio.MediaIO
import java.nio.ByteBuffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/28/21
 */
class BufferSource : IVideoSource {

    private var mConsumer: IVideoFrameConsumer? = null
    private var mStart = false

    override fun onInitialize(consumer: IVideoFrameConsumer?): Boolean {
        mConsumer = consumer
        return true
    }

    override fun onStart(): Boolean {
        mStart = true
        return true
    }

    override fun onStop() {
        mStart = false
    }

    override fun onDispose() {
        mConsumer = null
    }

    fun onBuffered(buffer: ByteBuffer, width: Int, height: Int) {
        if (mStart && mConsumer != null) {
            mConsumer?.consumeByteBufferFrame(
                buffer, MediaIO.PixelFormat.RGBA.intValue(), width, height, 0, System
                    .currentTimeMillis()
            )
        }
    }

    override fun getBufferType(): Int = MediaIO.BufferType.BYTE_BUFFER.intValue()

    override fun getCaptureType(): Int = MediaIO.CaptureType.SCREEN.intValue()

    override fun getContentHint(): Int = MediaIO.ContentHint.DETAIL.intValue()

}