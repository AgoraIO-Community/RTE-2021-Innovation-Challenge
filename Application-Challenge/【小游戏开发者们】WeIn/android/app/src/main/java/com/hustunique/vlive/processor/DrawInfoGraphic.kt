package com.hustunique.vlive.processor

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class DrawInfoGraphic(
    val overlay: GraphicOverlay,
    val frameLatency: Long,
    val detectorLatency: Long,
    val framesPerSecond: Int,
) :
    GraphicOverlay.Graphic(overlay) {

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = TEXT_SIZE
        setShadowLayer(5.0f, 0f, 0f, Color.BLACK)
    }

    @Synchronized
    override fun draw(canvas: Canvas?) {
        val x: Float = TEXT_SIZE * 0.5f
        val y: Float = TEXT_SIZE * 1.5f

        canvas?.drawText(
            "InputImage size: ${overlay.getImageWidth()} x ${overlay.getImageHeight()}",
            x,
            y,
            textPaint
        )
        // Draw FPS (if valid) and inference latency
        canvas?.drawText(
            "FPS: $framesPerSecond, Frame latency: $frameLatency ms",
            x,
            y + TEXT_SIZE,
            textPaint
        )
        canvas?.drawText(
            "Detector latency: $detectorLatency ms",
            x,
            y + TEXT_SIZE * 2,
            textPaint
        )
    }

    companion object {
        private const val TEXT_SIZE = 60.0f
    }
}