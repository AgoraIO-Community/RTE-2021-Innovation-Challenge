package com.kangraoo.basektlib.tools

import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.kangraoo.basektlib.tools.log.ULog
import java.lang.reflect.InvocationTargetException
private const val RAITO = 0.85

object UUi {

    private var screenWidth = 0
    private var screenHeight = 0
    private var screenMin = 0// 宽高中，小的一边
    private var screenMax = 0// 宽高中，较大的值
    private var density = 0f
    private var scaleDensity = 0f
    private var xdpi = 0f
    private var ydpi = 0f
    private var densityDpi = 0
    private var dialogWidth = 0
    var statusbarheight = 0

    fun getWidth(context: Context): Int {
        if (screenWidth == 0) {
            getScreen(context)
        }
        return screenWidth
    }

    fun getDialogWidth(context: Context): Int {
        if (screenMin == 0) {
            getScreen(context)
        }
        dialogWidth = (screenMin * RAITO).toInt()
        return dialogWidth
    }

    fun init(context: Context) {
        getScreen(context)
    }

    @Synchronized
    private fun getScreen(context: Context) {
        val dm: DisplayMetrics = context.resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        screenMin = if (screenWidth > screenHeight) screenHeight else screenWidth
        screenMax = if (screenWidth < screenHeight) screenHeight else screenWidth
        density = dm.density
        scaleDensity = dm.scaledDensity
        xdpi = dm.xdpi
        ydpi = dm.ydpi
        densityDpi = dm.densityDpi
    }
    @JvmStatic
    fun dp2px(context: Context, dipValue: Float): Int {
        if (density == 0f) {
            getScreen(context)
        }
        return (dipValue * density + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        if (density == 0f) {
            getScreen(context)
        }
        return (pxValue / density + 0.5f).toInt()
    }
    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        if (scaleDensity == 0f) {
            getScreen(context)
        }
        return (spValue * scaleDensity + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        if (scaleDensity == 0f) {
            getScreen(context)
        }
        return (pxValue / scaleDensity + 0.5f).toInt()
    }

    fun getHeight(context: Context): Int {
        if (screenHeight == 0) {
            getScreen(context)
        }
        return screenHeight
    }

    /**
     * 设置activity背景恢复原先透明
     * @param activity
     */
    fun becomeNormal(activity: Activity) {
        val params = activity.window.attributes
        params.alpha = 1.0f
        activity.window.attributes = params
    }

    /**
     * 设置activity透明度
     * @param activity
     * @param alpha
     */
    fun becomeDark(activity: Activity, alpha: Float) {
        val params = activity.window.attributes
        params.alpha = alpha
        activity.window.attributes = params
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        if (statusbarheight == 0) {
            val x =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            statusbarheight = context.resources.getDimensionPixelSize(x)
        }
        if (statusbarheight == 0) {
            statusbarheight = dp2px(context, 25f)
        }
        return statusbarheight
    }

    /**
     * 获取导航栏高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun getNavigationBarHeight(context: Context): Int {
        // below version 4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0
        }
        // has navigation bar
        if (getNavigationBarVisibility(context)) {
            val resourceId = Resources.getSystem().getIdentifier(
                "navigation_bar_height", "dimen",
                "android"
            )
            if (resourceId > 0) {
                return Resources.getSystem().getDimensionPixelSize(resourceId)
            }
        }
        return 0
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getNavigationBarVisibility(context: Context): Boolean {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val realSize = getDisplayRealSize(context)
        val screen_height = Math.max(size.y, size.x)
        val real_screen_height = Math.max(realSize.y, realSize.x)
        return real_screen_height > screen_height
    }

    private fun getDisplayRealSize(context: Context): Point {
        val point = Point()
        val wManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point)
        } else {
            try {
                val mGetRawH =
                    Display::class.java.getMethod("getRawHeight")
                val mGetRawW =
                    Display::class.java.getMethod("getRawWidth")
                point.x = (mGetRawW.invoke(display) as Int)
                point.y = (mGetRawH.invoke(display) as Int)
            } catch (e: NoSuchMethodException) {
                display.getSize(point)
                ULog.e(e, e.message)
            } catch (e: IllegalAccessException) {
                display.getSize(point)
                ULog.e(e, e.message)
            } catch (e: InvocationTargetException) {
                display.getSize(point)
                ULog.e(e, e.message)
            }
        }
        return point
    }

    /**
     * 获取actionbar高度
     * @return
     */
    fun getActionBarHeight(context: Context): Int {
        val ta: TypedArray = context
            .obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        val actionBarSize = ta.getDimension(0, 0f).toInt()
        ta.recycle()
        return if (actionBarSize > 0) {
            actionBarSize
        } else {
            dp2px(context, 100f)
        }
    }

    /**
     * 获取activity尺寸
     *
     * @param activity
     * @return
     */
    fun getRealScreenSize(activity: Activity): IntArray? {
        val size = IntArray(2)
        var screenWidth: Int
        var screenHeight: Int
        val w = activity.windowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT < 17) {
            screenWidth = Display::class.java.getMethod("getRawWidth")
                .invoke(d) as Int
            screenHeight = Display::class.java
                .getMethod("getRawHeight").invoke(d) as Int
        }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            val realSize = Point()
            Display::class.java.getMethod("getRealSize", Point::class.java)
                .invoke(
                    d,
                    realSize
                )
            screenWidth = realSize.x
            screenHeight = realSize.y
        }
        size[0] = screenWidth
        size[1] = screenHeight
        return size
    }

    /**
     * 是否是横屏
     *
     * @param context
     * @return
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == 2
    }

    /**
     * 是否是竖屏
     *
     * @param context
     * @return
     */
    fun isPortrait(context: Context): Boolean {
        var flag = true
        if (context.resources.configuration.orientation != 1) {
            flag = false
        }
        return flag
    }




}
