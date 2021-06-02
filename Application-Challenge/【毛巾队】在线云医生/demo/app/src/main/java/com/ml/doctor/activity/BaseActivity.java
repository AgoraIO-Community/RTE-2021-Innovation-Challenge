package com.ml.doctor.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ml.doctor.R;
import com.ml.doctor.dialog.LoadingDialog;


public class BaseActivity extends FragmentActivity {
    protected LayoutInflater mInflater;
    protected Resources mResources;
    protected Context mContext;

    private LinearLayout rootView;
    private View mTitleView;
    private TextView mTitleText;
    private TextView mRightText;
    private ImageView mLeftView;
    private ImageView mRightView;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInflater = LayoutInflater.from(this);
        mResources = getResources();
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);
        rootView.addView(mTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (50 * mResources.getDisplayMetrics().density)));
//        rootView.setBackgroundResource();
        initTitleView();
        hideLeftImg(mLeftView);
    }

    @Override
    public void setContentView(int layoutResID) {
        mInflater.inflate(layoutResID, rootView);
        super.setContentView(rootView);
    }

    @Override
    public void setContentView(View view) {
        rootView.addView(view);
        super.setContentView(rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    private void initTitleView(){
        mTitleText = (TextView) mTitleView.findViewById(R.id.tv_top_title);
        mRightText = (TextView) mTitleView.findViewById(R.id.tv_top_right);
        mLeftView = (ImageView) mTitleView.findViewById(R.id.iv_top_left);
        mRightView = (ImageView) mTitleView.findViewById(R.id.iv_top_right);
        mLeftView.setOnClickListener(mTitleClickListener);
        mRightText.setOnClickListener(mTitleClickListener);
        mRightView.setOnClickListener(mTitleClickListener);
    }

    private View.OnClickListener mTitleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_top_right:
                    onRightTextClick();
                    break;
                case R.id.iv_top_left:
                    onLeftViewClick();
                    break;
                case R.id.iv_top_right:
                    onRightViewClick();
                    break;
            }
        }
    };

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    public void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, message);
        }
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected void setBackResource(int resId){
        rootView.setBackgroundResource(resId);
    }

    protected void setTopTitle(int resId){
        mTitleText.setText(getString(resId));
    }

    protected void setTopTitle(String res){
        mTitleText.setText(res);
    }

    protected void setRightText(int resId){
        mRightText.setText(getString(resId));
    }

    protected void setRightText(String res){
        mRightText.setText(res);
    }

    protected void setRightView(int resId){
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(resId);
    }

    protected void hideTopTitle(){
        mTitleView.setVisibility(View.GONE);
    }

    protected void onRightTextClick(){}

    protected void onRightViewClick(){}

    protected void onLeftViewClick(){
        finish();
    }
    protected void hideLeftImg(ImageView mLeftImage){}

}
