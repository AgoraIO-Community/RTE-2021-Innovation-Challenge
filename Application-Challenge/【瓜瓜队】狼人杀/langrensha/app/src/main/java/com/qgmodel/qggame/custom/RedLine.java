package com.qgmodel.qggame.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

public class RedLine extends View {
    private static final String TAG = "RedLine";

    private Paint paint;
    int Yoffset = 30;
    int Xoffset = 60;

    public RedLine(Context context) {
        this(context,null);
    }

    public RedLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RedLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ininPaint();
    }

    private void ininPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor("#FF6868"));
        paint.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "=== width--> "+getWidth());
        Log.d(TAG, "=== height--> "+getHeight());
        canvas.drawLine(Xoffset,Yoffset,getWidth()-Xoffset,getHeight()-Yoffset,paint);
    }



}
