package com.kangraoo.basektlib.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kangraoo.basektlib.widget.floatwindow.ConsoleManager

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/09
 * desc :
 */
class DefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    var create = false

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        create = true
        ActivityLifeManager.create(p0,p1)
        ActivityLifeManager.sysCurrentActivity(p0)
        ConsoleManager.instance.showFloatWindow()
        ActivityLifeManager.pushActivity(p0)
    }

    override fun onActivityStarted(p0: Activity) {
        if(create){
            ActivityLifeManager.createAfter(p0)
            create = !create
        }
    }

    override fun onActivityResumed(p0: Activity) {
        ActivityLifeManager.sysCurrentActivity(p0)
        ConsoleManager.instance.resumeFloat()
    }

    override fun onActivityPaused(p0: Activity) {
        ConsoleManager.instance.pauseFloat()
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        ActivityLifeManager.sysClearCurrentActivity(p0)
        ActivityLifeManager.popActivity(p0)
    }
}
