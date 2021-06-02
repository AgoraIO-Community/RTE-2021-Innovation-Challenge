package io.agora.openlive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.agora.openlive.AgoraApplication;
import io.agora.openlive.R;
import io.agora.openlive.adapter.MessageAdapter;
import io.agora.openlive.model.MessageBean;
import io.agora.openlive.model.MessageListBean;
import io.agora.openlive.stats.LocalStatsData;
import io.agora.openlive.stats.RemoteStatsData;
import io.agora.openlive.stats.StatsData;
import io.agora.openlive.ui.VideoGridContainer;
import io.agora.openlive.utils.ImageUtil;
import io.agora.openlive.utils.MessageUtil;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;

public class LiveActivity extends RtcBaseActivity {

    private static final String TAG = LiveActivity.class.getSimpleName();

    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;
    private ImageView mMuteVideoBtn;

    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    private  ESP8266Connector esp8266Connector;
    private RelativeLayout carContralLayout;
    private Button forwardBtn, leftBtn, stopBtn,rightBtn,backBtn; /*  打开串口 关闭串口 发送数据 按钮*/
    private SeekBar seekBtn;
    //对话
    private TextView mTitleTextView;
    private EditText mMsgEditText;
    private ImageView mBigImage;
    private RecyclerView mRecyclerView;
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    private String mUserId = "";
    private String mPeerId = "";
    private String mChannelName = "";
    private int mChannelMemberCount = 1;

    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private RtmClientListener mClientListener;
    private RtmChannel mRtmChannel;
    private boolean isRobot=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_live_room);
        carContralLayout=findViewById(R.id.controlLayout0);
        carContralLayout.setVisibility(View.INVISIBLE);
        int role = getIntent().getIntExtra(
                io.agora.openlive.Constants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);
        isRobot=  (role == Constants.CLIENT_ROLE_BROADCASTER);
        baseChatName= isRobot ?"robot":"admin";
        super.onCreate(savedInstanceState);
        initUI();
        initChat();
        initData();

    }
    private void initChat() {
        mChatManager = ((AgoraApplication)getApplication()).getChatManager();
        mRtmClient =rtmClient();// mChatManager.getRtmClient();
        mClientListener = new LiveActivity.MyRtmClientListener();
        mChatManager.registerListener(mClientListener);
        Intent intent = getIntent();
        //对话人
        mUserId = intent.getStringExtra(MessageUtil.INTENT_EXTRA_USER_ID);
        String targetName = intent.getStringExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME);

        mTitleTextView = findViewById(R.id.message_title);
            mChannelName = targetName;
            mChannelMemberCount = 1;
            mTitleTextView.setText(MessageFormat.format("{0}({1})", mChannelName, mChannelMemberCount));
            createAndJoinChannel();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
                if (!TextUtils.isEmpty(message.getCacheFile())) {
                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
                    mBigImage.setVisibility(View.VISIBLE);
                } else {
                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String file) {
                            message.setCacheFile(file);
                            runOnUiThread(() -> {
                                Glide.with(LiveActivity.this).load(file).into(mBigImage);
                                mBigImage.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {

                        }
                    });
                }
            }
        });
        mRecyclerView = findViewById(R.id.message_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMessageAdapter);
      //  mMsgEditText = findViewById(R.id.message_edittiext);
      //  mBigImage = findViewById(R.id.big_image);
    }
    private void initUI() {
        TextView roomName = findViewById(R.id.live_room_name);
        roomName.setText(config().getChannelName());
        roomName.setSelected(true);
        initUserIcon();
        int role = getIntent().getIntExtra(
                io.agora.openlive.Constants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);
        boolean isBroadcaster =  (role == Constants.CLIENT_ROLE_BROADCASTER);
        baseChatName= isBroadcaster ?"robot":"admin";
        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video);
        mMuteVideoBtn.setActivated(isBroadcaster);

        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio);
        mMuteAudioBtn.setActivated(isBroadcaster);

       // ImageView beautyBtn = findViewById(R.id.live_btn_beautification);
      //  beautyBtn.setActivated(true);
      //  rtcEngine().setBeautyEffectOptions(beautyBtn.isActivated(),
    //            io.agora.openlive.Constants.DEFAULT_BEAUTY_OPTIONS);

        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());
        rtcEngine().setClientRole(role);
        if (isBroadcaster){
            startBroadcast();
            //开启远程控制支持
            esp8266Connector=new ESP8266Connector(this,"192.168.10.207","80");
        }else{
            seekBtn=findViewById(R.id.seekBar);
            seekBtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    String deg=""+seekBar.getProgress();
                   // esp8266Connector.turnCamera(deg);
                    String finalMessage = deg;
                    runOnUiThread(() -> {
                        if (!finalMessage.equals("")) {
                            RtmMessage mess = mRtmClient.createMessage();
                            mess.setText(finalMessage);
                            MessageBean sendBean = new MessageBean(mUserId, mess, false);
                            mMessageBeanList.add(sendBean);
                            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                            sendChannelMessage(mess);
                            Log.e("TAG", mUserId);
                        }
                    });
                }
            });
