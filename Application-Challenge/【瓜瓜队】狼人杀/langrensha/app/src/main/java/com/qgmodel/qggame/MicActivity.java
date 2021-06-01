package com.qgmodel.qggame;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.qgmodel.qggame.databinding.ActivityMicBinding;
import com.qgmodel.qggame.manager.MicManager;

/**
 * Created by HeYanLe on 2020/9/5 0005 15:31.
 * https://github.com/heyanLE
 */
public class MicActivity extends AppCompatActivity {

    private ActivityMicBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMicBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        MicManager.getInstance().init(this);

    }

    @Override
    protected void onStart() {
        super.onStart();


        mBinding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MicActivity.this, "开始识别",Toast.LENGTH_SHORT).show();
                MicManager.getInstance().setTargetWord(mBinding.edit.getText().toString());
                MicManager.getInstance().start();
            }
        });
        mBinding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MicManager.getInstance().onStop();
            }
        });
        MicManager.getInstance().setOnWord(new MicManager.OnWord() {
            @Override
            public void onWord(String s) {
                Toast.makeText(MicActivity.this,"你刚刚说了"+s,Toast.LENGTH_SHORT).show();
            }
        });


    }


}
