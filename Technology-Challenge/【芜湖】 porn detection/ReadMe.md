# **作品简介** #

作品用于在视频聊天中或直播中，视频内容是否涉及色情问题。
内容包含色情、写真、画像。

**功能：**
检测直播或者视频过程中的色情违法问题。
违规的视频帧会被保存在检测目录中。
图片检测可返回属性：中性值、变态值（身体裸露）、画像值、色情值、性感值。

1. 使用方式：
	自身类中复写onCaptureVideoFrame函数
	包含头文件ViolationObserver.h
	
	bool ViolationObserver::onCaptureVideoFrame(VideoFrame &videoFrame){

        ViolationObserver::m_VideoFrame mVideoFrame;
        mVideoFrame.width = videoFrame.width;
        mVideoFrame.height = videoFrame.height;
        mVideoFrame.yBuffer = videoFrame.yBuffer;
        mVideoFrame.uBuffer = videoFrame.uBuffer;
        mVideoFrame.vBuffer = videoFrame.vBuffer;

        OnObserverViolatingContent(mVideoFrame);

    return true;
}


2. 依赖库：目前编译了x64位版本，都放在Lib_文件夹里面，需要x86可自行编译。
	libcurl: C++ 的Http库
	FreeImage: 改变Jpeg图像尺寸
	libjpeg_turbo:YUV420图像保存为Jpeg方式.
	rapidJson:官方的Json库，直接Include可以使用。
