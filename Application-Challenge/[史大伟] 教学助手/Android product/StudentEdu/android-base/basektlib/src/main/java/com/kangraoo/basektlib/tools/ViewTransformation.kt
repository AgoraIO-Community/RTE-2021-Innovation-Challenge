package com.kangraoo.basektlib.tools

import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.kangraoo.basektlib.widget.image.RoundViewOutlineProvider

/**
 * description: ViewTransformation
 * author: liping
 * date: 2021/1/28 9:28
 */

fun ImageView.clipRound(drawable: Drawable, cornerDp: Float) {
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    this.setImageDrawable(drawable)
    this.clipToOutline = true // 用outline裁剪内容区域
    this.outlineProvider = RoundViewOutlineProvider(UUi.dp2px(context, cornerDp).toFloat())
    this.imageMatrix = Matrix().apply {
        val dwidth: Int = drawable.intrinsicWidth
        val dheight: Int = drawable.intrinsicHeight
        val vwidth: Int = this@clipRound.layoutParams.width
        val vheight: Int = this@clipRound.layoutParams.height
        val scale: Float
        if (dwidth * vheight > vwidth * dheight) {
            scale = vheight.toFloat() / dheight.toFloat()
        } else {
            scale = vwidth.toFloat() / dwidth.toFloat()
        }
        setScale(scale, scale)
    }
}
