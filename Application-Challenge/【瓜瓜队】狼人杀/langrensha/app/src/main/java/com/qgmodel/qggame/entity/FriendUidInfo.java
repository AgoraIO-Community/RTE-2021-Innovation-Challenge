package com.qgmodel.qggame.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class FriendUidInfo extends BmobObject {

    private String uid;
    private String friendUid;

    public FriendUidInfo(String uid, String friendUid) {
        this.uid = uid;
        this.friendUid = friendUid;
    }

    public FriendUidInfo() {
    }

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
