package com.example.jialichun.client.view;

/**
 * Created by jialichun on 2018/2/25.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.example.jialichun.client.R;


/**
 * 圆角背景控件
 */
public class CircleRec extends android.support.v7.widget.AppCompatTextView {

    public int getDimen720Px(Context context, int dimen) {
        float dp = dimen * 1080f / 720 / 3;
        return dip2px(context, dp);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    Paint paint;
    int borderWidth = 1;// 默认1dimpx
    boolean isHasBorder = false;

    int borderColor;// 线条的颜色，默认与字的颜色相同
    int bgColor;// 背景的颜色，默认是透明的
    int mRadius = 3;// 默认3

    private RectF rectf = new RectF();// 方角

    // 四个角落是否是全是圆角
    boolean isTopLeftCorner = true;
    boolean isBottomLeftCorner = true;
    boolean isTopRightCorner = true;
    boolean isBottomRightCorner = true;

    public CircleRec(Context context, AttributeSet attrs) {
        super(context, attrs);

        borderWidth = dip2px(context, borderWidth);
        mRadius = dip2px(context, mRadius);
        borderColor = getCurrentTextColor();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCornerBgView);
        isHasBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBorder, isHasBorder);// 默认无边框
        borderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appBorderWidth, borderWidth);
        mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appRadius, mRadius);

        borderColor = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBorderColor, borderColor);

        bgColor = isHasBorder ? Color.TRANSPARENT : Color.RED;

        bgColor = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBgColor, bgColor);
        // 四个角落是否全是圆角,默认全是真的
        isTopLeftCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appTopLeftCorner, isTopLeftCorner);
        isBottomLeftCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBottomLeftCorner, isBottomLeftCorner);
        isTopRightCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appTopRightCorner, isTopRightCorner);
        isBottomRightCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBottomRightCorner, isBottomRightCorner);
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为无锯齿
        this.setGravity(Gravity.CENTER);// 全部居中显示
    }

    // 修正画圆角矩形的位置
    private void changeRectF(RectF rectF) {
        int half = borderWidth / 2;
        rectF.top += half;
        rectF.left += half;
        rectF.bottom -= half;
        rectF.right -= half;
    }

    protected void onDraw(Canvas canvas) {
        if (width == 0) // 没初始化完成不需要绘制
            return;
        // 先画背景
        if (bgColor != Color.TRANSPARENT) {// 透明就不用画了
            paint.setColor(bgColor); // 设置画笔颜色
            paint.setStyle(Paint.Style.FILL);
            rectf.left = 0; // 左边
            rectf.top = 0; // 上边
            rectf.right = width; // 右边
            rectf.bottom = height; // 下边
            //有边框的时候较正一下
            if(isHasBorder)
                changeRectF(rectf);
            canvas.drawRoundRect(rectf, mRadius, mRadius, paint); // 绘制圆角矩形
            fillCorner(canvas);
        }
        // 有边框
        if (isHasBorder) {
            paint.setColor(borderColor); // 设置画笔颜色
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            // 画圆角
            if (isTopLeftCorner && isTopRightCorner && isBottomLeftCorner && isBottomRightCorner) {
                rectf.left = 0; // 左边
                rectf.top = 0; // 上边
                rectf.right = width; // 右边
                rectf.bottom = height; // 下边
                changeRectF(rectf);
                canvas.drawRoundRect(rectf, mRadius, mRadius, paint); // 绘制圆角矩形
            } else {// 画路径
                Path path = getWantPath();
                canvas.drawPath(path, paint);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void fillCorner(Canvas canvas) {
        int half = 0;
        // 拿背景去填充，不需要圆角的地方
        // 左上角
        if (isTopLeftCorner == false) {
            rectf.left = half; // 左边
            rectf.top = half; // 上边
            rectf.right = mRadius;
            rectf.bottom = mRadius;
            canvas.drawRect(rectf, paint);
        }
        // 左下角
        if (isBottomLeftCorner == false) {
            rectf.right = mRadius;
            rectf.top = height - mRadius;
            rectf.left = half;
            rectf.bottom = height - half;
            canvas.drawRect(rectf, paint);
        }
        // 右上角
        if (isTopRightCorner == false) {
            rectf.right = width - half;
            rectf.left = width - mRadius; // 左边
            rectf.top = half; // 上边
            rectf.bottom = mRadius; // 下边
            canvas.drawRect(rectf, paint);
        }
        // 右下角
        if (isBottomRightCorner == false) {
            rectf.left = width - mRadius; // 左边
            rectf.top = height - mRadius; // 上边
            rectf.right = width - half; // 右边
            rectf.bottom = height - half; // 下边
            canvas.drawRect(rectf, paint);
        }
    }

    private Path getWantPath() {
        Path path = new Path();
        float half = borderWidth / 2;
        path.moveTo(half, mRadius + half);

        if (isTopLeftCorner) {// 左上角是圆角
            path.arcTo(new RectF(half, half, 2 * mRadius + half, 2 * mRadius + half), 180, 90);
        } else {
            path.lineTo(half, half);
            path.lineTo(mRadius, half);
        }
        path.lineTo(width - mRadius - half, half);
        if (isTopRightCorner) {
            path.arcTo(new RectF(width - 2 * mRadius - half, half, width - half, 2 * mRadius + half), -90, 90);
        } else {
            path.lineTo(width - half, half);
            path.lineTo(width - half, mRadius + half);
        }
        path.lineTo(width - half, height - mRadius - half);
        if (isBottomRightCorner) {
            path.arcTo(new RectF(width - 2 * mRadius - half, height - 2 * mRadius - half, width - half, height - half), 0, 90);
        } else {
            path.lineTo(width - half, height - half);
            path.lineTo(width - mRadius - half, height - half);
        }
        path.lineTo(mRadius + half, height - half);
        if (isBottomLeftCorner) {
            path.arcTo(new RectF(half, height - 2 * mRadius - half, 2 * mRadius + half, height - half), 90, 90);
        } else {
            path.lineTo(half, height - half);
            path.lineTo(half, height - mRadius - half);
        }
        path.close();
        return path;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    int width, height;
}