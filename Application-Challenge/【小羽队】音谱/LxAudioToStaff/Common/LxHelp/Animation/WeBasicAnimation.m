//
//  WeBasicAnimation.m
//  face
//
//  Created by 李翔 on 16/4/27.
//  Copyright © 2016年 李翔. All rights reserved.
//

#import "WeBasicAnimation.h"

//rotation.x
//rotation.y
//rotation.z
//rotation 旋轉
//scale.x
//scale.y
//scale.z
//scale 缩放
//translation.x
//translation.y
//translation.z
//translation 平移

//CGPoint Key Paths : (example)position.x
//x
//y

//CGRect Key Paths : (example)bounds.size.width
//origin.x
//origin.y
//origin
//size.width
//size.height
//size

//opacity
//backgroundColor
//cornerRadius
//borderWidth
//contents

//Shadow Key Path:
//shadowColor
//shadowOffset
//shadowOpacity
//shadowRadius

@implementation WeBasicAnimation
+(CABasicAnimation *)getOpacityRepeatBasicAnimationWithFromValue:(float)from andToValue:(float)to andDuration:(NSTimeInterval)duration andAutoreverses:(BOOL)reverses andStateSave:(BOOL)stateSave
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"opacity"];
    animation.fromValue = @(from);
    
    animation.toValue = @(to);
    animation.duration = duration;
    animation.repeatCount = MAXFLOAT;
    animation.autoreverses = reverses;
    if (stateSave) {
        animation.fillMode = kCAFillModeForwards;
        animation.removedOnCompletion = !stateSave;
    }
    
    return animation;
}
+ (CABasicAnimation *)getOpacityBasicAnimationWithFromValue:(float)from andToValue:(float)to andDuration:(NSTimeInterval)duration andAutoreverses:(BOOL)reverses andStateSave:(BOOL)stateSave
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"opacity"];
    animation.fromValue = @(from);
    
    animation.toValue = @(to);
    animation.duration = duration;
    animation.repeatCount = 1;
    animation.autoreverses = reverses;
    if (stateSave) {
        animation.fillMode = kCAFillModeForwards;
        animation.removedOnCompletion = !stateSave;
    }
    
    return animation;
}

+ (CABasicAnimation *)getScaleBasicAnimationWithFromValue:(float)from andToValue:(float)to andDuration:(NSTimeInterval)duration andAutoreverses:(BOOL)reverses andStateSave:(BOOL)stateSave
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
    animation.fromValue = @(from);
    
    animation.toValue = @(to);
    animation.duration = duration;
    animation.repeatCount = 1;
    animation.autoreverses = reverses;
    if (stateSave) {
        animation.fillMode = kCAFillModeForwards;
        animation.removedOnCompletion = !stateSave;
    }
    
    return animation;
}

+ (CABasicAnimation *)getScaleAnimationKey:(NSString *)key Begin:(NSTimeInterval)begin Duration:(NSTimeInterval)duration StateSave:(BOOL)stateSave
{
    NSString *path = [NSString stringWithFormat:@"transform.scale.%@",key];
    CABasicAnimation *pathAnimation = [CABasicAnimation animationWithKeyPath:path];
    pathAnimation.beginTime = begin;
    pathAnimation.duration = duration;
    if (stateSave) {
        pathAnimation.fillMode = kCAFillModeForwards;
        pathAnimation.removedOnCompletion = !stateSave;
    }else{
        pathAnimation.fillMode = kCAFillModeRemoved;
    }
    //    pathAnimation.rotationMode = kCAAnimationRotateAuto;
    return pathAnimation;
    
}

+ (CABasicAnimation *)getRotationBacicAnimationWithFromValue:(CGFloat)from
                                                     ToValue:(CGFloat)to
                                                 repeatCount:(NSInteger)repeat
                                                    Duration:(NSTimeInterval)duration
                                                AutoReverses:(BOOL)reverses
                                                   StateSave:(BOOL)stateSave
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"transform.rotation"];
    animation.fromValue = @(from);
    animation.toValue = @(to);
    animation.duration = duration;
    animation.repeatCount = repeat;
    animation.autoreverses = reverses;
    if (stateSave) {
        animation.fillMode = kCAFillModeForwards;
        animation.removedOnCompletion = !stateSave;
    }else
    {
        animation.fillMode = kCAFillModeRemoved;
    }
    return animation;
    
}

