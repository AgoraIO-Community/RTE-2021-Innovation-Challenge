package com.kangraoo.basektlib.ui.mvp

import com.kangraoo.basektlib.ui.IBaseView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by hyy on 2018/08/31.
 */
abstract class BaseRxPresenter<V : IBaseView> : BasePresenter<V>(), IRxPresenter {
    protected var mCompositeDisposable = CompositeDisposable()
    override fun attachView(view: V) {
        super.attachView(view)
        registerObservers()
    }

    /**
     * For register Rx observers
     */
    open fun registerObservers() {}

    override fun remove(s: Disposable) {
        mCompositeDisposable.remove(s)
    }

    override fun add(s: Disposable) {
        mCompositeDisposable.add(s)
    }

    override fun addAll(vararg ds: Disposable) {
        mCompositeDisposable.addAll(*ds)
    }

    override fun detachView() {
        unsubscribe()
        super.detachView()
    }

    override fun unsubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear()
        }
    }

    override fun onResume() {}
    override fun onPause() {}
}
