package com.example.jialichun.client.activities;

import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jialichun.client.R;
import com.example.jialichun.client.view.CircleRec;

public class PassportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
    }
    public void showDialog(View view){
        final BottomSheetDialog dialog=new BottomSheetDialog(PassportActivity.this);
        View dialogView= LayoutInflater.from(PassportActivity.this)
                .inflate(R.layout.bottom_pass,null);
        dialog.setContentView(dialogView);
        dialog.show();

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.pass_more:
                showDialog(v);
                break;
        }
    }
    public void onClickBack(View v) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
