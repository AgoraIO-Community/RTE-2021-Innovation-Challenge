//
//  NSMutableArray+Helper.h
//  SmartPiano
//
//  Created by 李翔 on 2017/7/3.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSMutableArray (Helper)
/**
 *@description 加入元素（避免空值或Null)
 **/
- (void)lx_addSafityObject:(id)object;

@end
