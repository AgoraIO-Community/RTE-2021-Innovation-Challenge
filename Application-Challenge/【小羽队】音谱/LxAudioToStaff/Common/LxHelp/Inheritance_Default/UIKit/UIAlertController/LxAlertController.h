//
//  LxAlertController.h
//  svgtest2
//
//  Created by 李翔 on 2017/6/20.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LxAlertController : UIAlertController
/**
 *@description 使用简单的Alert提示
 *@param title alert提示标题
 *@param message alert提示具体信息
 *@param actionTitles alert的点击事件标题
 *@param actionStyles 点击事件按钮类型，无则默认default
 *@param block 点击事件生效后的回调，对应actionTitles的index
 **/
+ (LxAlertController *)lx_alertShowWithTitle:(NSString *)title
                                     message:(NSString *)message
                                actionTitles:(NSArray <NSString *>*)actionTitles
                                actionStyles:(NSArray *)actionStyles
                             clickIndexBlock:(void(^)(NSInteger clickIndex))block;


/**
 *@description 使用带有文本输入框的Alert提示
 *@param title alert提示标题
 *@param message alert提示具体信息
 *@param textFiledHolder 文本框提示文字
 *@param actionTitles alert的点击事件标题
 *@param actionStyles 点击事件按钮类型，无则默认default
 *@param block 点击事件生效后的回调，对应actionTitles的index
 **/
+ (LxAlertController *)lx_alertShowWithTitle:(NSString *)title
                                     message:(NSString *)message
                           textfiledHolderStr:(NSString *)textFiledHolder
                                actionTitles:(NSArray <NSString *>*)actionTitles
                                actionStyles:(NSArray *)actionStyles
                             clickIndexBlock:(void(^)(NSInteger clickIndex,NSString *text))block;


@end
