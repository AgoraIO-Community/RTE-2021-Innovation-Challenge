package com.kangraoo.basektlib.tools.okhttp

import com.kangraoo.basektlib.data.model.SExpModel
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.MemoryStore
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URL

/**
 * okhttp 重试次数
 */
class RetryIntercepter(private var maxRetry: Int) : Interceptor {
// 假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var path = URL(request.url.toString()).path + "_exp"
        var retryNum = MemoryStore.instance.get(path, 0)!!
        ULog.d("当前请求次数=$retryNum")
        var response = chain.proceed(request)
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum = MemoryStore.instance.get(path, 0)!! + 1
            MemoryStore.instance.put(path,SExpModel(retryNum,300000))
            response.close()
            ULog.d("正在重试次数=$retryNum")
            response = chain.proceed(request)
        }
        return response
    }
}