//展示控制界面
            carContralLayout.setVisibility(View.VISIBLE);
        }

    }
    public void onCarClick(View v) {
        //控制按钮
        String message = "";
        switch (v.getId()) {
            case R.id.for_btn:
                message = "forward";
                break;
            case R.id.left_btn:
                message = "left";
                break;
            case R.id.right_btn:
                message = "right";
                break;
            case R.id.stop_btn:
                message = "stop";
                break;
            case R.id.back_btn:
                message = "back";
                break;
        }

        String finalMessage = message;
        runOnUiThread(() -> {
            if (!finalMessage.equals("")) {
                RtmMessage mess = mRtmClient.createMessage();
                mess.setText(finalMessage);
                MessageBean sendBean = new MessageBean(mUserId, mess, false);
                mMessageBeanList.add(sendBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                sendChannelMessage(mess);
                Log.e("TAG", mUserId);
            }
        });
    }
    /**
     * API CALL: create and join channel
     */
    private void createAndJoinChannel() {
        // step 1: create a channel instance
        mRtmChannel = mRtmClient.createChannel(mChannelName, new MyChannelListener());
        if (mRtmChannel == null) {
            showToast(getString(R.string.join_channel_failed));
            finish();
            return;
        }
        Log.e("channel", mRtmChannel + "");
        // step 2: join the channel
        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i(TAG, "join channel success");
                getChannelMemberList();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "join channel failed");
                runOnUiThread(() -> {
                    showToast(getString(R.string.join_channel_failed));
                    finish();
                });
            }
        });
    }

    /**
     * API CALL: get channel member list
     */
    private void getChannelMemberList() {
        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(final List<RtmChannelMember> responseInfo) {
                runOnUiThread(() -> {
                    mChannelMemberCount = responseInfo.size();
                    refreshChannelTitle();
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "failed to get channel members, err: " + errorInfo.getErrorCode());
            }
        });
    }

    /**
     * API CALL: send message to a channel
     */
    private void sendChannelMessage(RtmMessage message) {
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.ChannelMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT:
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE:
                            showToast(getString(R.string.send_msg_failed));
                            break;
                    }
                });
            }
        });
    }

    /**
     * API CALL: leave and release channel
     */
    private void leaveAndReleaseChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
            mRtmChannel = null;
        }
    }


    private void initUserIcon() {
        Bitmap origin = BitmapFactory.decodeResource(getResources(), R.drawable.fake_user_icon);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), origin);
        drawable.setCircular(true);
        ImageView iconView = findViewById(R.id.live_name_board_icon);
        iconView.setImageDrawable(drawable);
    }

    private void initData() {
        mVideoDimension = io.agora.openlive.Constants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];

    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }

    private void startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);
        mVideoGridContainer.addUserVideoSurface(0, surface, true);
        mMuteAudioBtn.setActivated(true);
    }

    private void stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideo(0, true);
        mMuteAudioBtn.setActivated(false);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRemoteUser(uid);
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //渲染远程进入的用户
                renderRemoteUser(uid);
            }
        });
    }

    private void renderRemoteUser(int uid) {
        SurfaceView surface = prepareRtcVideo(uid, false);
        mVideoGridContainer.addUserVideoSurface(uid, surface, false);
    }

    private void removeRemoteUser(int uid) {
        removeRtcVideo(uid, false);
        mVideoGridContainer.removeUserVideo(uid, false);
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));
    }

    @Override
    public void finish() {
        super.finish();
        statsManager().clearAllData();
        //清理对话信息
        leaveAndReleaseChannel();
        mChatManager.unregisterListener(mClientListener);
    }

    public void onLeaveClicked(View view) {
        finish();
    }

    public void onSwitchCameraClicked(View view) {
        rtcEngine().switchCamera();
    }

    public void onMuteAudioClicked(View view) {
        if (!mMuteVideoBtn.isActivated()) return;

        rtcEngine().muteLocalAudioStream(view.isActivated());
        view.setActivated(!view.isActivated());
    }

    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            stopBroadcast();
        } else {
            startBroadcast();
        }
        view.setActivated(!view.isActivated());
    }

