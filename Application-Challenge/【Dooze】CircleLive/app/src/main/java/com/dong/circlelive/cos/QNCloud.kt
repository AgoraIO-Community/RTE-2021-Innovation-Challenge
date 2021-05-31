package com.dong.circlelive.cos

import cn.leancloud.AVUser
import com.dong.circlelive.base.Timber
import com.qiniu.android.common.AutoZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File


/**
 * Create by dooze on 2021/5/23  11:10 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class QNCloud {

    companion object {
        const val BUCKET = "dooze-live"

        val uploadManager by lazy {
            val config: Configuration = Configuration.Builder()
                .connectTimeout(90) // 链接超时。默认90秒
                .useHttps(true) // 是否使用https上传域名
                .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
                .concurrentTaskCount(3) // 并发上传线程数量为3
                .responseTimeout(90) // 服务器响应超时。默认90秒
//                .recorder(recorder) // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen) // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(AutoZone()) // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build()
            // 重用uploadManager。一般地，只需要创建一个uploadManager对象
            UploadManager(config)
        }
    }

}

suspend fun QNCloud.Companion.upload(file: File): String? {
    val token = QiniuAuth.getToken(BUCKET)?.result ?: return null
    val key = "${AVUser.currentUser().objectId}_${file.name}"
    return suspendCancellableCoroutine { ucot ->
        uploadManager.put(
            file, key, token, { k, info, response ->
                Timber.d { "upload $k $info  $response" }
                if (info.isOK) {
                    ucot.resume("https://${info.host}/$key", null)

                } else {
                    ucot.resume(null, null)
                }
            },
            null
        )
    }
}

suspend fun QNCloud.Companion.upload(bytes: ByteArray, objKey: String): String? {
    val token = QiniuAuth.getToken(BUCKET)?.result ?: run {
        Timber.e { "can't get qiniu token" }
        return null
    }
    val key = "${AVUser.currentUser().objectId}_${objKey}"
    return suspendCancellableCoroutine { ucot ->
        uploadManager.put(
            bytes, key, token, { k, info, response ->
                Timber.d { "upload $k $info  $response" }
                if (info.isOK) {
                    ucot.resume("https://${info.host}/$key", null)

                } else {
                    ucot.resume(null, null)
                }
            },
            null
        )
    }

}