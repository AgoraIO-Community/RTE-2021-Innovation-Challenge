package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.net.NetMap
import java.nio.charset.Charset
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/03/19
 * desc :
 * version: 1.0
 */
abstract class BaseRequestBodyConverter : Converter<BParam, RequestBody> {

    protected val utf8 = Charset.forName("UTF-8")
    fun getParamsMap(value: BParam, buildMap: Boolean): Map<String, Any> {
        var map = NetMap.paramDataToMapNoThrows(value)
        if (buildMap) {
            map = NetMap.httpBuildQueryMap(map)
        }
        ULog.o(map)
        return map
    }
}
