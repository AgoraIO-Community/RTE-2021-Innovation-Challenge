//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.kangraoo.basektlib.ui

import android.app.Activity
import androidx.annotation.StringRes
import com.kangraoo.basektlib.tools.tip.ITipToast

interface IBaseView {
    fun visitActivity(): Activity
    fun showToastMsg(var1: String?)
    fun showToastMsg(@StringRes var1: Int)
    fun showToastMsg(iTipToast: ITipToast, @StringRes message: Int)
    fun showToastMsg(iTipToast: ITipToast, message: String?)
    fun showProgressingDialog()
    fun showProgressingDialog(@StringRes var1: Int)
    fun showProgressingDialog(var1: String)
    fun dismissProgressDialog()
    fun postRunnable(runnable: Runnable)
    fun postDelayed(runnable: Runnable, delay: Long)
    fun removeCallbacks(runnable: Runnable)
}
