//
//  BRProcessor.h
//  TYSMExtension
//
//  Created by jele lam on 24/5/2021.
//

#include <AgoraRtcKit2/NGIAgoraExtensionControl.h>
#import <AgoraRtcKit2/AgoraExtObjects.h>

#include <AgoraRtcKit2/AgoraRefPtr.h>
#include <AgoraRtcKit2/AgoraMediaBase.h>

#include <thread>
#include <string>
#include <mutex>
#include <vector>

namespace TYSM {
    namespace Extension {
        class BRProcessor {
        public:
            
            bool initOpenGLES();
            
            bool releaseOpenGLES();
            
            int setParameters(std::string parameter);
            
            int processFrame(AgoraExtVideoFrame* capturedFrame);

            int releaseEffectEngine();

            std::thread::id getThreadId();
            
            int setExtensionControl(agora::rtc::IExtensionControl* control){
                control_ = control;
                return 0;
            };
            
            int setExtensionVendor(const char *id) {
                int len = std::string(id).length() +1;
                id_ = static_cast<char *>(malloc(len));
                memset(id_,0,len);
                strcpy(id_,id);
                return 0;
            }
            
        protected:
            ~Processor() {}
        private:
            
            void prepareCachedVideoFrame(AgoraExtVideoFrame* capturedFrame);
            void processEffect(AgoraExtVideoFrame* capturedFrame);

            AgoraExtVideoFrame* prevFrame_ = nil;
            
            unsigned char* yuvBuffer_ = nullptr;
            
            // 锁
            std::mutex mutex_;

            agora::rtc::IExtensionControl *control_;
            char * id_;
        };
    }
}

