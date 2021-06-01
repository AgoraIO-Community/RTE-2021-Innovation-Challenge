//
//  NSObject+Default.m
//  LxChristmasCard
//
//  Created by DavinLee on 2017/12/21.
//  Copyright © 2017年 DavinLee. All rights reserved.
//

#import "NSObject+Default.h"

@implementation NSObject (Default)
#pragma mark - 计算、计量类
/**
 *@description 设置frame比例且中心点重新确认
 *@param frame 需要设置的frame
 *@param scale 需要设置的比例
 *@return 重设置后的frame
 **/
+ (CGRect)lx_scaleFrame:(CGRect)frame
                  scale:(CGFloat)scale
{
    CGPoint center = CGPointMake(CGRectGetMidX(frame), CGRectGetMidY(frame));
    CGSize newSize = CGSizeMake(CGRectGetWidth(frame) * scale, CGRectGetHeight(frame) * scale);
    return CGRectMake(center.x - newSize.width/2.f,
                      center.y - newSize.height/2.f,
                      newSize.width,
                      newSize.height);
}
/**
 *@description 获取对象内存地址
 **/
- (NSString *)lx_objcAddress
{
    return [NSString stringWithFormat:@"%p",self];
}

/** 确保没有导致崩溃的字符 **/
- (NSString *)lx_SafeStrWithStr
{
    NSString *tempStr = [self copy];
    if (tempStr == nil || [tempStr isKindOfClass:[NSNull class]]) {
        debugLog(@"出现了空字符");
        tempStr = @"";
    }else if ([self isKindOfClass:[NSNumber class]]){
        tempStr = [NSString stringWithFormat:@"%ld",((NSNumber *)self).integerValue];
    }
    return tempStr;
}

@end
