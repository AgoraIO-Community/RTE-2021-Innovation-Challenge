package com.hustunique.vlive.util

import android.os.Handler
import android.os.Looper

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
object ThreadUtil {

    private val handler = Handler(Looper.getMainLooper())

    fun execUi(runnable: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            runnable()
        } else {
            handler.post {
                runnable()
            }
        }
    }
}