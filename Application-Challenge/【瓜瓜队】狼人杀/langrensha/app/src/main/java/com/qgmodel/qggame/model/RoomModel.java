package com.qgmodel.qggame.model;

import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.model.exception.RoomException;

import java.util.List;

/**
 * Room 业务接口
 * @see com.qgmodel.qggame.model.impl.RoomModelImpl
 * Created by HeYanLe on 2020/8/8 0008 16:52.
 * https://github.com/heyanLE
 */
public interface RoomModel {

    public interface OnGetAll{
        void all(List<RoomInfo> rooms);
    }

    void deleteRoom(String roomerId);

    void getAll(OnGetAll onGetAll) ;

    /**
     * 创建新房间
     * @return  新房间实体
     * @throws RoomException    异常
     * @see com.qgmodel.qggame.model.exception.RoomException.RoomExceptionEnum
     */
    RoomInfo create() throws RoomException;

    /**
     * 获取当前房间
     * @return  房间实体
     * @throws RoomException    异常
     * @see com.qgmodel.qggame.model.exception.RoomException.RoomExceptionEnum
     */
    RoomInfo now() throws RoomException;

    /**
     * 进入房间
     * @param roomId    房间 Id
     * @return  房间实体
     * @throws RoomException    异常
     * @see com.qgmodel.qggame.model.exception.RoomException.RoomExceptionEnum
     */
    RoomInfo enter(long roomId) throws RoomException;


    /**
     * 退出房间
     * @throws RoomException    异常
     * @see com.qgmodel.qggame.model.exception.RoomException.RoomExceptionEnum
     */
    void exit() throws RoomException;

}
