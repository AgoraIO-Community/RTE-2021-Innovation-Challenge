package io.agora.openlive.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.agora.openlive.Constants;


/**
 * 类描述：android连接websocket的服务类，
 * * 定义了对设备的网络状态监听、
 * * 发送消息以及接受服务器推送的消息
 * * （通过EventBus传输信息）
 *
 * @author Mr.小艾
 */
public class WebSocketService extends Service {
    private static final String TAG=WebSocketService.class.getSimpleName();

    public static WebSocketService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static WebSocketService INSTANCE = new WebSocketService();
    }

    private WebSocketClient webSocketClient;

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启websocket连接 网络监听与websocket之间没有关联
        Log.i(TAG,"onStartCommand");
        try {
            webSocketConnect();
        } catch (URISyntaxException e) {
            //Log.e(TAG,"URISyntaxException" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            //Log.e(TAG,"NoSuchAlgorithmException" + e.getMessage());
            e.printStackTrace();
        } catch (KeyManagementException e) {
            //Log.e(TAG,"KeyManagementException" + e.getMessage());
            e.printStackTrace();
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void closeWebSocket() {
        Log.i(TAG,"closeWebSocket");
        if (webSocketClient != null) {
            webSocketClient.close();
            webSocketClient = null;
        }
    }

    public void webSocketConnect() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        System.out.println("--->onOpen" + Constants.websocketUrl);
        webSocketClient = new WebSocketClient(new URI(Constants.websocketUrl), new Draft_6455()) {
            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                Constants.webSocketState = false;
                System.out.println("--->onClose" + arg0 + "  arg1:" + arg1);
            }

            @Override
            public void onError(Exception arg0) {
                System.out.println("--->onError" + arg0);
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("--->onMessage" + arg0);
//                if (!JsonParsingUtil.x().isJson(arg0)) {
//                    Log.i(TAG,arg0);
//                }
                EventBus.getDefault().post(arg0);
            }

            @Override
            public void onOpen(ServerHandshake arg0) {
                SingletonHolder.INSTANCE = WebSocketService.this;
                Constants.webSocketState = true;
                System.out.println("--->onOpen");
            }
        };

        // wss需添加
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        SSLSocketFactory factory = sslContext.getSocketFactory();
        webSocketClient.setSocketFactory(factory);
        webSocketClient.connect();
        System.out.println("--->connect");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
    }

    /**
     * 这里使用binder 主要是为了在activity中进行通讯， 大家也可以使用EventBus进行通讯
     */
    public class MyBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }
}
