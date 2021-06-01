package com.kangraoo.basektlib.tools.net.requestbody

import com.kangraoo.basektlib.data.model.BParam
import java.lang.reflect.Type
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit

class LibConverterFactory : Converter.Factory() {

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        if (type !is Class<*>) {
            return null
        }
        if (!BParam::class.java.isAssignableFrom(type)) {
            return null
        }
        return FormRequestBodyConverter()
    }

//    override fun stringConverter(
//        type: Type,
//        annotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): Converter<*, String>? {
//        if (type !is Class<*>) {
//            return null
//        }
//        if (!BParam::class.java.isAssignableFrom(type)) {
//            return null
//        }
//
//        return StringConverter()
//    }

    companion object {
        fun create(): LibConverterFactory {
            return LibConverterFactory()
        }
    }
}
