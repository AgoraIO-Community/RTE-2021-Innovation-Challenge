package com.dong.circlelive.live.wigets

import android.view.View
import com.dong.circlelive.utils.cornerOutlineProvider

/**
 * 用户为不同场景下提供不同的布局方式。如正常模式与pick game模式下，布局模式差别很大。
 * 如果只在一个方法改动，会造成布局计算代码越来越复杂，所以抽象出不同的布局算法策略
 */
interface RenderLayoutProvider {

    fun renderWidthHeight(index: Int, renderCount: Int): RenderLayoutParams

    fun totalRenderHeight(): Int

    fun renderDisplayHeight(): Int

    fun avatarCoverScale(index: Int, renderCount: Int): Float

    fun muteIconScale(index: Int, renderCount: Int): Float

    fun applyCornerForRenderContainerIfNeed()
}

data class RenderLayoutParams(
    val width: Int,
    val height: Int,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val radius: Float = 0f,
    val leftTop: Boolean = false,
    val rightTop: Boolean = false,
    val leftBottom: Boolean = false,
    val rightBottom: Boolean = false
) {

    fun applyRadius(view: View) {
        if (radius > 0) {
            view.cornerOutlineProvider(radius, leftTop, rightTop, leftBottom, rightBottom)
        } else {
            view.clipToOutline = false
        }
    }
}