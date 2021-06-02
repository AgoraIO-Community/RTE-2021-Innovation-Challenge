package com.qgmodel.qggame.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.qgmodel.qggame.databinding.NoticePopwindowBinding;

public class NoticePopupWindows extends PopupWindow {

    private NoticePopwindowBinding mBinding;

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
        isDissmiss = is;//控制开关
    }

    public EditText getTextBox(){
        return mBinding.noticeContentTextbox;
    }

    public RelativeLayout getTextLayout(){return mBinding.textlayout;}

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

    public void setContentTv(String content){
        try {
            mBinding.noticeContentText.setText(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public NoticePopupWindows(Context context) {
        super(context);
        mBinding = NoticePopwindowBinding.inflate(LayoutInflater.from(context));

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setContentView(mBinding.getRoot());
    }

}