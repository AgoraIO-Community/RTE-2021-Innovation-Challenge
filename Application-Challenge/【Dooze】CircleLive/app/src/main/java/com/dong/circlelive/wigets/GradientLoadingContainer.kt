package com.dong.circlelive.wigets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.withRotation
import com.dong.circlelive.R
import com.dong.circlelive.base.lazyFast
import com.dong.circlelive.getColor

open class GradientLoadingContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private var startGradientColor = getColor(R.color.colorAccent, context)
    private var endGradientColor = getColor(R.color.ui_green_tertiary, context)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint by lazyFast { Paint(Paint.ANTI_ALIAS_FLAG) }

    private var gradientRotate = 0f
    private var curAnimator: Animator? = null
    private var needAnimation = true

    private var shader: Shader? = null

    private var borderWidth = 0f
    private var borderColor: Int = Color.TRANSPARENT
    private var borderRadius: Float = 0f

    override fun dispatchDraw(canvas: Canvas) {
        paint.shader = shader
        canvas.withRotation(gradientRotate, width / 2f, height / 2f) {
            drawPaint(paint)
        }

        if (borderWidth > 0) {
            borderPaint.strokeWidth = borderWidth
            borderPaint.color = borderColor
            borderPaint.style = Paint.Style.STROKE
            canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), borderRadius, borderRadius, borderPaint)
        }
        super.dispatchDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shader = LinearGradient(
            w / 2f, 0f, w / 2f, h.toFloat(),
            startGradientColor, endGradientColor, Shader.TileMode.CLAMP
        )
    }

    fun updateBorder(borderWidth: Float, borderColor: Int, radius: Float) {
        this.borderColor = borderColor
        this.borderWidth = borderWidth
        borderRadius = radius
    }

    fun updateGradientColor(startColor: Int, endColor: Int) {
        startGradientColor = startColor
        endGradientColor = endColor
        shader = LinearGradient(
            width / 2f, 0f, width / 2f, height.toFloat(),
            startGradientColor, endGradientColor, Shader.TileMode.CLAMP
        )
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation(isAttach = true)
    }

    fun startAnimation(isAttach: Boolean = false) {
        if (isAttach && !needAnimation) {
            curAnimator?.cancel()
            return
        }
        needAnimation = true
        val anim = ValueAnimator.ofFloat(0f, 360f)
        anim.duration = 5000L
        anim.addUpdateListener {
            gradientRotate = it.animatedValue as Float
            invalidate()
        }
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = ValueAnimator.INFINITE
        anim.repeatMode = ValueAnimator.RESTART
        anim.start()
        curAnimator = anim
    }

    fun stopAnimation() {
        needAnimation = false
        curAnimator?.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        curAnimator?.cancel()
        curAnimator = null
    }
}