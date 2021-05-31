@file:Suppress("DEPRECATION")

package com.dong.circlelive.rtc

import android.content.Context
import android.graphics.Bitmap
import android.hardware.Camera
import android.opengl.GLES20
import android.view.Surface
import android.view.WindowManager
import com.dong.circlelive.base.Timber
import com.dong.circlelive.camera.Accelerometer
import com.dong.circlelive.camera.CameraController
import com.dong.circlelive.camera.CameraController1
import com.dong.circlelive.gl.GPUImageFilter
import com.dong.circlelive.gl.LookupFilter
import com.dong.circlelive.gl.TextureRotationUtil
import io.agora.rtc.gl.GlUtil
import io.agora.rtc.gl.RendererCommon
import io.agora.rtc.mediaio.MediaIO
import java.nio.ByteBuffer
import java.nio.FloatBuffer

/**
 * Create by dooze on 2020/7/3  10:29 AM
 * Email: stonelavender@hotmail.com
 * Description:
 */
@Suppress("DEPRECATION")
class CameraSource(
    var context: Context?,
    val width: Int,
    val height: Int,
) :
    TextureSource(null, width, height), CameraController.ErrorCallback {

    private var cameraController: CameraController? = null
    private var filter: GPUImageFilter? = null
    private var lookupFilter: LookupFilter? = null
    private var beautyChicFilter: LookupFilter? = null

    private val noFlipPos = TextureRotationUtil.getPhotoRotation(0, false, true)
    private val flipHorizontalPos = TextureRotationUtil.getPhotoRotation(90, false, true)

    private val vertexBuffer: FloatBuffer = GlUtil.createFloatBuffer(TextureRotationUtil.CUBE)

    private val textureBuffer: FloatBuffer = GlUtil.createFloatBuffer(
        flipHorizontalPos
    )
    private val textureFrontBuffer: FloatBuffer = GlUtil.createFloatBuffer(
        TextureRotationUtil.getPhotoRotation(90, true, true)
    )
    private val textureBuffer2: FloatBuffer = GlUtil.createFloatBuffer(noFlipPos)

    private var cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT

    private var pendingPreviewCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT

    @Volatile
    private var triggerFirstFrame = false

    var screenShotListener: ((bitmap: Bitmap) -> Unit)? = null

    @Volatile
    private var cameraOpened = false


    var outerRender: LocalRender? = null

    var preDisplayRender: LocalRender? = null

    fun init(forceFrontCamera: Boolean? = null) {
        val front = forceFrontCamera ?: true
        cameraId = if (front) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK
        handler.post {
            initBaseFilter()
        }
    }

    private fun doAction(tag: String? = null, block: () -> Unit) {
        val handler = surfaceTextureHelper?.handler
        if (handler != null) {
            handler.post {
                block.invoke()
            }
        } else {
            throw RuntimeException("doAction($tag) with null handler")
        }
    }

    private fun initBaseFilter() {
        if (filter == null) {
            filter = GPUImageFilter().apply {
                init()
                onOutputSizeChanged(width, height)
            }
        }

        if (lookupFilter == null) {
            lookupFilter = LookupFilter().apply {
                init()
                onOutputSizeChanged(width, height)
            }
        }
    }

    fun screenShot(screenShotListener: ((bitmap: Bitmap) -> Unit)) {
        this.screenShotListener = screenShotListener
    }

    fun changeIntensity(intensity: Float) {
        lookupFilter?.intensity = intensity
    }


    fun changeFilter(assetPath: String) {
        lookupFilter?.changeFilter(assetPath)
    }

    fun manualStart() {
        doAction("manualStart") {
            var ready = true
            if (cameraController == null) {
                ready = onCapturerOpened()
            }
            if (ready) {
                startCameraPreview()
            }
        }
    }

    fun manualClose() {
        doAction("manualClose") {
            onCapturerClosed()
        }
    }

    override fun onCapturerStarted(): Boolean {
        Timber.d(TAG) { "onCapturerStarted" }
        startCameraPreview()
        return true
    }

    override fun onCapturerOpened(): Boolean {
        Timber.d(TAG) { "onCapturerOpened hasController = ${cameraController != null}" }
        if (cameraController != null) {
            return true
        }
        val res = kotlin.runCatching {
            openCamera()
        }
        return res.isSuccess
    }

    override fun onCapturerStopped() {
        Timber.d(TAG) { "onCapturerStopped" }
        cameraController?.stopPreview()
        triggerFirstFrame = false
    }

    override fun onCapturerClosed() {
        Timber.d(TAG) { "onCapturerClosed" }
        releaseCamera()
        triggerFirstFrame = false
    }

    override fun onTextureFrameAvailable(oesTextureId: Int, transformMatrix: FloatArray, timestampNs: Long) {
        super.onTextureFrameAvailable(oesTextureId, transformMatrix, timestampNs)
        val textureId = preProcess(oesTextureId)
        val orientation = getFrameOrientation()
        var newTransformMatrix = transformMatrix
        if (isFrontFacing()) {
            newTransformMatrix = RendererCommon.multiplyMatrices(transformMatrix, RendererCommon.horizontalFlipMatrix())
        }

        val render = outerRender
        render?.consumeTextureFrame(
            textureId,
            MediaIO.PixelFormat.TEXTURE_2D.intValue(),
            width,
            height,
            orientation,
            System.currentTimeMillis(),
            newTransformMatrix
        )

        val consumer = consumer
        if (consumer != null) {
            preDisplayRender = null
            val frameConsumer = consumer.get()
            frameConsumer?.consumeTextureFrame(
                textureId,
                MediaIO.PixelFormat.TEXTURE_2D.intValue(),
                width,
                height,
                orientation,
                System.currentTimeMillis(),
                newTransformMatrix
            )
        } else {
            preDisplayRender?.consumeTextureFrame(
                textureId,
                MediaIO.PixelFormat.TEXTURE_2D.intValue(),
                width,
                height,
                orientation,
                System.currentTimeMillis(),
                newTransformMatrix
            )
        }

        if (screenShotListener != null) {
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
            tmpBuffer.clear()
            screenShotListener?.invoke(processedImage)
            screenShotListener = null
        }
    }

    /**
     * 将相机返回的原始纹理经过美颜以及滤镜处理后转变成普通2D纹理，并继续分发后后续流程消费
     * @param oesTextureId 相机原始OES纹理
     */
    private fun preProcess(oesTextureId: Int): Int {

        if (filter == null) {
            Timber.w(TAG) { "init filter on preProcess" }
            initBaseFilter()
        }

        filter!!.onDrawWithBuffer(oesTextureId, vertexBuffer, if (isFrontFacing()) textureFrontBuffer else textureBuffer)

        var tId = filter!!.outputTexture

        lookupFilter!!.onDrawInFrameTexture(tId, vertexBuffer, textureBuffer2)

        tId = lookupFilter!!.outputTexture

        return tId
    }

    /**
     * 切换前后摄像头，默认启动时前置
     */
    fun switchCamera() {
        doAction("switchCamera") {
            cameraId = if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Camera.CameraInfo.CAMERA_FACING_BACK
            } else {
                Camera.CameraInfo.CAMERA_FACING_FRONT
            }
            releaseCamera()
            openCamera()
            startCameraPreview()
        }
    }


    fun applyFilter(filter: String, intensity: Float) {
        doAction("applyFilter") {
            lookupFilter?.apply {
                changeFilter(filter)
                setIntensity(intensity)
            }
        }
    }

    fun isFrontFacing(): Boolean {
        return pendingPreviewCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT
    }


    private fun openCamera() {
        cameraController = CameraController1(cameraId, this)
        Timber.i(TAG) { "openCamera ${cameraController?.hashCode()}" }
        setupCamera()
    }

    private fun startCameraPreview() {
        val controller = cameraController ?: return
        pendingPreviewCameraId = cameraId
        controller.startPreview()
    }

    private fun setupCamera() {
        val controller = cameraController!!
        try {

            controller.edgeMode = CameraController.EDGE_MODE_DEFAULT
            controller.noiseReductionMode = CameraController.NOISE_REDUCTION_MODE_DEFAULT
            controller.sceneMode = "auto"
            controller.antiBanding = "auto"
            controller.setManualISO(false, 0)
            controller.focusValue = "focus_mode_continuous_video"
            controller.setPreviewSize(width, height)
        } catch (e: Exception) {
            Timber.e(TAG, e) { "setupCamera" }
        }
        controller.setPreviewTexture(surfaceTexture)
        cameraOpened = true
    }

    private fun releaseCamera() {
        cameraOpened = false
        cameraController?.let { controller ->
            controller.stopPreview()
            controller.release()
        }
        Timber.i(TAG) { "releaseCamera ${cameraController?.hashCode()}" }
        cameraController = null
        screenShotListener = null
    }

    private fun getDeviceOrientation(): Int {
        val context = context ?: return 0
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    private fun getFrameOrientation(): Int {
        cameraController ?: return 0
        val cameraController = requireNotNull(cameraController)
        var orientation = getDeviceOrientation()
        if (!isFrontFacing()) {
            orientation = 360 - orientation
        }
        return (cameraController.cameraOrientation + orientation) % 360
    }

    private fun getCurrentOrientation(): Int {
        val dir = Accelerometer.getDirection()
        var orientation = dir - 1
        if (orientation < 0) {
            orientation = dir xor 3
        }
        return orientation
    }

    fun destroy() {
        doAction("destroy") {
            kotlin.runCatching {
                release()
            }
            filter?.destroy()
            lookupFilter?.destroy()
            beautyChicFilter?.destroy()
            filter = null
            lookupFilter = null
            beautyChicFilter = null
        }
    }

    override fun onError() {
        Timber.d(TAG) { "camera error" }
    }

    companion object {
        private const val TAG = "DCCameraSource"
    }

}