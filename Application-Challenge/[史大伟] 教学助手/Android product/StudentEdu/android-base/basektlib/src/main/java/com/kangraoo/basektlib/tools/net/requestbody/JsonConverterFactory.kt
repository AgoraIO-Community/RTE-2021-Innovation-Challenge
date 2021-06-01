package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import java.lang.reflect.Type
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit

class JsonConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type !is Class<*>) {
            return null
        }
        if (!BParam::class.java.isAssignableFrom(type)) {
            return null
        }
        return JsonRequestBodyConverter()
    }

    companion object {
        fun create(): JsonConverterFactory {
            return JsonConverterFactory()
        }
    }
}
