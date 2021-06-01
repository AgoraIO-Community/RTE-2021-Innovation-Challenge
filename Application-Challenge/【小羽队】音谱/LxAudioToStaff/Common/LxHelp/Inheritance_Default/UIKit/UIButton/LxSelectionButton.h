//
//  LxSelectionButton.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/29.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LxSelectionButton : UIButton
/**
 *@description 设置按钮默认图片名称（选择状态图片后缀添加_selected,暂时只使用@2x图)
 **/
@property (copy, nonatomic) NSString *defaultImageName;

/**
 *@description 获取基础按钮
 *@param imageName 默认图片名称（暂只使用@2x图)
 *@return btn
 **/
+ (LxSelectionButton *)lx_defaultBtnWithImageName:(NSString *)imageName;

@end
