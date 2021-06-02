package com.example.jialichun.client.activities;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jialichun.client.R;
import com.example.jialichun.client.model.Door;
import com.example.jialichun.client.view.CircleRec;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    ArrayList<Door> dl = new ArrayList<Door>();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dl.add(new Door("北门",false));
        dl.add(new Door("西门",true));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }
    public void showDialog(View view,int i){
        final BottomSheetDialog dialog=new BottomSheetDialog(MapActivity.this);
        View dialogView= LayoutInflater.from(MapActivity.this)
                .inflate(R.layout.bottom_door,null);
        TextView tv_name = (TextView)dialogView.findViewById(R.id.door_name);
        TextView tv_state = (TextView)dialogView.findViewById(R.id.door_state);
        CircleRec cr_sym = (CircleRec)dialogView.findViewById(R.id.door_stateSymbol);
        tv_name.setText(dl.get(i).getName());
        if(dl.get(i).getState()){
            tv_state.setText("已损坏");
            cr_sym.setBgColor(R.color.yellow);
        }
        dialog.setContentView(dialogView);
        dialog.show();

    }
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.map1:
                showDialog(v,0);
                break;
            case R.id.map2:
                showDialog(v,1);
                break;
            default:
                break;
            }
    }
    public void onClickBar(View v){
        switch (v.getId()) {

            case R.id.door_wrong:
                Toast tst = Toast.makeText(this,"已报修"+v.getId() , Toast.LENGTH_SHORT);
                tst.show();
                break;
            case R.id.door_note:
                intent=new Intent(MapActivity.this,NoteActivity.class);
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
    public void onClickMessage(View v){
        intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }
}
