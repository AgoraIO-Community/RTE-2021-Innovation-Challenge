package com.example.jialichun.client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jialichun.client.R;

public class UserDetailActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }
    public void onClickBack(View v) {
        // TODO Auto-generated method stub
        this.finish();
    }
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.send_mes:
                intent = new Intent(UserDetailActivity.this, ChatActivity.class);
                startActivity(intent);
                break;
        }
    }
}
