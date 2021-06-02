package com.qdedu.baselibcommon.data.source

import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.tools.net.UploadProgressListener
import com.qdedu.baselibcommon.data.model.params.*
import com.qdedu.baselibcommon.data.model.responses.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult

/**
 * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
 * LibCommenDataSource
 */
interface LibCommenDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2020-11-24 15:07.
     * #uploadUrl#
     * #获取文件上传url#
     */
    suspend fun uploadUrl( param: UploadUrlParams): DataResult<BasicApiResult<String>>

    /**
     * 上传单文件
     *
     * @param uploadUrl
     * @param file
     * @return
     */
    suspend fun uploadFile(
        uploadUrl: String,
        filePath: String,
        listener: UploadProgressListener? = null
    ): DataResult<List<ServerUploadResultResponses>>


//#06#
}
