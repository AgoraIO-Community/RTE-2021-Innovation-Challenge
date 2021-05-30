package com.dong.circlelive.rtc

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.Surface
import com.dong.circlelive.base.Timber
import io.agora.rtc.gl.EglBase
import io.agora.rtc.gl.RendererCommon
import io.agora.rtc.gl.VideoFrame
import io.agora.rtc.utils.ThreadUtils
import java.util.*
import java.util.concurrent.CountDownLatch

class EglRenderer(private val name: String, val id: String) {
    private val handlerLock = "lock_$name-$id"
    private var renderThreadHandler: Handler? = null
    private val frameListeners: ArrayList<FrameListenerAndParams> = ArrayList<FrameListenerAndParams>()
    private var eglBase: EglBase? = null
    private val frameDrawer = VideoFrameDrawer()
    private var drawer: RendererCommon.GlDrawer? = null
    private val drawMatrix = Matrix()
    private val frameLock = Any()
    private var pendingFrame: VideoFrame? = null
    private val layoutLock = Any()
    private var layoutAspectRatio = 0f
    private var mirror = false
    private val eglSurfaceCreationRunnable = EglSurfaceCreation()

    @Volatile
    private var isReleased = false

    @Volatile
    private var isInitOk = false

    fun init(context: EglBase.Context?, var2: IntArray?, glDrawer: RendererCommon.GlDrawer?) {
        synchronized(handlerLock) {
            if (renderThreadHandler != null) {
                throw IllegalStateException("${name}($id)Already initialized")
            } else {
                logD("Initializing EglRenderer")
                drawer = glDrawer
                val handlerThread = HandlerThread("${name}-${id}-EglRenderer")
                handlerThread.start()
                val renderThreadHandler = Handler(handlerThread.looper)
                this.renderThreadHandler = renderThreadHandler
                ThreadUtils.invokeAtFrontUninterruptibly(renderThreadHandler) {
                    eglBase = if (context == null) {
                        logD("EglBase.create context")
                        EglBase.create(context, var2)
                    } else {
                        logD("EglBase.create shared context")
                        EglBase.create(context, var2)
                    }
                    Timber.d(name) { "inited eglBase($id) $this" }
                    isInitOk = true
                }
                renderThreadHandler.post(eglSurfaceCreationRunnable)
            }
        }
    }

    val eglContext: EglBase.Context
        get() = eglBase!!.eglBaseContext

    fun createEglSurface(su: Surface) {
        createEglSurfaceInternal(su)
    }

    fun createEglSurface(surfaceTexture: SurfaceTexture) {
        createEglSurfaceInternal(surfaceTexture)
    }

    private fun createEglSurfaceInternal(var1: Any) {
        eglSurfaceCreationRunnable.setSurface(var1)
        postToRenderThread(eglSurfaceCreationRunnable)
    }

    fun isEglReleased(): Boolean = isInitOk && (eglBase == null || eglBase?.hasSurface() == false)

    fun release() {
        logD("Releasing.")
        if (renderThreadHandler == null) {
            logD("Already released 1 ")
            return
        }
        isReleased = true
        val countDownLatch = CountDownLatch(2)
        synchronized(handlerLock) {
            val handler = renderThreadHandler
            if (handler == null) {
                logD("Already released 2 ")
                return
            }
            handler.postAtFrontOfQueue {
                drawer?.release()
                drawer = null
                frameDrawer.release()
                val eglBase = eglBase
                if (eglBase != null) {
                    logD("eglBase($id) detach and release.")
                    eglBase.detachCurrent()
                    eglBase.release()
                    this.eglBase = null
                }
                countDownLatch.countDown()
            }
            val looper: Looper = handler.looper
            handler.post {
                logD("Quitting render thread.")
                looper.quit()
                countDownLatch.countDown()
            }
            renderThreadHandler = null
        }
        ThreadUtils.awaitUninterruptibly(countDownLatch)
        synchronized(frameLock) {
            if (pendingFrame != null) {
                pendingFrame!!.release()
                pendingFrame = null
            }
        }
        logD("Releasing done.")
    }

    fun setMirror(var1: Boolean) {
        logD("setMirror: $var1")
        synchronized(layoutLock) { mirror = var1 }
    }

    fun setLayoutAspectRatio(ratio: Float) {
        logD("setLayoutAspectRatio: $ratio")
        val finalRatio = if (ratio.isNaN() || ratio.isInfinite()) 0f else ratio
        synchronized(layoutLock) { layoutAspectRatio = finalRatio }
    }

    @JvmOverloads
    fun addFrameListener(
        frameListener: FrameListener,
        var2: Float,
        glDrawer: RendererCommon.GlDrawer? = null,
        var4: Boolean = false
    ) {
        postToRenderThread {
            val drawer = glDrawer ?: drawer
            frameListeners.add(FrameListenerAndParams(frameListener, var2, drawer, var4))
        }
    }

