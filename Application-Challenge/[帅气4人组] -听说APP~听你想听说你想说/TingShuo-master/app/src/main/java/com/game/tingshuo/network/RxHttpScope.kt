package com.game.tingshuo.network
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.*

/**
 * User: ljx
 * Date: 2020-02-05
 * Time: 20:30
 */
open class RxHttpScope() {
    private lateinit var mRefreshLayout:RefreshLayout

    constructor(
        lifecycleOwner: LifecycleOwner,
        refreshLayout: RefreshLayout? = null,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        if (refreshLayout != null) {
            mRefreshLayout = refreshLayout
        }
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) {
                    onCancel()
                    lifecycleOwner.lifecycle.removeObserver(this)
                }
            }
        })
    }

    //协程异常回调
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        catch(throwable)
    }

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + exceptionHandler + SupervisorJob())

    private var onError: ((Throwable) -> Unit)? = {
        ToastUtils.showShort(it.errorMsg())
    }

    private var job: Job? = null

    fun launch(
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = coroutineScope.launch { block() }
        this.job = job
        closeRefreshLayout()
        return job
    }

    private fun onCancel() {
        job?.cancel()
    }

    private fun catch(e: Throwable) {
        onError?.invoke(e)
    }

    //关闭RefreshLayout
    private fun closeRefreshLayout() {
        if (::mRefreshLayout.isInitialized && mRefreshLayout!= null && mRefreshLayout.isRefreshing) {
            mRefreshLayout.finishRefresh()
        }
        if (::mRefreshLayout.isInitialized && mRefreshLayout != null && mRefreshLayout.isLoading) {
            mRefreshLayout.finishLoadMore()
        }
    }
}