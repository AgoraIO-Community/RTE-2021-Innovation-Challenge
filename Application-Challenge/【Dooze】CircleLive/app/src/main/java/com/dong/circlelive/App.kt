package com.dong.circlelive

import android.app.Application
import cn.leancloud.AVOSCloud
import com.dong.circlelive.base.MapDeserializer
import com.dong.circlelive.base.Timber
import com.dong.circlelive.base.lazyFast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import androidx.multidex.MultiDex
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * Create by dooze on 5/3/21  7:06 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class App : Application() {


    val gson: Gson by lazy {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(
            object : TypeToken<Map<String, String>>() {}.type,
            MapDeserializer()
        )
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }

    val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl("https://dooze.applinzi.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        Timber.plant(Timber.DebugTree())
        MMKV.initialize(this)
        IM.init(this)
        Live.init(this)
        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        AVOSCloud.initialize(this, "bzfullqEzVbJrcPur7fflyAc-gzGzoHsz", "72dDxPXS4yh3aF8GMSEkf57G", "https://bzfullqe.lc-cn-n1-shared.com");
    }

    companion object {
        lateinit var app: App
    }
}

val appContext by lazyFast { App.app }