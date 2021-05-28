package com.kangraoo.basektlib.tools.view.statebuilder

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/06
 * desc :
 * version: 1.0
 */
class EmptyBuilder : BaseBuilder<EmptyBuilder>() {
    private var view: View? = null
    fun setView(view: View?): EmptyBuilder {
        this.view = view
        return this
    }

    fun build(): Drawable? {
        var background: Drawable? = null
        if (view != null) {
            background = view!!.background
        }
        return background?.let { buildRipple(it) } ?: buildRipple(GradientDrawable())
    }
}
