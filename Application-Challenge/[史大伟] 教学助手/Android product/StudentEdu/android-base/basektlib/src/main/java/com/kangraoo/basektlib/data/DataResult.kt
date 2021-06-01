/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kangraoo.basektlib.data

import com.google.gson.JsonSyntaxException
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibNetWorkException
import com.kangraoo.basektlib.tools.HNetwork
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.rx.RxTransformerHelper
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DataResult<out R> {

    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>() {
        init {
            ULog.e(exception, exception.message)
//            if(SApplication.instance().sConfiger.debugStatic){
//                TipToast.tip(Tip.Error,exception.message.toString())
//            }
        }
    }
    object Loading : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val DataResult<*>.succeeded
    get() = this is DataResult.Success && data != null

val networkMsg: Int = R.string.libNetFailCheck
val networkNoMsg: Int = R.string.libNetNotNetCheck
val networkTimeOutMsg: Int = R.string.libNetTimeOutCheck
val networkErrorMsg: Int = R.string.libNetErrorCheck
val networkSuccessMsg: Int = R.string.libNetSuccessCheck
val networkJsonErrorMsg: Int = R.string.libNetJsonErrorCheck
val networkUnkonwnHostErrorMsg: Int = R.string.libNetUnknownHostCheck

fun DataResult.Error.netError(): DataResult.Error {
    if (exception is ConnectException) {
        TipToast.tip(
            Tip.Error,
            RxTransformerHelper.networkMsg
        )
    } else if (exception is SocketTimeoutException) {
        TipToast.tip(
            Tip.Error,
            RxTransformerHelper.networkTimeOutMsg
        )
    } else if (exception is JsonSyntaxException) {
        TipToast.tip(
            Tip.Error,
            RxTransformerHelper.networkJsonErrorMsg
        )
    } else if (exception is HttpException) {
        val code = (exception as HttpException).code()
        val msg: String = HString.concatObject(
            " ",
            SApplication.context().getString(networkErrorMsg),
            "code:",
            code
        )
        TipToast.tip(Tip.Error, msg)
    } else if (exception is UnknownHostException) {
        if (HNetwork.checkNetwork(SApplication.context())) {
            TipToast.tip(
                Tip.Error,
                RxTransformerHelper.networkUnkonwnHostErrorMsg
            )
        } else {
            TipToast.tip(
                Tip.Error,
                RxTransformerHelper.networkNoMsg
            )
        }
    } else if (exception is LibNetWorkException) {
        if (exception.code == 504) {
            TipToast.tip(
                Tip.Error,
                RxTransformerHelper.networkNoMsg
            )
        } else {
            val msg: String = HString.concatObject(
                " ",
                SApplication.context().getString(networkErrorMsg),
                "code:",
                exception.code,
                exception.message
            )
            TipToast.tip(Tip.Error, msg)
        }
    } else {
        if(SApplication.instance().sConfiger.debugStatic){
            TipToast.tip(Tip.Error,exception.message.toString())
        }
    }
    return this
}

val DataResult<*>.netLibError
    get() = this is DataResult.Error && (this.exception is ConnectException || this.exception is SocketTimeoutException || this.exception is JsonSyntaxException || this.exception is HttpException || this.exception is UnknownHostException || this.exception is LibNetWorkException)
