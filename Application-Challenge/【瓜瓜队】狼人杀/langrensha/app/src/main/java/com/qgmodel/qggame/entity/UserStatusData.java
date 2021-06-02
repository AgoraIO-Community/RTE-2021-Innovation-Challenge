package com.qgmodel.qggame.entity;

import android.view.SurfaceView;

public class UserStatusData {

    public int mUid;
    public SurfaceView mView;

    public UserStatusData(Integer mUid, SurfaceView mView) {
        this.mUid = mUid;
        this.mView = mView;
    }

    public int getmUid() {
        return mUid;
    }

    public void setmUid(int mUid) {
        this.mUid = mUid;
    }

    public SurfaceView getmView() {
        return mView;
    }

    public void setmView(SurfaceView mView) {
        this.mView = mView;
    }
}
