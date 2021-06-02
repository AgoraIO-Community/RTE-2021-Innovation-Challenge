package com.agora.data.provider.service;

import com.agora.data.model.Action;

import cn.leancloud.AVObject;

public class ActionService extends AttributeManager<Action> {

    private volatile static ActionService instance;

    public static final String OBJECT_KEY = "ACTION";

    public static final String TAG_ROOMID = "roomId";
    public static final String TAG_MEMBERID = "memberId";
    public static final String TAG_ACTION = "action";
    public static final String TAG_STATUS = "status";

    private ActionService() {
        super();
    }

    public synchronized static ActionService Instance() {
        if (instance == null) {
            synchronized (ActionService.class) {
                if (instance == null) {
                    instance = new ActionService();
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
    protected Action convertObject(AVObject object) {
        return mGson.fromJson(object.toJSONObject().toJSONString(), Action.class);
    }
}
