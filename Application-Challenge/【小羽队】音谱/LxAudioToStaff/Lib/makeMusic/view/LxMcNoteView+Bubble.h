//
//  LxMcNoteView+Bubble.h
//  SmartPiano
//
//  Created by DavinLee on 2018/2/5.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcNoteView.h"
#import "UIView+Default.h"

@interface LxMcNoteView (Bubble)
/**
 *@description 获取默认泡泡音符
 *@param noteType 音符时值类型
 *@param isRest 是否休止符
 *@param isDot 是否附点音符
 **/
+ (LxMcNoteView *)lx_bubbleNoteViewWithNoteType:(MusicNodeType)noteType
                                         isRest:(BOOL)isRest
                                          isDot:(BOOL)isDot;

/**
 *@description 获取默认黑白音符
 *@param noteType 音符时值类型
 *@param isRest 是否休止符
 *@param isDot 是否附点音符
 **/
+ (LxMcNoteView *)lx_defaultNoteViewWithNoteType:(MusicNodeType)noteType
                                         isRest:(BOOL)isRest
                                          isDot:(BOOL)isDot;

/**
*@description 作曲游戏音名使用
*@param noteType 音符时值类型
*@param isRest 是否休止符
*@param isDot 是否附点音符
**/
+ (LxMcNoteView *)lx_defaultABCNoteViewWithNoteType:(MusicNodeType)noteType
                                             isRest:(BOOL)isRest
                                              isDot:(BOOL)isDot
                                          isTouchUI:(BOOL)isTouchUI
                                              isABC:(BOOL)isABC
                                            abcName:(NSString *)name;

/**
 *@description 切换为泡泡音符
 **/
- (void)createBubbleUI;
/**
 *@description 设置音符是否选中状态（作曲游戏)
 *@param selected 是否选中
 **/
- (void)lx_resetSelecteState:(BOOL)selected;
/**
 *@description 重新布置音符大小比例
 *@param
 **/
- (void)lx_zoomNoteViewScale:(CGFloat)scale scalePriScaleDirection:(LxViewScalePriDirection)direction;
@end
