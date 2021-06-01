package com.qgmodel.qggame.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.io.Serializable;

public class MyRelativeLayout extends RelativeLayout implements Serializable {
    public MyRelativeLayout(Context context) {
        this(context,null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
