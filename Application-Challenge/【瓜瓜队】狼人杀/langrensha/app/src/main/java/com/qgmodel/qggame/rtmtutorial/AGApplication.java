package com.qgmodel.qggame.rtmtutorial;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.qgmodel.qggame.MicActivity;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.handler.AGEventHandler;
import com.qgmodel.qggame.handler.MyRtcEngineEventHandler;
import com.qgmodel.qggame.manager.MicManager;
import com.qgmodel.qggame.model.Model;

import java.util.concurrent.ConcurrentHashMap;

import cn.bmob.v3.Bmob;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

public class AGApplication extends Application {
    private static AGApplication sInstance;
    private static Context context;
    private ChatManager mChatManager;

    private static final String TAG = "AGApplication";

    private RtcEngine rtcEngine;
    private MyRtcEngineEventHandler mEventHandler;


    public static AGApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5f5339b7");

        //MicManager.getInstance().init(this);
        Model.USERID = System.currentTimeMillis();

        mChatManager = new ChatManager(this);
        mChatManager.init();

        try {
            createRtcEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        context = getApplicationContext();
        MultiDex.install(this);

        Bmob.initialize(this, "03bdb4b21def908fb53e9c3c33022be4");


    }

    public ChatManager getChatManager() {
        return mChatManager;
    }

    public void createRtcEngine() throws Exception {
        Context context = getApplicationContext();
        mEventHandler = new MyRtcEngineEventHandler();
        String appId = getString(R.string.agora_app_id);
        rtcEngine = RtcEngine.create(context,appId, mEventHandler);
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_GAME);
        rtcEngine.enableVideo();
        rtcEngine.enableAudioVolumeIndication(200, 3, false);
    }

    public RtcEngine getRtcEngine() {
        return rtcEngine;
    }

    public MyRtcEngineEventHandler getmEventHandler() {
        return mEventHandler;
    }

    public void setmEventHandler(MyRtcEngineEventHandler mEventHandler) {
        this.mEventHandler = mEventHandler;
    }

    public void addEventHandler(AGEventHandler handler){
        mEventHandler.addEventHandler(handler);
    }

    public void remoteEventHandler(AGEventHandler handler){
        mEventHandler.removeEventHandler(handler);
    }

    public ConcurrentHashMap<AGEventHandler, Integer> getmEventHandlerList() {
        return mEventHandler.getmEventHandlerList();
    }

    public static Context getContext(){
        return context;
    }




}

