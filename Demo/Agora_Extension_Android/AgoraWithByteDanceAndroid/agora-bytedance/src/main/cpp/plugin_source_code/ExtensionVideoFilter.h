//
// Created by 张涛 on 2020/4/26.
//

#ifndef AGORAWITHBYTEDANCE_EXTENSIONVIDEOFILTER_H
#define AGORAWITHBYTEDANCE_EXTENSIONVIDEOFILTER_H

#include "AgoraRtcKit/NGIAgoraMediaNode.h"
#include <AgoraRtcKit/AgoraRefCountedObject.h>
#include "AgoraRtcKit/AgoraRefPtr.h"
#include "VideoProcessor.h"

namespace agora {
    namespace extension {
        class ExtensionVideoFilter : public agora::rtc::IVideoFilter {
        public:
            ExtensionVideoFilter(agora_refptr<ByteDanceProcessor> byteDanceProcessor);

            ~ExtensionVideoFilter();

            bool adaptVideoFrame(const agora::media::base::VideoFrame &capturedFrame,
                                 agora::media::base::VideoFrame &adaptedFrame) override;

            void setEnabled(bool enable) override;

            bool isEnabled() override;

            size_t setProperty(const char *key, const void *buf, size_t buf_size) override;

            size_t getProperty(const char *key, void *buf, size_t buf_size) override;

        private:
            agora_refptr<ByteDanceProcessor> byteDanceProcessor_;
            bool isInitOpenGL = false;
        protected:
            ExtensionVideoFilter() = default;

        };
    }
}


#endif //AGORAWITHBYTEDANCE_EXTENSIONVIDEOFILTER_H
