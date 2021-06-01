//
//  CATextLayer+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "CATextLayer+Default.h"
#import "UIFont+Default.h"

@implementation CATextLayer (Default)

#pragma mark - GetMethod
+ (instancetype)lx_getDefaultTextLayerWithFontSize:(CGFloat)fontSize
{
    CATextLayer *textLayer = [CATextLayer layer];
    textLayer.alignmentMode = kCAAlignmentCenter;
    textLayer.wrapped = NO;//换行
    
    UIFont *font = [UIFont boldSystemFontOfSize:fontSize];
    CFStringRef fontName = (__bridge CFStringRef)font.fontName;
    CGFontRef fontRef = CGFontCreateWithFontName(fontName);
    textLayer.font = fontRef;
    textLayer.fontSize = font.pointSize;
    CGFontRelease(fontRef);
    
    textLayer.contentsScale = [UIScreen mainScreen].scale;
    return textLayer;
}
/**
 *@description 修改对应textlayer字体信息
 *@font  字体
 *@textColor 字体颜色
 **/
- (void)lx_setupFont:(UIFont *)font
           textColor:(UIColor *)textColor
{
    CFStringRef fontName = (__bridge CFStringRef)font.fontName;
    CGFontRef fontRef = CGFontCreateWithFontName(fontName);
    self.font = fontRef;
    self.fontSize = font.pointSize;
    self.contentsScale = [UIScreen mainScreen].scale;
    self.foregroundColor = textColor.CGColor;
}

@end
