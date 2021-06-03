package io.agora.education.api

interface EduCallback<T> {
    fun onSuccess(res: T?)

    fun onFailure(code: Int, reason: String?)
}
