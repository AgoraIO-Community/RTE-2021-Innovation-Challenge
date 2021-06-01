package com.hustunique.vlive.filament.model_object

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 5/3/21
 */
class VFaceModelObject : FilamentBaseModelObject("models/nfface.glb") {
    private var eyeEntity = 0
    private var mouthEntity = 0

    override fun onAssetSet() {
        eyeEntity = asset?.getFirstEntityByName("HyperNURBS_6C4dObjectSymmetry_6Polygon") ?: 0
        mouthEntity = asset?.getFirstEntityByName("HyperNURBS") ?: 0
    }

    private fun setAnim(frameTimeNanos: Long) {
        val elapsedTimeSeconds = frameTimeNanos.toDouble() / 1_000_000_000
        filamentContext?.getRenderableManager()?.run {
            setMorphWeights(
                getInstance(eyeEntity),
                floatArrayOf(
                    abs(sin(elapsedTimeSeconds * Math.PI)).toFloat(),
                    abs(cos(elapsedTimeSeconds * Math.PI)).toFloat(),
                    0f,
                    0f
                )
            )
            setMorphWeights(
                getInstance(mouthEntity),
                floatArrayOf(abs(sin(elapsedTimeSeconds * Math.PI)).toFloat(), 0f, 0f, 0f)
            )
        }
    }

    override fun update(frameTimeNanos: Long) {
        setAnim(frameTimeNanos)
    }
}