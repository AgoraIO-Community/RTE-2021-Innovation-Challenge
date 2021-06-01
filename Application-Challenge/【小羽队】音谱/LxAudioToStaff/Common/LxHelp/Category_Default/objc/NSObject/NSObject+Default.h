//
//  NSObject+Default.h
//  LxChristmasCard
//
//  Created by DavinLee on 2017/12/21.
//  Copyright © 2017年 DavinLee. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>

//弧度转角度
#define Radians_To_Degrees(radians) ((radians) * (180.0 / M_PI))
//角度转弧度
#define Degrees_To_Radians(angle) ((angle) / 180.0 * M_PI)
@interface NSObject (Default)
#pragma mark - 计算、计量类
/**
 *@description 设置frame比例且中心点重新确认
 *@param frame 需要设置的frame
 *@param scale 需要设置的比例
 *@return 重设置后的frame
 **/
+ (CGRect)lx_scaleFrame:(CGRect)frame
                  scale:(CGFloat)scale;
/**
 *@description 获取对象内存地址
 **/
- (NSString *)lx_objcAddress;


/** 确保没有导致崩溃的字符 **/
- (NSString *)lx_SafeStrWithStr;

@end
