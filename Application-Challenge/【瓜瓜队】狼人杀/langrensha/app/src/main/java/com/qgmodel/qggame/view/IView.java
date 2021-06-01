package com.qgmodel.qggame.view;

import android.content.Context;
import android.view.View;

import com.qgmodel.qggame.contract.Contract;
import com.qgmodel.qggame.presenter.IPresenter;

/**
 * Created by HeYanLe on 2020/8/8 0008 17:27.
 * https://github.com/heyanLE
 */
public interface IView {

    Context getContext();

    <T extends View> T findViewById(int id);

}
