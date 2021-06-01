package com.qgmodel.qggame;

import android.app.Application;

/**
 * Created by HeYanLe on 2020/8/8 0008 16:00.
 * https://github.com/heyanLE
 */
public class QGApplication extends Application {

    public static QGApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
