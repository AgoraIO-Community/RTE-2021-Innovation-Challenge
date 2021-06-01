package com.qgmodel.qggame.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.qgmodel.qggame.R;
import com.qgmodel.qggame.container.GridVideoViewContainer;
import com.qgmodel.qggame.custom.MyRelativeLayout;
import com.qgmodel.qggame.custom.RedLine;
import com.qgmodel.qggame.entity.ConstantApp;
import com.qgmodel.qggame.entity.MessageDetail;
import com.qgmodel.qggame.entity.MyCardSystem;
import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.entity.UserStatusData;
import com.qgmodel.qggame.handler.AGEventHandler;
import com.qgmodel.qggame.handler.DuringCallEventHandler;
import com.qgmodel.qggame.manager.MicManager;
import com.qgmodel.qggame.model.Model;
import com.qgmodel.qggame.rtmtutorial.AGApplication;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.view.activity.mainActivity.MainActivityImpl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.b.I;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmAttribute;
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
import io.agora.rtm.jni.IFileMessage;

public class CallActivity extends AppCompatActivity implements DuringCallEventHandler, View.OnClickListener {
    private static final String TAG = "CallActivity";

    private Handler handler = new Handler(Looper.myLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                micManager.start();
            }catch (Exception e){
                e.printStackTrace();
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    private GridVideoViewContainer gridVideoViewContainer;
    HashMap<Integer,SurfaceView> mUidsList = new HashMap<>();
    private int myId = 0;
    private String joinChannel = "666666";

    String accessToken = AGApplication.getContext().getString(R.string.agora_access_token);

    //add
    private RtmChannel mRtmChannel;
    private RtmClient mRtmClient;

    //add
    private List<MyRelativeLayout> cardNumLayouts = new ArrayList<>();
    private List<MyRelativeLayout> wordLayouts = new ArrayList<>();
    private List<ImageView> cats = new ArrayList<>();
    private List<Integer> leftUsers = new ArrayList<>();
    private List<ImageView> forbids = new ArrayList<>();
    private List<ImageView> kicks = new ArrayList<>();
    private List<ImageView> readys = new ArrayList<>();
    private int primaryCardNum = 10;
    private int userNum = 4;
    private int myLeft  = 10;
    private List<String> myWords = new ArrayList<>();
    private List<MessageDetail> messageList = new ArrayList<>();
    private MessageDetail myDetail;
    private boolean afterResetContainer = false;
    private int addCount;
    private Timer timer;
    private MessageDetail newMessageDetail;
    boolean receiveNewMessage = false;
    private final int RECEIVE_NEW_MESSAGE = 1;
    private int nowUserNum;
    private int changeCount;
    private AlertDialog.Builder changeDialog;
    private AlertDialog.Builder kickDialog;
    private  final int SINGLE_CHANGE = 2;
    private  final int NOTIFY_ALL = 3;
    private final int I_WIN = 4;
    private final int FORBID = 5;
    private final int I_AM_READY = 6;
    private final int I_LOSE = 7;
    private final int KICK_SOMEONE = 8;
    private final int GAME_START = 9;
    private final int LEAVE = 10;
    private final int KEEP_READY = 11;
    private final int I_AM_ROOM_OWNER = 12;
    private int changeMarginCount = 0;
    private boolean isMute = true;
    private boolean isBlind = false;
    private ImageView microphone;
    private ImageView video;
    private int nowState;
    private int nowReason;
    private boolean isRoomer = false;
    private boolean relieveForbid = true;
    private String roomerId;
    private MicManager micManager;
    private boolean afterGrid = false;
    private boolean iWin = false;
    private boolean voiceRecognition = false;
    int kickId = 0;
    int readyNum = 0;
    private ImageView readyImage;
    private ImageView startImage;
    private boolean canStartGame = false;
    private boolean alreadyStarted = false;
    private boolean haveReady = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        nowUserNum = 1;


        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                initUiAndEvent();
            }
        });

        voiceRecognition = getIntent().getBooleanExtra("voiceRecognition",true);

        //语音识别相关的初始化
        if (voiceRecognition){
            micManager = MicManager.getInstance();
            micManager.init(CallActivity.this);
            micManager.setTargetWord("666");
            //检测到靶词汇后的回调
            micManager.setOnWord(new MicManager.OnWord() {
                @Override
                public void onWord(String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(CallActivity.this,"检测到你说了 "+s,Toast.LENGTH_SHORT).show();

                            if (myWords.size()>0){
                                myWords.remove(0);
                                myLeft--;
                            }
                            TextView numText = null;
                            TextView wordText = null;
                            if (cardNumLayouts.size()>0&&wordLayouts.size()>0){
                                numText = (TextView) cardNumLayouts.get(0).findViewById(R.id.card_num_text);
                                wordText = wordLayouts.get(0).findViewById(R.id.text_word);

                                numText.setText(String.valueOf(myLeft));
                                if (myWords.size()>0){
                                    wordText.setText(myWords.get(0));
                                }else {
                                    wordText.setText("");
                                    MyRelativeLayout parentView = (MyRelativeLayout) wordText.getParent();
                                    if (parentView!=null){
                                        View layerView = parentView.findViewById(R.id.gray_layer);
                                        layerView.setAlpha(0.8f);
                                    }
                                }

                                RtmMessage firstMessage = mRtmClient.createMessage();
                                String text = null;
                                if (myWords.size()>0){
                                    text = combineMessage(myId,myWords.get(0),myLeft,SINGLE_CHANGE,0);
                                }else if (myWords.size() == 0){
                                    text = combineMessage(myId,"",myLeft,SINGLE_CHANGE,0);
                                }

                                firstMessage.setText(text);
                                sendChannelMessage(firstMessage);

                            }

                        }
                    });
                }
            });

            micManager.start();
            handler.postDelayed(runnable, 2000);

        }


        myLeft = primaryCardNum;
        setAlertDialog();
        setKickAlertDialog();

        mRtmClient = AGApplication.the().getChatManager().getRtmClient();




        myId = getIntent().getIntExtra("myId",0);


        accessToken = getIntent().getStringExtra("accessToken");



        //gc的
        roomerId = getIntent().getStringExtra("roomerId");
        isRoomer = roomerId.equals(myId+"");




        loginMessage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createAndJoinChannel();
            }
        },200);

        //add
        //模拟自己获得的牌
        MyCardSystem myCardSystem = new MyCardSystem(4,10);
        String[] resultCards = myCardSystem.getResultCards();
        for (int i=0;i<10;i++){
            myWords.add(resultCards[i]);
        }
        myDetail = new MessageDetail();
        myDetail.setTag(myId);
        myDetail.setWord(myWords.get(0));
        myDetail.setLeft(myLeft);

        if (getIntent().getStringExtra("videoChannel")!=null){
            joinChannel = getIntent().getStringExtra("videoChannel");
        }



        

    }

    public void loginMessage(){

        if (mRtmClient!=null){

            if ("".equals(accessToken)){
                accessToken = null;
            }
            mRtmClient.login(accessToken, String.valueOf(myId), new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "=== 登陆RTM成功");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.d(TAG, "=== 登录RTM失败 error--> "+errorInfo);
                }
            });
        }


    }


    //禁言
    public void forbidSomeBodyVoice(int forbidId){
        if (!isRoomer){
            return;
        }
        if (myWords.size()>0){
            //房主通过发送频道信息，包含指定的id，来将某人禁言
            String text = combineMessage(myId,myWords.get(0),myLeft,FORBID,forbidId);
            RtmMessage rtmMessage = mRtmClient.createMessage();
            rtmMessage.setText(text);
            sendChannelMessage(rtmMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cardNumLayouts!=null&&wordLayouts!=null){
            if (wordLayouts.size()>0){
                MyRelativeLayout relativeLayout = wordLayouts.get(0);
                Log.d(TAG, "=== Serializable? "+(relativeLayout instanceof Serializable));
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }




    public AGApplication application(){
        return (AGApplication) getApplication();
    }

    public RtcEngine getRtcEngine(){
        return application().getRtcEngine();
    }

    public void initUiAndEvent(){

        microphone = findViewById(R.id.image_microphone);
        video = findViewById(R.id.image_video);
        ImageView backImage = findViewById(R.id.iamge_back_to_home);
        readyImage = findViewById(R.id.image_ready);
        startImage = findViewById(R.id.image_start_game);

        readyImage.setOnClickListener(this);
        microphone.setOnClickListener(this);
        video.setOnClickListener(this);
        backImage.setOnClickListener(this);
        startImage.setOnClickListener(this);

        if (isRoomer){
            readyImage.setVisibility(View.INVISIBLE);
            startImage.setVisibility(View.VISIBLE);
        }else {
            readyImage.setVisibility(View.VISIBLE);
        }




        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        getRtcEngine().enableVideo();
        getRtcEngine().enableAudio();
        getRtcEngine().muteLocalAudioStream(true);



        doConfigEngine();

        application().addEventHandler(this);
        gridVideoViewContainer = findViewById(R.id.grid_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(AGApplication.getContext());
        preview(true,surfaceView,0);
        surfaceView.setZOrderOnTop(false);
        surfaceView.setZOrderMediaOverlay(false);
        mUidsList.put(myId,surfaceView);

        gridVideoViewContainer.initGridVideoViewContainer(this,myId,mUidsList);

        //玩家第一次进入频道的初始化（词语，卡牌数），需要RecyclerView（gridVideoViewContainer）更新完成后才执行
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gridVideoViewContainer.getCountContainers().size()>0&&gridVideoViewContainer.getWordLayouts().size()>0){
                    MyRelativeLayout cardNumLayout = gridVideoViewContainer.getCountContainers().get(0);
                    MyRelativeLayout wordLayout = gridVideoViewContainer.getWordLayouts().get(0);
                    TextView num = (TextView) cardNumLayout.getChildAt(1);
                    num.setText(myLeft+"");
                    TextView word = wordLayout.findViewById(R.id.text_word);
                    word.setText(myWords.get(0));
                    if (isRoomer){
                        RelativeLayout rootView = (RelativeLayout) wordLayout.getParent().getParent().getParent();
                        if (rootView!=null){
                            ImageView roomOwner = rootView.findViewById(R.id.room_owner);
                            if (roomOwner!=null){
                                roomOwner.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }
        },500);

        joinChannel(joinChannel,myId);

        optional();
    }

    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    public final void joinChannel(final String channel, int uid) {
        Log.d(TAG, "=== joinChannel: ");
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "<#YOUR ACCESS TOKEN#>")) {
            accessToken = null; // default, no token
        }
        application().getRtcEngine().joinChannel(accessToken, channel, "OpenVCall", uid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //gc的
        if (voiceRecognition){
            handler.removeCallbacks(runnable);
            micManager.onStop();
            handler = null;
        }


        getRtcEngine().disableVideo();
        getRtcEngine().disableAudio();
        //离开实时视频频道


        String text = null;
        if (haveReady){
            text = combineMessage(myId,"离开前已准备",myLeft,LEAVE,1);
        }else {
            text = combineMessage(myId,"离开前未准备",myLeft,LEAVE,2);
        }
        RtmMessage message = mRtmClient.createMessage();
        message.setText(text);
        sendChannelMessage(message);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                leaveVideoChannel();

                //离开实时信息频道
                if (mRtmChannel!=null){
                    mRtmChannel.leave(null);
                    mRtmChannel.release();
                    logoutMessage();
                }

                //gc的
                if (voiceRecognition){
                    Model.ROOM.deleteRoom(myId+"");
                }
            }
        },300);






    }

    //退出实时信息频道
    public void logoutMessage(){
//        RtmClient client = application().getChatManager().getRtmClient();
        mRtmClient.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "=== 登出RTM成功！");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(TAG, "=== 登出RTM失败 ERROR-->"+errorInfo);
            }
        });
    }


    //创建和加入实时信息频道
    private void createAndJoinChannel() {
        String mChannelName = "666666";
        mRtmChannel = mRtmClient.createChannel(mChannelName,new MyChannelListener());
        if (mRtmChannel == null) {
            finish();
            return;
        }

        Log.e("channel", mRtmChannel + "");

        try {
            mRtmChannel.join(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void responseInfo) {
                    Log.d(TAG, "=== join channel success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.d(TAG, "=== join channel failed error--> "+errorInfo);
                }
            });
        }catch (Exception e){
            Log.d(TAG, "=== e--> "+e);
        }

    }

    //add
    //组合信息
    public String combineMessage(int tag,String word,int left,int mark,int targetUid){
        return tag+"-"+word+"-"+left+"-"+mark+"-"+targetUid;
    }


    //解析收到的频道信息
    public MessageDetail parseMessage(String text){
        String[] ret = text.split("-");
        MessageDetail detail = new MessageDetail();
        if (ret.length==5){
            detail.setTag(Integer.parseInt(ret[0]));
            detail.setWord(ret[1]);
            detail.setLeft(Integer.parseInt(ret[2]));
            detail.setMark(Integer.parseInt(ret[3]));
            detail.setTargetId(Integer.parseInt(ret[4]));
        }
        return detail;
    }

    public void setKickAlertDialog(){
        kickDialog = new AlertDialog.Builder(CallActivity.this);
        kickDialog.setTitle("踢人");
        kickDialog.setMessage("确认将该玩家踢出吗？");
        kickDialog.setCancelable(false);
        kickDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                int Id = users.get(kickId).mUid;
                kickSomeBody(Id);
            }
        });
        kickDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
            }
        });
    }

    public void setAlertDialog(){
        changeDialog = new AlertDialog.Builder(CallActivity.this);
        changeDialog.setTitle("更换卡牌");
        changeDialog.setMessage("确认更换卡牌吗？");
        changeDialog.setCancelable(false);
        changeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String lastWord = null;
                    if (myWords.size()>0){
                        lastWord = myWords.get(0);
                    }else {
                        lastWord = "";
                    }
                    if (myWords.size()>0){
                        myWords.remove(0);
                    }else {
                        return;
                    }
                    myLeft--;
                    TextView numText = null;
                    TextView wordText = null;
                    if (cardNumLayouts.size()>0&&wordLayouts.size()>0){
                        numText = (TextView) cardNumLayouts.get(0).findViewById(R.id.card_num_text);
                        wordText = wordLayouts.get(0).findViewById(R.id.text_word);
                    }
                    if (myWords.size()>0){
                        if (cardNumLayouts.size()>0&&wordLayouts.size()>0){
                            if (numText!=null){
                                numText.setText(String.valueOf(myLeft));
                            }
                            if (wordText!=null){
                                wordText.setText(myWords.get(0));
                            }

                            if (voiceRecognition&&myWords.get(0).charAt(0) == '说'){
                                String word = myWords.get(0).substring(1);
                                micManager.setTargetWord(word);
                                micManager.start();
                            }


                            String messageText = combineMessage(myId,myWords.get(0),myLeft,SINGLE_CHANGE,0);
                            RtmMessage rtmMessage = mRtmClient.createMessage();
                            rtmMessage.setText(messageText);
                            sendChannelMessage(rtmMessage);
                            Toast.makeText(CallActivity.this,"已更换,你刚才更换的牌为 "+lastWord,Toast.LENGTH_LONG).show();
                        }
                    }else if (myWords.size() == 0){
                        numText.setText(String.valueOf(0));
                        wordText.setText("");
                        String messageText = combineMessage(myId,"",myLeft,SINGLE_CHANGE,0);
                        RtmMessage rtmMessage = mRtmClient.createMessage();
                        rtmMessage.setText(messageText);
                        sendChannelMessage(rtmMessage);
                        Toast.makeText(CallActivity.this,"已更换",Toast.LENGTH_SHORT).show();


                        MyRelativeLayout parentView = (MyRelativeLayout) wordText.getParent();
                        if (parentView!=null){
                            View layerView = parentView.findViewById(R.id.gray_layer);
                            if (layerView!=null){
                                layerView.setVisibility(View.VISIBLE);
                                layerView.setAlpha(0.8f);
                            }
                        }
                        String text = null;
                        if (myWords.size()>0){
                            text = combineMessage(myId,myWords.get(0),myLeft, I_LOSE,0);
                        }else {
                            text = combineMessage(myId,"",myLeft, I_LOSE,0);
                        }
                        RtmMessage rtmMessage1 = mRtmClient.createMessage();
                        rtmMessage1.setText(text);
                        sendChannelMessage(rtmMessage1);


                        micManager.onStop();

                    }

                }catch (Exception e){
                    Log.d(TAG, "=== positive button exception --> "+e);
                }


            }
        });
        changeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


    }

    //离开视频频道
    public void leaveVideoChannel(){
        getRtcEngine().disableVideo();
        getRtcEngine().disableAudio();
        getRtcEngine().leaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
        if(timer!=null){
            timer.cancel();
        }
    }

    protected void removeEventHandler(AGEventHandler handler) {
        application().remoteEventHandler(handler);
    }




    protected void preview(boolean start, SurfaceView view, int uid) {
        if (start) {
            application().getRtcEngine().setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            application().getRtcEngine().startPreview();
        } else {
            application().getRtcEngine().stopPreview();
        }
    }

    //发送频道信息
    private void sendChannelMessage(RtmMessage message) {
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "=== 发送通道信息成功！");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(TAG, "=== 发送通道信息失败！--> "+errorInfo);

            }
        });
    }

    @Override
    public void onUserJoined(int uid) {
        nowUserNum++;
        canStartGame = false;
        if(roomerId.equals(myId+"")){

            BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
            query.addWhereEqualTo("channelName", joinChannel);
            query.findObjects(new FindListener<RoomInfo>() {
                @Override
                public void done(List<RoomInfo> list, BmobException e) {
                    if(list != null && list.size() > 0){

                        RoomInfo info = list.get(0);

                        if(info.getPlayer() == 4){
                            kickSomeBody(uid);
                            return;
                        }
                        info.setPlayer(nowUserNum);
                        info.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e != null){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

        }

        iWin = false;
        messageList.clear();

        doRenderRemoteUi(uid);

    }

    public void setMute(){
        getRtcEngine().disableAudio();
    }

    @Override
    public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
        nowState = state;
        nowReason = reason;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == Constants.REMOTE_VIDEO_STATE_STOPPED && reason ==Constants.REMOTE_AUDIO_REASON_REMOTE_MUTED){
                    //当其他玩家关闭视频时，将我端的相应画面关闭
                    Iterator<Map.Entry<Integer,SurfaceView>> iterator = mUidsList.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<Integer,SurfaceView> entry = iterator.next();
                        if (entry.getKey() == uid){
                            //找到相应的SurfaceView并将它的背景色设置为黑色
                            entry.getValue().setBackgroundColor(Color.BLACK);
                            break;
                        }
                    }
                }else if (state == Constants.REMOTE_VIDEO_STATE_DECODING && reason == Constants.REMOTE_AUDIO_REASON_REMOTE_UNMUTED){
                    //当其他玩家恢复视频时，将我端的相应画面打开
                    Iterator<Map.Entry<Integer,SurfaceView>> iterator = mUidsList.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<Integer,SurfaceView> entry = iterator.next();
                        if (entry.getKey() == uid){
                            //找到相应的SurfaceView并将它的背景色设置为透明
                            entry.getValue().setBackgroundColor(Color.TRANSPARENT);
                            break;
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Log.d(TAG, "=== 加入频道成功 ######");

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        Log.d(TAG, "=== ~~~~~ onUserOffline");
        nowUserNum--;
        alreadyStarted = false;
        if(roomerId.equals(myId+"")){

            BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
            query.addWhereEqualTo("channelName", joinChannel);
            query.findObjects(new FindListener<RoomInfo>() {
                @Override
                public void done(List<RoomInfo> list, BmobException e) {
                    if(list != null && list.size() > 0){

                        RoomInfo info = list.get(0);
                        info.setPlayer(nowUserNum);
                        info.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e != null){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

        }

        if (messageList!=null){
            messageList.clear();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    doRemoveRemoteUi(uid);
                }catch (Exception e){
                    Log.d(TAG, "=== onUserOffline: exception--> "+e);
                }
                //gc的
                if (roomerId.equals(uid+"")){
                    Model.ROOM.deleteRoom(roomerId);
                    Toast.makeText(CallActivity.this, "房主已退出",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void doRemoveRemoteUi(final int uid) {
        try {
            if (gridVideoViewContainer!=null){
                gridVideoViewContainer.clearWords();
                gridVideoViewContainer.clearCountContainers();
                gridVideoViewContainer.clearCats();
                gridVideoViewContainer.clearForbids();
                gridVideoViewContainer.clearKicks();
                gridVideoViewContainer.clearReadys();
                cats.clear();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()){
                        return;
                    }
                    Object target = mUidsList.remove(uid);
                    if (target == null){
                        return;
                    }
                    gridVideoViewContainer.initGridVideoViewContainer(CallActivity.this,myId,mUidsList);
                    switchToDefaultVideoView();
                }
            });
        }catch (Exception e){
            Log.d(TAG, "=== remove exception--> "+e);
        }

    }

    private void doRenderRemoteUi(final int uid){

        if (gridVideoViewContainer!=null){
            gridVideoViewContainer.clearCountContainers();
            gridVideoViewContainer.clearWords();
            gridVideoViewContainer.clearCats();
            gridVideoViewContainer.clearForbids();
            gridVideoViewContainer.clearKicks();
            gridVideoViewContainer.clearReadys();
        }
        if (cats!=null){
            cats.clear();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()){
                    return;
                }
                if (mUidsList.containsKey(uid)){
                    return;
                }
                SurfaceView surfaceView = RtcEngine.CreateRendererView(AGApplication.getContext());
                mUidsList.put(uid,surfaceView);


                surfaceView.setZOrderOnTop(true);
                surfaceView.setZOrderMediaOverlay(true);
                getRtcEngine().setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN,uid));
                gridVideoViewContainer.initGridVideoViewContainer(CallActivity.this,myId,mUidsList);
                switchToDefaultVideoView();
            }
        });
    }


    //当其他用户加入或退出时调用的方法，用于重新设置相应的词汇，卡牌数
    public void switchToDefaultVideoView(){
        //add
        //发送第一张牌
        RtmMessage firstMessage = mRtmClient.createMessage();
        String text = null;
        if (myWords.size()>0){
            text = combineMessage(myId,myWords.get(0),myLeft,NOTIFY_ALL,0);
        }else if (myWords.size() == 0){
            text = combineMessage(myId,"",myLeft,NOTIFY_ALL,0);
        }

        firstMessage.setText(text);

        int sendDelayTime =1500;

        try {
            afterResetContainer = false;
            if (gridVideoViewContainer!=null){

                gridVideoViewContainer.clearCountContainers();
                gridVideoViewContainer.clearWords();
                gridVideoViewContainer.clearCats();
                gridVideoViewContainer.clearForbids();
                gridVideoViewContainer.clearKicks();
                gridVideoViewContainer.clearReadys();
                cats.clear();
            }
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    changeMarginCount = 0;
                    Iterator iterator = mUidsList.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry entry = (Map.Entry) iterator.next();
                        Integer key = (Integer) entry.getKey();

                    }

                    sendChannelMessage(firstMessage);

                    addCount=0;

                    cardNumLayouts = gridVideoViewContainer.getCountContainers();
                    wordLayouts = gridVideoViewContainer.getWordLayouts();
                    cats = gridVideoViewContainer.getCats();
                    forbids = gridVideoViewContainer.getForbids();
                    kicks = gridVideoViewContainer.getKicks();
                    readys = gridVideoViewContainer.getReadys();



                    for (int i=0;i<cardNumLayouts.size();i++){
                        TextView numText = cardNumLayouts.get(i).findViewById(R.id.card_num_text);
                        TextView wordText = wordLayouts.get(i).findViewById(R.id.text_word);
                        ImageView forbid = forbids.get(i);
                        ImageView kick = kicks.get(i);

                        if (i==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    numText.setText((myLeft)+"");
                                    if(myWords.size()>0){
                                        wordText.setText(myWords.get(0));
                                        MyRelativeLayout parentView = (MyRelativeLayout) wordText.getParent();
                                        if (parentView!=null){
                                            View layerView = parentView.findViewById(R.id.gray_layer);
                                            layerView.setAlpha(1);
                                            layerView.setVisibility(View.VISIBLE);
                                        }
                                    }else if (myWords.size() == 0){
                                        wordText.setText("");
                                    }

                                }
                            });
                        }
                        if (isRoomer){
                            if (i == 0){
                                continue;
                            }
                            int finalI = i;
                            forbid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                                    int forbidId = users.get(finalI).mUid;
                                    forbidSomeBodyVoice(forbidId);
                                }
                            });
                            forbid.setVisibility(View.VISIBLE);

                            kick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    kickDialog.show();
                                    kickId = finalI;
                                }
                            });
                            kick.setVisibility(View.VISIBLE);

                            startImage.setVisibility(View.VISIBLE);
                            alreadyStarted = false;


                        }else {
                            forbid.setVisibility(View.INVISIBLE);
                        }


                    }
                    afterResetContainer = true;
                }
            },sendDelayTime);
        }catch (Exception e){
            Log.d(TAG, "=== postDelay exception --> "+e);
        }

        try{
            if (timer==null){
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        cardNumLayouts = gridVideoViewContainer.getCountContainers();
                        wordLayouts = gridVideoViewContainer.getWordLayouts();
                        if (timer!=null){
                            Log.d(TAG, "=== run: timer实例-->  "+timer);
                        }

                        if (cardNumLayouts.size()>0&&wordLayouts.size()>0){
                            afterGrid = true;
                        }

                        //TODO 换牌
                        //等待RecyclerView的更新完成 和 收到所有其他用户的信息
                        if (afterResetContainer&&messageList.size()+1>=nowUserNum&&afterGrid){

                            if (isRoomer){
                                //房主通知其他用户在他的视频画面上显示房主图标
                                String text = combineMessage(myId,"我是房主",myLeft,I_AM_ROOM_OWNER,0);
                                RtmMessage message = mRtmClient.createMessage();
                                message.setText(text);
                                sendChannelMessage(message);
                                runOnUiThread(new Runnable() {
                                    //自己也显示房主图标
                                    @Override
                                    public void run() {
                                        if (wordLayouts.size()>0){
                                            RelativeLayout rootView = (RelativeLayout) wordLayouts.get(0).getParent().getParent().getParent();
                                            if (rootView!=null){
                                                ImageView ownerImage = rootView.findViewById(R.id.room_owner);
                                                ownerImage.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                });


                            }

                            if (haveReady){
                                resetMyReady();
                                String text = combineMessage(myId,"我之前准备过了",myLeft,KEEP_READY,0);
                                RtmMessage message = mRtmClient.createMessage();
                                message.setText(text);
                                sendChannelMessage(message);
                                alreadyStarted = false;
                            }
                            if (isRoomer&&(readyNum+1!=nowUserNum)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startImage.setVisibility(View.VISIBLE);
                                        alreadyStarted = false;
                                    }
                                });
                            }


                            if (nowUserNum==1){
                                timer.cancel();
                            }
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                            leftUsers.clear();
                            for (int i=0;i<users.size();i++){
                                UserStatusData user = users.get(i);
                                leftUsers.add(user.getmUid());
                            }
                            if (voiceRecognition&&myWords.size()>0){
                                if (myWords.get(0).charAt(0) == '说'){
                                    String word = myWords.get(0).substring(1);
                                    micManager.setTargetWord(word);
                                    micManager.start();
                                }

                            }

                            //根据收到的全部卡牌信息来设置相应的词汇，卡牌数
                            for (int i=0;i<messageList.size();i++){
                                MessageDetail tempDetail = messageList.get(i);
                                int tag = tempDetail.getTag();
                                for (int j=0;j<users.size();j++){
                                    if (cardNumLayouts.size()>0&&wordLayouts.size()>0){
                                        MyRelativeLayout cardNumLayout = cardNumLayouts.get(j);
                                        TextView numText = cardNumLayout.findViewById(R.id.card_num_text);
                                        MyRelativeLayout wordLayout = wordLayouts.get(j);
                                        TextView wordText = wordLayout.findViewById(R.id.text_word);
                                        if (j==0){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    numText.setText(String.valueOf(myLeft));
                                                    if (myWords.size()>0){
                                                        wordText.setText(myWords.get(0));
                                                    }else {
                                                        wordText.setText("");
                                                    }
                                                    wordText.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (iWin == true){
                                                                        Toast.makeText(CallActivity.this,"你已经赢了哦！",Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                    if (!alreadyStarted){
                                                                        Toast.makeText(CallActivity.this,"请等待其他玩家准备或房主开始游戏",Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                    if (myLeft>0){
                                                                        changeDialog.show();
                                                                    }else if (myLeft == 0){
                                                                        Toast.makeText(CallActivity.this,"你已经没有牌了哦！",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }

                                        UserStatusData userStatusData = users.get(j);
                                        int id = userStatusData.mUid;
                                        if (id == tag){
                                            if (numText!=null&&wordText!=null){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        numText.setText(String.valueOf(tempDetail.getLeft()));
                                                        wordText.setText(tempDetail.getWord());
                                                        MyRelativeLayout relativeLayout = (MyRelativeLayout) wordText.getParent();
                                                        if(relativeLayout!=null){
                                                            View layerView = relativeLayout.findViewById(R.id.gray_layer);
                                                            layerView.setAlpha(0);
                                                        }
                                                    }
                                                });
                                                break;
                                            }
                                        }

                                    }


                                }

                            }

                            afterGrid = false;
                            afterResetContainer = false;
                            timer.cancel();
                            timer = null;
                        }

                    }
                },0,300);
            }
        }catch (Exception e){
            Log.d(TAG, "=== timer exception --> "+e);
        }



    }

    private void resetMyReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (haveReady){
                    RelativeLayout rootView = (RelativeLayout) wordLayouts.get(0).getParent().getParent().getParent();
                    if (rootView!=null){
                        ImageView haveReady = rootView.findViewById(R.id.image_have_ready);
                        haveReady.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void kickSomeBody(int kickId) {
        if (isRoomer){
            String text = combineMessage(myId,"这是踢人",myLeft,KICK_SOMEONE,kickId);
            RtmMessage message = mRtmClient.createMessage();
            message.setText(text);
            sendChannelMessage(message);
        }
    }

    private void doConfigEngine() {
        VideoEncoderConfiguration.VideoDimensions videoDimension = ConstantApp.VIDEO_DIMENSIONS[getVideoEncResolutionIndex()];
        VideoEncoderConfiguration.FRAME_RATE videoFps = ConstantApp.VIDEO_FPS[getVideoEncFpsIndex()];
        configEngine(videoDimension, videoFps);
    }

    private int getVideoEncResolutionIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AGApplication.getContext());
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(AGApplication.getContext());
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

    protected void configEngine(VideoEncoderConfiguration.VideoDimensions videoDimension, VideoEncoderConfiguration.FRAME_RATE fps) {
        // Set the Resolution, FPS. Bitrate and Orientation of the video
        getRtcEngine().setVideoEncoderConfiguration(new VideoEncoderConfiguration(videoDimension,
                fps,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_microphone:
                isMute = !isMute;
                if (isMute){
                    Log.d(TAG, "=== 静音");
                    microphone.setImageResource(R.mipmap.forbit_microphone);
                    getRtcEngine().muteLocalAudioStream(true);
                }else {
                    Log.d(TAG, "=== 解除静音");
                    if (relieveForbid){
                        microphone.setImageResource(R.mipmap.microphone);
                        getRtcEngine().muteLocalAudioStream(false);
                    }else {
                        Toast.makeText(CallActivity.this,"你被房主禁言了哦！",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.image_video:
                try {
                    isBlind = !isBlind;
                    if (isBlind){
                        Log.d(TAG, "=== 关闭视频");
                        if (video!=null){
                            video.setImageResource(R.mipmap.forbit_camera);
                            Iterator<Map.Entry<Integer, SurfaceView>> iterator = mUidsList.entrySet().iterator();
                            while (iterator.hasNext()){
                                Map.Entry<Integer,SurfaceView> entry = iterator.next();
                                if (entry.getKey() == myId){
                                    entry.getValue().setBackgroundColor(Color.BLACK);
                                    break;
                                }
                            }
                        }
//                    getRtcEngine().enableLocalVideo(false);
                        getRtcEngine().muteLocalVideoStream(true);
                    }else {
                        Log.d(TAG, "=== 不关闭视频");
                        video.setImageResource(R.mipmap.camera);
                        Iterator<Map.Entry<Integer, SurfaceView>> iterator = mUidsList.entrySet().iterator();
                        while (iterator.hasNext()){
                            Map.Entry<Integer,SurfaceView> entry = iterator.next();
                            if (entry.getKey() == myId){
                                entry.getValue().setAlpha(1);
                                entry.getValue().setBackgroundColor(Color.TRANSPARENT);
                                break;
                            }
                        }
//                    getRtcEngine().enableLocalVideo(true);
                        getRtcEngine().muteLocalVideoStream(false);
                    }
                    break;
                }catch (Exception e){
                    Log.d(TAG, "=== 关闭视频异常--> "+e);
                }


            case R.id.iamge_back_to_home:
                finish();

            case R.id.image_ready:
                if (!isRoomer&&nowUserNum>1){
                    String text = combineMessage(myId,"我准备好了",myLeft,I_AM_READY,0);
                    RtmMessage message = mRtmClient.createMessage();
                    message.setText(text);
                    sendChannelMessage(message);
                    readyImage.setVisibility(View.INVISIBLE);

                    if (wordLayouts.size()>0){
                        RelativeLayout rootView = (RelativeLayout) wordLayouts.get(0).getParent().getParent().getParent();
                        if (rootView!=null){
                            ImageView haveReady = rootView.findViewById(R.id.image_have_ready);
                            haveReady.setVisibility(View.VISIBLE);
                        }
                    }

                    haveReady = true;

                }
                break;


            case R.id.image_start_game:
                if (canStartGame){
                    String startText = combineMessage(myId,"可以开始游戏啦！",myLeft,GAME_START,0);
                    RtmMessage startMessage = mRtmClient.createMessage();
                    startMessage.setText(startText);
                    sendChannelMessage(startMessage);
                    alreadyStarted = true;
                    Toast.makeText(CallActivity.this,"游戏开始啦！！！",Toast.LENGTH_SHORT).show();
                    startImage.setVisibility(View.INVISIBLE);

                    ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                    for (int i = 0;i<users.size();i++){
                        if (wordLayouts.size()>i){
                            RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                            if (rootView!=null){
                                ImageView haveReady = rootView.findViewById(R.id.image_have_ready);
                                haveReady.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }else {
                    Toast.makeText(CallActivity.this,"还有玩家没准备好哦！",Toast.LENGTH_SHORT).show();
                }
                break;



            default:
                break;


        }
    }

    class MyChannelListener implements RtmChannelListener {
        @Override
        public void onMemberCountUpdated(int i) {

        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {

        }

        @Override
        public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {

            try {
                Log.d(TAG, "=== 收到信息--> "+message.getText()+" 来自 "+fromMember.getUserId());
                MessageDetail messageDetail = parseMessage(message.getText());
                if (messageDetail.getMark() == NOTIFY_ALL){
                    //有玩家加入 / 退出后的处理的信息
                    if (messageList.size()==0){
                        messageList.add(messageDetail);
                        Log.d(TAG, "=== "+messageDetail.getWord()+" 加入messageList ");
                    }else {
                        int newTag = messageDetail.getTag();
                        for (int i=0;i<messageList.size();i++){
                            int tag = messageList.get(i).getTag();
                            if (tag==newTag){
                                return;
                            }
                        }
                        messageList.add(messageDetail);
                    }
                }
                else if (messageDetail.getMark() == SINGLE_CHANGE){
                    //某玩家换牌后要处理的信息
                    int changeTag = messageDetail.getTag();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (messageDetail.getLeft() == 0){
                                    int loserTag = messageDetail.getTag();
                                    for (int i=0;i<leftUsers.size();i++){
                                        if (leftUsers.get(i) == loserTag){
                                            leftUsers.remove(i);
                                            break;
                                        }
                                    }
                                    if (leftUsers.size() == 1&&leftUsers.get(0) == myId){
                                        if (cats.size()>0){
                                            cats.get(0).setVisibility(View.VISIBLE);
                                            iWin = true;
                                            Toast.makeText(CallActivity.this,"恭喜你赢了！！！",Toast.LENGTH_LONG).show();
                                            //通知别人显示我的冠军图标
                                            String text = null;
                                            if (myWords.size()>0){
                                                text = combineMessage(myId,myWords.get(0),myLeft,I_WIN,0);
                                            }else if(myWords.size()==0){
                                                text = combineMessage(myId,"",myLeft,I_WIN,0);
                                            }
                                            RtmMessage message1 = mRtmClient.createMessage();
                                            message1.setText(text);
                                            sendChannelMessage(message1);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                Log.d(TAG, "=== e--> "+e);
                            }


                            if (cardNumLayouts.size() == nowUserNum && wordLayouts.size() == nowUserNum){
                                ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                                for (int i=1;i<users.size();i++){
                                    int tag = users.get(i).mUid;
                                    if (tag == changeTag){
                                        TextView numText = cardNumLayouts.get(i).findViewById(R.id.card_num_text);
                                        TextView wordText = wordLayouts.get(i).findViewById(R.id.text_word);
                                        if (numText!=null&&wordText!=null) {
                                            runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    numText.setText(String.valueOf(messageDetail.getLeft()));
                                                    wordText.setText(messageDetail.getWord());
                                                }
                                            });
                                        }
                                    }
                                }
                            }


                        }
                    });

                }else if (messageDetail.getMark() == I_WIN){
                    //某玩家胜利后要处理的信息，在其相应的画面上加相应的胜利图标
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int winnerId = messageDetail.getTag();
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                            for (int i=0;i<users.size();i++){
                                UserStatusData user = users.get(i);
                                if (user.mUid != winnerId){
                                    cats.get(i).setVisibility(View.INVISIBLE);
                                }else {
                                    cats.get(i).setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                    });

                }else if (messageDetail.getMark() == FORBID){
                    //被禁言者要处理的信息
                    int forbidId = messageDetail.getTargetId();
                    if (forbidId!=myId){
                        return;
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                relieveForbid = !relieveForbid;
                                if (relieveForbid){
                                    Toast.makeText(CallActivity.this, "房主解除了你的禁言", Toast.LENGTH_LONG).show();
                                    getRtcEngine().muteLocalAudioStream(false);
                                    ImageView microphone = findViewById(R.id.image_microphone);
                                    microphone.setImageResource(R.mipmap.microphone);
                                    isMute = false;

                                    RelativeLayout rootView = (RelativeLayout) wordLayouts.get(0).getParent().getParent().getParent();
                                    if (rootView!=null){
                                        ImageView forbid = rootView.findViewById(R.id.image_forbid);
                                        forbid.setVisibility(View.INVISIBLE);
                                    }

                                }else {
                                    Toast.makeText(CallActivity.this, "你被禁言了", Toast.LENGTH_LONG).show();
                                    getRtcEngine().muteLocalAudioStream(true);
                                    ImageView microphone = findViewById(R.id.image_microphone);
                                    microphone.setImageResource(R.mipmap.forbit_microphone);
                                    isMute = true;

                                    RelativeLayout rootView = (RelativeLayout) wordLayouts.get(0).getParent().getParent().getParent();
                                    if (rootView!=null){
                                        ImageView forbid = rootView.findViewById(R.id.image_forbid);
                                        forbid.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        });
                    }
                }else if (messageDetail.getMark() == I_LOSE){
                    //某玩家输了以后，改变其相应的UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                            int loserTag = messageDetail.getTag();
                            if (users!=null&&users.size()>0){
                                for (int i=0;i<users.size();i++){
                                    UserStatusData user = users.get(i);
                                    if (user.mUid == loserTag){
                                        TextView wordText = wordLayouts.get(i).findViewById(R.id.text_word);
                                        MyRelativeLayout parentView = (MyRelativeLayout) wordText.getParent();
                                        if (parentView!=null){
                                            View layerView = parentView.findViewById(R.id.gray_layer);
                                            if (layerView!=null){
                                                layerView.setVisibility(View.VISIBLE);
                                                layerView.setAlpha(0.8f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });

                }else if (messageDetail.getMark() == KICK_SOMEONE){
                    //被踢出玩家要处理的信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (myId == messageDetail.getTargetId()){
                                Toast.makeText(CallActivity.this,"你被房主踢出了房间",Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(CallActivity.this,"房主将用户"+messageDetail.getTag()+"踢出了房间",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else if (messageDetail.getMark() == I_AM_READY){
                    //接收已经准备好后的玩家发出的信息，改变相应UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRoomer){
                                readyNum++;
                                if (readyNum+1 == nowUserNum){
                                    canStartGame = true;
                                    alreadyStarted = false;
                                }
                            }
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();

                            for (int i=0;i<users.size();i++){
                                UserStatusData user = users.get(i);
                                if (user.mUid == messageDetail.getTag()){
                                    if (wordLayouts.size()>i){
                                        RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                                        ImageView readyImage = rootView.findViewById(R.id.image_have_ready);
                                        readyImage.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    });

                }else if (messageDetail.getMark() == GAME_START){
                    //处理 房主点击开始后发出的信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CallActivity.this,"游戏开始啦！！！",Toast.LENGTH_SHORT).show();
                            alreadyStarted = true;

                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                            for (int i=0;i<users.size();i++) {
                                if (wordLayouts.size() > i) {
                                    RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                                    ImageView haveReady = rootView.findViewById(R.id.image_have_ready);
                                    haveReady.setVisibility(View.INVISIBLE);
                                }
                            }

                        }
                    });
                }else if (messageDetail.getMark() == LEAVE){
                    //处理某玩家退出前发出的信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (messageDetail.getTargetId() == 1){
                                        readyNum--;
                                        if (isRoomer&&(readyNum+1 == nowUserNum)){
                                            startImage.setVisibility(View.VISIBLE);
                                        }
                                        for (int i=0;i<wordLayouts.size();i++){
                                            if (wordLayouts.size()>i){
                                                RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                                                if (rootView!=null){
                                                    ImageView haveReadyImage = rootView.findViewById(R.id.image_have_ready);
                                                    if (haveReadyImage!=null){
                                                        haveReadyImage.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            }
                                        }
                                    }else if (messageDetail.getTargetId() == 2){
                                        if (isRoomer){
                                            startImage.setVisibility(View.VISIBLE);
                                            alreadyStarted = false;
                                        }
                                    }
                                }
                            },1000);

                        }
                    });

                }else if (messageDetail.getMark() == KEEP_READY){
                    //处理 若已经准备好，但之后又其他玩家加入的信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRoomer){
                                if (readyNum+1 == nowUserNum){
                                    canStartGame = true;
                                }
                            }
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();

                            for (int i=0;i<users.size();i++){
                                UserStatusData user = users.get(i);
                                if (user.mUid == messageDetail.getTag()){
                                    if (wordLayouts.size()>i){
                                        RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                                        ImageView readyImage = rootView.findViewById(R.id.image_have_ready);
                                        readyImage.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    });
                }else if (messageDetail.getMark() == I_AM_ROOM_OWNER){
                    //处理 房主发送的用来告知其他玩家房主ID的信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<UserStatusData> users = gridVideoViewContainer.getUsers();
                            for (int i=0;i<users.size();i++){
                                UserStatusData user = users.get(i);
                                if (user.mUid == messageDetail.getTag()){
                                    if (wordLayouts.size()>i){
                                        RelativeLayout rootView = (RelativeLayout) wordLayouts.get(i).getParent().getParent().getParent();
                                        ImageView roomOwnerImage = rootView.findViewById(R.id.room_owner);
                                        roomOwnerImage.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }
                            }
                        }
                    });

                }


            }catch (Exception e){
                Log.d(TAG, "=== receive exception--> "+e);
            }

        }

        @Override
        public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onMemberJoined(RtmChannelMember member) {

        }

        @Override
        public void onMemberLeft(RtmChannelMember member) {

        }
    }




}