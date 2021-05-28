package com.hustunique.vlive.processor

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import com.hustunique.vlive.processor.VisionProcessorBase.Companion.MANUAL_TESTING_LOG
import java.util.*

class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
    VisionProcessorBase<List<Face>>(context) {

    private val detector: FaceDetector

    init {
        val options = detectorOptions
            ?: FaceDetectorOptions.Builder()
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build()
        detector = FaceDetection.getClient(options)
    }

    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun onSuccess(results: List<Face>, graphicOverlay: GraphicOverlay) {
        Log.i(TAG, "onSuccess: ")
        results.forEach {
            graphicOverlay.add(FaceGraphic(graphicOverlay, it))
            it.logForTest()
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }
}

fun Face.logForTest() {
    Log.v(
        MANUAL_TESTING_LOG,
        "face bounding box: " + boundingBox.flattenToString()
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face Euler Angle X: $headEulerAngleX"
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face Euler Angle Y: $headEulerAngleY"
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face Euler Angle Z: $headEulerAngleZ"
    )
    // All landmarks
    val landMarkTypes = intArrayOf(
        FaceLandmark.MOUTH_BOTTOM,
        FaceLandmark.MOUTH_RIGHT,
        FaceLandmark.MOUTH_LEFT,
        FaceLandmark.RIGHT_EYE,
        FaceLandmark.LEFT_EYE,
        FaceLandmark.RIGHT_EAR,
        FaceLandmark.LEFT_EAR,
        FaceLandmark.RIGHT_CHEEK,
        FaceLandmark.LEFT_CHEEK,
        FaceLandmark.NOSE_BASE
    )
    val landMarkTypesStrings = arrayOf(
        "MOUTH_BOTTOM",
        "MOUTH_RIGHT",
        "MOUTH_LEFT",
        "RIGHT_EYE",
        "LEFT_EYE",
        "RIGHT_EAR",
        "LEFT_EAR",
        "RIGHT_CHEEK",
        "LEFT_CHEEK",
        "NOSE_BASE"
    )
    for (i in landMarkTypes.indices) {
        val landmark = getLandmark(landMarkTypes[i])
        if (landmark == null) {
            Log.v(
                MANUAL_TESTING_LOG,
                "No landmark of type: " + landMarkTypesStrings[i] + " has been detected"
            )
        } else {
            val landmarkPosition = landmark.position
            val landmarkPositionStr =
                String.format(Locale.US, "x: %f , y: %f", landmarkPosition.x, landmarkPosition.y)
            Log.v(
                MANUAL_TESTING_LOG,
                "Position for face landmark: " +
                        landMarkTypesStrings[i] +
                        " is :" +
                        landmarkPositionStr
            )
        }
    }
    Log.v(
        MANUAL_TESTING_LOG,
        "face left eye open probability: $leftEyeOpenProbability"
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face right eye open probability: $rightEyeOpenProbability"
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face smiling probability: $smilingProbability"
    )
    Log.v(
        MANUAL_TESTING_LOG,
        "face tracking id: $trackingId"
    )
}
