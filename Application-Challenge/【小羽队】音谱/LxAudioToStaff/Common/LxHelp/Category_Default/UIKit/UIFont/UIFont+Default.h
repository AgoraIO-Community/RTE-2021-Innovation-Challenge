//
//  UIFont+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIFont (Default)

/**
 *@description 打印并显示所有字体
 **/
+(void)lx_showAllFonts;

/***********************************Normal Font************************************/
+ (UIFont *)lx_pingFangSCofSize:(CGFloat)size;
/**
 *@description 宋体(字体缺失)
 **/
+(UIFont *)lx_songTypefaceFontOfSize:(CGFloat)size;
/**
 *@description 喵体
 **/
+ (UIFont *)lx_hyMiaoFontOfSize:(CGFloat)size;
/**
 *@description 尚黑
 **/
+ (UIFont *)lx_shangGFontOfSize:(CGFloat)size;
/**
 *@description 汉仪素圆
 **/
+ (UIFont *)lx_hyShuYuanOfSize:(CGFloat)size;
/**
 *@description 小顽家体
 **/
+ (UIFont *)lx_xiaowanjiatiOfSize:(CGFloat)size;
/**
 *@description 胖体
 **/
+ (UIFont *)lx_pangtyFontOfSize:(CGFloat)size;

/**
 *@description 微软雅黑：正常字体
 **/
+ (UIFont *)lx_microsoftYaHeiFontOfSize:(CGFloat)size;

/**
 *@description 微软雅黑：加粗字体(字体缺失)
 **/
+ (UIFont *)lx_boldMicrosoftYaHeiFontOfSize:(CGFloat)size;
/**
 *@description 乐谱字体
 **/
+ (UIFont *)lx_guidoFontOfSize:(CGFloat)size;

/***********************************TextLayer Font************************************/
+ (void)lx_setLayer:(CATextLayer *)textLayer font:(UIFont *)font;

@end
