/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.TextureView;
import android.widget.FrameLayout;

/**
 * 基于 系统TextureView实现的预览View。
 *
 * @Time: 2019/1/28
 * @Author: v_chaixiaogang
 */
public class AttrbuteAutoTexturePreviewView extends FrameLayout {

    public TextureView textureView;

    private int videoWidth = 0;
    private int videoHeight = 0;


    public static int previewWidth = 0;
    private int previewHeight = 0;
    private static int scale = 2;

    public static float circleRadius;
    public static float circleX;
    public static float circleY;
    public boolean isDraw = false;
    private boolean mIsRegister;   // 注册

    private float[] pointXY = new float[3];

    public AttrbuteAutoTexturePreviewView(Context context) {
        super(context);
        init();
    }

    public AttrbuteAutoTexturePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttrbuteAutoTexturePreviewView(Context context, AttributeSet attrs,
                                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Handler handler = new Handler(Looper.getMainLooper());

    private void init() {
        setWillNotDraw(false);
        textureView = new TextureView(getContext());
        addView(textureView);
    }

    public void setIsRegister(boolean isRegister) {
        mIsRegister = isRegister;
        invalidate();
    }

    public boolean getIsRegister() {
        return mIsRegister;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        previewWidth = getWidth();
        previewHeight = getHeight();

        if (videoWidth == 0 || videoHeight == 0 || previewWidth == 0 || previewHeight == 0) {
            return;
        }

        if (previewWidth * videoHeight > previewHeight * videoWidth) {
            int scaledChildHeight = videoHeight * previewWidth / videoWidth;
            textureView.layout(0, (previewHeight - scaledChildHeight) / scale,
                    previewWidth, (previewHeight + scaledChildHeight) / scale);
        } else {
            int scaledChildWidth = videoWidth * previewHeight / videoHeight;
            textureView.layout((previewWidth - scaledChildWidth) / scale, 0,
                    (previewWidth + scaledChildWidth) / scale, previewHeight);

        }


    }

    public TextureView getTextureView() {
        return textureView;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewSize(int width, int height) {
        if (this.videoWidth == width && this.videoHeight == height) {
            return;
        }
        this.videoWidth = width;
        this.videoHeight = height;
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (isDraw) {
            Path path = new Path();
            // 设置裁剪的圆心坐标，半径
            path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, Path.Direction.CCW);
            // 裁剪画布，并设置其填充方式
            canvas.clipPath(path, Region.Op.REPLACE);

            // 圆的半径
            circleRadius = getWidth() / 3;
            // 圆心的X坐标
            circleX = (getRight() - getLeft()) / 2;
            // 圆心的Y坐标
            circleY = (getBottom() - getTop()) / 2;
        }

        if (mIsRegister) {
            Path path = new Path();
            // 设置裁剪的圆心坐标，半径
            path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, Path.Direction.CCW);
            // 裁剪画布，并设置其填充方式
            canvas.clipPath(path, Region.Op.REPLACE);
            // 圆的半径
            circleRadius = getWidth() / 3;
            // 圆心的X坐标
            circleX = (getRight() - getLeft()) / 2;
            // 圆心的Y坐标
            circleY = (getBottom() - getTop()) / 2;
        }
        super.onDraw(canvas);
    }
}
