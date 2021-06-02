package com.kangraoo.basektlib.tools

// import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibNetWorkException
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.net.requestbody.JsonConverterFactory
import com.kangraoo.basektlib.tools.net.requestbody.LibConverterFactory
import com.kangraoo.basektlib.tools.okhttp.UOkHttp
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.SSLException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class HRetrofit(baseUrl: String, factory: List<Converter.Factory>) {
    var retrofit: Retrofit

    init {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
        for (cf in factory) {
            builder.addConverterFactory(cf)
        }
        retrofit = builder
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(HJson.uMosh.moshiBuild))
            .client(UOkHttp.instance.defaultOkhttp())
            .build()
    }

    companion object {
        private val hRetrofitMap: MutableMap<String, HRetrofit> = HashMap()
        fun instance(baseUrl: String, factory: List<Converter.Factory>): HRetrofit {
            if (!hRetrofitMap.containsKey(baseUrl)) {
                synchronized(hRetrofitMap) {
                    if (!hRetrofitMap.containsKey(baseUrl)) {
                        hRetrofitMap[baseUrl] = HRetrofit(baseUrl, factory)
                    }
                }
            }
            return hRetrofitMap[baseUrl]!!
        }

        fun getDefaultFromRetrofit(baseUrl: String): HRetrofit {
            val factory: MutableList<Converter.Factory> = ArrayList()
            factory.add(ScalarsConverterFactory.create())
            factory.add(LibConverterFactory.create())
            return instance(baseUrl, factory)
        }

        fun getDefaultJsonRetrofit(baseUrl: String): HRetrofit {
            val factory: MutableList<Converter.Factory> = ArrayList()
            factory.add(ScalarsConverterFactory.create())
            factory.add(JsonConverterFactory.create())
            return HRetrofit.instance(baseUrl, factory)
        }
    }
}

suspend fun <T> Call<T>.subscribe(
    scope: CoroutineScope
): T? {
    return try {
        withContext(Dispatchers.IO) {
            val result = execute().body()
            // 检查协程是否取消
            if (isActive) {
                result
            } else null
        }
    } catch (e: IOException) {
        // 检查协程是否取消
        if (scope.isActive) {
            ULog.e("URL ", request().url.toString())
            errorHandle(e)
        }
        throw e
    }
}

val networkMsg: Int = R.string.libNetFailCheck
val networkNoMsg: Int = R.string.libNetNotNetCheck
val networkTimeOutMsg: Int = R.string.libNetTimeOutCheck
val networkErrorMsg: Int = R.string.libNetErrorCheck
val networkSuccessMsg: Int = R.string.libNetSuccessCheck
val networkJsonErrorMsg: Int = R.string.libNetJsonErrorCheck
val networkUnkonwnHostErrorMsg: Int = R.string.libNetUnknownHostCheck
val socketClose: Int = R.string.libSocketCloseCheck
val sslError: Int = R.string.libSslErrorCheck
private fun errorHandle(e: Exception) {
    when (e) {
        is HttpException -> {
            defaultError(e.code(), networkErrorMsg)
        }
        is UnknownHostException -> {
            defaultError(-1, networkUnkonwnHostErrorMsg)
        }
        is SocketTimeoutException -> {
            defaultError(-1, networkTimeOutMsg)
        }
        is ConnectException -> {
            defaultError(-1, networkMsg)
        }
        is SocketException -> {
            defaultError(-1, socketClose)
        }
        is EOFException -> {
            defaultError(-1, socketClose)
        }
        is SSLException -> {
            defaultError(-1, sslError)
        }
        else -> {
            throw e
        }
    }
}

private val defaultError = fun(code: Int, msg: Int) {
    val message = SApplication.context().resources.getString(msg)
    ULog.e(code, message)
    throw LibNetWorkException(code, message)
}
