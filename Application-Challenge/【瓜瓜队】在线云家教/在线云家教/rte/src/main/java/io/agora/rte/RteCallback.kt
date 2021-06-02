package io.agora.rte

interface RteCallback<T> {
    fun onSuccess(res: T?)

    fun onFailure(code: Int, reason: String?)
}
