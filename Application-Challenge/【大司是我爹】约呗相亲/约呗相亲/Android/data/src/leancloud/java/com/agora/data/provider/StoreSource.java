package com.agora.data.provider;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.model.User;
import com.agora.data.provider.service.MemberService;
import com.agora.data.provider.service.RoomService;
import com.agora.data.provider.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class StoreSource implements IStoreSource {

    private Gson mGson = new Gson();

    @Override
    public Observable<User> login(@NonNull User user) {
        if (TextUtils.isEmpty(user.getObjectId())) {
            AVObject avObject = new AVObject(UserService.OBJECT_KEY);
            avObject.put(UserService.TAG_NAME, user.getName());
            avObject.put(UserService.TAG_AVATAR, user.getAvatar());
            return avObject.saveInBackground()
                    .subscribeOn(Schedulers.io())
                    .concatMap(new Function<AVObject, ObservableSource<? extends User>>() {
                        @Override
                        public ObservableSource<? extends User> apply(@NonNull AVObject avObject) throws Exception {
                            User user = mGson.fromJson(avObject.toJSONObject().toJSONString(), User.class);
                            return Observable.just(user);
                        }
                    });
        } else {
            AVQuery<AVObject> query = AVQuery.getQuery(UserService.OBJECT_KEY);
            query.whereEqualTo(UserService.TAG_OBJECTID, user.getObjectId());
            return query.countInBackground()
                    .subscribeOn(Schedulers.io())
                    .concatMap(new Function<Integer, Observable<User>>() {
                        @Override
                        public Observable<User> apply(@NonNull Integer integer) throws Exception {
                            if (integer <= 0) {
                                AVObject avObject = new AVObject(UserService.OBJECT_KEY);
                                avObject.put(UserService.TAG_NAME, user.getName());
                                avObject.put(UserService.TAG_AVATAR, user.getAvatar());
                                return avObject.saveInBackground()
                                        .concatMap(new AVObjectToObservable<>(new TypeToken<User>() {
                                        }.getType()));
                            } else {
                                return query.getFirstInBackground()
                                        .concatMap(new Function<AVObject, ObservableSource<? extends User>>() {
                                            @Override
                                            public ObservableSource<? extends User> apply(@NonNull AVObject avObject) throws Exception {
                                                User user = mGson.fromJson(avObject.toJSONObject().toJSONString(), User.class);
                                                return Observable.just(user);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    @Override
    public Observable<User> update(@NonNull User user) {
        AVObject avObject = AVObject.createWithoutData(UserService.OBJECT_KEY, user.getObjectId());
        avObject.put(UserService.TAG_NAME, user.getName());
        avObject.put(UserService.TAG_AVATAR, user.getAvatar());
        return avObject.saveInBackground()
                .subscribeOn(Schedulers.io())
                .concatMap(new AVObjectToObservable<>(new TypeToken<User>() {
                }.getType()));
    }

    @Override
    public Maybe<List<Room>> getRooms() {
        AVQuery<AVObject> query = AVQuery.getQuery(RoomService.OBJECT_KEY);
        query.include(RoomService.ANCHOR_ID_KEY);
        query.limit(10);
        query.orderByDescending(RoomService.TAG_CREATEDAT);
        return query.findInBackground()
                .subscribeOn(Schedulers.io())
                .firstElement()
                .concatMap(avObjects -> {
                    List<Room> rooms = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        AVObject userObject = object.getAVObject(RoomService.ANCHOR_ID_KEY);
                        User user = mGson.fromJson(userObject.toJSONObject().toJSONString(), User.class);

                        Room room = mGson.fromJson(object.toJSONObject().toJSONString(), Room.class);
                        room.setAnchorId(user);
                        rooms.add(room);
                    }
                    return Maybe.just(rooms);
                });
    }

    @Override
    public Observable<Room> getRoomCountInfo(@NonNull Room room) {
        String roomId = room.getObjectId();
        AVObject roomObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, roomId);

        //查询成员数量
        AVQuery<AVObject> queryMember1 = AVQuery.getQuery(MemberService.OBJECT_KEY);
        queryMember1.whereEqualTo(MemberService.TAG_ROOMID, roomObject);

        //查询演讲者数量
        AVQuery<AVObject> queryMember2 = AVQuery.getQuery(MemberService.OBJECT_KEY);
        queryMember2.whereEqualTo(MemberService.TAG_ROOMID, roomObject);
        queryMember2.whereEqualTo(MemberService.TAG_IS_SPEAKER, 1);

        return Observable.zip(queryMember1.countInBackground(), queryMember2.countInBackground(), new BiFunction<Integer, Integer, Room>() {
            @NonNull
            @Override
            public Room apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                room.setMembers(integer);
                return room;
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Maybe<Room> getRoomSpeakersInfo(@NonNull Room room) {
        String roomId = room.getObjectId();
        AVObject roomObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, roomId);

        //查询3个用户成员
        AVQuery<AVObject> queryMember3 = AVQuery.getQuery(MemberService.OBJECT_KEY);
        queryMember3.whereEqualTo(MemberService.TAG_ROOMID, roomObject);
        queryMember3.whereEqualTo(MemberService.TAG_IS_SPEAKER, 1);
        queryMember3.limit(3);
        queryMember3.include(MemberService.TAG_USERID);

        return queryMember3.findInBackground()
                .subscribeOn(Schedulers.io())
                .firstElement()
                .concatMap(new Function<List<AVObject>, MaybeSource<? extends Room>>() {
                    @Override
                    public MaybeSource<? extends Room> apply(@NonNull List<AVObject> avObjects) throws Exception {
                        List<Member> speakers = new ArrayList<>();
                        for (AVObject item : avObjects) {
                            AVObject userObject = item.getAVObject(MemberService.TAG_USERID);
                            User user = mGson.fromJson(userObject.toJSONObject().toJSONString(), User.class);

                            Member member = mGson.fromJson(item.toJSONObject().toJSONString(), Member.class);
                            member.setUserId(user);

                            if (member.getIsSpeaker() == 1) {
                                speakers.add(member);
                            }
                        }
                        room.setSpeakers(speakers);
                        return Maybe.just(room);
                    }
                });
    }

    @Override
    public Observable<Room> creatRoom(@NonNull Room room) {
        AVObject userAVObject = AVObject.createWithoutData(UserService.OBJECT_KEY, room.getAnchorId().getObjectId());
        AVObject avObject = new AVObject(RoomService.OBJECT_KEY);
        avObject.put(RoomService.ANCHOR_ID_KEY, userAVObject);
        avObject.put(RoomService.CHANNEL_NAME_KEY, room.getChannelName());
        return avObject.saveInBackground()
                .subscribeOn(Schedulers.io())
                .concatMap(new AVObjectToObservable<>(new TypeToken<Room>() {
                }.getType()));
    }

    @Override
    public Maybe<Room> getRoom(@NonNull Room room) {
        AVQuery<AVObject> query = AVQuery.getQuery(RoomService.OBJECT_KEY);
        query.include(RoomService.ANCHOR_ID_KEY);
        return query.getInBackground(room.getObjectId())
                .subscribeOn(Schedulers.io())
                .firstElement()
                .concatMap(new Function<AVObject, MaybeSource<? extends Room>>() {
                    @Override
                    public MaybeSource<? extends Room> apply(@NonNull AVObject avObject) throws Exception {
                        AVObject userObject = avObject.getAVObject(RoomService.ANCHOR_ID_KEY);
                        User user = mGson.fromJson(userObject.toJSONObject().toJSONString(), User.class);

                        Room roomNew = mGson.fromJson(avObject.toJSONObject().toJSONString(), Room.class);
                        roomNew.setAnchorId(user);
                        return Maybe.just(roomNew);
                    }
                });
    }

    @Override
    public Observable<List<Member>> getMembers(@NonNull Room room) {
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, room.getObjectId());

        AVQuery<AVObject> query = AVQuery.getQuery(MemberService.OBJECT_KEY);
        query.include(MemberService.TAG_USERID);
        query.include(MemberService.TAG_ROOMID);
        query.include(MemberService.TAG_ROOMID + "." + RoomService.ANCHOR_ID_KEY);
        query.whereEqualTo(MemberService.TAG_ROOMID, roomAVObject);
        return query.findInBackground()
                .subscribeOn(Schedulers.io())
                .concatMap(avObjects -> {
                    List<Member> list = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        AVObject userObject = object.getAVObject(MemberService.TAG_USERID);
                        AVObject roomObject = object.getAVObject(MemberService.TAG_ROOMID);
                        AVObject ancherObject = roomObject.getAVObject(RoomService.ANCHOR_ID_KEY);

                        User user = mGson.fromJson(userObject.toJSONObject().toJSONString(), User.class);
                        Room roomTemp = mGson.fromJson(roomObject.toJSONObject().toJSONString(), Room.class);
                        User ancher = mGson.fromJson(ancherObject.toJSONObject().toJSONString(), User.class);
                        room.setAnchorId(ancher);

                        Member member = mGson.fromJson(object.toJSONObject().toJSONString(), Member.class);
                        member.setUserId(user);
                        member.setRoomId(roomTemp);
                        list.add(member);
                    }
                    return Observable.just(list);
                });
    }

    @Override
    public Maybe<Member> getMember(@NonNull String roomId, @NonNull String userId) {
        AVObject roomAVObject = AVObject.createWithoutData(RoomService.OBJECT_KEY, roomId);
        AVObject userAVObject = AVObject.createWithoutData(UserService.OBJECT_KEY, userId);
        AVQuery<AVObject> avQuery = AVQuery.getQuery(MemberService.OBJECT_KEY);
        avQuery.whereEqualTo(MemberService.TAG_USERID, userAVObject);
        avQuery.whereEqualTo(MemberService.TAG_ROOMID, roomAVObject);
        avQuery.include(MemberService.TAG_ROOMID);
        avQuery.include(MemberService.TAG_ROOMID + "." + RoomService.ANCHOR_ID_KEY);
        avQuery.include(MemberService.TAG_USERID);
        return avQuery.getFirstInBackground()
                .subscribeOn(Schedulers.io()).firstElement()
                .concatMap(new Function<AVObject, MaybeSource<? extends Member>>() {
                    @Override
                    public MaybeSource<? extends Member> apply(@NonNull AVObject avObject) throws Exception {
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
                        return Maybe.just(memberTemp);
                    }
                });
    }
}
