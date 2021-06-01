package com.kangraoo.basektlib.tools.net

import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * desc :自定义回调加载速度类RequestBody
 * author：Liu li
 * date：2020/06/09
 */
class ProgressRequestBody(
    // 实际的待包装请求体
    private val requestBody: RequestBody,
    // 进度回调接口
    private val progressListener: UploadProgressListener
) : RequestBody() {

    // 包装完成的BufferedSink
    private var bufferedSink: BufferedSink? = null

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    override fun contentType() = requestBody.contentType()

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun contentLength() = requestBody.contentLength()

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (null == bufferedSink) {
            bufferedSink = sink(sink).buffer()
        }
        requestBody.writeTo(bufferedSink!!)
        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()
    }
    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink) = object : ForwardingSink(sink) {
            // 当前写入字节数
            var writtenBytesCount = 0L

//            // 总字节长度，避免多次调用contentLength()方法
//            var totalBytesCount = 0L

            val totalLength = contentLength()

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                // 增加当前写入的字节数
                writtenBytesCount += byteCount
//                // 获得contentLength的值，后续不再调用
//                if (totalBytesCount == 0L) {
//                    totalBytesCount = contentLength()
//                }
                progressListener.onProgress(writtenBytesCount, totalLength)
            }
        }
}
