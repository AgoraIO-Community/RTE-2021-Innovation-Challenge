//
//  LxNotableTextLayer.m
//  SmartPiano
//
//  Created by DavinLee on 2018/5/29.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "LxNotableTextLayer.h"
#import "CATextLayer+Default.h"
#import "WeBasicAnimation.h"
#import "NSString+Helper.h"
#import <yykit/CALayer+YYAdd.h>
@interface LxNotableTextLayer ()<CAAnimationDelegate>
/** 文本 **/
@property (strong, nonatomic) NSString *storeString;
/** 是否连续滚动 **/
@property (assign, nonatomic) BOOL succession;
/** 滚动总时间 **/
@property (assign, nonatomic) CGFloat scrollDuration;
/** 滚动方向 **/
@property (assign, nonatomic) LxNTlayerScroDirectin scrollDirection;
/** 字体暂存 **/
@property (strong, nonatomic) UIFont *storeFont;
/** 计时器 **/
@property (strong, nonatomic) dispatch_source_t timer;
@end
@implementation LxNotableTextLayer
#pragma mark - ********************  CallFunction   ********************
/**
 *@description 设置字体、颜色
 **/
- (void)lx_setFont:(UIFont *)font textColor:(UIColor *)textColor
{
    self.storeFont = font;
    [self lx_setupFont:font textColor:textColor];

}

/**
 *@description 开始滚动
 *@param scroDirection 滚动方向
 *@param duration 滚动时间
 *@param succession 是否直接空几个单位后直接出现滚动
 **/
- (void)lx_startScrollWithDirection:(LxNTlayerScroDirectin)scroDirection
                           duration:(CGFloat)duration
                         succession:(BOOL)succession
{
    self.storeString = [self.string copy];
    [self lx_stopScroll];
    self.masksToBounds = YES;
    self.string = nil;
    self.scrollDuration = duration;
    
    CATextLayer *tempLayer = [self copyTextLayer];
    [self addSublayer:tempLayer];
    tempLayer.position = CGPointMake(CGRectGetWidth(self.frame)/2.f + (CGRectGetWidth(tempLayer.frame) - CGRectGetWidth(self.frame))/2.f, CGRectGetHeight(self.frame)/2.f);
    
    CGFloat maxTextLayerWidth = [self newTextLayerWidth];
    CABasicAnimation *moveAnimation = [WeBasicAnimation getTranslateAnimationKey:@"x"
                                                                          WithTo:- maxTextLayerWidth
                                                                     RepeatCount:NO
                                                                        Duration:self.scrollDuration / 2.f
                                                                    AutoReverses:NO
                                                                       StateSave:YES];
    [tempLayer addAnimation:moveAnimation forKey:@"move"];
    [moveAnimation setValue:tempLayer forKey:@"layer"];
    moveAnimation.delegate = self;
    [self performSelector:@selector(startNotableScroll)
               withObject:nil
               afterDelay:self.scrollDuration / 2.f];
}

/**
 *@description 停止滚动
 **/
- (void)lx_stopScroll
{
    [self removeAllAnimations];
    [self removeAllSublayers];
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    if (self.timer) {
        dispatch_cancel(self.timer);
    }
    if (self.storeString) {
        self.string = self.storeString;
    }
}
#pragma mark - ********************  AnimationDelegate  ********************
- (void)animationDidStop:(CAAnimation *)anim finished:(BOOL)flag
{
    CALayer *layer = (CALayer *)[anim valueForKey:@"layer"];
    if (layer) {
        [layer removeFromSuperlayer];
    }
}

#pragma mark - ********************  Function  ********************
- (void)startNotableScroll
{
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    self.timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0));
    dispatch_source_set_timer(self.timer, DISPATCH_TIME_NOW, (int64_t)self.scrollDuration * 0.85 *NSEC_PER_SEC, 0);
    dispatch_source_set_event_handler(self.timer, ^{
        dispatch_async(dispatch_get_main_queue(), ^{
                  [self newTextLayerScroll];
        });
    });
    dispatch_resume(self.timer);
}

- (void)newTextLayerScroll
{
    CATextLayer *tempLayer = [self copyTextLayer];
    [self addSublayer:tempLayer];
    tempLayer.contentsScale = [UIScreen mainScreen].scale;
    tempLayer.position = CGPointMake([self newTextLayerWidth] * 1.5, CGRectGetHeight(self.frame)/2.f);
    
    CGFloat maxTextLayerWidth = [self newTextLayerWidth];
    CABasicAnimation *moveAnimation = [WeBasicAnimation getTranslateAnimationKey:@"x"
                                                                          WithTo:- maxTextLayerWidth * 2
                                                                     RepeatCount:NO
                                                                        Duration:self.scrollDuration
                                                                    AutoReverses:NO
                                                                       StateSave:YES];
    
    [moveAnimation setValue:tempLayer forKey:@"layer"];
    moveAnimation.delegate = self;
    [tempLayer addAnimation:moveAnimation forKey:@"move"];
}

- (CATextLayer *)copyTextLayer
{
    CATextLayer *tempLayer = [CATextLayer layer];
    tempLayer.frame = CGRectMake(0, 0, [self newTextLayerWidth], CGRectGetHeight(self.frame));
    tempLayer.font = self.font;
    tempLayer.fontSize = self.fontSize;
    tempLayer.alignmentMode = self.alignmentMode;
    tempLayer.foregroundColor = self.foregroundColor;
    tempLayer.string = self.storeString;
    tempLayer.contentsScale = [UIScreen mainScreen].scale;
    return tempLayer;
}

- (CGFloat)newTextLayerWidth
{
     CGSize strSize = [self.storeString lx_textSizeWithFont:self.storeFont MaxSize:CGSizeMake(MAXFLOAT, CGRectGetHeight(self.frame))];
    return MAX(CGRectGetWidth(self.frame), strSize.width);
}

- (CGPoint)newLayerPosition
{
    switch (self.scrollDirection) {
        case LxNTlayerScroHorLeft:
        {
            CGSize strSize = [self.storeString lx_textSizeWithFont:self.storeFont MaxSize:CGSizeMake(MAXFLOAT, CGRectGetHeight(self.frame))];
            if (strSize.width >= CGRectGetWidth(self.frame) - 10 /** 强制添加10的偏移 **/) {
                return CGPointMake(strSize.width + CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame)/2.f);
            }else
            {
                return CGPointMake([self newTextLayerWidth], CGRectGetHeight(self.frame)/2.f);
            }
        }
            break;
        case LxNTlayerScroVerDown:
        default:
            break;
    }
    return CGPointMake(CGRectGetWidth(self.frame), CGRectGetHeight(self.frame)/2.f);
}

- (void)dealloc
{
    debugLog(@"%s",__func__);
    if (self.timer) {
        dispatch_cancel(self.timer);
       
    }
     [self removeAllSublayers];
}

@end
