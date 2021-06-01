package com.qdedu.baselibcommon.data.service

import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import com.qdedu.baselibcommon.data.model.responses.ServerUploadResultResponses
import com.qdedu.baselibcommon.data.model.responses.UploadUrlResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {



    /**
     * 自动生成：by WaTaNaBe on 2020-12-23 13:15.
     * #uploadUrl#
     * #获取文件上传url#
     */
    @GET(ApiMethods.uploadUrl)
    suspend fun uploadUrlAsync(@QueryMap params:Map<String,String>): Response<BasicApiResult<String>>

    /**
     * 上传单文件
     *
     * @param uploadUrl
     * @param file
     * @return
     */
    @Multipart
    @POST
    suspend fun uploadFileAsync(
        @Url uploadUrl: String,
        @Part file: MultipartBody.Part
    ): Response<List<ServerUploadResultResponses>>
//#06#
}
