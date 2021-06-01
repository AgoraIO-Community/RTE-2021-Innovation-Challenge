package com.qgmodel.qggame.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.qgmodel.qggame.R;

public class MyCardView extends RelativeLayout {

    private Integer tag;

    public MyCardView(Context context) {
        this(context,null);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflateView = LayoutInflater.from(getContext()).inflate(R.layout.my_card_view,null,false);
        addView(inflateView);
    }

    @Override
    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }
}
