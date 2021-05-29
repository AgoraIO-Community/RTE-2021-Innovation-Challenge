//
//  TYSMIVideoFilter.h
//  TYSMExtension
//
//  Created by Jele on 19/5/2021.
//

#include "AgoraRtcKit2/NGIAgoraMediaNode.h"
#include <AgoraRtcKit2/AgoraRefCountedObject.h>
#include "AgoraRtcKit2/AgoraRefPtr.h"
//#include "BRFProcessor.h"
#include "BRFProcessor.h"

namespace agora {
    namespace extension {
        
        class TYSMIVideoFilter : public agora::rtc::IVideoFilter {
        public:
            TYSMIVideoFilter(agora_refptr<BRFProcessor> processor);

            ~TYSMIVideoFilter();
            
            virtual bool adaptVideoFrame(const agora::media::base::VideoFrame &capturedFrame,
                                         agora::media::base::VideoFrame &adaptedFrame) override;
            
            virtual void setEnabled(bool enable) override;
//
            virtual  bool isEnabled() override;
            
            virtual size_t setProperty(const char *key, const void *buf, size_t buf_size) override;
            
            virtual size_t getProperty(const char *key, void *buf, size_t buf_size) override;
                        
            //
        private:
            
            agora_refptr<BRFProcessor> brf_processor_;
            
            bool isInitOpenGL = false;
            bool enable_effect_ = false;
            
            
            virtual bool onDataStreamWillStart() override;
            virtual void onDataStreamWillStop() override;
            
        protected:
            TYSMIVideoFilter() = default;
            
        };
    }
}
