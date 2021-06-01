//
//  UIView+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "UIView+Default.h"
#import <objc/runtime.h>
@implementation UIView (Default)
static char zoomScaleStr;
#pragma mark - 位置相关
/**
 *@description 设置中心点
 **/
- (void)lx_setCenter:(CGPoint)center
{
    self.frame = CGRectMake(center.x - CGRectGetWidth(self.frame)/2.f,
                            center.y - CGRectGetHeight(self.frame)/2.f,
                            CGRectGetWidth(self.frame),
                            CGRectGetHeight(self.frame));
}
#pragma mark - SetMethod
- (void)setZoom_scale:(NSString *)zoom_scale
{
    objc_setAssociatedObject(self, &zoomScaleStr, zoom_scale, OBJC_ASSOCIATION_ASSIGN);
}
#pragma mark - GetMethod
- (NSString *)zoom_scale
{
    NSString *scale = objc_getAssociatedObject(self, &zoomScaleStr);

    return [scale floatValue] > 0 ? scale :@"1";
}

- (void)lx_zoomScaleReset
{
    CGFloat scale = 1 / [self.zoom_scale floatValue];
    [self lx_zoomScale:scale
scalePriScaleDirection:LxViewScalePriCenter];
}
#pragma mark - CallFunction
/******************************************************ScaleAbout***************************************************************/
/**
 *@description 设置layer比例缩放
 **/
- (void)lx_zoomScale:(CGFloat)scale
scalePriScaleDirection:(LxViewScalePriDirection)priDirection
{
    
    CGFloat newWidth = CGRectGetWidth(self.frame) * (1 / [self.zoom_scale floatValue]) * scale;
    CGFloat newHeight = CGRectGetHeight(self.frame) * (1 / [self.zoom_scale floatValue]) * scale;
    self.zoom_scale = [NSString stringWithFormat:@"%f",scale];
    switch (priDirection) {
        case LxViewScalePriCenter:
        {
            self.frame = CGRectMake(CGRectGetMidX(self.frame) - newWidth/2.f,
                                    CGRectGetMidY(self.frame) - newHeight/2.f,
                                    newWidth,
                                    newHeight);
        }
            break;
        case LxViewScalePriTop:
        {
            self.frame = CGRectMake(CGRectGetMidX(self.frame) - newWidth/2.f,
                                    CGRectGetMinY(self.frame),
                                    newWidth,
                                    newHeight);
        }
            break;
        case LxViewScalePriLeft:
        {
            self.frame = CGRectMake(CGRectGetMinX(self.frame),
                                    CGRectGetMinY(self.frame),
                                    newWidth,
                                    newHeight);
        }
            break;
        case LxViewScalePriRight:
        {
            self.frame = CGRectMake(CGRectGetMaxX(self.frame) - newWidth,
                                    CGRectGetMidY(self.frame) - newHeight/2.f, newWidth, newHeight);
        }
            break;
        case LxViewScalePriBottom:
        {
            self.frame = CGRectMake(CGRectGetMidX(self.frame) - newWidth/2.f,
                                    CGRectGetMaxY(self.frame) - newHeight,
                                    newWidth,
                                    newHeight);
        }
            break;
        default:
            break;
    }
}

/**
 *@description 相对于父视图的位置和大小缩放
 **/
- (void)lx_relativeToParentView:(UIView *)parentView
                      zoomScale:(CGFloat)scale;
{
    CGPoint scalePoint = CGPointMake(self.center.x / CGRectGetWidth(parentView.frame), self.center.y / CGRectGetHeight(parentView.frame));
    self.center = CGPointMake(CGRectGetWidth(parentView.frame) * (1 / [self.zoom_scale floatValue] * scale) * scalePoint.x, CGRectGetHeight(parentView.frame) * (1 / [self.zoom_scale floatValue] * scale) * scalePoint.y);
    [self lx_zoomScale:scale scalePriScaleDirection:LxViewScalePriCenter];
}
@end
