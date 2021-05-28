package com.kangraoo.basektlib.tools.view

import android.view.LayoutInflater.Factory2
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
object UView {
    @JvmOverloads
    fun inject(
        activity: AppCompatActivity,
        factory2: Factory2 = LibViewFactory(LibFactory(activity.delegate))
    ) {
        val inflater = activity.layoutInflater ?: return
        LayoutInflaterCompat.setFactory2(inflater, factory2)
    }
}
