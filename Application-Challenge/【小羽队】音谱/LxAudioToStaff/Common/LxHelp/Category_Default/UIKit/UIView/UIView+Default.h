//
//  UIView+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger,LxViewScalePriDirection)
{
    LxViewScalePriCenter = 0,
    LxViewScalePriTop,
    LxViewScalePriLeft,
    LxViewScalePriRight,
    LxViewScalePriBottom,
};
@interface UIView (Default)
#pragma mark - 位置相关
/**
 *@description 设置中心点
 **/
- (void)lx_setCenter:(CGPoint)center;
/******************************************************ScaleAbout***************************************************************/
@property (copy, nonatomic,readonly) NSString *zoom_scale;
/**
 *@description 设置layer比例缩放(不能设置为0,不能与relative方法混合使用)
 **/
- (void)lx_zoomScale:(CGFloat)scale
scalePriScaleDirection:(LxViewScalePriDirection)priDirection;
/**
 *@description 相对于父视图的位置和大小缩放
 **/
- (void)lx_relativeToParentView:(UIView *)parentView
                      zoomScale:(CGFloat)scale;
/**
 *@description 重新设置比例大小(暂时只针对中心缩放的恢复)
 **/
- (void)lx_zoomScaleReset;


@end
