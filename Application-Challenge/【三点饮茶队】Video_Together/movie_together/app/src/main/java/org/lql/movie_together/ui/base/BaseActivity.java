package org.lql.movie_together.ui.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.lql.movie_together.MTApplication;
import org.lql.movie_together.R;
import org.lql.movie_together.model.AGEventHandler;
import org.lql.movie_together.model.CurrentUserSettings;
import org.lql.movie_together.model.EngineConfig;
import org.lql.movie_together.utils.Constant;
import org.lql.movie_together.utils.ConstantApp;
import org.lql.movie_together.utils.LogUtils;

import java.util.Arrays;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.internal.EncryptionConfig;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initUIandEvent();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        requestPermission();
    }

    public abstract int getLayoutId();

    protected abstract void initUIandEvent();

    protected abstract void deInitUIandEvent();

    // mt means Movie Together
    protected MTApplication getMtApplication() {
        return (MTApplication) getApplication();
    }

    protected RtcEngine rtcEngine() {
        return getMtApplication().getmRtcEngine();
    }

    protected EngineConfig config() {
        return getMtApplication().getmConfig();
    }

    protected void addEventHandler(AGEventHandler handler) {
        getMtApplication().addEventHandler(handler);
    }

    protected void removeEventHandler(AGEventHandler handler) {
        getMtApplication().remoteEventHandler(handler);
    }

    protected CurrentUserSettings vSettings() {
        return getMtApplication().getmVideoSettings();
    }

    // 预览本地视图
    protected void preview(boolean start, SurfaceView view, int uid) {
        if (start) {
            rtcEngine().setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            rtcEngine().startPreview();
        } else {
            rtcEngine().stopPreview();
        }
    }

    /**
     * 加入频道
     *
     * 成功加入以后会调用两个回调：
     * 本地回调方法onJoinChannelSuccess
     * 远程用户回调方法onUserJoined
     *
     * 若因为网络原因断开连接，声网服务器会自动进行重连，重连以后回调onRejoinChannelSuccess方法
     */
    public final void joinChannel(final String channel, int uid) {
        LogUtils.d("joinChannel " + channel + " " + uid);

        String accessToken = getApplicationContext().getString(R.string.agora_access_token);
        rtcEngine().joinChannel(accessToken, channel, "OpenVCall", uid);
        config().mChannel = channel;
        enablePreProcessor();
    }

    public final void leaveChannel(String channel) {
        LogUtils.d("leaveChannel " + channel);

        config().mChannel = null;
        disablePreProcessor();
        rtcEngine().leaveChannel();
        config().reset();
    }

    // 打开美颜
    protected void enablePreProcessor() {
        if (Constant.BEAUTY_EFFECT_ENABLED) {
            rtcEngine().setBeautyEffectOptions(true, Constant.BEAUTY_OPTIONS);
        }
    }

    public final void setBeautyEffectParameters(float lightness, float smoothness, float redness) {
        Constant.BEAUTY_OPTIONS.lighteningLevel = lightness;
        Constant.BEAUTY_OPTIONS.smoothnessLevel = smoothness;
        Constant.BEAUTY_OPTIONS.rednessLevel = redness;
    }

    // 关闭美颜
    protected void disablePreProcessor() {
        // do not support null when setBeautyEffectOptions to false
        rtcEngine().setBeautyEffectOptions(false, Constant.BEAUTY_OPTIONS);
    }

    // 配置rtc引擎
    protected void configEngine(VideoEncoderConfiguration.VideoDimensions videoDimension, VideoEncoderConfiguration.FRAME_RATE fps, String encryptionKey, String encryptionMode) {
        // 加密配置
        EncryptionConfig config = new EncryptionConfig();
        if (!TextUtils.isEmpty(encryptionKey)) {
            config.encryptionKey = encryptionKey;

            if(TextUtils.equals(encryptionMode, "AES-128-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_128_XTS;
            } else if(TextUtils.equals(encryptionMode, "AES-256-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_256_XTS;
            }
            rtcEngine().enableEncryption(true, config);
        } else {
            rtcEngine().enableEncryption(false, config);
        }

        LogUtils.d("configEngine " + videoDimension + " " + fps + " " + encryptionMode);
        // Set the Resolution, FPS. Bitrate and Orientation of the video
        //该方法设置视频编码属性。每个属性对应一套视频参数，如分辨率、帧率、码率、视频方向等。
        // 所有设置的参数均为理想情况下的最大值。
        // 当视频引擎因网络环境等原因无法达到设置的分辨率、帧率或码率的最大值时，会取最接近最大值的那个值。
        rtcEngine().setVideoEncoderConfiguration(new VideoEncoderConfiguration(videoDimension,
                fps,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        deInitUIandEvent();
        super.onDestroy();
    }

    // toast
    public final void showShortToast(final String msg) {
        this.runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        });
    }

    // 请求权限
    private void requestPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return;
                checkSelfPermissions();
            }
        },500);
    }

    public boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.CAMERA, ConstantApp.PERMISSION_REQ_ID_CAMERA);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        LogUtils.d("checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }

        if (Manifest.permission.CAMERA.equals(permission)) {
            permissionGranted();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtils.d("onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults));
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, ConstantApp.PERMISSION_REQ_ID_CAMERA);
                } else {
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    protected void permissionGranted() {
    }

    protected final int getStatusBarHeight() {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        if (statusBarHeight == 0) {
            LogUtils.e("无法获取状态栏高度");
        }

        return statusBarHeight;
    }

    protected final int getActionBarHeight() {
        // action bar height
        int actionBarHeight = 0;
        final TypedArray styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize}
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        if (actionBarHeight == 0) {
            LogUtils.e("无法获取顶部导航栏高度");
        }

        return actionBarHeight;
    }

}
