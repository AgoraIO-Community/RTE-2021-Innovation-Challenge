package com.qgmodel.qggame.model.impl;

import com.qgmodel.qggame.QGApplication;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.model.RoomModel;
import com.qgmodel.qggame.model.exception.RoomException;
import com.qgmodel.qggame.rtmtutorial.AGApplication;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 房间业务接口实现
 * Created by HeYanLe on 2020/8/8 0008 16:52.
 * https://github.com/heyanLE
 */
public class RoomModelImpl implements RoomModel {

    @Override
    public RoomInfo create() throws RoomException {
        return null;
    }

    @Override
    public RoomInfo now() throws RoomException {
        return null;
    }

    @Override
    public RoomInfo enter(long roomId) throws RoomException {
        return null;
    }

    @Override
    public void exit() throws RoomException {

    }

    @Override
    public void deleteRoom(String roomerId) {
        BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
        query.addWhereEqualTo("roomerId",roomerId);
        query.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                if(list != null){
                    for(int i = 0 ; i < list.size() ; i ++){
                        list.get(i).delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public void getAll(OnGetAll onGetAll) {
        BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
        query.setLimit(50);
        query.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                onGetAll.all(list);
                if (e != null){
                    e.printStackTrace();
                }
            }
        });
    }
}
