package io.agora.education.impl.board.network

import io.agora.education.impl.ResponseBody
import io.agora.education.impl.board.data.request.BoardRoomStateReq
import io.agora.education.impl.board.data.request.BoardUserStateReq
import io.agora.education.impl.board.data.response.BoardRoomRes
import io.agora.education.impl.board.data.response.BoardUserRes
import retrofit2.Call
import retrofit2.http.*

internal interface BoardService {
    /**
     * 获取白板房间
     */
    @GET("/scenario/board/apps/{appId}/v1/rooms/{roomUuid}")
    fun getBoardRoom(
            @Header("token") token: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String
    ): Call<ResponseBody<BoardRoomRes>>

    /**
     * 修改白板房间状态
     */
    @PUT("/scenario/board/apps/{appId}/v1/rooms/{roomUuid}/state")
    fun updateBoardRoomState(
            @Header("token") token: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Body body: BoardRoomStateReq
    ): Call<ResponseBody<Nothing>>

    /**
     * 获取白板人员状态
     */
    @GET("/scenario/board/apps/{appId}/v1/rooms/{roomUuid}/users")
    fun getBoardUsers(
            @Header("token") token: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String
    ): Call<ResponseBody<List<BoardUserRes>>>

    /**
     * 修改白板人员状态
     */
    @PUT("/scenario/board/apps/{appId}/v1/rooms/{roomUuid}/users/{userUuid}")
    fun updateBoardUserState(
            @Header("token") token: String,
            @Path("appId") appId: String,
            @Path("roomUuid") roomUuid: String,
            @Path("userUuid") userUuid: String,
            @Body body: BoardUserStateReq
    ): Call<ResponseBody<Nothing>>
}
