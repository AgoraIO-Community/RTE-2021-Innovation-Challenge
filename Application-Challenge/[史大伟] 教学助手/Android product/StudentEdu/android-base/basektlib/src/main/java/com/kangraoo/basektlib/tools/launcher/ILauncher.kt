package com.kangraoo.basektlib.tools.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/07/30
 * desc :
 * version: 1.0
 */
interface ILauncher<T> {
    fun launch(launcher: T, intent: Intent?)
    fun launch(launcher: T, actClass: Class<out Activity?>?)
    fun launch(
        launcher: T,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    )

    fun launchForResult(launcher: T, intent: Intent?, requestCode: Int)
    fun launchForResult(
        launcher: T,
        actClass: Class<out Activity?>?,
        requestCode: Int
    )

    fun launchForResult(
        launcher: T,
        actClass: Class<out Activity?>?,
        bundle: Bundle?,
        requestCode: Int
    )

    fun launchThenFinish(launcher: T, intent: Intent?)
    fun launchThenFinish(launcher: T, actClass: Class<out Activity?>?)
    fun launchThenFinish(
        launcher: T,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    )
}
