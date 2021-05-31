package com.hustunique.vlive.local

import android.media.Image

interface LocalVideoSink {
    /**
     * Each time device's camera data is generated,
     * this function will be called (when consumer prefer REALITY)
     * Image.format == YUV_420_888
     */
    fun onFrame(image: Image)

    /**
     * Each time model's matrix is generated,
     * this function will be called (when consumer prefer VIRTUAL)
     */
    fun onPropertyReady(property: CharacterProperty)

    /**
     * Get what type this object want to consume
     * if it is REALITY, onFrame will be called
     * otherwise onMatrixReady will be called
     *
     * @see LocalVideoType
     */
    fun getConsumeType(): LocalVideoType
}

