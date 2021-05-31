package com.hustunique.vlive.local

import android.content.Context
import android.graphics.Matrix
import android.os.Handler
import android.util.Log
import com.google.ar.core.*
import java.nio.FloatBuffer
import java.util.*

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/28/21
 */
@LocalGLThread
class ARCoreController(
    context: Context,
    private val glHandler: Handler,
    private val oesTexture: Int,
    private val onNewFrame: () -> Unit,
) : Runnable {

    private lateinit var session: Session

    private val objectMatrixData = FloatArray(16)
    private val objectMatrix = Matrix()

    init {
        configSession(context)
    }

    private fun configSession(context: Context) {
        session = Session(context, EnumSet.noneOf(Session.Feature::class.java))
        val cameraConfigFilter = CameraConfigFilter(session)
        cameraConfigFilter.facingDirection = CameraConfig.FacingDirection.FRONT
        val cameraConfigs = session.getSupportedCameraConfigs(cameraConfigFilter)
        if (cameraConfigs.isNotEmpty()) {
            session.cameraConfig = cameraConfigs[0]
        } else {
            throw Exception("Camera Not support")
        }

        val config = Config(session)
        config.augmentedFaceMode = Config.AugmentedFaceMode.MESH3D
        session.configure(config)

        Log.i(TAG, "configSession: $oesTexture")
    }

    fun release() {
        session.close()
        glHandler.removeCallbacks(this)
    }

    fun resume() {
        Log.i(TAG, "resume: ")
        session.resume()
        glHandler.post(this)
    }

    fun pause() {
        session.pause()
        glHandler.removeCallbacks(this)
    }

    override fun run() {
        session.setCameraTextureName(oesTexture)
        session.update()
        session.getAllTrackables(AugmentedFace::class.java)
            .firstOrNull { it.trackingState == TrackingState.TRACKING }
            ?.getRegionPose(AugmentedFace.RegionType.NOSE_TIP)
            ?.toMatrix(objectMatrixData, 0)
        objectMatrixData[0] = objectMatrixData[0] * -1
        objectMatrixData[4] = objectMatrixData[4] * -1
        objectMatrixData[8] = objectMatrixData[8] * -1
        objectMatrixData[12] = objectMatrixData[12] * -1
        objectMatrix.setValues(objectMatrixData)
        onNewFrame()

        glHandler.post(this)
    }

    fun getObjectMatrixData(buffer: FloatBuffer) {
        buffer.put(objectMatrixData)
    }

    companion object {
        private const val TAG = "SurfaceTextureHelper"
    }
}