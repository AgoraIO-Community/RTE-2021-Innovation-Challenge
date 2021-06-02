package com.example.jialichun.client.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jialichun.client.R;

import java.io.ByteArrayOutputStream;


public class TakePictureActivity extends Activity {
    /**
     * 图片预览及展示
     */
    private SurfaceView surfaceView;
    /**
     * 拍照
     */
    private Button takePic;
    /**
     * 图片展示
     */
    private ImageView preViewPic;
    /**
     * 调用系统相机
     */
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_take_picture);
        /**
         * 窗口布满全局
         */
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_2);
        takePic = (Button) findViewById(R.id.take_pic);
        preViewPic = (ImageView) findViewById(R.id.pic_pre_view);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFixedSize(176, 155);
        holder.setKeepScreenOn(true);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new TakePictureSurfaceCallback());

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.takePicture(null, null, new TakePictureCallback());
                }
            }
        });
    }


    private final class TakePictureSurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(0);
                if (camera == null) {
                    int cametacount = Camera.getNumberOfCameras();
                    camera = Camera.open(cametacount - 1);
                }

                Camera.Parameters params = camera.getParameters();
                params.setJpegQuality(80);//照片质量
                params.setPictureSize(1024, 768);//图片分辨率
                params.setPreviewFrameRate(5);//预览帧率

                camera.setDisplayOrientation(90);
                /**
                 * 设置预显示
                 */
                        camera.setPreviewCallback(new Camera.PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data, Camera camera) {

                            }
                        });
                /**
                 * 开启预览
                 */
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }


    private final class TakePictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
            byte[] tempData = os.toByteArray();
            if (tempData != null && tempData.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(tempData, 0, tempData.length);
                preViewPic.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.GONE);
                preViewPic.setImageBitmap(bitmap);
            }
        }
    }
}