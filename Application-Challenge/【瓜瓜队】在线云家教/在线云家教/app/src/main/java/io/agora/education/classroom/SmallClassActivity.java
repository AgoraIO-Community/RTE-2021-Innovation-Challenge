package io.agora.education.classroom;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.herewhite.sdk.domain.GlobalState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.education.R;
import io.agora.education.api.EduCallback;
import io.agora.education.api.message.EduActionMessage;
import io.agora.education.api.message.EduChatMsg;
import io.agora.education.api.message.EduMsg;
import io.agora.education.api.room.EduRoom;
import io.agora.education.api.room.data.EduRoomChangeType;
import io.agora.education.api.statistics.ConnectionState;
import io.agora.education.api.statistics.NetworkQuality;
import io.agora.education.api.stream.data.EduStreamEvent;
import io.agora.education.api.stream.data.EduStreamInfo;
import io.agora.education.api.stream.data.EduStreamStateChangeType;
import io.agora.education.api.user.EduStudent;
import io.agora.education.api.user.data.EduUserEvent;
import io.agora.education.api.user.data.EduUserInfo;
import io.agora.education.api.user.data.EduUserRole;
import io.agora.education.api.user.data.EduUserStateChangeType;
import io.agora.education.classroom.adapter.ClassVideoAdapter;
import io.agora.education.classroom.bean.board.BoardState;
import io.agora.education.classroom.bean.channel.Room;
import io.agora.education.classroom.fragment.UserListFragment;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcChannel;
import io.agora.rte.RteEngineImpl;
import io.agora.rte.listener.RteAudioMixingListener;
import io.agora.rte.listener.RteMediaDeviceListener;
import io.agora.rte.listener.RteSpeakerReportListener;
import io.agora.rte.listener.RteStatisticsReportListener;

