//
//  NSTimer+QHEOCBlocksSupport.h
//  QHChatScreenDemo
//
//  Created by Anakin chen on 2018/2/6.
//  Copyright © 2018年 Anakin Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 生成一个不会导致循环依赖的Timer
 */
@interface NSTimer (QHEOCBlocksSupport)

/**
 生成一个不会导致循环依赖的Timer
 
 @param interval 触发时间
 @param block 触发后的操作
 @param repeats 是否重复触发
 @return timer
 */
+ (NSTimer *)qheoc_scheduledTimerWithTimeInterval:(NSTimeInterval)interval block:(void (^)(void))block repeats:(BOOL)repeats;

@end
