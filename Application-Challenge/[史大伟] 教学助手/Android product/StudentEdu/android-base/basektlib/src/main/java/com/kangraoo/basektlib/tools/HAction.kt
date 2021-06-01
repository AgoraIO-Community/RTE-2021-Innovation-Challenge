package com.kangraoo.basektlib.tools

import android.os.Handler
import android.os.Looper
import com.kangraoo.basektlib.tools.task.TaskManager
import java.util.concurrent.ConcurrentHashMap
import kotlin.IllegalArgumentException

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
object HAction {
    @JvmField val mainHandler = Handler(Looper.getMainLooper())
    private val actions = ConcurrentHashMap<String, Action>()

    fun put(key: String, task: Runnable, inUiThread: Boolean) = put(key, task, inUiThread, 0L)

    fun put(key: String, task: Runnable, inUiThread: Boolean, delayedTime: Long) = actions.put(key, Action(key, task, inUiThread, delayedTime))

    fun exec(key: String) {

        if (key in actions.keys) {
            val ac = actions[key]
            exec(ac!!)
        }
        throw IllegalArgumentException("key not found")
    }

    fun exec(ac: Action) {
        if (ac.inUiThread) {
            if (ac.delayedTime> 0) {
                mainHandler.postDelayed(ac.task, ac.delayedTime)
            } else {
                mainHandler.post(ac.task)
            }
        } else {
            if (ac.delayedTime> 0) {
                TaskManager.taskExecutor.execute(ac.task, ac.delayedTime)
            } else {
                TaskManager.taskExecutor.execute(ac.task)
            }
        }
    }
}

data class Action(val name: String, val task: Runnable, val inUiThread: Boolean, val delayedTime: Long)
