/*
 * Copyright (C) 2011 Baidu Inc. All rights reserved.
 */

package org.lql.movie_together.ui.face.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 这个类提供一些操作Bitmap的方法
 */
public final class BitmapUtils {
    /**
     * 图像的旋转方向是0
     */
    public static final int ROTATE0 = 0;
    /**
     * 图像的旋转方向是90
     */
    public static final int ROTATE90 = 90;
    /**
     * 图像的旋转方向是180
     */
    public static final int ROTATE180 = 180;
    /**
     * 图像的旋转方向是270
     */
    public static final int ROTATE270 = 270;
    /**
     * 图像的旋转方向是360
     */
    public static final int ROTATE360 = 360;
    /**
     * 图片太大内存溢出后压缩的比例
     */
    public static final int PIC_COMPRESS_SIZE = 4;
    /**
     * 图像压缩边界
     */
    public static final int IMAGEBOUND = 128;
    /**
     * 图片显示最大边的像素
     */
    public static final int MAXLENTH = 1024;
    /**
     * Log TAG
     */
    private static final String TAG = "ImageUtils";
    /**
     * Log switch
     */
    private static final boolean DEBUG = false;
    /**
     * 保存图片的质量：100
     */
    private static final int QUALITY = 100;
    /**
     * 默认的最大尺寸
     */
    private static final int DEFAULT_MAX_SIZE_CELL_NETWORK = 600;
    /**
     * 题编辑wifi环境下压缩的最大尺寸
     */
    private static final int QUESTION_MAX_SIZE_CELL_NETWORK = 1024;
    /**
     * 图片压缩的质量
     */
    private static final int QUESTION_IMAGE_JPG_QUALITY = 75;
    /**
     * 默认的图片压缩的质量
     */
    private static final int DEFAULT_IMAGE_JPG_QUALITY = 50;
    /**
     * 网络请求超时时间
     */
    private static final int CONNECTTIMEOUT = 3000;

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
    private BitmapUtils() {
    }

    /**
     * 得到要显示的图片数据
     */
    public static Bitmap createBitmap(byte[] data, float orientation) {
        Bitmap bitmap = null;
        Bitmap transformed = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        try {

            int width = 2000;
            int hight = 2000;
            int min = Math.min(width, hight);
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            opts.inSampleSize =
                    BitmapUtils.computeSampleSize(opts, min, BitmapUtils.MAXLENTH * BitmapUtils.MAXLENTH);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            transformed = BitmapUtils.rotateBitmap(orientation, bitmap);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            if (transformed != null && !transformed.isRecycled()) {
                transformed.recycle();
                transformed = null;
            }
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            opts.inSampleSize =
                    BitmapUtils.computeSampleSize(opts, -1, opts.outWidth * opts.outHeight
                            / BitmapUtils.PIC_COMPRESS_SIZE);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            transformed = BitmapUtils.rotateBitmap(orientation, bitmap);
        }
        if (transformed != bitmap && bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return transformed;

    }

    /**
     * 根据从数据中读到的方向旋转图片
     *
     * @param orientation 图片方向
     * @param bitmap      要旋转的bitmap
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(float orientation, Bitmap bitmap) {
        Bitmap transformed;
        Matrix m = new Matrix();
        if (orientation == 0) {
            transformed = bitmap;
        } else {
            m.setRotate(orientation);
            transformed = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        return transformed;
    }

    /**
     * 获取无损压缩图片合适的压缩比例
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = BitmapUtils.computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) { // SUPPRESS CHECKSTYLE
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8; // SUPPRESS CHECKSTYLE
        }
        return roundedSize;
    }

    /**
     * 获取无损压缩图片的压缩比
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                               int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? BitmapUtils.IMAGEBOUND : (int) Math.min(
                        Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 解析图片的旋转方向
     *
     * @param path 图片的路径
     * @return 旋转角度
     */
    public static int decodeImageDegree(String path) {
        int degree = ROTATE0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = ROTATE90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = ROTATE180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = ROTATE270;
                    break;
                default:
                    degree = ROTATE0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            degree = ROTATE0;
        }
        return degree;
    }

