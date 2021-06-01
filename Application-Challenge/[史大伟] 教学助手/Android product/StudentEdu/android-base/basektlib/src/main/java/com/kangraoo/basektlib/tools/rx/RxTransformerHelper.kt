package com.kangraoo.basektlib.tools.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.JsonSyntaxException
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HNetwork
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.rx.schedulers.SchedulerProvider
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

object RxTransformerHelper {
    //    subscribeOn 用于指定 subscribe() 时所发生的线程
    // observeOn 方法用于指定下游 Observer 回调发生的线程。
    // 取消订阅位于特定线程中
//    fun <T> ioToUI(): ObservableTransformer<T, T> {
//        return ObservableTransformer { observable ->
//            observable.subscribeOn(SchedulerProvider.instance.io())
//                .unsubscribeOn(SchedulerProvider.instance.ui())
//                .observeOn(SchedulerProvider.instance.ui())
//        }
//    }

    fun <T> Observable<T>.ioToUI(): Observable<T> =
        this.subscribeOn(SchedulerProvider.instance.io())
            .unsubscribeOn(SchedulerProvider.instance.ui())
            .observeOn(SchedulerProvider.instance.ui())

    fun <T> Observable<T>.newThreadToUI(): Observable<T> =
        this.subscribeOn(SchedulerProvider.instance.newThread())
            .unsubscribeOn(SchedulerProvider.instance.ui())
            .observeOn(SchedulerProvider.instance.ui())

//    fun <T> newThreadToUI(): ObservableTransformer<T, T> {
//        return ObservableTransformer { observable ->
//            observable.subscribeOn(SchedulerProvider.instance.newThread())
//                .unsubscribeOn(SchedulerProvider.instance.ui())
//                .observeOn(SchedulerProvider.instance.ui())
//        }
//    }

    /*
    * 对Observable销毁进行生命周期绑定
    *
    * */
    fun <T> Observable<T>.destory(owner: LifecycleOwner): ObservableSubscribeProxy<T> {
        return this.autoDispose(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY))
    }

    /*
    * 对UI线程调度和生命周期绑定
    *
    * */
    fun <T> Observable<T>.transform(owner: LifecycleOwner): ObservableSubscribeProxy<T> {
        return this.ioToUI().destory(owner)
    }

    val networkMsg: Int = R.string.libNetFailCheck
    val networkNoMsg: Int = R.string.libNetNotNetCheck
    val networkTimeOutMsg: Int = R.string.libNetTimeOutCheck
    val networkErrorMsg: Int = R.string.libNetErrorCheck
    val networkSuccessMsg: Int = R.string.libNetSuccessCheck
    val networkJsonErrorMsg: Int = R.string.libNetJsonErrorCheck
    val networkUnkonwnHostErrorMsg: Int = R.string.libNetUnknownHostCheck

    private val onNextStub: (Any) -> Unit = {}
    // 重写rxkotlin,全局toast异常
    private val onErrorStub: (Throwable) -> Unit = {
        var throwable: Throwable? = it
        // 获取最根源的异常
        while (throwable!!.cause != null) {
            throwable = throwable.cause
        }
        if (throwable is ConnectException) {
            TipToast.tip(
                Tip.Error,
                networkMsg
            )
        } else if (throwable is SocketTimeoutException) {
            TipToast.tip(
                Tip.Error,
                networkTimeOutMsg
            )
        } else if (throwable is JsonSyntaxException) {
            TipToast.tip(
                Tip.Error,
                networkJsonErrorMsg
            )
        } else if (throwable is HttpException) {
            val code = (throwable as HttpException).code()
            val msg: String = HString.concatObject(
                " ",
                networkErrorMsg,
                "code:",
                code
            )
            TipToast.tip(Tip.Error, msg)
        } else if (throwable is UnknownHostException) {
            if (HNetwork.checkNetwork(SApplication.context())) {
                TipToast.tip(
                    Tip.Error,
                    networkUnkonwnHostErrorMsg
                )
            } else {
                TipToast.tip(
                    Tip.Error,
                    networkNoMsg
                )
            }
        }
    }
    private val onCompleteStub: () -> Unit = {}

    /**
     * Overloaded subscribe function that allows passing named parameters
     */
    fun <T : Any> Observable<T>.subscribeNext(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub,
        onNext: (T) -> Unit = onNextStub
    ): Disposable = subscribe(onNext, onError, onComplete)

//    /*
// * 数据转换
// *
// * */
//    fun <T> Observable<BaseResp<T>>.dataConvert(): Observable<T> {
//        return flatMap {
//            if (it.errorCode == ResponseCode.SUCCESS) Observable.just(it.result) else Observable.error(Throwable(message = it.reason))
//        }
}
