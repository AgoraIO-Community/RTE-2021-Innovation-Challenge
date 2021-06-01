package com.agora.data.provider;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;

import com.agora.data.BaseError;
import com.agora.data.manager.RoomManager;
import com.agora.data.model.Action;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.model.User;
import com.agora.data.observer.DataMaybeObserver;
import com.agora.data.provider.service.ActionService;
import com.agora.data.provider.service.AttributeManager;
import com.agora.data.provider.service.MemberService;
import com.agora.data.provider.service.RoomService;
import com.agora.data.provider.service.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.types.AVNull;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class MessageSource extends BaseMessageSource {

    private Gson mGson = new Gson();
    private Context mContext;

    /**
     * 申请举手用户列表
     */
    private final Map<String, Member> handUpMembers = new ConcurrentHashMap<>();

    public MessageSource(@NonNull Context context, @NonNull IRoomProxy iRoomProxy) {
        super(iRoomProxy);
        this.mContext = context;
    }

    @Override
    public Observable<Member> joinRoom(@NonNull Room room, @NonNull Member member) {
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());
        AVObject userAVObject = AVObject.createWithoutData(UserService.OBJECT_KEY, member.getUserId().getObjectId());

        AVQuery<AVObject> avQuery = AVQuery.getQuery(MemberService.OBJECT_KEY)
                .whereEqualTo(MemberService.TAG_USERID, userAVObject)
                .whereEqualTo(MemberService.TAG_ROOMID, roomAVObject);

        return avQuery.countInBackground()
                .subscribeOn(Schedulers.io())
                .concatMap(new Function<Integer, ObservableSource<Member>>() {
                    @Override
                    public ObservableSource<Member> apply(@NonNull Integer integer) throws Exception {
                        if (integer <= 0) {
                            AVObject avObject = new AVObject(MemberService.OBJECT_KEY);
                            avObject.put(MemberService.TAG_ROOMID, roomAVObject);
                            avObject.put(MemberService.TAG_USERID, userAVObject);

                            avObject.put(MemberService.TAG_STREAMID, member.getStreamId());
                            avObject.put(MemberService.TAG_IS_SPEAKER, member.getIsSpeaker());
                            avObject.put(MemberService.TAG_ISMUTED, member.getIsMuted());
                            avObject.put(MemberService.TAG_ISSELFMUTED, member.getIsSelfMuted());
                            return avObject.saveInBackground()
                                    .flatMap(new Function<AVObject, ObservableSource<Member>>() {
                                        @Override
                                        public ObservableSource<Member> apply(@NonNull AVObject avObject) throws Exception {
                                            Member memberTemp = mGson.fromJson(avObject.toJSONObject().toJSONString(), Member.class);
                                            memberTemp.setUserId(member.getUserId());
                                            memberTemp.setRoomId(member.getRoomId());
                                            return Observable.just(memberTemp);
                                        }
                                    });
                        } else {
                            avQuery.include(MemberService.TAG_ROOMID);
                            avQuery.include(MemberService.TAG_ROOMID + "." + RoomService.ANCHOR_ID_KEY);
                            avQuery.include(MemberService.TAG_USERID);
                            return avQuery.getFirstInBackground()
                                    .flatMap(new Function<AVObject, ObservableSource<Member>>() {
                                        @Override
                                        public ObservableSource<Member> apply(@NonNull AVObject avObject) throws Exception {
                                            AVObject userObject = avObject.getAVObject(MemberService.TAG_USERID);
                                            AVObject roomObject = avObject.getAVObject(MemberService.TAG_ROOMID);
                                            AVObject ancherObject = roomObject.getAVObject(RoomService.ANCHOR_ID_KEY);

                                            User user = mGson.fromJson(userObject.toJSONObject().toJSONString(), User.class);
                                            Room room = mGson.fromJson(roomObject.toJSONObject().toJSONString(), Room.class);
                                            User ancher = mGson.fromJson(ancherObject.toJSONObject().toJSONString(), User.class);
                                            room.setAnchorId(ancher);

                                            Member memberTemp = mGson.fromJson(avObject.toJSONObject().toJSONString(), Member.class);
                                            memberTemp.setUserId(user);
                                            memberTemp.setRoomId(room);
                                            return Observable.just(memberTemp);
                                        }
                                    });
                        }
                    }
                }).doOnComplete(() -> {
                    registerMemberChanged();

                    //todo：如果同时订阅，会导致前一个订阅收不到回调，所以这里做了延迟
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ObjectsCompat.equals(room.getAnchorId(), member.getUserId())) {
                                registerAnchorActionStatus();
                            } else {
                                registerMemberActionStatus();
                            }
                        }
                    }, 500L);
                });
    }

    @Override
    public Completable leaveRoom(@NonNull Room room, @NonNull Member member) {
        unregisterAnchorActionStatus();
        unregisterMemberActionStatus();
        unregisterMemberChanged();

        if (ObjectsCompat.equals(room.getAnchorId(), member.getUserId())) {
            AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

            //删除Member。
            AVQuery<AVObject> avQueryMember = AVQuery.getQuery(MemberService.OBJECT_KEY)
                    .whereEqualTo(MemberService.TAG_ROOMID, roomAVObject);

            //删除Action
            AVQuery<AVObject> avQueryAction = AVQuery.getQuery(ActionService.OBJECT_KEY)
                    .whereEqualTo(ActionService.TAG_ROOMID, roomAVObject);

            //删除房间
            AVObject avObjectRoom = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

            return Observable.concat(avQueryMember.deleteAllInBackground(), avQueryAction.deleteAllInBackground(), avObjectRoom.deleteInBackground()).concatMapCompletable(new Function<AVNull, CompletableSource>() {
                @Override
                public CompletableSource apply(@NonNull AVNull avNull) throws Exception {
                    return Completable.complete();
                }
            }).subscribeOn(Schedulers.io());
        } else {
            AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());
            AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());

            //删除Action
            AVQuery<AVObject> avQueryAction = AVQuery.getQuery(ActionService.OBJECT_KEY)
                    .whereEqualTo(ActionService.TAG_ROOMID, roomAVObject)
                    .whereEqualTo(ActionService.TAG_MEMBERID, memberAVObject);

            //删除Member
            AVObject avObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());

            return avQueryAction.deleteAllInBackground()
                    .concatWith(avObject.deleteInBackground()).concatMapCompletable(new Function<AVNull, CompletableSource>() {
                        @Override
                        public CompletableSource apply(@NonNull AVNull avNull) throws Exception {
                            return Completable.complete();
                        }
                    }).subscribeOn(Schedulers.io());
        }
    }

    @Override
    public Completable muteVoice(@NonNull Member member, int muted) {
        AVObject avObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        avObject.put(MemberService.TAG_ISMUTED, muted);
        return avObject.saveInBackground()
                .subscribeOn(Schedulers.io())
                .concatMapCompletable(new Function<AVObject, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                        return Completable.complete();
                    }
                });
    }

    @Override
    public Completable muteSelfVoice(@NonNull Member member, int muted) {
        AVObject avObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        avObject.put(MemberService.TAG_ISSELFMUTED, muted);
        return avObject.saveInBackground()
                .subscribeOn(Schedulers.io())
                .concatMapCompletable(new Function<AVObject, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                        return Completable.complete();
                    }
                });
    }

    @Override
    public Completable requestHandsUp(@NonNull Member member) {
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        AVObject avObject = new AVObject(ActionService.OBJECT_KEY);
        avObject.put(ActionService.TAG_MEMBERID, memberAVObject);
        avObject.put(ActionService.TAG_ROOMID, roomAVObject);
        avObject.put(ActionService.TAG_ACTION, Action.ACTION.HandsUp.getValue());
        avObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Ing.getValue());
        return avObject.saveInBackground()
                .concatMapCompletable(new Function<AVObject, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                        return Completable.complete();
                    }
                });
    }

    @Override
    public Completable agreeHandsUp(@NonNull Member member) {
        AVObject memberObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        //更新Member表
        memberObject.put(MemberService.TAG_IS_SPEAKER, 1);

        //更新Action表
        AVObject actionObject = new AVObject(ActionService.OBJECT_KEY);
        actionObject.put(ActionService.TAG_MEMBERID, memberObject);
        actionObject.put(ActionService.TAG_ROOMID, roomAVObject);
        actionObject.put(ActionService.TAG_ACTION, Action.ACTION.HandsUp.getValue());
        actionObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Agree.getValue());

        return Completable.concatArray(memberObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        }), actionObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        })).subscribeOn(Schedulers.io()).doOnComplete(new io.reactivex.functions.Action() {
            @Override
            public void run() throws Exception {
                handUpMembers.remove(member.getObjectId());
            }
        });
    }

    @Override
    public Completable refuseHandsUp(@NonNull Member member) {
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        //更新Action表
        AVObject actionObject = new AVObject(ActionService.OBJECT_KEY);
        actionObject.put(ActionService.TAG_MEMBERID, memberAVObject);
        actionObject.put(ActionService.TAG_ROOMID, roomAVObject);
        actionObject.put(ActionService.TAG_ACTION, Action.ACTION.HandsUp.getValue());
        actionObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Refuse.getValue());

        return actionObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        }).subscribeOn(Schedulers.io()).doOnComplete(new io.reactivex.functions.Action() {
            @Override
            public void run() throws Exception {
                handUpMembers.remove(member.getObjectId());
            }
        });
    }

    @Override
    public Completable inviteSeat(@NonNull Member member) {
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        AVObject avObject = new AVObject(ActionService.OBJECT_KEY);
        avObject.put(ActionService.TAG_MEMBERID, memberAVObject);
        avObject.put(ActionService.TAG_ROOMID, roomAVObject);
        avObject.put(ActionService.TAG_ACTION, Action.ACTION.Invite.getValue());
        avObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Ing.getValue());

        return avObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        });
    }

    @Override
    public Completable agreeInvite(@NonNull Member member) {
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        //更新Member表
        memberAVObject.put(MemberService.TAG_IS_SPEAKER, 1);

        //更新Action表
        AVObject actionObject = new AVObject(ActionService.OBJECT_KEY);
        actionObject.put(ActionService.TAG_MEMBERID, memberAVObject);
        actionObject.put(ActionService.TAG_ROOMID, roomAVObject);
        actionObject.put(ActionService.TAG_ACTION, Action.ACTION.Invite.getValue());
        actionObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Agree.getValue());

        return Completable.concatArray(memberAVObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        }), actionObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        })).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable refuseInvite(@NonNull Member member) {
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, member.getRoomId().getObjectId());

        //更新Action表
        AVObject actionObject = new AVObject(ActionService.OBJECT_KEY);
        actionObject.put(ActionService.TAG_MEMBERID, memberAVObject);
        actionObject.put(ActionService.TAG_ROOMID, roomAVObject);
        actionObject.put(ActionService.TAG_ACTION, Action.ACTION.Invite.getValue());
        actionObject.put(ActionService.TAG_STATUS, Action.ACTION_STATUS.Refuse.getValue());

        return actionObject.saveInBackground().concatMapCompletable(new Function<AVObject, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                return Completable.complete();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable seatOff(@NonNull Member member) {
        AVObject memberObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());
        memberObject.put(MemberService.TAG_IS_SPEAKER, 0);
        return memberObject.saveInBackground()
                .subscribeOn(Schedulers.io())
                .concatMapCompletable(new Function<AVObject, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull AVObject avObject) throws Exception {
                        return Completable.complete();
                    }
                });
    }

    @Override
    public Observable<List<Member>> getHandUpList() {
        return Observable.just(new ArrayList<>(handUpMembers.values()));
    }

    @Override
    public int getHandUpListCount() {
        return handUpMembers.size();
    }

    /**
     * 作为房主，需要监听房间中Action变化。
     */
    private void registerAnchorActionStatus() {
        Room room = iRoomProxy.getRoom();
        if (room == null) {
            return;
        }
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, room.getObjectId());

        AVQuery<AVObject> query = AVQuery.getQuery(ActionService.OBJECT_KEY);
        query.whereEqualTo(ActionService.TAG_ROOMID, roomAVObject);
        Log.i(AttributeManager.TAG, String.format("%s registerObserve roomId= %s", ActionService.OBJECT_KEY, room.getObjectId()));
        ActionService.Instance().registerObserve(query, new AttributeManager.AttributeListener<Action>() {
            @Override
            public void onCreated(Action item) {
                if (item.getAction() == Action.ACTION.HandsUp.getValue()) {
                    if (item.getStatus() == Action.ACTION_STATUS.Ing.getValue()) {
                        Member member = item.getMemberId();
                        member = iRoomProxy.getMemberById(member.getObjectId());
                        if (member == null) {
                            return;
                        }

                        if (handUpMembers.containsKey(member.getObjectId())) {
                            return;
                        }

                        handUpMembers.put(member.getObjectId(), member);
                        iRoomProxy.onReceivedHandUp(member);
                    }
                } else if (item.getAction() == Action.ACTION.Invite.getValue()) {
                    if (item.getStatus() == Action.ACTION_STATUS.Agree.getValue()) {
                        Member member = item.getMemberId();
                        member = iRoomProxy.getMemberById(member.getObjectId());
                        if (member == null) {
                            return;
                        }

                        iRoomProxy.onInviteAgree(member);
                    } else if (item.getStatus() == Action.ACTION_STATUS.Refuse.getValue()) {
                        Member member = item.getMemberId();
                        member = iRoomProxy.getMemberById(member.getObjectId());
                        if (member == null) {
                            return;
                        }

                        iRoomProxy.onInviteRefuse(member);
                    }
                }
            }

            @Override
            public void onUpdated(Action item) {

            }

            @Override
            public void onDeleted(String objectId) {

            }

            @Override
            public void onSubscribeError(int error) {
                if (error == AttributeManager.ERROR_EXCEEDED_QUOTA) {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD_EXCEEDED_QUOTA);
                } else {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD);
                }
            }
        });
    }

    private void unregisterAnchorActionStatus() {
        ActionService.Instance().unregisterObserve();
    }

    /**
     * 作为观众，需要监听自己的Action变化。
     */
    private void registerMemberActionStatus() {
        Member member = iRoomProxy.getMine();
        if (member == null) {
            return;
        }
        AVObject memberAVObject = AVObject.createWithoutData(MemberService.OBJECT_KEY, member.getObjectId());

        AVQuery<AVObject> query = AVQuery.getQuery(ActionService.OBJECT_KEY);
        query.whereEqualTo(ActionService.TAG_MEMBERID, memberAVObject);
        Log.i(AttributeManager.TAG, String.format("%s registerObserve memberId= %s", ActionService.OBJECT_KEY, member.getObjectId()));
        ActionService.Instance().registerObserve(query, new AttributeManager.AttributeListener<Action>() {
            @Override
            public void onCreated(Action item) {
                if (item.getAction() == Action.ACTION.HandsUp.getValue()) {
                    if (item.getStatus() == Action.ACTION_STATUS.Agree.getValue()) {
                        iRoomProxy.onHandUpAgree(member);
                    } else if (item.getStatus() == Action.ACTION_STATUS.Refuse.getValue()) {
                        iRoomProxy.onHandUpRefuse(member);
                    }
                } else if (item.getAction() == Action.ACTION.Invite.getValue()) {
                    if (item.getStatus() == Action.ACTION_STATUS.Ing.getValue()) {
                        iRoomProxy.onReceivedInvite(member);
                    }
                }
            }

            @Override
            public void onUpdated(Action item) {

            }

            @Override
            public void onDeleted(String objectId) {

            }

            @Override
            public void onSubscribeError(int error) {
                if (error == AttributeManager.ERROR_EXCEEDED_QUOTA) {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD_EXCEEDED_QUOTA);
                } else {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD);
                }
            }
        });
    }

    private void unregisterMemberActionStatus() {
        ActionService.Instance().unregisterObserve();
    }

    /**
     * 监听房间内部成员信息变化
     */
    private void registerMemberChanged() {
        Room room = iRoomProxy.getRoom();
        if (room == null) {
            return;
        }
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, room.getObjectId());

        AVQuery<AVObject> query = AVQuery.getQuery(MemberService.OBJECT_KEY);
        query.whereEqualTo(MemberService.TAG_ROOMID, roomAVObject);
        Log.i(AttributeManager.TAG, String.format("%s registerObserve roomId= %s", MemberService.OBJECT_KEY, room.getObjectId()));
        MemberService.Instance().registerObserve(query, new AttributeManager.AttributeListener<Member>() {
            @Override
            public void onCreated(Member member) {
                if (iRoomProxy.isMembersContainsKey(member.getObjectId())) {
                    return;
                }

                iRoomProxy.getMember(room.getObjectId(), member.getUserId().getObjectId())
                        .subscribe(new DataMaybeObserver<Member>(mContext) {
                            @Override
                            public void handleError(@NonNull BaseError e) {

                            }

                            @Override
                            public void handleSuccess(@Nullable Member member) {
                                if (member == null) {
                                    return;
                                }

                                if (iRoomProxy.isMembersContainsKey(member.getObjectId())) {
                                    return;
                                }

                                iRoomProxy.onMemberJoin(member);
                            }
                        });
            }

            @Override
            public void onUpdated(Member member) {
                if (!iRoomProxy.isMembersContainsKey(member.getObjectId())) {
                    return;
                }

                Member memberOld = iRoomProxy.getMemberById(member.getObjectId());
                if (memberOld == null) {
                    return;
                }

                if (memberOld.getIsSpeaker() != member.getIsSpeaker()) {
                    iRoomProxy.onRoleChanged(false, member);
                }

                if (memberOld.getIsSelfMuted() != member.getIsSelfMuted()) {
                    iRoomProxy.onAudioStatusChanged(false, member);
                }

                if (memberOld.getIsMuted() != member.getIsMuted()) {
                    iRoomProxy.onAudioStatusChanged(false, member);
                }
            }

            @Override
            public void onDeleted(String objectId) {
                if (!iRoomProxy.isMembersContainsKey(objectId)) {
                    return;
                }

                Member member = iRoomProxy.getMemberById(objectId);
                if (member == null) {
                    return;
                }

                iRoomProxy.onMemberLeave(member);
            }

            @Override
            public void onSubscribeError(int error) {
                if (error == AttributeManager.ERROR_EXCEEDED_QUOTA) {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD_EXCEEDED_QUOTA);
                } else {
                    iRoomProxy.onRoomError(RoomManager.ERROR_REGISTER_LEANCLOUD);
                }
            }
        });
    }

    private void unregisterMemberChanged() {
        MemberService.Instance().unregisterObserve();
    }
}
