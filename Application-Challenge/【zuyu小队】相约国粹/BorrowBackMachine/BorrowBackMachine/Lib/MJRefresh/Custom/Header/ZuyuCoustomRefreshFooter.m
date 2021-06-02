//
//  ZuyuCoustomRefreshFooter.m
//  CChat
//
//  Created by  cox on 2018/12/20.
//  Copyright © 2018  cox. All rights reserved.
//

#import "ZuyuCoustomRefreshFooter.h"

@implementation ZuyuCoustomRefreshFooter

- (void)prepare {
    [super prepare];
    // 初始化文字
    [self setTitle:@"---- 已经到底部了 ----" forState:MJRefreshStateIdle];
    [self setTitle:@"---- 已经到底部了 ----" forState:MJRefreshStatePulling];
    [self setTitle:@"---- 已经到底部了 ----"  forState:MJRefreshStateRefreshing];
    [self setTitle:@"---- 已经到底部了 ----" forState:MJRefreshStateNoMoreData];
    
}


@end
