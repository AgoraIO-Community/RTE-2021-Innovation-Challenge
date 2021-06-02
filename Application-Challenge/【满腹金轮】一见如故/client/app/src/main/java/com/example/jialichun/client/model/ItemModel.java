package com.example.jialichun.client.model;

import java.io.Serializable;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ItemModel implements Serializable {

    public static final int CHAT_A = 1001;
    public static final int CHAT_B = 1002;
    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}
