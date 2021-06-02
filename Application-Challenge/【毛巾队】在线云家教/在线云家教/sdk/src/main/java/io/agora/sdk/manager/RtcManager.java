package io.agora.sdk.manager;

import android.content.Context;
import android.view.SurfaceView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.log.LogManager;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.sdk.annotation.AudioProfile;
import io.agora.sdk.annotation.AudioScenario;
import io.agora.sdk.annotation.ChannelProfile;
import io.agora.sdk.annotation.ClientRole;
import io.agora.sdk.annotation.RenderMode;
import io.agora.sdk.annotation.StreamType;
import io.agora.sdk.listener.RtcEventListener;

public final class RtcManager extends SdkManager<RtcEngine> {
    private final LogManager log = new LogManager(this.getClass().getSimpleName());

    private List<RtcEventListener> listeners;

    private static RtcManager instance;

    private RtcManager() {
        listeners = new ArrayList<>();
    }

    public static RtcManager instance() {
        if (instance == null) {
            synchronized (RtcManager.class) {
                if (instance == null)
                    instance = new RtcManager();
            }
        }
        return instance;
    }

    @Override
    protected RtcEngine creakSdk(Context context, String appId) throws Exception {
        return RtcEngine.create(context, appId, eventHandler);
    }

    @Override
    protected void configSdk() {
        getSdk().setLogFile(new File(LogManager.getPath(), "agorasdk.log").getAbsolutePath());
        getSdk().enableAudio();
        getSdk().enableVideo();
        getSdk().enableWebSdkInteroperability(true);
    }

    @Override
    public void joinChannel(@NonNull Map<String, String> data) {
        getSdk().joinChannel(data.get(TOKEN), data.get(CHANNEL_ID), data.get(USER_EXTRA), Integer.parseInt(data.get(USER_ID)));
    }

    @Override
    public void leaveChannel() {
        getSdk().leaveChannel();
    }

    @Override
    protected void destroySdk() {
        RtcEngine.destroy();
    }

    public void registerListener(RtcEventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(RtcEventListener listener) {
        listeners.remove(listener);
    }

    public void setChannelProfile(@ChannelProfile int profile) {
        getSdk().setChannelProfile(profile);
    }

    public void setClientRole(@ClientRole int role) {
        getSdk().setClientRole(role);
    }

    public void setAudioProfile(@AudioProfile int profile, @AudioScenario int scenario) {
        getSdk().setAudioProfile(profile, scenario);
    }

    public void setVideoEncoderConfiguration(@NonNull VideoEncoderConfiguration configuration) {
        getSdk().setVideoEncoderConfiguration(configuration);
    }

    public void setParameters(@NonNull String key, @NonNull Object value) {
        getSdk().setParameters(new Gson().toJson(new HashMap<String, Object>() {{
            put(key, value);
        }}));
    }

    public void enableLocalAudio(boolean enable) {
        getSdk().enableLocalAudio(enable);
    }

    public void enableLocalVideo(boolean enable) {
        getSdk().enableLocalVideo(enable);
    }

    public void muteLocalAudioStream(boolean isMute) {
        getSdk().muteLocalAudioStream(isMute);
    }

    public void muteLocalVideoStream(boolean isMute) {
        getSdk().muteLocalVideoStream(isMute);
    }

    public void setEnableSpeakerphone(boolean enable) {
        getSdk().setEnableSpeakerphone(enable);
    }

    public void enableDualStreamMode(boolean enable) {
        getSdk().setParameters(String.format("{\"che.audio.live_for_comm\":%b}", enable));
        getSdk().enableDualStreamMode(enable);
        getSdk().setRemoteDefaultVideoStreamType(enable ? Constants.VIDEO_STREAM_LOW : Constants.VIDEO_STREAM_HIGH);
    }

    public void setRemoteVideoStreamType(int uid, @StreamType int streamType) {
        getSdk().setRemoteVideoStreamType(uid, streamType);
    }

    public void setRemoteDefaultVideoStreamType(@StreamType int streamType) {
        getSdk().setRemoteDefaultVideoStreamType(streamType);
    }

    public SurfaceView createRendererView(Context context) {
        return RtcEngine.CreateRendererView(context);
    }

    public void setupLocalVideo(@Nullable SurfaceView view, @RenderMode int renderMode) {
        log.d("setupLocalVideo %b", view != null);
        VideoCanvas canvas = new VideoCanvas(view, renderMode, 0);
        getSdk().setupLocalVideo(canvas);
    }

    public void startPreview() {
        getSdk().startPreview();
    }

    public void switchCamera() {
        getSdk().switchCamera();
    }

    public void setupRemoteVideo(@Nullable SurfaceView view, @RenderMode int renderMode, int uid) {
        log.d("setupRemoteVideo %b %d", view != null, uid);
        VideoCanvas canvas = new VideoCanvas(view, renderMode, uid);
        getSdk().setupRemoteVideo(canvas);
    }

    public void rate(@IntRange(from = 1, to = 5) int rating, @Nullable String description) {
        getSdk().rate(getSdk().getCallId(), rating, description);
    }

    public void enableLastMileTest(boolean enable) {
        if (enable) {
            getSdk().enableLastmileTest();
        } else {
            getSdk().disableLastmileTest();
        }
    }

    private IRtcEngineEventHandler eventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            log.i("onJoinChannelSuccess %s %d", channel, uid);
            for (RtcEventListener listener : listeners) {
                listener.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        @Override
        public void onRtcStats(RtcStats stats) {
            for (RtcEventListener listener : listeners) {
                listener.onRtcStats(stats);
            }
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            log.i("onUserJoined %d", uid);
            for (RtcEventListener listener : listeners) {
                listener.onUserJoined(uid, elapsed);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            log.i("onUserOffline %d", uid);
            for (RtcEventListener listener : listeners) {
                listener.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onAudioRouteChanged(int routing) {
            for (RtcEventListener listener : listeners) {
                listener.onAudioRouteChanged(routing);
            }
        }

        @Override
        public void onLastmileQuality(int quality) {
            for (RtcEventListener listener : listeners) {
                listener.onLastmileQuality(quality);
            }
        }
    };
}
