package com.kangraoo.basektlib.tools.rx

import com.kangraoo.basektlib.tools.rx.RxTransformerHelper.ioToUI
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

object RxTimer {

    fun interval(seconds: Int, callback: RxIntervalCallback) {
        // interval对应参数 ：首次执行延时时间 、 每次轮询间隔时间 、 时间类型
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .take(seconds + 1L)
            .map {
                seconds - it
            }.ioToUI()
            .subscribe(object : Observer<Long> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    callback.currentDisposable(d)
                }

                override fun onNext(t: Long) {
                    if (t == 0L) {
                        callback.finish()
                        return
                    }

                    callback.interval(t)
                }

                override fun onError(e: Throwable) {
                    callback.finish()
                }
            })
    }
}

interface RxIntervalCallback {
    fun currentDisposable(disposable: Disposable)

    fun interval(t: Long)

    fun finish()
}
