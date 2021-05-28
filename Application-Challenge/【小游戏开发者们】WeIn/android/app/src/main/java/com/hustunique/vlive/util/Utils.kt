package com.hustunique.vlive.util

import android.content.Context
import android.content.pm.ApplicationInfo

object Utils {
    private var isDebug = true

    fun init(context: Context) {
        isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
}