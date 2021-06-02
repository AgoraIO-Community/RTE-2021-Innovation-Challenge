package com.ml.doctor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by lenovo on 2017/10/19.
 */

public class UiUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    private static float sDesignWidth = 1980;

    private static float sDesignHeight = 1200;

    public static void init(Context context, float designWidth, float designHeight) {
        sContext = context.getApplicationContext();
        sDesignWidth = designWidth;
        sDesignHeight = designHeight;
    }

    public static void compat(float designSize) {
        if (sContext != null) {
            compat(sContext, designSize);
        }
    }

    public static void compat(Context context, float designWidth) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        context.getResources().getDisplayMetrics().xdpi = 72.0f * size.x / designWidth;
    }

    public static void compatWithOrientation() {
        Configuration config = sContext.getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            compat(sContext, sDesignHeight);
            return;
        }
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            compat(sContext, sDesignWidth);
        }
    }

    public static int pt(float pt) {
        float xdpi = sContext.getResources().getDisplayMetrics().xdpi;
        return (int) (pt * xdpi / 72f);
    }
}