public class SmallClassActivity extends BaseClassActivity implements TabLayout.OnTabSelectedListener,
        RteAudioMixingListener, RteMediaDeviceListener, RteSpeakerReportListener, RteStatisticsReportListener {
    private static final String TAG = "SmallClassActivity";

    @BindView(R.id.layout_placeholder)
    protected ConstraintLayout layout_placeholder;
    @BindView(R.id.rcv_videos)
    protected RecyclerView rcv_videos;
    @BindView(R.id.layout_im)
    protected View layout_im;
    @BindView(R.id.layout_tab)
    protected TabLayout layout_tab;

    private ClassVideoAdapter classVideoAdapter;
    private UserListFragment userListFragment;
    private View teacherPlaceholderView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_small_class;
    }

    @Override
    protected void initData() {
        super.initData();
        joinRoom(getMainEduRoom(), roomEntry.getUserName(), roomEntry.getUserUuid(), true, true, true,
                new EduCallback<EduStudent>() {
                    @Override
                    public void onSuccess(@org.jetbrains.annotations.Nullable EduStudent res) {
                        runOnUiThread(() -> showFragmentWithJoinSuccess());
                    }

                    @Override
                    public void onFailure(int code, @org.jetbrains.annotations.Nullable String reason) {
                        joinFailed(code, reason);
                    }
                });
        classVideoAdapter = new ClassVideoAdapter();
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcv_videos.setLayoutManager(layoutManager);
        rcv_videos.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) > 0) {
                    outRect.left = getResources().getDimensionPixelSize(R.dimen.dp_2_5);
                }
            }
        });
        rcv_videos.setAdapter(classVideoAdapter);
        layout_tab.addOnTabSelectedListener(this);
        userListFragment = new UserListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_chat_room, userListFragment)
                .show(userListFragment)
                .commitNow();
        /**测试监听回调*/
        RteEngineImpl.INSTANCE.setMediaDeviceListener(this);
        RteEngineImpl.INSTANCE.setAudioMixingListener(this);
        RteEngineImpl.INSTANCE.setSpeakerReportListener(this);
        findViewById(R.id.send1).setOnClickListener((v) -> {

        });
        findViewById(R.id.send2).setOnClickListener((v) -> {
            RteEngineImpl.INSTANCE.startAudioMixing("/sdcard/1/111.mp3", false, false, 1);
        });
        findViewById(R.id.send3).setOnClickListener((v) -> {

        });
        findViewById(R.id.send4).setOnClickListener((v) -> {

        });
    }

    @Override
    protected int getClassType() {
        return Room.Type.SMALL;
    }

    @OnClick(R.id.iv_float)
    public void onClick(View view) {
        boolean isSelected = view.isSelected();
        view.setSelected(!isSelected);
        layout_im.setVisibility(isSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tab.getPosition() == 0) {
            transaction.show(chatRoomFragment).hide(userListFragment);
        } else {
            transaction.show(userListFragment).hide(chatRoomFragment);
        }
        transaction.commitNow();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onAudioMixingFinished() {
        Log.e(TAG, "onAudioMixingFinished");
    }

    @Override
    public void onAudioMixingStateChanged(int state, int errorCode) {
        Log.e(TAG, "onAudioMixingStateChanged->state:" + state + ",errorCode:" + errorCode);
    }

    @Override
    public void onAudioRouteChanged(int routing) {
        Log.e(TAG, "onAudioRouteChanged->routing:" + routing);
    }

    @Override
    public void onAudioVolumeIndicationOfLocalSpeaker(@Nullable IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
        Log.e(TAG, "onAudioVolumeIndicationOfLocalSpeaker->totalVolume:" + totalVolume);
    }

    @Override
    public void onAudioVolumeIndicationOfRemoteSpeaker(@Nullable IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
        Log.e(TAG, "onAudioVolumeIndicationOfRemoteSpeaker->totalVolume:" + totalVolume);
    }

    @Override
    public void onRtcStats(@Nullable RtcChannel channel, @Nullable IRtcEngineEventHandler.RtcStats stats) {
//        Log.e(TAG, "onRtcStats->stats:" + stats.rxKBitRate);
    }

    @Override
    public void onVideoSizeChanged(@Nullable RtcChannel channel, int uid, int width, int height, int rotation) {
        Log.e(TAG, "onVideoSizeChanged->uid:" + uid + ",width:" + width + ",height:" + height + ",rotation:" + rotation);
    }

    @Override
    public void onRemoteUsersInitialized(@NotNull List<? extends EduUserInfo> users, @NotNull EduRoom classRoom) {
        /*测试回调*/
        RteEngineImpl.INSTANCE.setStatisticsReportListener(classRoom.getRoomInfo().getRoomUuid(), this);
        super.onRemoteUsersInitialized(users, classRoom);
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getMediaRoomName()));
    }

    @Override
    public void onRemoteUsersJoined(@NotNull List<? extends EduUserInfo> users, @NotNull EduRoom classRoom) {
        super.onRemoteUsersJoined(users, classRoom);
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getMediaRoomName()));
    }

    @Override
    public void onRemoteUserLeft(@NotNull EduUserEvent userEvent, @NotNull EduRoom classRoom) {
        super.onRemoteUserLeft(userEvent, classRoom);
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getMediaRoomName()));
    }

    @Override
    public void onRemoteUserUpdated(@NotNull EduUserEvent userEvent, @NotNull EduUserStateChangeType type,
                                    @NotNull EduRoom classRoom) {
        super.onRemoteUserUpdated(userEvent, type, classRoom);
    }

    @Override
    public void onRoomMessageReceived(@NotNull EduMsg message, @NotNull EduRoom classRoom) {
        super.onRoomMessageReceived(message, classRoom);
    }

    @Override
    public void onUserMessageReceived(@NotNull EduMsg message) {
        super.onUserMessageReceived(message);
    }

    @Override
    public void onRoomChatMessageReceived(@NotNull EduChatMsg eduChatMsg, @NotNull EduRoom classRoom) {
        super.onRoomChatMessageReceived(eduChatMsg, classRoom);
    }

    @Override
    public void onUserChatMessageReceived(@NotNull EduChatMsg chatMsg) {
        super.onUserChatMessageReceived(chatMsg);
    }

    @Override
    public void onRemoteStreamsInitialized(@NotNull List<? extends EduStreamInfo> streams, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsInitialized(streams, classRoom);
        for (EduStreamInfo streamInfo : streams) {
            switch (streamInfo.getVideoSourceType()) {
                case SCREEN:
                    runOnUiThread(() -> {
                        layout_whiteboard.setVisibility(View.GONE);
                        layout_share_video.setVisibility(View.VISIBLE);
                        layout_share_video.removeAllViews();
                        renderStream(getMainEduRoom(), streamInfo, layout_share_video);
                    });
                    break;
                default:
                    break;
            }
        }
        userListFragment.setLocalUserUuid(classRoom.getLocalUser().getUserInfo().getUserUuid());
        userListFragment.setUserList(getCurFullStream());
        showVideoList(getCurFullStream());
    }

    @Override
    public void onRemoteStreamsAdded(@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsAdded(streamEvents, classRoom);
        boolean notify = false;
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    notify = true;
                    break;
                default:
                    break;
            }
        }
        /**有远端Camera流添加，刷新视频列表*/
        if (notify) {
            Log.e(TAG, "有远端Camera流添加，刷新视频列表");
            showVideoList(getCurFullStream());
        }
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onRemoteStreamUpdated(@NotNull EduStreamEvent streamEvent,
                                      @NotNull EduStreamStateChangeType type, @NotNull EduRoom classRoom) {
        super.onRemoteStreamUpdated(streamEvent, type, classRoom);
        boolean notify = false;
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        switch (streamInfo.getVideoSourceType()) {
            case CAMERA:
                notify = true;
                break;
            default:
                break;
        }
        /**有远端Camera流添加，刷新视频列表*/
        if (notify) {
            Log.e(TAG, "有远端Camera流被修改，刷新视频列表");
            showVideoList(getCurFullStream());
        }
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onRemoteStreamsRemoved(@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsRemoved(streamEvents, classRoom);
        boolean notify = false;
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    notify = true;
                    break;
                default:
                    break;
            }
        }
        /**有远端Camera流被移除，刷新视频列表*/
        if (notify) {
            Log.e(TAG, "有远端Camera流被移除，刷新视频列表");
            showVideoList(getCurFullStream());
        }
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onRoomStatusChanged(@NotNull EduRoomChangeType event, @NotNull EduUserInfo operatorUser, @NotNull EduRoom classRoom) {
        super.onRoomStatusChanged(event, operatorUser, classRoom);
//        EduRoomStatus roomStatus = classRoom.getRoomStatus();
//        switch (event) {
//            case COURSE_STATE:
//                title_view.setTimeState(roomStatus.getCourseState() == EduRoomState.START,
//                        System.currentTimeMillis() - roomStatus.getStartTime());
//                break;
//            case STUDENT_CHAT:
//                chatRoomFragment.setMuteAll(!roomStatus.isStudentChatAllowed());
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onRoomPropertyChanged(@NotNull EduRoom classRoom, @Nullable Map<String, Object> cause) {
        super.onRoomPropertyChanged(classRoom, cause);
    }

    @Override
    public void onRemoteUserPropertyUpdated(@NotNull EduUserInfo userInfo, @NotNull EduRoom classRoom,
                                            @Nullable Map<String, Object> cause) {
        super.onRemoteUserPropertyUpdated(userInfo, classRoom, cause);
    }

    @Override
    public void onNetworkQualityChanged(@NotNull NetworkQuality quality, @NotNull EduUserInfo user,
                                        @NotNull EduRoom classRoom) {
        super.onNetworkQualityChanged(quality, user, classRoom);
        title_view.setNetworkQuality(quality);
    }

    @Override
    public void onConnectionStateChanged(@NotNull ConnectionState state, @NotNull EduRoom classRoom) {
        super.onConnectionStateChanged(state, classRoom);
    }

    @Override
    public void onLocalUserUpdated(@NotNull EduUserEvent userEvent, @NotNull EduUserStateChangeType type) {
        super.onLocalUserUpdated(userEvent, type);
        /**更新用户信息*/
        showVideoList(getCurFullStream());
        userListFragment.updateLocalStream(getLocalCameraStream());
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onLocalUserPropertyUpdated(@NotNull EduUserInfo userInfo, @Nullable Map<String, Object> cause) {
        super.onLocalUserPropertyUpdated(userInfo, cause);
    }

    @Override
    public void onLocalStreamAdded(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamAdded(streamEvent);
        showVideoList(getCurFullStream());
        userListFragment.updateLocalStream(getLocalCameraStream());
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onLocalStreamUpdated(@NotNull EduStreamEvent streamEvent, @NotNull EduStreamStateChangeType type) {
        super.onLocalStreamUpdated(streamEvent, type);
        showVideoList(getCurFullStream());
        userListFragment.updateLocalStream(getLocalCameraStream());
        userListFragment.setUserList(getCurFullStream());
    }

    @Override
    public void onLocalStreamRemoved(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamRemoved(streamEvent);
        /**小班课场景下，此回调被调用就说明classroom结束，人员退出；所以此回调可以不处理*/
    }

    @Override
    public void onUserActionMessageReceived(@NotNull EduActionMessage actionMessage) {
        super.onUserActionMessageReceived(actionMessage);
        Log.e(TAG, "action->" + new Gson().toJson(actionMessage));
    }

    @Override
    public void onGlobalStateChanged(GlobalState state) {
        super.onGlobalStateChanged(state);
        BoardState boardState = (BoardState) state;
        List<String> grantedUuids = boardState.getGrantUsers();
        userListFragment.setGrantedUuids(grantedUuids);
    }

    private void showVideoList(List<EduStreamInfo> list) {
        runOnUiThread(() -> {
            for (int i = 0; i < list.size(); i++) {
                EduStreamInfo streamInfo = list.get(i);
                if (streamInfo.getPublisher().getRole().equals(EduUserRole.TEACHER)) {
                    /*隐藏老师的占位布局*/
                    layout_placeholder.setVisibility(View.GONE);
                    if (i != 0) {
                        Collections.swap(list, 0, i);
                    }
                    classVideoAdapter.setNewList(list);
                    return;
                }
            }
            /*显示老师的占位布局*/
            if (teacherPlaceholderView == null) {
                teacherPlaceholderView = LayoutInflater.from(this).inflate(R.layout.layout_video_small_class,
                        layout_placeholder);
            }
            layout_placeholder.setVisibility(View.VISIBLE);
            classVideoAdapter.setNewList(list);
        });
    }
}
