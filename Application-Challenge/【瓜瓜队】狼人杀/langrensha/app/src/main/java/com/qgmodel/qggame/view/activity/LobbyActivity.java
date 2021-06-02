package com.qgmodel.qggame.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.view.adapter.RoomAdapter;
import com.qgmodel.qggame.contract.LobbyContract;
import com.qgmodel.qggame.databinding.ActivityLobbyBinding;
import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.presenter.LobbyPresenter;
import com.qgmodel.qggame.utils.NoticePopupWindows;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.utils.ViewUtils;
import com.qgmodel.qggame.view.IView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by HeYanLe on 2020/8/8 0008 17:27.
 * https://github.com/heyanLE
 */
public class LobbyActivity extends AppCompatActivity implements IView, View.OnClickListener {

    private LobbyPresenter mPresenter;
    private ActivityLobbyBinding mBinding;
    private RoomAdapter roomAdapter;
    private List<RoomInfo> rooms = new ArrayList<>();

    private NoticePopupWindows noticePopupWindows = null;

    public ActivityLobbyBinding getBinding() {
        return mBinding;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

    public RoomAdapter getRoomAdapter() {
        return roomAdapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLobbyBinding.inflate(LayoutInflater.from(this));

        setContentView(mBinding.getRoot());
        ViewUtils.setTitleCenter(mBinding.toolbar);
        setSupportActionBar(mBinding.toolbar);
        mBinding.roomTalkLayout.setVisibility(View.GONE);
        mBinding.toolbar.setNavigationIcon(R.mipmap.back);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 游戏界面返回
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        mPresenter = LobbyContract.getInstance().bind(this);

    }

    private void initView(){
        roomAdapter = new RoomAdapter(rooms);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(LobbyActivity.this));
        mBinding.recycler.setAdapter(roomAdapter);

        roomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(rooms.get(position).getPlayer() < RoomInfo.MAX_PLAYER){
                    Intent callIntent = new Intent(LobbyActivity.this, CallActivity.class);
                    callIntent.putExtra("myId", 114514);
                    callIntent.putExtra("videoChannel",rooms.get(position).getChannelName());
                    //callIntent.putExtra("accessToken",getString(R.string.token));
                    callIntent.putExtra("roomerId",rooms.get(position).getRoomerId());
                    startActivity(callIntent);
                }else{
                    Toast.makeText(LobbyActivity.this, "房间已经满人了哦！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        initPopWindow();
    }

    private void initPopWindow() {

        if(noticePopupWindows == null){
            noticePopupWindows = new NoticePopupWindows(this.getContext());
            noticePopupWindows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noticePopupWindows.setEditDissmiss(false);
            noticePopupWindows.setFocusable(true);
            noticePopupWindows.setTopTv("新建房间");
            noticePopupWindows.setContentTv("请输入房间名");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        LobbyContract.getInstance().unbind();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.room_add).setOnMenuItemClickListener(menuItem -> {

            noticePopupWindows.showAtLocation(mBinding.getRoot(), Gravity.CENTER,0,0);
            noticePopupWindows.getCancel().setOnClickListener(view1 -> noticePopupWindows.close());

            noticePopupWindows.getTextBox().setText("一起来玩~");

            initPopClick();

            return false;
        });
        return true;
    }

    private void initPopClick() {
        noticePopupWindows.getSure().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.notice_content_sure:

                String s = noticePopupWindows.getTextBox().getText().toString();
                if (s.isEmpty()){
                    Toast.makeText(getContext(), "房间名不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    RoomInfo roomInfo = new RoomInfo();
                    roomInfo.setRoomerId(SPUtils.getString(LobbyActivity.this, "uid"));
                    roomInfo.setTitle(s);
                    roomInfo.setPlayer(1);
                    roomInfo.setChannelName(System.currentTimeMillis()+"");
                    roomInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Toast.makeText(LobbyActivity.this, "创建房间成功",Toast.LENGTH_SHORT).show();
                                Intent callIntent = new Intent(LobbyActivity.this, CallActivity.class);
                                callIntent.putExtra("myId", Integer.parseInt(SPUtils.getString(LobbyActivity.this, "uid")));
                                callIntent.putExtra("videoChannel",roomInfo.getChannelName());
                                callIntent.putExtra("roomerId",roomInfo.getRoomerId());

                                startActivity(callIntent);
                            }else{
                                BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
                                query.addWhereEqualTo("channelName", roomInfo.getChannelName());
                                query.findObjects(new FindListener<RoomInfo>() {
                                    @Override
                                    public void done(List<RoomInfo> list, BmobException e) {
                                        if (list == null || list.size() == 0) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "创建房间失败", Toast.LENGTH_SHORT).show();
                                        } else {
                                            RoomInfo roomInfo = list.get(0);
                                            Intent callIntent = new Intent(LobbyActivity.this, CallActivity.class);
                                            callIntent.putExtra("myId", Integer.parseInt(SPUtils.getString(LobbyActivity.this, "uid")));
                                            callIntent.putExtra("videoChannel", roomInfo.getChannelName());
                                            callIntent.putExtra("roomerId", roomInfo.getRoomerId());
                                            callIntent.putExtra("isRoomer", true);
                                            startActivity(callIntent);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }


                noticePopupWindows.close();
                noticePopupWindows.getTextBox().getText().clear();
                break;
        }
    }
}







