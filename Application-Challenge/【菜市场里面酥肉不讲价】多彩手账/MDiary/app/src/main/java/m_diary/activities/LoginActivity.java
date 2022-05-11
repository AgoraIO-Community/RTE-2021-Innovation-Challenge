package m_diary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import android.os.Bundle;

import java.util.TimerTask;

import m_diary.assets.User;
import m_diary.clientsocket.SocketClient;
import m_diary.utils.DiaryApplication;
import m_diary.utils.MD5Utils;
import m_diary.utils.Protocol;
import m_diary.utils.UserManager;


public class LoginActivity extends AppCompatActivity {
    private TextView tv_register,tv_find_psw;  //显示的注册，找回密码
    private Button btn_login;  //登录按钮
    private String userName, psw,spPsw;  //获取的用户名，密码，加密密码
    private EditText et_user_name,et_psw;  //编辑框
    private static Handler handler;
    private CheckBox checkBoxLogin;
    private CheckBox checkBoxPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        init();
    }
    private void setLoginInfo(){
        userName = DiaryApplication.readUserName();
        psw = DiaryApplication.readPsw(userName);
        et_user_name.setText(userName);
        et_psw.setText(psw);
        if(!psw.equals("")){
            checkBoxPsw.setChecked(true);
        }
        else {
            checkBoxPsw.setChecked(false);
        }
    }
    //获取界面控件
    private void init() {
        //从activity_login.xml中获取的
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        checkBoxLogin = findViewById(R.id.checkBox_login);
        checkBoxPsw = findViewById(R.id.checkBox_password);
        setLoginInfo();
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码界面（此页面暂未创建）
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SocketClient.getSocketClient().isNetworkConnected(LoginActivity.this)) {
                    //开始登录，获取用户名和密码
                    userName = et_user_name.getText().toString().trim();
                    psw = et_psw.getText().toString().trim();
                    if(userName.equals("")||psw.equals("")) {
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String md5Psw = MD5Utils.md5(psw);
                        User user = new User(Protocol.LOGIN, userName, md5Psw);
                        UserManager.getUserManager().login(LoginActivity.this, user);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "网络未连接，请打开流量或WiFi！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     *从SharedPreferences中根据用户名读取密码
     */
    public void login(String result){
        Looper.prepare();
        switch (result) {
            case Protocol.LOGIN_SUCCESS: {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //保存登录状态，在界面保存登录的用户名
                if(checkBoxPsw.isChecked()) {
                    DiaryApplication.saveLoginInfo(true, userName, psw);
                }
                else {
                    DiaryApplication.saveLoginInfo(true, userName, "");
                }
                //登录成功后关闭此页面进入主页
                Intent data = new Intent();
                data.putExtra(Protocol.IS_LOGIN, true);
                //RESULT_OK为Activity系统常量，状态码为-1，表示此页面下的内容操作成功将data返回到上一页面
                setResult(RESULT_OK, data);
                finish();
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            break;
            case Protocol.LOGIN_FAILED: {
                Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致或用户不存在！", Toast.LENGTH_SHORT).show();
            }
            break;
            case Protocol.LOGIN_NULL: {
                Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    /**
     * 注册成功的数据返回至此
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    @Override
    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            et_psw.setText("");
            String userName=data.getStringExtra(Protocol.USER_NAME);
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                et_user_name.setText(userName);
                //et_user_name控件的setSelection()方法来设置光标位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}