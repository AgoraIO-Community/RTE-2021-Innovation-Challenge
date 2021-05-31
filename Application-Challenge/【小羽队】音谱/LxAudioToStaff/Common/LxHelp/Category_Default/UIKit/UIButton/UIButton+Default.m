//
//  UIButton+Default.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/31.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "UIButton+Default.h"
#import "UIImage+Default.h"

@implementation UIButton (Default)
/**
 *@description 设置默认button图片
 **/
+ (UIButton *)lx_defaultBtnWithImageName:(NSString *)imageName
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
    [btn setImage:image forState:UIControlStateNormal];
    btn.frame = CGRectMake(0, 0, image.size.width, image.size.height);
    return btn;
}

@end
