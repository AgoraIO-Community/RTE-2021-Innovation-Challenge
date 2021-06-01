package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.net.NetMap
import retrofit2.Converter

@Deprecated("无法转义对象")
class StringConverter : Converter<BParam, String> {
    override fun convert(value: BParam): String? {
        var map: Map<String, Any> = NetMap.paramDataToMapNoThrows(value)
        map = NetMap.httpBuildQueryMap(map)
        return HString.getNetString(map)
    }
}
