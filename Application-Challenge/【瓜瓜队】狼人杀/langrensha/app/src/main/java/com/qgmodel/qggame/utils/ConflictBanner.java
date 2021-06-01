package com.qgmodel.qggame.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.youth.banner.Banner;

public class ConflictBanner extends Banner {

    public ConflictBanner(Context context) {
        super(context);
    }

    public ConflictBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ConflictBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);  //设置不拦截
        return super.dispatchTouchEvent(ev);
    }

}
