package com.kangraoo.basektlib.ui.mvp

import io.reactivex.disposables.Disposable

/**
 * Created by WaTaNaBe on 2017/9/30.
 */
interface IRxPresenter {
    fun unsubscribe()
    fun add(s: Disposable)
    fun remove(s: Disposable)
    fun addAll(vararg subscriptions: Disposable)
}
