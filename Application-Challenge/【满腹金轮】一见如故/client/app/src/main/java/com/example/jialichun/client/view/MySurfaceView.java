package com.example.jialichun.client.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by jialichun on 2018/3/28.
 */

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {
    private static final String TAG = "Kintai";

    private static SurfaceHolder holder;
    private Camera mCamera;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "new View ...");

        holder = getHolder();//后面会用到！
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceCreated...");
        if (mCamera == null) {
            mCamera = Camera.open(1);//开启相机，可以放参数 1 或 0，分别代表前置、后置摄像头，默认为 0
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);//整个程序的核心，相机预览的内容放在 holder
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    @Override
//    public void draw(Canvas canvas) {
//        Log.e("onDraw", "draw: test");
//        Path path = new Path();
//        //设置裁剪的圆心，半径
//        path.addCircle(300, 300, 300, Path.Direction.CCW);
//        //裁剪画布，并设置其填充方式
//        canvas.clipPath(path, Region.Op.REPLACE);
//        super.draw(canvas);
//    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.i(TAG, "surfaceChanged...");
        mCamera.startPreview();//该方法只有相机开启后才能调用
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceChanged...");
        if (mCamera != null) {
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }

}