package com.hustunique.vlive.processor

import androidx.camera.core.ImageProxy

interface VisionImageProcessor {

    fun processImageProxy(image: ImageProxy, graphicOverlay: GraphicOverlay)

    fun stop()
}