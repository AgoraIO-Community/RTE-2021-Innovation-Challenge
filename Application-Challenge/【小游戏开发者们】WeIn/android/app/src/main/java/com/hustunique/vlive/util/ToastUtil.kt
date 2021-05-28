package com.hustunique.vlive.util

import android.widget.Toast
import com.hustunique.vlive.BuildConfig
import com.hustunique.vlive.VLiveApplication

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
object ToastUtil {
    fun makeDebug(text: String) {
        if (BuildConfig.DEBUG) {
            ThreadUtil.execUi {
                Toast.makeText(VLiveApplication.application, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun makeShort(text: String) {
        ThreadUtil.execUi {
            Toast.makeText(VLiveApplication.application, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun makeLong(text: String) {
        ThreadUtil.execUi {
            Toast.makeText(VLiveApplication.application, text, Toast.LENGTH_LONG).show()
        }
    }
}