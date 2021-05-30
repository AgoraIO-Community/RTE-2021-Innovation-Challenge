package com.dong.circlelive.rtc

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import com.dong.circlelive.base.Timber
import io.agora.rtc.gl.EglBase
import io.agora.rtc.gl.GlUtil
import io.agora.rtc.gl.TextureTransformer
import io.agora.rtc.gl.YuvConverter
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.utils.ThreadUtils

class SurfaceTextureHelper {

    var handler: Handler? = null
        private set
    private var eglBase: EglBase? = null
    var surfaceTexture: SurfaceTexture? = null
    private var oesTextureId = 0
    private val yuvConverter: YuvConverter? = null
    private var mCopyTo2DTexture: Boolean
    private var textureTransformer: TextureTransformer?
    private var mWidth = 0
    private var mHeight = 0
    private var listener: OnTextureFrameAvailableListener? = null
    private var hasPendingTexture: Boolean

    @Volatile
    private var isTextureInUse: Boolean
    private var isQuitting: Boolean
    private var pendingListener: OnTextureFrameAvailableListener? = null
    private val setListenerRunnable: Runnable

    @SuppressLint("Recycle")
    private constructor(sharedContext: EglBase.Context?, handler: Handler) {
        mCopyTo2DTexture = false
        textureTransformer = null
        hasPendingTexture = false
        isTextureInUse = false
        isQuitting = false
        setListenerRunnable = InitRunnable()
        check(!(handler.looper.thread !== Thread.currentThread())) { "SurfaceTextureHelper must be created on the handler thread" }
        this.handler = handler
        val eglBase = EglBase.create(sharedContext, EglBase.CONFIG_PIXEL_BUFFER)
        try {
            eglBase.createDummyPbufferSurface()
            eglBase.makeCurrent()
        } catch (var4: RuntimeException) {
            Timber.e(TAG) { "SurfaceTextureHelper: failed to create pbufferSurface!!" }
            eglBase.release()
            handler.looper.quit()
            throw var4
        }
        this.eglBase = eglBase
        oesTextureId = GlUtil.generateTexture(36197)
        surfaceTexture = SurfaceTexture(oesTextureId)
        setOnFrameAvailableListener(surfaceTexture, {
            hasPendingTexture = true
            tryDeliverTextureFrame()
        }, handler)
    }

    @SuppressLint("Recycle")
    private constructor(sharedContext: EglBase.Context?, handler: Handler, copyTo2DTexture: Boolean, width: Int, height: Int) {
        mCopyTo2DTexture = false
        textureTransformer = null
        hasPendingTexture = false
        isTextureInUse = false
        isQuitting = false
        setListenerRunnable = InitRunnable()
        check(!(handler.looper.thread !== Thread.currentThread())) { "SurfaceTextureHelper must be created on the handler thread" }
        this.handler = handler
        val eglBase = EglBase.create(sharedContext, EglBase.CONFIG_PIXEL_BUFFER)
        try {
            eglBase.createDummyPbufferSurface()
            eglBase.makeCurrent()
        } catch (var7: RuntimeException) {
            Timber.e(TAG) { "SurfaceTextureHelper: failed to create pbufferSurface!!" }
            eglBase.release()
            handler.looper.quit()
            throw var7
        }
        this.eglBase = eglBase
        mCopyTo2DTexture = copyTo2DTexture
        if (copyTo2DTexture) {
            mWidth = width
            mHeight = height
            textureTransformer = TextureTransformer(2)
        }
        oesTextureId = GlUtil.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        surfaceTexture = SurfaceTexture(oesTextureId)
        setOnFrameAvailableListener(surfaceTexture, {
            hasPendingTexture = true
            tryDeliverTextureFrame()
        }, handler)
    }

    internal inner class InitRunnable : Runnable {
        override fun run() {
            Timber.d(TAG) { "Setting listener to $pendingListener" }
            listener = pendingListener
            pendingListener = null
            if (hasPendingTexture) {
                updateTexImage()
                hasPendingTexture = false
            }
        }
    }

    val eglContext: EglBase.Context
        get() = eglBase!!.eglBaseContext

    fun startListening(listener: OnTextureFrameAvailableListener?) {
        if (this.listener == null && pendingListener == null) {
            pendingListener = listener
            handler!!.post(setListenerRunnable)
        } else {
            throw IllegalStateException("SurfaceTextureHelper listener has already been set.")
        }
    }

    fun stopListening() {
        Timber.d(TAG) { "stopListening()" }
        handler!!.removeCallbacks(setListenerRunnable)
        ThreadUtils.invokeAtFrontUninterruptibly(handler) {
            listener = null
            pendingListener = null
        }
    }

