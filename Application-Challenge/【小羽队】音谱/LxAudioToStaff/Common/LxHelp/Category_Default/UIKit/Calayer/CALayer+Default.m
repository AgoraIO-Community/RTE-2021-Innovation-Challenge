//
//  CALayer+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/5/9.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "CALayer+Default.h"
#import "UIImage+Default.h"
#import <objc/runtime.h>


@implementation CALayer (Default)
static char zoomScaleStr;
#pragma mark - SetMethod
- (void)lx_setImage:(UIImage *)image
{
    self.contents = (id)[image CGImage];
//    CGRect rect = self.frame;
//    rect.size = image.size;
//    self.frame = rect;
}

- (void)lx_setImageScale:(UIImage *)image {
    self.contents = (id)[image CGImage];
    CGRect rect = self.frame;
    rect.size = CGSizeMake(image.size.width * kMcStaffScale, image.size.height * kMcStaffScale);//image.size;
    self.frame = rect;
}

- (void)lx_shadowWithCornerRadius:(CGFloat)CornerRadius
                shadowColor:(UIColor *)shadowColor
                    opacity:(CGFloat)opacity
                     radius:(CGFloat)radius
                     offset:(CGSize)offset;{
    
//    CGRect frame = self.frame;
//    frame = CGRectInset(frame, -inset.x, -inset.y);
//    self.shadowPath = [UIBezierPath bezierPathWithRect:frame].CGPath;
    self.shadowColor = shadowColor.CGColor;
    self.shadowOpacity = opacity;
    self.shadowOffset = offset;
    self.shadowRadius = radius;
    self.cornerRadius = CornerRadius;
}

//- (void)lx_setImageScale:(UIImage *)image {
//    self.contents = (id)[image CGImage];
//    CGRect rect = self.frame;
//    rect.size = CGSizeMake(image.size.width * kMcStaffScale, image.size.height * kMcStaffScale);//image.size;
//    self.frame = rect;
//}
//
- (void)setScaleImage:(NSString *)imageName {
    UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
    if (image) {
        [self lx_setImage:image];
        self.contents = (id)[image CGImage];
        CGSize size = CGSizeMake(image.size.width * kMcStaffScale, image.size.height * kMcStaffScale);
        CGRect rect = self.frame;
        rect.size = size;
        self.frame = rect;
    }
}

- (void)setZoomScale:(NSString *)zoomScale
{
    objc_setAssociatedObject(self, &zoomScaleStr, zoomScale, OBJC_ASSOCIATION_COPY_NONATOMIC);
}
#pragma mark - GetMethod
- (NSString *)lx_address
{
    return [NSString stringWithFormat:@"%p",self];
}
- (NSString *)zoomScale
{
    NSString *scale = objc_getAssociatedObject(self, &zoomScaleStr);
    
    return [scale floatValue] > 0 ? scale :@"1";
}


/** Lx description   临时存放，快速实现白色粒子效果  **/
+ (CAEmitterLayer *)lx_quickEmitter{
      CAEmitterLayer *emitter = [[CAEmitterLayer alloc]init];
      emitter.preservesDepth = YES;

      CAEmitterCell *cell = [[CAEmitterCell alloc]init];
      cell.velocity = 150;
      cell.velocityRange = 30;
      cell.scale = 0.7;
    cell.scaleRange = 0.3;
//    cell.alphaSpeed = 0.5;
//    cell.alphaRange = -2;
    cell.alphaSpeed = -0.94;
     
     //粒子大小范围: 0.4 - 1 倍大
      cell.scaleRange = 0.3;
    cell.scale = 0.6;
      
      // 4.4.设置粒子方向
      //这个是设置经度，就是竖直方向 --具体看我们下面图片讲解
      //这个角度是逆时针的，所以我们的方向要么是 (2/3 π)， 要么是 (-π)
      cell.emissionLongitude = M_PI_2 * 1.4;
      cell.emissionRange = M_PI_2 * 0.75 ;
      
      // 4.5.设置粒子的存活时间
      cell.lifetime = 1.0 ;
      cell.lifetimeRange = 0.3;
//      // 4.6.设置粒子旋转
//      cell.spin = M_PI_2;
//      cell.spinRange = M_PI_2 ;
      // 4.6.设置粒子每秒弹出的个数
      cell.birthRate = 4;
//      // 4.7.设置粒子展示的图片 --这个必须要设置为CGImage
      cell.contents = (__bridge id _Nullable)([UIImage imageNamed:@"circle"].CGImage);
    
//    cell.color = [UIColor colorWithWhite:1 alpha:0.6].CGColor;
      // 5.将粒子设置到发射器中--这个是要放个数组进去
      emitter.emitterCells = @[cell];
    return emitter;
}
#pragma mark - CallFunction

