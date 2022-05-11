package m_diary.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import m_diary.activities.LoginActivity;
import m_diary.clientsocket.SocketClient;

public class DiaryApplication extends Application {
    private Context context;
    public static SharedPreferences loginPreferences;
    public static SharedPreferences diaryPreferences;
    public static SharedPreferences.Editor loginEditor;
    public static final String LOGIN_INFO = "loginInfo";
    public static final String DIARY_INFO = "diaryInfo";
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if(!SocketClient.getSocketClient().connectToServer()){
            Toast.makeText(context, "服务器连接失败！", Toast.LENGTH_SHORT).show();
        }
        loginPreferences = context.getSharedPreferences(LOGIN_INFO, MODE_PRIVATE);
        loginEditor = loginPreferences.edit();
        loginPreferences = context.getSharedPreferences(DIARY_INFO, MODE_PRIVATE);
        loginEditor = loginPreferences.edit();
    }
    //保存登录信息
    public static void saveLoginInfo(boolean status, String userName, String passWord){
        loginEditor.putBoolean(Protocol.IS_LOGIN, status);
        loginEditor.putString(Protocol.USER_NAME, userName);
        loginEditor.putString(userName, passWord);
        loginEditor.apply();
    }
    //保存注册信息
    public static void saveRegisterInfo(String userName) {
        loginEditor.putString(Protocol.USER_NAME, userName);
        //提交修改 editor.commit();
        loginEditor.apply();
    }
    //读取已保存的用户密码
    public static String readUserName(){
        return loginPreferences.getString(Protocol.USER_NAME , "");
    }
    //读取已保存的用户密码
    public static String readPsw(String userName){
        return loginPreferences.getString(userName , "");
    }
}
