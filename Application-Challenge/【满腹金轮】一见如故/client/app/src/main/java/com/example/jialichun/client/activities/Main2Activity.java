package com.example.jialichun.client.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.jialichun.client.R;
import com.example.jialichun.client.view.MySurfaceView;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Kintai";
    private MySurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题

        initUI();
        setContentView(R.layout.activity_main2);
    }

    private void initUI() {
        Log.d(TAG, "initUI...");
        mView = (MySurfaceView) findViewById(R.id.mView);
    }

}
