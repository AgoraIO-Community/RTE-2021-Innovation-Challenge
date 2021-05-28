//
//  TYSMVideoFilterProvider.h
//  TYSMExtension
//
//  Created by Jele on 18/5/2021.
//

#pragma once

#include <AgoraRtcKit2/NGIAgoraExtensionProvider.h>
#include <iostream>
#include "TYSMIVideoFilter.h"

namespace agora {
    namespace extension {
        
        class TYSMProcessor;
        
        class VideoProvider : public agora::rtc::IExtensionProvider  {
        private:
            static VideoProvider *instance_;
            
            agora_refptr<BRFProcessor> brf_processor_;
            
        public:
            
            VideoProvider();
            ~VideoProvider();
            
            PROVIDER_TYPE getProviderType() override;
            
            virtual void setExtensionControl(rtc::IExtensionControl* control) override;

            virtual agora_refptr<rtc::IAudioFilter> createAudioFilter() override;

            virtual agora_refptr<rtc::IVideoFilter> createVideoFilter() override;

            virtual agora_refptr<rtc::IVideoSinkBase> createVideoSink() override;
            
            int setExtensionVendor(std::string vendor);

        };
    }
}
