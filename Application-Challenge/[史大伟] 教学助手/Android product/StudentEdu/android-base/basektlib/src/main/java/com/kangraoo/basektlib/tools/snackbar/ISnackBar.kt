package com.kangraoo.basektlib.tools.snackbar

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2020/01/09
 * desc :
 * version: 1.0
 */
interface ISnackBar {
    fun tip(view: View, @StringRes message: Int): Snackbar
    fun tip(
        view: View,
        @StringRes message: Int,
        @StringRes actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar

    fun tip(view: View, @StringRes message: Int, duration: Int): Snackbar
    fun tip(
        view: View,
        @StringRes message: Int,
        duration: Int,
        @StringRes actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar

    fun tip(view: View, message: CharSequence): Snackbar
    fun tip(
        view: View,
        message: CharSequence,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar

    fun tip(view: View, message: CharSequence, duration: Int): Snackbar
    fun tip(
        view: View,
        message: CharSequence,
        duration: Int,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar

    fun tipIndefinite(view: View, @StringRes message: Int, duration: Int): Snackbar
    fun tipIndefinite(
        view: View,
        @StringRes message: Int,
        duration: Int,
        @StringRes actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar

    fun tipIndefinite(view: View, message: CharSequence, duration: Int): Snackbar
    fun tipIndefinite(
        view: View,
        message: CharSequence,
        duration: Int,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar
}
