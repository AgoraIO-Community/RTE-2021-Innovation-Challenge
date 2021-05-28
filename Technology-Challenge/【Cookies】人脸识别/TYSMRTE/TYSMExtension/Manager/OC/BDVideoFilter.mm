//
//  BDVideoFilter.mm
//  TYSMExtension
//
//  Created by LLF on 2020/9/21.
//

#include "BDVideoFilter.h"
//#import "BDErrorCode.h"

@implementation BDVideoFilter {
  TYSM::Extension::BRProcessor* _processor;
  BOOL _opengl_released;
}

- (instancetype)initWithProcessor:(TYSM::Extension::BRProcessor *)processor {
  if (self = [super init]) {
    _processor = processor;
    _opengl_released = NO;
  }
  return self;
}

- (void)dealloc {
  if (_processor && !_opengl_released) {
    _processor->releaseOpenGLES();
  }
  _processor = nullptr;
}

- (BOOL)isUseCVPixelBuffer {
  return NO;
}

- (BOOL)adaptVideoFrame:(AgoraExtVideoFrame *)srcFrame dstFrame:(AgoraExtVideoFrame **)dstFrame {
  if (_processor) {
    *dstFrame = nil;
    _processor->processFrame(srcFrame);
    *dstFrame = srcFrame;
    return true;
  }
  return false;
}

- (BOOL)didDataStreamWillStart {
  if (_processor) {
    return _processor->initOpenGLES();
  }
  
  return false;
}

- (void)didDataStreamWillStop {
  if (_processor) {
    _processor->releaseOpenGLES();
    _opengl_released = YES;
  }
}

- (NSInteger)getPropertyWithKey:(NSString *)key value:(NSData **)value { return -1; }

- (BOOL)isEnabled { return false; }

- (void)setEnabled:(BOOL)enabled { }

- (NSInteger)setPropertyWithKey:(NSString * _Nonnull)key value:(NSData * _Nonnull)value {
  if ((!key || !key.length) || (!value || !value.length)) { return -1; }
  const void *buf = [value bytes];
  std::string param((char *)buf);
  _processor->setParameters(param);
  return 0;
}

@end

