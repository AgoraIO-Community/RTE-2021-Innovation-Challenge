package io.agora.education.impl.network

import com.google.gson.Gson
import io.agora.base.callback.Callback
import io.agora.base.callback.ThrowableCallback
import io.agora.base.network.BusinessException
import io.agora.base.network.ResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

internal class RetrofitManager private constructor() {
    private val client: OkHttpClient
    private val headers: MutableMap<String, String>? = HashMap()
    private var logger: HttpLoggingInterceptor.Logger? = null
    fun addHeader(key: String, value: String) {
        headers!![key] = value
    }

    fun setLogger(logger: HttpLoggingInterceptor.Logger) {
        this.logger = logger
    }

    fun <T> getService(baseUrl: String, tClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(tClass)
    }

    class Callback<T : ResponseBody<*>?>(private val code: Int, private val callback: io.agora.base.callback.Callback<T>) : retrofit2.Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.errorBody() != null) {
                try {
                    val errorBodyStr = String(response.errorBody()!!.bytes())
                    val errorBody = Gson().fromJson(errorBodyStr, ResponseBody::class.java)
                    if (errorBody == null) {
                        throwableCallback(Throwable(response.errorBody()!!.string()))
                    } else {
                        throwableCallback(BusinessException(errorBody.code, errorBody.msg.toString()))
                    }
                } catch (e: IOException) {
                    throwableCallback(e)
                }
            } else {
                val body = response.body()
                if (body == null) {
                    throwableCallback(Throwable("response body is null"))
                } else {
                    if (body.code != code) {
                        throwableCallback(BusinessException(body.code, body.msg.toString()))
                    } else {
                        callback.onSuccess(body)
                    }
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            throwableCallback(t)
        }

        private fun throwableCallback(throwable: Throwable) {
            if (callback is ThrowableCallback<*>) {
                (callback as ThrowableCallback<T>).onFailure(throwable)
            }
        }

    }

    companion object {
        private var instance: RetrofitManager? = null
        fun instance(): RetrofitManager? {
            if (instance == null) {
                synchronized(RetrofitManager::class.java) {
                    if (instance == null) {
                        instance = RetrofitManager()
                    }
                }
            }
            return instance
        }
    }

    init {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                    .method(request.method, request.body)
            if (headers != null) {
                for ((key, value) in headers) {
                    requestBuilder.addHeader(key, value)
                }
            }
            chain.proceed(requestBuilder.build())
        })
        clientBuilder.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (logger == null) {
                    Platform.get().log(message, Platform.INFO, null)
                } else {
                    logger!!.log(message)
                }
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY))
        client = clientBuilder.build()
    }
}