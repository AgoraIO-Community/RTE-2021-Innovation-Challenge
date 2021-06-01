//
//  CATextLayer+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>

@interface CATextLayer (Default)
/**
 *@description 获取基础textLayer
 *@param fontSize 字体大小
 **/
+ (instancetype)lx_getDefaultTextLayerWithFontSize:(CGFloat)fontSize;
/**
 *@description 修改对应textlayer字体信息
 *@font  字体
 *@textColor 字体颜色
 **/
- (void)lx_setupFont:(UIFont *)font
           textColor:(UIColor *)textColor;



@end
