package com.qgmodel.qggame.contract;

import com.qgmodel.qggame.presenter.IPresenter;
import com.qgmodel.qggame.view.IView;

/**
 * 契约类 - 指定一个 V 层和 P 层
 * Created by HeYanLe on 2020/8/8 0008 17:36.
 * https://github.com/heyanLE
 */
public abstract class Contract<V extends IView, P extends IPresenter<V>> {

    private V iView;
    private P iPresenter;


    public V getView() {
        return iView;
    }

    public P getPresenter() {
        return iPresenter;
    }

    abstract P newPresenter();

    public P bind(V iView){
        this.iView  = iView;
        if (null == iPresenter){
            iPresenter = newPresenter();
            iPresenter.onInit();
        }else{
            iPresenter.onUnbind();
        }
        iPresenter.onBind(iView);
        return iPresenter;
    }

    public void unbind(){
        if (null != iPresenter){
            iPresenter.onUnbind();
            iView = null;
        }
    }


}
