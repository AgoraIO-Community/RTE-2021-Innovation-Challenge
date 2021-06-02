package com.example.jialichun.client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.jialichun.client.R;
import com.example.jialichun.client.view.MySurfaceView;

public class CamActivity extends AppCompatActivity {

    private static final String TAG = "Kintai";
    private MySurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题

        initUI();
        setContentView(R.layout.activity_cam);
    }

    private void initUI() {
        Log.d(TAG, "initUI...");
        mView = (MySurfaceView) findViewById(R.id.mView);
    }
    public void onClickBack(View v) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
