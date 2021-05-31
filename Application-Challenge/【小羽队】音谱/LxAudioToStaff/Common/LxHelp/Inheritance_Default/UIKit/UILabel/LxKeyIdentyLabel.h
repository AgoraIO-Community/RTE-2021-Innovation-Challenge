//
//  LxKeyIdentyLabel.h
//  SmartPiano
//
//  Created by DavinLee on 2018/5/28.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LxKeyIdentyLabel : UILabel
/** 键位tag标识 **/
@property(copy, nonatomic)IBInspectable NSString *keyTag;
/**
 *@description 设置琴键tag显示
 *@param keyTag 琴键tag
 **/
- (void)lx_setupWithKeyTag:(NSInteger)keyTag;
/**
 *@description 获取琴键的音阶
 *@param keyTag 琴键tag
 *@return NSInteger 音阶
 **/
+ (NSInteger)lx_stepWithKeyTag:(NSInteger)keyTag;
/**
 *@description 获取琴键音名
 *@param keytag 琴键tag
 *@praram step 音阶
 **/
+ (NSString *)lx_octaveWithKeyTag:(NSInteger)keytag step:(NSInteger)step;
/**
 *@description 获取琴键keyTag
 **/
+ (NSInteger)lx_keyTagWithOctave:(NSString *)octave step:(NSInteger)step;
@end
