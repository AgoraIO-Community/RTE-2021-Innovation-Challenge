package com.example.jialichun.client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jialichun.client.R;

public class MessageActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }
    public void onClick(View v){
        Boolean i = false;
        switch (v.getId()) {
            case R.id.maneger:
                 intent=new Intent(MessageActivity.this,ChatActivity.class);
                 i=true;
                intent.putExtra("data",i);
                startActivity(intent);
                break;
            case R.id.cus:
                intent=new Intent(MessageActivity.this,ChatActivity.class);
                i=false;
                intent.putExtra("data",i);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public void onClickBack(View v) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
