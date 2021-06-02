package io.agora.education.impl.room.network

import io.agora.education.impl.ResponseBody
import io.agora.education.impl.room.data.request.EduSyncRoomReq
import io.agora.education.impl.room.data.request.EduUpdateRoomPropertyReq
import io.agora.education.impl.room.data.response.*
import io.agora.education.impl.room.data.response.EduSequenceListRes
import io.agora.education.impl.room.data.response.EduSequenceSnapshotRes
import io.agora.education.impl.user.data.request.*
import io.agora.education.impl.user.data.request.EduRoomChatMsgReq
import io.agora.education.impl.user.data.request.EduRoomMsgReq
import io.agora.education.impl.user.data.request.EduUserChatMsgReq
import io.agora.education.impl.user.data.request.EduUserMsgReq
import retrofit2.Call
import retrofit2.http.*

internal interface RoomService {

    /**登录*/
    @POST("/scene/apps/{appId}/v1/users/{userUuid}/login")
    fun login(
            @Path("appId") appId: String,
            @Path("userUuid") userUuid: String
    ): Call<ResponseBody<EduLoginRes>>

    /**查询房间信息*/
    @GET("/scene/apps/{appId}/v1/rooms/{roomUuid}/info")
    fun queryClassroomState(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String
    ): Call<ResponseBody<EduEntryRoomRes>>

    /**请求同步房间的信息（包括RoomInfo以及人流信息）*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/sync")
    fun syncRoom(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Body eduSyncRoomReq: EduSyncRoomReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**更新课堂状态*/
    @PUT("/scene/apps/{appId}/v1/rooms/{roomUUid}/states/{state}")
    fun updateClassroomState(
            @Path("appId") appId: String,
            @Path("roomUUid") roomUUid: String,
            @Path("state") state: Int
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**更新课堂中对应角色的禁用状态
     * 包括禁止聊天、禁止摄像头、禁用麦克风*/
    @PUT("/scene/apps/{appId}/v1/rooms/{roomUuid}/roles/mute")
    fun updateClassroomMuteState(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Body eduRoomMuteStateReq: EduRoomMuteStateReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**发送自定义的频道消息*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/message/channel")
    fun sendChannelCustomMessage(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Body eduRoomMsgReq: EduRoomMsgReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**发送自定义的点对点消息*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{toUserUuid}/messages/peer")
    fun sendPeerCustomMessage(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("toUserUuid") toUserUuid: String,
            @Body eduUserMsgReq: EduUserMsgReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**发送课堂内群聊消息*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/chat/channel")
    fun sendRoomChatMsg(
            @Header("token") userToken: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Body eduRoomChatMsgReq: EduRoomChatMsgReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**发送用户间的私聊消息*/
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/users/{toUserUuid}/chat/peer")
    fun sendPeerChatMsg(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("toUserUuid") toUserUuid: String,
            @Body eduUserChatMsgReq: EduUserChatMsgReq
    ): Call<io.agora.base.network.ResponseBody<String>>


    /**为room添加自定义属性
     * @param key 属性key
     * @param value 属性值（null为删除）*/
    @PUT("/scene/apps/{appId}/v1/rooms/{roomUuid}/properties/{key}")
    fun addRoomProperty(
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("key") key: String,
            @Body req: EduUpdateRoomPropertyReq
    ): Call<io.agora.base.network.ResponseBody<String>>

    /**查询丢失的消息列表*/
    @GET("/scene/apps/{appId}/v1/rooms/{roomUuid}/sequences")
    fun fetchLostSequences(
            @Header("token") userToken: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Query("nextId") nextId: Int,
            @Query("count") count: Int?
    ): Call<ResponseBody<EduSequenceListRes<Any>>>

    /**查询快照(全量更新)*/
    @GET("/scene/apps/{appId}/v1/rooms/{roomUuid}/snapshot")
    fun fetchSnapshot(
            @Header("token") userToken: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String
    ): Call<ResponseBody<EduSequenceSnapshotRes>>

}