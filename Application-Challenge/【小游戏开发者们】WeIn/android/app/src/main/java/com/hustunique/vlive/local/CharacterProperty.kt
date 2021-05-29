package com.hustunique.vlive.local

import java.nio.ByteBuffer
import java.nio.FloatBuffer

// Properties for virtual characters
data class CharacterProperty(
    val lEyeOpenProbability: Float,
    val rEyeOpenProbability: Float,
    val mouthOpenWeight: Float,
    val faceQuaternion: FloatBuffer, // 4 * float
    var objectData: FloatBuffer, // rotation data + position data
) {

    companion object {
        fun fromArray(array: ByteArray): CharacterProperty = let {
            val bf = ByteBuffer.wrap(array)
            CharacterProperty(
                bf.float,
                bf.float,
                bf.float,
                FloatBuffer.wrap(FloatArray(4) { bf.float }),
                FloatBuffer.wrap(FloatArray(7) { bf.float })
            )
        }

        fun empty(): CharacterProperty {
            return CharacterProperty(
                1f,
                1f,
                1f,
                FloatBuffer.allocate(4),
                FloatBuffer.allocate(7),
            )
        }
    }

    fun toByteArray(): ByteArray =
        ByteBuffer.allocate(14 * 4).apply {
            putFloat(lEyeOpenProbability)
            putFloat(rEyeOpenProbability)
            putFloat(mouthOpenWeight)
            faceQuaternion.array().forEach {
                putFloat(it)
            }
            objectData.array().forEach {
                putFloat(it)
            }
        }.array()
}