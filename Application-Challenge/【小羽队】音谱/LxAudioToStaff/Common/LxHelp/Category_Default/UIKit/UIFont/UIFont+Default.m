//
//  UIFont+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "UIFont+Default.h"

@implementation UIFont (Default)

#pragma mark  打印并显示所有字体
+(void)lx_showAllFonts{
    NSArray *familyNames = [UIFont familyNames];
    for( NSString *familyName in familyNames ){
        debugLog( @"Family: %s \n", [familyName UTF8String] );
        NSArray *fontNames = [UIFont fontNamesForFamilyName:familyName];
        for( NSString *fontName in fontNames ){
            debugLog( @"\tFont: %s \n", [fontName UTF8String] );
        }
    }
}


#pragma mark  宋体
+(UIFont *)lx_songTypefaceFontOfSize:(CGFloat)size{
    
    return [UIFont fontWithName:@"经典宋体简" size:size];
    
}

#pragma mark  微软雅黑
+(UIFont *)lx_microsoftYaHeiFontOfSize:(CGFloat)size{
    return [UIFont fontWithName:@"MicrosoftYaHei" size:size];
}
#pragma mark 尚黑
+ (UIFont *)lx_shangGFontOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"RTWSShangGoG0v1-Bold" size:size];
}

#pragma mark 汉仪素圆
+ (UIFont *)lx_hyShuYuanOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"HYShuYuanHeiJ" size:size];
}
#pragma mark 小顽家体
+ (UIFont *)lx_xiaowanjiatiOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"xiaowanjiati" size:size];
}
#pragma mark  微软雅黑：加粗字体
+(UIFont *)lx_boldMicrosoftYaHeiFontOfSize:(CGFloat)size{
    return [UIFont fontWithName:@"MicrosoftYaHei-Bold" size:size];
}

+ (UIFont *)lx_hyMiaoFontOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"HYLeMiaoTiW" size:size];
}
#pragma mark - 胖体
+ (UIFont *)lx_pangtyFontOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"CloudPangToYuGBK" size:size];
}

+ (UIFont *)lx_pingFangSCofSize:(CGFloat)size{
    return [UIFont fontWithName:@"PingFangSC-Regular" size:size];
}

#pragma mark - 乐谱字体
+ (UIFont *)lx_guidoFontOfSize:(CGFloat)size
{
    return [UIFont fontWithName:@"guido2" size:size];
    
}


+ (void)lx_setLayer:(CATextLayer *)textLayer font:(UIFont *)font
{
    CFStringRef fontName = (__bridge CFStringRef)font.fontName;
    CGFontRef fontRef = CGFontCreateWithFontName(fontName);
    textLayer.font = fontRef;
    textLayer.fontSize = font.pointSize;
    CGFontRelease(fontRef);
}

@end
