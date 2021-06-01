package com.qdedu.baselibcommon.data.source.remote

import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.model.toNetMap
import com.kangraoo.basektlib.data.source.remote.BaseRemoteDataSource
import com.qdedu.baselibcommon.data.AppHuanJingFactory
import com.kangraoo.basektlib.exception.LibNetWorkException
import com.qdedu.baselibcommon.data.source.ApiSource
import com.qdedu.baselibcommon.data.source.LibCommenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.qdedu.baselibcommon.data.model.params.*
import com.qdedu.baselibcommon.data.model.responses.*
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import com.qdedu.baselibcommon.data.netSuccess
import com.kangraoo.basektlib.data.netError
import com.kangraoo.basektlib.tools.net.HttpPart
import com.kangraoo.basektlib.tools.net.UploadProgressListener
import com.qdedu.baselibcommon.data.source.LibCommonService

/**
 * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
 * LibCommenRemoteDataSource
 */
 class LibCommenRemoteDataSource internal constructor(
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
 ) : BaseRemoteDataSource(), LibCommenDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2020-11-24 15:07.
     * #uploadUrl#
     * #获取文件上传url#
     */
    override suspend fun uploadUrl(param: UploadUrlParams)= withContext(mainDispatcher) {
        try {
            val data = LibCommonService.getApiService(AppHuanJingFactory.appModel.apiDomains).uploadUrlAsync(param.toNetMap())
            if(data.isSuccessful) {
                return@withContext DataResult.Success(data.body()!!).netSuccess()
            }else{
                return@withContext DataResult.Error(LibNetWorkException(data.code(),data.message())).netError()
            }
        } catch (e: Exception) {
            return@withContext DataResult.Error(e).netError()
        }
    }

    override suspend fun uploadFile(
        uploadUrl: String,
        filePath: String,
        listener: UploadProgressListener?
    ) = withContext(mainDispatcher) {
        try {
            val data = LibCommonService.getApiService(AppHuanJingFactory.appModel.apiDomains).uploadFileAsync(uploadUrl,
                HttpPart.getSimplePart(filePath, "file",listener))
            if(data.isSuccessful) {
                return@withContext DataResult.Success(data.body()!!).netSuccess()
            }else{
                return@withContext DataResult.Error(LibNetWorkException(data.code(),data.message())).netError()
            }
        } catch (e: Exception) {
            return@withContext DataResult.Error(e).netError()
        }
    }
//#06#
 }