    /**
     * 等比压缩图片
     *
     * @param bitmap 原图
     * @param scale  压缩因子
     * @return 压缩后的图片
     */
    public static Bitmap scale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 尺寸缩放
     *
     * @param bitmap bitmap
     * @param w      width
     * @param h      height
     * @return scaleBitmap
     */
    public static Bitmap scale(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

    }

    /**
     * 等比压缩图片
     *
     * @param resBitmap 原图
     * @param desWidth  压缩后图片的宽度
     * @param desHeight 压缩后图片的高度
     * @return 压缩后的图片
     */
    public static Bitmap calculateInSampleSize(Bitmap resBitmap, int desWidth, int desHeight) {
        if (resBitmap == null) {
            return null;
        }
        int resWidth = resBitmap.getWidth();
        int resHeight = resBitmap.getHeight();
        if (resHeight > desHeight || resWidth > desWidth) {
            // 计算出实际宽高和目标宽高的比率
            final float heightRatio = (float) desHeight / (float) resHeight;
            final float widthRatio = (float) desWidth / (float) resWidth;
            float scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            return scale(resBitmap, scale);
        }
        return resBitmap;
    }


    public static Uri saveTakePictureImage(Context context, byte[] data) {
        File file = context.getExternalFilesDir("file1");
        file = new File(file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(data);
            fout.flush();
        } catch (Exception e) {
            e.printStackTrace();

            // 异常时删除保存失败的文件
            try {
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, file.getAbsolutePath());
        return Uri.fromFile(file);
    }

    public static Bitmap yuv2Bitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap depth2Bitmap(byte[] depthBytes, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] argbData = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            argbData[i] = (((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10 & 0x000000ff)
                    | ((((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10) & 0x000000ff) << 8
                    | ((((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10) & 0x000000ff) << 16
                    | 0xff000000;

        }
        bitmap.setPixels(argbData, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap rgb2Bitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 4 + 0] = b1;
            rgba[i * 4 + 1] = b2;
            rgba[i * 4 + 2] = b3;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    public static Bitmap bgr2Bitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 4 + 0] = b3;
            rgba[i * 4 + 1] = b2;
            rgba[i * 4 + 2] = b1;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    public static byte[] argb2R(byte[] bytes, int width, int height) {
        byte[] IR = new byte[width * height];
        for (int i = 0; i < width * height; i++) {
            IR[i] = bytes[i * 4];
        }
        return IR;
    }

    public static Bitmap getInstaceBmp(BDFaceImageInstance newInstance) {
        Bitmap transBmp = null;
        if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_RGBA) {
            transBmp = Bitmap.createBitmap(newInstance.width, newInstance.height, Bitmap.Config.ARGB_8888);
            transBmp.copyPixelsFromBuffer(ByteBuffer.wrap(newInstance.data));
        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_BGR) {
            transBmp = BitmapUtils.bgr2Bitmap(newInstance.data, newInstance.width, newInstance.height);
        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_YUV_NV21) {
            transBmp = BitmapUtils.yuv2Bitmap(newInstance.data, newInstance.width, newInstance.height);
        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_GRAY) {
            transBmp = depth2Bitmap(newInstance.data, newInstance.width, newInstance.height);
        }
        return transBmp;
    }

    /**
     * 获取图片数据
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        FileInputStream fis;
        Bitmap bm = null;
        try {
            fis = new FileInputStream(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 图片的长宽都是原来的1/8
            options.inSampleSize = 8;
            BufferedInputStream bis = new BufferedInputStream(fis);
            bm = BitmapFactory.decodeStream(bis, null, options);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static Bitmap base64ToBitmap(String base64String) {
        base64String = Uri.decode(base64String);
        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
