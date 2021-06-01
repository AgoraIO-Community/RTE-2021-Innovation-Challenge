package com.qgmodel.qggame.view.activity.mainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.ActivityMainBinding;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.view.MainActivity;
import com.qgmodel.qggame.view.activity.CourseActivity;
import com.qgmodel.qggame.view.adapter.PagerAdapter;
import java.util.ArrayList;

public class MainActivityImpl extends AppCompatActivity implements View.OnClickListener, MainActivity {
    private static final String TAG = "MainActivityImpl";

    private ActivityMainBinding mBinding;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private boolean isFirst;

    private static final int PERMISSION_REQ_ID = 6;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));

        initView();
        setContentView(mBinding.getRoot());

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
        }

    }


    private void initView() {

        boolean isFirstIn = false;
        if(isFirstIn) {

            //默认id九位整型
            uid = String.valueOf((int)(Math.random()*1000000000));
            SPUtils.setString(this,"uid",uid);

            //第一次进引导页
            Intent intent = new Intent(MainActivityImpl.this, CourseActivity.class);
            startActivity(intent);

            isFirst = false;

            SPUtils.setBoolean(this.getContext(), "isFirstIn", false);
        }else{
            initFragment();
        }

    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    @Override
    public ActivityMainBinding getBinding() {
        return mBinding;
    }

    @Override
    public void initFragment() {

        fragments.add(new GameFragment());
        fragments.add(new FriendFragment());

        PagerAdapter pagerAdapter = new PagerAdapter(this.getSupportFragmentManager(),fragments);
        mBinding.viewpager.setAdapter(pagerAdapter);

        mBinding.mainGameIcon.setOnClickListener(this);
        mBinding.mainFriendIcon.setOnClickListener(this);
        mBinding.mainTalkIcon.setOnClickListener(this);

        mBinding.mainTalkLayout.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.main_game_icon:
                mBinding.viewpager.setCurrentItem(0);
                break;

            case R.id.main_friend_icon:
                mBinding.viewpager.setCurrentItem(1);
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isFirst){
            initFragment();
            isFirst = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public Context getContext() {
        return this;
    }


}

