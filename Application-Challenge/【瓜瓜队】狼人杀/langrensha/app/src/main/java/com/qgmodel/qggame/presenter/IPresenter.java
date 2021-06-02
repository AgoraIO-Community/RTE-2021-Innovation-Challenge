package com.qgmodel.qggame.presenter;

import com.qgmodel.qggame.view.IView;

/**
 * Presenter 接口， 所有 P 层都要实现
 * Created by HeYanLe on 2020/8/8 0008 17:30.
 * https://github.com/heyanLE
 */
public interface IPresenter<T extends IView> {

    /**
     * 当 V 层绑定时调用 - 如果是 Activity 对应 onStart 生命周期
     * @param view  View 层接口
     */
    void onBind(T view);

    /**
     * 当 V 层取消绑定时调用 - 如果是 Activity 对应 onStop 生命周期
     */
    void onUnbind();

    /*
    当 初始化时候调用
     */
    void onInit();

}
