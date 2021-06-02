package com.example.jialichun.client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jialichun.client.R;
import com.zhy.android.percent.support.PercentFrameLayout;

public class NoteActivity extends AppCompatActivity {
    View dialogView;
    BottomSheetDialog dialog;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

    }
    public void showDialog(View view){
        dialogView= LayoutInflater.from(NoteActivity.this)
                .inflate(R.layout.bottom_tab,null);
        dialog=new BottomSheetDialog(NoteActivity.this);//实例化必须在这里进行
        dialog.setContentView(dialogView);
        dialog.show();

    }
    public void showCalendar(View view){
        dialogView= LayoutInflater.from(NoteActivity.this)
                .inflate(R.layout.bottom_calendar,null);
        dialog=new BottomSheetDialog(NoteActivity.this);//实例化必须在这里进行
        dialog.setContentView(dialogView);
        dialog.show();

    }
    public void onClickBtmBar(View v){
        switch (v.getId()){
            case R.id.choose:
                showDialog(v);
                break;
            case R.id.calendar:
                showCalendar(v);
                break;
            case R.id.search:
                PercentFrameLayout root = findViewById(R.id.note_root);
                final EditText et = (EditText)findViewById(R.id.searchBox);
                final InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et,0);
                et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            et.clearFocus();
                            Toast tst = Toast.makeText(NoteActivity.this,"搜索操作执行"+v.getId() , Toast.LENGTH_SHORT);
                            tst.show();
                            Log.i("---","搜索操作执行");

                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            et.setVisibility(View.INVISIBLE);
                        }
                        return false;
                    }
                });
                et.setVisibility(View.VISIBLE);
        }
    }
    public void onClickTab(View v){
        LinearLayout contain = (LinearLayout)dialogView.findViewById(R.id.tabContain);
        View child;
        contain.removeAllViews();
        switch (v.getId()){
            case R.id.user:
                contain.removeAllViews();
                child = LayoutInflater.from(NoteActivity.this)
                        .inflate(R.layout.tag_user,null);
                contain.addView(child);
                break;
            case R.id.door:
                contain.removeAllViews();
                child =LayoutInflater.from(NoteActivity.this)
                        .inflate(R.layout.tag_door,null);
                contain.addView(child);
                break;
            case R.id.direction:
                contain.removeAllViews();
                child =LayoutInflater.from(NoteActivity.this)
                        .inflate(R.layout.tag_direction,null);
                contain.addView(child);
                break;
        }
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
    public void onClickMessage(View v){
        intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }
}
