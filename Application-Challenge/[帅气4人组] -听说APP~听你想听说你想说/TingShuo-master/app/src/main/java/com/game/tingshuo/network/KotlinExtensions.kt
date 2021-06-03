package com.game.tingshuo.network

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.JsonSyntaxException
import com.orhanobut.logger.Logger
import com.game.tingshuo.R
import com.game.tingshuo.app.App
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

fun Throwable.errorCode(): Int {
    val errorCode = when (this) {
        is HttpStatusCodeException -> {//请求失败异常
            this.statusCode
        }
        is ParseException -> {  // ParseException异常表明请求成功，但是数据不正确
            this.errorCode
        }
        else -> {
            "-1"
        }
    }
    return try {
        errorCode.toInt()
    } catch (e: Exception) {
        -1
    }
}

fun Throwable.errorMsg(): String? {
    var errorMsg = handleNetworkException(this)  //网络异常
    if (this is HttpStatusCodeException) {               //请求失败异常
        val code = this.statusCode
        if ("416" == code) {
            errorMsg = "请求范围不符合要求"
        }
    } else if (this is JsonSyntaxException) {  //请求成功，但Json语法异常,导致解析失败
        errorMsg = "数据解析失败,请稍后再试"
    } else if (this is ParseException) {       // ParseException异常表明请求成功，但是数据不正确
        errorMsg = this.message ?: errorCode   //errorMsg为空，显示errorCode
    }
    return errorMsg
}

//处理网络异常
fun <T> handleNetworkException(throwable: T): String? {
    var exceptionData = ""
    val stringId =
        if (throwable is UnknownHostException) { //网络异常
            if (!isNetworkConnected(App.getInstance())) R.string.network_error else R.string.notify_no_network
        } else if (throwable is SocketTimeoutException || throwable is TimeoutException) {
            R.string.time_out_please_try_again_later  //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
        } else if (throwable is ConnectException) {
            R.string.esky_service_exception  //连接异常
        } else if (throwable is SSLHandshakeException) {//本机时间不对
            R.string.network_local_time_exception
        } else if (throwable is Exception){
            Logger.e(throwable,"数据异常")
            exceptionData = if (throwable.message ==null) throwable.toString() else throwable.message.toString()
            R.string.network_unknown_exception
        } else {
            -1
        }
    var desc = String.format(App.getInstance().getString(stringId),exceptionData)
    return if (stringId == -1) null else desc
}

fun isNetworkConnected(context: Context): Boolean {
    val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mNetworkInfo = mConnectivityManager.activeNetworkInfo
    if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable
    }
    return false
}
