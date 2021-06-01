//
//  UIImage+Pixels.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/18.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "UIImage+Pixels.h"
#define Clamp255(a) (a>255 ? 255 : a)
@implementation UIImage (Pixels)
- (double) checkPixelsWithImage {
    CGImageRef imageRef = [self CGImage];
    NSUInteger width = CGImageGetWidth(imageRef);
    NSUInteger height = CGImageGetHeight(imageRef);
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    Byte *rawData = malloc(height * width * 4);
    NSUInteger bytesPerPixel = 4;
    NSUInteger bytesPerRow = bytesPerPixel * width;
    NSUInteger bitsPerComponent = 8;
    CGContextRef context = CGBitmapContextCreate(rawData, width, height,
                                                 bitsPerComponent, bytesPerRow, colorSpace,
                                                 kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
    
    CGContextDrawImage(context, CGRectMake(0, 0, width, height), imageRef);
    CGContextRelease(context);
    
    int byteIndex = 0;
    double count = 0;
    double totalCount = 0;
    for (int ii = 0 ; ii < width * height ; ++ii)
    {
        
        CGFloat alpha = (CGFloat)rawData[byteIndex + 3]/255.0f;
        CGFloat red = (CGFloat)rawData[byteIndex] ;
        CGFloat green = (CGFloat)rawData[byteIndex + 1] ;
        CGFloat blue = (CGFloat)rawData[byteIndex + 2] ;
        
        byteIndex += 4;
        //        if (alpha > 0 && (red < 255 && red > 0 && green > 0 && green < 255 && blue > 0 && blue < 255)) {
        //            count ++;
        ////             debugLog(@"r = %f, g = %f, b = %f, a = %f",red,green, blue, alpha);
        //        }
        //        if (alpha > 0 && red == 0 && green == 0 && blue == 0) {
        //            count ++;
        //        }
        if (alpha > 0 && red < 245 && blue < 245 && green < 245) {
            count ++;
        }
        totalCount ++;
    }
    debugLog(@"数量%f  总数%f  width = %f  height = %f",count,totalCount,self.size.width,self.size.height);
    return count;
    
}


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



@end
