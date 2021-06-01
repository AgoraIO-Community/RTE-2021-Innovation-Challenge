//
//  UIImageView+Default.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImageView (Default)
/**
 *@description 获取图片的imageView
 **@param imageName 图片名称
 *@return UIImageView
 **/
+ (UIImageView *)lx_imageViewWithImageName:(NSString *)imageName;
/**
 *@description 设置imageview的图片，自设置图片大小
 *@param imageName 图片名称
 **/
- (void)lx_imageViewWithImageName:(NSString *)imageName;
/**
 *@description 设置iamgeview的图片自设图片大小
 **/
- (void)lx_resetImage:(UIImage *)image;
@end
