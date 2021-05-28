package com.kangraoo.basektlib.tools.view.statebuilder

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
open class BaseBuilder<T : BaseBuilder<T>> {
    private var rippleEnable = false
    private var rippleColor: Int? = null
    fun setRipple(rippleEnable: Boolean, rippleColor: Int): T {
        this.rippleEnable = rippleEnable
        this.rippleColor = rippleColor
        return this as T
    }

    fun setRippleTypeArray(typedArray: TypedArray): T {
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.LibRipple_lib_ripple_enable) {
                rippleEnable = typedArray.getBoolean(attr, false)
            } else if (attr == R.styleable.LibRipple_lib_ripple_color) {
                rippleColor = typedArray.getColor(attr, 0)
            }
        }
        return this as T
    }

    fun buildRipple(drawable: Drawable): Drawable {
        if (rippleEnable && rippleColor != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return RippleDrawable(ColorStateList.valueOf(rippleColor!!), drawable, drawable)
            }
        }
        return drawable
    }
}
