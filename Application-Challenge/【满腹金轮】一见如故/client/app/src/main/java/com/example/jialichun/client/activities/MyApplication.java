package com.example.jialichun.client.activities;

import android.app.Activity;
import android.app.Application;
import android.view.View;

/**
 * Created by jialichun on 2018/3/19.
 */

public class MyApplication extends Application{
    public  boolean state;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean s) {
        state = s;
    }


}