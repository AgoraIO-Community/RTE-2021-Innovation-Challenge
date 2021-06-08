package com.ml.doctor;

import android.app.Application;
import android.util.Log;

import com.ml.doctor.utils.LocalShared;
import com.ml.doctor.utils.T;


public class CustomApplication extends Application {
    private static CustomApplication mInstance;
    public String userToken;
    public int userId;

    public static CustomApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LocalShared mLocalShared = LocalShared.getInstance(this);
        userToken = mLocalShared.getUserToken();
        userId = mLocalShared.getUserId();
        initializeEngine();
        T.init(this);
    }

    // Java
    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // 注册 onJoinChannelSuccess 回调。
        // 本地用户成功加入频道时，会触发该回调。
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        // 注册 onUserJoined 回调。
        // 远端用户成功加入频道时，会触发该回调。
        // 可以在该回调中调用 setupRemoteVideo 方法设置远端视图。
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","Remote user joined, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // 注册 onUserOffline 回调。
        // 远端用户离开频道或掉线时，会触发该回调。
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft();
                }
            });
        }
    };


    // 初始化 RtcEngine 对象。
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }


}
