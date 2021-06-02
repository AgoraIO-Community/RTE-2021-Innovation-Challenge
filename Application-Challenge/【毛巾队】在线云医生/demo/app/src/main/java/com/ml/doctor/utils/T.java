package com.ml.doctor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/10/11.
 */

public class T {
    private static Toast sToast;

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static void show(@StringRes final int resId) {
        show(sContext.getString(resId));
    }

    public static void showLong(@StringRes final int resId) {
        showLong(sContext.getString(resId));
    }

    public static void show(final String text) {
        showOnUiThread(text, Toast.LENGTH_SHORT);
    }


    public static void showLong(final String text) {
        showOnUiThread(text, Toast.LENGTH_LONG);
    }

    private static void showOnUiThread(final String text, final int duration) {
        Handlers.ui().post(new Runnable() {
            @Override
            public void run() {
                show(text, duration);
            }
        });
    }

    private static void show(String text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, text, duration);
            sToast.show();
            return;
        }
        sToast.setText(text);
        sToast.setDuration(duration);
        sToast.show();
    }
}
