//
//  CALayer+Default.h
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger,LxCalayerScalePriDirection)
{
    LxCalayerScalePriCenter = 0,
    LxCalayerScalePriTop,
    LxCalayerScalePriLeft,
    LxCalayerScalePriRight,
    LxCalayerScalePriBottom,
};
@interface CALayer (Default)
/***********************************Image************************************//**
 *@description 设置图片
 *@param image 设置的图片对象
 **/
- (void)lx_setImage:(UIImage *)image;

//- (void)lx_setImageScale:(UIImage *)image;
/**
 *@description 根据图片名称设置图片
 *@param imageName 图片名称
 *@return 图片
 **/
- (UIImage *)lx_setImageWithImageName:(NSString *)imageName;
- (void)setScaleImage:(NSString *)imageName;
/**
 *@description 获取图片
 **/
- (UIImage *)lx_snapshotImage;
/**
 *@description 获取图片,默认居中
 *@param edge 截取图片画板内容个方向的增量
 **/
- (UIImage *)lx_snapshotImageWithOffsetEdge:(UIEdgeInsets)edge;
/** *********************************************   shadow  *********************************************  **/
/** Lx description   设置阴影  **/
- (void)lx_shadowWithCornerRadius:(CGFloat)CornerRadius
                shadowColor:(UIColor *)shadowColor
                    opacity:(CGFloat)opacity
                     radius:(CGFloat)radius
                     offset:(CGSize)offset;

/***********************************coreAnimation************************************/

/**
 *@description 暂停动画
 **/
- (void)lx_pauseAnimation;
/**
 *@description 重新开始动画
 **/
- (void)lx_resumeAnimation;
/***********************************base************************************/
- (NSString *)lx_address;
/******************************************************ScaleAbout***************************************************************/
@property (copy, nonatomic,readonly) NSString *zoomScale;
/**
 *@description 设置layer比例缩放
 **/
- (void)lx_zoomScale:(CGFloat)scale
scalePriScaleDirection:(LxCalayerScalePriDirection)priDirection;
/**
 *@description 重新设置比例大小(暂时只针对中心缩放的恢复)
 **/
- (void)lx_zoomScaleReset;
/** Lx description   临时存放，快速实现白色粒子效果  **/
+ (CAEmitterLayer *)lx_quickEmitter;

- (void)lx_setImageScale:(UIImage *)image;
@end
