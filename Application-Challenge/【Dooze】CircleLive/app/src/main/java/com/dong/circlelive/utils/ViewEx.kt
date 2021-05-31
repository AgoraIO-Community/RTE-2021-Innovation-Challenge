package com.dong.circlelive.utils

import android.content.Context
import android.graphics.Outline
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.view.forEach
import com.dong.circlelive.appContext
import kotlin.math.roundToInt

/**
 * Create by dooze on 2021/5/14  11:51 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

fun View.roundCorner(
    w: Int? = null,
    h: Int? = null,
    radius: Float? = null,
    paddingStart: Int? = null,
    paddingTop: Int? = null,
    paddingEnd: Int? = null,
    paddingBottom: Int? = null,
    isCircle: Boolean = false,
    circleRound: Boolean = false,
) {

    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            if (isCircle) {
                val r = if (width > height) height else width
                val start = paddingStart ?: getPaddingStart() + (width - r) / 2
                val top = paddingTop ?: getPaddingTop() + (height - r) / 2
                val right = (w ?: width) - (paddingEnd ?: getPaddingEnd()) - (width - r) / 2
                val bottom = (h ?: height) - (paddingBottom ?: getPaddingBottom()) - (height - r) / 2

                outline?.setOval(
                    start,
                    top,
                    right,
                    bottom
                )
            } else {
                val realRadius = if (circleRound) {
                    height / 2f
                } else {
                    radius ?: 15.dpF(context)
                }
                outline?.setRoundRect(
                    paddingStart ?: getPaddingStart(),
                    paddingTop ?: getPaddingTop(),
                    (w ?: width) - (paddingEnd ?: getPaddingEnd()),
                    (h ?: height) - (paddingBottom ?: getPaddingBottom()),
                    realRadius
                )
            }
        }
    }
    this.clipToOutline = true
}

fun View.cornerOutlineProvider(
    radius: Float,
    leftTop: Boolean = false,
    rightTop: Boolean = false,
    leftBottom: Boolean = false,
    rightBottom: Boolean = false
): ViewOutlineProvider {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val w = view.width
            val h = view.height
            if (w == 0 || h == 0) {
                return
            }
            var left = 0
            var right = w
            var top = 0
            var bottom = h
            if (leftTop && rightTop) {
                left = 0
                right = w
            } else if (leftTop) {
                right = (w + radius).toInt()
                bottom = (h + radius).toInt()
            } else if (rightTop) {
                left = (-radius).toInt()
                bottom = (h + radius).toInt()
            }
            if (leftBottom && rightBottom) {
                right = w
                left = 0
            } else if (leftBottom) {
                top = (-radius).toInt()
                right = (w + radius).toInt()
            } else if (rightBottom) {
                left = if (leftTop) 0 else (-radius).toInt()
                top = (-radius).toInt()
            }
            outline.setRoundRect(left, top, right, bottom, radius)
        }
    }
    return outlineProvider
}



fun View.updateViewHeight(height: Int) {
    val lp = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    if (layoutParams == null || lp.height != height) {
        lp.height = height
        layoutParams = lp
    }
}

fun View.updateViewSize(width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {
    val lp = layoutParams ?: ViewGroup.LayoutParams(width, height)
    if (layoutParams == null || lp.height != height || lp.width != width) {
        lp.height = height
        lp.width = width
        layoutParams = lp
    }
}

fun View.removeFromParent() {
    (parent as? ViewGroup)?.removeView(this)
}

fun ViewGroup.find(predicate: (view: View) -> Boolean): View? {
    forEach {
        if (predicate.invoke(it)) return it
    }
    return null
}


fun Int.dp(context: Context = appContext): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
).roundToInt()

fun Int.dpF(context: Context = appContext): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)

fun Float.dp(context: Context = appContext): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
).roundToInt()

fun Float.dpF(context: Context = appContext): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
)

fun Int.sp(context: Context = appContext): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this.toFloat(),
    context.resources.displayMetrics
)

fun Float.sp(context: Context = appContext): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)