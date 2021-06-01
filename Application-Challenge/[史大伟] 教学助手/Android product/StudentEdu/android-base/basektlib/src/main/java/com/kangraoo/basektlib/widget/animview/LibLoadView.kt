package com.kangraoo.basektlib.widget.animview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

/**
 * 加载框,为框架独家开发
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2018/12/13
 * desc :
 * version: 1.0
 */
class LibLoadView(context: Context) : View(context), ILibAnimView {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG) // 绘图画笔

    init {
        mPaint.style = Paint.Style.FILL // 充满
        mPaint.isAntiAlias = true // 设置画笔的锯齿效果
        onCreateAnimators()
    }

    var degrees1 = 0f
    var degrees2 = 0f
    var degrees3 = 0f

    /**
     * 绘制
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width / 2.toFloat()
        val y = height / 2.toFloat()
        val circleSpacing = x * 0.3f
        canvas.translate(x, y)
        canvas.save()
        canvas.rotate(degrees1)
        canvasRectF(canvas, x, y, "#ff333333", circleSpacing)
        canvas.restore()
        canvas.save()
        canvas.rotate(degrees2)
        canvasRectF(canvas, x, y, "#CE7B1DF7", circleSpacing)
        canvas.restore()
        canvas.save()
        canvas.rotate(degrees3)
        canvasRectF(canvas, x, y, "#CB269DFF", circleSpacing)
        canvas.restore()
        mPaint.color = Color.parseColor("#ff333333")
        canvas.drawCircle(0f, 0f, x - circleSpacing, mPaint)
    }

    private fun canvasRectF(canvas: Canvas, x: Float, y: Float, color: String, circleSpacing: Float) {
        mPaint.color = Color.parseColor(color)
        val oval = RectF(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing)
        canvas.drawRoundRect(oval, circleSpacing, circleSpacing, mPaint)
    }

    var animSet: AnimatorSet? = null

    /**
     * 动画效果
     */
    override fun onCreateAnimators() {
        animSet = AnimatorSet()
        val rotateAnim = ValueAnimator.ofFloat(0f, 360f)
        rotateAnim.duration = 3000
        rotateAnim.repeatCount = -1
        rotateAnim.addUpdateListener { animation ->
            degrees1 = animation.animatedValue as Float
            invalidate()
        }
        val rotateAnim2 = ValueAnimator.ofFloat(0f, 360f)
        rotateAnim2.duration = 3000
        rotateAnim2.startDelay = 400
        rotateAnim2.repeatCount = -1
        rotateAnim2.addUpdateListener { animation -> degrees2 = animation.animatedValue as Float }
        val rotateAnim3 = ValueAnimator.ofFloat(0f, 360f)
        rotateAnim3.duration = 3000
        rotateAnim3.startDelay = 800
        rotateAnim3.repeatCount = -1
        rotateAnim3.addUpdateListener { animation -> degrees3 = animation.animatedValue as Float }
        animSet!!.play(rotateAnim).with(rotateAnim2).with(rotateAnim3)
    }

    @Synchronized
    override fun startAnimators() {
        if (animSet != null && !(animSet!!.isStarted || animSet!!.isRunning)) {
            animSet!!.start()
        }
    }

    override fun stopAnimators() {
        if (animSet != null) {
            animSet!!.end()
        }
    }

    override fun setVisibility(v: Int) {
        if (visibility != v) {
            super.setVisibility(v)
            if (v == GONE || v == INVISIBLE) {
                stopAnimators()
            } else {
                startAnimators()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimators()
        } else {
            startAnimators()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimators()
    }

    override fun onDetachedFromWindow() {
        stopAnimators()
        super.onDetachedFromWindow()
    }
}
