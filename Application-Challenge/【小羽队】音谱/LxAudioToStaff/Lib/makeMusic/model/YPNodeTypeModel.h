//
//  YPNodeTypeModel.h
//  SmartPiano
//
//  Created by yuanpeijun on 2019/3/1.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPNodeTypeModel : NSObject

/**
 *@description 按键音符时值最小值
 **/
@property (assign, nonatomic) CGFloat intervalMin;

/**
 *@description 按键音符时值最大值
 **/
@property (assign, nonatomic) CGFloat intervalMax;

/**
 *@description 音符类型
 **/
@property (assign, nonatomic) MusicNodeType nodeType;

/**
 *@description 是否为休止符
 **/
@property (assign, nonatomic) BOOL isRest;

/**
 *@description 是否附点音符
 **/
@property (assign, nonatomic) BOOL isDot;


@end

NS_ASSUME_NONNULL_END
