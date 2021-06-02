package com.kangraoo.basektlib.tools.launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/07/30
 * desc :
 * version: 1.0
 */
abstract class AbsLauncher<T> : ILauncher<T> {
    fun toActivity(launcher: Activity, intent: Intent?) {
        launcher.startActivity(intent)
    }

    fun toActivityExit(launcher: Activity, intent: Intent?) {
        toActivity(launcher, intent)
        launcher.finish()
    }

    protected fun getIntent(
        context: Context?,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    ): Intent {
        val intent = Intent(context, actClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return intent
    }
}
