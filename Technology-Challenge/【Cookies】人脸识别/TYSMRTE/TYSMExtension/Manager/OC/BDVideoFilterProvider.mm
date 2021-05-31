//
//  BDVideoFilterProvider.m
//  ByteDanceExtension
//
//  Created by LLF on 2020/10/21.
//

#import "BDVideoFilterProvider.h"
#include "BDVideoFilter.h"

@implementation BDExtensionProvider {
    TYSM::Extension::BRProcessor* _processor;
    id<AgoraExtControlDelegate> _extControl;
}

- (instancetype)initWithProcessor:(TYSM::Extension::BRProcessor *)processor {
    if (self = [super init]) {
        _processor = processor;
    }
    return self;
}

- (NSInteger)log:(AgoraExtLogLevel)level message:(NSString * __nullable)message {
    if (!message || !message.length) { return -1; }
    if (_extControl) {
        return [_extControl log:level message:message];
    }
    
    return -1;
}

- (NSInteger)fireEvent:(NSString * __nonnull)vendor key:(NSString * __nullable)key value:(NSString * __nullable)value {
    if (!vendor || !vendor.length) { return -1; }
    if (_extControl) {
        return [_extControl fireEvent:vendor key:key value:value];
    }
    
    return -1;
}

- (AgoraExtProviderType)extType {
    return AgoraExtProviderTypeLocalVideoFilter;
}

- (void)setExtensionControl:(id<AgoraExtControlDelegate> __nullable)control {
    _extControl = control;
}

- (id<AgoraAudioFilterDelegate> __nullable)createAudioFilter {
    return nil;
}

- (id<AgoraVideoFilterDelegate> __nullable)createVideoFilter {
    if (!_processor) { return nil; }
    return [[BDVideoFilter alloc] initWithProcessor:_processor];
}

- (id<AgoraVideoSinkDelegate> __nullable)createVideoSink {
    return nil;
}

@end
