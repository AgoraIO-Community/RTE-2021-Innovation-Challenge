//
// Created by DYF on 2020/7/13.
//

#ifndef AGORAWITHBYTEDANCE_VIDEOPROCESSOR_H
#define AGORAWITHBYTEDANCE_VIDEOPROCESSOR_H

#include <thread>
#include <string>
#include <mutex>
#include <vector>
#include <AgoraRtcKit/AgoraRefPtr.h>
#include <AgoraRtcKit/NGIAgoraExtensionControl.h>

#include "AgoraRtcKit/AgoraMediaBase.h"
#include "../bytedance/bef_effect_ai_api.h"
#include "../bytedance/bef_effect_ai_lightcls.h"

#include "EGLCore.h"
#include "rapidjson/rapidjson.h"

namespace agora {
    namespace extension {
        class ByteDanceProcessor  : public RefCountInterface {
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
            ~ByteDanceProcessor() {}
        private:
            void dataCallback(const char* data);
            void processFaceDetect();
            void processHandDetect();
            void processLightDetect();
            void processEffect(const agora::media::base::VideoFrame &capturedFrame);
            void prepareCachedVideoFrame(const agora::media::base::VideoFrame &capturedFrame);

#if defined(__ANDROID__) || defined(TARGET_OS_ANDROID)
            EglCore *eglCore_ = nullptr;
            EGLSurface offscreenSurface_ = nullptr;
#endif
            std::mutex mutex_;

            bef_effect_handle_t byteEffectHandler_ = nullptr;
            std::string licensePath_;
            std::string modelDir_;
            bool aiEffectEnabled_ = false;
            char** aiNodes_ = nullptr;
            rapidjson::SizeType aiNodeCount_ = 0;
            std::vector<float> aiNodeIntensities_;
            std::vector<std::string> aiNodeKeys_;
            bool aiEffectNeedUpdate_ = false;

            bool faceAttributeEnabled_ = false;
            std::string faceDetectModelPath_;
            std::string faceAttributeModelPath_;
            bef_effect_handle_t faceDetectHandler_ = nullptr;
            bef_effect_handle_t faceAttributesHandler_ = nullptr;

            bool handDetectEnabled_ = false;
            std::string handDetectModelPath_;
            std::string handBoxModelPath_;
            std::string handGestureModelPath_;
            std::string handKPModelPath_;
            bef_effect_handle_t handDetectHandler_ = nullptr;

            bool lightDetectEnabled_ = false;
            std::string lightDetectModelPath_;
            bef_effect_handle_t lightDetectHandler_ = nullptr;

            agora::media::base::VideoFrame prevFrame_;
            unsigned char* yuvBuffer_ = nullptr;
            unsigned char* rgbaBuffer_ = nullptr;

            bool faceStickerEnabled_ = false;
            std::string faceStickerItemPath_;
            agora::rtc::IExtensionControl* control_;
            char* id_;
        };
    }
}


#endif //AGORAWITHBYTEDANCE_VIDEOPROCESSOR_H
