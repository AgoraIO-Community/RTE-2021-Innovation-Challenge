package com.hustunique.vlive.opengl

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.opengl.GLES11Ext
import io.agora.rtc.gl.GlUtil

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
class LocalFrameManager {

    private val glRender = GLRender(null)

    private var inputTexture: Int = 0

    private val imageReader = ImageReader.newInstance(640, 480, ImageFormat.YUV_420_888, 7)

    private val oesTextureRenderTask = OesTextureRenderTask(0)

    var onImage: (Image) -> Unit = { it.close() }

    fun init() {
        glRender.run {
            addRenderTask(oesTextureRenderTask)

            bindSurface(imageReader.surface)
            init()
        }
        glRender.post {
            inputTexture = GlUtil.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
//            oesTextureRenderTask.inTexture = inputTexture
        }
        imageReader.setOnImageAvailableListener(
            {
                if (it.maxImages > 0) {
//                    val i = it.acquireLatestImage()
//                    onImage(i)
                }
            },
            glRender.getHandler()
        )
    }

    fun refreshImageReader() {
        glRender.render()
    }

    fun getEglContext() = glRender.getEglContext()

    fun getOesTexture() = inputTexture

    fun getHandler() = glRender.getHandler()

    fun release() {
        glRender.release()
    }

}