/**
 * API CALLBACK: rtm event listener
 */
class MyRtmClientListener implements RtmClientListener {

    @Override
    public void onConnectionStateChanged(final int state, int reason) {
        runOnUiThread(() -> {
            switch (state) {
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                    showToast(getString(R.string.reconnecting));
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                    showToast(getString(R.string.account_offline));
                    setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                    finish();
                    break;
            }
        });
    }

    @Override
    public void onMessageReceived(final RtmMessage message, final String peerId) {
        runOnUiThread(() -> {
            if (peerId.equals(mPeerId)) {
                MessageBean messageBean = new MessageBean(peerId, message, false);
                messageBean.setBackground(getMessageColor(peerId));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            } else {
                MessageUtil.addMessageBean(peerId, message);
            }
        });
    }

    @Override
    public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
        runOnUiThread(() -> {
            if (peerId.equals(mPeerId)) {
                MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
                messageBean.setBackground(getMessageColor(peerId));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            } else {
                MessageUtil.addMessageBean(peerId, rtmImageMessage);
            }
        });
    }

    @Override
    public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

    }

    @Override
    public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

    }


}

/**
 * API CALLBACK: rtm channel event listener
 */
class MyChannelListener implements RtmChannelListener {
    @Override
    public void onMemberCountUpdated(int i) {

    }

    @Override
    public void onAttributesUpdated(List<RtmChannelAttribute> list) {

    }

    @Override
    public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
        runOnUiThread(() -> {
            String account = fromMember.getUserId();
            Log.i(TAG, "onMessageReceived account = " + account + " msg = " + message);
            MessageBean messageBean = new MessageBean(account, message, false);
            messageBean.setBackground(getMessageColor(account));
            mMessageBeanList.add(messageBean);
            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            Log.e("TAG",message.getText());
            if(isRobot) {
                switch (message.getText()) {
                    case "forward":
                        esp8266Connector.moveForward();
                        break;
                    case "left":
                        esp8266Connector.turnLeft();
                        break;
                    case "right":
                        esp8266Connector.turnRight();
                        break;
                    case "stop":
                        esp8266Connector.stopMoving();
                        break;
                    case "back":
                        esp8266Connector.moveBackward();
                        break;
                    default:
                        if(isInteger(message.getText())){
                           // int deg=Integer.valueOf(message.getText());
                            esp8266Connector.turnCamera(message.getText());
                        }
                }
            }
        });
    }
    //是否是字符串数字
    public  boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    @Override
    public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
        runOnUiThread(() -> {
            String account = rtmChannelMember.getUserId();
            Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
            MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);
            messageBean.setBackground(getMessageColor(account));
            mMessageBeanList.add(messageBean);
            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
        });
    }

    @Override
    public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberJoined(RtmChannelMember member) {
        runOnUiThread(() -> {
            mChannelMemberCount++;
            refreshChannelTitle();
        });
    }

    @Override
    public void onMemberLeft(RtmChannelMember member) {
        runOnUiThread(() -> {
            mChannelMemberCount--;
            refreshChannelTitle();
        });
    }
}

    private int getMessageColor(String account) {
        for (int i = 0; i < mMessageBeanList.size(); i++) {
            if (account.equals(mMessageBeanList.get(i).getAccount())) {
                return mMessageBeanList.get(i).getBackground();
            }
        }
        return MessageUtil.COLOR_ARRAY[MessageUtil.RANDOM.nextInt(MessageUtil.COLOR_ARRAY.length)];
    }

    private void refreshChannelTitle() {
        String titleFormat = getString(R.string.channel_title);
        String title = String.format(titleFormat, mChannelName, mChannelMemberCount);
        mTitleTextView.setText(title);
    }

}
