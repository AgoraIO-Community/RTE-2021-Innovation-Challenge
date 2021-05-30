package org.lql.movie_together.ui.face.model;

/**
 * author : shangrong
 * date : 2019/5/22 9:10 PM
 * description :配置文件
 */
public class AttributeBaseConfig {
    // 设备通信密码
    private String dPass = "";
    // RGB检测帧回显
    private Boolean display = true;
    // RGB预览Y轴转向falese为0，true为180
    private Boolean rgbRevert = false;
    // NIR或depth实时视频预览
    private Boolean isNirOrDepth = true;
    // 默认为false。可选项为"true"、"false"，是否开启调试显示，将会作用到所有视频流识别页面，包含1：N、1：1采集人脸图片环节。
    private boolean debug = false;
    // 默认为0。可传入0、90、180、270四个选项。
    private int videoDirection = 0;
    // 默认为wireframe。可选项为"wireframe"、"fixedarea"，如选择fixed_area，需要传入半径，px像素为单位
    private String detectFrame = "wireframe";
    // 当选择fixed_area，需要传入半径信息，以px为单位，如50px
//    private int radius = 50;
    // 默认为0。可传入0、90、180、270四个选项
    private int detectDirection = 270;
    // 默认为max。分为"max" 、"none"三个方式，分别是最大人脸 ，和不检测人脸
    private String trackType = "max";
    // 默认为80px。可传入大于50px的数值，小于此大小的人脸不予检测
    private int minimumFace = 40;
    // 模糊度设置，默认0.5。取值范围[0~1]，0是最清晰，1是最模糊
    private float blur = 0.5f;
    // 光照设置，默认40.取值范围[0~255], 数值越大，光线越强
    private int illumination = 40;
    // 姿态阈值
    private float gesture = 15;
    // 三维旋转之俯仰角度[-90(上), 90(下)]，默认20
    private float pitch = 20;
    // 平面内旋转角[-180(逆时针), 180(顺时针)]，默认20
    private float roll = 20;
    // 三维旋转之左右旋转角[-90(左), 90(右)]，默认20
    private float yaw = 20;
    // 遮挡阈值
    private float occlusion = 0.6f;
    // 左眼被遮挡的阈值，默认0.6
    private float leftEye = 0.6f;
    // 右眼被遮挡的阈值，默认0.6
    private float rightEye = 0.6f;
    // 鼻子被遮挡的阈值，默认0.7
    private float nose = 0.7f;
    // 嘴巴被遮挡的阈值，默认0.7
    private float mouth = 0.7f;
    // 左脸颊被遮挡的阈值，默认0.8
    private float leftCheek = 0.8f;
    // 右脸颊被遮挡的阈值，默认0.8
    private float rightCheek = 0.8f;
    // 下巴被遮挡阈值，默认为0.6
    private float chinContour = 0.6f;
    // 人脸完整度，默认为1。0为人脸溢出图像边界，1为人脸都在图像边界内
    private float completeness = 1f;
    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    private int liveThreshold = 80;
    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    private int idThreshold = 80;
    // 识别阈值，0-100，默认为80分,需要选择具体模型的阈值。live：80、idcard：80
    private int rgbAndNirThreshold = 80;
    // 模态切换光线阈值
    private int camera_lightThreshold = 90;
    // 使用的特征抽取模型默认为生活照：1；证件照：2；RGB+NIR混合模态模型：3；
    private int activeModel = 1;
    // 识别结果出来后的演示展示，默认为0ms
    private int timeLapse = 0;
    // RGB活体：1
    // RGB+NIR活体：2
    // RGB+Depth活体：3
    // RGB+NIR+Depth活体：4
    private int type = 1;
    // 是否开启质量检测开关
    private boolean qualityControl = true;
    // 是否开启活体检测开关
    private boolean livingControl = true;
    // RGB活体阀值
    private float rgbLiveScore = 0.80f;
    // NIR活体阀值
    private float nirLiveScore = 0.80f;
    // Depth活体阀值
    private float depthLiveScore = 0.80f;


