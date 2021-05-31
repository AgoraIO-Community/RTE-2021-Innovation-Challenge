package com.kangraoo.basektlib.app

import android.app.Activity
import android.os.Bundle
import androidx.annotation.NonNull
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.log.ULog
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/14
 * desc :
 */
object ActivityLifeManager {

    private var currentActivity: WeakReference<Activity>? = null
    private val activityStack = HashMap<Int, Stack<Activity>>()
    /**
     * 设置当前活动的activity
     */
    @JvmStatic fun sysCurrentActivity(activity: Activity) {
        currentActivity?.clear()
        currentActivity = null
        currentActivity = WeakReference(activity)
    }

    /**
     * 得到当前活动的activity
     */
    @JvmStatic fun getCurrentActivity(): Activity? {
        return currentActivity?.get()
    }

    /**
     * 清除当前活动的activity
     */
    @JvmStatic fun sysClearCurrentActivity(activity: Activity) {
        val cur = currentActivity?.get()
        if (cur != null && cur === activity) {
            currentActivity?.clear()
            currentActivity = null
        }
    }

    /**
     * 栈中有多少activity
     */
    @JvmStatic fun activityInStack(activity: Activity): Int {
        val taskId = activity.taskId
        if (taskId in activityStack.keys) {
            val stack = activityStack[taskId]
            if (stack != null && stack.size> 0) {
                return stack.size
            }
        }
        return 0
    }

    /**
     * 将Activity入栈
     */
    @JvmStatic fun pushActivity(@NonNull activity: Activity) {
        val taskId = activity.taskId
        val stack: Stack<Activity>?
        if (taskId in activityStack.keys) {
            stack = activityStack[taskId]
        } else {
            stack = Stack<Activity>()
            activityStack[taskId] = stack
        }
        stack!!.add(activity)

        ULog.i(activity.getString(R.string.libActivityFrom), "(" + activity.javaClass.simpleName + ".kt :" + 1 + ")", "(" + activity.javaClass.simpleName + ".java :" + 1 + ")", activity.getString(R.string.libActivityInStack))
    }

    /**
     * 将activity出栈
     */
    @JvmStatic fun popActivity(@NonNull activity: Activity) {
        val taskId = activity.taskId
        val stack: Stack<Activity>?
        if (taskId in activityStack.keys) {
            stack = activityStack[taskId]
            if (stack != null && stack.size> 0) {
                stack.remove(activity)
            }
            if (activityInStack(activity) == 0) {
                activityStack.remove(activity.taskId)
            }
        }
        ULog.i(activity.getString(R.string.libActivityFrom), "(" + activity.javaClass.simpleName + ".kt :" + 1 + ")", "(" + activity.javaClass.simpleName + ".java :" + 1 + ")", activity.getString(R.string.libActivityOutStack))

        if (activityInStack() == 0) {
            SApplication.instance().quit()
        }
    }

    /**
     * 所有栈中有多少activity
     */
    @JvmStatic fun activityInStack(): Int {
        var size: Int = 0
        activityStack.forEach {
            size += it.value.size
        }
        return size
    }

    /**
     * 退出栈中所有Activity
     */
    @JvmStatic fun finishAllActivity() {
        activityStack.forEach {
            it.value.forEach { ac ->
                ac.finish()
            }
        }
    }

    /**
     * 退出当前task栈中所有Activity
     */
    @JvmStatic fun finishAllActivity(activity: Activity) {
        val taskId = activity.taskId
        if (taskId in activityStack.keys) {
            activityStack[taskId]?.forEach { ac ->
                ac.finish()
            }
        }
    }

    /**
     * 将最后进入的activity出栈并清理
     */
    @JvmStatic fun popActivityFinish(taskId: Int) {
        activityStack[taskId]?.pop()?.finish()
    }

    @JvmStatic fun create(p0: Activity, p1: Bundle?) {
        if(activityFirstLifeListener.isNotEmpty()){
            for(ac in activityFirstLifeListener){
                ac.createBefore(p0, p1)
            }
        }
    }

    @JvmStatic fun createAfter(p0: Activity) {
        if(activityFirstLifeListener.isNotEmpty()){
            for(ac in activityFirstLifeListener){
                ac.createAfter(p0)
            }
        }
    }

    var activityFirstLifeListener:MutableList<ActivityFirstLifeListener> = arrayListOf()

}

interface ActivityFirstLifeListener{
    fun createBefore(p0: Activity, p1: Bundle?)
    fun createAfter(p0: Activity)
}
