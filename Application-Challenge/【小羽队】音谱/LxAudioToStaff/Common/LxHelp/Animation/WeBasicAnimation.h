//
//  WeBasicAnimation.h
//  face
//
//  Created by 李翔 on 16/4/27.
//  Copyright © 2016年 李翔. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface WeBasicAnimation : NSObject
/**
 *透明度变化
 */
+ (CABasicAnimation *)getOpacityBasicAnimationWithFromValue:(float)from
                                                 andToValue:(float)to
                                                andDuration:(NSTimeInterval)duration
                                            andAutoreverses:(BOOL)reverses
                                               andStateSave:(BOOL)stateSave;
/**
 * 大小变化
 */
+ (CABasicAnimation *)getScaleBasicAnimationWithFromValue:(float)from
                                               andToValue:(float)to
                                              andDuration:(NSTimeInterval)duration
                                          andAutoreverses:(BOOL)reverses
                                             andStateSave:(BOOL)stateSave;
/**
 * 目标位置变化(path)
 */
+ (CAKeyframeAnimation *)getPositionAnimationBegin:(NSTimeInterval)begin
                                          Duration:(NSTimeInterval)duration
                                         StateSave:(BOOL)stateSave;
/**
 * 缩放：x/y/z
 */
+ (CABasicAnimation *)getScaleAnimationKey:(NSString *)key
                                        Begin:(NSTimeInterval)begin
                                     Duration:(NSTimeInterval)duration
                                    StateSave:(BOOL)stateSave;

/**
 * 相对位置变化(单一变化)
 */
+ (CABasicAnimation *)getTranslateAnimationKey:(NSString *)key
                                        WithTo:(CGFloat)to
                                   RepeatCount:(NSInteger)repeatCount
                                      Duration:(NSTimeInterval)duration
                                  AutoReverses:(BOOL)reverses
                                     StateSave:(BOOL)stateSave;

/**
 * 相对位置变化(相对位置变化)
 */
+ (CABasicAnimation *)getTranslatePositionAnimationTo:(CGPoint)toPoint
                                          RepeatCount:(NSInteger)repeatCount
                                             Duration:(NSTimeInterval)duration
                                         AutoReverses:(BOOL)reverses
                                            StateSave:(BOOL)stateSave;

/**
 * 旋转变化
 */
+ (CABasicAnimation *)getRotationBacicAnimationWithFromValue:(CGFloat)from
                                                     ToValue:(CGFloat)to
                                                 repeatCount:(NSInteger)repeat
                                                    Duration:(NSTimeInterval)duration
                                                AutoReverses:(BOOL)reverses
                                                   StateSave:(BOOL)stateSave;
/**
 * 获取组动画
 */
+ (CAAnimationGroup *)getGroupWithDuration:(NSTimeInterval)duration
                                  delegate:(id)delegate
                                statusSave:(BOOL)saveStatus
                               autoReverse:(BOOL)reverser;


+ (CABasicAnimation *)getRotationAnimationWithDuration:(NSTimeInterval)duration
                                             fromValue:(double)from
                                               toValue:(double)to
                                            statusSave:(BOOL)saveStatus
                                           autoReverse:(BOOL)reverser;
/**
 * 序列帧动画
 */
+ (CAKeyframeAnimation *)getGifAnimationWithDuration:(NSTimeInterval)duration
                                          statusSave:(BOOL)saveStatus
                                         autoReverse:(BOOL)reverser;
/**
 *获取颜色变化动画
 */
+ (CAKeyframeAnimation *)getBackGroundColorWithDuration:(NSTimeInterval)duration
                                             statusSave:(BOOL)saveStatus
                                            autoReverse:(BOOL)reverser;

/**
 *获取填充色变化动画
 **/
+ (CAKeyframeAnimation *)getFillColorWithDuration:(NSTimeInterval)duration
                                       statusSave:(BOOL)saveStatus
                                      autoReverse:(BOOL)reverser;


/**
 *暂停coreAnimation动画
 */
+ (void)pauseLayer:(CALayer *)layer;
/**
 *重新开始coreAnimation动画
 */
+ (void)resumeLayer:(CALayer *)layer;

/*
 Yoyo工具类
 */

/*
 序列帧动画
 */

+(NSMutableArray *) setGifArr: (int)index strpath:(NSString*) strpath strpath:(NSString*) strpath1;
/**
 *透明度变化重复
 */
+ (CABasicAnimation *)getOpacityRepeatBasicAnimationWithFromValue:(float)from
                                                 andToValue:(float)to
                                                andDuration:(NSTimeInterval)duration
                                            andAutoreverses:(BOOL)reverses
                                               andStateSave:(BOOL)stateSave;
@end
