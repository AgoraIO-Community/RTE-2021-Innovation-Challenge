package com.kangraoo.basektlib.tools.net

import com.kangraoo.basektlib.tools.store.file.FileUtil
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

object HttpPart {

    const val IMAGETYPE = "image/jpeg"
    const val VIDEOTYPE = "video/mp4"
    const val AUDIOTYPE = "audio/wav"

    /**
     * 获取图片part，支持进度反馈
     *
     * @param filepath 文件路径
     * @param paramName 参数名
     * @param listener 进度回调
     * @return
     */
    fun getImagePart(
        filepath: String,
        paramName: String,
        listener: UploadProgressListener? = null
    ): MultipartBody.Part {
        return getPart(IMAGETYPE, filepath, paramName, listener)
    }

    /**
     * 获取视频part，支持进度反馈
     *
     * @param filepath 文件路径
     * @param paramName 参数名
     * @param listener 进度回调
     * @return
     */
    fun getVideoPart(
        filepath: String,
        paramName: String,
        listener: UploadProgressListener? = null
    ): MultipartBody.Part {
        return getPart(VIDEOTYPE, filepath, paramName, listener)
    }

    /**
     * 获取音频part
     *
     * @param filepath 文件路径
     * @param paramName 参数名
     * @return
     */
    fun getAudioPart(
        filepath: String,
        paramName: String,
        listener: UploadProgressListener? = null
    ): MultipartBody.Part {
        return getPart(AUDIOTYPE, filepath, paramName, listener)
    }

    /**
     * 通用获取part方法
     *
     * @param mediaType 文件类型
     * @param filepath 文件路径
     * @param paramName 参数名
     * @param listener 进度回调
     * @return
     */
    private fun getPart(
        mediaType: String,
        filepath: String,
        paramName: String,
        listener: UploadProgressListener? = null
    ): MultipartBody.Part {
        val file = File(filepath)
        val requestBody = file.asRequestBody(mediaType.toMediaTypeOrNull())
        return if (listener != null) {
            val progressRequestBody = ProgressRequestBody(requestBody, listener)
            MultipartBody.Part.createFormData(paramName, file.name, progressRequestBody)
        } else {
            MultipartBody.Part.createFormData(paramName, file.name, requestBody)
        }
    }

    /**
     * 简单通过文件来获取 MultipartBody.Part性能差
     */
    public fun getSimplePart(
        filepath: String,
        paramName: String,
        listener: UploadProgressListener? = null
    ): MultipartBody.Part {
        return getPart(FileUtil.getMimeType(filepath), filepath, paramName, listener)
    }
}
