package io.agora.education.classroom;

import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.education.R;
import io.agora.education.api.EduCallback;
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
import io.agora.education.api.user.data.EduUserStateChangeType;
import io.agora.education.classroom.bean.channel.Room;
import io.agora.education.classroom.widget.RtcVideoView;


public class OneToOneClassActivity extends BaseClassActivity {
    private static final String TAG = OneToOneClassActivity.class.getSimpleName();

    @BindView(R.id.layout_video_teacher)
    protected RtcVideoView video_teacher;
    @BindView(R.id.layout_video_student)
    protected RtcVideoView video_student;
    @BindView(R.id.layout_im)
    protected View layout_im;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_one2one_class;
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
    }

    @Override
    protected void initView() {
        super.initView();
        video_teacher.init(R.layout.layout_video_one2one_class, true);
        video_student.init(R.layout.layout_video_one2one_class, true);
        video_student.setOnClickAudioListener(v -> OneToOneClassActivity.this.muteLocalAudio(!video_student.isAudioMuted()));
        video_student.setOnClickVideoListener(v -> OneToOneClassActivity.this.muteLocalVideo(!video_student.isVideoMuted()));
    }

    @Override
    protected int getClassType() {
        return Room.Type.ONE2ONE;
    }

    @OnClick(R.id.iv_float)
    public void onClick(View view) {
        boolean isSelected = view.isSelected();
        view.setSelected(!isSelected);
        layout_im.setVisibility(isSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRemoteUsersInitialized(@NotNull List<? extends EduUserInfo> users, @NotNull EduRoom classRoom) {
        super.onRemoteUsersInitialized(users, classRoom);
        video_student.setName(getLocalUserInfo().getUserName());
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getMediaRoomName()));
//        runOnUiThread(() -> {
//            /**一对一，默认学生可以针对白板进行输入*/
//            whiteboardFragment.disableCameraTransform(false);
//            whiteboardFragment.disableDeviceInputs(false);
//        });
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

    /**
     * 群聊自定义消息回调
     */
    @Override
    public void onRoomMessageReceived(@NotNull EduMsg message, @NotNull EduRoom classRoom) {
        super.onRoomMessageReceived(message, classRoom);
    }

    /**
     * 私聊自定义消息回调
     */
    @Override
    public void onUserMessageReceived(@NotNull EduMsg message) {
        super.onUserMessageReceived(message);
    }

    /**
     * 群聊消息回调
     */
    @Override
    public void onRoomChatMessageReceived(@NotNull EduChatMsg eduChatMsg, @NotNull EduRoom classRoom) {
        super.onRoomChatMessageReceived(eduChatMsg, classRoom);
    }

    /**
     * 私聊消息回调
     */
    @Override
    public void onUserChatMessageReceived(@NotNull EduChatMsg chatMsg) {
        super.onUserChatMessageReceived(chatMsg);
    }

    @Override
    public void onRemoteStreamsInitialized(@NotNull List<? extends EduStreamInfo> streams,
                                           @NotNull EduRoom classRoom) {
        super.onRemoteStreamsInitialized(streams, classRoom);
        Log.e(TAG, "onRemoteStreamsInitialized");
//        EduStreamInfo streamInfo = getTeacherStream();
//        Log.e(TAG, "老师的流信息:" + new Gson().toJson(streamInfo));
//        if (streamInfo != null) {
//            video_teacher.setName(streamInfo.getPublisher().getUserName());
//            renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
//            video_teacher.muteVideo(!streamInfo.getHasVideo());
//            video_teacher.muteAudio(!streamInfo.getHasAudio());
//        }
        for (EduStreamInfo streamInfo : streams) {
            /**一对一场景下，远端流就是老师的流*/
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
                case SCREEN:
                    /**有屏幕分享的流进入，说明是老师打开了屏幕分享，此时把这个流渲染出来*/
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
    }

    @Override
    public void onRemoteStreamsAdded(@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsAdded(streamEvents, classRoom);
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            /**一对一场景下，远端流就是老师的流*/
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
//                case SCREEN:
//                    /**有屏幕分享的流进入，说明是老师打开了屏幕分享，此时把这个流渲染出来*/
//                    runOnUiThread(() -> {
//                        layout_whiteboard.setVisibility(View.GONE);
//                        layout_share_video.setVisibility(View.VISIBLE);
//                        layout_share_video.removeAllViews();
//                        renderStream(getMainEduRoom(), streamInfo, layout_share_video);
//                    });
//                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRemoteStreamUpdated(@NotNull EduStreamEvent streamEvent, @NotNull EduStreamStateChangeType type,
                                      @NotNull EduRoom classRoom) {
        super.onRemoteStreamUpdated(streamEvent, type, classRoom);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        switch (streamInfo.getVideoSourceType()) {
            case CAMERA:
                /**一对一场景下，远端流就是老师的流*/
                video_teacher.setName(streamInfo.getPublisher().getUserName());
                renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                video_teacher.muteVideo(!streamInfo.getHasVideo());
                video_teacher.muteAudio(!streamInfo.getHasAudio());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRemoteStreamsRemoved(@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsRemoved(streamEvents, classRoom);
        /**一对一场景下，远端流就是老师的流*/
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, null);
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
//                case SCREEN:
//                    /**老师关闭了屏幕分享，移除屏幕分享的布局*/
//                    runOnUiThread(() -> {
//                        layout_whiteboard.setVisibility(View.VISIBLE);
//                        layout_share_video.setVisibility(View.GONE);
//                        layout_share_video.removeAllViews();
//                        renderStream(getMainEduRoom(), streamInfo, null);
//                    });
//                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRoomStatusChanged(@NotNull EduRoomChangeType event, @NotNull EduUserInfo operatorUser,
                                    @NotNull EduRoom classRoom) {
        super.onRoomStatusChanged(event, operatorUser, classRoom);
    }

    @Override
    public void onRoomPropertyChanged(@NotNull EduRoom classRoom, @Nullable Map<String, Object> cause) {
        super.onRoomPropertyChanged(classRoom, cause);
//        runOnUiThread(() -> {
//            /**小班课，默认学生可以针对白板进行输入*/
//            whiteboardFragment.disableCameraTransform(false);
//            whiteboardFragment.disableDeviceInputs(false);
//        });
    }

    @Override
    public void onRemoteUserPropertyUpdated(@NotNull EduUserInfo userInfo, @NotNull EduRoom classRoom,
                                            @Nullable Map<String, Object> cause) {
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
    }

    @Override
    public void onLocalUserPropertyUpdated(@NotNull EduUserInfo userInfo, @Nullable Map<String, Object> cause) {
        super.onLocalUserPropertyUpdated(userInfo, cause);
    }

    @Override
    public void onLocalStreamAdded(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamAdded(streamEvent);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        renderStream(getMainEduRoom(), streamInfo, video_student.getVideoLayout());
        video_student.muteVideo(!streamInfo.getHasVideo());
        video_student.muteAudio(!streamInfo.getHasAudio());
        Log.e(TAG, "本地流被添加：" + getLocalCameraStream().getHasAudio() + "," + streamInfo.getHasVideo());
    }

    @Override
    public void onLocalStreamUpdated(@NotNull EduStreamEvent streamEvent, @NotNull EduStreamStateChangeType type) {
        super.onLocalStreamUpdated(streamEvent, type);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        video_student.muteVideo(!streamInfo.getHasVideo());
        video_student.muteAudio(!streamInfo.getHasAudio());
        Log.e(TAG, "本地流被修改：" + streamInfo.getHasAudio() + "," + streamInfo.getHasVideo());
    }

    @Override
    public void onLocalStreamRemoved(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamRemoved(streamEvent);
        /**一对一场景下，此回调被调用就说明classroom结束，人员退出；所以此回调可以不处理*/
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        renderStream(getMainEduRoom(), streamInfo, null);
        video_student.muteVideo(true);
        video_student.muteAudio(true);
        Log.e(TAG, "本地流被移除");
    }
}
