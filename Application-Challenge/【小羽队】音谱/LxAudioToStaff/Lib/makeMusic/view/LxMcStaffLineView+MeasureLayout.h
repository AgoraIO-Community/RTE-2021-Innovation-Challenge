//
//  LxMcStaffLineView+MeasureLayout.h
//  SmartPiano
//
//  Created by DavinLee on 2018/2/28.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcStaffLineView.h"
@interface LxMcStaffLineView (MeasureLayout)
/**
 *@description 添加小节
 **/
- (void)measureAdd;
/**
 *@description 删除小节
 **/
- (void)measureDelete;
/**
 *@description 检测音符放置
 **/
- (BOOL)checkNoteView:(LxMcNoteView *)noteView
     superOffsetPoint:(CGPoint)offsetPoint;
- (BOOL)checkNoteView:(LxMcNoteView *)noteView superOffsetPoint:(CGPoint)offsetPoint touchPoint:(CGPoint)touchPoint;
- (BOOL)checkNoteView:(LxMcNoteView *)noteView superOffsetPoint:(CGPoint)offsetPoint touchPoint:(CGPoint)touchPoint miditag:(NSInteger)miditag;
/**
 *@description 获取小节总时长单位
 **/
+ (CGFloat)durationUnitWithBeatasType:(LxMcStaffBeatsType)beatSType;


- (void)setStaticNotes:(LxMcMeasureModel *)measureModel;
/**
 *@description 重新对小节内高音谱和低音谱音符布局
 *@param 对应布局的小节model
 **/
- (void)lx_reLayoutAllelementsInMeasure:(LxMcMeasureModel *)measureModel;
/** 获取正常小节宽 **/
- (CGFloat)defaultMeasureWidth;
/** 获取小节末尾宽 **/
- (CGFloat)defaultMeasureEndWidth;
/** 去除所有元素 **/
- (void)resetDefaultStaffLineView;
/** 检查是否所有小节音符时值填满 **/
- (BOOL)checkMeasuresRight;
/** 查找当前所在位置的小节 **/
- (LxMcMeasureModel *)searchMeasureModelWithHeadRealPoint:(CGPoint)realPoint;
/** 删除某个已放置的音符 **/
- (void)lx_deleteNote:(LxMcNoteView *)noteView;
/** 根据已放置的音符添加小节线 **/
- (void)measureAddForNote:(BOOL)isAdd;

/**
 *@description 更改曲谱的高低音谱显示(需要五线谱父视图更改视图的大小）
 *@return 返回修改视图后的父视图高度
 **/
- (CGFloat)lx_changeClefType:(LxMcState)clefType
                  staffScale:(CGFloat)staffScale
              animationBlock:(void(^)(void))animationBlock
               completeBlock:(void(^)(void))completeBlock;
- (CGFloat)lx_changeClefType:(LxMcState)clefType;
/**
 *@description 获取缩放后滚动视图大小(执行并赋值scrollview后需要执行获取staff中心位置重新放置)
 *@param clefType 当前高低音谱模式
 *@param staffScale 针对曲谱缩放比例
 **/
- (CGFloat)lx_getScrollHeightWithClefType:(LxMcState)clefType
                               staffScale:(CGFloat)staffScale;
/**
 *@description 获取缩放后stafflineview的中心y位置
 **/
- (CGFloat)lx_getStaffCenterYWithClefType:(LxMcState)clefType
                               staffScale:(CGFloat)staffScale;
/** 获取对应小节model **/

- (CGFloat)noteBeatsForStaffBeatsType:(LxMcStaffBeatsType)staffBeatsType;

- (CGFloat)noteTypeBeatsForNoteType:(MusicNodeType)noteType isDot:(BOOL)isDot;

/** Lx description   根据当前纵列index，插入音符  **/
- (void)lx_addNote:(LxMcNoteView *)noteView;

@end
