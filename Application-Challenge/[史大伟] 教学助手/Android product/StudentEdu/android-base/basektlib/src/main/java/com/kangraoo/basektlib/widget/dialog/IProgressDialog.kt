package com.kangraoo.basektlib.widget.dialog

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/02
 * desc :
 * version: 1.0
 */
interface IProgressDialog : LifecycleObserver {
    fun showProgress()
    fun showProgress(@StringRes message: Int)
    fun showProgress(message: String)
    fun dismissProgress()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destoryProgress()
}
