//
//  CAShapeLayer+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/18.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
typedef NS_ENUM(NSInteger,LxShapeLayerDirectionType)
{
    LxShapeLayerDirectionHor = 0,
    LxShapeLayerDirectionVer = 1,
};
@interface CAShapeLayer (Default)

/**
 *@description 获取带圆角矩形
 *@param rectangSize 矩形大小
 *@param corners 圆角
 **/
+ (CAShapeLayer *)lx_roundedRectangleWithSize:(CGSize)rectangSize corners:(CGFloat)corners;
/**
 *@description 获取线条
 *@param direction 线条朝向
 *@param width 横向则为线条长度，纵向则为线条粗细
 *@param height 横向为线条粗细，纵向则为线条长度
 *@return CAShapeLayer
 **/
+ (CAShapeLayer *)lx_defaultLineLayerWithDirection:(LxShapeLayerDirectionType)direction
                                             width:(CGFloat)width
                                            height:(CGFloat)height;
@end
