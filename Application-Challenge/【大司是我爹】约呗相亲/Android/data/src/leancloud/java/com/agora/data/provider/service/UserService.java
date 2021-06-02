package com.agora.data.provider.service;

import cn.leancloud.AVObject;

import com.agora.data.model.User;

public class UserService extends AttributeManager<User> {

    private volatile static UserService instance;

    public static final String OBJECT_KEY = "USER";

    public static final String TAG_NAME = "name";
    public static final String TAG_AVATAR = "avatar";

    private UserService() {
        super();
    }

    public synchronized static UserService Instance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
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
    protected User convertObject(AVObject object) {
        return mGson.fromJson(object.toJSONObject().toJSONString(), User.class);
    }
}
