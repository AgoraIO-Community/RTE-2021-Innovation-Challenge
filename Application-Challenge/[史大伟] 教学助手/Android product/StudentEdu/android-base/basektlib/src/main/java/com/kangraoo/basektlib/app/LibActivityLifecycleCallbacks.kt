package com.kangraoo.basektlib.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kangraoo.basektlib.ui.BActivity

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/14
 * desc :
 */
@Deprecated("抛弃")
open class LibActivityLifecycleCallbacks(val lifecycleCallbacks: Application.ActivityLifecycleCallbacks) : Application.ActivityLifecycleCallbacks by lifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        lifecycleCallbacks.onActivityCreated(activity, savedInstanceState)
        if (activity is BActivity) {
            bActivity(activity)
        }
    }

    open fun bActivity(activity: BActivity) {
    }
}
