package com.qgmodel.qggame.contract;

import android.view.View;

import com.qgmodel.qggame.presenter.LobbyPresenter;
import com.qgmodel.qggame.presenter.impl.LobbyPresenterImpl;
import com.qgmodel.qggame.view.activity.LobbyActivity;

/**
 * Created by HeYanLe on 2020/8/8 0008 18:00.
 * https://github.com/heyanLE
 */
public class LobbyContract extends Contract<LobbyActivity, LobbyPresenter> {

    @Override
    LobbyPresenter newPresenter() {
        return new LobbyPresenterImpl();
    }

    private static LobbyContract INSTANCE = new LobbyContract();
    public static LobbyContract getInstance(){
        return INSTANCE;
    }
    private LobbyContract(){};

}
