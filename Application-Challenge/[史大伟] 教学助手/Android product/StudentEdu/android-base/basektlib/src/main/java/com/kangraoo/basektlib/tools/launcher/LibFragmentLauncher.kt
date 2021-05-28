package com.kangraoo.basektlib.tools.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/07/30
 * desc :
 * version: 1.0
 */
class LibFragmentLauncher : AbsLauncher<Fragment>() {

    companion object {
        val instance: LibFragmentLauncher by lazy {
            LibFragmentLauncher()
        }
    }

    override fun launch(
        launcher: Fragment,
        intent: Intent?
    ) {
        toActivity(launcher.activity!!, intent)
    }

    override fun launch(
        launcher: Fragment,
        actClass: Class<out Activity?>?
    ) {
        launch(launcher, getIntent(launcher.activity, actClass, null))
    }

    override fun launch(
        launcher: Fragment,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    ) {
        launch(launcher, getIntent(launcher.activity, actClass, bundle))
    }

    override fun launchForResult(
        launcher: Fragment,
        intent: Intent?,
        requestCode: Int
    ) {
        launcher.startActivityForResult(intent, requestCode)
    }

    override fun launchForResult(
        launcher: Fragment,
        actClass: Class<out Activity?>?,
        requestCode: Int
    ) {
        launcher.startActivityForResult(
            getIntent(launcher.activity, actClass, null),
            requestCode
        )
    }

    override fun launchForResult(
        launcher: Fragment,
        actClass: Class<out Activity?>?,
        bundle: Bundle?,
        requestCode: Int
    ) {
        launcher.startActivityForResult(
            getIntent(launcher.activity, actClass, bundle),
            requestCode
        )
    }

    override fun launchThenFinish(
        launcher: Fragment,
        intent: Intent?
    ) {
        toActivityExit(launcher.activity!!, intent)
    }

    override fun launchThenFinish(
        launcher: Fragment,
        actClass: Class<out Activity?>?
    ) {
        launchThenFinish(launcher, getIntent(launcher.activity, actClass, null))
    }

    override fun launchThenFinish(
        launcher: Fragment,
        actClass: Class<out Activity?>?,
        bundle: Bundle?
    ) {
        launchThenFinish(launcher, getIntent(launcher.activity, actClass, bundle))
    }
}