    public int getFramesThreshold() {
        return FramesThreshold;
    }

    public void setFramesThreshold(int framesThreshold) {
        FramesThreshold = framesThreshold;
    }

    // 帧数阈值
    private int FramesThreshold = 3;

    // 0:奥比中光海燕、大白（640*400）
    // 1:奥比中光海燕Pro、Atlas（400*640）
    // 2:奥比中光蝴蝶、Astra Pro\Pro S（640*480）
    // 3:舜宇Seeker06
    // 4:螳螂慧视天蝎P1
    // 5:瑞识M720N
    // 6:奥比中光Deeyea(结构光)
    // 7:华捷艾米A100S、A200(结构光)
    // 8:Pico DCAM710(ToF)
    private int cameraType = 0;

    // 0：RGB无镜像，1：有镜像
    private int mirrorRGB = 0;
    // 0：NIR无镜像，1：有镜像
    private int mirrorNIR = 0;

    // 是否开启属性检测
    private boolean attribute = true;

    // rgb和nir摄像头宽
    private int rgbAndNirWidth = 640;
    // rgb和nir摄像头高
    private int rgbAndNirHeight = 480;
    // depth摄像头宽
    private int depthWidth = 640;
    // depth摄像头高
    private int depthHeight = 480;

    // 是否开启最优人脸检测
    private boolean usingBestImage = false;
    // 最优人脸分数
    private int bestImageScore = 30;

    public boolean isUsingBestImage() {
        return usingBestImage;
    }

    public void setUsingBestImage(boolean usingBestImage) {
        this.usingBestImage = usingBestImage;
    }

    public int getBestImageScore() {
        return bestImageScore;
    }

    public void setBestImageScore(int bestImageScore) {
        this.bestImageScore = bestImageScore;
    }

    public int getRgbAndNirWidth() {
        return rgbAndNirWidth;
    }

    public void setRgbAndNirWidth(int rgbAndNirWidth) {
        this.rgbAndNirWidth = rgbAndNirWidth;
    }

    public int getRgbAndNirHeight() {
        return rgbAndNirHeight;
    }

    public void setRgbAndNirHeight(int rgbAndNirHeight) {
        this.rgbAndNirHeight = rgbAndNirHeight;
    }

    public int getDepthWidth() {
        return depthWidth;
    }

    public void setDepthWidth(int depthWidth) {
        this.depthWidth = depthWidth;
    }

    public int getDepthHeight() {
        return depthHeight;
    }

    public void setDepthHeight(int depthHeight) {
        this.depthHeight = depthHeight;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }


    public float getRgbLiveScore() {
        return rgbLiveScore;
    }

    public void setRgbLiveScore(float rgbLiveScore) {
        this.rgbLiveScore = rgbLiveScore;
    }

    public float getNirLiveScore() {
        return nirLiveScore;
    }

    public void setNirLiveScore(float nirLiveScore) {
        this.nirLiveScore = nirLiveScore;
    }

    public float getDepthLiveScore() {
        return depthLiveScore;
    }

    public void setDepthLiveScore(float depthLiveScore) {
        this.depthLiveScore = depthLiveScore;
    }

    public String getdPass() {
        return dPass;
    }

