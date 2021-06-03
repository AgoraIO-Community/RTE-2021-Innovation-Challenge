package com.game.tingshuo.app

import android.app.Activity
import android.app.Application
import android.os.Process
import rxhttp.wrapper.param.RxHttp
import kotlin.system.exitProcess

class App : Application() {

    companion object {
        private var allActivities = mutableSetOf<Activity>()
        private lateinit var appInstance: App

        @Synchronized
        @JvmStatic
        fun getInstance(): App {
            return appInstance
        }

        @JvmStatic
        fun addActivity(act: Activity) {
            allActivities.add(act)
        }

        @JvmStatic
        fun removeActivity(act: Activity?) {
            allActivities.remove(act)
        }

        @JvmStatic
        fun removeActivity(cls: Class<*>) {
            synchronized(allActivities) {
                for (act in allActivities) {
                    if (act.javaClass == cls){
                        act.finish()
                    }
                }
            }
        }

        @JvmStatic
        fun exitApp() {
            synchronized(allActivities) {
                for (act in allActivities) {
                    act.finish()
                }
            }
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }

    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        //添加公共请求头
        //RxHttp.setOnParamAssembly { it.addHeader("authorization", "Bearer ${MyUtils.getToken()}") }
        //RxHttp.setDebug(true)
    }

}