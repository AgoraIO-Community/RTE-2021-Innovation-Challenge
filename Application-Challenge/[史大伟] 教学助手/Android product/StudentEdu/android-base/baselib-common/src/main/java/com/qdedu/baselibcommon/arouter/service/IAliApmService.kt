package com.qdedu.baselibcommon.arouter.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author shidawei
 * 创建日期：2021/3/1
 * 描述：
 */
interface IAliApmService : IProvider {

    /**
     *  配置项：自定义环境信息
     */
    fun addCustomInfo(key: String, value: String)

    fun reportCustomError(exception: Exception)

    fun setErrorCallback(aliErrorCallback: AliErrorCallback)

    fun getLog() : IAliLog
}

interface AliErrorCallback {
    fun onError(errorType:Int,throwable: Throwable): Map<String, String>
}

interface IAliLog{
    fun v(model : String,tag : String,message : String)
    fun d(model : String,tag : String,message : String)
    fun i(model : String,tag : String,message : String)
    fun w(model : String,tag : String,message : String)
    fun e(model : String,tag : String,message : String)
}