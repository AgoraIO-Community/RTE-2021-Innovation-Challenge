//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.kangraoo.basektlib.widget.listener

import android.view.View

/**
 * 防止多次点击监听器
 */
abstract class OnOnceClickListener : View.OnClickListener {
    private var lastClickTime = 0L
    private val minClickDelayTime = 500
    override fun onClick(v: View) {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime > minClickDelayTime) {
            onOnceClick(v)
        }
        lastClickTime = currentClickTime
    }

    abstract fun onOnceClick(var1: View)
}
