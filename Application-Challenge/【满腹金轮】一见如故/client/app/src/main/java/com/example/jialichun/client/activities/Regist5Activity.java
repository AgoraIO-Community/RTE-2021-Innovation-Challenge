package com.example.jialichun.client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jialichun.client.R;

public class Regist5Activity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist5);
    }
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.next:
                intent = new Intent(Regist5Activity.this, MainActivity.class);
                startActivity(intent);
                MyApplication application = (MyApplication)this.getApplicationContext();
                application.setState(true);
                break;

        }
    }
    public void onClickBack(View v) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
