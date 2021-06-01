package com.kangraoo.basektlib.tools.tip

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc :
 */
internal interface ITipStyle {

    fun getTipBackGroundColor(): Int

    @DrawableRes
    fun getIcon(): Int

    @ColorRes
    fun getTextColor(): Int

    fun getTextSize(): Float
}
