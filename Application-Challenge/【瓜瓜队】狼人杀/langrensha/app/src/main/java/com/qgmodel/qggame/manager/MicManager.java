package com.qgmodel.qggame.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.qgmodel.qggame.MicActivity;
import com.qgmodel.qggame.rtmtutorial.AGApplication;

/**
 * Created by HeYanLe on 2020/9/5 0005 16:37.
 * https://github.com/heyanLE
 */
public class MicManager {

    public interface OnWord{
        void onWord(String s);
    }

    private static MicManager INSTANCE = new MicManager();
    public static MicManager getInstance(){
        return INSTANCE;
    }

    private String targetWord = "";
    private SpeechRecognizer mIat;
    private OnWord onWord = new OnWord() {
        @Override
        public void onWord(String s) {

        }
    };



    public void init(Activity context){
        mIat = SpeechRecognizer.createRecognizer(context, new InitListener() {
            @Override
            public void onInit(int i) {
                Log.i("xxx",i+"");
                mIat.setParameter( SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL );
                mIat.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
                mIat.setParameter( SpeechConstant.SUBJECT, null );
                mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
                mIat.setParameter(SpeechConstant.ASR_PTT,"0");
                mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
                mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
                mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath(context));
                mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, context.getExternalFilesDir(null)+"/msc/iat.wav");
            }
        });
        try {
            mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
            mIat.setParameter(SpeechConstant.SUBJECT, null);
            mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
            mIat.setParameter(SpeechConstant.ASR_PTT, "0");
            mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
            mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath(context));
            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, context.getExternalFilesDir(null) + "/msc/iat.wav");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }
    public void setOnWord(OnWord onWord) {
        this.onWord = onWord;
    }

    public void onStop(){
        mIat.stopListening();
    }

    public void start(){
        if (mIat.isListening()){
            return;
        }

        mIat.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String s = recognizerResult.getResultString();
                Log .i("MicManager","识别 -> "+s);
            //Toast.makeText(AGApplication.the(), "识别"+s,Toast.LENGTH_SHORT).show();
                if (s.contains(targetWord)){
                    onWord.onWord(targetWord);
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    private String getResourcePath(Context context) {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }
}
