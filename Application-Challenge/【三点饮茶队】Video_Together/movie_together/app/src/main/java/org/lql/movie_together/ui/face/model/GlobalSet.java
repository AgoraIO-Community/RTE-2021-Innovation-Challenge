package org.lql.movie_together.ui.face.model;

/**
 * @Time: 2019/5/24
 * @Author: v_zhangxiaoqing01
 */

public class GlobalSet {

    // 模型在asset 下path 为空
    public static final String PATH = "";
    // 模型在SD 卡下写对应的绝对路径
    // public static final String PATH = "/storage/emulated/0/baidu_face/model/";

    public static final int FEATURE_SIZE = 512;

    public static final String TIME_TAG = "face_time";

    // 遮罩比例
    public static final float SURFACE_RATIO = 0.6f;

    public static final String DETECT_VIS_MODEL = PATH
            + "face-sdk-models/detect/detect_rgb-customized-pa-faceid6_0.model.int8-0.0.11.1";
    public static final String DETECT_NIR_MODE = PATH
            + "face-sdk-models/detect/detect_nir-customized-pa-normquant.model.int8-0.0.12.1";
    //    public static final String ALIGN_RGB_MODEL = PATH
//            + "align/align_rgb-mobilenet-pa-bigmodel_191224.model.int8-0.7.6.1";
    public static final String ALIGN_RGB_MODEL = PATH
            + "face-sdk-models/align/align_rgb-customized-pa-model.model.float32-6.4.10.1";
    public static final String ALIGN_NIR_MODEL = PATH
            + "face-sdk-models/align/align_nir-customized-pa-model.model.float32-6.4.11.1";
    public static final String ALIGN_TRACK_MODEL = PATH
            + "face-sdk-models/align/align_rgb-customized-pa-mobile.model.float32-0.7.5.3";
    public static final String LIVE_VIS_MODEL = PATH
            + "face-sdk-models/silent_live/liveness_rgb-customized-pa-LGSC.model.float32-1.1.12.1";
    public static final String LIVE_NIR_MODEL = PATH
            + "face-sdk-models/silent_live/liveness_nir-customized-pa-60_android.model.float32-1.1.11.1";
    public static final String LIVE_DEPTH_MODEL = PATH
            + "face-sdk-models/silent_live/liveness_depth-customized-pa-paddle_60.model.float32-1.1.13.1";
    public static final String RECOGNIZE_VIS_MODEL = PATH
            + "face-sdk-models/feature/feature_live-mnasnet-pa-life.model.int8-2.0.134.1";
    public static final String RECOGNIZE_IDPHOTO_MODEL = PATH
            + "face-sdk-models/feature/feature_id-mnasnet-pa-renzheng.model.int8-2.0.135.2";
    public static final String RECOGNIZE_NIR_MODEL = PATH
            + "face-sdk-models/feature/feature_nir-mnasnet-pa-nir_rgb_mix.model.int8-2.0.138.1";
    public static final String RECOGNIZE_RGBD_MODEL = PATH
            + "face-sdk-models/feature/feature_rgbd-mnasnet-pa-RGBD_FaceID_5.model.int8-2.0.88.2";
    public static final String OCCLUSION_MODEL = PATH
            + "face-sdk-models/occlusion/occlusion-customized-pa-paddle.model.float32-2.0.7.1";
    public static final String BLUR_MODEL = PATH
            + "face-sdk-models/blur/blur-customized-pa-blurnet_9768.model.float32-3.0.9.1";

    public static final String ATTRIBUTE_MODEL = PATH
            + "face-sdk-models/attribute/attribute-customized-pa-mobile.model.float32-1.0.9.4";
    public static final String EMOTION_MODEL = PATH
            + "";
    public static final String GAZE_MODEL = PATH
            + "face-sdk-models/gaze/gaze-customized-pa-mobile.model.float32-1.0.3.3";
    public static final String DRIVEMONITOR_MODEL = PATH
            + "face-sdk-models/driver_monitor/driver_monitor_nir-customized-pa-DMS_rgb_nir_detect." +
            "model.float32-1.0.1.1";
    public static final String MOUTH_MASK = PATH
            + "face-sdk-models/mouth_mask/mouth_mask-customized-pa-20200306.model.float32-1.0.6.3";
    public static final String BEST_IMAGE = PATH
            + "face-sdk-models/best_image/best_image-mobilenet-pa-faceid60.model.float32-1.0.1.1";
    // 图片尺寸限制大小
    public static final int PICTURE_SIZE = 1000000;

    // 摄像头类型
    public static final String TYPE_CAMERA = "TYPE_CAMERA";
    public static final int ORBBEC = 1;
    public static final int IMIMECT = 2;
    public static final int ORBBECPRO = 3;
    public static final int ORBBECPROS1 = 4;
    public static final int ORBBECPRODABAI = 5;
    public static final int ORBBECPRODEEYEA = 6;
    public static final int ORBBECATLAS = 7;

}
