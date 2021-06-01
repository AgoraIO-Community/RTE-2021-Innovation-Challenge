package com.agora.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.agora.data.manager.RoomManager;
import com.agora.data.model.Member;
import com.agora.data.model.Room;
import com.agora.data.model.User;
import com.agora.data.provider.DataProvider;
import com.agora.data.provider.IDataProvider;
import com.agora.data.provider.IMessageSource;
import com.agora.data.provider.IStoreSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class DataRepositroy implements IStoreSource, IMessageSource {
    private static final String TAG = DataRepositroy.class.getSimpleName();

    private volatile static DataRepositroy instance;

    private Context mContext;

    private final IDataProvider mDataProvider;

    private DataRepositroy(Context context) {
        mContext = context.getApplicationContext();

        mDataProvider = new DataProvider(context, RoomManager.Instance(mContext));
    }

    public static synchronized DataRepositroy Instance(Context context) {
        if (instance == null) {
            synchronized (DataRepositroy.class) {
                if (instance == null)
                    instance = new DataRepositroy(context);
            }
        }
        return instance;
    }

    @Override
    public Observable<User> login(@NonNull User user) {
        return mDataProvider.getStoreSource().login(user);
    }

    @Override
    public Observable<User> update(@NonNull User user) {
        return mDataProvider.getStoreSource().update(user);
    }

    @Override
    public Maybe<List<Room>> getRooms() {
        return mDataProvider.getStoreSource().getRooms();
    }

    @Override
    public Observable<Room> getRoomCountInfo(@NonNull Room room) {
        return mDataProvider.getStoreSource().getRoomCountInfo(room);
    }

    @Override
    public Maybe<Room> getRoomSpeakersInfo(@NonNull Room room) {
        return mDataProvider.getStoreSource().getRoomSpeakersInfo(room);
    }

    @Override
    public Observable<Room> creatRoom(@NonNull Room room) {
        return mDataProvider.getStoreSource().creatRoom(room);
    }

    @Override
    public Maybe<Room> getRoom(@NonNull Room room) {
        return mDataProvider.getStoreSource().getRoom(room);
    }

    @Override
    public Observable<List<Member>> getMembers(@NonNull Room room) {
        return mDataProvider.getStoreSource().getMembers(room);
    }

    @Override
    public Maybe<Member> getMember(@NonNull String roomId, @NonNull String userId) {
        return mDataProvider.getStoreSource().getMember(roomId, userId);
    }

    @Override
    public Observable<Member> joinRoom(@NonNull Room room, @NonNull Member member) {
        return mDataProvider.getMessageSource().joinRoom(room, member);
    }

    @Override
    public Completable leaveRoom(@NonNull Room room, @NonNull Member member) {
        return mDataProvider.getMessageSource().leaveRoom(room, member);
    }

    @Override
    public Completable muteVoice(@NonNull Member member, int muted) {
        return mDataProvider.getMessageSource().muteVoice(member, muted);
    }

    @Override
    public Completable muteSelfVoice(@NonNull Member member, int muted) {
        return mDataProvider.getMessageSource().muteSelfVoice(member, muted);
    }

    @Override
    public Completable requestHandsUp(@NonNull Member member) {
        return mDataProvider.getMessageSource().requestHandsUp(member);
    }

    @Override
    public Completable agreeHandsUp(@NonNull Member member) {
        return mDataProvider.getMessageSource().agreeHandsUp(member);
    }

    @Override
    public Completable refuseHandsUp(@NonNull Member member) {
        return mDataProvider.getMessageSource().refuseHandsUp(member);
    }

    @Override
    public Completable inviteSeat(@NonNull Member member) {
        return mDataProvider.getMessageSource().inviteSeat(member);
    }

    @Override
    public Completable agreeInvite(@NonNull Member member) {
        return mDataProvider.getMessageSource().agreeInvite(member);
    }

    @Override
    public Completable refuseInvite(@NonNull Member member) {
        return mDataProvider.getMessageSource().refuseInvite(member);
    }

    @Override
    public Completable seatOff(@NonNull Member member) {
        return mDataProvider.getMessageSource().seatOff(member);
    }

    @Override
    public Observable<List<Member>> getHandUpList() {
        return mDataProvider.getMessageSource().getHandUpList();
    }

    @Override
    public int getHandUpListCount() {
        return mDataProvider.getMessageSource().getHandUpListCount();
    }
}
