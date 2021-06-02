package com.example.jialichun.client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.jialichun.client.R;
import com.example.jialichun.client.adapter.MyBaseExpandableListAdapter;
import com.example.jialichun.client.model.Group;
import com.example.jialichun.client.model.Item;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private ExpandableListView exlist_lol;
    private MyBaseExpandableListAdapter myAdapter = null;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mContext = UsersActivity.this;
        exlist_lol = (ExpandableListView) findViewById(R.id.exlist_lol);


        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("我的家人"));
        gData.add(new Group("我的访客"));
        gData.add(new Group("我的邻居"));

        lData = new ArrayList<Item>();

        //AD组
        lData.add(new Item(R.mipmap.iv_lol_icon13,"男枪"));
        lData.add(new Item(R.mipmap.iv_lol_icon14,"韦鲁斯"));
        iData.add(lData);
        //AP组
        lData = new ArrayList<Item>();
        lData.add(new Item(R.mipmap.iv_lol_icon1, "提莫"));
        lData.add(new Item(R.mipmap.iv_lol_icon7, "安妮"));
        lData.add(new Item(R.mipmap.iv_lol_icon8, "天使"));
        lData.add(new Item(R.mipmap.iv_lol_icon11, "狐狸"));
        iData.add(lData);
        //TANK组
        lData = new ArrayList<Item>();
        lData.add(new Item(R.mipmap.iv_lol_icon2, "诺手"));
        lData.add(new Item(R.mipmap.iv_lol_icon5, "德邦"));
        lData.add(new Item(R.mipmap.iv_lol_icon6, "奥拉夫"));
        lData.add(new Item(R.mipmap.iv_lol_icon10, "龙女"));
        lData.add(new Item(R.mipmap.iv_lol_icon12, "狗熊"));
        iData.add(lData);

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        exlist_lol.setAdapter(myAdapter);


        //为列表设置点击事件
        exlist_lol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = null;
                if(groupPosition == 1){
                    intent=new Intent(mContext,CusDetailActivity.class);

                }
                else
                    intent=new Intent(mContext,UserDetailActivity.class);
                Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition).getiName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return true;
            }
        });


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
