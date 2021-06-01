package com.qgmodel.qggame.handler;

import android.util.Log;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;

public class MyRtcEngineEventHandler extends IRtcEngineEventHandler {
    private static final String TAG = "MyRtcEngineEventHandler";
    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();
    private int tag;

    public void addEventHandler(AGEventHandler handler) {
        mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        mEventHandlerList.remove(handler);
    }

    public MyRtcEngineEventHandler(){

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
        while (it.hasNext()){
            Log.d(TAG, "=== next");
            AGEventHandler handler = it.next();
            if (handler instanceof  DuringCallEventHandler){
                ((DuringCallEventHandler) handler).onJoinChannelSuccess(channel,uid,elapsed);
            }
        }
    }

    @Override
    public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
        Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
        while (it.hasNext()){
            AGEventHandler handler = it.next();
            if (handler instanceof DuringCallEventHandler){
                ((DuringCallEventHandler) handler).onRemoteVideoStateChanged(uid,state,reason,elapsed);
            }
        }
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
        while (it.hasNext()){
            AGEventHandler handler = it.next();
            if (handler instanceof DuringCallEventHandler){
                ((DuringCallEventHandler) handler).onUserOffline(uid,reason);
            }
        }
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
        while (it.hasNext()){
            AGEventHandler handler = it.next();
            if (handler instanceof DuringCallEventHandler){
                ((DuringCallEventHandler) handler).onUserJoined(uid);
            }
        }
    }

    public ConcurrentHashMap<AGEventHandler, Integer> getmEventHandlerList() {
        return mEventHandlerList;
    }



    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
