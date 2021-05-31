package com.kangraoo.basektlib.tools.okhttp

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.kangaroo.simpleinterceptor.SimpleInterceptor
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.net.OkHttpDns
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.File
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.OkHttpClient

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/07/29
 * desc :
 */
class UOkHttp private constructor() {
    companion object {
        val instance: UOkHttp by lazy {
            UOkHttp()
        }
    }
    private val cookieJar= PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(SApplication.context())
    )
    var config = SApplication.instance().sConfiger.okHttpConfig
    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.connectTimeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(config.writeTimeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(config.readTimeout.toLong(), TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .dns(OkHttpDns.instance)
        .build()
//        .addInterceptor(RetryIntercepter(config.maxRetry))
// 缓存大小为10M
    var cache = Cache(File(UStorage.getDirectoryByDirType(StorageType.TYPE_CACHE)), 10 * 1024 * 1024)

    fun defaultOkhttp() = okHttpClient.newBuilder()
            .cache(cache)
            .addInterceptor(MockInterceptor())
            .addInterceptor(LibInterceptor(true))
            .addInterceptor(RetryIntercepter(config.maxRetry))
        .addInterceptor(
            SimpleInterceptor(
                SApplication.context()
            )
        )
            .build()

    fun imageOkhttp() = okHttpClient.newBuilder()
        .addNetworkInterceptor(LibInterceptor())
        .build()
}
