//
//  TYSMVideoFilterProvider.m
//  TYSMExtension
//
//  Created by Jele on 18/5/2021.
//

#import "TYSMVideoFilterProvider.h"
#include <AgoraRtcKit2/AgoraRefCountedObject.h>
#include <sstream>
#import <Foundation/Foundation.h>

namespace agora {
    namespace extension {

    VideoProvider *VideoProvider::instance_;
        
        VideoProvider::VideoProvider() {
            std::cout << "初始化 VideoProvider" << std::endl;
            brf_processor_ = new agora::RefCountedObject<BRFProcessor>();
        }
        
        VideoProvider::~VideoProvider() {
            std::cout << "释放 VideoProvider" << std::endl;

        }
        
        int VideoProvider::setExtensionVendor(std::string vendor) {
            std::cout << __func__ << std::endl;
            brf_processor_->setExtensionVendor(vendor.c_str());
            return 0;
        }
        
        agora_refptr<agora::rtc::IVideoFilter> VideoProvider::createVideoFilter() {
            std::cout << __func__ << std::endl;
            if (brf_processor_ == nullptr) return nullptr;
            
            return new agora::RefCountedObject<agora::extension::TYSMIVideoFilter>(brf_processor_);
        }
        
        agora_refptr<agora::rtc::IAudioFilter> VideoProvider::createAudioFilter() {
            std::cout << __func__ << std::endl;
            return nullptr;
        }
        
        agora_refptr<agora::rtc::IVideoSinkBase> VideoProvider::createVideoSink() {
            std::cout << __func__ << std::endl;
            return nullptr;
        }
        
        VideoProvider::PROVIDER_TYPE VideoProvider::getProviderType() {
            std::cout << __func__ << std::endl;
            return agora::rtc::IExtensionProvider::LOCAL_VIDEO_FILTER;
        }
        
        void VideoProvider::setExtensionControl(rtc::IExtensionControl* control){
            std::cout << __func__ << std::endl;
            brf_processor_->setExtensionControl(control);
        }
        
    }
}

