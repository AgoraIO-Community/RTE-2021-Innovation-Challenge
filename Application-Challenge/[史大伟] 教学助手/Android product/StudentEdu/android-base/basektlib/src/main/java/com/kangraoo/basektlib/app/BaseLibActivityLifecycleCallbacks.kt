package com.kangraoo.basektlib.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.dialog.LibSimpleProgressDialog

class BaseLibActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is BActivity) {
            activity.iProgressDialog = LibSimpleProgressDialog(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
