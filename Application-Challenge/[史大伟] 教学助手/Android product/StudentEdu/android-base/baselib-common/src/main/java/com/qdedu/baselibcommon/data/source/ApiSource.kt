package com.qdedu.baselibcommon.data.source

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HRetrofit
import com.kangraoo.basektlib.tools.SSystem
import retrofit2.Retrofit
import kotlin.collections.HashMap

class ApiSource private constructor(var mApi: String) {

    companion object{
        private val apiSources:MutableMap<String, ApiSource> by lazy {
            HashMap<String, ApiSource>()
        }

        fun instance(api: String): ApiSource {
            if (!apiSources.containsKey(api)) {
                synchronized(ApiSource::class.java) {
                    if (!apiSources.containsKey(api)) {
                        apiSources[api] =
                            ApiSource(api)
                    }
                }
            }
            return apiSources[api]!!
        }
    }

    val getApiRetrofit :Retrofit by lazy {
        HRetrofit.getDefaultJsonRetrofit(mApi).retrofit
    }

    val getApiFromRetrofit :Retrofit by lazy {
        HRetrofit.getDefaultFromRetrofit(mApi).retrofit
    }
}