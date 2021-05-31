package com.qdedu.baselibcommon.data.source

import android.content.Context
import com.kangraoo.basektlib.data.model.toNetMap
import com.qdedu.baselibcommon.arouter.BaseNavigator
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.data.model.httpparams.ReadDetailHttpParam
import com.qdedu.baselibcommon.data.model.params.UploadUrlParams
import com.qdedu.baselibcommon.data.service.HttpMethods

class LibHttpCommonRepository {
    companion object{
        val instance: LibHttpCommonRepository by lazy {
            LibHttpCommonRepository()
        }
    }
    /**
     * 跳转阅读详情
     */
    fun readDetail(context: Context,param: ReadDetailHttpParam){
        var map = param.toNetMap()
        BaseNavigator.navigationWebPage(context, HttpMethods.ROUTE_READING_DETAIL,"",map)
    }
}