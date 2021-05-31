package com.qgmodel.qggame.entity;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmAttribute;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class MessageBean {

    private RtmMessage message;
    private String cacheFile;
    private String account;

    private boolean beSelf;


    public MessageBean(String account, RtmMessage message, boolean beSelf) {
        this.account = account;
        this.message = message;
        this.beSelf = beSelf;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public RtmMessage getMessage() {
        return message;
    }

    public void setMessage(RtmMessage message) {
        this.message = message;
    }

    public String getCacheFile() {
        return cacheFile;
    }

    public void setCacheFile(String cacheFile) {
        this.cacheFile = cacheFile;
    }

    public boolean isBeSelf() {
        return beSelf;
    }

    public void setBeSelf(boolean beSelf) {
        this.beSelf = beSelf;
    }
}
