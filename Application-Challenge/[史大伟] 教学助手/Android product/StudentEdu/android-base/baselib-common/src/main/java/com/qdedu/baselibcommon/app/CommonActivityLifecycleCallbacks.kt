package com.qdedu.baselibcommon.app

import android.app.Activity
import android.app.Application
import com.kangraoo.basektlib.app.LibActivityLifecycleCallbacks
import com.umeng.analytics.MobclickAgent
@Deprecated("友盟AUTO模式不需要")
class CommonActivityLifecycleCallbacks(lifecycleCallbacks : Application.ActivityLifecycleCallbacks):LibActivityLifecycleCallbacks(lifecycleCallbacks){

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        MobclickAgent.onResume(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        MobclickAgent.onPause(activity)
    }


}