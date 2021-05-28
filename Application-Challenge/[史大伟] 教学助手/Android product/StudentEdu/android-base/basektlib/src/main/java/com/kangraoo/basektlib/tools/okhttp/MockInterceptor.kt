package com.kangraoo.basektlib.tools.okhttp

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.log.ULog
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

/**
 * 假数据代理
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (!SApplication.instance().sConfiger.debugStatic) {
            chain.proceed(request)
        } else {
            mockResult(chain, request) ?: saveResult(chain, request)
        }
    }

    val proxy = "content://com.qdedu.ProxyUri/*"

    private fun mockResult(chain: Interceptor.Chain, request: Request): Response? {
        return try {
            val url = request.url.toString()
            val resolver: ContentResolver = SApplication.context().contentResolver
            // 设置URI
            val uri: Uri = Uri.parse(proxy)
            // 通过ContentResolver 向ContentProvider中查询数据
            val cursor: Cursor? =
                resolver.query(uri, null, null, arrayOf(url), null)
            var json: String? = null
            if (cursor != null && cursor.moveToFirst()) {
                json = cursor.getString(0)
//                ULog.d("mock-json",json)
            }
            cursor?.close()

            return if (json != null) {

                val builder = Response.Builder()
                    .request(request)
                    .code(200)
                    .receivedResponseAtMillis(System.currentTimeMillis())
                    .sentRequestAtMillis(-1L)
                    .message("mock request succeed")
                    .protocol(Protocol.HTTP_1_1)

                val contentType = "application/json;charset=UTF-8".toMediaTypeOrNull()

//                val body = ResponseBody.create(mediaType,json.toByteArray())
                return builder.body(json.toByteArray().toResponseBody(contentType)).build()
            } else {
                null
            }
        } catch (e: Exception) {
            ULog.e(e.message, "未加载模拟数据，如果有配置请加载")
            null
        }
    }

    private fun saveResult(chain: Interceptor.Chain, request: Request): Response {

        val url = request.url.toString()
        val requestBody = request.body


        var postjson: String? = null
        if (requestBody != null) {
            if(requestBody is MultipartBody){
                return chain.proceed(request)
            }
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val contentType = requestBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
            if (buffer.isProbablyUtf8()) {
                postjson = buffer.readString(charset)
            }
        }

        val response = chain.proceed(request)
        val responseBody = response.body
        val contentType = responseBody!!.contentType()
        val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        val json = responseBody.source().buffer.clone().readString(charset)
        // 设置URI
        val uri: Uri = Uri.parse(proxy)
        val values = ContentValues()
        // 插入表中数据
        values.put("method", request.method)
        var token = HttpPersistentManager.instance.getPersistent(request.url.host,
            ALL_PERSISTENT)
        if (token != null) {
            values.put("token", token.toString())
        }
        if (postjson != null) {
            values.put("postjson", postjson)
        }
        values.put("apiName", url)
        values.put("json", json)
//        response.close()

        try {
            // 获取ContentResolver
            val resolver: ContentResolver = SApplication.context().contentResolver
            // 通过ContentResolver 根据URI 向ContentProvider中插入数据
            resolver.insert(uri, values)
        } catch (e: Exception) {
            ULog.e(e.message, "未存储模拟数据，如果有配置请加载")
        }
        return response
    }
}
