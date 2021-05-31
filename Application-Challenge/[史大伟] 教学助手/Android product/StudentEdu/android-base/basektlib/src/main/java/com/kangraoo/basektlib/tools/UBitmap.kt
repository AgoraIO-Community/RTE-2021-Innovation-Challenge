package com.kangraoo.basektlib.tools

import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.view.View
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import java.io.IOException

object UBitmap {
    private const val TAG = "UBitmap"
    fun reviewPicRotate(bitmap: Bitmap, path: String?): Bitmap {
        var bitmap = bitmap
        val degree = getPicRotate(path)
        if (degree != 0) {
            val m = Matrix()
            val width = bitmap.width
            val height = bitmap.height
            m.setRotate(degree.toFloat())
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true)
        }
        return bitmap
    }

    fun rotate(b: Bitmap, degrees: Int): Bitmap {
        var bCopy = b
        if (degrees != 0) {
            val m = Matrix()
            m.setRotate(degrees.toFloat(), bCopy.width.toFloat() / 2, bCopy.height.toFloat() / 2)
            try {
                val b2 = Bitmap.createBitmap(bCopy, 0, 0, bCopy.width, bCopy.height, m, true)
                if (bCopy != b2) {
                    bCopy.recycle() //Android开发网再次提示Bitmap操作完应该显示的释放
                    bCopy = b2
                }
            } catch (ex: OutOfMemoryError) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。
                ULog.e(ex)
                return b
            }
        }
        return bCopy
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     * 图片绝对路径
     * @return degree旋转的角度
     */
    fun getPicRotate(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path!!)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            ULog.e(e, e.message)
        }
        return degree
    }

    fun resizeBitmap(bitmap: Bitmap?, w: Int, h: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }
        val BitmapOrg: Bitmap = bitmap
        val width = BitmapOrg.width
        val height = BitmapOrg.height
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true)
    }

    /**
     * 获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth 所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    fun decodeSampledBitmapFromResource(
        res: Resources?,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        /**
         * 1.获取图片的像素宽高(不加载图片至内存中,所以不会占用资源)
         * 2.计算需要压缩的比例
         * 3.按将图片用计算出的比例压缩,并加载至内存中使用
         */
        // 首先不加载图片,仅获取图片尺寸
        val options = BitmapFactory.Options()
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeResource(res, resId, options)

        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false
        options.inScaled = false
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * 计算压缩比例值
     *
     * @param options 解析图片的配置信息
     * @param reqWidth 所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // 保存图片原宽高值
        val height = options.outHeight
        val width = options.outWidth
        // 初始化压缩比例为1
        var inSampleSize = 1

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 将view生成图片输出到path
     * @param view
     * @param imagePath
     * @return
     */
    fun getImage(view: View, imagePath: String): Bitmap {
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        view.drawingCacheBackgroundColor = Color.WHITE
        val cachebmp = loadBitmapFromView(view)
        AttachmentStore.saveBitmap(cachebmp, imagePath, false)
        view.destroyDrawingCache()
        return cachebmp
    }

    private fun loadBitmapFromView(v: View): Bitmap {
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }
}
