package m_diary.utils;

import android.content.Context;

import com.google.gson.Gson;

import m_diary.assets.User;
import m_diary.clientsocket.SocketClient;

public class UserManager {
    private UserManager() {}
    //实现单例化
    private static UserManager userManager = new UserManager();
    //保存当前登录用户
    public static String username;
    public static String psw;
    public static UserManager getUserManager() {
        return userManager;
    }
    //登录函数，返回登录结果字符串
    public void login(Context context, User user){
        UserManager.username = user.username;
        UserManager.psw = user.password;
        String jsonString = new Gson().toJson(user);//{request_code:"login", username:"", password:""}
        SocketClient.getSocketClient().sendMessage(jsonString);
        SocketClient.getSocketClient().getMessage(context, Protocol.LOGIN);
    }
    //注册函数，返回注册结果字符串
    public void register(Context context, User user){
        UserManager.username = user.username;
        UserManager.psw = user.password;
        String jsonString = new Gson().toJson(user);
        SocketClient.getSocketClient().sendMessage(jsonString);
        SocketClient.getSocketClient().getMessage(context, Protocol.REGISTER);
    }
    //注销函数，返回注销结果字符串
    public void logout(Context context, User user) {
        UserManager.username = user.username;
        UserManager.psw = user.password;
        String jsonString = new Gson().toJson(user);
        SocketClient.getSocketClient().sendMessage(jsonString);
        SocketClient.getSocketClient().getMessage(context, Protocol.LOGOUT);
    }
}
