package org.lql.movie_together.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import org.lql.movie_together.R;
import org.lql.movie_together.adapters.InChannelMessageListAdapter;
import org.lql.movie_together.adapters.VideoItemAdapter;
import org.lql.movie_together.model.AGEventHandler;
import org.lql.movie_together.model.Command;
import org.lql.movie_together.model.DuringCallEventHandler;
import org.lql.movie_together.model.Message;
import org.lql.movie_together.model.UserStatusData;
import org.lql.movie_together.model.VideoInfoData;
import org.lql.movie_together.ui.base.BaseActivity;
import org.lql.movie_together.ui.face.callback.CameraDataCallback;
import org.lql.movie_together.ui.face.callback.FaceDetectCallBack;
import org.lql.movie_together.ui.face.camera.CameraPreviewManager;
import org.lql.movie_together.ui.face.listener.SdkInitListener;
import org.lql.movie_together.ui.face.manager.FaceSDKManager;
import org.lql.movie_together.ui.face.model.LivenessModel;
import org.lql.movie_together.ui.face.model.SingleBaseConfig;
import org.lql.movie_together.ui.face.rawaudio.MediaDataObserverPlugin;
import org.lql.movie_together.ui.face.rawaudio.MediaDataVideoObserver;
import org.lql.movie_together.ui.face.rawaudio.MediaPreProcessing;
import org.lql.movie_together.ui.face.utils.ToastUtils;
import org.lql.movie_together.ui.layout.MessageListDecoration;
import org.lql.movie_together.ui.layout.MyItemTouchHelperCallback;
import org.lql.movie_together.utils.Constant;
import org.lql.movie_together.utils.ConstantApp;
import org.lql.movie_together.utils.LogUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static com.baidu.idl.main.facesdk.model.BDFaceSDKCommon.BDFaceEmotionEnum.*;
import static com.baidu.idl.main.facesdk.model.BDFaceSDKCommon.BDFaceEmotion.*;

