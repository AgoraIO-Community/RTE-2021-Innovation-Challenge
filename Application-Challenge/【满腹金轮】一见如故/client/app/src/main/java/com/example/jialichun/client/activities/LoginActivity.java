package com.example.jialichun.client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jialichun.client.R;

public class LoginActivity extends AppCompatActivity {
    Toast tst;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_bt:
                intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                MyApplication application = (MyApplication)this.getApplicationContext();
                application.setState(true);
                break;
            case R.id.forget_bt:
                intent=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.regist_bt:
                intent=new Intent(LoginActivity.this,Regist1Activity.class);
                startActivity(intent);
                break;

        }
    }
}
