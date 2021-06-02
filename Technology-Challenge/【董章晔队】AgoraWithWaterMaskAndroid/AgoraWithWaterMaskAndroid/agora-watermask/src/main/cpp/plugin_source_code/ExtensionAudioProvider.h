//
// Created by 张涛 on 2020/4/26.
//

#ifndef AGORAWITHBYTEDANCE_EXTENSION_AUDIOPROVIDER_H
#define AGORAWITHBYTEDANCE_EXTENSION_AUDIOPROVIDER_H

#include "AgoraRtcKit/NGIAgoraExtensionProvider.h"
#include "ExtensionAudioFilter.h"

namespace agora {
    namespace extension {
        class ExtensionAudioProvider : public agora::rtc::IExtensionProvider {
        private:
            static ExtensionAudioProvider* instance_;
            agora_refptr<AdjustVolumeAudioProcessor> audioProcessor_;
        public:
            static void create() {
                if (instance_ == nullptr){
                    instance_ = new agora::RefCountedObject<ExtensionAudioProvider>();
                }
            }

            static ExtensionAudioProvider* getInstance(){
                return instance_;
            };

            ExtensionAudioProvider();

            ~ExtensionAudioProvider();

            PROVIDER_TYPE getProviderType() override;

            virtual void setExtensionControl(rtc::IExtensionControl* control) override;

            virtual agora_refptr<rtc::IAudioFilter> createAudioFilter() override;

            virtual agora_refptr<rtc::IVideoFilter> createVideoFilter() override;

            virtual agora_refptr<rtc::IVideoSinkBase> createVideoSink() override;

            int setExtensionVendor(std::string vendor);
        };
    }
}
#endif //AGORAWITHBYTEDANCE_EXTENSION_AUDIOPROVIDER_H