    fun removeFrameListener(frameListener: FrameListener) {
        if (Thread.currentThread() === renderThreadHandler!!.looper.thread) {
            throw RuntimeException("removeFrameListener must not be called on the render thread.")
        } else {
            val downLatch = CountDownLatch(1)
            postToRenderThread {
                downLatch.countDown()
                val iterator = frameListeners.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().listener === frameListener) {
                        iterator.remove()
                    }
                }
            }
            ThreadUtils.awaitUninterruptibly(downLatch)
        }
    }

    fun renderFrame(videoFrame: VideoFrame?) {
        onFrame(videoFrame)
    }

    fun renderFrameReady(): Boolean {
        val eglBase = eglBase
        return eglBase != null && eglBase.hasSurface()
    }

    fun hasSurface(): Boolean = eglBase?.hasSurface() == true

    private fun onFrame(videoFrame: VideoFrame?) {
        if (isReleased) return
        var padding: Boolean
        synchronized(handlerLock) {
            if (renderThreadHandler == null) {
                logD("Dropping frame - Not initialized or already released 1.")
                return
            }
            synchronized(frameLock) {
                padding = pendingFrame != null
                if (padding) {
                    pendingFrame?.release()
                }
                pendingFrame = videoFrame
                pendingFrame?.retain()
            }
            if (renderThreadHandler == null) {
                logD("Dropping frame - Not initialized or already released 2.")
                return
            }

            ThreadUtils.invokeAtFrontUninterruptibly(renderThreadHandler) { renderFrameOnRenderThread() }
        }
    }

    fun releaseEglSurface(runnable: Runnable) {
        eglSurfaceCreationRunnable.setSurface(null)
        synchronized(handlerLock) {
            val renderThreadHandler = renderThreadHandler
            if (renderThreadHandler != null) {
                renderThreadHandler.removeCallbacks(eglSurfaceCreationRunnable)
                renderThreadHandler.postAtFrontOfQueue {
                    val eglBase = eglBase
                    if (eglBase != null) {
                        eglBase.detachCurrent()
                        eglBase.releaseSurface()
                        Timber.d(name) { "releaseEglSurface($id). $this" }
                    }
                    runnable.run()
                }
                return
            }
        }
        runnable.run()
    }

    private fun postToRenderThread(runnable: Runnable) {
        synchronized(handlerLock) {
            renderThreadHandler?.post(runnable)
        }
    }

    private fun clearSurfaceOnRenderThread(red: Float, green: Float, blue: Float, alpha: Float) {
        if (eglBase != null && eglBase!!.hasSurface()) {
            logD("clearSurface")
            GLES20.glClearColor(red, green, blue, alpha)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            eglBase!!.swapBuffers()
        }
    }

    @JvmOverloads
    fun clearImage(red: Float = 0.0f, green: Float = 0.0f, blue: Float = 0.0f, alpha: Float = 0.0f) {
        synchronized(handlerLock) {
            if (renderThreadHandler != null) {
                renderThreadHandler!!.postAtFrontOfQueue {
                    clearSurfaceOnRenderThread(
                        red,
                        green,
                        blue,
                        alpha
                    )
                }
            }
        }
    }

    private fun renderFrameOnRenderThread() {
        var videoFrame: VideoFrame?
        synchronized(frameLock) {
            if (pendingFrame == null) {
                return
            }
            videoFrame = pendingFrame
            pendingFrame = null
        }
        val frame = videoFrame
        if (frame == null) {
            logD("Skipping frame rendering - null frame.")
            return
        }
        val eglBase = eglBase
        if (eglBase != null && eglBase.hasSurface()) {
            val ratio = frame.rotatedWidth.toFloat() / frame.rotatedHeight.toFloat()
            var finalRatio: Float
            synchronized(layoutLock) { finalRatio = if (layoutAspectRatio != 0.0f) layoutAspectRatio else ratio }
            val scaleX: Float
            val scaleY: Float
            if (ratio > finalRatio) {
                scaleX = finalRatio / ratio
                scaleY = 1.0f
            } else {
                scaleX = 1.0f
                scaleY = ratio / finalRatio
            }
            drawMatrix.reset()
            drawMatrix.preTranslate(0.5f, 0.5f)
            if (mirror) {
                drawMatrix.preScale(-1.0f, 1.0f)
            }
            drawMatrix.preScale(scaleX, scaleY)
            drawMatrix.preTranslate(-0.5f, -0.5f)
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
//                Timber.d(name) { "onRenderFrame $videoFrame $this" }
            frameDrawer.drawFrame(
                frame,
                drawer!!,
                drawMatrix,
                0,
                0,
                eglBase.surfaceWidth(),
                eglBase.surfaceHeight()
            )
            eglBase.swapBuffers()
            frame.release()
        } else {
            frame.release()
        }
    }

    private fun logD(info: String) {
        Timber.d("EglRenderer") { "$name($id):$info " }
    }

    private inner class EglSurfaceCreation : Runnable {
        private var surface: Any? = null

        @Synchronized
        fun setSurface(surface: Any?) {
            this.surface = surface
        }

        @Synchronized
        override fun run() {
            val eglBase = eglBase
            logD("EglSurfaceCreation ${eglBase?.hasSurface()} - $surface $eglBase")
            if (surface != null && eglBase == null) {
                Timber.w(LocalRender.TAG) { "create egl surface but not egl context" }
            }
            if (surface != null && eglBase != null && !eglBase.hasSurface()) {
                if (surface is Surface) {
                    eglBase.createSurface(surface as Surface?)
                } else {
                    check(surface is SurfaceTexture) { "Invalid surface: $surface" }
                    eglBase.createSurface(surface as SurfaceTexture?)
                }
                eglBase.makeCurrent()
                isReleased = false
                GLES20.glPixelStorei(3317, 1)
            }
        }
    }

    data class FrameListenerAndParams(
        val listener: FrameListener,
        val scale: Float,
        val drawer: RendererCommon.GlDrawer?,
        val applyFpsReduction: Boolean
    )

    interface FrameListener {
        fun onFrame(var1: Bitmap?)
    }

}