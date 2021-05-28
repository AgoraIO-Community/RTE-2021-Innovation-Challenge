package com.hustunique.vlive.agora

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.filament.*
import com.hustunique.vlive.opengl.ImageReaderRender
import io.agora.rtc.mediaio.IVideoSink
import io.agora.rtc.mediaio.MediaIO
import java.nio.ByteBuffer

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
class AgoraRawVideoConsumer(
) : IVideoSink {
    companion object {
        private const val TAG = "AgoraRawVideoConsumer"
    }

    private val imageReaderRender = ImageReaderRender()
    val handler = Handler(Looper.getMainLooper())

    var filamentEngine: Engine? = null
    var filamentMaterial: MaterialInstance? = null
    private var stream: Stream? = null
    private var texture: Texture? = null

    override fun consumeByteBufferFrame(
        buffer: ByteBuffer?,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        timestamp: Long
    ) {

    }

    override fun consumeByteArrayFrame(
        data: ByteArray?,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        timestamp: Long
    ) {
    }

    override fun consumeTextureFrame(
        textureId: Int,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        timestamp: Long,
        matrix: FloatArray?
    ) {
        if (stream == null) {
            stream = Stream.Builder()
                .width(width)
                .height(height)
                .build(filamentEngine!!)
            texture = Texture.Builder()
                .sampler(Texture.Sampler.SAMPLER_EXTERNAL)
                .format(Texture.InternalFormat.RGB8)
                .build(filamentEngine!!).apply {
                    setExternalStream(filamentEngine!!, stream!!)
                    val sampler = TextureSampler(
                        TextureSampler.MinFilter.LINEAR,
                        TextureSampler.MagFilter.LINEAR,
                        TextureSampler.WrapMode.CLAMP_TO_EDGE
                    )
                    filamentMaterial?.setParameter("videoTexture", this, sampler)
                }
            imageReaderRender.bindTexture(textureId)
        }
        matrix?.let {
            filamentMaterial?.setParameter(
                "textureTransform",
                MaterialInstance.FloatElement.MAT4,
                it,
                0,
                1
            )
        }
        imageReaderRender.refreshImageReader()
    }

    fun updateStream() {
        imageReaderRender.apply {
            if (imageReader.maxImages > 0) {
                imageReader.acquireLatestImage()?.let {
                    stream?.setAcquiredImage(it.hardwareBuffer!!, handler) {
                        it.close()
                    } ?: it.close()
                }
            }
        }
    }

    override fun onInitialize(): Boolean {
        Log.i(TAG, "onInitialize: ")
        imageReaderRender.init()
        return true
    }

    override fun onStart(): Boolean {
        Log.i(TAG, "onStart: ")
        return true
    }

    override fun onStop() {
        Log.i(TAG, "onStop: ")
    }

    override fun onDispose() {
        Log.i(TAG, "onDispose: ")
    }

    override fun getEGLContextHandle(): Long =
        imageReaderRender.getEglContext()?.nativeHandle ?: 0L.apply {
            Log.i(
                TAG,
                "getEGLContextHandle: "
            )
        }

    override fun getBufferType(): Int = MediaIO.BufferType.TEXTURE.intValue().apply {
        Log.i(TAG, "getBufferType: ")
    }

    override fun getPixelFormat(): Int = MediaIO.PixelFormat.TEXTURE_2D.intValue().apply {
        Log.i(TAG, "getPixelFormat: ")
    }
}