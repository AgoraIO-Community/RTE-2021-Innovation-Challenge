package com.kangraoo.basektlib.ui.mvvm

import android.app.Activity
import android.os.Handler
import androidx.lifecycle.*
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.tip.ITipToast
import com.kangraoo.basektlib.ui.IBaseView
import com.kangraoo.basektlib.ui.action.IAction

abstract class BViewModel : ViewModel(), LifecycleObserver, IBaseView {

    val stateActionEvent = MutableLiveData<BStateActionEvent>()

    private lateinit var lifcycleOwner: LifecycleOwner
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        this.lifcycleOwner = owner
    }

    private var map: HashMap<String, IAction<IBaseView>>? = null

    fun addAction(name: String, action: IAction<IBaseView>) {
        if (map == null) {
            map = HashMap()
        }
        map!![name] = action
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        if (map != null) {
            map!!.forEach {
                it.value.setBaseView(this)
                it.value.initAction()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {}

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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        if (map != null) {
            map!!.forEach {
                it.value.detach()
            }
            map!!.clear()
            map = null
        }
    }

    override fun visitActivity(): Activity {
        return ActivityLifeManager.getCurrentActivity()!!
    }
    private val handler: Handler by lazy {
        HAction.mainHandler
    }
    override fun postRunnable(runnable: Runnable) {
        handler.post { runnable.run() }
    }

    override fun postDelayed(runnable: Runnable, delay: Long) {
        handler.postDelayed({ runnable.run() }, delay)
    }

    override fun removeCallbacks(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }

     override fun showToastMsg(var1: String?) {
         stateActionEvent.value = BStateActionEvent.ToastMsgState(var1)
     }

     override fun showToastMsg(var1: Int) {
         stateActionEvent.value = BStateActionEvent.ToastIntMsgState(var1)
     }

     override fun showToastMsg(iTipToast: ITipToast, message: Int) {
         stateActionEvent.value = BStateActionEvent.ToastTipIntMsgState(iTipToast, message)
     }

     override fun showToastMsg(iTipToast: ITipToast, message: String?) {
         stateActionEvent.value = BStateActionEvent.ToastTipMsgState(iTipToast, message)
     }

     override fun showProgressingDialog() {
         stateActionEvent.value = BStateActionEvent.ProgressingState(true)
     }

     override fun showProgressingDialog(var1: Int) {
         stateActionEvent.value = BStateActionEvent.ProgressingIntMessageState(var1)
     }

     override fun showProgressingDialog(var1: String) {
         stateActionEvent.value = BStateActionEvent.ProgressingMessageState(var1)
     }

     override fun dismissProgressDialog() {
         stateActionEvent.value = BStateActionEvent.ProgressingState(false)
     }

    companion object {
        @JvmStatic
        fun <T : BViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
            return ViewModelFactory(viewModel)
        }
    }
}

/**
 * 假如我们需要在实例化ViewModel的时候传入参数，那么我们就必须使用工厂方式来创建ViewModel
 * 创建ViewModel的工厂，以此方法创建的ViewModel，可在构造函数中传参
 */
class ViewModelFactory(val viewModel: BViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}

// ViewModel的创建并不止一种形式，我们常用的创建方式，如下
// val viewModel = ViewModelProvider(this).get(vm::class.java)
// 弊端：无法viewmodel的构造函数传入更多数据
