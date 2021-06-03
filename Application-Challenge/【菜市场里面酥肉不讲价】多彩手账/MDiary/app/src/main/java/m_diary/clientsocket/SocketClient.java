package m_diary.clientsocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import m_diary.activities.DiaryActivity;
import m_diary.activities.LoginActivity;
import m_diary.activities.MainActivity;
import m_diary.activities.RegisterActivity;
import m_diary.assets.User;
import m_diary.utils.MD5Utils;
import m_diary.utils.Protocol;
import m_diary.utils.UserManager;

import static android.app.Activity.RESULT_OK;

public class SocketClient {
    private Socket socket = null;
    private OutputStreamWriter socketOut = null;
    private BufferedWriter bw = null;
    private DataOutputStream dos = null;
    private InputStreamReader isr = null;
    private BufferedReader socketIn = null;
    private String serverIp = "10.181.206.21";//"101.37.24.119";本地：localhost
    private int serverPort = 1081;
    private ExecutorService mExecutorService = null;
    // 实现单例化
    private static final SocketClient socketClient = new SocketClient();
    public static SocketClient getSocketClient() {// 返回值为ChatManager
        return socketClient;
    }
    private SocketClient() {mExecutorService = Executors.newCachedThreadPool();}
    private void connect(){
        try {
            //初始化客户端
            socket = new Socket(serverIp, serverPort);
            //获取输出打印流
            socketOut = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            bw = new BufferedWriter(socketOut);
            dos = new DataOutputStream(socket.getOutputStream());
            isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
            socketIn = new BufferedReader(isr);
            System.out.println("连接服务器成功!");
            // 已启动连接socket服务器，准备实时接收来自其他客户端的消息
        } catch (IOException e) {
            System.out.println("连接服务器失败!");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /***********************各种访问服务器线程类*****************************/
    //连接服务器
    private class ConnectSocket implements Runnable{
        @Override
        public void run() {
            connect();
        }
    }
    //向服务端发送消息
    private class SendMessage implements Runnable{
        private String message;
        public SendMessage(String message){
            this.message = message;
        }
        @Override
        public void run() {
            if(socket == null){
                connect();
            }
            else {
                //发送信息
                try {
                    bw.write(message + "\n");
                    bw.flush();
                } catch (Exception e) {
                    System.out.println("消息发送失败！");
                    e.printStackTrace();
                }
            }
        }
    }
    //向服务端发送文件
    private class SendFile implements Runnable{
        private String filePath;
        private Context context;
        public SendFile(Context context, String filePath){
            this.context = context;
            this.filePath = filePath;
        }
        @Override
        public void run() {
            if(socket == null){
                connect();
            }
            else {
                try {
                    File file = new File(filePath);
                    System.out.println("文件大小：" + file.length() + "b");
                    DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
                    byte[] buf = new byte[1024];
                    int len = 0;
                    dos.writeUTF(file.getName());
                    dos.flush();
                    dos.writeLong(file.length());
                    dos.flush();
                    while ((len = dis.read(buf)) != -1) {
                        dos.write(buf, 0, len);
                    }
                    dos.flush();
                    System.out.println("文件上传结束，，，，");
                    dis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    //得到消息
    private class GetMessage implements Runnable{
        private String result;
        private String requestCode;
        private LoginActivity loginActivity;
        private RegisterActivity registerActivity;
        private MainActivity mainActivity;
        private DiaryActivity diaryActivity;
        public GetMessage(Context context, String requestCode){
            result = "";
            this.requestCode = requestCode;
            switch (requestCode){
                case Protocol.LOGIN:{
                    loginActivity = (LoginActivity)context;
                }break;
                case Protocol.REGISTER:{
                    registerActivity = (RegisterActivity)context;
                }break;
                case Protocol.Get_Weather:{
                    mainActivity = (MainActivity)context;
                }
                break;
                case Protocol.SEND_FILE:{
                    diaryActivity = (DiaryActivity)context;
                }
                break;
            }
        }
        @Override
        public void run() {
            if(socket == null){
                connect();
            }
            else {
                try {
                    String message = socketIn.readLine();
                    System.out.println("服务端消息：" + message);
                    //这里是得到消息
                    if (!message.equals("")) {
                        result = message;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println("消息接收失败！");
                    e.printStackTrace();
                }
                if (!result.equals("")) {
                    switch (requestCode) {
                        case Protocol.LOGIN: {
                            loginActivity.login(result);
                        }
                        break;
                        case Protocol.REGISTER: {
                            registerActivity.register(result);
                        }
                        break;
                        case Protocol.Get_Weather: {
                            mainActivity.setWeather(result);
                        }
                        break;
                        case Protocol.SEND_FILE:
                        case Protocol.SEND_SUCCESS:
                        case Protocol.SEND_FAILED: {
                            boolean sendComplete = diaryActivity.sendFile(result);
                            if (sendComplete) {
                                diaryActivity.resetNum();
                                Toast.makeText(diaryActivity, "已全部传输完成！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    Looper.loop();
                }
            }
        }
    }
    /***********************各种访问服务器线程类*****************************/
    /***********************外部使用接口*****************************/
    //连接服务器
    public boolean connectToServer(){
        mExecutorService.execute(new ConnectSocket());
        if(socket == null){
            return false;
        }
        else{
            return true;
        }
        //mExecutorService.shutdown();
    }
    //向服务端发送消息
    public void sendMessage(String message) {
        mExecutorService.execute(new SendMessage(message));
        //mExecutorService.shutdown();
    }
    //向服务端发送文件
    public void sendFile(Context context, String filePath){
        mExecutorService.execute(new SendFile(context, filePath));
        //mExecutorService.shutdown();
    }
    //得到消息
    public void getMessage(Context context, String requestCode) {
        mExecutorService.execute(new GetMessage(context, requestCode));
        //mExecutorService.shutdown();
    }
    //判断是否有网络
    public boolean isNetworkConnected(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //断开时关闭连接
    public void shutDownClient() {
        //依次关闭各种流
        try {
            bw.write("bye");
            bw.flush();
            bw.close();
            socketOut.close();
        }catch(Exception e) {
            System.out.println("消息发送失败！");
            e.printStackTrace();
        }
        try{
            socket.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    /***********************外部使用接口*****************************/
}
