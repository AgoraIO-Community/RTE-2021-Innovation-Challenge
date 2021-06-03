package m_diary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

import m_diary.assets.User;
import m_diary.clientsocket.SocketClient;
import m_diary.utils.DiaryApplication;
import m_diary.utils.MD5Utils;
import m_diary.utils.Protocol;
import m_diary.utils.UserManager;


public class RegisterActivity extends AppCompatActivity {
    private Button btn_register;  //注册按钮
    private EditText et_user_name, et_psw, et_psw_again;  //用户名，密码，再次输入的密码的控件
    private String userName, psw, pswAgain;  //用户名，密码，再次输入的密码的控件的获取值
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        init();
    }
    //初始化
    private void init() {
        //从activity_register.xml 页面中获取对应的UI控件
        //注册按钮
        btn_register = findViewById(R.id.btn_register);
        //用户名，密码，再次输入的密码的控件
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
//        tv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //返回键
//                RegisterActivity.this.finish();
//            }
//        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SocketClient.getSocketClient().isNetworkConnected(RegisterActivity.this)){
                    //获取输入在相应控件中的字符串
                    getEditString();
                    //判断输入框内容
                    if (TextUtils.isEmpty(userName)) {
                        Toast.makeText(RegisterActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(psw)) {
                        Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(pswAgain)) {
                        Toast.makeText(RegisterActivity.this, "请再次输入密码！", Toast.LENGTH_SHORT).show();
                    } else if (!psw.equals(pswAgain)) {
                        Toast.makeText(RegisterActivity.this, "输入两次的密码不一样！", Toast.LENGTH_SHORT).show();
                    } else {
                        String md5Psw = MD5Utils.md5(psw);
                        User user = new User(Protocol.REGISTER, userName, md5Psw);
                        UserManager.getUserManager().register(RegisterActivity.this, user);
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "网络未连接，请打开流量或WiFi！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
    }
    //注册结果判断
    public void register(String result){
        Looper.prepare();
        switch (result) {
            case Protocol.REGISTER_NULL: {
                Toast.makeText(RegisterActivity.this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();
            }
            break;
            case Protocol.REGISTER_EXIST: {
                Toast.makeText(RegisterActivity.this, "用户名已存在，请登录！", Toast.LENGTH_SHORT).show();
            }
            break;
            case Protocol.REGISTER_FAILED: {
                Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
            }
            break;
            case Protocol.REGISTER_SUCCESS: {
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                //把账号、密码和账号标识保存到sp里面
                DiaryApplication.saveRegisterInfo(UserManager.username);
                //注册成功后把账号传递到LoginActivity.java中，返回值到loginActivity显示
                Intent data = new Intent();
                data.putExtra(Protocol.USER_NAME, UserManager.username);
                setResult(RESULT_OK, data);
                //RESULT_OK为Activity系统常量，状态码为-1，表示此页面下的内容操作成功将data返回到上一页面
                finish();
            }
            break;
        }
    }
}