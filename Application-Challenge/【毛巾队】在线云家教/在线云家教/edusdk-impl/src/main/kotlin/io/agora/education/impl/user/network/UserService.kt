package io.agora.education.impl.user.network

import io.agora.education.impl.room.data.response.EduStreamListRes
import io.agora.education.impl.room.data.response.EduUserListRes
import io.agora.education.impl.user.data.request.*
import io.agora.education.impl.ResponseBody
import io.agora.education.impl.room.data.request.EduJoinClassroomReq
import io.agora.education.impl.room.data.response.EduEntryRes
import retrofit2.Call
import retrofit2.http.*

internal interface UserService {

    /**加入房间*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{userUuid}/entry")
    fun joinClassroom(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("userUuid") userUuid: String,
            @Body eduJoinClassroomReq: EduJoinClassroomReq
    ): Call<ResponseBody<EduEntryRes>>

    /**@param role 角色, 多个逗号分隔 非必须参数（拉全量数据，不传此参数等于所有角色值全传）
     * @param nextId 下一页起始ID；非必须参数
     * @param count 返回条数	*/
    @GET("/scene/apps/{appId}/v1/rooms/{roomUuid}/users")
    fun getUserList(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
//            @Query("role")       role: String?,
            @Query("nextId") nextId: String?,
            @Query("count") count: Int,
            @Query("updateTimeOffset") updateTimeOffset: Long?,
            @Query("includeOffline") includeOffline: Int?
    ): Call<ResponseBody<EduUserListRes>>

    /**@param role 角色, 多个逗号分隔 非必须参数（拉全量数据，不传此参数等于所有角色值全传）
     * @param nextId 本次查询起始userId；非必须参数
     * @param count 返回条数	*/
    @GET("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/streams")
    fun getStreamList(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
//            @Query("role")       role: String?,
            @Query("nextId") nextId: String?,
            @Query("count") count: Int,
            @Query("updateTimeOffset") updateTimeOffset: Long?,
            @Query("includeOffline") includeOffline: Int?
    ): Call<ResponseBody<EduStreamListRes>>


    /**更新某一个用户的禁止聊天状态
     * @param mute 可否聊天 1可 0否*/
    @PUT("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{userUuid}")
    fun updateUserMuteState(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("userUuid") userUuid: String,
            @Body eduUserStatusReq: EduUserStatusReq
    ): Call<io.agora.base.network.ResponseBody<String>>


    /**调用此接口需要添加header->userToken
     * 此处的返回值没有写错，确实只返回code 和 msg*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{userUuid}/exit")
    fun leaveClassroom(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("userUuid") userUuid: String
    ): Call<io.agora.base.network.ResponseBody<String>>


    /**为用户添加自定义属性
     * @param key 属性key
     * @param value 属性值（null为删除）*/
    @PUT("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{userUuid}/properties/{key}")
    fun addProperty(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("userUuid") userUuid: String,
            @Path("key") key: String,
            @Body req: EduUpdateUserPropertyReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**开启 邀请/申请流程*/
    @POST("/scene/apps/{appId}/v1/process/{processUuid}/start")
    fun startAction(
            @Path("appId") appId: String,
            @Path("processUuid") processUuid: String,
            @Body startActionReq: EduStartActionReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**开启 邀请/申请流程*/
    @POST("/scene/apps/{appId}/v1/process/{processUuid}/stop")
    fun stopAction(
            @Path("appId") appId: String,
            @Path("processUuid") processUuid: String,
            @Body stopActionReq: EduStopActionReq
    ): Call<io.agora.base.network.ResponseBody<String>>
}