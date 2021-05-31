package com.hustunique.vlive.remote

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
interface RemoteApi {

    @Headers("Content-Type: application/json")
    @POST(Constants.USER_REG)
    suspend fun userReg(@Body regReq: RegReq): BaseRsp<RegRsp>

    @Headers("Content-Type: application/json")
    @POST(Constants.CHANNEL_JOIN)
    suspend fun channelJoin(@Body channelJoinReq: ChannelJoinReq): BaseRsp<JoinRspData>

    @Headers("Content-Type: application/json")
    @POST(Constants.CHANNEL_LEAVE)
    suspend fun channelLeave(@Body requestBody: RequestBody): BaseRsp<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.CHANNEL_LIST)
    suspend fun channelList(@Body requestBody: RequestBody): BaseRsp<List<Channel>>

    @Headers("Content-Type: application/json")
    @POST(Constants.CHANNEL_CREATE)
    suspend fun createChannel(@Body requestBody: RequestBody): BaseRsp<String>

}