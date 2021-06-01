//
//  UIImage+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/6/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "UIImage+Default.h"


@implementation UIImage (Default)
#pragma mark - GetMethod
+ (UIImage *)lx_imageFromBundleWithName:(NSString *)imageName
{
    NSString *tempPath ;
    
    if ([imageName containsString:@".jpg"]) {
        tempPath = [[NSBundle mainBundle] pathForResource:[imageName stringByReplacingOccurrencesOfString:@".jpg" withString:@""] ofType:@"jpg"];
    }else
    {
        if ([imageName rangeOfString:@"@2x"].location == NSNotFound) {
            tempPath = [[NSBundle mainBundle] pathForResource:[imageName stringByAppendingString:@"@2x"] ofType:@"png"];
        }else
        {
            tempPath = [[NSBundle mainBundle] pathForResource:imageName ofType:@"png"];
        }
    }
    UIImage *image = [UIImage imageWithContentsOfFile:tempPath];
    if (image == nil) {
        image = [UIImage imageNamed:imageName];
        if (image) {
            return image;
        }else
        {
            debugLog(@"出现空的图片%@",imageName);
            return [UIImage imageNamed:@"Loading...@2x"];
        }
    }
    NSAssert(image, @"出现空的图片%@",imageName);
    
    return image;
}

- (UIImage *)lx_imageWithTintColor:(UIColor *)tintColor
{
    return [self lx_imageWithTintColor:tintColor blendMode:kCGBlendModeDestinationIn];
}


#pragma mark - Function
- (UIImage *)lx_imageWithTintColor:(UIColor *)tintColor blendMode:(CGBlendMode)blendMode
{
    //We want to keep alpha, set opaque to NO; Use 0.0f for scale to use the scale factor of the device’s main screen.
    UIGraphicsBeginImageContextWithOptions(self.size, NO, 0.0f);
    [tintColor setFill];
    CGRect bounds = CGRectMake(0, 0, self.size.width, self.size.height);
    UIRectFill(bounds);
    
    //Draw the tinted image in context
    [self drawInRect:bounds blendMode:blendMode alpha:1.0f];
    
    if (blendMode != kCGBlendModeDestinationIn) {
        [self drawInRect:bounds blendMode:kCGBlendModeDestinationIn alpha:1.0f];
    }
    
    UIImage *tintedImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return tintedImage;
}

@end