- (UIImage *)lx_setImageWithImageName:(NSString *)imageName
{
    UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
    if (image) {
        [self lx_setImage:image];
    }
    return image;
}

- (UIImage *)lx_snapshotImage {
    @autoreleasepool {
        UIGraphicsBeginImageContextWithOptions(self.bounds.size, self.opaque, 0);
        CGContextRef context = UIGraphicsGetCurrentContext();
        [self renderInContext:context];
        UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        
        return image;
    }
}

- (void)lx_zoomScaleReset
{
    CGFloat scale = 1 / [self.zoomScale floatValue];
    [self lx_zoomScale:scale
scalePriScaleDirection:LxCalayerScalePriCenter];
}
/**
 *@description 获取图片,默认居中
 **/
- (UIImage *)lx_snapshotImageWithOffsetEdge:(UIEdgeInsets)edge
{
    @autoreleasepool {
        UIGraphicsBeginImageContextWithOptions(CGSizeMake(self.bounds.size.width + (edge.left + edge.right), self.bounds.size.height + (edge.top + edge.right)), self.opaque, 0);
        CGContextRef context = UIGraphicsGetCurrentContext();
        CGContextTranslateCTM(context, edge.left, edge.top);
        [self renderInContext:context];
        UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        return image;
    }
}

- (void)lx_pauseAnimation
{
    CFTimeInterval pausedTime = [self convertTime:CACurrentMediaTime() fromLayer:nil];
    self.speed = 0.0;
    self.timeOffset = pausedTime;
}

- (void)lx_resumeAnimation
{
    CFTimeInterval pausedTime = [self timeOffset];
    self.speed = 1.0;
    self.timeOffset = 0.0;
    self.beginTime = 0.0;
    CFTimeInterval timeSincePause = [self convertTime:CACurrentMediaTime() fromLayer:nil] - pausedTime;
    self.beginTime = timeSincePause;
}
/******************************************************ScaleAbout***************************************************************/
/**
 *@description 设置layer比例缩放
 **/
- (void)lx_zoomScale:(CGFloat)scale
scalePriScaleDirection:(LxCalayerScalePriDirection)priDirection
{
    CGFloat newWidth = CGRectGetWidth(self.frame) * (1 / [self.zoomScale floatValue]) * scale;
    CGFloat newHeight = CGRectGetHeight(self.frame) * (1 / [self.zoomScale floatValue]) * scale;
    self.zoomScale = [NSString stringWithFormat:@"%f",scale];
    switch (priDirection) {
        case LxCalayerScalePriCenter:
        {
           self.frame = CGRectMake(CGRectGetMidX(self.frame) - newWidth/2.f,
                                   CGRectGetMidY(self.frame) - newHeight/2.f,
                                   newWidth,
                                   newHeight);
        }
            break;
        case LxCalayerScalePriTop:
        {
            self.frame = CGRectMake(CGRectGetMidX(self.frame) - newWidth/2.f,
                                    CGRectGetMinY(self.frame),
                                    newWidth,
                                    newHeight);
        }
            break;
            case LxCalayerScalePriLeft:
        {
            self.frame = CGRectMake(CGRectGetMinX(self.frame),
                                    CGRectGetMinY(self.frame),
                                    newWidth,
                                    newHeight);
        }
            break;
            case LxCalayerScalePriRight:
        {
            self.frame = CGRectMake(CGRectGetMaxX(self.frame) - newWidth,
                                    CGRectGetMidY(self.frame) - newHeight/2.f,
                                    newWidth,
                                    newHeight);
        }
            break;
            case LxCalayerScalePriBottom:
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
@end
