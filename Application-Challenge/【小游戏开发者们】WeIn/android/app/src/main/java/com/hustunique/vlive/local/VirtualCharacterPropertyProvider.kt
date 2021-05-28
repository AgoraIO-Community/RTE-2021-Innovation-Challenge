package com.hustunique.vlive.local

import android.content.Context
import android.graphics.PixelFormat
import android.media.ImageReader
import android.opengl.GLES11Ext
import android.opengl.GLES30
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.hustunique.vlive.opengl.EGLHelper
import com.hustunique.vlive.opengl.OesTextureRenderTask
import com.hustunique.vlive.opengl.RenderTask
import io.agora.rtc.gl.GlUtil
import java.nio.FloatBuffer

/**
 * Sampler for local video/audio
 * Retrieve interest point and provide original frame
 */
class VirtualCharacterPropertyProvider(
    context: Context,
    onPropertyReady: (CharacterProperty) -> Unit,
) {
    private val localVideoRenderThread = HandlerThread("lo cal_video_renderer").apply { start() }
    private val glHandler = Handler(localVideoRenderThread.looper)

    private val imageReader = ImageReader.newInstance(120, 160, PixelFormat.RGBA_8888, 3)

    private lateinit var arCore: ARCoreController
    private lateinit var mlKit: MLKitController

    private val eglHelper = EGLHelper(null)
    private lateinit var oesRenderer: RenderTask

    init {
        glHandler.post {
            // bind image reader's surface to opengl's context and initialize render tasks
            eglHelper.bindSurface(imageReader.surface)
            eglHelper.makeCurrent()

            // setup texture where camera could render
            val texture = GlUtil.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
            Log.i(TAG, "Create OES texture: ${GLES30.glIsTexture(texture)}")

            // setup task that using opengl to draw texture to gl's buffer(provided by imageReader)
            oesRenderer = OesTextureRenderTask(texture).apply { init() }

            // setup AR core producer that render camera data to texture
            val arCore = ARCoreController(context, glHandler, texture, ::onNewFrame)
            // setup MLKit to detect eye open properties and face contours
            val mlKit = MLKitController { f1, f2, f3 ->
                glHandler.post {
                    // TODO: store quaternion data instead
                    val buffer = FloatBuffer.allocate(4)
//                    arCore.getObjectMatrixData(buffer)
                    onPropertyReady(CharacterProperty(f1, f2, f3, buffer, FloatBuffer.allocate(7)))
                }
            }

            imageReader.setOnImageAvailableListener(mlKit::onImageAvailable, glHandler)
            this.arCore = arCore
            this.mlKit = mlKit
        }
    }

    private fun onNewFrame() {
        oesRenderer.render()
        eglHelper.swapBuffer()
    }

    fun resume() = glHandler.post {
        arCore.resume()
    }

    fun pause() = glHandler.post {
        arCore.pause()
    }

    fun destroy() = glHandler.post {
        arCore.release()
        imageReader.close()
        mlKit.stop()
        eglHelper.release()
        localVideoRenderThread.quitSafely()
    }

    companion object {
        private const val TAG = "LocalVideoModule"
    }
}