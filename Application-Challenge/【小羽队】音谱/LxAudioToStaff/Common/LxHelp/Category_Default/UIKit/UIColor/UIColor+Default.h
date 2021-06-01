//
//  UIColor+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>
#define lx_UIColorFrom_HexStr(aStr) [UIColor lx_colorWithHexString:aStr]  //emp. @"#f83900"
#define lx_UIColorFrom_Rgb(r, g, b) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:1.0]
@interface UIColor (Default)
/**
 *@description 获取颜色根据十六进制(格式)@"#000000"
 **/
+ (UIColor *)lx_colorWithHexString: (NSString *)color;

/**
 *@description 获取基础颜色棕色
 **/
+ (UIColor *)lx_DefaultBlackColor;
/**
 *@description 获取课堂三册文本颜色
 **/
+ (UIColor *)lx_defaultBrownColor;
/**
 *@description 获取课堂四册文本颜色
 **/
+ (UIColor *)lx_defaultWhiteColor;
/**
 *@description 获取暗蓝色
 **/
+ (UIColor *)lx_DefaultDarkBlueColor;
/**
 *@description 获取随机颜色
 **/
+ (UIColor *)lx_randomColor;
/** Lx description   获取对应RGB颜色  **/
+ (UIColor *)lx_rbgColorWithR:(NSInteger)r G:(NSInteger)g B:(NSInteger)b alpha:(CGFloat)alpha;
@end
