//
//  NSTimer+QHEOCBlocksSupport.m
//  QHChatScreenDemo
//
//  Created by Anakin chen on 2018/2/6.
//  Copyright © 2018年 Anakin Network Technology. All rights reserved.
//

#import "NSTimer+QHEOCBlocksSupport.h"

@implementation NSTimer (QHEOCBlocksSupport)

+ (NSTimer *)qheoc_scheduledTimerWithTimeInterval:(NSTimeInterval)interval block:(void (^)(void))block repeats:(BOOL)repeats {
    return [self scheduledTimerWithTimeInterval:interval target:self selector:@selector(qheoc_blockInvoke:) userInfo:[block copy] repeats:repeats];
}

+ (void)qheoc_blockInvoke:(NSTimer *)timer {
    void (^block)(void) = timer.userInfo;
    if (block) {
        block();
    }
}

@end
