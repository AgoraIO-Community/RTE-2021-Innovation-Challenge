package com.kangraoo.basektlib.widget.image

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider

/**
 * description: RoundViewOutlineProvider
 * author: liping
 * date: 2021/1/27 18:00
 */
class RoundViewOutlineProvider(val mRadius: Float) : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        val selfRect =
            Rect(0, 0, view.width, view.height) // 绘制区域
        outline.setRoundRect(selfRect, mRadius)
    }
}
