//
//  NSMutableArray+Helper.m
//  SmartPiano
//
//  Created by 李翔 on 2017/7/3.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "NSMutableArray+Helper.h"

@implementation NSMutableArray (Helper)

- (void)lx_addSafityObject:(id)object
{
    
    if (object != nil && ![object isKindOfClass:[NSNull class]]) {
        [self addObject:object];
    }
}


@end
