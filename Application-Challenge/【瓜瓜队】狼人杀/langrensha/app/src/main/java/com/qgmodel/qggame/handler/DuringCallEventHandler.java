package com.qgmodel.qggame.handler;

public interface DuringCallEventHandler extends AGEventHandler {

    void onUserJoined(int uid);

    public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

}
