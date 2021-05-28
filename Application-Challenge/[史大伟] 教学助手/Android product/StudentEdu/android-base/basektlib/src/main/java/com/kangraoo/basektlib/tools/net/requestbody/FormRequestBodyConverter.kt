package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import java.net.URLEncoder
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class FormRequestBodyConverter : BaseRequestBodyConverter() {
    private val mediaType = "application/x-www-form-urlencoded; charset=utf-8".toMediaTypeOrNull()

    override fun convert(value: BParam): RequestBody? {
        val paramsMap = getParamsMap(value, true)
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return mediaType
            }

            override fun writeTo(sink: BufferedSink) {
                sink.use {
                    val keySet: Set<String> = paramsMap.keys
                    for ((index, key) in keySet.withIndex()) {
                        if (index > 0) {
                            it.buffer.writeByte('&'.toInt())
                        }
                        val encodeKey = URLEncoder.encode(key, "UTF-8")
                        it.buffer.writeUtf8(encodeKey)
                        it.buffer.writeByte('='.toInt())
                        val encodeValue = URLEncoder.encode(paramsMap[key] as String?, "UTF-8")
                        it.buffer.writeUtf8(encodeValue)
                    }
                }
            }
        }
    }
}
