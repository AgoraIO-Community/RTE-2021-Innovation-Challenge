//
//  UIView+flashAnimal.m
//  SmartPiano
//
//  Created by JM(jieson) on 16/4/19.
//  Copyright © 2016年 XiYun. All rights reserved.
//

#import "UIView+flashAnimal.h"

@implementation UIView (flashAnimal)

- (void)flashInDuarition:(NSTimeInterval) aTime {
    [self removeFlashError];
   
    [self.layer addAnimation:[self opacityForever_Animation:0.5]forKey:@"flash"];
    if (aTime != 0) {
        [self performSelector:@selector(removeFlashError) withObject:nil afterDelay:aTime];
    }
}

- (void)flashInDuarition:(NSTimeInterval)aTime withOnceDuarion:(NSTimeInterval) aOnceDuarion {
    [self.layer addAnimation:[self opacityForever_Animation:aOnceDuarion]forKey:@"flash"];
    if (aTime != 0) {
        [self performSelector:@selector(removeFlashError) withObject:nil afterDelay:aTime];
    }
}

- (void)removeFlashError {
    [self.layer removeAllAnimations];
}

- (CABasicAnimation *)opacityForever_Animation:(float)time
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"opacity"];//必须写opacity才行。
    animation.fromValue = [NSNumber numberWithFloat:1.0f];
    animation.toValue = [NSNumber numberWithFloat:0.0f];//这是透明度。
    animation.autoreverses = YES;
    animation.duration = time;
    animation.repeatCount = MAXFLOAT;
    animation.removedOnCompletion = NO;
    animation.fillMode = kCAFillModeForwards;
    animation.timingFunction=[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseIn];///没有的话是均匀的动画。
    return animation;
}


- (void)yingHuoChongflashInDuarition:(NSTimeInterval)aTime withOnceDuarion:(NSTimeInterval) aOnceDuarion {
    [self.layer addAnimation:[self opacityForever_AnimationForYingHuoChong:aOnceDuarion]forKey:@"flash"];
    if (aTime != 0) {
        [self performSelector:@selector(removeFlashError) withObject:nil afterDelay:aTime];
    }
}

- (CABasicAnimation *)opacityForever_AnimationForYingHuoChong:(float)time
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"opacity"];//必须写opacity才行。
    animation.fromValue = [NSNumber numberWithFloat:0.8f];
    animation.toValue = [NSNumber numberWithFloat:0.0f];//这是透明度。
    animation.autoreverses = YES;
    animation.duration = time;
    animation.repeatCount = MAXFLOAT;
    animation.removedOnCompletion = NO;
    animation.fillMode = kCAFillModeForwards;
    animation.timingFunction=[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseIn];///没有的话是均匀的动画。
    return animation;
}
@end
