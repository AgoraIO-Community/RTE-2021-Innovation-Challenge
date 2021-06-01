package com.kangraoo.basektlib.ui.mvp

import android.os.Bundle
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.ui.IBaseView

abstract class BMvpActivity<V : IBaseView, P : BasePresenter<V>> : BActivity() {
    protected val _presenter: P by lazy {
        createPresenterInstance()
    }

    abstract fun createPresenterInstance(): P

    override fun onViewCreatedBefore(savedInstanceState: Bundle?) {
        super.onViewCreatedBefore(savedInstanceState)
        lifecycle.addObserver(_presenter)
        _presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(_presenter)
    }
}
