//
// Created by 张涛 on 2020/4/26.
//

#include "ExtensionAudioProvider.h"
#include "../logutils.h"
#include "AudioProcessor.h"

namespace agora {
    namespace extension {
        ExtensionAudioProvider* ExtensionAudioProvider::instance_;
        ExtensionAudioProvider::ExtensionAudioProvider() {
            PRINTF_INFO("ExtensionAudioProvider create");
            audioProcessor_ = new agora::RefCountedObject<AdjustVolumeAudioProcessor>();
        }

        ExtensionAudioProvider::~ExtensionAudioProvider() {
            PRINTF_INFO("ExtensionAudioProvider destroy");
            instance_ = nullptr;
        }

        int ExtensionAudioProvider::setExtensionVendor(std::string vendor) {
            PRINTF_INFO("ExtensionAudioProvider vendor %s", vendor.c_str());
            audioProcessor_->setVendorName(vendor.c_str());
            return 0;
        }

        agora_refptr<agora::rtc::IVideoFilter> ExtensionAudioProvider::createVideoFilter() {
            return nullptr;
        }

        agora_refptr<agora::rtc::IAudioFilter> ExtensionAudioProvider::createAudioFilter() {
            PRINTF_ERROR("ExtensionAudioProvider::createAudioFilter");
            auto audioFilter = new agora::RefCountedObject<agora::extension::ExtensionAudioFilter>(audioProcessor_);
            return audioFilter;
        }

        agora_refptr<agora::rtc::IVideoSinkBase> ExtensionAudioProvider::createVideoSink() {
            return nullptr;
        }

        ExtensionAudioProvider::PROVIDER_TYPE ExtensionAudioProvider::getProviderType() {
            return agora::rtc::IExtensionProvider::LOCAL_AUDIO_FILTER;
        }

        void ExtensionAudioProvider::setExtensionControl(rtc::IExtensionControl* control){
            audioProcessor_->setExtensionControl(control);
        }
    }
}
