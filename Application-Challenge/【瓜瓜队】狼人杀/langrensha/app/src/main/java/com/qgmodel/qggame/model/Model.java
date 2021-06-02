package com.qgmodel.qggame.model;

import com.qgmodel.qggame.model.impl.RoomModelImpl;
import com.qgmodel.qggame.model.impl.UserModelImpl;

/**
 * Model 层总体类
 * Created by HeYanLe on 2020/8/8 0008 17:22.
 * https://github.com/heyanLE
 */
public class Model {


    public static long USERID = 0;
    public static UserModel USER = new UserModelImpl();
    public static RoomModel ROOM = new RoomModelImpl();

}
