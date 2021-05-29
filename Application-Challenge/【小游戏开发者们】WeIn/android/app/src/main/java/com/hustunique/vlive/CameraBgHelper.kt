package com.hustunique.vlive

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.HardwareBuffer
import android.media.ImageReader
import android.opengl.Matrix
import android.os.Build
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.Surface
import androidx.annotation.RequiresApi
import com.google.android.filament.*

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 4/30/21
 */
class CameraBgHelper(
    private val filamentEngine: Engine,
    private val filamentMaterial: MaterialInstance,
    private val display: Display,
) {

    companion object {
        private const val TAG = "CameraBgHelper"
        private const val kImageReaderMaxImages = 7
    }

    private var resolution = Size(640, 480)
    private var filamentTexture: Texture? = null
    private var filamentStream: Stream? = null

    var inTexture: Int = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    private val imageReader = ImageReader.newInstance(
        resolution.width,
        resolution.height,
        ImageFormat.PRIVATE,
        kImageReaderMaxImages,
        HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE
    )

    val surface: Surface
        get() = imageReader.surface

    var surfaceTexture: SurfaceTexture? = null


    fun initHelper() {
//        surfaceTexture = SurfaceTexture(inTexture)
        filamentStream = Stream.Builder()
//            .stream(surfaceTexture!!)
            .stream(inTexture.toLong())
            .width(640)
            .height(480)
            .build(filamentEngine)
        filamentTexture = Texture.Builder()
            .sampler(Texture.Sampler.SAMPLER_EXTERNAL)
            .format(Texture.InternalFormat.RGB8)
            .build(filamentEngine)

        val sampler = TextureSampler(
            TextureSampler.MinFilter.LINEAR,
            TextureSampler.MagFilter.LINEAR,
            TextureSampler.WrapMode.CLAMP_TO_EDGE
        )

        val aspectRatio = resolution.width.toFloat() / resolution.height.toFloat()
        val textureTransform = FloatArray(16)
        Matrix.setIdentityM(textureTransform, 0)
        when (display.rotation) {
            Surface.ROTATION_180 -> {
                Matrix.translateM(textureTransform, 0, 1.0f, 0.0f, 0.0f)
                Matrix.rotateM(textureTransform, 0, 90.0f, 0.0f, 0.0f, 1.0f)
                Matrix.translateM(textureTransform, 0, 1.0f, 0.0f, 0.0f)
                Matrix.scaleM(textureTransform, 0, -1.0f, 1.0f / aspectRatio, 1.0f)
            }
            Surface.ROTATION_270 -> {
                Matrix.translateM(textureTransform, 0, 1.0f, 1.0f, 0.0f)
                Matrix.rotateM(textureTransform, 0, 180.0f, 0.0f, 0.0f, 1.0f)
                Matrix.translateM(textureTransform, 0, 1.0f, 0.0f, 0.0f)
                Matrix.scaleM(textureTransform, 0, -1.0f / aspectRatio, 1.0f, 1.0f)
            }
            Surface.ROTATION_90 -> {
                Matrix.translateM(textureTransform, 0, 1.0f, 0.0f, 0.0f)
                Matrix.scaleM(textureTransform, 0, -1.0f / aspectRatio, 1.0f, 1.0f)
            }
        }

        filamentTexture!!.setExternalStream(filamentEngine, filamentStream!!)
        filamentMaterial.setParameter("videoTexture", filamentTexture!!, sampler)
        filamentMaterial.setParameter("textureTransform", MaterialInstance.FloatElement.MAT4, textureTransform, 0, 1)
    }

    fun pushExternalImageToFilament() {
//        val stream = filamentStream
//        if (stream != null && imageReader.maxImages > 0) {
//            Log.i(TAG, "pushExternalImageToFilament: ")
//            imageReader.acquireLatestImage()?.also {
//                stream.setAcquiredImage(it.hardwareBuffer, Handler()) {
//                    Log.i(TAG, "pushExternalImageToFilament: close")
//                    it.close()
//                }
//            }
//        }
    }
}