//
//  UIImageView+Default.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "UIImageView+Default.h"
#import "UIImage+Default.h"

@implementation UIImageView (Default)
#pragma mark - CallFunction
/**
 *@description 获取图片的imageView
 **@param imageName 图片名称
 *@return UIImageView
 **/
+ (UIImageView *)lx_imageViewWithImageName:(NSString *)imageName
{
    UIImageView *imageView = [[UIImageView alloc] init];
    [imageView lx_imageViewWithImageName:imageName];
    return imageView;
}

- (void)lx_imageViewWithImageName:(NSString *)imageName
{
    UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
    [self lx_resetImage:image];
}

/**
 *@description 设置iamgeview的图片自设图片大小
 **/
- (void)lx_resetImage:(UIImage *)image
{
    self.image = image;
    CGRect rect = self.frame;
    rect.size = CGSizeMake(image.size.width, image.size.height);
    self.frame = rect;
}
@end
