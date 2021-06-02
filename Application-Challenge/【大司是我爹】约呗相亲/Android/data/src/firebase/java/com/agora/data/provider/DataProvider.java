package com.agora.data.provider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class DataProvider implements IDataProvider {

    public static final String TAG_TABLE_USER = "USER";
    public static final String TAG_TABLE_ROOM = "ROOM";
    public static final String TAG_TABLE_MEMBER = "MEMBER";
    public static final String TAG_TABLE_ACTION = "ACTION";

    public static final String USER_OBJECTID = "objectId";
    public static final String USER_NAME = "name";
    public static final String USER_AVATAR = "avatar";
    public static final String USER_CREATEDAT = "createdAt";

    public static final String MEMBER_OBJECTID = "objectId";
    public static final String MEMBER_ISSPEAKER = "isSpeaker";
    public static final String MEMBER_CHANNELNAME = "channelName";
    public static final String MEMBER_ANCHORID = "anchorId";
    public static final String MEMBER_ROOMID = "roomId";
    public static final String MEMBER_USERID = "userId";
    public static final String MEMBER_ISMUTED = "isMuted";
    public static final String MEMBER_ISSELFMUTED = "isSelfMuted";
    public static final String MEMBER_STREAMID = "streamId";
    public static final String MEMBER_CREATEDAT = "createdAt";

    public static final String ROOM_OBJECTID = "objectId";
    public static final String ROOM_CREATEDAT = "createdAt";

    public static final String ACTION_OBJECTID = "objectId";
    public static final String ACTION_MEMBERID = "memberId";
    public static final String ACTION_ROOMID = "roomId";
    public static final String ACTION_ACTION = "action";
    public static final String ACTION_STATUS = "status";
    public static final String ACTION_CREATEDAT = "createdAt";

    private final IStoreSource mIStoreSource;
    private final IMessageSource mIMessageSource;

    public DataProvider(@NonNull Context mContext, @NonNull IRoomProxy iRoomProxy) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        mIStoreSource = new StoreSource();
        mIMessageSource = new MessageSource(mContext, iRoomProxy);
    }

    @Override
    public IStoreSource getStoreSource() {
        return mIStoreSource;
    }

    @Override
    public IMessageSource getMessageSource() {
        return mIMessageSource;
    }
}
