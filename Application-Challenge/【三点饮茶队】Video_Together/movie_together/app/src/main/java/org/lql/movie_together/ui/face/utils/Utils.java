package org.lql.movie_together.ui.face.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  常用功能封装
 */
public class Utils {

    /**
     *  获取版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "4.0.0";
    }

    /**
     * 时间格式转化
     * @param timeStamp
     * @param pattern
     * @return
     */
    public static String formatTime(long timeStamp, String pattern) {
        Date date = new Date(timeStamp);
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}


