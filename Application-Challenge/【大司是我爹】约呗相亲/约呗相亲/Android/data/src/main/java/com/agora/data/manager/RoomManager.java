package com.agora.data.manager;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.preference.PreferenceManager;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.RoomEventCallback;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.observer.DataCompletableObserver;
import com.agora.data.provider.IRoomProxy;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.models.ClientRoleOptions;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 负责房间内数据的管理以及事件通知
 */
public final class RoomManager implements IRoomProxy {

    public static final String TAG_AUDIENCELATENCYLEVEL = "audienceLatencyLevel";

    private Logger.Builder mLogger = XLog.tag("RoomManager");

    public static final int ERROR_REGISTER_LEANCLOUD = 1000;
    public static final int ERROR_REGISTER_LEANCLOUD_EXCEEDED_QUOTA = ERROR_REGISTER_LEANCLOUD + 1;

    private volatile static RoomManager instance;

    private Context mContext;

    private RoomManager(Context context) {
        mContext = context.getApplicationContext();
    }

    private final IRtcEngineEventHandler mIRtcEngineEventHandler = new IRtcEngineEventHandler() {
    };

    public synchronized static RoomManager Instance(Context context) {
        if (instance == null) {
            synchronized (RoomManager.class) {
                if (instance == null)
                    instance = new RoomManager(context);
            }
        }
        return instance;
    }

    /**
     * 正在退出房间，防止回调处理。
     */
    public volatile static boolean isLeaving = false;

    private Room mRoom;
    private Member mMine;

    /**
     * Member表中objectId和Member的键值对
     */
    private final Map<String, Member> membersMap = new ConcurrentHashMap<>();

    /**
     * RTC的UID和Member的键值对
     */
    private final Map<Long, Member> streamIdMap = new ConcurrentHashMap<>();

    private final MainThreadDispatch mainThreadDispatch = new MainThreadDispatch();

    public Completable requestHandsUp() {
        mLogger.d("requestHandsUp() called");
        return DataRepositroy.Instance(mContext)
                .requestHandsUp(mMine);
    }

