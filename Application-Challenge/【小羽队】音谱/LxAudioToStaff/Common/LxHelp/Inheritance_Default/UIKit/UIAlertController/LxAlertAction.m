//
//  LxAlertAction.m
//  svgtest2
//
//  Created by 李翔 on 2017/6/20.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import "LxAlertAction.h"

@implementation LxAlertAction
#pragma mark - CallFunction
+ (LxAlertAction *)lx_actionWithTitle:(NSString *)title
                                style:(UIAlertActionStyle)style
                          actionIndex:(NSInteger)actionIndex
                                block:(void(^)(LxAlertAction *clickAction))block
{
    LxAlertAction *alertAction = [LxAlertAction actionWithTitle:title style:style handler:^(UIAlertAction * _Nonnull action) {
        if (block) {
            block((LxAlertAction *)action);
        }
    }];
    alertAction.click_index = actionIndex;
    return alertAction;
}

@end
