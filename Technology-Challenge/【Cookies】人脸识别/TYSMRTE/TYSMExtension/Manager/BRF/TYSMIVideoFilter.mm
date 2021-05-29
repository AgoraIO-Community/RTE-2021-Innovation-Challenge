//
//  TYSMIVideoFilter.m
//  TYSMExtension
//
//  Created by Jele on 19/5/2021.
//

#include "TYSMIVideoFilter.h"
#include <sstream>
#include <iostream>

namespace agora {
    namespace extension {

        TYSMIVideoFilter::TYSMIVideoFilter(agora_refptr<BRFProcessor> processor) {
            std::cout<< "初始化 VideoFilter" <<std::endl;

            brf_processor_ = processor;
        }

        TYSMIVideoFilter::~TYSMIVideoFilter() {
            std::cout<< "释放 VideoFilter" <<std::endl;
            brf_processor_->releaseEffectEngine();
        }

        bool TYSMIVideoFilter::adaptVideoFrame(const agora::media::base::VideoFrame &capturedFrame,
                             agora::media::base::VideoFrame &adaptedFrame) {
            
            if (brf_processor_ == nullptr) return false;
            // 处理 数据
            int ret = brf_processor_->processFrame(capturedFrame);
            // 处理结果
            if (ret) return false;
            // 将处理好的数据标记成 adaptedFrame
            adaptedFrame = capturedFrame;
            
            return true;
        }

        size_t TYSMIVideoFilter::setProperty(const char *key, const void *buf,
                                                 size_t buf_size) {
            std::cout<< __FUNCTION__ << key << buf <<std::endl;
            
            std::string stringParameter((char*)buf);
            
            brf_processor_->setParameters(stringParameter);
            return 0;
        }

        size_t TYSMIVideoFilter::getProperty(const char *key, void *buf, size_t buf_size) {
            std::cout<< __FUNCTION__ << key << buf <<std::endl;
            return 0;
        }

        void TYSMIVideoFilter::setEnabled(bool enable) {
            std::cout << __func__ << enable << std::endl;
        }

        bool TYSMIVideoFilter::isEnabled() {
            std::cout << __func__ << std::endl;
            return true;
        }
        
        /**
         * This function is invoked right before data stream starts.
         * Custom filter can override this function for initialization.
         * @return
         * - `true`: The initialization succeeds.
         * - `false`: The initialization fails.
         */
        bool TYSMIVideoFilter::onDataStreamWillStart() {
            std::cout << __func__ << std::endl;
            if (!isInitOpenGL) {
                isInitOpenGL = brf_processor_->initEffectEngine();
            }
            return isInitOpenGL;
        }
         /**
         * This function is invoked right before data stream stops.
         * Custom filter can override this function for deinitialization.
         */
        void TYSMIVideoFilter::onDataStreamWillStop() {
            std::cout << __FUNCTION__ << std::endl;
        }
    }
}
