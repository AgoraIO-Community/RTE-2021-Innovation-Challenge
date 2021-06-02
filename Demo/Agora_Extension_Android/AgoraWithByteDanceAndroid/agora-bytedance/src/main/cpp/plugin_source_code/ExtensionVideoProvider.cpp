//
// Created by 张涛 on 2020/4/26.
//

#include "ExtensionVideoProvider.h"
#include "../logutils.h"
#include "VideoProcessor.h"

namespace agora {
    namespace extension {
        ExtensionVideoProvider* ExtensionVideoProvider::instance_;
        ExtensionVideoProvider::ExtensionVideoProvider() {
            PRINTF_INFO("ExtensionVideoProvider create");
            byteDanceProcessor_ = new agora::RefCountedObject<ByteDanceProcessor>();
        }

        ExtensionVideoProvider::~ExtensionVideoProvider() {
            PRINTF_INFO("ExtensionVideoProvider destroy");
            instance_ = nullptr;
        }

        int ExtensionVideoProvider::setExtensionVendor(std::string vendor) {
            PRINTF_INFO("ExtensionVideoProvider vendor %s", vendor.c_str());
            byteDanceProcessor_->setExtensionVendor(vendor.c_str());
            return 0;
        }

        agora_refptr<agora::rtc::IVideoFilter> ExtensionVideoProvider::createVideoFilter() {
            PRINTF_INFO("ExtensionVideoProvider::createVideoFilter");
            auto videoFilter = new agora::RefCountedObject<agora::extension::ExtensionVideoFilter>(byteDanceProcessor_);
            return videoFilter;
        }

        agora_refptr<agora::rtc::IAudioFilter> ExtensionVideoProvider::createAudioFilter() {
            return nullptr;
        }

        agora_refptr<agora::rtc::IVideoSinkBase> ExtensionVideoProvider::createVideoSink() {
            return nullptr;
        }

        ExtensionVideoProvider::PROVIDER_TYPE ExtensionVideoProvider::getProviderType() {
            return agora::rtc::IExtensionProvider::LOCAL_VIDEO_FILTER;
        }

        void ExtensionVideoProvider::setExtensionControl(rtc::IExtensionControl* control){
            byteDanceProcessor_->setExtensionControl(control);
        }
    }
}
