package com.qgmodel.qggame.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by HeYanLe on 2020/8/8 0008 16:04.
 * https://github.com/heyanLE
 */
public class RoomInfo extends BmobObject {

    public static int MAX_PLAYER = 6;


    private String title;
    private String channelName;
    private String roomerId;
    private Integer player = 1;


    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }

    public void setRoomerId(String roomerId) {
        this.roomerId = roomerId;
    }

    public String getRoomerId() {
        return roomerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
