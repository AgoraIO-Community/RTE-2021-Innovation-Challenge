package com.kangaroo.studentedu.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.studentedu.R;
import kotlinx.android.synthetic.main.activity_bu_zhi_home_work_detail.*
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import android.content.DialogInterface
import com.kangraoo.basektlib.tools.glide.GlideApp
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.widget.dialog.LibCheckDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.qdedu.base_module_picselect.image.tools.applyPictureConfigAndStart
import kotlinx.android.synthetic.main.activity_bu_zhi_home_work_detail.button
import kotlinx.android.synthetic.main.activity_bu_zhi_home_work_detail.img

/**
 * 自动生成：by WaTaNaBe on 2021-05-27 13:20
 * #布置作业详情#
 */
class BuZhiHomeWorkDetailActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, BuZhiHomeWorkDetailActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_bu_zhi_home_work_detail


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "布置作业详情"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})
        img.setOnClickListener {
            PictureSelector.create(visitActivity()).applyPictureConfigAndStart(
                PictureSelectionConfig().apply {
                    maxSelectNum = 1 // 最大图片选择数量
                    minSelectNum = 1 // 最小选择数量
                    imageSpanCount = 4 // 每行显示个数
                    selectionMode = PictureConfig.MULTIPLE // 多选 or 单选
                    enablePreview = true // 是否可预览图片
                    isCamera = true // 是否显示拍照按钮
                    enableCrop = true // 是否裁剪
                    isCompress = true // 是否压缩
                    hideBottomControls = true // 是否显示uCrop工具栏，默认不显示
                    showCropFrame = true // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    showCropGrid = true // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    openClickSound = false // 是否开启点击声音
                    cropCompressQuality = 100 // 裁剪压缩质量 默认100
                    minimumCompressSize = 100 // 小于100kb的图片不压缩
                    rotateEnabled = false // 裁剪是否可旋转图片
                    scaleEnabled = true // 裁剪是否可放大缩小图片
                    circleDimmedLayer = false // 是否圆形裁剪
                    cropWidth = 0// 裁剪宽度
                    cropHeight = 0// 裁剪高度
                    aspect_ratio_x = 0  // 裁剪X比例
                    aspect_ratio_y  = 0// 裁剪Y比例
                },object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        if(result!=null&&result.size>0){
                            GlideApp.with(visitActivity()).load(result[0].path).into(img)
                        }
                    }

                    override fun onCancel() {
                    }

                })
        }
        val libCheckDialog = LibCheckDialog(visitActivity())

        libCheckDialog.title("提示")
        libCheckDialog.content("确定发布作业吗")
        libCheckDialog.sureVisable(View.VISIBLE)
        libCheckDialog.cancleVisable(View.VISIBLE)
        libCheckDialog.sure("确定")
        libCheckDialog.cancle("取消")
        libCheckDialog.onLibDialogListener =
            (object : LibCheckDialog.OnLibCheckDialogListener {
                override fun onSure() {
                    showToastMsg("点击确定")
                    libCheckDialog.dismiss()
                    showToastMsg(Tip.Success,"作业已发布")
                    finish()
                }

                override fun onCancle() {
                }

                override fun onShow() {
                }

                override fun onDismiss(dialog: DialogInterface) {
                }

            })
        button.setOnClickListener {
            libCheckDialog.show()

        }
    }

}
