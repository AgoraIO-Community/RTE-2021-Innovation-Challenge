//
//  CAShapeLayer+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/5/18.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "CAShapeLayer+Default.h"
#import <UIKit/UIKit.h>

@implementation CAShapeLayer (Default)

#pragma mark - GetMethod
+ (CAShapeLayer *)lx_roundedRectangleWithSize:(CGSize)rectangSize corners:(CGFloat)corners
{
    CAShapeLayer *layer = [CAShapeLayer layer];
    UIBezierPath *path = [UIBezierPath bezierPathWithRoundedRect:CGRectMake(0, 0, rectangSize.width, rectangSize.height) cornerRadius:corners];
    layer.path = path.CGPath;
    
    return layer;
}
/**
 *@description 获取线条
 *@param direction 线条朝向
 *@param width 横向则为线条长度，纵向则为线条粗细
 *@param height 横向为线条粗细，纵向则为线条长度
 *@return CAShapeLayer
 **/
+ (CAShapeLayer *)lx_defaultLineLayerWithDirection:(LxShapeLayerDirectionType)direction
                                             width:(CGFloat)width
                                            height:(CGFloat)height
{
    CAShapeLayer *shapeLayer = [CAShapeLayer layer];
    shapeLayer.lineCap = kCALineCapSquare;
    UIBezierPath *path = [UIBezierPath bezierPath];
   
    switch (direction) {
        case LxShapeLayerDirectionHor:
        {
            shapeLayer.lineWidth = height;
             [path moveToPoint:CGPointMake(0, height/2.f)];
            [path addLineToPoint:CGPointMake(width, height/2.f)];
        }
            break;
            case LxShapeLayerDirectionVer:
        {
            shapeLayer.lineWidth = width;
            [path moveToPoint:CGPointMake(width/2.f, 0)];
            [path addLineToPoint:CGPointMake(width/2.f, height)];
        }
            break;
        default:
            break;
    }
    shapeLayer.path = path.CGPath;
    
    return shapeLayer;
}

@end
