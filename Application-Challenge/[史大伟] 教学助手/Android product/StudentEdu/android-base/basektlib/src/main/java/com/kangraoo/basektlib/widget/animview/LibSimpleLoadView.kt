package com.kangraoo.basektlib.widget.animview

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.task.TaskManager

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/10/16
 * desc :
 * version: 1.0
 */
class LibSimpleLoadView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr), ILibAnimView {
    private var radius = 0f
    private var radiusOffset = 0f

    // 不是固定不变的，当width为30dp时，它为2dp，当宽度变大，这个也会相应的变大
    private var stokeWidth = 2f
    private val argbEvaluator = ArgbEvaluator()
    private val startColor = Color.parseColor("#CCCCCC")
    private val endColor = Color.parseColor("#333333")
    var lineCount = 12 // 共12条线

    var avgAngle = 360f / lineCount

    @Volatile
    private var time = 0 // 重复次数

    var centerX = 0f
    var centerY = 0f // 中心x，y
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.isAntiAlias = true // 设置画笔的锯齿效果
        stokeWidth = UUi.dp2px(SApplication.context(), stokeWidth).toFloat()
        paint.strokeWidth = stokeWidth
        onCreateAnimators()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = measuredWidth / 2.toFloat()
        radiusOffset = radius / 2.5f
        centerX = measuredWidth / 2.toFloat()
        centerY = measuredHeight / 2.toFloat()
        stokeWidth *= measuredWidth * 1f / UUi.dp2px(SApplication.context(), 30f)
        paint.strokeWidth = stokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        // 1 2 3 4 5
        // 2 3 4 5 1
        // 3 4 5 1 2
        // ...
        for (i in lineCount - 1 downTo 0) {
            val temp = Math.abs(i + time) % lineCount
            val fraction = (temp + 1) * 1f / lineCount
            val color = argbEvaluator.evaluate(fraction, startColor, endColor) as Int
            paint.setColor(color)
            val startX = centerX + radiusOffset
            val endX = startX + radius / 3f
            canvas.drawLine(startX, centerY, endX, centerY, paint)
            // 线的两端画个点，看着圆滑
            canvas.drawCircle(startX, centerY, stokeWidth / 2, paint)
            canvas.drawCircle(endX, centerY, stokeWidth / 2, paint)
            canvas.rotate(avgAngle, centerX, centerY)
        }
//        postDelayed(increaseTask, 80);
    }

    private var increaseTask: Runnable? = null

    override fun onCreateAnimators() {
        increaseTask = Runnable {
            while (run) {
                time++
                val m = mz
                postInvalidate()
                try {
                    Thread.sleep(80)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (m != mz) {
                    break
                }
            }
        }
    }

    @Volatile
    var run = false

    @Volatile
    var mz = 0

    @Synchronized
    override fun startAnimators() {
        if (increaseTask != null && !run) {
            run = true
            mz++
            TaskManager.taskExecutor.execute(increaseTask!!)
        }
    }

    override fun stopAnimators() {
        if (increaseTask != null) {
            run = false
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
