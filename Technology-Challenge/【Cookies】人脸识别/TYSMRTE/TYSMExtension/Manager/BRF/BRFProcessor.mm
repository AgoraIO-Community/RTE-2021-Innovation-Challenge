//
//  BRFProcessor.m
//  TYSMExtension
//
//  Created by jele lam on 23/5/2021.
//

#include "BRFProcessor.h"
#include <chrono>
#include "TYSMErrorCode.h"

#include "document.h"
#include "writer.h"
#include "stringbuffer.h"
#include "prettywriter.h"


#include "libyuv.h"
#import <CoreImage/CoreImage.h>

#include "brfv4/BRFManager.hpp"
#include "brfv4/image/BRFBitmapData.hpp"

#include "brfv4/ios/DrawingUtils.hpp"

#include "brfv4/utils/BRFv4PointUtils.hpp"
#include "brfv4/utils/BRFv4ExtendedFace.hpp"
#include "brfv4/examples/Basic_Example.hpp"

extern "C" bool initGL();
extern "C" bool releaseGL();
extern "C" bool makeCurrent();

using namespace::rapidjson;

namespace agora {
    namespace extension {
        
        class brf::BRFBasicCppExample *example;

        bool BRFProcessor::initEffectEngine() {
            const std::lock_guard<std::mutex> lock(mutex_);
            return initGL();
        }

        int BRFProcessor::releaseEffectEngine() {
            const std::lock_guard<std::mutex> lock(mutex_);
            
            smileEnable_ = yawnEnable_ = faceEnable_ = false;
            
            free(id_);
            id_ = nullptr;
            
            example->reset();
            
            free(control_);
            control_ = nullptr;
            
            return TYSMErrorCodeOK;
        }
        
        int BRFProcessor::processFrame(const agora::media::base::VideoFrame &capturedFrame) {
            const std::lock_guard<std::mutex> lock(mutex_);
            
            if (example->_initialized == false) return -1;
            
            int ret = 0;
            
            uint8 *argb = reinterpret_cast<uint8 *>(malloc(capturedFrame.width * capturedFrame.height * 4 *3 * sizeof(uint8)));
            
            ret = libyuv::I420ToBGRA(capturedFrame.yBuffer, capturedFrame.yStride,
                                     capturedFrame.uBuffer, capturedFrame.uStride,
                                     capturedFrame.vBuffer, capturedFrame.vStride,
                                     argb, capturedFrame.width * 4,
                                     capturedFrame.width, capturedFrame.height);
            
            if (ret) return -TYSMErrorCodeInvalidYUV;
            
            ret = processEffect(capturedFrame ,argb);
            
            if (ret) return ret;
            
            ret = libyuv::BGRAToI420(argb, capturedFrame.width *4,
                                     capturedFrame.yBuffer, capturedFrame.yStride,
                                     capturedFrame.uBuffer, capturedFrame.uStride,
                                     capturedFrame.vBuffer, capturedFrame.vStride,
                                     capturedFrame.width, capturedFrame.height);

            free(argb);
            argb = nullptr;
            
            if (ret) return -TYSMErrorCodeInvalidYUV;

            return TYSMErrorCodeOK;
        }
        
        void BRFProcessor::processFaceDetect() {
            example->updateFace();
        }
        
        void BRFProcessor::processSmileDetect() {
            double result = example->updateSmille();
            dataCallback("brf.smile.enable", std::to_string(result).c_str());
        }
        
        void BRFProcessor::processYawnDetect() {
            double result = example->updateYawn();
            dataCallback("brf.yawn.enable", std::to_string(result).c_str());
        }
        
        int BRFProcessor::processEffect(const agora::media::base::VideoFrame &capturedFrame, UInt8 *imageData) {
            
            if (makeCurrent() == false) return -TYSMErrorCodeInvalidEAGLContext;
            
            CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
            CGContextRef context = CGBitmapContextCreate(imageData, capturedFrame.width, capturedFrame.height, 8, capturedFrame.width * 4 , colorSpace,kCGImageAlphaPremultipliedFirst);
            
            example->_drawing._context = context;
            
            // Update example and draw the results into the context
            
            example->update(imageData);
            
            if (faceEnable_) {
                this->processFaceDetect();
            }
            
            if (smileEnable_) {
                this->processSmileDetect();
            }
            
            if (yawnEnable_) {
                this->processYawnDetect();
            }
            // Create a Quartz image from the pixel data in the bitmap graphics context
            // ... free up the context and color space
            // ... create an image object from the Quartz image
            // ... release stuff
            CGContextRelease(context);
            CGColorSpaceRelease(colorSpace);
            
            return TYSMErrorCodeOK;
        }
        
        void BRFProcessor::dataCallback(const char * _Nonnull key, const char * _Nonnull data) {
            if (control_ == nullptr) return;
            
            /// 响应本类事件给 客户端，客户端通过 AgoraMediaFilterEventDelegate 进行监听
            control_->fireEvent(id_, key, data);
        }
        
        __attribute__((optnone)) int BRFProcessor::setParameters(std::string parameter) {
            const std::lock_guard<std::mutex> lock(mutex_);
            control_->fireEvent(id_, __func__, parameter.c_str());
            
            Document d;
            d.Parse(parameter.c_str());
            
            if (d.HasMember("brf.init")) {
                Value& config = d["brf.init"];
                
                if (config.IsObject() == false) {
                    return -TYSMErrorCodeInvalidJSONType;
                }
                
                Value &widthValue = config["width"];
                Value &heightValue = config["height"];
                
                if (widthValue.IsInt() == false ||
                    heightValue.IsInt() == false) {
                    return -TYSMErrorCodeInvalidJSONType;
                }
                
                int height = heightValue.GetInt();
                int width = widthValue.GetInt();
                
                if (example == nullptr) {
                    example = new brf::BRFBasicCppExample();
                    example->configBmd(height, width, brf::ImageDataType::U8_BGRA);
                    example->configLayout(height, width);
                    example->configImageRoi(height, width);
                    example->configFaceDetictionRoi(height, width);
                    example->init(height, width);
                    
                    return -TYSMErrorCodeOK;
                }
                
                return -TYSMErrorCodeErrorParameter;
            }
            
            if (d.HasMember("brf.single_face.enable")) {
                Value& enabled = d["brf.single_face.enable"];
                if (enabled.IsBool() == false) {
                    return -TYSMErrorCodeInvalidJSONType;
                }
                faceEnable_ = enabled.GetBool();
            }
            
            if (d.HasMember("brf.smile.enable")) {
                Value& enabled = d["brf.smile.enable"];
                if (enabled.IsBool() == false) {
                    return -TYSMErrorCodeInvalidJSONType;
                }
                smileEnable_ = enabled.GetBool();
            }
            
            if (d.HasMember("brf.yawn.enable")) {
                Value& enabled = d["brf.yawn.enable"];
                if (enabled.IsBool() == false) {
                    return -TYSMErrorCodeInvalidJSONType;
                }
                yawnEnable_ = enabled.GetBool();
            }
            
            return TYSMErrorCodeOK;
        }
        
        std::thread::id BRFProcessor::getThreadId() {
            std::thread::id id = std::this_thread::get_id();
            return id;
        }
        
    }
}

