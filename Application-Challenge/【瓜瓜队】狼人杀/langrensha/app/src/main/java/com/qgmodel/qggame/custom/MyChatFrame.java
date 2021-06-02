package com.qgmodel.qggame.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.qgmodel.qggame.R;

public class MyChatFrame extends RelativeLayout {


    public MyChatFrame(Context context) {
        this(context,null);
    }

    public MyChatFrame(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyChatFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflateView = LayoutInflater.from(context).inflate(R.layout.my_chat_frame,null,false);
        addView(inflateView);
        init();
    }

    private void init() {

    }


}
