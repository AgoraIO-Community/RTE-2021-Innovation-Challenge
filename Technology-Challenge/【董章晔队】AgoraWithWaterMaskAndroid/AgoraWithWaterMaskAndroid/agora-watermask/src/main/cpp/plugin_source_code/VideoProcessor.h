#ifndef AGORAWITHWATERMASK_VIDEOPROCESSOR_H
#define AGORAWITHWATERMASK_VIDEOPROCESSOR_H

#include <thread>
#include <string>
#include <mutex>
#include <vector>
#include <AgoraRtcKit/AgoraRefPtr.h>
#include <AgoraRtcKit/NGIAgoraExtensionControl.h>

#include "AgoraRtcKit/AgoraMediaBase.h"

#include "EGLCore.h"
#include "rapidjson/rapidjson.h"

#include "opencv2/core/utility.hpp"

namespace agora {
    namespace extension {
        class WaterMaskProcessor  : public RefCountInterface {
        public:
            bool initOpenGL();

            bool releaseOpenGL();

            int processFrame(const agora::media::base::VideoFrame &capturedFrame);

            int releaseEffectEngine();

            int setParameters(std::string parameter);

            std::thread::id getThreadId();

            int setExtensionControl(agora::rtc::IExtensionControl* control){
                control_ = control;
                return 0;
            };

            int setExtensionVendor(const char* id){
                int len = std::string(id).length() + 1;
                id_ = static_cast<char *>(malloc(len));
                memset(id_, 0, len);
                strcpy(id_, id);
                return 0;
            };
        protected:
            ~WaterMaskProcessor() {}
        private:
            void processEffect(const agora::media::base::VideoFrame &capturedFrame);

            void transformImageWithText(cv::Mat image, cv::String watermarkText,
                                                            cv::Point point, double fontSize, cv::Scalar scalar);
            cv::Mat optimizeImageDim(cv::Mat image);
            cv::Mat antitransformImage();

#if defined(__ANDROID__) || defined(TARGET_OS_ANDROID)
            EglCore *eglCore_ = nullptr;
            EGLSurface offscreenSurface_ = nullptr;
#endif
            std::mutex mutex_;
            agora::media::base::VideoFrame prevFrame_;
            unsigned char* yuvBuffer_ = nullptr;

            agora::rtc::IExtensionControl* control_;
            char* id_;
            bool wmEffectEnabled_ = false;
            std::string wmStr_= "wm";;

            // dzy
            std::vector<cv::Mat> planes;
            cv::Mat complexImage;
        };
    }
}


#endif //AGORAWITHWATERMASK_VIDEOPROCESSOR_H