    public Completable toggleSelfAudio() {
        mLogger.d("toggleSelfAudio() called");
        Member member = getMine();
        if (member == null) {
            return Completable.complete();
        }

        int newValue = mMine.getIsSelfMuted() == 0 ? 1 : 0;
        return DataRepositroy.Instance(mContext)
                .muteSelfVoice(mMine, newValue)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        mMine.setIsSelfMuted(newValue);

                        RtcManager.Instance(mContext).muteLocalAudioStream(newValue == 1);
                        onAudioStatusChanged(true, mMine);
                    }
                });
    }

    public Completable toggleTargetAudio(@NonNull Member member) {
        mLogger.d("toggleTargetAudio() called with: member = [" + member + "]");
        if (ObjectsCompat.equals(member, getMine())) {
            return toggleSelfAudio();
        }

        int newValue = member.getIsMuted() == 0 ? 1 : 0;
        return DataRepositroy.Instance(mContext)
                .muteVoice(member, newValue)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        member.setIsMuted(newValue);

                        RtcManager.Instance(mContext).muteRemoteVideoStream(member.getStreamId().intValue(), newValue != 0);
                        onAudioStatusChanged(false, member);
                    }
                });
    }

    public Completable seatOff(@NonNull Member member) {
        mLogger.d("seatOff() called with: member = [" + member + "]");
        return DataRepositroy.Instance(mContext)
                .seatOff(member)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        member.setIsSpeaker(0);

                        if (isMine(member)) {
                            onRoleChanged(true, member);
                        } else {
                            onRoleChanged(false, member);
                        }
                    }
                });
    }

    public void addRoomEventCallback(@NonNull RoomEventCallback callback) {
        this.mainThreadDispatch.addRoomEventCallback(callback);
    }

    public void removeRoomEventCallback(@NonNull RoomEventCallback callback) {
        this.mainThreadDispatch.removeRoomEventCallback(callback);
    }

    public Observable<Member> joinRoom() {
        mLogger.d("joinRoom() called");
        return DataRepositroy.Instance(mContext)
                .joinRoom(mRoom, mMine)
                .doOnNext(new Consumer<Member>() {
                    @Override
                    public void accept(Member member) throws Exception {
                        mLogger.d("joinRoom() called member= [%s]", member);
                        mMine = member;
                    }
                });
    }

    public void onJoinRoom(Room room, Member member) {
        mLogger.d("onJoinRoom() called room= [%s] member= [%s]", room, member);
        this.mRoom = room;
        this.mMine = member;
        isLeaving = false;

        RtcManager.Instance(mContext).addHandler(mIRtcEngineEventHandler);
    }

    public void leaveRoom() {
        mLogger.d("onJoinRoom() called");
        isLeaving = true;

        RoomManager.Instance(mContext).stopLivePlay();
        RtcManager.Instance(mContext).leaveChannel();
        DataRepositroy.Instance(mContext)
                .leaveRoom(mRoom, mMine)
                .subscribe(new DataCompletableObserver(mContext) {
                    @Override
                    public void handleError(@NonNull BaseError e) {
                    }

                    @Override
                    public void handleSuccess() {
                    }
                });

        onLeaveRoom();
    }

    private void onLeaveRoom() {
        mLogger.d("onLeaveRoom() called");
        RtcManager.Instance(mContext).removeHandler(mIRtcEngineEventHandler);

        mainThreadDispatch.onLeaveRoom(getRoom());

        this.mRoom = null;
        this.mMine = null;
        this.membersMap.clear();
        this.streamIdMap.clear();
    }

    public void onRoomUpdated(Room room) {
        mLogger.d("onRoomUpdated() called with: room = [" + room + "]");
        this.mRoom = room;
    }

    public void onLoadRoomMembers(@NonNull List<Member> members) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (isMine(member)) {
                member = mMine;
                members.set(i, mMine);
            }

            member.setRoomId(mRoom);
            membersMap.put(member.getObjectId(), member);
            if (member.getStreamId() != null) {
                streamIdMap.put(member.getStreamId(), member);
            }
        }
    }

    public Maybe<Room> getRoom(Room room) {
        mLogger.d("getRoom() called with: room = [" + room + "]");
        return DataRepositroy.Instance(mContext)
                .getRoom(room)
                .doOnSuccess(new Consumer<Room>() {
                    @Override
                    public void accept(Room room) throws Exception {
                        onRoomUpdated(room);
                    }
                });
    }

    public List<Member> getMembers() {
        return new ArrayList<>(membersMap.values());
    }

    @Nullable
    @Override
    public Member getMine() {
        return mMine;
    }

    @Nullable
    @Override
    public Room getRoom() {
        return mRoom;
    }

    public void startLivePlay() {
        RtcManager.Instance(mContext).startAudio();
        RtcManager.Instance(mContext).setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_BROADCASTER);
    }

    public void stopLivePlay() {
        RtcManager.Instance(mContext).stopAudio();

        int audienceLatencyLevel = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(TAG_AUDIENCELATENCYLEVEL, Constants.AUDIENCE_LATENCY_LEVEL_ULTRA_LOW_LATENCY);
        ClientRoleOptions mClientRoleOptions = new ClientRoleOptions();
        mClientRoleOptions.audienceLatencyLevel = audienceLatencyLevel;
        RtcManager.Instance(mContext).setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_AUDIENCE, mClientRoleOptions);
    }

    public Maybe<Member> getMember(String userId) {
        return DataRepositroy.Instance(mContext)
                .getMember(mRoom.getObjectId(), userId)
                .doOnSuccess(new Consumer<Member>() {
                    @Override
                    public void accept(Member member) throws Exception {
                        member.setRoomId(mRoom);
                    }
                });
    }

    public Completable agreeInvite(@NonNull Member member) {
        mLogger.d("agreeInvite() called with: member = [" + member + "]");
        return DataRepositroy.Instance(mContext)
                .agreeInvite(member)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        startLivePlay();
                        onInviteAgree(member);
                    }
                });
    }

    public Completable refuseInvite(@NonNull Member member) {
        mLogger.d("refuseInvite() called with: member = [" + member + "]");
        return DataRepositroy.Instance(mContext)
                .refuseInvite(member)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        onInviteRefuse(member);
                    }
                });
    }

    public Completable agreeHandsUp(@NonNull Member member) {
        mLogger.d("agreeHandsUp() called with: member = [" + member + "]");
        return DataRepositroy.Instance(mContext)
                .agreeHandsUp(member)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        onHandUpAgree(member);
                    }
                });
    }

    public Completable refuseHandsUp(@NonNull Member member) {
        mLogger.d("refuseHandsUp() called with: member = [" + member + "]");
        return DataRepositroy.Instance(mContext)
                .refuseHandsUp(member)
                .doOnComplete(new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        onHandUpRefuse(member);
                    }
                });
    }

    @Override
    public boolean isMembersContainsKey(@NonNull String memberId) {
        return membersMap.containsKey(memberId);
    }

    @Override
    public Member getMemberById(@NonNull String memberId) {
        return membersMap.get(memberId);
    }

    @Override
    public Member getMemberByStramId(long streamId) {
        return streamIdMap.get(streamId);
    }

    @Override
    public Maybe<Member> getMember(@NonNull String roomId, @NonNull String userId) {
        return DataRepositroy.Instance(mContext).getMember(roomId, userId);
    }

    @Override
    public boolean isOwner(@NonNull Member member) {
        return ObjectsCompat.equals(member.getUserId(), mRoom.getAnchorId());
    }

    @Override
    public boolean isOwner() {
        return isOwner(mMine);
    }

    @Override
    public boolean isOwner(String userId) {
        return ObjectsCompat.equals(userId, mRoom.getAnchorId().getObjectId());
    }

    @Override
    public boolean isMine(@NonNull Member member) {
        return ObjectsCompat.equals(member, mMine);
    }

    @Override
    public void onMemberJoin(@NonNull Member member) {
        mLogger.d("onMemberJoin() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        if (!TextUtils.isEmpty(member.getObjectId())) {
            membersMap.put(member.getObjectId(), member);
        }

        if (member.getStreamId() != null) {
            streamIdMap.put(member.getStreamId(), member);
        }

        mainThreadDispatch.onMemberJoin(member);
    }

    @Override
    public void onMemberLeave(@NonNull Member member) {
        mLogger.d("onMemberLeave() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        if (!TextUtils.isEmpty(member.getObjectId())) {
            membersMap.remove(member.getObjectId());
        }

        if (member.getStreamId() != null) {
            streamIdMap.remove(member.getStreamId());
        }

        if (isOwner(member)) {
            mainThreadDispatch.onOwnerLeaveRoom(getRoom());
            leaveRoom();
        } else {
            mainThreadDispatch.onMemberLeave(member);
        }
    }

    @Override
    public void onRoleChanged(boolean isMine, @NonNull Member member) {
        mLogger.d("onRoleChanged() called with: isMine = [" + isMine + "], member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        Member old = getMemberById(member.getObjectId());
        if (old == null) {
            return;
        }
        old.setIsSpeaker(member.getIsSpeaker());

        if (isMine(old)) {
            if (old.getIsSpeaker() == 1) {
                startLivePlay();
            } else {
                stopLivePlay();
            }
        }
        mainThreadDispatch.onRoleChanged(isMine, old);
    }

    @Override
    public void onAudioStatusChanged(boolean isMine, @NonNull Member member) {
        mLogger.d("onAudioStatusChanged() called with: isMine = [" + isMine + "], member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        Member old = getMemberById(member.getObjectId());
        if (old == null) {
            return;
        }
        old.setIsMuted(member.getIsMuted());
        old.setIsSelfMuted(member.getIsSelfMuted());
        mainThreadDispatch.onAudioStatusChanged(isMine, old);
    }

    @Override
    public void onReceivedHandUp(@NonNull Member member) {
        mLogger.d("onReceivedHandUp() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onReceivedHandUp(member);
    }

    @Override
    public void onHandUpAgree(@NonNull Member member) {
        mLogger.d("onHandUpAgree() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onHandUpAgree(member);
    }

    @Override
    public void onHandUpRefuse(@NonNull Member member) {
        mLogger.d("onHandUpRefuse() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onHandUpRefuse(member);
    }

    @Override
    public void onReceivedInvite(@NonNull Member member) {
        mLogger.d("onReceivedInvite() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onReceivedInvite(member);
    }

    @Override
    public void onInviteAgree(@NonNull Member member) {
        mLogger.d("onInviteAgree() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onInviteAgree(member);
    }

    @Override
    public void onInviteRefuse(@NonNull Member member) {
        mLogger.d("onInviteRefuse() called with: member = [" + member + "]");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onInviteRefuse(member);
    }

    @Override
    public void onEnterMinStatus() {
        mLogger.d("onEnterMinStatus() called");
        if (isLeaving) {
            return;
        }

        mainThreadDispatch.onEnterMinStatus();
    }

    @Override
    public void onRoomError(int error) {
        mLogger.d("onRoomError() called with: error = [" + error + "]");
        mainThreadDispatch.onRoomError(error);
    }
}
