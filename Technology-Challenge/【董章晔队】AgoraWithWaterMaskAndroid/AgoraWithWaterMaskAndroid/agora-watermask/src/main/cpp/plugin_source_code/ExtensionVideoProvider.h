//
// Created by 张涛 on 2020/4/26.
//

#ifndef AGORAWITHWATERMASK_EXTENSION_VIDEOPROVIDER_H
#define AGORAWITHWATERMASK_EXTENSION_VIDEOPROVIDER_H

#include "AgoraRtcKit/NGIAgoraExtensionProvider.h"
#include "ExtensionVideoFilter.h"

namespace agora {
    namespace extension {

        class WaterMaskProcessor;

        class ExtensionVideoProvider : public agora::rtc::IExtensionProvider {
        private:
            static ExtensionVideoProvider* instance_;
            agora_refptr<WaterMaskProcessor> waterMaskProcessor_;
        public:
            static void create() {
                if (instance_ == nullptr){
                    instance_ = new agora::RefCountedObject<ExtensionVideoProvider>();
                }
            }

            static ExtensionVideoProvider* getInstance(){
                return instance_;
            };

            ExtensionVideoProvider();

            ~ExtensionVideoProvider();

            PROVIDER_TYPE getProviderType() override;

            virtual void setExtensionControl(rtc::IExtensionControl* control) override;

            virtual agora_refptr<rtc::IAudioFilter> createAudioFilter() override;

            virtual agora_refptr<rtc::IVideoFilter> createVideoFilter() override;

            virtual agora_refptr<rtc::IVideoSinkBase> createVideoSink() override;

            int setExtensionVendor(std::string vendor);
        };
    }
}
#endif //AGORAWITHWATERMASK_EXTENSION_VIDEOPROVIDER_H
