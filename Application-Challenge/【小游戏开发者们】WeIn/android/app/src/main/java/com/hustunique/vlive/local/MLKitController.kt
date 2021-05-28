package com.hustunique.vlive.local

import android.graphics.Bitmap
import android.media.Image
import android.media.ImageReader
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

@LocalGLThread
class MLKitController(
    private val onStateChange: (Float, Float, Float) -> Unit
) {

    private val detectOptions = FaceDetectorOptions.Builder()
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setMinFaceSize(10f)
        .build()
    private val detector = FaceDetection.getClient(detectOptions)
    private var mouthOpenWeight = 0f
    private var lEyeOpenWeight = 0f
    private var rEyeOpenWeight = 0f
    private val maxMouthOpen = 80.0

    fun stop() {
        detector.close()
    }

    private fun onProcess(faces: List<Face>) {
        faces.firstOrNull()?.let {
            lEyeOpenWeight = it.leftEyeOpenProbability ?: 0f
            rEyeOpenWeight = it.rightEyeOpenProbability ?: 0f

            val contourUpperPoints =
                it.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points ?: emptyList()
            val contourLowerPoints = it.getContour(FaceContour.LOWER_LIP_TOP)?.points ?: emptyList()
            val upperY = contourUpperPoints.sumByDouble { point -> point.y.toDouble() }
            val lowerY = contourLowerPoints.sumByDouble { point -> point.y.toDouble() }
            mouthOpenWeight = ((lowerY - upperY) / maxMouthOpen)
                .coerceIn(0.0, 1.0)
                .toFloat()
        }
        onStateChange(lEyeOpenWeight, rEyeOpenWeight, mouthOpenWeight)
        Log.d(
            TAG,
            "onProcess: \n\tlEye: $lEyeOpenWeight\n\trEye: $rEyeOpenWeight\n\tMouth: $mouthOpenWeight"
        )
    }

    private var processing = false
    private var bitmap: Bitmap? = null
    private fun process(image: Image) {
        processing = true

        val pixelStride: Int = image.planes[0].pixelStride
        val rowStride: Int = image.planes[0].rowStride
        val rowPadding: Int = rowStride - pixelStride * image.width
        val buffer = image.planes[0].buffer
        val w = image.width + rowPadding / pixelStride

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(w, image.height, Bitmap.Config.ARGB_8888)
        }
        bitmap?.copyPixelsFromBuffer(buffer)
        detector.process(InputImage.fromBitmap(bitmap!!, 0))
            .addOnSuccessListener { results -> onProcess(results) }
            .addOnFailureListener { e -> e.printStackTrace() }
            .addOnCompleteListener {
                image.close()
                processing = false
            }
    }

    fun onImageAvailable(reader: ImageReader) {
        reader.acquireNextImage()?.let { img ->
            if (!processing) process(img)
            else img.close()
        }
    }

    companion object {
        private const val TAG = "MLKitController"
    }
}