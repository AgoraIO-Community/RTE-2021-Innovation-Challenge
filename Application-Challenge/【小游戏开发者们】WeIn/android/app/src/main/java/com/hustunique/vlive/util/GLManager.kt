package com.hustunique.vlive.util

import android.os.Handler
import android.os.HandlerThread

class GLManager {
    private val handlerThread = HandlerThread("ar_core_helper").apply {
        start()
    }
    private val handler = Handler(handlerThread.looper)

    fun getGLHandler() = handler

    fun stop() {
        handlerThread.quitSafely()
    }
}