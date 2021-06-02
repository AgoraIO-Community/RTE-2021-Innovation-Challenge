package io.agora.education.service;

import io.agora.education.service.bean.ResponseBody;
import io.agora.education.service.bean.request.AllocateGroupReq;
import io.agora.education.service.bean.request.RoomCreateOptionsReq;
import io.agora.education.service.bean.response.EduRoomInfoRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommonService {

    /**
     * 分配小组:请求服务端分配一个小教室
     */
    @POST("/grouping/apps/{appId}/v1/rooms/{roomUuid}/groups")
    Call<ResponseBody<EduRoomInfoRes>> allocateGroup(
            @Path("appId") String appId,
            @Path("roomUuid") String roomUuid,
            @Body AllocateGroupReq allocateGroupReq);

    /**创建房间*/
    /**
     * @return 房间id(roomId)
     */
    @POST("/scene/apps/{appId}/v1/rooms/{roomUuid}/config")
    Call<ResponseBody<String>> createClassroom(
            @Path("appId") String appId,
            @Path("roomUuid") String roomUuid,
            @Body RoomCreateOptionsReq roomCreateOptionsReq
    );
}
