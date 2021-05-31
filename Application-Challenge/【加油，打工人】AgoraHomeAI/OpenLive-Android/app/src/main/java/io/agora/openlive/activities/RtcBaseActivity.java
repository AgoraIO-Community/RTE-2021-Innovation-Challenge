package io.agora.openlive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.text.TextUtils;
import android.widget.Toast;

import io.agora.openlive.Constants;
import io.agora.openlive.rtc.EventHandler;
import io.agora.openlive.utils.MessageUtil;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.openlive.R;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;

public abstract class RtcBaseActivity extends BaseActivity implements EventHandler,ResultCallback{
    private final String TAG = LoginActivity.class.getSimpleName();
    public  String baseChatName="robot";
    boolean    mIsInChat = false;
  //  private boolean mIsPeerToPeerMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRtcEventHandler(this);        
        joinChannel();
        joinChat();
    }

    private void configVideo() {
        VideoEncoderConfiguration configuration = new VideoEncoderConfiguration(
                Constants.VIDEO_DIMENSIONS[config().getVideoDimenIndex()],
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        );
        configuration.mirrorMode = Constants.VIDEO_MIRROR_MODES[config().getMirrorEncodeIndex()];
        rtcEngine().setVideoEncoderConfiguration(configuration);
    }

    private void joinChannel() {
        // Initialize token, extra info here before joining channel
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name and uid that
        // you use to generate this token.
        String token = getString(R.string.agora_access_token);
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
//创建RTM 回调
    @Override
    public void onSuccess(Object o) {
        Log.i(TAG, "login success");
        mIsInChat=true;
        runOnUiThread(() -> {
         //   Intent intent = new Intent(LoginActivity.this, SelectionActivity.class);
         //   intent.putExtra(MessageUtil.INTENT_EXTRA_USER_ID, mUserId);
        //    startActivity(intent);
     //       Intent intent = new Intent(this, MessageActivity.class);
        //    intent.putExtra(MessageUtil.INTENT_EXTRA_IS_PEER_MODE, mIsPeerToPeerMode);
         //   intent.putExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME, mTargetName);
          //  intent.putExtra(MessageUtil.INTENT_EXTRA_USER_ID, config().getChannelName());

        });
    }

    @Override
    public void onFailure(ErrorInfo errorInfo) {
        Log.i(TAG, "login failed: " + errorInfo.getErrorCode());
        runOnUiThread(() -> {
            mIsInChat = false;
            showToast(getString(R.string.login_failed));
        });
    }

    /**
     * API CALL: login RTM server
     */
    private void joinChat() {
        //     mIsInChat = true;
        String token = getString(R.string.agora_access_token);
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        rtmClient().login(token, /*config().getChannelName()*/baseChatName, this);
    }


    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        // Render local/remote video on a SurfaceView

        SurfaceView surface = RtcEngine.CreateRendererView(getApplicationContext());
        if (local) {
            rtcEngine().setupLocalVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            0,
                            Constants.VIDEO_MIRROR_MODES[config().getMirrorLocalIndex()]
                    )
            );
        } else {
            rtcEngine().setupRemoteVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            uid,
                            Constants.VIDEO_MIRROR_MODES[config().getMirrorRemoteIndex()]
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
        if (mIsInChat) {
            rtmClient().logout(null);
            MessageUtil.cleanMessageListBeanList();
        }
    }
     void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
