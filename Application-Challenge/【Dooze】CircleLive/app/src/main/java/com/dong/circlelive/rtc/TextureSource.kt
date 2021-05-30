package com.dong.circlelive.rtc

import android.graphics.SurfaceTexture
import android.os.Handler
import io.agora.rtc.gl.EglBase
import io.agora.rtc.mediaio.IVideoFrameConsumer
import io.agora.rtc.mediaio.IVideoSource
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.mediaio.MediaIO.CaptureType
import io.agora.rtc.mediaio.MediaIO.ContentHint
import java.lang.ref.WeakReference

@Suppress("LeakingThis")
abstract class TextureSource : IVideoSource, SurfaceTextureHelper.OnTextureFrameAvailableListener {
    protected var consumer: WeakReference<IVideoFrameConsumer?>? = null
    protected var surfaceTextureHelper: SurfaceTextureHelper?
    protected var mWidth: Int
    protected var mHeight: Int
    private var mPixelFormat: Int

    val handler: Handler
        get() = surfaceTextureHelper!!.handler!!

    constructor(sharedContext: EglBase.Context?, width: Int, height: Int) {
        mWidth = width
        mHeight = height
        mPixelFormat = 11
        val surfaceTextureHelper = SurfaceTextureHelper.create("TexCamThread", sharedContext)
        surfaceTextureHelper.surfaceTexture!!.setDefaultBufferSize(width, height)
        surfaceTextureHelper.startListening(this)
        this.surfaceTextureHelper = surfaceTextureHelper
    }

    constructor(sharedContext: EglBase.Context?, width: Int, height: Int, copyOesTo2DTex: Boolean) {
        mWidth = width
        mHeight = height
        mPixelFormat = 11
        val surfaceTextureHelper = SurfaceTextureHelper.create("TexCamThreadOesTo2D", sharedContext, copyOesTo2DTex, width, height)
        surfaceTextureHelper.surfaceTexture!!.setDefaultBufferSize(width, height)
        surfaceTextureHelper.startListening(this)
        this.surfaceTextureHelper = surfaceTextureHelper
    }


    override fun onInitialize(observer: IVideoFrameConsumer): Boolean {
        consumer = WeakReference<IVideoFrameConsumer?>(observer)
        return onCapturerOpened()
    }

    override fun onStart(): Boolean {
        return onCapturerStarted()
    }

    override fun onStop() {
        onCapturerStopped()
    }

    override fun onDispose() {
        consumer = null
        onCapturerClosed()
    }

    override fun getBufferType(): Int {
        return 3
    }

    override fun getCaptureType(): Int {
        return CaptureType.CAMERA.intValue()
    }

    override fun getContentHint(): Int {
        return ContentHint.NONE.intValue()
    }

    override fun onTextureFrameAvailable(oesTextureId: Int, transformMatrix: FloatArray, timestampNs: Long) {
        surfaceTextureHelper!!.returnTextureFrame()
    }

    override fun onTextureFrameAvailable(oesTextureId: Int, format: MediaIO.PixelFormat, transformMatrix: FloatArray, timestampNs: Long) {
        surfaceTextureHelper!!.returnTextureFrame()
    }

    val surfaceTexture: SurfaceTexture
        get() = surfaceTextureHelper!!.surfaceTexture!!
    val eglContext: EglBase.Context
        get() = surfaceTextureHelper!!.eglContext

    fun release() {
        surfaceTextureHelper!!.stopListening()
        surfaceTextureHelper!!.dispose()
        surfaceTextureHelper = null
    }

    protected abstract fun onCapturerOpened(): Boolean
    protected abstract fun onCapturerStarted(): Boolean
    protected abstract fun onCapturerStopped()
    protected abstract fun onCapturerClosed()
}