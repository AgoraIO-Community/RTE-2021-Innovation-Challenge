package com.qgmodel.qggame.view.activity.mainActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.FragmentFriendBinding;
import com.qgmodel.qggame.entity.FriendUidInfo;
import com.qgmodel.qggame.entity.PlayerInfo;
import com.qgmodel.qggame.utils.MessageUtil;
import com.qgmodel.qggame.utils.NoticePopupWindows;
import com.qgmodel.qggame.utils.PinyinUtils;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.utils.ViewUtils;
import com.qgmodel.qggame.view.activity.MessageActivity;
import com.qgmodel.qggame.view.adapter.FriendListAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class FriendFragment extends Fragment implements View.OnClickListener {

    private FragmentFriendBinding mBinding;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private List<PlayerInfo> playerInfos = new ArrayList<>();
    private FriendListAdapter friendListAdapter;

    private NoticePopupWindows noticePopupWindows = null;

    private static final int CHAT_REQUEST_CODE = 1;
    private boolean mIsPeerToPeerMode = true;

    private String nowUID;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            getFriendList();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = FragmentFriendBinding.inflate(inflater, container,false);
        nowUID = SPUtils.getString(this.getContext(),"uid");
        initView();

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.friendToolbar.inflateMenu(R.menu.friend_add);
        mBinding.friendToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.friend_add){

                noticePopupWindows.showAtLocation(mBinding.getRoot(),Gravity.CENTER,0,0);
                noticePopupWindows.getCancel().setOnClickListener(view1 -> noticePopupWindows.close());

                initPopClick();

                return true;
            }

            return false;
        });
    }

    private void initPopClick() {
        noticePopupWindows.getSure().setOnClickListener(this);
    }

    private void initView() {
        initFriendList();
        initToolBar();
        initPopWindow();
        refreshFriendData();
    }

    private void refreshFriendData() {

        mBinding.friendSwipeRefreshLayout.setColorSchemeResources(R.color.blue);

        mBinding.friendSwipeRefreshLayout.setOnRefreshListener(() -> {
            //下拉刷新获取好友列表
            getFriendList();
            mBinding.friendSwipeRefreshLayout.setRefreshing(false);
        });

    }

    private void initPopWindow() {

        if(noticePopupWindows == null){
            noticePopupWindows = new NoticePopupWindows(this.getContext());
            noticePopupWindows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noticePopupWindows.setEditDissmiss(false);
            noticePopupWindows.setFocusable(true);
            noticePopupWindows.setTopTv("添加好友");
            noticePopupWindows.setContentTv("请输入UID");
        }

    }

    private void initToolBar() {
        ViewUtils.setTitleCenter(mBinding.friendToolbar);
    }

    private void initFriendList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendListAdapter = new FriendListAdapter(playerInfos,this.getContext(),mBinding);

        friendListAdapter.setOnItemClickLitener((view, playerInfo) -> jumpToMessageActivity(playerInfo));

        mBinding.friendRecycleview.setLayoutManager(layoutManager);
        mBinding.friendRecycleview.setAdapter(friendListAdapter);

    }

    /*获取好友列表*/
    private void getFriendList() {

        BmobQuery<FriendUidInfo> query = new BmobQuery<>();

        query.findObjects(new FindListener<FriendUidInfo>() {
            @Override
            public void done(List<FriendUidInfo> list, BmobException e) {
                if(e == null){
                    if(list.size()!=0){

                        boolean haveFriend = false;

                        for(FriendUidInfo friendUidInfo:list){

                            if(friendUidInfo.getUid().equals(nowUID)){
                                haveFriend = true;
                                uidToPlayerInfo(friendUidInfo.getFriendUid());

                            }else if(friendUidInfo.getFriendUid().equals(nowUID)){
                                haveFriend = true;
                                uidToPlayerInfo(friendUidInfo.getUid());
                            }

                        }

                        //好友列表按好友姓名首字母排序
                        Collections.sort(playerInfos, (playerInfo1, playerInfo2) -> {
                            PlayerInfo playerInfo3 = new PlayerInfo();
                            playerInfo3.setName(PinyinUtils.ToPinyinAndGetFirstChar(playerInfo1.getName()));
                            PlayerInfo playerInfo4 = new PlayerInfo();
                            playerInfo4.setName(PinyinUtils.ToPinyinAndGetFirstChar(playerInfo2.getName()));
                            int i =playerInfo3.getName().substring(0,1).compareTo(playerInfo4.getName().substring(0,1));
                            return i;
                        });

                        if(friendListAdapter!=null){
                            friendListAdapter.notifyDataSetChanged();
                        }

                        if(!haveFriend){
                            clearFriendList();
                            showToast("好友列表为空，请添加好友");
                        }

                    }else{
                        clearFriendList();
                        showToast("好友列表为空，请添加好友");
                    }
                }else{

                    switch(e.getErrorCode()){

                        case 9016:showToast("查询好友列表失败，请检查网络连接");
                        break;

                        case 9009:
                            clearFriendList();
                            showToast("好友列表为空，请添加好友");
                        break;

                    }

                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    /*获取好友列表为空时 清空好友缓存*/
    private void clearFriendList(){
        playerInfos.clear();
        friendListAdapter.notifyDataSetChanged();
    }

    /*根据uid获取用户信息*/
    private void uidToPlayerInfo(String friendUid) {

        List<PlayerInfo> playerInfoList = new ArrayList<>();
        BmobQuery<PlayerInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<PlayerInfo>() {
            @Override
            public void done(List<PlayerInfo> list, BmobException e) {

                if(e==null){
                    for(PlayerInfo playerInfo :list){
                        if(playerInfo.getUid().equals(friendUid)&&!playerInfos.contains(playerInfo)){
                            playerInfoList.add(playerInfo);
                        }
                    }

                    playerInfos.addAll(playerInfoList);
                    friendListAdapter.notifyDataSetChanged();

                }else{
                    Log.e("bmob",e.toString());
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.notice_content_sure:
                String uid = String.valueOf(noticePopupWindows.getTextBox().getText());

                if(!uid.isEmpty()){

                    BmobQuery<PlayerInfo> query = new BmobQuery<>();
                    query.findObjects(new FindListener<PlayerInfo>() {
                        @Override
                        public void done(List<PlayerInfo> list, BmobException e) {
                            if(e == null){

                                boolean isExist = false;

                                if(list.size()!=0){
                                    for(PlayerInfo playerInfo :list) {

                                        //若uid存在于用户表
                                        if(playerInfo.getUid().equals(uid)){

                                            isExist = true;

                                            //将该uid加入FriendUidInfo好友表 建立uid-friendUid
                                            FriendUidInfo friendUidInfo = new FriendUidInfo(nowUID, playerInfo.getUid());
                                            friendUidInfo.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e == null) {
                                                        //好友不重复
                                                        if(!playerInfos.contains(playerInfo)){
                                                            playerInfos.add(playerInfo);
                                                            getFriendList();

                                                            showToast("添加好友成功");
                                                        }else{
                                                            showToast("该用户已经存在于好友列表");
                                                        }

                                                    } else {
                                                        Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    if(!isExist){
                                        showToast("该用户不存在");
                                    }
                                }


                            }else{
                                showToast("查询用户失败");
                                Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });


                }else{
                    showToast("输入昵称不能为空");
                }

                noticePopupWindows.close();
                noticePopupWindows.getTextBox().getText().clear();
                break;
        }
    }

    private void jumpToMessageActivity(PlayerInfo playerInfo) {
        Intent intent = new Intent(this.getContext(), MessageActivity.class);
        intent.putExtra(MessageUtil.INTENT_EXTRA_IS_PEER_MODE, mIsPeerToPeerMode);
        intent.putExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME, playerInfo.getUid());
        intent.putExtra(MessageUtil.INTENT_EXTRA_FRIEND_NAME, playerInfo.getName());
        intent.putExtra(MessageUtil.INTENT_EXTRA_USER_ID, nowUID);
        startActivityForResult(intent, CHAT_REQUEST_CODE);
    }


    private void showToast(final String text) {
        mainHandler.post(() -> Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show());
    }

}
