package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import com.kangraoo.basektlib.tools.json.HJson
import java.io.OutputStreamWriter
import java.io.Writer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.Buffer

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/03/19
 * desc :
 * version: 1.0
 */
class JsonRequestBodyConverter : BaseRequestBodyConverter() {
    private val mediaType = "application/json; charset=UTF-8".toMediaTypeOrNull()

    override fun convert(value: BParam): RequestBody? {
        val paramsMap: Map<*, *> = getParamsMap(value, false)
        return RequestBody.create(mediaType, Buffer().use {
            val writer: Writer = OutputStreamWriter(it.outputStream(), utf8)
            var json = HJson.toJson(paramsMap)
            writer.use { wit ->
                wit.write(json)
            }
            it
        }.readByteString())
    }
}
