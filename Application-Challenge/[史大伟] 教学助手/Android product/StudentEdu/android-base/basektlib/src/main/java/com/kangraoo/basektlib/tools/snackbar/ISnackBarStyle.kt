package com.kangraoo.basektlib.tools.snackbar

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

interface ISnackBarStyle {
    fun getTipBackGroundColor(): Int

    @DrawableRes
    fun getIcon(): Int

    @ColorRes
    fun getTextColor(): Int

    @ColorRes
    fun getActionTextColor(): Int

    fun getTextSize(): Float

    fun getActionTextSize(): Float
}
