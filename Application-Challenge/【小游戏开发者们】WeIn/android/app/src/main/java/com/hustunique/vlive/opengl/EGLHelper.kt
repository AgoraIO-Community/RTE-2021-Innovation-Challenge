package com.hustunique.vlive.opengl

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.view.Surface

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/5/21
 */
class EGLHelper(sharedContext: EGLContext?) {

    companion object {
        private const val TAG = "EGLHelper"
    }

    val eglVersion = IntArray(2)

    private val eglConfigAttr = intArrayOf(
        EGL14.EGL_BUFFER_SIZE, 32,
        EGL14.EGL_ALPHA_SIZE, 8,
        EGL14.EGL_BLUE_SIZE, 8,
        EGL14.EGL_RED_SIZE, 8,
        EGL14.EGL_GREEN_SIZE, 8,
        EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
        EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
        EGL14.EGL_NONE
    )

    private val eglContextAttr = intArrayOf(
        EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL14.EGL_NONE
    )

    private var eglDisplay: EGLDisplay? = getEglDisplay()

    private var eglConfig: EGLConfig? = getEglConfig(eglDisplay, eglConfigAttr)

    private var eglContext: EGLContext? = createEglContext(sharedContext, eglDisplay, eglConfig)

    private var eglSurface = EGL14.EGL_NO_SURFACE

    var hasSurface = false

    fun bindSurface(surface: Surface) {
        checkNotRelease()
        val surfaceAttr = intArrayOf(EGL14.EGL_NONE)
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttr, 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            throw RuntimeException("no surface")
        }
        hasSurface = true
    }

    fun bindSurface() {
        checkNotRelease()
        val surfaceAttr = intArrayOf(EGL14.EGL_NONE)
        eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, surfaceAttr, 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            throw RuntimeException("no surface")
        }
        hasSurface = true
    }

    fun swapBuffer() {
        checkNotRelease()
        checkSurface()
        EGL14.eglSwapBuffers(eglDisplay, eglSurface)
    }

    fun makeCurrent() {
        checkNotRelease()
        checkSurface()
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    fun getContext() = eglContext

    fun release() {
        checkNotRelease()
        checkSurface()
        EGL14.eglMakeCurrent(
            eglDisplay,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_CONTEXT
        )
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglTerminate(eglDisplay)
        eglSurface = null
        eglContext = null
        eglDisplay = null
    }

    private fun getEglDisplay(): EGLDisplay {
        val eglDisplay: EGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay === EGL14.EGL_NO_DISPLAY) {
            throw RuntimeException("Unable to get EGL14 display")
        }
        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw RuntimeException("Unable to initialize EGL14")
        }
        return eglDisplay
    }

    private fun getEglConfig(eglDisplay: EGLDisplay?, configAttributes: IntArray): EGLConfig? {
        val configs: Array<EGLConfig?> = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        if (!EGL14.eglChooseConfig(
                eglDisplay, configAttributes, 0, configs, 0, configs.size, numConfigs, 0
            )
        ) {
            throw RuntimeException("Unable to find any matching EGL config")
        }
        return configs[0]
    }

    private fun createEglContext(
        sharedContext: EGLContext?, eglDisplay: EGLDisplay?, eglConfig: EGLConfig?
    ): EGLContext {
        val rootContext: EGLContext =
            sharedContext ?: EGL14.EGL_NO_CONTEXT
        val eglContext: EGLContext =
            EGL14.eglCreateContext(eglDisplay, eglConfig, rootContext, eglContextAttr, 0)
        if (eglContext === EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("Failed to create EGL context")
        }
        return eglContext
    }

    private fun checkSurface() {
        if (eglSurface == null || eglSurface == EGL14.EGL_NO_SURFACE) {
            throw RuntimeException("no surface")
        }
    }

    private fun checkNotRelease() {
        if (eglDisplay == null || eglContext == null || eglConfig == null) {
            throw RuntimeException("egl released")
        }
    }
}