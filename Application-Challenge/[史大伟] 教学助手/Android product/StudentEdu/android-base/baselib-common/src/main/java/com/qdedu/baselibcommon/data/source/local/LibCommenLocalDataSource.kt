package com.qdedu.baselibcommon.data.source.local

import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.source.local.BaseLocalDataSource
import com.kangraoo.basektlib.tools.net.UploadProgressListener
import com.qdedu.baselibcommon.data.source.LibCommenDataSource
import com.qdedu.baselibcommon.data.model.params.*
import com.qdedu.baselibcommon.data.model.responses.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
 * LibCommenLocalDataSource
 */
public class LibCommenLocalDataSource internal constructor(
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseLocalDataSource(), LibCommenDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
     * #uploadUrl#
     * #获取文件上传url#
     */
    override suspend fun uploadUrl(param: UploadUrlParams): DataResult<BasicApiResult<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadFile(
        uploadUrl: String,
        filePath: String,
        listener: UploadProgressListener?
    ): DataResult<List<ServerUploadResultResponses>> {
        TODO("Not yet implemented")
    }
//#06#
}
