//
//  LxStaffNoteModel.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/2.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LxMusicDefine.h"
@class LxStaffNoteNode;
@interface LxStaffNoteModel : NSObject
/**
 *@description 音符类型
 **/
@property (assign, nonatomic) MusicNodeType nodeType;
/**
 *@description layer类型（音符、休止、附点)
 **/
@property (assign, nonatomic) NoteLayerType layerType;
/**
 *@description 音符时值
 **/
@property (assign, nonatomic) NSTimeInterval duration;
/**
 *@description Octave
 **/
@property (assign, nonatomic) NSInteger octave;
/**
 *@description Step
 **/
@property (copy, nonatomic) NSString *step;
/**
 *@description 左右手 0->左手 1->双手  2->右手
 **/
@property (assign, nonatomic) LxStaffHandleType handType;
/**
 *@description midiTag(升降调后的tag）
 **/
@property (assign, nonatomic) NSInteger midiTag;
/**
 *@description 实际miditag
 **/
@property (assign, nonatomic) NSInteger realMidiTag;
/**
 *@description 升降调
 **/
@property (assign, nonatomic) NSInteger alter;
/**
 *@description 在第几小节
 **/
@property (assign, nonatomic) NSInteger measureIndex;
/**
 *@description 在小节中位置
 **/
@property (assign, nonatomic) NSInteger indexOfMeasure;
/**
 *@description 音符node
 **/
@property (strong, nonatomic) LxStaffNoteNode *node;
/**
 *@description 是否已播放过
 **/
@property (assign, nonatomic) BOOL isPlayed;
/**
 *@description 是否点击正确
 **/
@property (assign, nonatomic) BOOL isTaped;
/**
 *@description 按键力度
 **/
@property (assign, nonatomic) NSInteger velocity;
/**
 *@description 在钢琴键输入
 **/
@property (assign, nonatomic) NSInteger isKeyboardInput;
/**
 *@description 获取音符数组
 **/
+ (NSArray <LxStaffNoteModel *>*)lx_getModelsWithInfoArray:(NSArray *)infoArray;
@end
