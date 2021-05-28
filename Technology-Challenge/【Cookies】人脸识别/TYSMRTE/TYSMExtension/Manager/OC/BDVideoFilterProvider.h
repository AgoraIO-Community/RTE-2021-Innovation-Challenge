//
//  BDVideoFilterProvider.h
//  ByteDanceExtension
//
//  Created by LLF on 2020/10/21.
//

#pragma once

#include <AgoraRtcKit2/AgoraMediaFilterExtensionDelegate.h>
#include "BRProcessor.h"

@interface BDExtensionProvider : NSObject <AgoraExtProviderDelegate>

- (instancetype)initWithProcessor:(TYSM::Extension::BRProcessor *)processor;
- (NSInteger)log:(AgoraExtLogLevel)level message:(NSString * __nullable)message;
- (NSInteger)fireEvent:(NSString * __nonnull)vendor key:(NSString * __nullable)key value:(NSString * __nullable)value;

@end
