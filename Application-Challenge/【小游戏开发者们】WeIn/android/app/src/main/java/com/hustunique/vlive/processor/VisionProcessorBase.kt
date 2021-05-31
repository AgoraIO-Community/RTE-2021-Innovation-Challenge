package com.hustunique.vlive.processor

import android.app.ActivityManager
import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.hustunique.vlive.ScopedExecutor
import java.util.*
import kotlin.math.max
import kotlin.math.min

abstract class VisionProcessorBase<T>(context: Context) : VisionImageProcessor {

    private var activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val fpsTimer = Timer()
    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

    private var isShutdown = false

    private var numRuns = 0
    private var totalFrameMs = 0L
    private var maxFrameMs = 0L
    private var minFrameMs = Long.MAX_VALUE
    private var totalDetectorMs = 0L
    private var maxDetectorMs = 0L
    private var minDetectorMs = Long.MAX_VALUE

    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

    init {
        fpsTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    framesPerSecond = frameProcessedInOneSecondInterval
                    frameProcessedInOneSecondInterval = 0
                }
            },
            0,
            1000
        )
    }

    override fun processImageProxy(image: ImageProxy, graphicOverlay: GraphicOverlay) {
        if (isShutdown) return
        val frameStartMs = SystemClock.elapsedRealtime()

        requestDetectInImage(
            InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees),
            graphicOverlay,
            frameStartMs
        ).addOnCompleteListener { image.close() }
    }

    private fun requestDetectInImage(
        image: InputImage,
        graphicOverlay: GraphicOverlay,
        frameStartMs: Long
    ): Task<T> {
        val detectorStartMs = SystemClock.elapsedRealtime()
        return detectInImage(image).addOnSuccessListener(executor) { results: T ->
            val endMs = SystemClock.elapsedRealtime()
            val currentFrameLatencyMs = endMs - frameStartMs
            val currentDetectorLatencyMs = endMs - detectorStartMs
            if (numRuns >= 500) {
                resetLatencyStats()
            }
            numRuns++
            frameProcessedInOneSecondInterval++
            totalFrameMs += currentFrameLatencyMs
            maxFrameMs = max(currentFrameLatencyMs, maxFrameMs)
            minFrameMs = min(currentFrameLatencyMs, minFrameMs)
            totalDetectorMs += currentDetectorLatencyMs
            maxDetectorMs = max(currentDetectorLatencyMs, maxDetectorMs)
            minDetectorMs = min(currentDetectorLatencyMs, minDetectorMs)

            printOncePerSecond()

            graphicOverlay.clear()
            graphicOverlay.add(
                DrawInfoGraphic(
                    graphicOverlay,
                    currentFrameLatencyMs,
                    currentDetectorLatencyMs,
                    framesPerSecond
                )
            )
            this@VisionProcessorBase.onSuccess(results, graphicOverlay)
            graphicOverlay.postInvalidate()
        }
            .addOnFailureListener(executor) { e: Exception ->
                graphicOverlay.clear()
                graphicOverlay.postInvalidate()
                Toast.makeText(
                    graphicOverlay.context,
                    "Failed to process.\nError: " +
                            e.localizedMessage +
                            "\nCause: " +
                            e.cause,
                    Toast.LENGTH_LONG
                )
                    .show()
                e.printStackTrace()
                this@VisionProcessorBase.onFailure(e)
            }
    }

    override fun stop() {
        if (isShutdown) return
        isShutdown = true
        resetLatencyStats()
        fpsTimer.cancel()
        executor.shutdown()
    }

    private fun resetLatencyStats() {
        numRuns = 0
        totalFrameMs = 0
        maxFrameMs = 0
        minFrameMs = Long.MAX_VALUE
        totalDetectorMs = 0
        maxDetectorMs = 0
        minDetectorMs = Long.MAX_VALUE
    }

    private fun printOncePerSecond() {
        if (frameProcessedInOneSecondInterval != 1) return
        Log.d(TAG, "Num of Runs: $numRuns")
        Log.d(
            TAG,
            "Frame latency: max=$maxFrameMs, min=$minFrameMs, avg=" +
                    (totalFrameMs / numRuns)
        )
        Log.d(
            TAG,
            "Detector latency: max=$maxDetectorMs, min=$minDetectorMs, avg=" +
                    (totalDetectorMs / numRuns)
        )
        val mi = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(mi)
        val availableMegs = mi.availMem / 0x100000L
        Log.d(TAG, "Memory available in system: $availableMegs MB")
    }

    protected abstract fun detectInImage(image: InputImage): Task<T>

    protected abstract fun onSuccess(results: T, graphicOverlay: GraphicOverlay)

    protected abstract fun onFailure(e: Exception)

    companion object {
        private const val TAG = "VisionProcessorBase"
        const val MANUAL_TESTING_LOG = "LogTagForTest"
    }
}