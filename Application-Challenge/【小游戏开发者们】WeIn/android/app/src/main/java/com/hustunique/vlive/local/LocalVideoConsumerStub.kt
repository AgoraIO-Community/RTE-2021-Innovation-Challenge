package com.hustunique.vlive.local

import android.graphics.Bitmap
import android.media.Image
import android.util.Log

class LocalVideoConsumerStub(
    private val type: LocalVideoType,
) : LocalVideoSink {
    val bitmap = Bitmap.createBitmap(480, 640, Bitmap.Config.ARGB_8888)

    override fun onFrame(image: Image) {
        Log.i(TAG, "onFrame: ${image.format} ${image.width} ${image.height}")
    }

    override fun onPropertyReady(property: CharacterProperty) {
        Log.i(TAG, "onMatrixReady: ${property.lEyeOpenProbability} ${property.rEyeOpenProbability} ${property.mouthOpenWeight}")
    }

    override fun getConsumeType() = type

    companion object {
        private const val TAG = "LocalVideoConsumerStub"
    }
}