+ (CAKeyframeAnimation *)getPositionAnimationBegin:(NSTimeInterval)begin Duration:(NSTimeInterval)duration StateSave:(BOOL)stateSave
{
    CAKeyframeAnimation *pathAnimation = [CAKeyframeAnimation animationWithKeyPath:@"position"];
    pathAnimation.beginTime = begin;
    pathAnimation.duration = duration;
    pathAnimation.calculationMode = kCAAnimationCubicPaced;
    if (stateSave) {
        pathAnimation.fillMode = kCAFillModeForwards;
        pathAnimation.removedOnCompletion = !stateSave;
    }else{
        pathAnimation.fillMode = kCAFillModeRemoved;
    }
//    pathAnimation.rotationMode = kCAAnimationRotateAuto;
    return pathAnimation;
}

+ (CABasicAnimation *)getTranslateAnimationKey:(NSString *)key
                                        WithTo:(CGFloat)to
                                   RepeatCount:(NSInteger)repeatCount
                                      Duration:(NSTimeInterval)duration
                                  AutoReverses:(BOOL)reverses
                                     StateSave:(BOOL)stateSave
{
    NSString *str = [NSString stringWithFormat:@"transform.translation.%@",key];
    CABasicAnimation *animtion = [CABasicAnimation animationWithKeyPath:str];
    animtion.toValue = @(to);
    animtion.repeatCount = repeatCount;
    animtion.duration = duration;
    animtion.autoreverses = reverses;
    if (stateSave) {
        animtion.fillMode = kCAFillModeForwards;
        animtion.removedOnCompletion = !stateSave;
    }else
    {
        animtion.fillMode = kCAFillModeRemoved;
        //        animation.removedOnCompletion = stateSave;
    }
    return animtion;

}

+ (CABasicAnimation *)getTranslatePositionAnimationTo:(CGPoint)toPoint
                                          RepeatCount:(NSInteger)repeatCount
                                             Duration:(NSTimeInterval)duration
                                         AutoReverses:(BOOL)reverses
                                            StateSave:(BOOL)stateSave;
{

    NSString *str = @"transform.translation";
    CABasicAnimation *animtion = [CABasicAnimation animationWithKeyPath:str];
    animtion.toValue = [NSValue valueWithCGPoint:toPoint];
    animtion.repeatCount = repeatCount;
    animtion.duration = duration;
    animtion.autoreverses = reverses;
    if (stateSave) {
        animtion.fillMode = kCAFillModeForwards;
        animtion.removedOnCompletion = !stateSave;
    }else
    {
        animtion.fillMode = kCAFillModeRemoved;
    }
    return animtion;
}

+ (CAAnimationGroup *)getGroupWithDuration:(NSTimeInterval)duration
                                  delegate:(id)delegate
                                statusSave:(BOOL)saveStatus
                               autoReverse:(BOOL)reverser
{
    CAAnimationGroup *group = [CAAnimationGroup animation];
      group.removedOnCompletion = !saveStatus;
    group.duration = duration;
    if (delegate) {
        group.delegate = delegate;
    }
    if (saveStatus) {
        group.fillMode = kCAFillModeForwards;
      
    }else
    {
        group.fillMode = kCAFillModeRemoved;
    }
    return group;
}

+ (CABasicAnimation *)getRotationAnimationWithDuration:(NSTimeInterval)duration
                                             fromValue:(double)from
                                               toValue:(double)to
                                            statusSave:(BOOL)saveStatus
                                           autoReverse:(BOOL)reverser
{
    CABasicAnimation *rotation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    rotation.fromValue = [NSNumber numberWithDouble:from];
    rotation.toValue = [NSNumber numberWithDouble:to];
    rotation.duration = duration;
    rotation.autoreverses = reverser;
    if (saveStatus) {
        rotation.fillMode = kCAFillModeForwards;
        rotation.removedOnCompletion = !saveStatus;
    }else
    {
        rotation.fillMode = kCAFillModeRemoved;
    }
    return rotation;
}

