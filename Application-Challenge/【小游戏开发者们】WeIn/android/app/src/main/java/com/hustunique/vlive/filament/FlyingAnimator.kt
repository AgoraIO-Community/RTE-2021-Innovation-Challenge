package com.hustunique.vlive.filament

import android.view.animation.AccelerateDecelerateInterpolator
import com.hustunique.vlive.data.Vector3
import kotlin.math.abs
import kotlin.math.sqrt

class FlyingAnimator(
    private val src: Vector3,
    private val dst: Vector3,
    private val time: Float,
) {
    private var passed: Float = .0f
    private val deltaPos = dst - src
    private val interpolator = AccelerateDecelerateInterpolator()
    private val height = sqrt(deltaPos.norm())

    fun update(delta: Float): Vector3 {
        passed += delta
        if (over()) return dst
        val unitDelta = interpolator.getInterpolation(passed / time)
        return src + Vector3(
            deltaPos.x * unitDelta,
            height * (1 - abs(1f - 2 * unitDelta)),
            deltaPos.z * unitDelta,
        )
    }

    fun over(): Boolean {
        return passed >= time
    }
}