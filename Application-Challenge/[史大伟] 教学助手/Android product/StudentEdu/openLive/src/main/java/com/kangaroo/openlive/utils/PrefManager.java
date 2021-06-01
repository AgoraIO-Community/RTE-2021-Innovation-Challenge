package com.kangaroo.openlive.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kangaroo.openlive.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(com.kangaroo.openlive.Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