+ (CAKeyframeAnimation *)getGifAnimationWithDuration:(NSTimeInterval)duration
                                       statusSave:(BOOL)saveStatus
                                      autoReverse:(BOOL)reverser
{
    CAKeyframeAnimation *imageGif = [CAKeyframeAnimation animation];
    [imageGif setKeyPath:@"contents"];
    imageGif.duration = duration;
    imageGif.calculationMode = kCAAnimationDiscrete;
    if (saveStatus) {
        imageGif.fillMode = kCAFillModeForwards;
        imageGif.removedOnCompletion = !saveStatus;
    }else
    {
        imageGif.fillMode = kCAFillModeRemoved;
    }

    return imageGif;
}

+ (CAKeyframeAnimation *)getBackGroundColorWithDuration:(NSTimeInterval)duration
                                             statusSave:(BOOL)saveStatus
                                            autoReverse:(BOOL)reverser
{
    CAKeyframeAnimation *imageGif = [CAKeyframeAnimation animation];
    [imageGif setKeyPath:@"backgroundColor"];
    imageGif.duration = duration;
    imageGif.calculationMode = kCAAnimationDiscrete;
    if (saveStatus) {
        imageGif.fillMode = kCAFillModeForwards;
        imageGif.removedOnCompletion = !saveStatus;
    }else
    {
        imageGif.fillMode = kCAFillModeRemoved;
    }
    
    return imageGif;
}


+ (CAKeyframeAnimation *)getFillColorWithDuration:(NSTimeInterval)duration
                                       statusSave:(BOOL)saveStatus
                                      autoReverse:(BOOL)reverser
{
    CAKeyframeAnimation *imageGif = [CAKeyframeAnimation animation];
    [imageGif setKeyPath:@"fillColor"];
    imageGif.duration = duration;
    imageGif.calculationMode = kCAAnimationDiscrete;
    if (saveStatus) {
        imageGif.fillMode = kCAFillModeForwards;
        imageGif.removedOnCompletion = !saveStatus;
    }else
    {
        imageGif.fillMode = kCAFillModeRemoved;
    }
    
    return imageGif;
}


/**
 *暂停coreAnimation动画
 */
+ (void)pauseLayer:(CALayer *)layer
{
    CFTimeInterval pausedTime = [layer convertTime:CACurrentMediaTime() fromLayer:nil];
    layer.speed = 0.0;
    layer.timeOffset = pausedTime;
}
/**
 *重新开始coreAnimation动画
 */
+ (void)resumeLayer:(CALayer *)layer;
{
    CFTimeInterval pausedTime = [layer timeOffset];
    layer.speed = 1.0;
    layer.timeOffset = 0.0;
    layer.beginTime = 0.0;
    CFTimeInterval timeSincePause = [layer convertTime:CACurrentMediaTime() fromLayer:nil] - pausedTime;
    layer.beginTime = timeSincePause;
}

/*
 Yoyo工具类
 */

// index 是从0开始 尾部+1
+(NSMutableArray *) setGifArr :(int) index strpath:(NSString*) strpath strpath:(NSString*) strpath1
{
    
    NSMutableArray *imageArray = [[NSMutableArray alloc] init];
    for (int i = 0; i < index; i++) {
        NSString *imageName;
        if(i<10)
            imageName = [NSString stringWithFormat:strpath, i];
        else
            imageName = [NSString stringWithFormat:strpath1,i];
        
        NSString * strpath = [[NSBundle mainBundle] pathForResource:imageName ofType:@"png"];
        UIImage *image = [UIImage imageNamed:strpath];
        [imageArray addObject:(id)[image CGImage]];
    }
    return imageArray;
}

@end
