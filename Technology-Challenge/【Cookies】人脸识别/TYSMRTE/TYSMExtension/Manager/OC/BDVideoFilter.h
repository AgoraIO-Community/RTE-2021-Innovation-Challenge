//
//  BDVideoFilter.h
//  ByteDanceExtension
//
//  Created by LLF on 2020/9/21.
//

#pragma once

#import <AgoraRtcKit2/AgoraVideoFilterDelegate.h>
#include "BRProcessor.h"

@interface BDVideoFilter : NSObject <AgoraVideoFilterDelegate>
- (instancetype)initWithProcessor:(TYSM::Extension::BRProcessor *)processor;
@end
