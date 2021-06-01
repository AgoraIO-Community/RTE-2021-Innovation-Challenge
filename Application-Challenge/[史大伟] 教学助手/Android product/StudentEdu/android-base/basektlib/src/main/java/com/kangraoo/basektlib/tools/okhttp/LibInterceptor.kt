package com.kangraoo.basektlib.tools.okhttp

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HNetwork
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.net.NetMap
import com.kangraoo.basektlib.tools.net.ProgressRequestBody
import okhttp3.*
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/07/30
 * desc : 框架拦截类
 */
class LibInterceptor(private val outJson: Boolean = false) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        val headerMap = NetMap.apiNetMap()
        var urlPersitents = HttpPersistentManager.instance.getPersistent(request.url.host,
            URL_PERSISTENT)
        var headerPersitents = HttpPersistentManager.instance.getPersistent(request.url.host,
            HEADER_PERSISTENT)
        for ((key, value) in headerMap) {
            builder.addHeader(key, value)
        }
        if (headerPersitents != null) {
            for ((key, value) in headerPersitents) {
                if (value != null) {
                    builder.addHeader(key, value)
                }
            }
        }
        if (urlPersitents != null) {
            val httpUrl = request.url.newBuilder()
            for ((key, value) in urlPersitents) {
                httpUrl.setEncodedQueryParameter(key, value)
            }
            builder.url(httpUrl.build())
        }

        if (!HNetwork.checkNetwork(SApplication.context())) {
            // 无网络情况下改用 缓存
            builder.cacheControl(CacheControl.FORCE_CACHE)
            ULog.d("无网络切换为缓存")
            return chain.proceed(builder.build())
        }

        request = builder.build()
        return if (SApplication.instance().sConfiger.debugStatic) {
            val requestSb = StringBuilder()
            val requestBody = request.body
            val hasRequestBody = requestBody != null
            val connection = chain.connection()
            requestSb.append(SApplication.context().getString(R.string.libNetSend))
                .append("\n")
                .append(request.method).append(" ")
                .append(request.url).append(" ")
                .append(if (connection != null) connection.protocol() else "").append(" ")
            if (hasRequestBody) {
                requestSb.append("(").append(requestBody!!.contentLength()).append("-byte body)")
                    .append("\n")
            }
            if (hasRequestBody) {
                if (requestBody!!.contentType() != null) {
                    requestSb.append("Content-Type: ").append(requestBody.contentType())
                        .append("\n")
                }
                if (requestBody.contentLength() != -1L) {
                    requestSb.append("Content-Length: ").append(requestBody.contentLength())
                        .append("\n")
                }
            }
            val headers = request.headers
            run {
                var i = 0
                val count = headers.size
                while (i < count) {
                    val name = headers.name(i)
                    if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(
                            name,
                            ignoreCase = true
                        )
                    ) {
                        requestSb.append(name).append(": ").append(headers.value(i)).append("\n")
                    }
                    i++
                }
            }

            if (!hasRequestBody) {
                ULog.i(requestSb.toString())
            } else if (bodyHasUnknownEncoding(request.headers)) {
                ULog.i(requestSb.append("(encoded body omitted)").toString())
            } else if (requestBody!!.isDuplex()) {
                ULog.i(requestSb.append("(duplex request body omitted)").toString())
            } else if (requestBody.isOneShot()) {
                ULog.i(requestSb.append("(one-shot body omitted)").toString())
            } else if (requestBody is MultipartBody){
                ULog.i(requestSb.append("(requestBody is MultipartBody)").toString())
            } else {

                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (buffer.isProbablyUtf8()) {
                    val str = buffer.readString(charset)
                    ULog.i(requestSb.append(str).append("\n").append("(${requestBody.contentLength()}-byte body)").toString())
                    if (outJson) {
                        try {
                            var any: Any? = HJson.fromJson(str, Any::class.java)
                            ULog.json(str)
                        } catch (e: Exception) {
                            ULog.i(str)
                            // ULog.n().t(TAG).e(SApplication.getInstance().getString(R.string.libBackNotJson));
                        }
                    }
                } else {
                    ULog.i(
                        requestSb.append("(binary").append(
                            requestBody.contentLength()
                        ).append("-byte body omitted)").toString()
                    )
                }
            }

            val startNs = System.nanoTime()
            val response: Response
            response = try {
                chain.proceed(request)
            } catch (e: Exception) {
                ULog.e(e, "HTTP FAILED:", e.message)
                throw e
            }

            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            val responseBody = response.body
            val contentLength = responseBody!!.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            val responseSb = StringBuilder()
            responseSb.append(SApplication.context().getString(R.string.libNetGet))
                .append("\n")
                .append(response.code).append(" ")
                .append(
                    if (response.message.isEmpty()) "" else ' '.toString() + response.message
                ).append(" ")
                .append(response.request.url).append(" ")
                .append(" (" + tookMs + "ms" + bodySize + " body" + ')').append("\n")

            val responseheaders = response.headers
            var i = 0
            val count = responseheaders.size
            while (i < count) {
                responseSb.append(responseheaders.name(i)).append(": ")
                    .append(responseheaders.value(i)).append("\n")
                i++
            }
            if (!response.promisesBody()) {
                ULog.i(responseSb.toString())
            } else if (bodyHasUnknownEncoding(response.headers)) {
                ULog.i(responseSb.append("(encoded body omitted)").toString())
            } else {

                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer
                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (!buffer.isProbablyUtf8()) {
                    ULog.i(
                        responseSb.append("(binary ").append(buffer.size)
                            .append("-byte body omitted)").toString()
                    )
                    return response
                }

                if (contentLength != 0L) {
                    val str = buffer.clone().readString(charset)
                    responseSb.append(str).append("\n")
                    if (outJson) {
                        try {
                            var any: Any? = HJson.fromJson(str, Any::class.java)
                            ULog.json(str)
                        } catch (e: Exception) {
                            ULog
                                .e(SApplication.instance().getString(R.string.libBackNotJson))
                        }
                    }
                }

                if (gzippedLength != null) {

                    ULog.i(responseSb.append("(${buffer.size}-byte, $gzippedLength-gzipped-byte body)").toString())
                } else {
                    ULog.i(
                        responseSb.append("(").append(buffer.size).append("-byte body)").toString()
                    )
                }
            }
            response
        } else {
            chain.proceed(request)
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}

internal fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size.coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}
