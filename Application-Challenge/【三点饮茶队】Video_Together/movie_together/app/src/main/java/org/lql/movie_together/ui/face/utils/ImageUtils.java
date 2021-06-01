/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageUtils {

    public static Uri geturi(android.content.Intent intent, Context context) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/*"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                    Log.e("qing", "");
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    public static void resize(Bitmap bitmap, File outputFile, int maxWidth, int maxHeight) {
        try {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 图片大于最大高宽，按大的值缩放
            if (bitmapWidth > maxHeight || bitmapHeight > maxWidth) {
                float widthScale = maxWidth * 1.0f / bitmapWidth;
                float heightScale = maxHeight * 1.0f / bitmapHeight;

                float scale = Math.min(widthScale, heightScale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            }

            // save image
            FileOutputStream out = new FileOutputStream(outputFile);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rgbToBitmap(byte[] bytes, int width, int height) {
        Bitmap stitchBmp = null;
        try {
            // use Bitmap.Config.ARGB_8888 instead of type is OK
            stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
        } catch (Exception e) {
            Log.e("qing", String.valueOf(e.getStackTrace()));
        }
        return stitchBmp;
    }


    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // 使用后乘
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }


    // 视频流状态下持续转换出现内存泄漏现象暂停使用
    public static Bitmap yuv420spToBitmap(byte[] data, int width, int height) {
        Bitmap bitmap = null;
        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 100, stream);
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap getMirrorBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        Matrix matrix = new Matrix();
        // 镜像水平翻转
        matrix.postScale(-1, 1);
        Bitmap new2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(),
                new2.getHeight()), new Rect(0, 0, width, height), null);
        return new2;
    }
}
