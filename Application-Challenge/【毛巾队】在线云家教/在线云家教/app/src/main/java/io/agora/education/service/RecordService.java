package io.agora.education.service;

import io.agora.education.service.bean.ResponseBody;
import io.agora.education.service.bean.response.RecordRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecordService {

    @GET("/recording/apps/{appId}/v1/rooms/{roomId}/records")
    Call<ResponseBody<RecordRes>> record(
            @Path("appId") String appId,
            @Path("roomId") String roomId,
            @Query("nextId") int nextId
    );

}
