//
//  LxCanInteraSubImageView.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxCanInteraSubImageView.h"

@implementation LxCanInteraSubImageView

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
{
    for (UIView *tempView in self.subviews) {
        if ([tempView hitTest:CGPointMake(point.x - CGRectGetMinX(tempView.frame), point.y - CGRectGetMinY(tempView.frame)) withEvent:event] && tempView.userInteractionEnabled == YES) {
            return [tempView hitTest:CGPointMake(point.x - CGRectGetMinX(tempView.frame), point.y - CGRectGetMinY(tempView.frame)) withEvent:event];
        }
    }
    return nil;
}
@end
