package com.qdedu.base_module_picselect.image.tools

import android.app.Activity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.qdedu.base_module_picselect.R
import com.qdedu.base_module_picselect.image.GlideEngine

/**
 * description: PictureSelector
 * author: liping
 * date: 2021/3/15 14:31
 */

fun pictureSelectorConfig() : PictureSelectionConfig = PictureSelectionConfig().apply {
    maxSelectNum = 6 // 最大图片选择数量
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
}

fun videoSelectorConfig() : PictureSelectionConfig = PictureSelectionConfig().apply {
    maxSelectNum = 1         // 最大选择数量
    minSelectNum = 1         // 最小选择数量
    imageSpanCount = 4         // 每行显示个数
    selectionMode = PictureConfig.SINGLE         // 多选 or 单选
    enablePreview = true         // 是否可预览
    isCamera = false         // 是否显示拍照按钮
    openClickSound = false         // 是否开启点击声音
}

fun takeVideoConfig() : PictureSelectionConfig = PictureSelectionConfig().apply {
    recordVideoSecond = 60 // 默认录制时长60秒
}

/**
 * 从相册选择
 */
fun PictureSelector.applyPictureConfigAndStart(config: PictureSelectionConfig? = null, callbackListener: OnResultCallbackListener<LocalMedia>){
    var config: PictureSelectionConfig? = config
    if (config == null) {
        config = pictureSelectorConfig()
    }
    openGallery(PictureMimeType.ofImage())
        .imageEngine(GlideEngine.instance)
        .theme(R.style.picture_custom_style)
        .maxSelectNum(config.maxSelectNum)// 最大图片选择数量
        .minSelectNum(config.minSelectNum)// 最小选择数量
        .imageSpanCount(config.imageSpanCount)// 每行显示个数
        .selectionMode(config.selectionMode)// 多选 or 单选
        .isPreviewImage(config.enablePreview)// 是否可预览图片
        .isCamera(config.isCamera)// 是否显示拍照按钮
        .isEnableCrop(config.enableCrop)// 是否裁剪
        .isCompress(config.isCompress)// 是否压缩
        .freeStyleCropEnabled(config.freeStyleCropEnabled)//true
        .hideBottomControls(config.hideBottomControls)// 是否显示uCrop工具栏，默认不显示
        .showCropFrame(config.showCropFrame)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
        .showCropGrid(config.showCropGrid)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
        .isOpenClickSound(config.openClickSound)// 是否开启点击声音
        .cutOutQuality(config.cropCompressQuality)// 裁剪压缩质量 默认100
        .minimumCompressSize(config.minimumCompressSize)// 小于100kb的图片不压缩
        .rotateEnabled(config.rotateEnabled) // 裁剪是否可旋转图片
        .scaleEnabled(config.scaleEnabled)// 裁剪是否可放大缩小图片
        .circleDimmedLayer(config.circleDimmedLayer)// 是否圆形裁剪
        .cropImageWideHigh(config.cropWidth, config.cropHeight) // 裁剪宽高
        .withAspectRatio(config.aspect_ratio_x, config.aspect_ratio_y)//裁剪比例
        .forResult(callbackListener)//结果回调onActivityResult code
}

/**
 * 拍照
 *
 * @param object
 * @param entity
 * @param requestCode
 */
fun PictureSelector.takePicture(config: PictureSelectionConfig? = null, callbackListener: OnResultCallbackListener<LocalMedia>) {
    var config: PictureSelectionConfig? = config
    if (config == null) {
        config = pictureSelectorConfig()
    }
    openCamera(PictureMimeType.ofImage())
            .theme(R.style.picture_custom_style)
            .isEnableCrop(config.enableCrop)// 是否裁剪
            .isCompress(config.isCompress)// 是否压缩
            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
            .hideBottomControls(config.hideBottomControls)// 是否显示uCrop工具栏，默认不显示
            .showCropFrame(config.showCropFrame)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(config.showCropGrid)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .isOpenClickSound(config.openClickSound)// 是否开启点击声音
            .cutOutQuality(config.cropCompressQuality)// 裁剪压缩质量 默认100
            .minimumCompressSize(config.minimumCompressSize)// 小于100kb的图片不压缩
            .rotateEnabled(config.rotateEnabled) // 裁剪是否可旋转图片
            .scaleEnabled(config.scaleEnabled)// 裁剪是否可放大缩小图片
            .withAspectRatio(config.aspect_ratio_x, config.aspect_ratio_y)//裁剪比例
            .forResult(callbackListener)//结果回调onActivityResult code

}

/**
 * 选择视频
 *
 * @param object
 * @param entity
 * @param requestCode
 */
fun PictureSelector.selectVideo(config: PictureSelectionConfig? = null, callbackListener: OnResultCallbackListener<LocalMedia>) {
    var config: PictureSelectionConfig? = config
    if (config == null) {
        config = videoSelectorConfig()
    }
    openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.instance)
            .theme(R.style.picture_custom_style)
            .maxSelectNum(config.maxSelectNum)// 最大图片选择数量
            .minSelectNum(config.minSelectNum)// 最小选择数量
            .imageSpanCount(config.imageSpanCount)// 每行显示个数
            .selectionMode(config.selectionMode)// 多选 or 单选
            .isPreviewImage(config.enablePreview)// 是否可预览图片
            .isCamera(config.isCamera)// 是否显示拍照按钮
            .isOpenClickSound(config.openClickSound)// 是否开启点击声音
            .forResult(callbackListener)//结果回调onActivityResult code

}


/**
 * 拍摄视频
 *
 * @param object
 * @param entity
 * @param requestCode
 */
fun PictureSelector.takeVideo(config: PictureSelectionConfig? = null, callbackListener: OnResultCallbackListener<LocalMedia>) {
    var config: PictureSelectionConfig? = config
    if (config == null) {
        config = takeVideoConfig()
    }
    openCamera(PictureMimeType.ofVideo())
            .recordVideoSecond(config.recordVideoSecond)
            .forResult(callbackListener)
}

/**
 * 预览图片
 *
 * @param activity
 * @param position
 * @param selectList
 */
fun PictureSelector.picturePreview(position: Int, selectList: List<LocalMedia>) {
    themeStyle(R.style.picture_custom_style)
            .imageEngine(GlideEngine.instance)
            .openExternalPreview(position, selectList)
}