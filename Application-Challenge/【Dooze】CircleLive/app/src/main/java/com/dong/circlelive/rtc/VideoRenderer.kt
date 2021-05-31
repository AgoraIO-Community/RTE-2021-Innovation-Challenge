package com.dong.circlelive.rtc

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import com.dong.circlelive.base.Timber
import io.agora.rtc.gl.*
import io.agora.rtc.gl.VideoFrame.TextureBuffer
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.utils.ThreadUtils
import java.nio.ByteBuffer
import java.util.concurrent.CountDownLatch

class VideoRenderer(
    name: String,
    val id: String,
    var noSurfaceFrameListener: ((userId: String?, count: Long) -> Unit)? = null,
) : SurfaceHolder.Callback, TextureView.SurfaceTextureListener {
    val eglRender: EglRenderer = EglRenderer(name, id)
    private var mBufferType = -1
    private var mPixelFormat = -1
    private var mSurfaceView: SurfaceView? = null
    private var mTextureView: TextureView? = null
    private var mSurface: Surface? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurfaceViewListener: SurfaceHolder.Callback? = null
    private var mSurfaceTextureListener: TextureView.SurfaceTextureListener? = null
    private var mHasEglSurface = false
    private var mStarted = false
    private var errFrameCount = 0L
    private var skipFrameCount = 0L
    private var noSurfaceFrameCount = 0L

    val eGLContextHandle: Long
        get() = eglRender.eglContext.nativeEglContext

    @JvmOverloads
    fun init(
        context: EglBase.Context?,
        var2: IntArray? = EglBase.CONFIG_PLAIN,
        drawer: RendererCommon.GlDrawer? = GlRectDrawer()
    ) {
        eglRender.init(context, var2, drawer)
    }

    fun setRenderView(surfaceView: SurfaceView?, var2: SurfaceHolder.Callback?) {
        ThreadUtils.checkIsOnMainThread()
        check(!mHasEglSurface) { "Only one egl surface allowed" }
        mSurfaceView = surfaceView
        mSurfaceViewListener = var2
        mSurfaceView!!.holder.addCallback(this)
    }

    fun setRenderView(textureView: TextureView?, var2: TextureView.SurfaceTextureListener?) {
        ThreadUtils.checkIsOnMainThread()
        check(!mHasEglSurface) { "Only one egl surface allowed" }
        mTextureView = textureView
        mSurfaceTextureListener = var2
        mTextureView!!.surfaceTextureListener = this
    }

    fun setRenderSurface(surface: Surface) {
        ThreadUtils.checkIsOnMainThread()
        check(!mHasEglSurface) { "Only one egl surface allowed" }
        mSurface = surface
        eglRender.createEglSurface(surface)
        mHasEglSurface = true
    }

    fun setRenderSurface(surfaceTexture: SurfaceTexture) {
        ThreadUtils.checkIsOnMainThread()
        check(!mHasEglSurface) { "Only one egl surface allowed" }
        mSurfaceTexture = surfaceTexture
        eglRender.createEglSurface(surfaceTexture)
        mHasEglSurface = true
    }

    fun setBufferType(bufferType: MediaIO.BufferType) {
        mBufferType = bufferType.intValue()
    }

    fun setPixelFormat(pixelFormat: MediaIO.PixelFormat) {
        mPixelFormat = pixelFormat.intValue()
    }

    fun release() {
        eglRender.release()
    }

    fun start(): Boolean {
        return true.also { mStarted = it }
    }

    fun stop() {
        mStarted = false
    }

    fun consume(
        texId: Int,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long,
        matrix: FloatArray
    ) {
        if (mStarted) {
            val bufferType: TextureBuffer.Type = if (format == MediaIO.PixelFormat.TEXTURE_OES.intValue()) {
                TextureBuffer.Type.OES
            } else {
                if (format != MediaIO.PixelFormat.TEXTURE_2D.intValue()) {
                    return
                }
                TextureBuffer.Type.RGB
            }
            rendTextureFrame(texId, bufferType, width, height, rotation, ts, matrix)
        }
    }

    fun consume(buffer: ByteBuffer?, format: Int, width: Int, height: Int, rotation: Int, ts: Long) {
        if (mStarted) {
            if (format == MediaIO.PixelFormat.I420.intValue()) {
                this.rendI420Frame(buffer, format, width, height, rotation, ts)
            } else if (format == MediaIO.PixelFormat.RGBA.intValue()) {
                this.rendRGBAFrame(buffer, format, width, height, rotation, ts)
            }
        }
    }

    fun consume(data: ByteArray?, format: Int, width: Int, height: Int, rotation: Int, ts: Long) {
        if (mStarted) {
            if (format == MediaIO.PixelFormat.I420.intValue()) {
                this.rendI420Frame(data, format, width, height, rotation, ts)
            } else if (format == MediaIO.PixelFormat.RGBA.intValue()) {
                this.rendRGBAFrame(data, format, width, height, rotation, ts)
            }
        }
    }

    val bufferType: Int
        get() = if (mBufferType == -1) {
            throw IllegalArgumentException("Buffer type is not set")
        } else {
            mBufferType
        }

    val pixelFormat: Int
        get() = if (mPixelFormat == -1) {
            throw IllegalArgumentException("Pixel format is not set")
        } else {
            mPixelFormat
        }

    /**
     * texId, bufferType, width, height, rotation, ts, matrix
     */
    private fun rendTextureFrame(
        textureId: Int,
        bufferType: TextureBuffer.Type,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long,
        matrix: FloatArray
    ) {
        if (!eglRender.hasSurface()) {
            noSurfaceFrameCount++
            if (noSurfaceFrameCount > 30) {
                noSurfaceFrameListener?.invoke(id, noSurfaceFrameCount)
                noSurfaceFrameCount = 0L
            }
        }
        val finalMatrix = RendererCommon.convertMatrixToAndroidGraphicsMatrix(matrix)
        val bufferImpl = TextureBufferImpl(eglRender.eglContext, width, height, bufferType, textureId, finalMatrix, null, null, null)
        val videoFrame = VideoFrame(bufferImpl, rotation, ts)
        eglRender.renderFrame(videoFrame)
        videoFrame.release()
    }

    private fun rendI420Frame(
        byteBuffer: ByteBuffer?,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long
    ) {
        if (byteBuffer != null) {
            val bytes = ByteArray(byteBuffer.remaining())
            byteBuffer[bytes, 0, bytes.size]
            val buffer = JavaI420Buffer.createYUV(bytes, width, height)
            if (buffer != null) {
                val videoFrame = VideoFrame(buffer, rotation, ts)
                eglRender.renderFrame(videoFrame)
                videoFrame.release()
            }
        }
    }

    private fun rendI420Frame(
        data: ByteArray?,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long
    ) {
        if (!eglRender.renderFrameReady()) {
            skipFrameCount++
            if (skipFrameCount <= 1) {
                Timber.d(TAG) { "rendI420Frame($id) but surface not ready count = $skipFrameCount" }
            } else {
                // 中间的日志，不写入日志
                Timber.v(TAG) { "rendI420Frame($id) but surface not ready count = $skipFrameCount" }
            }
            return
        }
        if (data != null && data.isNotEmpty()) {
            val res = kotlin.runCatching {
                JavaI420Buffer.createYUV(data, width, height)
            }
            // 忽略错误帧
            val buffer = res.getOrNull()
            if (res.isSuccess && buffer != null) {
                if (skipFrameCount > 1) {
                    Timber.d(TAG) { "rendI420Frame($id) but surface not ready count = $skipFrameCount" }
                    skipFrameCount = 0
                }
                val videoFrame = VideoFrame(buffer, rotation, ts)
                eglRender.renderFrame(videoFrame)
                videoFrame.release()
            }
        }
    }

    private fun rendRGBAFrame(
        var1: ByteBuffer?,
        var2: Int,
        var3: Int,
        var4: Int,
        var5: Int,
        var6: Long
    ) {
        if (var1 != null) {
            val buffer = RgbaBuffer(var1, var3, var4, null)
            val videoFrame = VideoFrame(buffer, var5, var6)
            eglRender.renderFrame(videoFrame)
            videoFrame.release()
        }
    }

    private fun rendRGBAFrame(
        data: ByteArray?,
        format: Int,
        width: Int,
        height: Int,
        rotation: Int,
        ts: Long
    ) {
        if (data != null && data.isNotEmpty()) {
            val buffer = ByteBuffer.wrap(data)
            val rgbaBuffer = RgbaBuffer(buffer, width, height, null)
            val videoFrame = VideoFrame(rgbaBuffer, rotation, ts)
            eglRender.renderFrame(videoFrame)
            videoFrame.release()
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        ThreadUtils.checkIsOnMainThread()
        eglRender.createEglSurface(surfaceHolder.surface)
        mHasEglSurface = true
        if (mSurfaceViewListener != null) {
            mSurfaceViewListener!!.surfaceCreated(surfaceHolder)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        ThreadUtils.checkIsOnMainThread()
        Timber.d(TAG) { "surfaceChanged: format: " + format + " size: " + width + "x" + height }
        if (mSurfaceViewListener != null) {
            mSurfaceViewListener!!.surfaceChanged(holder, format, width, height)
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        ThreadUtils.checkIsOnMainThread()
        Timber.d(TAG) { "surfaceDestroyed($id) $this" }
        val countDownLatch = CountDownLatch(1)
        eglRender.releaseEglSurface { countDownLatch.countDown() }
        ThreadUtils.awaitUninterruptibly(countDownLatch)
        if (mSurfaceViewListener != null) {
            mSurfaceViewListener!!.surfaceDestroyed(surfaceHolder)
        }
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        ThreadUtils.checkIsOnMainThread()
        Timber.d(TAG) { "onSurfaceTextureAvailable($id) $this" }
        eglRender.createEglSurface(surfaceTexture)
        mHasEglSurface = true
        if (mSurfaceTextureListener != null) {
            mSurfaceTextureListener!!.onSurfaceTextureAvailable(surfaceTexture, width, height)
        }
    }

    override fun onSurfaceTextureSizeChanged(var1: SurfaceTexture, var2: Int, var3: Int) {
        mSurfaceTextureListener?.onSurfaceTextureSizeChanged(var1, var2, var3)
    }

    override fun onSurfaceTextureDestroyed(var1: SurfaceTexture): Boolean {
        ThreadUtils.checkIsOnMainThread()
        Timber.d(TAG) { "onSurfaceTextureDestroyed($id) $this" }
        val countDownLatch = CountDownLatch(1)
        eglRender.releaseEglSurface { countDownLatch.countDown() }
        ThreadUtils.awaitUninterruptibly(countDownLatch)
        mSurfaceTextureListener?.onSurfaceTextureDestroyed(var1)
        return true
    }

    override fun onSurfaceTextureUpdated(var1: SurfaceTexture) {
        if (mSurfaceTextureListener != null) {
            mSurfaceTextureListener!!.onSurfaceTextureUpdated(var1)
        }
    }

    companion object {
        private val TAG = VideoRenderer::class.java.simpleName
    }

}