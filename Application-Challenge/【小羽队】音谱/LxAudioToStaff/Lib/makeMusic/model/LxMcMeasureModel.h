//
//  LxMcMeasureModel.h
//  SmartPiano
//
//  Created by DavinLee on 2018/2/1.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LxMcNoteView.h"
#import "LxMcStaffHeader.h"
@interface LxMcMeasureModel : NSObject
/******************************************************位置相关***************************************************************/
/**
 *@description 小节index（从0开始）
 **/
@property (assign, nonatomic) NSInteger measureIndex;
/**
 *@description 小节末尾相对五线谱偏移横向坐标
 **/
@property (assign, nonatomic) CGFloat measureEndOffsetX;
/**
 *@description 小节起始相对五线谱偏移横向坐标
 **/
@property (assign, nonatomic) CGFloat measureBeginOffsetX;
/**
 *@description 小节线UI
 **/
@property (strong, nonatomic) CAShapeLayer *measureLayer;
/**
 *@description 小节index
 **/
@property (strong, nonatomic) CATextLayer *measureIndexLayer;
/**
 *@description 音符编辑模式
 **/
@property (assign, nonatomic) BOOL isMoveEditMode;
/**
 *@description 当前曲谱显示上限
 **/
@property (assign, nonatomic) LxMcMaxSection maxSection;

/******************************************************音符相关***************************************************************/

/**
 *@description 存放所有高音谱音符
 **/
@property (strong, nonatomic) NSMutableArray <LxMcNoteView *>*clefUpNoteViewArray;
/**
 *@description 存放所有高音谱强度标记
 **/
@property (strong, nonatomic) NSMutableArray <LxMcNoteView *>*clefUpStrengthArray;
/** 存放高音谱音名元素 **/
@property (strong, nonatomic) NSMutableArray <CALayer *>* clefUpPhoneNameLayerArray;
/** 存放低音谱音名元素 **/
@property (strong, nonatomic) NSMutableArray <CALayer *>* clefDoPhoneNameLayerArray;
//* 存放音谱音名元素 *
@property (strong, nonatomic) NSMutableArray <CALayer *>* clefPhoneNameLayerArray;
/**
 *@description 最小时值音符，默认为四分音符
 **/
@property (assign, nonatomic) MusicNodeType minNodeType;
/**
 *@description 存放所有低音谱音符
 **/
@property (strong, nonatomic) NSMutableArray <LxMcNoteView *>*clefDoNoteViewArray;
/**
 *@description 存放所有低音谱强度标记
 **/
@property (strong, nonatomic) NSMutableArray <LxMcNoteView *>*clefDoStrengthArray;

/**
 *@description 获取基础小节model
 **/
+ (LxMcMeasureModel *)lx_defaultMeasureModel;
/**
 *@description 判断并加入音符放置
 *@param noteView 音符
 *@param beatsType 曲谱类型
 *@param BOOL 是否可添加至当前小节
 **/
- (BOOL)judgeAndAddNoteView:(LxMcNoteView *)noteView
                  staffLine:(LxMcStaffBeatsType)beatsType;

- (BOOL)judgePhoneAndAddNoteView:(LxMcNoteView *)noteView
                       staffLine:(LxMcStaffBeatsType)beatsType;

/**
 *@description 删除所有元素
 **/
- (void)lx_clearAllElements;
/**
 *@description 获取所有音符
 **/
- (NSMutableArray <LxMcNoteView *>*)allNoteViewsArray;

/**
 *@description 获取所有需要播放的音符
 **/
- (NSMutableArray <LxMcNoteView *>*)allPlayNoteViewsArray;
/**
 *@description 获取小节总时值的单位是否满足五线谱小节
 *@param beatsType 曲谱节拍类型
 *@param
 **/
- (BOOL)checkMeasureDurationUnitWithStaffBeatsType:(LxMcStaffBeatsType)beatsType
                                          clefType:(LxMcState)clefType;



/**
 *@description 选取最小音符（最小为4分音符)
 **/
- (void)checkMinNodeUnit;
/**
 *@description 重新对小节内的音符做偏移
 **/
- (void)lx_allElementsAddOffsetX:(CGFloat)offset_x;
/** 是否为第一个出现的对应midita **/
- (BOOL)lx_firstAppeargNoteView:(LxMcNoteView *)noteView;
/** 单个小节是否满足音符是否加满 **/
- (BOOL)ypj_isFullWithStaffLine:(LxMcStaffBeatsType)beatsType isUpClef:(BOOL)isUpClef;
/** 计算音符总时长 **/
- (CGFloat)clefMeasureTotalDurationWithNoteViews:(NSMutableArray <LxMcNoteView *>*)noteViews;
@end
