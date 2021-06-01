package com.qgmodel.qggame.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.FragmentFriendBinding;
import com.qgmodel.qggame.entity.FriendUidInfo;
import com.qgmodel.qggame.entity.PlayerInfo;
import com.qgmodel.qggame.utils.NoticePopupWindows;
import com.qgmodel.qggame.utils.SPUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class FriendListAdapter extends BaseQuickAdapter<PlayerInfo, BaseViewHolder> {

    private NoticePopupWindows noticePopupWindows;
    private Context context;
    private FragmentFriendBinding mBinding;

    private OnItemClickLitener  mOnItemClickLitener;
    
    private List<PlayerInfo> data;
    private android.os.Handler mainHandler = new android.os.Handler(Looper.getMainLooper());


    //设置回调接口
    public interface OnItemClickLitener{
        void onItemClick(View view, PlayerInfo playerInfo);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public FriendListAdapter(int layoutResId) {
        super(layoutResId);
    }

    public FriendListAdapter(@Nullable List<PlayerInfo> data, Context context,FragmentFriendBinding mBinding) {
        super(R.layout.friend_list_item, data);
        this.context = context;
        this.mBinding = mBinding;
        this.data = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PlayerInfo playerInfo) {

        baseViewHolder.setText(R.id.friend_uid, String.valueOf(playerInfo.getUid()))
                .setText(R.id.friend_name, playerInfo.getName());

        baseViewHolder.findView(R.id.friend_more).setOnClickListener(view -> {

            if(noticePopupWindows == null){
                noticePopupWindows = new NoticePopupWindows(context);
                noticePopupWindows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                noticePopupWindows.setOutsideTouchable(false);
                noticePopupWindows.getTextLayout().setVisibility(View.GONE);
                noticePopupWindows.setTopTv("删除好友");
                noticePopupWindows.setContentTv("是否删除好友");
            }

            if(!noticePopupWindows.isShowing()){
                noticePopupWindows.showAtLocation(mBinding.getRoot(), Gravity.CENTER,0,0);

                noticePopupWindows.getCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noticePopupWindows.dismiss();
                    }
                });

                noticePopupWindows.getSure().setOnClickListener(view1 -> {
                    data.remove(playerInfo);

                    String deleteFriendUid = playerInfo.getUid();

                    BmobQuery<FriendUidInfo> query1 = new BmobQuery<>();
                    query1.addWhereEqualTo("uid", SPUtils.getString(context,"uid"));
                    BmobQuery<FriendUidInfo> query2 = new BmobQuery<>();
                    query2.addWhereEqualTo("friendUid", deleteFriendUid);

                    List<BmobQuery<FriendUidInfo>> queryList = new ArrayList<>();
                    queryList.add(query1);
                    queryList.add(query2);

                    BmobQuery<FriendUidInfo> query = new BmobQuery<>();
                    query.and(queryList);
                    query.findObjects(new FindListener<FriendUidInfo>() {
                        @Override
                        public void done(List<FriendUidInfo> list, BmobException e) {
                            if(e==null){

                                if(list.size()!=0){

                                    for(FriendUidInfo friendUidInfo:list){
                                        String deleteObjectId = friendUidInfo.getObjectId();

                                        FriendUidInfo friendUidInfo1 = new FriendUidInfo();
                                        friendUidInfo1.setObjectId(deleteObjectId);
                                        friendUidInfo1.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    showToast("删除好友成功");
                                                }else{

                                                    showToast("删除好友失败");

                                                }
                                            }
                                        });
                                    }

                                }else{
                                    deleteFriendByOther(deleteFriendUid);
                                }

                            }else{
                                Log.e("bmob",e.toString());
                            }
                        }
                    });





                    notifyDataSetChanged();
                    noticePopupWindows.dismiss();
                });
            }

        });

        if(playerInfo.getUrl() == null){

            //无头像 设置默认头像
            baseViewHolder.setImageResource(R.id.friend_avatar,R.mipmap.room_hall_icon);

        }else{

            Glide.with(getContext())
                    .load(playerInfo.getUrl())
                    .dontAnimate()
                    .into((ImageView) baseViewHolder.getView(R.id.friend_avatar));

        }

        //通过为条目设置点击事件触发回调
        if (mOnItemClickLitener != null) {
            baseViewHolder.findView(R.id.friend_card).setOnClickListener(view -> mOnItemClickLitener.onItemClick(view,playerInfo));
        }

    }

    private void showToast(final String text) {
        mainHandler.post(() -> Toast.makeText(context, text, Toast.LENGTH_SHORT).show());
    }

    private void deleteFriendByOther(String deleteFriendUid){
        BmobQuery<FriendUidInfo> query11 = new BmobQuery<>();
        query11.addWhereEqualTo("uid", deleteFriendUid);
        BmobQuery<FriendUidInfo> query21 = new BmobQuery<>();
        query21.addWhereEqualTo("friendUid", SPUtils.getString(context,"uid"));

        List<BmobQuery<FriendUidInfo>> queryList1 = new ArrayList<>();
        queryList1.add(query11);
        queryList1.add(query21);

        BmobQuery<FriendUidInfo> query0 = new BmobQuery<>();
        query0.and(queryList1);
        query0.findObjects(new FindListener<FriendUidInfo>() {
            @Override
            public void done(List<FriendUidInfo> list, BmobException e) {
                if(e==null){

                    if(list.size()!=0){

                        for(FriendUidInfo friendUidInfo:list){
                            String deleteObjectId = friendUidInfo.getObjectId();

                            FriendUidInfo friendUidInfo1 = new FriendUidInfo();
                            friendUidInfo1.setObjectId(deleteObjectId);
                            friendUidInfo1.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        showToast("删除好友成功");
                                    }else{
                                        showToast("删除好友失败");
                                    }
                                }
                            });
                        }

                    }

                }else{
                    Log.e("bmob",e.toString());
                }
            }
        });
    }

}
