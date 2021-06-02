package com.kangaroo.openlive;

import android.app.Application;
import android.content.SharedPreferences;

import com.kangaroo.openlive.rtc.AgoraEventHandler;
import com.kangaroo.openlive.rtc.EngineConfig;
import com.kangaroo.openlive.rtc.EventHandler;
import com.kangaroo.openlive.stats.StatsManager;
import com.kangaroo.openlive.utils.FileUtil;
import com.kangaroo.openlive.utils.PrefManager;
import com.kangraoo.basektlib.app.SApplication;

import io.agora.rtc.RtcEngine;

public class AgoraApplication {
    private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();
    public String appId = "";
    public String appCe = "";
    public String user;

    public void init(){
        try {
            mRtcEngine = RtcEngine.create(SApplication.context(), appId, mHandler);
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(SApplication.context()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initConfig();

    }


    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(SApplication.context());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                com.kangaroo.openlive.Constants.PREF_RESOLUTION_IDX, com.kangaroo.openlive.Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(com.kangaroo.openlive.Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(com.kangaroo.openlive.Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(com.kangaroo.openlive.Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(com.kangaroo.openlive.Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }

    public void onTerminate() {
        RtcEngine.destroy();
    }
}
