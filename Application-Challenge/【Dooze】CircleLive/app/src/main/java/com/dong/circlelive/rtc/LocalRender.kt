package com.dong.circlelive.rtc

import android.graphics.SurfaceTexture
import android.view.TextureView
import com.dong.circlelive.base.Timber
import io.agora.rtc.gl.EglBase
import io.agora.rtc.gl.RendererCommon
import io.agora.rtc.mediaio.IVideoSink
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.utils.ThreadUtils
import io.agora.rtc.video.AgoraVideoFrame
import java.nio.ByteBuffer

/**
 * Create by dooze on 2020/7/3  12:04 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LocalRender(
    var textureView: TextureView?,
    val userId: String,
) :
    IVideoSink,
    TextureView.SurfaceTextureListener {
    private val render: VideoRenderer = VideoRenderer(TAG, userId)
    private var eglContext: EglBase.Context? = null
    private var attributes: IntArray? = null
    private var glDrawer: RendererCommon.GlDrawer? = null

    fun init(sharedContext: EglBase.Context?) {
        eglContext = sharedContext
    }

    fun init(sharedContext: EglBase.Context?, configAttributes: IntArray?, drawer: RendererCommon.GlDrawer?) {
        eglContext = sharedContext
        attributes = configAttributes
        glDrawer = drawer
    }

    fun setBufferType(bufferType: MediaIO.BufferType?) {
        bufferType?.let { render.setBufferType(it) }
    }

    fun setPixelFormat(pixelFormat: MediaIO.PixelFormat?) {
        if (pixelFormat != null) {
            render.setPixelFormat(pixelFormat)
        }
    }

    fun setSurfaceNoFrameListener(noSurfaceFrameListener: ((userId: String?, count: Long) -> Unit)?) {
        render.noSurfaceFrameListener = noSurfaceFrameListener
    }

    fun setMirror(mirror: Boolean) {
        render.eglRender.setMirror(mirror)
    }

    // from IVideoRenderer begin
    override fun onInitialize(): Boolean {
        Timber.d(TAG) { "onInitialize($userId) $this" }
        kotlin.runCatching {
            if (attributes != null && glDrawer != null) {
                render.init(eglContext, attributes, glDrawer)
            } else {
                render.init(eglContext)
            }
        }
        return true
    }

    override fun onStart(): Boolean {
        return render.start()
    }

    override fun onStop() {
        render.stop()
    }

    override fun onDispose() {
        setSurfaceNoFrameListener(null)
        render.release()
        Timber.d(TAG) { "render($userId) onDispose  $this" }
    }

    fun release() {
        setSurfaceNoFrameListener(null)
        render.release()
        textureView = null
        Timber.d(TAG) { "render($userId) release  $this" }
    }

    override fun getEGLContextHandle(): Long {
        return render.eGLContextHandle
    }

    override fun getPixelFormat(): Int {
        val format = render.pixelFormat
        require(format != AgoraVideoFrame.FORMAT_NONE) { "Pixel format is not set" }
        return format
    }

    override fun getBufferType(): Int {
        val type = render.bufferType
        require(type != AgoraVideoFrame.BUFFER_TYPE_NONE) { "Buffer type is not set" }
        return type
    }

    override fun consumeTextureFrame(
        texId: Int,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long,
        matrix: FloatArray
    ) {
        render.consume(texId, format, width, height, rotation, ts, matrix)
    }

    override fun consumeByteBufferFrame(
        buffer: ByteBuffer,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long
    ) {
        render.consume(buffer, format, width, height, rotation, ts)
    }

    override fun consumeByteArrayFrame(
        data: ByteArray,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long
    ) {
        render.consume(data, format, width, height, rotation, ts)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    fun isEglReleased(): Boolean {
        return render.eglRender.isEglReleased()
    }

    init {
        render.setRenderView(textureView, this)
        textureView?.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
            ThreadUtils.checkIsOnMainThread()
            render.eglRender.setLayoutAspectRatio((right - left).toFloat() / (bottom - top).toFloat())
        }
    }

    companion object {
        const val TAG = "DCLocalRender"
    }
}