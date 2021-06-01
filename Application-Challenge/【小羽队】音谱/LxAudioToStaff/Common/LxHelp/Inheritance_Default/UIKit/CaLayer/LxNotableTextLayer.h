//
//  LxNotableTextLayer.h
//  SmartPiano
//
//  Created by DavinLee on 2018/5/29.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger,LxNTlayerScroDirectin)
{
    LxNTlayerScroHorLeft = 0,
    LxNTlayerScroVerDown = 1,
};
@interface LxNotableTextLayer : CATextLayer
/**
 *@description 设置字体、颜色
 **/
- (void)lx_setFont:(UIFont *)font textColor:(UIColor *)textColor;
/**
 *@description 开始滚动
 *@param scroDirection 滚动方向
 *@param duration 滚动时间
 *@param succession 是否直接空几个单位后直接出现滚动
 **/
- (void)lx_startScrollWithDirection:(LxNTlayerScroDirectin)scroDirection
                           duration:(CGFloat)duration
                         succession:(BOOL)succession;
/**
 *@description 停止滚动
 **/
- (void)lx_stopScroll;
@end
