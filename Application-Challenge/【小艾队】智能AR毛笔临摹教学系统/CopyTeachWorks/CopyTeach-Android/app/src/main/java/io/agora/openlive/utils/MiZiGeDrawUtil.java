package io.agora.openlive.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class MiZiGeDrawUtil extends View {

    private Paint mPaint = null;
    private int width;
    private int height;

    public MiZiGeDrawUtil(Context context) {
        super(context);
    }

    public MiZiGeDrawUtil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiZiGeDrawUtil(Context context, int width , int height) {
        super(context);
        this.width = width;
        this.height = height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(Color.RED);
        canvas.drawRect(new Rect(0, (height - width) / 2, width, (height + width) / 2), mPaint);
        mPaint.setStrokeWidth(5);
        mPaint.setPathEffect(new DashPathEffect(new float[] {20, 20}, 0));
        canvas.drawLine(0, (height - width) / 2, width, (height + width) / 2, mPaint);
        canvas.drawLine(width / 2, (height - width) / 2, width / 2, (height + width) / 2, mPaint);
        canvas.drawLine(width, (height - width) / 2, 0, (height + width) / 2, mPaint);
        canvas.drawLine(0, height / 2, width, height / 2, mPaint);
    }
}
