package com.qgmodel.qggame.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    private static SharedPreferences getSharePreferences(Context context) {
        return context.getSharedPreferences("PLAYER_PREFERENCES", Context.MODE_PRIVATE);
    }
    //   储存字符串数据
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharePreferences = getSharePreferences(context);
        SharedPreferences.Editor edit = sharePreferences.edit();
        edit.putBoolean(key, value); edit.apply();
    }
    //   获取布尔值数据
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = getSharePreferences(context);
        return preferences.getBoolean(key, true);
    }
    // 获取字符串数据
    public static void setString(Context context, String key, String value) {
        SharedPreferences sharePreferences = getSharePreferences(context);
        SharedPreferences.Editor edit = sharePreferences.edit();
        edit.putString(key, value); edit.apply();
    }
    //   储存布尔值数据
    public static String getString(Context context, String key) {
        SharedPreferences sharePreferences = getSharePreferences(context);
        return sharePreferences.getString(key, "");
    }

}
