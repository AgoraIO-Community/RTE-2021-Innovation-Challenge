package com.qgmodel.qggame.presenter.impl;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.model.Model;
import com.qgmodel.qggame.model.RoomModel;
import com.qgmodel.qggame.presenter.LobbyPresenter;
import com.qgmodel.qggame.view.IView;
import com.qgmodel.qggame.view.activity.LobbyActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HeYanLe on 2020/8/8 0008 18:51.
 * https://github.com/heyanLE
 */
public class LobbyPresenterImpl implements LobbyPresenter {

    @Override
    public void onBind(LobbyActivity view) {

        view.getBinding().refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view.getBinding().refresh.setRefreshing(true);
                Model.ROOM.getAll(new RoomModel.OnGetAll() {
                    @Override
                    public void all(List<RoomInfo> rooms) {
                        view.getRooms().clear();
                        if(rooms != null) {
                            view.getRooms().addAll(rooms);
                        }
                        view.getBinding().refresh.setRefreshing(false);
                        view.getRoomAdapter().notifyDataSetChanged();
                    }
                });
            }
        });
        view.getBinding().refresh.setRefreshing(true);
        Model.ROOM.getAll(new RoomModel.OnGetAll() {
            @Override
            public void all(List<RoomInfo> rooms) {
                view.getRooms().clear();
                if(rooms != null) {
                    view.getRooms().addAll(rooms);
                }
                view.getBinding().refresh.setRefreshing(false);
                view.getRoomAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void onInit() {

    }


}
