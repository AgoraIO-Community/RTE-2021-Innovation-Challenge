package org.lql.movie_together;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.lql.movie_together.handler.MyEngineEventHandler;
import org.lql.movie_together.model.AGEventHandler;
import org.lql.movie_together.model.CurrentUserSettings;
import org.lql.movie_together.model.EngineConfig;
import org.lql.movie_together.utils.Constant;
import org.lql.movie_together.utils.ConstantApp;
import org.lql.movie_together.utils.LogUtils;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.DataStreamConfig;

import static io.agora.rtc.Constants.RAW_AUDIO_FRAME_OP_MODE_READ_ONLY;

public class MTApplication extends Application {

    private RtcEngine mRtcEngine;
    private EngineConfig mConfig;
    private MyEngineEventHandler mEventHandler;
    private CurrentUserSettings mVideoSettings = new CurrentUserSettings();

    public RtcEngine getmRtcEngine() {
        return mRtcEngine;
    }

    public MyEngineEventHandler getmEventHandler() {
        return mEventHandler;
    }

    public EngineConfig getmConfig() {
        return mConfig;
    }

    public CurrentUserSettings getmVideoSettings() {
        return mVideoSettings;
    }

    public void addEventHandler(AGEventHandler handler) {
        mEventHandler.addEventHandler(handler);
    }

    public void remoteEventHandler(AGEventHandler handler) {
        mEventHandler.removeEventHandler(handler);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createRtcEngine();
    }

    private void createRtcEngine() {
        Context context = getApplicationContext();
        // 应用ID
        String appId = context.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("需要设置您自己的app_id");
        }

        mEventHandler = new MyEngineEventHandler();
        try {
            // 创建声网引擎实例
            mRtcEngine = RtcEngine.create(context, appId, mEventHandler);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        // 能够发送流信息
        ConstantApp.STREAM_ID = mRtcEngine.createDataStream(new DataStreamConfig());
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mRtcEngine.setRecordingAudioFrameParameters(4000, 1, RAW_AUDIO_FRAME_OP_MODE_READ_ONLY, 1024);

        mConfig = new EngineConfig();
    }


}
