package com.qdedu.baselibcommon.data.source

import com.qdedu.baselibcommon.data.service.ApiService

object LibCommonService {
    fun getApiService(mApi: String): ApiService {
        return ApiSource.instance(mApi).getApiRetrofit.create(ApiService::class.java)
    }
}