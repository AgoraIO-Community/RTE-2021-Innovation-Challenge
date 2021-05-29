//
//  BRFProcessor.h
//  TYSMExtension
//
//  Created by jele lam on 23/5/2021.
//

#include <thread>
#include <string>
#include <mutex>
#include <vector>
#include <iostream>

#include <AgoraRtcKit2/AgoraRefPtr.h>
#include <AgoraRtcKit2/NGIAgoraExtensionControl.h>
#include <AgoraRtcKit2/AgoraMediaBase.h>

#import <AgoraRtcKit2/AgoraExtObjects.h>

#import "BRFManager.hpp"




namespace agora {
    namespace extension {

        class BRFProcessor : public RefCountInterface {
            
        public:
            
            bool initEffectEngine();

            int releaseEffectEngine();

            int processFrame(const agora::media::base::VideoFrame &capturedFrame);
            
            int setParameters(std::string parameter);
            std::thread::id getThreadId();
            
            int setExtensionControl(agora::rtc::IExtensionControl* _Nonnull control){
                control_ = control;
                return 0;
            };
            
            int setExtensionVendor(const char * _Nonnull id) {
                int len = (int)std::string(id).length() +1;
                id_ = static_cast<char *>(malloc(len));
                memset(id_,0,len);
                strcpy(id_,id);
                return 0;
            }
            
        private:
            int processEffect(const agora::media::base::VideoFrame &capturedFrame, UInt8 * _Nonnull imageData);

            /// 识别 脸部特征
            void processFaceDetect();
            
            /// 识别 微笑表情
            void processSmileDetect();
            
            /// 识别 张嘴表情
            void processYawnDetect();
            
            /// 数据回调 客户端监听 onEvent 可见
            /// @param data 数据内容
            /// @param key 事件标识
            void dataCallback(const char* _Nonnull key,const char* _Nonnull data);
            
            /// 脸部特征 开关
            bool faceEnable_ = false;
            /// 微笑表情 开关
            bool smileEnable_ = false;
            /// 张口表情 开关
            bool yawnEnable_ = false;
            
            std::mutex mutex_;

            agora::rtc::IExtensionControl * _Nullable control_;
            
            char * _Nullable id_;
        };
    }
}
