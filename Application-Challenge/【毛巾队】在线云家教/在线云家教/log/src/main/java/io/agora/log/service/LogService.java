package io.agora.log.service;

import androidx.annotation.NonNull;

import io.agora.log.UploadManager;
import io.agora.log.service.bean.ResponseBody;
import io.agora.log.service.bean.response.LogParamsRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LogService {
    @POST("/monitor/v1/log/oss/policy")
    Call<ResponseBody<LogParamsRes>> logParams(
            @Header("sign") @NonNull String sign,
            @Header("timestamp") @NonNull String timestamp,
            @Body @NonNull UploadManager.UploadParam param
    );

    @POST
    Call<ResponseBody<String>> logStsCallback(@Url String url);
}
