package com.kangraoo.basektlib.app

import android.app.Application
import android.content.Context
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.init.DefaultInit
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.SSystem
import com.kangraoo.basektlib.tools.crash.AndroidCrash
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.rx.RxBus
import com.kangraoo.basektlib.tools.task.TaskManager
import com.kangraoo.basektlib.widget.floatwindow.ConsoleManager
import kotlin.properties.Delegates

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/08
 * desc :
 */
abstract class SApplication : Application() {

    /**
     * 相当于静态成员
     */
    companion object {
        private var instance: SApplication by Delegates.notNull<SApplication>()
        private var context: Context by Delegates.notNull<Context>()
        @JvmStatic fun instance() = instance
        @JvmStatic fun context() = context
    }

    val sConfiger: SConfiger by lazy {
        configer()
    }

    protected abstract fun configer(): SConfiger

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = instance.applicationContext
        AndroidCrash.instance
        if (SSystem.inMainProcess(this)) {
            appInit().init()
            TaskManager.taskExecutor.execute {
                appInit().threadInit()
            }
        } else {
            appInit().notMainProcessInit()
            TaskManager.taskExecutor.execute {
                appInit().notMainProcessThreadInit()
            }
        }
        for(alc in getActivityLifecycleCallbacks()){
            registerActivityLifecycleCallbacks(alc)
        }
        init()
    }

    open fun appInit(): IInit {
        return DefaultInit()
    }

    open fun getActivityLifecycleCallbacks(): MutableList<ActivityLifecycleCallbacks> {
        return arrayListOf(DefaultActivityLifecycleCallbacks(),BaseLibActivityLifecycleCallbacks())
    }

    abstract fun init()

    /**
     * 退出整个app(app并不会销毁)
     */
    fun quit() {
        ULog.i(getString(R.string.libQuit))
        ConsoleManager.instance.closeFloat()
        RxBus.instance.removeAllStickyEvents()
    }
}
