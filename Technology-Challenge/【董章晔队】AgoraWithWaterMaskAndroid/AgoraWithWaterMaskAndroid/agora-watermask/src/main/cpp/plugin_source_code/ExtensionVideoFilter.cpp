//
// Created by 张涛 on 2020/4/26.
//

#include "ExtensionVideoFilter.h"
#include "../logutils.h"
#include <sstream>

namespace agora {
    namespace extension {

        ExtensionVideoFilter::ExtensionVideoFilter(agora_refptr<WaterMaskProcessor> waterMaskProcessor) {
            waterMaskProcessor_ = waterMaskProcessor;
        }

        ExtensionVideoFilter::~ExtensionVideoFilter() {
            waterMaskProcessor_->releaseOpenGL();
        }

        bool ExtensionVideoFilter::adaptVideoFrame(const agora::media::base::VideoFrame &capturedFrame,
                             agora::media::base::VideoFrame &adaptedFrame) {
//            PRINTF_INFO("adaptVideoFrame %d %d", capturedFrame.width, capturedFrame.height);
            if (!isInitOpenGL) {
                isInitOpenGL = waterMaskProcessor_->initOpenGL();
            }
            waterMaskProcessor_->processFrame(capturedFrame);
            adaptedFrame = capturedFrame;
            return true;
        }

        size_t ExtensionVideoFilter::setProperty(const char *key, const void *buf,
                                                 size_t buf_size) {
            PRINTF_INFO("setProperty  %s  %s", key, buf);
            std::string stringParameter((char*)buf);
            waterMaskProcessor_->setParameters(stringParameter);
            return 0;
        }

        size_t ExtensionVideoFilter::getProperty(const char *key, void *buf, size_t buf_size) {
            return 0;
        }

        void ExtensionVideoFilter::setEnabled(bool enable) {
        }

        bool ExtensionVideoFilter::isEnabled() {
            return true;
        }
    }
}
