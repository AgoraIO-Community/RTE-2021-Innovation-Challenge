//
//  QHViewUtil.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/21.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHViewUtil.h"

@implementation QHViewUtil

+ (void)fullScreen:(UIView *)subView {
    [self fullScreen:subView edgeInsets:UIEdgeInsetsZero];
}

+ (void)fullScreen:(UIView *)subView edgeInsets:(UIEdgeInsets)edgeInsets {
    if (subView == nil || subView.superview == nil) {
        return;
    }
    subView.translatesAutoresizingMaskIntoConstraints = NO;
    NSDictionary *viewsDict = NSDictionaryOfVariableBindings(subView);
    NSString *lcString = [NSString stringWithFormat:@"|-%f-[subView]-%f-|", edgeInsets.left, edgeInsets.right];
    [subView.superview addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:lcString options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    NSString *lcString2 = [NSString stringWithFormat:@"V:|-%f-[subView]-%f-|", edgeInsets.top, edgeInsets.bottom];
    [subView.superview addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:lcString2 options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
}

+ (void)fullScreen2:(UIView *)subView {
    if (subView == nil || subView.superview == nil) {
        return;
    }
    UIView *superV = subView.superview;
    subView.translatesAutoresizingMaskIntoConstraints = NO;
    [superV addConstraint:[NSLayoutConstraint constraintWithItem:subView attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:superV attribute:NSLayoutAttributeTop multiplier:1. constant:0]];
    [superV addConstraint:[NSLayoutConstraint constraintWithItem:subView attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:superV attribute:NSLayoutAttributeBottom multiplier:1. constant:0]];
    [superV addConstraint:[NSLayoutConstraint constraintWithItem:subView attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual toItem:superV attribute:NSLayoutAttributeLeading multiplier:1. constant:0]];
    [superV addConstraint:[NSLayoutConstraint constraintWithItem:subView attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual toItem:superV attribute:NSLayoutAttributeTrailing multiplier:1. constant:0]];
}

@end
