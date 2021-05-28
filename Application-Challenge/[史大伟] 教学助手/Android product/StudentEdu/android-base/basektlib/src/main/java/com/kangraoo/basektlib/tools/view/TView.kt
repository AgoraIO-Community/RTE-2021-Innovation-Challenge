package com.kangraoo.basektlib.tools.view

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.widget.TextView
import androidx.annotation.IntDef
import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/05
 * desc :
 * version: 1.0
 */
object TView {
    const val LEFT = 1
    const val TOP = 2
    const val RIGHT = 4
    const val BOTTOM = 8
    fun buildTextDrawable(
        textView: TextView,
        drawable: Drawable,
        textViewDrawableArray: TypedArray
    ) {
        if (textViewDrawableArray.hasValue(R.styleable.LibTextViewDrawable_lib_position)) {
            buildTextDrawable(
                textView,
                drawable,
                textViewDrawableArray.getInt(R.styleable.LibTextViewDrawable_lib_position, 0)
            )
        }
    }

    fun buildTextDrawable(
        textView: TextView,
        drawable: Drawable,
        @TextViewDrawableType posion: Int
    ) {
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        when (posion) {
            LEFT -> textView.setCompoundDrawables(drawable, null, null, null)
            TOP -> textView.setCompoundDrawables(null, drawable, null, null)
            RIGHT -> textView.setCompoundDrawables(null, null, drawable, null)
            BOTTOM -> textView.setCompoundDrawables(null, null, null, drawable)
        }
    }

    @JvmStatic
    fun buildLayer(
        shapeArray: TypedArray,
        stokePosition: TypedArray,
        drawable: Drawable?
    ): Drawable? {
        var drawable = drawable
        if (shapeArray.hasValue(R.styleable.LibShape_lib_stroke_width) && stokePosition.hasValue(R.styleable.LibStokePosition_lib_stroke_position)) {
            val width = shapeArray.getDimension(R.styleable.LibShape_lib_stroke_width, 0f)
            val position = stokePosition.getInt(R.styleable.LibStokePosition_lib_stroke_position, 0)
            val left = 1 shl 1
            val top = 1 shl 2
            val right = 1 shl 3
            val bottom = 1 shl 4
            val leftValue = if (hasStatus(position, left)) width else -width
            val topValue = if (hasStatus(position, top)) width else -width
            val rightValue = if (hasStatus(position, right)) width else -width
            val bottomValue = if (hasStatus(position, bottom)) width else -width
            drawable = LayerDrawable(arrayOf(drawable))
            drawable.setLayerInset(
                0,
                leftValue.toInt(),
                topValue.toInt(),
                rightValue.toInt(),
                bottomValue.toInt()
            )
        }
        return drawable
    }

    private fun hasStatus(flag: Int, status: Int): Boolean {
        return flag and status == status
    }

    @IntDef(LEFT, TOP, RIGHT, BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TextViewDrawableType
}
