package com.hustunique.vlive.agora

import android.util.Log
import android.view.Surface
import com.hustunique.vlive.filament.ModelViewer
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.mediaio.TextureSource

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/27/21
 */
class FilamentTextureSource(
    width: Int,
    height: Int,
    private val modelViewer: ModelViewer
) : TextureSource(null, width, height) {

    companion object {
        private const val TAG = "FilamentTextureSource"
    }

    private var mStarted = false

    override fun onCapturerOpened(): Boolean {
        Log.i(TAG, "onCapturerOpened: ")
        modelViewer.addRenderTarget(Surface(surfaceTexture))
        return true
    }

    override fun onCapturerStarted(): Boolean {
        Log.i(TAG, "onCapturerStarted: ")
        mStarted = true
        return true
    }

    override fun onCapturerStopped() {
        Log.i(TAG, "onCapturerStopped: ")
        mStarted = false
    }

    override fun onCapturerClosed() {
        Log.i(TAG, "onCapturerClosed: ")
    }

    override fun onTextureFrameAvailable(
        oesTextureId: Int,
        transformMatrix: FloatArray?,
        timestampNs: Long
    ) {
        super.onTextureFrameAvailable(oesTextureId, transformMatrix, timestampNs)
        if (mStarted && mConsumer != null && mConsumer.get() != null) {
            Log.i(TAG, "onTextureFrameAvailable: ")
            mConsumer!!.get()?.consumeTextureFrame(
                oesTextureId, MediaIO.PixelFormat.TEXTURE_OES.intValue(), mWidth, mHeight,
                0, System.currentTimeMillis(), transformMatrix
            )
        }
    }
}