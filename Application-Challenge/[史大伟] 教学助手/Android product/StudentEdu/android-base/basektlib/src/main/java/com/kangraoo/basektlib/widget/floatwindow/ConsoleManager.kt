package com.kangraoo.basektlib.widget.floatwindow

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kangraoo.basektlib.app.ActivityLifeManager.getCurrentActivity
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.SApplication.Companion.context
import com.kangraoo.basektlib.app.SApplication.Companion.instance
import com.kangraoo.basektlib.tools.SSysStore
import com.kangraoo.basektlib.tools.UUi.dp2px
import com.kangraoo.basektlib.tools.UUi.getHeight
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.widget.floatwindow.FloatWindowPermissionChecker.askForFloatWindowPermission
import com.kangraoo.basektlib.widget.floatwindow.FloatWindowPermissionChecker.checkFloatWindowPermission

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/06
 * desc :
 * version: 1.0
 */
class ConsoleManager private constructor() {
    var isAddToWindow = false
    var windowManager: WindowManager? = null
    var floatWindowsView: FloatWindowsView? = null

    companion object {
        val instance: ConsoleManager by lazy {
            ConsoleManager()
        }
    }

    fun checkDebugAndShow(): Boolean {
        if (!instance().sConfiger.debugStatic) {
            return false
        }
        val check: Boolean = SSysStore.instance.sysDebugConsole
        if (!check) {
            closeFloat()
        }
        return check
    }

    fun showFloatWindow() {
        if (isAddToWindow) return
        if (!checkDebugAndShow()) {
            return
        }
        if (!checkFloatWindowPermission()) {
            askForFloatWindowPermission(getCurrentActivity()!!)
            return
        }
        windowManager = instance().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        // 新建悬浮窗控件
        floatWindowsView = FloatWindowsView(context())
        // 设置LayoutParam
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = dp2px(context(), 100f)
        layoutParams.x = 0
        layoutParams.y = getHeight(context()) - layoutParams.height
        //        小窗口设置FLAG_NOT_TOUCH_MODAL，可以在弹出小窗口的同时，点击Activity中未被覆盖的按钮等。
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        floatWindowsView!!.setOnTouchHandler(object : FloatWindowsView.OnTouchHandler {
            override fun onMove(moveX: Float, moveY: Float) {
                layoutParams.x += moveX.toInt()
                layoutParams.y += moveY.toInt()
                windowManager!!.updateViewLayout(floatWindowsView, layoutParams)
            }

            override fun onMoveEnd() {}
            override fun min() {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = dp2px(context(), 100f)
                layoutParams.x = 0
                layoutParams.y = getHeight(context()) - layoutParams.height
                windowManager!!.updateViewLayout(floatWindowsView, layoutParams)
            }

            override fun max() {
                layoutParams.x = 0
                layoutParams.y = 0
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                windowManager!!.updateViewLayout(floatWindowsView, layoutParams)
            }

            override fun close() {
                SSysStore.instance.putSysDebugConsole(false)
                closeFloat()
            }

            override fun debug() {
                if (consoleDebug != null) {
                    consoleDebug!!.debugAction()
                }
            }
        })
        ULog.addIlog(floatWindowsView!!)

//        floatWindowsView.setFocusableInTouchMode(true);
        // 将悬浮窗控件添加到WindowManager
        windowManager!!.addView(floatWindowsView, layoutParams)
        isAddToWindow = true
    }

    interface IConsoleDebug {
        fun debugAction()
    }

    var consoleDebug: IConsoleDebug? = null

    fun resumeFloat() {
        if (windowManager != null && isAddToWindow && floatWindowsView != null && !SApplication.instance().sConfiger.consoleOutwindow) {
            floatWindowsView!!.visibility = View.VISIBLE
        }
        if (windowManager != null && isAddToWindow && floatWindowsView != null) {
            floatWindowsView!!.testFloatView()
        }
    }

    fun pauseFloat() {
        if (windowManager != null && isAddToWindow && floatWindowsView != null && !SApplication.instance().sConfiger.consoleOutwindow) {
            floatWindowsView!!.visibility = View.GONE
        }
        if (windowManager != null && isAddToWindow && floatWindowsView != null) {
            floatWindowsView!!.testFloatViewGone()
        }
    }

    fun closeFloat() {
        if (windowManager != null && isAddToWindow && floatWindowsView != null) {
            windowManager!!.removeView(floatWindowsView)
            floatWindowsView!!.setOnTouchHandler(null)
            ULog.removeIlog(floatWindowsView)
            floatWindowsView = null
            isAddToWindow = false
        }
    }
}
