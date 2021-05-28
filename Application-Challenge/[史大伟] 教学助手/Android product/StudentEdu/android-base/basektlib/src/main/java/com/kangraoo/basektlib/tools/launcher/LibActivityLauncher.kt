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
class LibActivityLauncher : AbsLauncher<Activity>() {
    companion object {
        val instance: LibActivityLauncher by lazy {
            LibActivityLauncher()
        }
    }

    override fun launch(launcher: Activity, intent: Intent?) {
        toActivity(launcher, intent)
    }

    override fun launch(
        launcher: Activity,
        actClass: Class<out Activity?>?
    ) {
        launch(launcher, Intent(launcher, actClass))
    }

    override fun launch(
        launcher: Activity,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    ) {
        launch(launcher, getIntent(launcher, actClass, bundle))
    }

    override fun launchForResult(
        launcher: Activity,
        intent: Intent?,
        requestCode: Int
    ) {
        launcher.startActivityForResult(intent, requestCode)
    }

    override fun launchForResult(
        launcher: Activity,
        actClass: Class<out Activity?>?,
        requestCode: Int
    ) {
        launchForResult(launcher, getIntent(launcher, actClass, null), requestCode)
    }

    override fun launchForResult(
        launcher: Activity,
        actClass: Class<out Activity?>?,
        bundle: Bundle?,
        requestCode: Int
    ) {
        launchForResult(launcher, getIntent(launcher, actClass, bundle), requestCode)
    }

    override fun launchThenFinish(launcher: Activity, intent: Intent?) {
        toActivityExit(launcher, intent)
    }

    override fun launchThenFinish(
        launcher: Activity,
        actClass: Class<out Activity?>?
    ) {
        launchThenFinish(launcher, getIntent(launcher, actClass, null))
    }

    override fun launchThenFinish(
        launcher: Activity,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    ) {
        launchThenFinish(launcher, getIntent(launcher, actClass, bundle))
    }
}
