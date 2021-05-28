package com.kangraoo.basektlib.ui.mvp

import android.os.Bundle
import com.kangraoo.basektlib.ui.BFragment
import com.kangraoo.basektlib.ui.IBaseView

abstract class BMvpFragment<V : IBaseView, P : BasePresenter<V>> : BFragment() {
    protected val _presenter: P by lazy {
        createPresenterInstance()
    }

    abstract fun createPresenterInstance(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(_presenter)
        _presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(_presenter)
    }
}
