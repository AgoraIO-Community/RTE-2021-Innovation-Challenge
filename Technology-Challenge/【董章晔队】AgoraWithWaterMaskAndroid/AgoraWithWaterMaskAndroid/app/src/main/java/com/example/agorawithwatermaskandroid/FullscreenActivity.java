package com.example.agorawithwatermaskandroid;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.agora.extension.ExtensionManager;
import io.agora.extension.ResourceHelper;
import io.agora.extension.UtilsAsyncTask;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements UtilsAsyncTask.OnUtilsAsyncTaskEvents,io.agora.rtc2.IMediaExtensionObserver{

    //申请权限
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    private final static String TAG = "Agora_dzy java :";
    private static final int PERMISSION_REQ_ID = 22;//why 22
    private static final String appId = "#YOUR APP ID#";
    private final static String channelName = "agora_extension";
    private Button addButton;
    private FrameLayout mContentView;
    private EditText  txtWaterMask;
    private int btnClickCnt;

    private RtcEngine mRtcEngine;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 500;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.view_container);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.add_button).setOnTouchListener(mDelayHideTouchListener);
        initUI();
        checkPermission();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initUI() {
        addButton = findViewById(R.id.add_button);

        btnClickCnt=0;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClickCnt == 1)
                {
                    btnClickCnt=0;
                    return;
                }
                btnClickCnt=1;
                if (addButton.isSelected()) {
                    Log.d(TAG, "disable water mask filter");
                    addButton.setSelected(false);
                    addButton.setText(R.string.add_watermask);
                    disableEffect();

                } else {
                    Log.d(TAG, "enable water mask filter");
                    addButton.setSelected(true);
                    addButton.setText(R.string.remove_watermask);
                    enableEffect();
                }
            }
        });
    }
    private void enableEffect() {
        JSONObject o = new JSONObject();
        try {
            txtWaterMask = (EditText)findViewById(R.id.wm_input);
            String wmStr = txtWaterMask.getText().toString();
            o.put("plugin.watermask.wmStr",wmStr);
            o.put("plugin.watermask.wmEffectEnabled", true);
            mRtcEngine.setExtensionProperty(Constants.MediaSourceType.VIDEO_SOURCE_CAMERA_PRIMARY, ExtensionManager.VENDOR_NAME_VIDEO, "key", o.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void disableEffect() {
        JSONObject o = new JSONObject();
        try {
            o.put("plugin.bytedance.wmEffectEnabled", false);
            mRtcEngine.setExtensionProperty(Constants.MediaSourceType.VIDEO_SOURCE_CAMERA_PRIMARY, ExtensionManager.VENDOR_NAME_VIDEO, "key", o.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setupLocalVideo() {
        SurfaceView view = RtcEngine.CreateRendererView(this);
        view.setZOrderMediaOverlay(true);
        mContentView.addView(view);
        mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        mRtcEngine.setLocalRenderMode(Constants.RENDER_MODE_FIT);//dzy change RENDER_MODE_ADAPTIVE HIDDEN
    }
    private void initAgoraEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = this;
            config.mAppId = appId;
            long videoProvider = ExtensionManager.nativeGetExtensionProvider(this, ExtensionManager.VENDOR_NAME_VIDEO,
                    ExtensionManager.PROVIDER_TYPE.LOCAL_VIDEO_FILTER.ordinal());
            Log.d(TAG, "Extension provider video: " + videoProvider);
            config.addExtension(ExtensionManager.VENDOR_NAME_VIDEO, videoProvider);
            config.mExtensionObserver = this;
            config.mEventHandler = new IRtcEngineEventHandler() {
                @Override
                public void onJoinChannelSuccess(String s, int i, int i1) {
                    Log.d(TAG, "onJoinChannelSuccess");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRtcEngine.startPreview();
                        }
                    });
                }

                @Override
                public void onFirstRemoteVideoDecoded(final int i, int i1, int i2, int i3) {
                    super.onFirstRemoteVideoDecoded(i, i1, i2, i3);
                    Log.d(TAG, "onFirstRemoteVideoDecoded  uid = " + i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //setupRemoteVideo(i); //TODO not necessary,远端不重要
                        }
                    });
                }

                @Override
                public void onUserJoined(int i, int i1) {
                    super.onUserJoined(i, i1);
                    Log.d(TAG, "onUserJoined  uid = " + i);
                }

                @Override
                public void onUserOffline(final int i, int i1) {
                    super.onUserOffline(i, i1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //onRemoteUserLeft();//TODO not necessary,远端不重要
                        }
                    });
                }
            };
            mRtcEngine = RtcEngine.create(config);
            setupLocalVideo();//显示本地图像------------------------------------------
            VideoEncoderConfiguration configuration = new VideoEncoderConfiguration(640, 360,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE);
            mRtcEngine.setVideoEncoderConfiguration(configuration);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            mRtcEngine.enableLocalVideo(true);
            mRtcEngine.enableVideo();
            Log.d(TAG, "api call join channel");
            mRtcEngine.joinChannel("", channelName, "", 0);
            mRtcEngine.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {//权限检查后，初始化
        Log.d(TAG, "checkPermission");
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)) {
            initAgoraEngine();//初始化
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute() {

    }

    @Override
    public void onEvent(String s, String s1, String s2) {

    }
}