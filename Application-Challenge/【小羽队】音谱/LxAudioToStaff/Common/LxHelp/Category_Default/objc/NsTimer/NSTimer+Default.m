//
//  NSTimer+Default.m
//  SmartPiano
//
//  Created by 李翔 on 2017/6/17.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "NSTimer+Default.h"

@implementation NSTimer (Default)

- (void)lx_pause
{
    [self setFireDate:[NSDate distantFuture]];
}

- (void)lx_resume
{
    [self setFireDate:[NSDate date]];
}

@end
