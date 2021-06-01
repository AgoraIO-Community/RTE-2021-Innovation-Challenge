//
//  UIViewController+Default.h
//  LxHelp
//
//  Created by DavinLee on 2017/11/3.
//  Copyright © 2017年 DavinLee. All rights reserved.
//

#import <UIKit/UIKit.h>

const

@interface UIViewController (Default)
/**
 *@description present时切换动画设置
 *@param type 动画类型
 *@param subtype 子类动画类型
 *@param duration 动画时间
 *@param view 操作夫类view
 *动画类型包括:
 kCATransitionFromLeft;
  kCATransitionFromBottom;
 kCATransitionFromRight;
 kCATransitionFromTop;
 kCATransitionReveal
 kCATransitionMoveIn
 @"cube"
 @"suckEffect"
 @"oglFlip"
 @"rippleEffect"
 @"pageCurl"
 @"cameraIrisHollowOpen"
 @"cameraIrisHollowClose"
 **/
- (void)lx_transitionWithType:(NSString *)type
                  WithSubtype:(NSString *)subtype
                     Duration:(NSTimeInterval)duration
                      ForView:(UIView *)view;
@end