    public void setdPass(String dPass) {
        this.dPass = dPass;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getVideoDirection() {
        return videoDirection;
    }

    public void setVideoDirection(int videoDirection) {
        this.videoDirection = videoDirection;
    }

    public String getDetectFrame() {
        return detectFrame;
    }

    public void setDetectFrame(String detectFrame) {
        this.detectFrame = detectFrame;
    }

//    public int getRadius() {
//        return radius;
//    }
//
//    public void setRadius(int radius) {
//        this.radius = radius;
//    }

    public int getDetectDirection() {
        return detectDirection;
    }

    public void setDetectDirection(int detectDirection) {
        this.detectDirection = detectDirection;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public int getMinimumFace() {
        return minimumFace;
    }

    public void setMinimumFace(int minimumFace) {
        this.minimumFace = minimumFace;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public int getIllumination() {
        return illumination;
    }

    public void setIllumination(int illumination) {
        this.illumination = illumination;
    }

    public float getGesture() {
        return gesture;
    }

    public void setGesture(float gesture) {
        this.gesture = gesture;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getOcclusion() {
        return occlusion;
    }

    public void setOcclusion(float occlusion) {
        this.occlusion = occlusion;
    }

    public float getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(float leftEye) {
        this.leftEye = leftEye;
    }

    public float getRightEye() {
        return rightEye;
    }

    public void setRightEye(float rightEye) {
        this.rightEye = rightEye;
    }

    public float getNose() {
        return nose;
    }

    public void setNose(float nose) {
        this.nose = nose;
    }

    public float getMouth() {
        return mouth;
    }

    public void setMouth(float mouth) {
        this.mouth = mouth;
    }

    public float getLeftCheek() {
        return leftCheek;
    }

    public void setLeftCheek(float leftCheek) {
        this.leftCheek = leftCheek;
    }

    public float getRightCheek() {
        return rightCheek;
    }

    public void setRightCheek(float rightCheek) {
        this.rightCheek = rightCheek;
    }

    public float getChinContour() {
        return chinContour;
    }

    public void setChinContour(float chinContour) {
        this.chinContour = chinContour;
    }

    public float getCompleteness() {
        return completeness;
    }

    public void setCompleteness(float completeness) {
        this.completeness = completeness;
    }

    public int getLiveThreshold() {
        return liveThreshold;
    }

    public void setLiveThreshold(int liveThreshold) {
        this.liveThreshold = liveThreshold;
    }

    public int getIdThreshold() {
        return idThreshold;
    }

    public void setIdThreshold(int idThreshold) {
        this.idThreshold = idThreshold;
    }

    public int getActiveModel() {
        return activeModel;
    }

    public void setActiveModel(int activeModel) {
        this.activeModel = activeModel;
    }

    public int getTimeLapse() {
        return timeLapse;
    }

    public void setTimeLapse(int timeLapse) {
        this.timeLapse = timeLapse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isQualityControl() {
        return qualityControl;
    }

    public void setQualityControl(boolean qualityControl) {
        this.qualityControl = qualityControl;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean getNirOrDepth() {
        return isNirOrDepth;
    }

    public void setNirOrDepth(Boolean nirOrDepth) {
        isNirOrDepth = nirOrDepth;
    }

    public int getMirrorRGB() {
        return mirrorRGB;
    }

    public void setMirrorRGB(int mirrorRGB) {
        this.mirrorRGB = mirrorRGB;
    }

    public int getMirrorNIR() {
        return mirrorNIR;
    }

    public void setMirrorNIR(int mirrorNIR) {
        this.mirrorNIR = mirrorNIR;
    }

    public Boolean getRgbRevert() {
        return rgbRevert;
    }

    public void setRgbRevert(Boolean rgbRevert) {
        this.rgbRevert = rgbRevert;
    }


    public boolean isAttribute() {
        return attribute;
    }

    public void setAttribute(boolean attribute) {
        this.attribute = attribute;
    }

    public boolean isLivingControl() {
        return livingControl;
    }

    public void setLivingControl(boolean livingControl) {
        this.livingControl = livingControl;
    }

    public int getCamera_lightThreshold() {
        return camera_lightThreshold;
    }

    public void setCamera_lightThreshold(int camera_lightThreshold) {
        this.camera_lightThreshold = camera_lightThreshold;
    }

    public int getRgbAndNirThreshold() {
        return rgbAndNirThreshold;
    }

    public void setRgbAndNirThreshold(int rgbAndNirThreshold) {
        this.rgbAndNirThreshold = rgbAndNirThreshold;
    }
}
