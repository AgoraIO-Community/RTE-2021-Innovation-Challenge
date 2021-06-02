package com.kangaroo.openlive.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.kangaroo.openlive.Constants;
import com.kangaroo.openlive.R;
import com.kangaroo.openlive.rtc.EventHandler;
import com.kangaroo.openlive.utils.media.RtcTokenBuilder;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public abstract class RtcBaseActivity extends BaseActivity implements EventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRtcEventHandler(this);        
        joinChannel();
    }

    private void configVideo() {
        VideoEncoderConfiguration configuration = new VideoEncoderConfiguration(
                com.kangaroo.openlive.Constants.VIDEO_DIMENSIONS[config().getVideoDimenIndex()],
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        );
        configuration.mirrorMode = com.kangaroo.openlive.Constants.VIDEO_MIRROR_MODES[config().getMirrorEncodeIndex()];
        rtcEngine().setVideoEncoderConfiguration(configuration);
    }

    private void joinChannel() {
        // Initialize token, extra info here before joining channel
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name and uid that
        // you use to generate this token.
        String id = agoraApplication.appId;
        String ce = agoraApplication.appCe;
        String channel = config().getChannelName();

        int timestamp = (int)(System.currentTimeMillis() / 1000 + 3600);
        String token = new RtcTokenBuilder().buildTokenWithUid(id, ce,
                channel, 0, RtcTokenBuilder.Role.Role_Publisher, timestamp);


        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }

        // Sets the channel profile of the Agora RtcEngine.
        // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
        rtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        rtcEngine().enableVideo();
        configVideo();
        rtcEngine().joinChannel(token, config().getChannelName(), "", 0);
    }

    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        // Render local/remote video on a SurfaceView

        SurfaceView surface = RtcEngine.CreateRendererView(getApplicationContext());
        if (local) {
            rtcEngine().setupLocalVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            0
                    )
            );
        } else {
            rtcEngine().setupRemoteVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            uid
                    )
            );
        }
        return surface;
    }

    protected void removeRtcVideo(int uid, boolean local) {
        if (local) {
            rtcEngine().setupLocalVideo(null);
        } else {
            rtcEngine().setupRemoteVideo(new VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeRtcEventHandler(this);
        rtcEngine().leaveChannel();
    }
}
