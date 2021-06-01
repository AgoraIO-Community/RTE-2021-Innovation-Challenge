package com.kangraoo.basektlib.tools.rx

import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

object RxExt {
    private const val TAG = "RxExt"
    fun clickThrottle(): ObservableTransformer<*, *> {
        return ObservableTransformer<Any?, Any?> { observable ->
            observable.throttleFirst(
                500,
                TimeUnit.MILLISECONDS
            )
        }
    }
}
