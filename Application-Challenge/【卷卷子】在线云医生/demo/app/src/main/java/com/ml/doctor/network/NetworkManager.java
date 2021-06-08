package com.ml.doctor.network;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ml.doctor.CustomApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager {
    private static NetworkManager mInstance;
    private OkHttpClient client;
    private Handler handler = new Handler();
    private Gson mGson;
    private Context mContext;

    private enum Method {
        GET, POST
    };

    private NetworkManager(){
        client = new OkHttpClient();
        mGson = new Gson();
        mContext = CustomApplication.getInstance();
    }

    public interface SuccessCallback<T> {
        public void onSuccess(T response);
    }

    public interface FailedCallback {
        public void onFailed(String message);
    }

    public static NetworkManager getInstance(){
        if (mInstance == null){
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public void getResultString(String url, SuccessCallback<String> successCallback){
//        try {
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        } catch (Exception e){
//            return e.getMessage();
//        }
        getResultString(url, null, successCallback);
    }


    public void getResultString(String url, Map<String, String> params, SuccessCallback<String> successCallback){
        getResultString(url, params, successCallback, null);
    }

    public void getResultString(String url, Map<String, String> params,
                                SuccessCallback<String> successCallback, FailedCallback failedCallback){
        doRequest(Method.GET, url, params, null, successCallback, failedCallback);
    }

    public void getResultClass(String url, Class mClass, SuccessCallback successCallback){
        getResultClass(url, null, mClass, successCallback);
    }

    public void getResultClass(String url, Map<String, String> params, Class mClass, SuccessCallback successCallback){
        getResultClass(url, params, mClass, successCallback, null);
    }

    public void getResultClass(String url, Map<String, String> params, Class mClass, SuccessCallback successCallback, FailedCallback failedCallback){
        doRequest(Method.GET, url, params, mClass, successCallback, failedCallback);
    }

    public void getResultClass(String url, Map<String, String> params, Type mType, SuccessCallback successCallback){
        doRequest(Method.GET, url, params, mType, successCallback, null);
    }

    public void getResultClass(String url, Map<String, String> params, Type mType, SuccessCallback successCallback, FailedCallback failedCallback){
        doRequest(Method.GET, url, params, mType, successCallback, failedCallback);
    }

    public void postResultString(String url, Map<String, String> params, SuccessCallback successCallback){
        postResultString(url, params, successCallback, null);
    }

    public void postResultString(String url, Map<String, String> params, SuccessCallback successCallback, FailedCallback failedCallback){
        doRequest(Method.POST, url, params, null, successCallback, failedCallback);
    }

    public void postResultClass(String url, Map<String, String> params, Class mClass, SuccessCallback successCallback, FailedCallback failedCallback){
        doRequest(Method.POST, url, params, mClass, successCallback, failedCallback);
    }

    public void postResultClass(String url, Map<String, String> params, Type mType, SuccessCallback successCallback, FailedCallback failedCallback){
        doRequest(Method.POST, url, params, mType, successCallback, failedCallback);
    }

    public void doRequest(Method method, String url, Map<String, String> paramMap, final Object type,
                          final SuccessCallback successCallback, final FailedCallback failedCallback){
        Callback responseCallback = initCallback(type, successCallback, failedCallback);
        Request.Builder builder = new Request.Builder();
        switch (method){
            case GET:
                builder.url(paramMap == null ? url : url + "?" + addParams(paramMap));
                break;
            case POST:
                FormBody.Builder paramBuilder = new FormBody.Builder();
                if (paramMap != null){
                    for (Map.Entry<String, String> item : paramMap.entrySet()){
                        paramBuilder.add(item.getKey(), item.getValue());
                    }
                }
                builder.url(url).post(paramBuilder.build());
//                builder.url(paramMap == null ? url : url + "?" + addParams(paramMap)).post(paramBuilder.build());
                break;
        }
//        if (!TextUtils.isEmpty(CustomApplication.getInstance().userToken)){
//            builder.addHeader("token", CustomApplication.getInstance().userToken);
//        }
        Request request = builder.build();
        if (request != null){
            client.newCall(request).enqueue(responseCallback);
        }
    }

    private Callback initCallback(final Object type, final SuccessCallback successCallback, final FailedCallback failedCallback) {
        Callback responseCallback = new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handleFailedRequest(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response){
                try {
                    if (response == null){
                        return;
                    }
                    String responseString = response.body().string();
                    handleResponseRequest(responseString);
                } catch (Exception e) {
                    handleFailedRequest(e.getMessage());
                    e.printStackTrace();
                }
            }

            private void handleResponseRequest(final String response) {
                if (successCallback == null){
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            if (responseObject.getBoolean("tag")){
                                if (type == null) {
                                    successCallback.onSuccess(responseObject.getString("data"));
                                } else if (type instanceof Class || type instanceof Type) {
                                    successCallback.onSuccess(mGson.fromJson(responseObject.getString("data"),
                                            type instanceof Class ? (Class)type : (Type)type));
                                }
                            } else {
                                setDefaultFailed(responseObject.getString("message"));
                            }
                        } catch (Exception e) {
                            setDefaultFailed(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }

            private void handleFailedRequest(final String message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setDefaultFailed(message);
                    }
                });
            }

            private void setDefaultFailed(String message){
                if (failedCallback != null) {
                    failedCallback.onFailed(message);
                } else {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        };
        return responseCallback;
    }

    private String addParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        if(params != null){
            for(Map.Entry<String, String> item : params.entrySet()){
                builder.append(item.getKey()).append("=").append(item.getValue()).append("&");
            }
        }
//        try {
//            for(Map.Entry<String, String> item : params.entrySet()){
//                builder.append(item.getKey()).append("=").
//                        append(URLEncoder.encode(item.getValue(), "UTF-8")).append("&");
//            }
//        } catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
        builder.append("timestamp_now=").append(System.currentTimeMillis());
        return builder.toString();
    }

//    private Callback responseCallback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.i("mylog", "failed : " + e.getMessage());
////            Toast.makeText(CustomApplication.getInstance(), "failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.i("mylog", "success : " + response.body().string());
////            Toast.makeText(CustomApplication.getInstance(), "success : " + response.body().string(), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    public void postResultString(String url, Map<String, String> params){
//        FormBody.Builder paramBuilder = new FormBody.Builder();
//        for (Map.Entry<String, String> item : params.entrySet()){
//            paramBuilder.add(item.getKey(), item.getValue());
//        }
//        Request request = new Request.Builder().url(url).post(paramBuilder.build()).build();
//        client.newCall(request).enqueue(responseCallback);
//    }


}
