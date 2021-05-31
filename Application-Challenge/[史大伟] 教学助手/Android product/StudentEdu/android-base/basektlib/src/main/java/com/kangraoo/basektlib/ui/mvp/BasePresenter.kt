//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.kangraoo.basektlib.ui.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kangraoo.basektlib.ui.IBaseView
import com.kangraoo.basektlib.ui.action.IAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BasePresenter<V : IBaseView> : LifecycleObserver, CoroutineScope by MainScope() {
    protected lateinit var view: V

    open fun attachView(view: V) {
        this.view = view
        if (map != null) {
            map!!.forEach {
                it.value.setBaseView(view)
                it.value.initAction()
            }
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun detachView() {
        cancel()
        if (map != null) {
            map!!.forEach {
                it.value.detach()
            }
            map!!.clear()
            map = null
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        if (map != null) {
            map!!.forEach {
                it.value.resume()
            }
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        if (map != null) {
            map!!.forEach {
                it.value.pause()
            }
        }
    }

    private var map: HashMap<String, IAction<V>>? = null

    fun addAction(name: String, action: IAction<V>) {
        if (map == null) {
            map = HashMap()
        }
        map!![name] = action
    }

    fun getAction(name: String): IAction<*>? = map!![name]
}
