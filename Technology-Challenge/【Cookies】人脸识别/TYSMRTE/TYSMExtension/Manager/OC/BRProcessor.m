//
//  BRProcessor.m
//  TYSMExtension
//
//  Created by jele lam on 24/5/2021.
//

#import "BRProcessor.h"
#include <iostream>
#import <Foundation/Foundation.h>
#include <chrono>

extern "C" bool initGL();
extern "C" bool releaseGL();
extern "C" bool makeCurrent();
extern "C" void dataCallback(NSString* data);
extern "C" void logMessage(int retval, NSString* message);


namespace TYSM {
    namespace Extension {
        
        int BRProcessor::processFrame(AgoraExtVideoFrame *capturedFrame) {
            
            return -1;
        }
        
        
        bool BRProcessor::initOpenGLES() {
            const std::lock_guard<std::mutex> lock(mutex_);
            
            return initGL();
        }
        
        bool BRProcessor::releaseOpenGLES() {
            const std::lock_guard<std::mutex> lock(mutex_);
            return releaseGL();
        }
        
        int BRProcessor::setParameters(std::string parameter) {
            return -1;
        }
        
        void BRProcessor::processEffect(AgoraExtVideoFrame *capturedFrame) {
            if (makeCurrent() == false) {
                
                return;
            }
        }
        
        void BRProcessor::prepareCachedVideoFrame(AgoraExtVideoFrame *capturedFrame) {
            int ysize = capturedFrame.yStride * capturedFrame.height;
            int usize = capturedFrame.uStride * capturedFrame.height / 2;
            int vsize = capturedFrame.vStride * capturedFrame.height / 2;
            if (yuvBuffer_ == nullptr ||
                prevFrame_.width != capturedFrame.width ||
                prevFrame_.height != capturedFrame.height ||
                prevFrame_.yStride != capturedFrame.yStride ||
                prevFrame_.uStride != capturedFrame.uStride ||
                prevFrame_.vStride != capturedFrame.vStride) {
                if (yuvBuffer_) {
                    free(yuvBuffer_);
                    yuvBuffer_ = nullptr;
                }
                yuvBuffer_ = (unsigned char*)malloc(ysize + usize + vsize);

            }
            
            // update YUV buffer
            memcpy(yuvBuffer_, capturedFrame.yBuffer, ysize);
            memcpy(yuvBuffer_ + ysize, capturedFrame.uBuffer, usize);
            memcpy(yuvBuffer_ + ysize + usize, capturedFrame.vBuffer, vsize);
            
        }
        
        CVPixelBufferRef coverPixelBufferFromYUV(unsigned char *yuvBuffer, int w, int h) {
            NSDictionary *pixelAttributes = @{(NSString*)kCVPixelBufferIOSurfacePropertiesKey:@{}};
            
            CVPixelBufferRef pixelBuffer = NULL;
            CVReturn err;
            err = CVPixelBufferCreate(kCFAllocatorDefault,
                                      w, h, kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange, (__bridge CFDictionaryRef)(pixelAttributes), &pixelBuffer);
            if (err != 0) {
                cout << "Error at CVPixelBufferCreate : ";
                cout << err << endl;
                return NULL;
            }
            CVPixelBufferLockBaseAddress(pixelBuffer,0);
            
            unsigned char *yDestPlane = (unsigned char *) CVPixelBufferGetBaseAddressOfPlane(pixelBuffer, 0);
            
            // Here y_ch0 is Y-Plane of YUV(NV12) data.
            unsigned char *y_ch0 = yuvBuffer;
            memcpy(yDestPlane, y_ch0, w * h);
            unsigned char *uvDestPlane = (unsigned char *)CVPixelBufferGetBaseAddressOfPlane(pixelBuffer, 1);
            
            // Here y_ch1 is UV-Plane of YUV(NV12) data.
            unsigned char *y_ch1 = yuvBuffer + w * h;
            memcpy(uvDestPlane, y_ch1, w * h/2);
            CVPixelBufferUnlockBaseAddress(pixelBuffer, 0);
            
            if (err != kCVReturnSuccess) {
                NSLog(@"Unable to create cvpixelbuffer %d", err);
            }
            
            return pixelBuffer;
        }
        
        int releaseEffectEngine() {
            return -1;
        }
        
        
    }
}
