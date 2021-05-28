package com.hustunique.vlive.opengl

import android.opengl.EGLContext
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
class GLRender(eglContext: EGLContext? = null) : RenderTask {

    private val renderThread = HandlerThread("GL_Render").apply {
        start()
    }

    private val handler = Handler(renderThread.looper)

    private val renderTasks = mutableListOf<RenderTask>()

    private val eglHelper by lazy { EGLHelper(eglContext) }

    // must be called before init()
    fun bindSurface(surface: Surface) {
        post {
            eglHelper.bindSurface(surface)
        }
    }

    fun addRenderTask(renderTask: RenderTask) {
        renderTasks.add(renderTask)
    }

    override fun init() {
        post {
            if (!eglHelper.hasSurface) {
                eglHelper.bindSurface()
            }
            eglHelper.makeCurrent()
            renderTasks.forEach(RenderTask::init)
        }
    }

    override fun render() {
        post {
            renderTasks.forEach(RenderTask::render)
            eglHelper.swapBuffer()
        }
    }

    override fun release() {
        post {
            renderTasks.forEach(RenderTask::release)
            eglHelper.release()
            renderThread.quitSafely()
        }
    }

    fun getHandler() = handler

    fun getEglContext() = eglHelper.getContext()

    fun post(action: () -> Unit) = handler.post {
        action.invoke()
    }
}