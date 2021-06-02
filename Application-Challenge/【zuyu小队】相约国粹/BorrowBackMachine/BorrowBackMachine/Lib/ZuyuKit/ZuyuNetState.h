//
//  ZuyuNetState.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/21.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZuyuNetState : NSObject
/**
    0 为无网络
    1 为蜂窝
    2 为 wifi
    3 为 热点
 */
+(NSInteger )zuyuGetNetState;

@end
