//package com.example.jialichun.client;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
///**
// * Created by jialichun on 2018/2/22.
// */
//
//public class TestButtonActivity extends Activity {
//
//    Button btn1, btn2;
//    Toast tst;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    // 注意 这里没有 @Override 标签
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//            case R.id.b1:
//                tst = Toast.makeText(this, "111111111", Toast.LENGTH_SHORT);
//                tst.show();
//                System.out.println("123");
//                break;
//            case R.id.b:
//                tst = Toast.makeText(this, "9", Toast.LENGTH_SHORT);
//                tst.show();
//                System.out.println("123");
//                break;
//            default:
//                tst = Toast.makeText(this, "wrong", Toast.LENGTH_SHORT);
//                tst.show();
//                System.out.println("wrong");
//                break;
//        }
//    }
//}