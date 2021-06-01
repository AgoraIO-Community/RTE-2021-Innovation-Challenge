//
//  NSTimer+EOCBlocksSupport.h
//  QHDanumuDemo
//
//  Created by chen on 15/7/10.
//  Copyright (c) 2015年 chen. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 生成一个不会导致循环依赖的Timer
 */
@interface NSTimer (EOCBlocksSupport)

/**
 生成一个不会导致循环依赖的Timer

 @param interval 触发时间
 @param block 触发后的操作
 @param repeats 是否重复触发
 @return timer
 */
+ (NSTimer *)eoc_scheduledTimerWithTimeInterval:(NSTimeInterval)interval block:(void (^)(void))block repeats:(BOOL)repeats;

@end
