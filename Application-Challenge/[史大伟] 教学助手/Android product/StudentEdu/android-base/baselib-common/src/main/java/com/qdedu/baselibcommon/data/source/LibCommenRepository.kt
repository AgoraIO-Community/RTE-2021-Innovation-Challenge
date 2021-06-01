package com.qdedu.baselibcommon.data.source

import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.source.BaseRepository
import com.kangraoo.basektlib.tools.net.UploadProgressListener
import com.qdedu.baselibcommon.data.source.local.LibCommenLocalDataSource
import com.qdedu.baselibcommon.data.source.remote.LibCommenRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.qdedu.baselibcommon.data.model.params.*
import com.qdedu.baselibcommon.data.model.responses.*

/**
 * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
 * LibCommenRepository
 */
class LibCommenRepository : BaseRepository<LibCommenLocalDataSource,LibCommenRemoteDataSource>(LibCommenLocalDataSource(),LibCommenRemoteDataSource()),LibCommenDataSource {

    companion object{
        val instance: LibCommenRepository by lazy {
            LibCommenRepository()
        }
    }
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


    /**
     * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
     * #uploadUrl#
     * #获取文件上传url#
     */
    override suspend fun uploadUrl( param: UploadUrlParams) = remoteDataSource.uploadUrl(param)
    override suspend fun uploadFile(
        uploadUrl: String,
        filePath: String,
        listener: UploadProgressListener?
    ) = remoteDataSource.uploadFile(uploadUrl, filePath,listener)


//#06#
}