public class RoomActivity extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();

    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>();
    private RecyclerView mRvVideos;
    private VideoItemAdapter adapter;

    // ????????????
    private RelativeLayout mVideoViewContainer;
    private volatile boolean mVideoMuted = false;
    private volatile boolean mAudioMuted = false;
    private volatile boolean mMixingAudio = false;
    private volatile boolean mFullScreen = false;
    private volatile int mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT;
    private volatile boolean mRoutingPhone = false;
    SurfaceView surfaceV;


    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;
    public int mLayoutType = LAYOUT_TYPE_SMALL;

    // ????????????????????????
    private InChannelMessageListAdapter mMsgAdapter;
    private ArrayList<Message> mMsgList;

    // ????????????
    private MediaPlayer mediaPlayer;
    private int videoDuration = 0;
    private MediaController controller;
    private int bufferPercentage = 0;
    private SurfaceView videoSuf;
    private BarChart mEmotionChart;

    // ???????????????????????????map??????????????????map???????????????????????????????????????
    private Map<Integer, Map<BDFaceSDKCommon.BDFaceEmotion, Integer>> mEmotionRecordsMap= new LinkedHashMap<>();
    private Map<BDFaceSDKCommon.BDFaceEmotion, Integer> mPeriodEmotion = new LinkedHashMap<>();
    private final Handler mFaceRecordHandler = new Handler();
    private Runnable mCounterRunable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                float process =(float) mediaPlayer.getCurrentPosition() / videoDuration;
                int percent =(int) (process * 100) % 5;
                if (percent == 0) {
                    // put(??????????????????????????????????????????????????????????????????)
                    Map<BDFaceSDKCommon.BDFaceEmotion, Integer> map = new LinkedHashMap<>(mPeriodEmotion);
                    if (!mEmotionRecordsMap.containsKey((int)(process * 100) / 5))
                        mEmotionRecordsMap.put((int)(process * 100) / 5, map);
                    // ?????????????????????
                    initPeriodEmotion();
                    initEmotionStackBar();
                }
            }
            mFaceRecordHandler.postDelayed(this, 1000);
        }
    };
    private MediaDataObserverPlugin mediaDataObserverPlugin;
    private int mObserveUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room;
    }

    @Override
    protected void initUIandEvent() {
        // ??????????????????SDK
        activeFaceSDK();

        // ????????????????????????channel_name
        initActionBar();
        // ????????????
        initMediaPlayer();

        addEventHandler(duringCallEventHandler);
        mRvVideos = (RecyclerView) findViewById(R.id.recycler_videos);

        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);
        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);
        doConfigEngine(encryptionKey, encryptionMode);

        mVideoViewContainer = (RelativeLayout) findViewById(R.id.grid_video_view_container);
        surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        preview(true, surfaceV, 0);

        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);
        mUidsList.put(0, surfaceV); // get first surface view

        // ?????????????????????
        adapter = new VideoItemAdapter(this,0,mUidsList);
        adapter.setLocalUid(0);
        adapter.setHasStableIds(true);

        // ?????????
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRvVideos);

        mRvVideos.setAdapter(adapter);
        LinearLayoutManager manager = new GridLayoutManager(this,3);
        mRvVideos.setLayoutManager(manager);

        // ????????????
        initMessageList();
        String channelName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        notifyMessageChanged(new Message("0", "start join " + channelName + " as " + (config().mUid & 0xFFFFFFFFL)));

        joinChannel(channelName, config().mUid);

        // ??????????????????????????????
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        // ????????????????????????????????????????????????
        if (Constant.DEBUG_FACE_ENABLED) {
            // ?????????????????????????????????
            initPeriodEmotion();
            mFaceRecordHandler.postDelayed(mCounterRunable, 1000);

            mediaDataObserverPlugin = MediaDataObserverPlugin.the();
            MediaPreProcessing.setCallback(mediaDataObserverPlugin);
            MediaPreProcessing.setVideoCaptureByteBuffer(mediaDataObserverPlugin.byteBufferCapture);
            mediaDataObserverPlugin.addVideoObserver(mediaDataVideoObserver);
        }

    }

    // ??????????????????
    private DuringCallEventHandler duringCallEventHandler = new DuringCallEventHandler() {
        // ??????????????????
        @Override
        public void onUserJoined(int uid) {
            LogUtils.d("onUserJoined " + (uid & 0xFFFFFFFFL));
            // ????????????
            doRenderRemoteUi(uid);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyMessageChanged(new Message("0", "user " + (uid & 0xFFFFFFFFL) + " joined"));
                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            LogUtils.d("onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " " + width + " " + height + " " + elapsed);

        }

        private void doRenderRemoteUi(final int uid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }

                    if (mUidsList.containsKey(uid)) {
                        return;
                    }

                /*
                  Creates the video renderer view.
                  CreateRendererView returns the SurfaceView type. The operation and layout of the
                  view are managed by the app, and the Agora SDK renders the view provided by the
                  app. The video display view must be created using this method instead of
                  directly calling SurfaceView.
                 */
                    SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                    mUidsList.put(uid, surfaceV);

                    boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT;

                    surfaceV.setZOrderOnTop(true);
                    surfaceV.setZOrderMediaOverlay(true);

                /*
                  Initializes the video view of a remote user.
                  This method initializes the video view of a remote stream on the local device. It affects only the video view that the local user sees.
                  Call this method to bind the remote video stream to a video view and to set the rendering and mirror modes of the video view.
                 */
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                    if (adapter != null) {
                        adapter.notifyUiChanged(0,mUidsList);
                    }
                    /*if (useDefaultLayout) {
                        LogUtils.d("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                        // switchToDefaultVideoView();

                    } else {
                       *//* int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                        log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                        switchToSmallVideoView(bigBgUid);*//*
                    }*/

                    notifyMessageChanged(new Message("0", "video from user " + (uid & 0xFFFFFFFFL) + " decoded"));
                }
            });
        }

        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            LogUtils.d("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
            mObserveUid = uid;
            if (mediaDataObserverPlugin != null) {
                mediaDataObserverPlugin.addDecodeBuffer(uid);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            LogUtils.d("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
            doRemoveRemoteUi(uid);
        }

        private void doRemoveRemoteUi(final int uid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }

                    Object target = mUidsList.remove(uid);
                    if (target == null) {
                        return;
                    }
                    int bigBgUid = -1;
                    LogUtils.d("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

                    if (adapter != null) {
                        adapter.notifyUiChanged(0,mUidsList);
                    }
                    notifyMessageChanged(new Message(uid+"", "user " + (uid & 0xFFFFFFFFL) + " left"));
                }
            });
        }

        @Override
        public void onExtraCallback(final int type, final Object... data) {
            runOnUiThread(() -> {
                if (isFinishing()) { return; }
                doHandleExtraCallback(type, data);
            });
        }
    };

    private void initMessageList() {
        mMsgList = new ArrayList<>();
        RecyclerView msgListView = (RecyclerView) findViewById(R.id.msg_list);
        msgListView.setVisibility(Constant.DEBUG_INFO_ENABLED ? View.VISIBLE : View.INVISIBLE);
        mMsgAdapter = new InChannelMessageListAdapter(this, mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
    }

    private void notifyMessageChanged(Message msg) {
        mMsgList.add(msg);
        int MAX_MESSAGE_COUNT = 16;
        if (mMsgList.size() > MAX_MESSAGE_COUNT) {
            int toRemove = mMsgList.size() - MAX_MESSAGE_COUNT;
            for (int i = 0; i < toRemove; i++) {
                mMsgList.remove(i);
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    private int getVideoEncResolutionIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncResolutionIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX);
        if (videoEncResolutionIndex > ConstantApp.VIDEO_DIMENSIONS.length - 1) {
            videoEncResolutionIndex = ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, videoEncResolutionIndex);
            editor.apply();
        }
        return videoEncResolutionIndex;
    }

    private int getVideoEncFpsIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncFpsIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX);
        if (videoEncFpsIndex > ConstantApp.VIDEO_FPS.length - 1) {
            videoEncFpsIndex = ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, videoEncFpsIndex);
            editor.apply();
        }
        return videoEncFpsIndex;
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        VideoEncoderConfiguration.VideoDimensions videoDimension = ConstantApp.VIDEO_DIMENSIONS[getVideoEncResolutionIndex()];
        VideoEncoderConfiguration.FRAME_RATE videoFps = ConstantApp.VIDEO_FPS[getVideoEncFpsIndex()];
        configEngine(videoDimension, videoFps, encryptionKey, encryptionMode);
    }

    @Override
    protected void deInitUIandEvent() {
        if (null != mediaPlayer){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaDataObserverPlugin != null) {
            mediaDataObserverPlugin.removeVideoObserver(mediaDataVideoObserver);
            mediaDataObserverPlugin.removeAllBuffer();
        }

        doLeaveChannel();
    }

    // ????????????????????????
    private void initActionBar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.ard_agora_actionbar);
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        controller = new MediaController(this);
        controller.setAnchorView(findViewById(R.id.root_ll));

        // ?????????SurfaceView
        videoSuf = (SurfaceView) findViewById(R.id.controll_surfaceView);
        videoSuf.setZOrderOnTop(true);
        //videoSuf.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        videoSuf.getHolder().addCallback(mSurfaceCallback);

        // ????????????
        try {
            Uri yaSo = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.videoplayback);
            mediaPlayer.setDataSource(this, yaSo);
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            mediaPlayer.setOnPreparedListener(mp ->{
                videoDuration = mediaPlayer.getDuration();
                changeVideoSize();
                mediaPlayer.start();
            });
            mediaPlayer.setLooping(true);

            controller.setMediaPlayer(mMediaPlayerControl);
            controller.setEnabled(true);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    private void doLeaveChannel() {
        leaveChannel(config().mChannel);
        preview(false, null, 0);
    }

    // *************????????????????????????*************
    // ????????????????????????
    public void onSwitchCameraClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.switchCamera();
    }

    // ????????????????????????
    public void onMixingAudioClicked(View view) {
        if (mUidsList.size() == 0) { return; }
        mMixingAudio = !mMixingAudio;
        RtcEngine rtcEngine = rtcEngine();
        if (mMixingAudio) {
            rtcEngine.startAudioMixing(Constant.MIX_FILE_PATH, false, false, -1);
        } else
            rtcEngine.stopAudioMixing();
        ImageView iv = (ImageView) view;
        iv.setImageResource(mMixingAudio ? R.drawable.btn_audio_mixing : R.drawable.btn_audio_mixing_off);
    }

    // ?????????????????????????????????
    public void onSwitchSpeakerClicked(View view) {
        mRoutingPhone = !mRoutingPhone;
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.setEnableSpeakerphone(mRoutingPhone);
        ImageView iv = (ImageView) view;
        iv.setImageResource(mRoutingPhone ? R.drawable.btn_mianti : R.drawable.btn_tingtong);
    }

    // ????????????
    public void onHangupClicked(View view) {
        LogUtils.d("onHangupClicked " + view);
        finish();
    }

    // ????????????????????????
    public void onFilterClicked(View view) {
        Constant.BEAUTY_EFFECT_ENABLED = !Constant.BEAUTY_EFFECT_ENABLED;

        if (Constant.BEAUTY_EFFECT_ENABLED) {
            setBeautyEffectParameters(Constant.BEAUTY_EFFECT_DEFAULT_LIGHTNESS, Constant.BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, Constant.BEAUTY_EFFECT_DEFAULT_REDNESS);
            enablePreProcessor();
        } else {
            disablePreProcessor();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(Constant.BEAUTY_EFFECT_ENABLED ? R.drawable.btn_filter : R.drawable.btn_filter_off);
    }

    // ?????????????????????????????????
    public void onVideoMuteClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        ViewParent parent;
        if (surfaceV == null || (parent = surfaceV.getParent()) == null) {
            LogUtils.d("onVoiceChatClicked " + view + " " + surfaceV);
            return;
        }
        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
            if (mediaDataObserverPlugin != null)
                mediaDataObserverPlugin.removeDecodeBuffer(mObserveUid);
        } else {
            rtcEngine.enableVideo();
            if (mediaDataObserverPlugin != null)
                mediaDataObserverPlugin.addDecodeBuffer(mObserveUid);
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(mVideoMuted ? R.drawable.btn_camera_off : R.drawable.btn_camera);

        hideLocalView(mVideoMuted);
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        //doHideTargetView(uid, hide);
    }

    // ???????????????????????????
    public void onVoiceMuteClicked(View view) {
        if (mUidsList.size() == 0) { return; }
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);
        ImageView iv = (ImageView) view;
        iv.setImageResource(mAudioMuted ? R.drawable.btn_microphone_off : R.drawable.btn_microphone);
    }
    // *************????????????????????????*************


    // *************????????????????????????*************
    public void onClickVideo(View view) {
        if (controller.isShowing()) controller.hide();
        else controller.show();
    }

    // surface_view Callback
    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
            mediaPlayer.prepareAsync();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            //changeVideoSize(videoSuf.getWidth(), videoSuf.getHeight());
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    };

    // media_controller
    private MediaController.MediaPlayerControl mMediaPlayerControl = new MediaController.MediaPlayerControl() {
        @Override
        public void start() {
            initPeriodEmotion();
            mFaceRecordHandler.postDelayed(mCounterRunable,1000);

            RtcEngine rtcEngine = rtcEngine();
            Log.d(TAG,"??????????????????");
            String commandStr = new Gson().toJson(
                    new Command.Builder()
                            .commandType(Command.COMMAND_START).build());
            rtcEngine.sendStreamMessage(1, commandStr.getBytes());

            if (null != mediaPlayer)
                mediaPlayer.start();
        }

        @Override
        public void pause() {
            mFaceRecordHandler.removeCallbacks(mCounterRunable);

            RtcEngine rtcEngine = rtcEngine();
            //int streamId = rtcEngine.createDataStream(new DataStreamConfig());
           /* byte[] commands =(String.valueOf(Constant.MEDIA_PAUSE)).getBytes();
            rtcEngine.sendStreamMessage(streamId, commands);*/
            Log.d(TAG, "??????????????????");
            String commandStr = new Gson().toJson(
                    new Command.Builder()
                            .commandType(Command.COMMAND_PAUSE).build());
            rtcEngine.sendStreamMessage(1, commandStr.getBytes());
            if (null != mediaPlayer)
                mediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            if (null != mediaPlayer)
                mediaPlayer.seekTo(pos);

            RtcEngine rtcEngine = rtcEngine();
            //int streamId = rtcEngine.createDataStream(new DataStreamConfig());
            /*byte[] commands =(String.valueOf(Constant.MEDIA_START)).getBytes();
            rtcEngine.sendStreamMessage(streamId, commands);*/
            Log.d(TAG, "????????????????????????,??????????????????"+mediaPlayer.getCurrentPosition());
            String commandStr = new Gson().toJson(
                    new Command.Builder()
                            .content(String.valueOf(mediaPlayer.getCurrentPosition()))
                            .commandType(Command.COMMAND_SEEK_TO).build());
            System.out.println(commandStr);
            rtcEngine.sendStreamMessage(1, commandStr.getBytes());
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return bufferPercentage;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return 0;
        }
    };

    // ???????????????
    public void changeVideoSize() {
        // ????????????video????????????
        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();

        // ???LinearLayout???????????? android:orientation="vertical" ??????
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_ll);
        int lw = linearLayout.getWidth();
        int lh = linearLayout.getHeight();

        //if (vWidth > lw || vHeight > lh) {
        // ??????video??????????????????????????????????????????????????????????????????
        float wRatio = (float) vWidth / (float) lw;
        float hRatio = (float) vHeight / (float) lh;

        // ??????????????????????????????
        float ratio = Math.max(wRatio, hRatio);
        vWidth = (int) Math.ceil((float) vWidth / ratio);
        vHeight = (int) Math.ceil((float) vHeight / ratio);

        // ??????surfaceView???????????????
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(vWidth, vHeight);
        lp.gravity = Gravity.CENTER;
        videoSuf.setLayoutParams(lp);
    }


    // *************????????????????????????*************

    // ????????????
    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                /*if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }*/
                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];
                //doHideTargetView(peerUid, muted);
                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                /*if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.rendererOutputFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoEncResolutionIndex();
                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(0)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }*/

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                /*if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }*/

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];
                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    String msg = getString(R.string.msg_connection_error)+"EVENT_TYPE_ON_APP_ERROR";
                    notifyMessageChanged(new Message("", msg));
                    showLongToast(msg);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:
                Log.d(TAG, "???????????????");
                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                notifyMessageChanged(new Message(peerUid+"",
                        new String(content)+"?????????"));
                Command command = new Gson().fromJson(new String(content), Command.class);
                System.out.println(command);
                switch (command.getCommandType()) {
                    case Command.COMMAND_MESSAGE:
                        break;
                    case Command.COMMAND_START:
                        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                        break;
                    case Command.COMMAND_PAUSE:
                        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                        break;
                    case Command.COMMAND_SEEK_TO:
                        int pos = Integer.parseInt(command.getContent());
                        if (null != mediaPlayer)
                            mediaPlayer.seekTo(pos);
                        break;
                }
                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];
                // notifyMessageChanged(new Message("", error + " " + description));
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                //notifyHeadsetPlugged((int) data[0]);
                break;

        }
    }

    // ??????????????????
    private MediaDataVideoObserver mediaDataVideoObserver = new MediaDataVideoObserver() {
        @Override
        public void onCaptureVideoFrame(byte[] rgbData, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
            dealRgb(rgbData);
        }

        @Override
        public void onRenderVideoFrame(int uid, byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {

        }

        @Override
        public void onPreEncodeVideoFrame(byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {

        }
    };

    private FaceAuth faceAuth;
    private void activeFaceSDK() {
        faceAuth = new FaceAuth();
        faceAuth.initLicenseOnLine(this, getResources().getString(R.string.face_license_id), new Callback() {
            @Override
            public void onResponse(final int code, final String response) {
                if (code == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           showShortToast("??????SDK????????????");
                           initFaceListener();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           showShortToast("??????SDK????????????");
                        }
                    });
                }
            }
        });
    }

    private void initFaceListener(){
        if (FaceSDKManager.initStatus != FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().initModel(this, new SdkInitListener() {
                @Override
                public void initStart() {
                }

                @Override
                public void initLicenseSuccess() {
                }

                @Override
                public void initLicenseFail(int errorCode, String msg) {
                }

                @Override
                public void initModelSuccess() {
                    FaceSDKManager.initModelSuccess = true;
                    LogUtils.d("?????????????????????????????????");
                    // ???????????????
                    SingleBaseConfig.getBaseConfig().setAttribute(true);
                    FaceSDKManager.getInstance().initConfig();
                    FaceSDKManager.isDetectMask = true;
                }

                @Override
                public void initModelFail(int errorCode, String msg) {
                    FaceSDKManager.initModelSuccess = false;
                    if (errorCode != -12) {
                        LogUtils.d("??????????????????????????????????????????");
                    }
                }
            });
        }
    }
    // RGB????????????????????????
    private static final int RGB_WIDTH = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int RGB_HEIGHT = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
    private void dealRgb(byte[] rgbData) {
        if (rgbData != null) {
            FaceSDKManager.getInstance().onAttrDetectCheck(rgbData, rgbData, rgbData, RGB_HEIGHT,
                    RGB_WIDTH, 1, new FaceDetectCallBack() {
                        @Override
                        public void onFaceDetectCallback(LivenessModel livenessModel) {
                            // ??????????????????
                            if (livenessModel != null) {
                                System.out.println(getMsg(livenessModel.getFaceInfo()));
                            }
                        }

                        @Override
                        public void onTip(int code, String msg) {
                            LogUtils.d(msg);
                        }

                        @Override
                        public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
                            System.out.println(null==livenessModel);
                        }
                    });
        }
    }

    public String getMsg(FaceInfo faceInfo) {
        StringBuilder msg = new StringBuilder();
        if (faceInfo != null && (faceInfo.age > 0)) {
            if (mPeriodEmotion != null) {
                int count = mPeriodEmotion.get(faceInfo.emotionThree);
                mPeriodEmotion.put(faceInfo.emotionThree, ++count);
            }
            msg.append(faceInfo.age);
            msg.append(",").append(faceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_CALM ?
                    "??????"
                    : faceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_SMILE ? "???"
                    : faceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_FROWN ? "??????" : "????????????");
            msg.append(",").append(faceInfo.emotionSeven == BDFACE_EMOTIONS_ANGRY? "??????"
                    : faceInfo.emotionSeven == BDFaceSDKCommon.BDFaceEmotionEnum.BDFACE_EMOTIONS_DISGUST ? "??????"
                    : faceInfo.emotionSeven == BDFaceSDKCommon.BDFaceEmotionEnum.BDFACE_EMOTIONS_FEAR ? "??????"
                    : faceInfo.emotionSeven == BDFaceSDKCommon.BDFaceEmotionEnum.BDFACE_EMOTIONS_HAPPY ? "??????"
                    : faceInfo.emotionSeven == BDFaceSDKCommon.BDFaceEmotionEnum.BDFACE_EMOTIONS_SAD ? "??????"
                    : faceInfo.emotionSeven == BDFaceSDKCommon.BDFaceEmotionEnum.BDFACE_EMOTIONS_SURPRISE ? "??????" : "?????????");
            msg.append(",").append(faceInfo.gender == BDFaceSDKCommon.BDFaceGender.BDFACE_GENDER_FEMALE ? "??????" :
                    faceInfo.gender == BDFaceSDKCommon.BDFaceGender.BDFACE_GENDER_MALE ? "??????" : "??????");
            msg.append(",").append(faceInfo.glasses == BDFaceSDKCommon.BDFaceGlasses.BDFACE_NO_GLASSES ? "?????????"
                    : faceInfo.glasses == BDFaceSDKCommon.BDFaceGlasses.BDFACE_GLASSES ? "?????????"
                    : faceInfo.glasses == BDFaceSDKCommon.BDFaceGlasses.BDFACE_SUN_GLASSES ? "??????" : "?????????");
            msg.append(",").append(faceInfo.race == BDFaceSDKCommon.BDFaceRace.BDFACE_RACE_YELLOW ? "?????????"
                    : faceInfo.race == BDFaceSDKCommon.BDFaceRace.BDFACE_RACE_WHITE ? "?????????"
                    : faceInfo.race == BDFaceSDKCommon.BDFaceRace.BDFACE_RACE_BLACK ? "?????????"
                    : faceInfo.race == BDFaceSDKCommon.BDFaceRace.BDFACE_RACE_INDIAN ? "?????????"
                    : "?????????");
        }
        return msg.toString();
    }

    private void initPeriodEmotion() {
        mPeriodEmotion.clear();
        mPeriodEmotion.put(BDFACE_EMOTION_CALM,1);
        mPeriodEmotion.put(BDFACE_EMOTION_SMILE,1);
        mPeriodEmotion.put(BDFACE_EMOTION_FROWN,1);
        /*
        * aceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_CALM ?
                    "??????"
                    : faceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_SMILE ? "???"
                    : faceInfo.emotionThree == BDFaceSDKCommon.BDFaceEmotion.BDFACE_EMOTION_FROWN ? "??????" : "????????????");
        * */
        /*mPeriodEmotion.put(BDFACE_EMOTIONS_ANGRY,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_DISGUST,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_FEAR,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_HAPPY,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_SAD,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_SURPRISE,1);
        mPeriodEmotion.put(BDFACE_EMOTIONS_NEUTRAL,1);*/
    }

    private void initEmotionStackBar() {
        mEmotionChart = findViewById(R.id.stack_bar_emotion);
        mEmotionChart.getDescription().setEnabled(false);
        XAxis xLabels = mEmotionChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        Legend l = mEmotionChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList singleValues = new ArrayList();
        // ??????????????????????????????
        for (int recordkey : mEmotionRecordsMap.keySet()) {
            Map<BDFaceSDKCommon.BDFaceEmotion, Integer> emotionMap = mEmotionRecordsMap.get(recordkey);
            LogUtils.d("????????????"+recordkey);
            // ????????????????????????????????????????????????
            for (BDFaceSDKCommon.BDFaceEmotion emotionEnum: emotionMap.keySet()) {
                LogUtils.d("?????????"+emotionEnum+":"+emotionMap.get(emotionEnum));
                singleValues.add((float) emotionMap.get(emotionEnum));
            }

            float[] value = new float[singleValues.size()];
            for(int i = 0; i < singleValues.size();i++){
                value[i] =(float) singleValues.get(i);
            }
            values.add(new BarEntry(recordkey, value, getResources().getDrawable(R.drawable.ic_launcher)));
            singleValues.clear();
        }

        BarDataSet barDataSet;
        if (mEmotionChart.getData() != null &&
                mEmotionChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mEmotionChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(values);
            mEmotionChart.getData().notifyDataChanged();
            mEmotionChart.notifyDataSetChanged();
        } else {
            barDataSet = new BarDataSet(values, "");
            barDataSet.setDrawIcons(false);
            /*int[] colors = {
                    Color.rgb(207, 248, 246), Color.rgb(148, 212, 212),
                    Color.rgb(136, 180, 187), Color.rgb(118, 174, 175),
                    Color.rgb(42, 109, 130), Color.rgb(187, 134, 252),
                    Color.rgb(1, 135, 134)
            };*/
            int[] colors = {
                    Color.rgb(42, 109, 130), Color.rgb(187, 134, 252),
                    Color.rgb(1, 135, 134)
            };
            barDataSet.setColors(colors);
            //barDataSet.setStackLabels(new String[]{"??????", "??????", "??????", "??????", "??????", "??????", "?????????"});
            barDataSet.setStackLabels(new String[]{"??????", "??????", "??????"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            mEmotionChart.setData(data);
        }

        mEmotionChart.setFitBars(true);
        mEmotionChart.invalidate();
    }

}