//
//  LxAlertAction.h
//  svgtest2
//
//  Created by 李翔 on 2017/6/20.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LxAlertAction : UIAlertAction
/**
 *@description 记录点击事件传入序列index，作为点击事件生效后的标识
 **/
@property (assign, nonatomic) NSInteger click_index;

/**
 *@description 获取实例action
 *@param title 响应标题
 *@param style 响应类型
 *@param actionIndex 响应序列
 *@param block 响应回调
 **/
+ (LxAlertAction *)lx_actionWithTitle:(NSString *)title
                     style:(UIAlertActionStyle)style
               actionIndex:(NSInteger)actionIndex
                     block:(void(^)(LxAlertAction *clickAction))block;

@end
