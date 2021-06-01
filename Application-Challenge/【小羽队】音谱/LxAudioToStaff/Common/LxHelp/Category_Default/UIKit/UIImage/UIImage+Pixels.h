//
//  UIImage+Pixels.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/18.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Pixels)
/**
 *@description 检测图片中有多少个非透明像素
 **/
- (double) checkPixelsWithImage;
@end
