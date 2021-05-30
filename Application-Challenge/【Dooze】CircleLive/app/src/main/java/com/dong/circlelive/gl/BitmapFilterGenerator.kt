package com.dong.circlelive.gl

import android.graphics.Bitmap
import android.opengl.GLES20
import java.nio.ByteBuffer

/**
 * Create by dooze on 2021/5/16  4:05 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class BitmapFilterGenerator(val width: Int, val height: Int) {

    private val eglHelper = EGLHelper()

    private val lookupFilter = LookupFilter()

    private var curTexturedId = OpenGlUtils.NO_TEXTURE

    init {
        eglHelper.eglInit(width, height)
        lookupFilter.init()
        lookupFilter.flip()
        lookupFilter.onOutputSizeChanged(width, height)
    }


    fun setupSourceBitmap(bitmap: Bitmap) {
        curTexturedId = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE, false)
    }

    fun changeLut(lutTextureId: Int) {
        lookupFilter.mLookupSourceTexture = lutTextureId
    }

    fun changeLut(assetPath: String) {
        lookupFilter.changeFilter(assetPath)
    }


    fun render(): Bitmap {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        lookupFilter.onDraw(curTexturedId)

        val tmpBuffer = ByteBuffer.allocate(width * height * 4)
        GLES20.glReadPixels(
            0,
            0,
            width,
            height,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            tmpBuffer
        )
        val processedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        processedImage.copyPixelsFromBuffer(tmpBuffer)

        return processedImage
    }


    fun release() {
        eglHelper.destroy()
    }

}