package com.qgmodel.qggame.view.activity.mainActivity;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.DialogNoticeBinding;
import com.qgmodel.qggame.databinding.FragmentGameBinding;
import com.qgmodel.qggame.entity.PlayerInfo;
import com.qgmodel.qggame.entity.RoomInfo;
import com.qgmodel.qggame.model.Model;
import com.qgmodel.qggame.model.RoomModel;
import com.qgmodel.qggame.utils.GlideImageLoader;
import com.qgmodel.qggame.utils.NoticePopupWindows;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.view.activity.CallActivity;
import com.qgmodel.qggame.view.activity.CourseActivity;
import com.qgmodel.qggame.view.activity.LobbyActivity;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class GameFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "GameFragment";

    private FragmentGameBinding mBinding;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private NoticePopupWindows noticePopupWindows;
    private NoticePopupWindows errorPopupWindows;
    private NoticeTPopupWindows noticeTPopupWindows;

    private String uid;
    private PlayerInfo nowPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container,false);

        uid = SPUtils.getString(this.getContext(),"uid");

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        initNowPlayerDetail();
        initBanner();
        setViewClick();
    }



    private void setViewClick() {
        mBinding.mainRoomHall.setOnClickListener(this);
        mBinding.mainNotice.setOnClickListener(this);
        mBinding.mainFastReady.setOnClickListener(this);
    }

    private void initNowPlayerDetail() {

        boolean isFirst = false;
        //???????????????????????? ???true

        if(isFirst){

            initPopWindow();//????????????id???????????????

        }else{

        }

    }






    private void initPopWindow() {

        //??????????????????false
        SPUtils.setBoolean(this.getContext(),"isFirst",false);

        if(noticePopupWindows == null){

            noticePopupWindows = new NoticePopupWindows(this.getContext());
            noticePopupWindows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noticePopupWindows.setFocusable(true);
            noticePopupWindows.setEditDissmiss(false);
            noticePopupWindows.setTopTv("????????????");
            noticePopupWindows.setContentTv("????????????????????????");
            noticePopupWindows.getCancel().setVisibility(View.GONE);

            noticePopupWindows.showAtLocation(mBinding.getRoot(), Gravity.CENTER,0,0);

        }


        noticePopupWindows.getSure().setOnClickListener(view -> {

            String nowName = String.valueOf(noticePopupWindows.getTextBox().getText());

            if(!nowName.isEmpty()){

                SPUtils.setString(this.getContext(),"name",nowName);

                nowPlayer = new PlayerInfo(nowName,uid);
                SPUtils.setString(this.getContext(),"url",nowPlayer.getUrl());


                nowPlayer.save(new SaveListener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            showToast("????????????");
                            SPUtils.setString(getContext(),"objectId",objectId);//??????objectId
                        }else{
                            showToast("????????????");
                            SPUtils.setBoolean(getContext(),"isFirstIn",false);
                            SPUtils.setBoolean(getContext(),"isFirst",false);

                            noticePopupWindows.close();

                            if(errorPopupWindows == null){
                                errorPopupWindows = new NoticePopupWindows(getContext());
                                errorPopupWindows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                errorPopupWindows.setFocusable(true);
                                errorPopupWindows.setEditDissmiss(false);

                                errorPopupWindows.setTopTv("??????????????????");
                                errorPopupWindows.setContentTv("?????????????????????????????????");

                                errorPopupWindows.getCancel().setVisibility(View.GONE);
                                errorPopupWindows.getTextLayout().setVisibility(View.GONE);
                                errorPopupWindows.showAtLocation(mBinding.getRoot(), Gravity.CENTER,0,0);

                                errorPopupWindows.getSure().setOnClickListener(view1 -> android.os.Process.killProcess(android.os.Process.myPid()));
                            }



                            Log.e("bmob","?????????"+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                noticePopupWindows.close();
            }else{
                showToast("??????????????????");
            }


        });


    }

    /*?????????*/
    private void initBanner() {

        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.room_hall_icon);
        images.add(R.mipmap.notice_icon);
        images.add(R.mipmap.fast_ready_icon);

        //???????????????
        mBinding.mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default)
                .setImages(images)
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setDelayTime(4000);

        mBinding.mainBanner.start();
    }


    @Override
    public void onClick(View view) {

        // TODO: 2020/8/11 ???????????????????????????????????? ????????????
        switch (view.getId()){
            case R.id.main_room_hall:
                Intent intentLobby = new Intent(getContext(), LobbyActivity.class);
                startActivity(intentLobby);
                break;

            case R.id.main_notice:
                NoticeTPopupWindows windows = new NoticeTPopupWindows(getContext());
                windows.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                windows.showAtLocation(mBinding.getRoot(), Gravity.CENTER,0,0);
                break;

            case R.id.main_fast_ready:

                try {
                    Model.ROOM.getAll(new RoomModel.OnGetAll() {
                        @Override
                        public void all(List<RoomInfo> rooms) {
                            if (rooms == null || rooms.size() == 0) {
                                Log.d(TAG, "=== ???????????????");
                                RoomInfo roomInfo = new RoomInfo();
                                roomInfo.setPlayer(1);
                                roomInfo.setRoomerId(SPUtils.getString(getContext(), "uid"));
                                roomInfo.setTitle("???????????????~");
                                roomInfo.setChannelName(System.currentTimeMillis() + "");
                                roomInfo.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Intent callIntent = new Intent(getActivity(), CallActivity.class);
                                            callIntent.putExtra("myId", Integer.parseInt(uid));
                                            callIntent.putExtra("videoChannel", roomInfo.getChannelName());
                                            callIntent.putExtra("roomerId", roomInfo.getRoomerId());
                                            callIntent.putExtra("isRoomer", true);
                                            callIntent.putExtra("voiceRecognition",true);
                                            startActivity(callIntent);
                                        } else {
                                            Log.d(TAG, " ==== $$$$$$$$$$ e---> "+e);
                                            BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
                                            query.addWhereEqualTo("channelName", roomInfo.getChannelName());
                                            query.findObjects(new FindListener<RoomInfo>() {
                                                @Override
                                                public void done(List<RoomInfo> list, BmobException e) {
                                                    if (list == null || list.size() == 0) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        RoomInfo roomInfo = list.get(0);
                                                        Intent callIntent = new Intent(getActivity(), CallActivity.class);
                                                        callIntent.putExtra("myId", Integer.parseInt(uid));
                                                        callIntent.putExtra("videoChannel", roomInfo.getChannelName());
                                                        callIntent.putExtra("roomerId", roomInfo.getRoomerId());
                                                        callIntent.putExtra("isRoomer", true);
                                                        callIntent.putExtra("voiceRecognition",true);
                                                        startActivity(callIntent);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "=== ????????????");
                                RoomInfo roomInfo = rooms.get(0);
                                if(roomInfo.getPlayer() < RoomInfo.MAX_PLAYER){
                                    Intent callIntent = new Intent(getActivity(), CallActivity.class);
                                    callIntent.putExtra("myId", Integer.parseInt(uid));
                                    callIntent.putExtra("videoChannel", roomInfo.getChannelName());
                                    callIntent.putExtra("roomerId", roomInfo.getRoomerId());
                                    callIntent.putExtra("voiceRecognition",true);
                                    startActivity(callIntent);
                                }else{
                                    Toast.makeText(getContext(), "?????????????????????",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("??????????????????");
                }

                break;
        }

    }

    private void showToast(final String text) {
        mainHandler.post(() -> Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}

class NoticeTPopupWindows extends PopupWindow {

    private DialogNoticeBinding mBinding;

    private boolean isDissmiss = true;

    @Override
    public void dismiss() {
        if(isDissmiss){
            super.dismiss();
        }
    }

    public void close(){
        super.dismiss();
    }

    public void  setEditDissmiss(boolean is){
        isDissmiss = is;//????????????
    }



    public ImageView getCancel(){
        return mBinding.noticeCancelIcon;
    }

    public Button getSure(){
        return mBinding.noticeContentSure;
    }

    public void setTopTv(String topTv){
        try {
            mBinding.noticeTopText.setText(topTv);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public NoticeTPopupWindows(Context context) {
        super(context);
        mBinding = DialogNoticeBinding.inflate(LayoutInflater.from(context));

        mBinding.noticeContentSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        mBinding.noticeCancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setContentView(mBinding.getRoot());
    }

}
