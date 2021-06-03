package com.game.tingshuo.pop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.lxj.xpopup.core.BottomPopupView
import com.game.tingshuo.BuildConfig
import com.game.tingshuo.R
import com.game.tingshuo.util.RxPermissionsUtil
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.pop_my_bottom_demo.view.*

/**
 * 调用方式: XPopup.Builder(this).asCustom(AddPicPopup(this)).show()
 */
class MyBottomPopup(context: Activity) : BottomPopupView(context) {
    val REQUEST_CODE_CHOOSE = 1001
    private var mContext = context
    override fun getImplLayoutId(): Int {
        return R.layout.pop_my_bottom_demo
    }

    override fun onCreate() {
        super.onCreate()
        btn_cancle.setOnClickListener { this@MyBottomPopup.dismiss() }
        //知乎matisse图片选择
        tv_gallery.setOnClickListener {
            RxPermissionsUtil.requestPermission(mContext as FragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA) {
                Matisse.from(mContext)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .captureStrategy(CaptureStrategy(true,"${BuildConfig.APPLICATION_ID}.fileprovider"))
                    .countable(true)
                    .maxSelectable(5)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
                    .showPreview(false) // Default is `true`
                    .forResult(REQUEST_CODE_CHOOSE)
            }
            this@MyBottomPopup.dismiss()
        }
        tv_take_photo.setOnClickListener {
            takePhoto()
            this@MyBottomPopup.dismiss()
        }


    }

    //拍照
    private fun takePhoto() {
        RxPermissionsUtil.requestPermission(mContext as FragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA) {
            var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
                var currPath= context.filesDir.path+ "/" + System.currentTimeMillis() + ".jpg"
                LogUtils.e("路径:$currPath")
                var iscreated = FileUtils.createFileByDeleteOldFile(currPath)
                var photoFile = FileUtils.getFileByPath(currPath)
                var photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) //指定了存储的路径的uri,intent返回的data就为null,这是一种通用做法,适配各类机型
                mContext.startActivityForResult(takePictureIntent, 1003)
                //EventBus.getDefault().post(TakePhotoEvent(currPath))
            }
        }
    }


}