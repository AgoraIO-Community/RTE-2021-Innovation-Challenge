package org.lql.movie_together.utils;

import android.util.Log;


/**
 * 描述: Log管理类
 * 作者: james
 * 日期: 2019/2/25 15:45
 * 类名: LogUtils
 */
public class LogUtils {

    private static final String TAG = "LogUtils";

    private static final boolean isDebug = ConstantApp.DEBUG_MODE;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, Utils.isEmpry(msg));
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, Utils.isEmpry(msg));
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, Utils.isEmpry(msg));
    }

    public static void e(String msg, Exception e) {
        if (isDebug)
            Log.w(TAG, Utils.isEmpry(msg), e);
    }

    public static void e(String tag, String msg, Exception e) {
        if (isDebug)
            Log.e(tag, Utils.isEmpry(msg));
        if (null != e)
            e.printStackTrace();
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, Utils.isEmpry(msg));
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, Utils.isEmpry(msg));
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, Utils.isEmpry(msg));
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, Utils.isEmpry(msg));
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, Utils.isEmpry(msg));
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, Utils.isEmpry(msg));
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, Utils.isEmpry(msg));
    }
}
