package io.agora.log;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import io.agora.base.callback.Callback;
import io.agora.base.callback.ThrowableCallback;
import io.agora.base.network.RetrofitManager;
import io.agora.log.service.LogService;
import io.agora.log.service.bean.ResponseBody;
import io.agora.log.service.bean.response.LogParamsRes;

public class UploadManager {

    public static final String ZIP = "zip";
    public static final String LOG = "log";
    private static final String callbackPath = "/monitor/v1/log/oss/callback";

    public static class UploadParam {
        public String appId;
        public String appVersion;
        public String deviceName;
        public String deviceVersion;
        public String fileExt;
        public String platform;
        public Object tag;

        public UploadParam(
                @NonNull String appId,
                @Nullable String appVersion,
                @Nullable String deviceName,
                @NonNull String deviceVersion,
                /**
                 * zip/log; 扩展名，如果传扩展名则以扩展名为准，如果不传，terminalType=3为log，其他为zip
                 */
                @Nullable String fileExt,
                @NonNull String platform,
                @Nullable Object tag
        ) {
            this.appId = appId;
            this.appVersion = appVersion;
            this.deviceName = deviceName;
            this.deviceVersion = deviceVersion;
            this.fileExt = fileExt;
            this.platform = platform;
            this.tag = tag;
        }
    }

    public static void upload(@NonNull Context context, @NonNull String appSecret,
                              @NonNull String host, @NonNull String uploadPath,
                              @NonNull UploadParam param, @Nullable Callback<String> callback) {
        LogService service = RetrofitManager.instance().getService(host, LogService.class);
        long timeStamp = System.currentTimeMillis();
        String sign = sign(appSecret, param, timeStamp);
        service.logParams(sign, String.valueOf(timeStamp), param)
                .enqueue(new RetrofitManager.Callback<>(0, new ThrowableCallback<ResponseBody<LogParamsRes>>() {
                    @Override
                    public void onSuccess(ResponseBody<LogParamsRes> res) {
                        res.data.callbackUrl = service.logStsCallback(host).request().url()
                                .toString().concat(callbackPath);
                        new Thread(() -> {
                            uploadByOss(context, uploadPath, res.data, callback);
                        }).start();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (callback instanceof ThrowableCallback) {
                            ((ThrowableCallback<String>) callback).onFailure(throwable);
                        }
                    }
                }));
    }

    private static void uploadByOss(@NonNull Context context, @NonNull String uploadPath, @NonNull LogParamsRes param, @Nullable Callback<String> callback) {
        try {
            File file = new File(new File(uploadPath).getParentFile(), "temp.zip");
            ZipUtils.zipFile(new File(uploadPath), file);

            // 构造上传请求。
            PutObjectRequest put = new PutObjectRequest(param.bucketName, param.ossKey, file.getAbsolutePath());
            put.setCallbackParam(new HashMap<String, String>() {{
                put("callbackUrl", param.callbackUrl);
                put("callbackBodyType", param.callbackContentType);
                put("callbackBody", param.callbackBody);
            }});
            // 推荐使用OSSAuthCredentialsProvider。token过期可以及时更新。
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(param.accessKeyId,
                    param.accessKeySecret, param.securityToken);
            OSS oss = new OSSClient(context, param.ossEndpoint, credentialProvider);
            oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                    if (callback != null) {
                        String body = result.getServerCallbackReturnBody();
                        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
                        callback.onSuccess(json.get("data").getAsString());
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                    if (callback instanceof ThrowableCallback) {
                        if (clientException != null) {
                            ((ThrowableCallback<String>) callback).onFailure(clientException);
                        } else if (serviceException != null) {
                            ((ThrowableCallback<String>) callback).onFailure(serviceException);
                        } else {
                            ((ThrowableCallback<String>) callback).onFailure(null);
                        }
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static String sign(String appSecret, UploadParam param, long timeStamp) {
//        StringBuilder stringBuilder = new StringBuilder(appSecret);
//        Map<String, Object> map = new TreeMap<>();
//        if (!TextUtils.isEmpty(param.appId)) {
//            map.put("appId", param.appId);
//        }
//        if (!TextUtils.isEmpty(param.roomId)) {
//            map.put("roomId", param.roomId);
//        }
//        if (!TextUtils.isEmpty(param.fileExt)) {
//            map.put("fileExt", param.fileExt);
//        }
//        if (!TextUtils.isEmpty(param.appCode)) {
//            map.put("appCode", param.appCode);
//        }
//        if (!TextUtils.isEmpty(param.osType)) {
//            map.put("osType", param.osType);
//        }
//        if (!TextUtils.isEmpty(param.terminalType)) {
//            map.put("terminalType", param.terminalType);
//        }
//        if (!TextUtils.isEmpty(param.appVersion)) {
//            map.put("appVersion", param.appVersion);
//        }
//        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry element = iterator.next();
//            stringBuilder.append(element.getValue());
//        }
//        stringBuilder.append(timeStamp);
//        return getMD5Str(stringBuilder.toString());
//    }

    private static String sign(String appSecret, UploadParam param, long timeStamp) {
        StringBuilder stringBuilder = new StringBuilder(appSecret);
        String json = new Gson().toJson(param);
        stringBuilder.append(json).append(timeStamp);
        return getMD5Str(stringBuilder.toString());
    }

    private static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
}
