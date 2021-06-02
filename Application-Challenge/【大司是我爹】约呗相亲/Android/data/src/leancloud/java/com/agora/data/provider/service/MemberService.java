package com.agora.data.provider.service;

import cn.leancloud.AVObject;

import com.agora.data.model.Member;

public class MemberService extends AttributeManager<Member> {

    private volatile static MemberService instance;

    public static final String OBJECT_KEY = "MEMBER";

    public static final String TAG_ROOMID = "roomId";
    public static final String TAG_STREAMID = "streamId";
    public static final String TAG_USERID = "userId";

    public static final String TAG_IS_SPEAKER = "isSpeaker";//是否是演讲者，区分开观众和演讲者。0-不是，1-是。
    public static final String TAG_ISMUTED = "isMuted";//是否被管理员禁言，0-没，1-被禁言。
    public static final String TAG_ISSELFMUTED = "isSelfMuted";//是否自己禁言，0-没，1-禁言。

    private MemberService() {
        super();
    }

    public synchronized static MemberService Instance() {
        if (instance == null) {
            synchronized (MemberService.class) {
                if (instance == null) {
                    instance = new MemberService();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
    }

    @Override
    protected String getObjectName() {
        return OBJECT_KEY;
    }

    @Override
    protected Member convertObject(AVObject object) {
        return mGson.fromJson(object.toJSONObject().toJSONString(), Member.class);
    }
}
