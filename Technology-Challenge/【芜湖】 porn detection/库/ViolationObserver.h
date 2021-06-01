#pragma once
#include<windows.h>
#include<string>
#include<future>
#include"include_/Agora/IAgoraMediaEngine.h"

class ViolationObserver:public agora::media::IVideoFrameObserver
{

public:

    /*
    * 自定义YUV420P视频流类型，内部为一帧图像的宽、高、和YUV三个通道的数据
    * 为了适应在其他的agore sdk版本中去使用
    */
    struct m_VideoFrame {
        int width;  // width of video frame
        int height;  // height of video frame
        void* yBuffer;  // Y data buffer
        void* uBuffer;  // U data buffer
        void* vBuffer;  // V data buffer
    };

    enum DetectType{
        Violation,
        Normal,
        INTERRUPT
    };

    /*
     * 预留的返回获取到的违规图片的返回信息。
     * time_Info:违规图片帧的截取时间。
     * ImagePath:违规图片的保存位置。
     * neutral:图像帧的中性属性，指普通图片的程度。
     * metamorphosis:图像帧中的变态属性,指图像中衣着裸露，但并非通常色情的范畴。
     * sex_addiction:图像帧中的性感属性，通常可识别出写真图片。
     * patinting:图片帧的画作属性，指图像和画作的相像程度。
     * pornography:图片帧的色情属性，即判定帧中涉及色情的程度。
     */
    struct Violation_Info{
        std::string time_Info;
        std::string ImagePath;
        DetectType DetectResult;
        double neutral;
        double metamorphosis;
        double sex_addiction;
        double patinting;
        double pornography;

    };

private:
    /*
    *Jpeg图像转为Json字符串通过http协议上传服务器检测的部分。
    */
    void interceptJpegAndUpload(m_VideoFrame& videoFrame);

    /*
     * 传输的YUV视频流转化为jpeg图像
    */
	int yuv420p_to_jpeg(const char* filename, char* Ybuffer, char* Ubuffer, char* Vbuffer, long image_width, long image_height, int quality = 60);

    /*
     *修改jpeg图像为224*224的大小，方便AI的图像属性识别
    **/
    void ChangeImageSize(const char* oriFilePath, const char* dstFilePath, const int nWidth =224, const int nHeight = 224);


private:
    //图像中性属性
	double neutral;
    //图像中变态属性
	double metamorphosis;
    //图像中性感属性
	double sex_addiction;
    //图像中画作属性
	double patinting;
    //图像中色情属性
	double pornography;


public:
    ViolationObserver(){}
    virtual ~ViolationObserver(){}
    virtual bool onCaptureVideoFrame(VideoFrame &videoFrame) override;
    virtual bool onRenderVideoFrame(unsigned int uid, VideoFrame &videoFrame) override;

public:
    //监视传来的视频帧
    void OnObserverViolatingContent(m_VideoFrame VideoFrame);
    //结束时调用，清空本地缓存
    void OffObserverViolatingContent();
public:
	double getNeutral();
	double getMetamorphosis();
	double getSexAddiction();
	double getPatinting();
	double getPornograph();

};

