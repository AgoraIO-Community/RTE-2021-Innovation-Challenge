package com.dong.circlelive.gl

import javax.microedition.khronos.egl.*
import javax.microedition.khronos.opengles.GL10

class EGLHelper {

    var egl: EGL10? = null
    var eglDisplay: EGLDisplay? = null
    var eglConfig: EGLConfig? = null
    var eglSurface: EGLSurface? = null
    var eglContext: EGLContext? = null
    var gl: GL10? = null

    private var surfaceType = SURFACE_BUFFER
    private var surface_native_obj: Any? = null
    private var red = 8
    private var green = 8
    private var blue = 8
    private var alpha = 8
    private var depth = 16
    private var renderType = 4
    private val bufferType = EGL10.EGL_SINGLE_BUFFER
    private val shareContext = EGL10.EGL_NO_CONTEXT

    fun config(red: Int, green: Int, blue: Int, alpha: Int, depth: Int, renderType: Int) {
        this.red = red
        this.green = green
        this.blue = blue
        this.alpha = alpha
        this.depth = depth
        this.renderType = renderType
    }

    fun setSurfaceType(type: Int, vararg obj: Any?) {
        surfaceType = type
        if (obj != null) {
            surface_native_obj = obj[0]
        }
    }

    fun eglInit(width: Int, height: Int): Int {
        val attributes = intArrayOf(
            EGL10.EGL_RED_SIZE, red,
            EGL10.EGL_GREEN_SIZE, green,
            EGL10.EGL_BLUE_SIZE, blue,
            EGL10.EGL_ALPHA_SIZE, alpha,
            EGL10.EGL_DEPTH_SIZE, depth,
            EGL10.EGL_RENDERABLE_TYPE, renderType,
            EGL10.EGL_NONE
        )

        egl = EGLContext.getEGL() as EGL10
        eglDisplay = egl!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        val version = IntArray(2)
        egl!!.eglInitialize(eglDisplay, version)

        val configNum = IntArray(1)
        egl!!.eglChooseConfig(eglDisplay, attributes, null, 0, configNum)
        if (configNum[0] == 0) {
            return -1
        }
        val c = arrayOfNulls<EGLConfig>(configNum[0])
        egl!!.eglChooseConfig(eglDisplay, attributes, c, configNum[0], configNum)
        eglConfig = c[0]


        val surAttr = intArrayOf(
            EGL10.EGL_WIDTH, width,
            EGL10.EGL_HEIGHT, height,
            EGL10.EGL_NONE
        )
        eglSurface = createSurface(surAttr)

        val contextAttr = intArrayOf(
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL10.EGL_NONE
        )
        eglContext = egl!!.eglCreateContext(eglDisplay, eglConfig, shareContext, contextAttr)
        makeCurrent()
        return 0
    }

    fun makeCurrent() {
        egl!!.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
        gl = eglContext!!.gl as GL10
    }

    fun destroy() {
        egl!!.eglMakeCurrent(
            eglDisplay, EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT
        )
        egl!!.eglDestroySurface(eglDisplay, eglSurface)
        egl!!.eglDestroyContext(eglDisplay, eglContext)
        egl!!.eglTerminate(eglDisplay)
    }

    private fun createSurface(attr: IntArray): EGLSurface {
        return when (surfaceType) {
            SURFACE_WINDOW -> egl!!.eglCreateWindowSurface(eglDisplay, eglConfig, surface_native_obj, attr)
            SURFACE_PIM -> egl!!.eglCreatePixmapSurface(eglDisplay, eglConfig, surface_native_obj, attr)
            else -> egl!!.eglCreatePbufferSurface(eglDisplay, eglConfig, attr)
        }
    }

    companion object {
        private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        const val SURFACE_BUFFER = 1
        const val SURFACE_PIM = 2
        const val SURFACE_WINDOW = 3
    }
}