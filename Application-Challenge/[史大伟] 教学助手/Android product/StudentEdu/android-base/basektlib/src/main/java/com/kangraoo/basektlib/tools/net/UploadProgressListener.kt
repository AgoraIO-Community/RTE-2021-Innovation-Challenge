package com.kangraoo.basektlib.tools.net

/**
 * desc :上传进度回调类
 * author：liuli
 * date：2020/06/09
 */
interface UploadProgressListener {
    /**
     * 上传进度
     *
     * @param currentBytesCount
     * @param totalBytesCount
     */
    fun onProgress(currentBytesCount: Long, totalBytesCount: Long)
}
