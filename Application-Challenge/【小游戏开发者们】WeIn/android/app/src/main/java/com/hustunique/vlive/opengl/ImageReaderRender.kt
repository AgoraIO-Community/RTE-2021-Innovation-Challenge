package com.hustunique.vlive.opengl

import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.media.Image
import android.media.ImageReader
import android.opengl.EGLContext
import android.os.Handler
import android.os.Looper

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/13
 */
class ImageReaderRender(eglContext: EGLContext? = null) {

    private val glRender = GLRender(eglContext)

    private var inputTexture: Int = 0

    val imageReader = ImageReader.newInstance(640, 480, PixelFormat.RGBA_8888, 7)

    private val textureRenderTask = TextureRenderTask()

    fun init() {
        glRender.run {
            addRenderTask(textureRenderTask)

            bindSurface(imageReader.surface)
            init()
        }
    }

    fun refreshImageReader() {
        glRender.render()
    }

    fun bindTexture(tex: Int) {
        glRender.post {
            inputTexture = tex
            textureRenderTask.inTexture = inputTexture
        }
    }

    fun getEglContext() = glRender.getEglContext()

}