    fun returnTextureFrame() {
        handler!!.post {
            isTextureInUse = false
            if (isQuitting) {
                release()
            } else {
                tryDeliverTextureFrame()
            }
        }
    }

    fun dispose() {
        Timber.d(TAG) { "dispose()" }
        ThreadUtils.invokeAtFrontUninterruptibly(handler) {
            isQuitting = true
            if (!isTextureInUse) {
                release()
            }
        }
    }

    private fun updateTexImage() {
        try {
            synchronized(EglBase.lock) { surfaceTexture!!.updateTexImage() }
        } catch (t: IllegalStateException) {
            Timber.e(TAG, t) { "SurfaceTextureHelper: failed to updateTexImage!!" }
        }
    }

    private fun tryDeliverTextureFrame() {
        check(!(handler!!.looper.thread !== Thread.currentThread())) { "Wrong thread." }
        val surfaceTexture = surfaceTexture
        if (surfaceTexture == null) {
            Timber.w(TAG) { "tryDeliverTextureFrame null surface texture" }
            return
        }
        if (!isQuitting && hasPendingTexture && !isTextureInUse && listener != null) {
            isTextureInUse = true
            hasPendingTexture = false
            updateTexImage()
            val transformMatrix = FloatArray(16)
            surfaceTexture.getTransformMatrix(transformMatrix)
            val timestampNs = surfaceTexture.timestamp
            if (mCopyTo2DTexture && textureTransformer != null) {
                val oesTextureId = textureTransformer!!.copy(oesTextureId, MediaIO.PixelFormat.TEXTURE_OES.intValue(), mWidth, mHeight)
                listener!!.onTextureFrameAvailable(oesTextureId, MediaIO.PixelFormat.TEXTURE_2D, transformMatrix, timestampNs)
            } else {
                listener!!.onTextureFrameAvailable(oesTextureId, transformMatrix, timestampNs)
            }
        }
    }

    private fun release() {
        check(!(handler!!.looper.thread !== Thread.currentThread())) { "Wrong thread." }
        if (!isTextureInUse && isQuitting) {
            yuvConverter?.release()
            textureTransformer?.release()
            GLES20.glDeleteTextures(1, intArrayOf(oesTextureId), 0)
            surfaceTexture!!.release()
            eglBase!!.release()
            handler!!.looper.quit()
        } else {
            throw IllegalStateException("Unexpected release.")
        }
    }

    interface OnTextureFrameAvailableListener {
        fun onTextureFrameAvailable(oesTextureId: Int, transformMatrix: FloatArray, timestampNs: Long)
        fun onTextureFrameAvailable(oesTextureId: Int, format: MediaIO.PixelFormat, transformMatrix: FloatArray, timestampNs: Long)
    }

    companion object {
        private const val TAG = "SurfaceTextureHelper"
        private const val MAX_TEXTURE_COPY = 2
        fun create(threadName: String, sharedContext: EglBase.Context?): SurfaceTextureHelper {
            val handlerThread = HandlerThread(threadName)
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            return ThreadUtils.invokeAtFrontUninterruptibly<SurfaceTextureHelper>(handler) {
                try {
                    return@invokeAtFrontUninterruptibly SurfaceTextureHelper(sharedContext, handler)
                } catch (exception: RuntimeException) {
                    Timber.e(TAG, exception) { "$threadName create failure" }
                    return@invokeAtFrontUninterruptibly null
                }
            }
        }

        fun create(threadName: String, sharedContext: EglBase.Context?, copyTo2DTexture: Boolean, width: Int, height: Int): SurfaceTextureHelper {
            val handlerThread = HandlerThread(threadName)
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            return ThreadUtils.invokeAtFrontUninterruptibly<SurfaceTextureHelper>(handler) {
                try {
                    return@invokeAtFrontUninterruptibly SurfaceTextureHelper(sharedContext, handler, copyTo2DTexture, width, height)
                } catch (exception: RuntimeException) {
                    Timber.e(TAG, exception) { "$threadName create failure" }
                    return@invokeAtFrontUninterruptibly null
                }
            }
        }

        @TargetApi(21)
        private fun setOnFrameAvailableListener(surfaceTexture: SurfaceTexture?, listener: OnFrameAvailableListener, handler: Handler) {
            if (Build.VERSION.SDK_INT >= 21) {
                surfaceTexture!!.setOnFrameAvailableListener(listener, handler)
            } else {
                surfaceTexture!!.setOnFrameAvailableListener(listener)
            }
        }
    }
}