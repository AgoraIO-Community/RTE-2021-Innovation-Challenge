package com.hustunique.vlive.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hustunique.vlive.R
import kotlin.math.*

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/25
 */
class RockerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "Rocker"

        const val MODE_NONE = 0
        const val MODE_INNER = 1
        const val MODE_OUTER = 2
    }

    private val bgColor: Int
    private val fgColor: Int
    private val radiusDiff: Float
    private val arcWidth: Float
    private val arcGap: Float
    private val smallCircleRadius: Float

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RockerView).run {
            bgColor = getColor(R.styleable.RockerView_bg_color, Color.RED)
            fgColor = getColor(R.styleable.RockerView_fg_color, Color.BLUE)
            radiusDiff = getDimension(R.styleable.RockerView_radius_diff, 0f)
            arcWidth = getDimension(R.styleable.RockerView_outer_line_width, 20f)
            arcGap = getDimension(R.styleable.RockerView_outer_line_gap, 20f)
            smallCircleRadius = getDimension(R.styleable.RockerView_outer_circle_radius, 20f)
            recycle()
        }
    }

    private val arcOffset = arcGap + arcWidth
    private val outGap =
        if (smallCircleRadius > arcWidth / 2f) smallCircleRadius - arcWidth / 2f else 0f

    private val bgPaint = Paint().apply {
        isAntiAlias = true
        color = bgColor
    }

    private val fgPaint = Paint().apply {
        isAntiAlias = true
        color = fgColor
    }

    private val pathPaint = Paint().apply {
        isAntiAlias = true
        color = bgColor
        style = Paint.Style.STROKE
        strokeWidth = arcWidth
        strokeCap = Paint.Cap.ROUND
    }

    private var curMode = MODE_NONE


    private val centerPos = PointF()
    private val curPos = PointF()
    private val circlePos = PointF()
    private var curPosRadius = 0f
    private var circleBgRadius = 0f
    private var smallCirclePathRadius = 0f

    // radians, progress
    var onUpdate: (Float, Float, Float) -> Unit = { _, _, _ -> }

    var enable = true
        set(value) {
            visibility = if (value) {
                VISIBLE
            } else {
                GONE
            }
            field = value
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerPos.x = measuredWidth / 2f
        centerPos.y = measuredHeight / 2f
        circleBgRadius = centerPos.x.coerceAtMost(centerPos.y) - arcOffset - outGap
        curPosRadius = centerPos.x - radiusDiff
        curPos.set(centerPos)
        smallCirclePathRadius = centerPos.x - arcWidth / 2f - outGap
        setSmallCycle((PI / 4f).toFloat())
    }

    private val arcPath by lazy {
        Path().apply {
            val a = arcWidth / 2f + outGap
            arcTo(a, a, measuredWidth - a, measuredHeight - a, 0f, -90f, true)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawPath(arcPath, pathPaint)
            centerPos.drawCircle(canvas, bgPaint, offset = arcOffset + outGap)
            curPos.drawCircle(canvas, fgPaint, curPosRadius, offset = arcOffset + outGap)
            circlePos.drawCircle(canvas, fgPaint, arcWidth)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!enable) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if ((PointF(event.x, event.y) - centerPos).distance() > circleBgRadius
                    && event.x > centerPos.x
                    && event.y < centerPos.y
                ) {
                    curMode = MODE_OUTER
                } else {
                    curMode = MODE_INNER
                }
            }
            MotionEvent.ACTION_MOVE -> {
                when (curMode) {
                    MODE_INNER -> calculateCurPos(event.x, event.y)
                    MODE_OUTER -> calculateSmallCircle(event.x, event.y)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                curPos.set(centerPos)
                setSmallCycle((PI / 4f).toFloat())
                onUpdate(0f, 0f, 0f)
                invalidate()
                curMode = MODE_NONE
            }
        }
        return enable
    }

    private fun calculateSmallCircle(x: Float, y: Float) {
        val b = y - x
        val progress = when {
            y > measuredHeight || x > measuredHeight -> 1f
            x < 0 || y < 0 -> -1f
            b > measuredHeight || b < -measuredHeight -> 0f
            b >= 0 -> (x / (measuredHeight - b) - 0.5f) * 2f
            b < 0 -> (y / (measuredHeight + b) - 0.5f) * 2f
            else -> 0f
        }
        setSmallCycle(atan(2f / (progress + 1f) - 1))
        onUpdate(0f, 1f, progress)
    }

    private fun setSmallCycle(radians: Float) {
        circlePos.set(
            centerPos.x + smallCirclePathRadius * cos(radians),
            centerPos.y - smallCirclePathRadius * sin(radians)
        )
    }

    private fun calculateCurPos(x: Float, y: Float) {
        val p = PointF(x, y) - centerPos
        val radians = getRadians(p) * if (y > centerPos.y) -1 else 1
        val progress = p.distance() / radiusDiff
        if (progress < 1f) {
            curPos.set(x, y)
            onUpdate(radians, progress, 0f)
        } else {
            val offset = PointF(radiusDiff * cos(radians), radiusDiff * sin(radians))
            curPos.set(centerPos.x + offset.x, centerPos.y - offset.y)
            onUpdate(radians, 1f, 0f)
        }
    }

    private fun PointF.drawCircle(
        canvas: Canvas,
        paint: Paint,
        radius: Float = x.coerceAtMost(y),
        offset: Float = 0f
    ) {
        canvas.drawCircle(x, y, radius - offset, paint)
    }


    private val zeroVec = PointF(1f, 0f)

    private fun getRadians(p: PointF): Float = acos(p.dot(zeroVec) / p.distance())

    private operator fun PointF.minus(p: PointF): PointF = PointF(x - p.x, y - p.y)

    private fun PointF.dot(p: PointF): Float = x * p.x + x * p.y

    private fun PointF.distance(): Float = sqrt(x * x + y * y)
}