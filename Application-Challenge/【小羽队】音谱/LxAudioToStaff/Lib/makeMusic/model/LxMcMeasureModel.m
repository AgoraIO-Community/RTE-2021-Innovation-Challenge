//
//  LxMcMeasureModel.m
//  SmartPiano
//
//  Created by DavinLee on 2018/2/1.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcMeasureModel.h"
#import "CATextLayer+Default.h"
#import "LxMcStaffLineView+MeasureLayout.h"

#pragma mark - CallFunction
/**
 *@description 获取基础小节model
 **/
@implementation LxMcMeasureModel
+ (LxMcMeasureModel *)lx_defaultMeasureModel
{
    LxMcMeasureModel *model = [LxMcMeasureModel new];
    model.clefUpNoteViewArray = [[NSMutableArray alloc] init];
    model.clefDoNoteViewArray = [[NSMutableArray alloc] init];
    model.minNodeType = MusicNodeQuarter;
    return model;
}


/**
 *@description 判断并加入音符放置
 *@param noteView 音符
 *@param beatsType
 **/
- (BOOL)judgeAndAddNoteView1:(LxMcNoteView *)noteView
                  staffLine:(LxMcStaffBeatsType)beatsType
{
    //单个音符超过最大小节(2/4拍的小节与2分附点音符)
    if ([noteView durationUnit] > [LxMcStaffLineView durationUnitWithBeatasType:beatsType]) {

        debugLog(@"音符总时长超过最大小节总时长");
        return NO;
    }
    
    NSMutableArray <LxMcNoteView *>* clefNoteViews = noteView.isUpClef ? self.clefUpNoteViewArray : self.clefDoNoteViewArray;
    //已添加音符时长
    CGFloat totalDuration = [self clefMeasureTotalDurationWithNoteViews:clefNoteViews];
    debugLog(@"总共添加了%lu个音符",(unsigned long)clefNoteViews.count);
    //乐谱时长上线
    CGFloat totalDurationMax = [LxMcStaffLineView durationUnitWithBeatasType:beatsType];
    if (!self.isMoveEditMode) {
        totalDurationMax = totalDurationMax * self.maxSection;
    }
    
    
    if (totalDuration + [noteView durationUnit] <= totalDurationMax) {
        if (clefNoteViews.count > 0) {
            LxMcNoteView *endNoteView = nil;
            for (LxMcNoteView *noteView in clefNoteViews) {
                if (noteView.fixedPosition.end && noteView.fixedPosition.fixed) {
                    endNoteView = noteView;
                    [clefNoteViews removeObject:noteView];
                    break;
                }
            }
            
            NSInteger insertIndex = 0;
            for (int i = 0 ; i < clefNoteViews.count; i ++) {
                LxMcNoteView *tempNoteView = clefNoteViews[i];
                if (tempNoteView.center.x + [tempNoteView judgePointToCenter].x > noteView.rightHeadPosition.x) {
                    if (tempNoteView.fixedPosition.fixed && i == 0) {
                        insertIndex = i + 1;
                    }else
                    {
                        insertIndex = i;
                    }
                    
                    break;
                }else
                {
                    insertIndex = i + 1;
                }
            }
            if ([clefNoteViews containsObject:noteView]) {
                NSInteger oldIndex = [clefNoteViews indexOfObject:noteView];
                [clefNoteViews insertObject:noteView atIndex:insertIndex];
                [clefNoteViews removeObjectAtIndex:(oldIndex <= insertIndex ? oldIndex  : oldIndex + 1)];
                
            }else
            {
                if (!self.isMoveEditMode) {
                    [clefNoteViews addObject:noteView];
                } else {
                    [clefNoteViews insertObject:noteView atIndex:insertIndex];
                }
            }
            if (endNoteView) {
                [clefNoteViews addObject:endNoteView];
            }
            
        }else
        {
            if ([clefNoteViews containsObject:noteView]) {
                [clefNoteViews removeObject:noteView];
            }
            [clefNoteViews addObject:noteView];
        }
        if (noteView.noteType > self.minNodeType) {
            self.minNodeType = noteView.noteType;
        }
        return YES;
    }else
    {
        
        debugLog(@"音符总时长超过最大小节总时长");
        return NO;
    }
}

/**
 *@description 判断并加入音符放置
 *@param noteView 音符
 *@param beatsType
 **/
- (BOOL)judgeAndAddNoteView:(LxMcNoteView *)noteView
                  staffLine:(LxMcStaffBeatsType)beatsType
{
    //单个音符超过最大小节(2/4拍的小节与2分附点音符)
//    if ([noteView durationUnit] > [LxMcStaffLineView durationUnitWithBeatasType:beatsType]) {
//
//        debugLog(@"音符总时长超过最大小节总时长");
//        return NO;
//    }

    NSMutableArray <LxMcNoteView *>* clefNoteViews = noteView.isUpClef ? self.clefUpNoteViewArray : self.clefDoNoteViewArray;
    //已添加音符时长
//    CGFloat totalDuration = [self clefMeasureTotalDurationWithNoteViews:clefNoteViews];
//    debugLog(@"总共添加了%lu个音符",(unsigned long)clefNoteViews.count);
    //乐谱时长上线
//    CGFloat totalDurationMax = [LxMcStaffLineView durationUnitWithBeatasType:beatsType];
//    if (!self.isMoveEditMode) {
//        totalDurationMax = totalDurationMax * self.maxSection;
//    }
//    if (totalDuration + [noteView durationUnit] <= totalDurationMax) {
        if (clefNoteViews.count > 0) {
            LxMcNoteView *endNoteView = nil;
            for (LxMcNoteView *noteView in clefNoteViews) {
                if (noteView.fixedPosition.end && noteView.fixedPosition.fixed) {
                    endNoteView = noteView;
                    [clefNoteViews removeObject:noteView];
                    break;
                }
            }
            
            NSInteger insertIndex = 0;
            for (int i = 0 ; i < clefNoteViews.count; i ++) {
                LxMcNoteView *tempNoteView = clefNoteViews[i];
                if (tempNoteView.center.x + [tempNoteView judgePointToCenter].x > noteView.rightHeadPosition.x) {
                    if (tempNoteView.fixedPosition.fixed && i == 0) {
                        insertIndex = i + 1;
                    }else
                    {
                       insertIndex = i;
                    }

                    break;
                }else
                {
                    insertIndex = i + 1;
                }
            }
            if ([clefNoteViews containsObject:noteView]) {
                NSInteger oldIndex = [clefNoteViews indexOfObject:noteView];
                [clefNoteViews insertObject:noteView atIndex:insertIndex];
                [clefNoteViews removeObjectAtIndex:(oldIndex <= insertIndex ? oldIndex  : oldIndex + 1)];
                
            }else
            {
                if (!self.isMoveEditMode) {

                    [clefNoteViews addObject:noteView];
                } else {
                    if ([self isCheckSameMidKeyNoteView:noteView withEventDuration:clefNoteViews[insertIndex - 1].eventDuration + [clefNoteViews[insertIndex - 1] durationUnit]]) {
                        return  NO;
                    }
                    [clefNoteViews insertObject:noteView atIndex:insertIndex];
                }
            }
            if (endNoteView) {
                [clefNoteViews addObject:endNoteView];
            }
            
        }else
        {
            if ([clefNoteViews containsObject:noteView]) {
                [clefNoteViews removeObject:noteView];
            }
            if ([self isCheckSameMidKeyNoteView:noteView withEventDuration:0.f]) {

                return  NO;
            }
            [clefNoteViews addObject:noteView];
        }
        if (noteView.noteType > self.minNodeType) {
            self.minNodeType = noteView.noteType;
        }
        return YES;
//    }
//    else
//    {
//        debugLog(@"音符总时长超过最大小节总时长");
//        return NO;
//    }
}


- (BOOL)isCheckSameMidKeyNoteView:(LxMcNoteView *)noteView withEventDuration:(CGFloat)eventDuration{
    NSMutableArray <LxMcNoteView *>* clefNoteViews = !noteView.isUpClef ? self.clefUpNoteViewArray : self.clefDoNoteViewArray;
    for (LxMcNoteView *noteViewT in clefNoteViews) {
        if (eventDuration == noteViewT.eventDuration) {
            if (noteView.miditag == noteViewT.miditag) {
                return YES;
            }
        }
    }
    return NO;
}

/**
 *@description 判断并加入音符放置
 *@param noteView 音符
 *@param beatsType
 **/
- (BOOL)judgePhoneAndAddNoteView:(LxMcNoteView *)noteView
                  staffLine:(LxMcStaffBeatsType)beatsType
{
    //单个音符超过最大小节(2/4拍的小节与2分附点音符)
    if ([noteView durationUnit] > [LxMcStaffLineView durationUnitWithBeatasType:beatsType]) {
       
        return NO;
    }
    
    NSMutableArray <LxMcNoteView *>* clefNoteViews = noteView.isUpClef ? self.clefUpNoteViewArray : self.clefDoNoteViewArray;
    //已添加音符时长
    CGFloat totalDuration = [self clefMeasureTotalDurationWithNoteViews:clefNoteViews];
    debugLog(@"总共添加了%lu个音符",(unsigned long)clefNoteViews.count);
    //乐谱时长上线
    CGFloat totalDurationMax = [LxMcStaffLineView durationUnitWithBeatasType:beatsType];
    if (!self.isMoveEditMode) {
        totalDurationMax = totalDurationMax * self.maxSection;
    }

    if (totalDuration + [noteView durationUnit] <= totalDurationMax) {
        if (noteView.isRest) {
            [clefNoteViews addObject:noteView];
            return YES;
        }
        
        if (!self.isMoveEditMode) {
            noteView.sectionIndex = clefNoteViews.count;
            [clefNoteViews addObject:noteView];
        }else if (self.clefPhoneNameLayerArray.count == 0){
            [clefNoteViews addObject:noteView];
            return YES;
        }
        else {
            NSArray *phoneABCLayerArray = self.clefPhoneNameLayerArray;
            for (CALayer *layer in phoneABCLayerArray) {
                if (CGRectGetMinX(layer.frame) - 13 <= noteView.centerX && noteView.centerX <= CGRectGetMaxX(layer.frame) + 13) {
                    NSNumber *indexNumber = [layer valueForKey:@"index"];
                    NSNumber *isDefault = [layer valueForKey:@"isDefault"];
                    noteView.sectionIndex = [indexNumber integerValue];
                    if ([isDefault isEqualToNumber:@(1)]) {
                        debugLog(@"音符已经添加");
                        return NO;
                    }else if (noteView.noteType != [[layer valueForKey:@"noteType"] integerValue])
                    {
                        debugLog(@"音符类型错误");
                        
                        return NO;
                    }
                    else {
                        [clefNoteViews addObject:noteView];
                        
                        for (LxMcNoteView *noteView in clefNoteViews) {
                            debugLog(@"section%ld",(long)noteView.sectionIndex);
                        }
                        return YES;
                    }
                }
            }
            return NO;
        }
    }else {
      
        debugLog(@"音符总时长超过最大小节总时长");
        return NO;
    }
    return NO;
}

/** 获取小节音谱内的音符总时值(0 - 1) **/
- (CGFloat)clefMeasureTotalDurationWithNoteViews:(NSMutableArray <LxMcNoteView *>*)noteViews
{
    CGFloat totalDuration = 0;
    for (LxMcNoteView *note in noteViews) {
        if (note.noteType != MusicNodeLoading) {
            totalDuration += [note durationUnit];
        }
    }
    return totalDuration;
}
/**
 *@description 删除所有元素
 **/
- (void)lx_clearAllElements
{
    for (LxMcNoteView *noteView in self.clefUpNoteViewArray) {
        [noteView removeFromSuperview];
    }
    [self.clefUpNoteViewArray removeAllObjects];
    for (LxMcNoteView *noteView in self.clefDoNoteViewArray) {
        [noteView removeFromSuperview];
    }
    [self.clefDoNoteViewArray removeAllObjects];
    for (LxMcNoteView *noteView in self.clefUpStrengthArray) {
        [noteView removeFromSuperview];
    }
    [self.clefUpStrengthArray removeAllObjects];
    for (LxMcNoteView *noteView in self.clefDoStrengthArray) {
        [noteView removeFromSuperview];
    }
    [self.clefDoStrengthArray removeAllObjects];
    [self.measureLayer removeFromSuperlayer];
    self.measureLayer = nil;
    [self.measureIndexLayer removeFromSuperlayer];
    self.measureIndexLayer = nil;
}
/**
 *@description 获取所有音符
 **/
- (NSMutableArray <LxMcNoteView *>*)allNoteViewsArray
{
    NSMutableArray <LxMcNoteView *>* allNoteS = [NSMutableArray array];
    [allNoteS addObjectsFromArray:self.clefUpNoteViewArray];
    [allNoteS addObjectsFromArray:self.clefDoNoteViewArray];
    [allNoteS addObjectsFromArray:self.clefUpStrengthArray];
    [allNoteS addObjectsFromArray:self.clefDoStrengthArray];
    return allNoteS;
    
}
/**
 *@description 获取所有需要播放的音符
 **/
- (NSMutableArray <LxMcNoteView *>*)allPlayNoteViewsArray {
    NSMutableArray <LxMcNoteView *>* allNoteS = [NSMutableArray array];
    [allNoteS addObjectsFromArray:self.clefUpNoteViewArray];
    [allNoteS addObjectsFromArray:self.clefDoNoteViewArray];
    return allNoteS;
}

/**
 *@description 获取小节总时值的单位是否满足五线谱小节
 *@param beatsType 曲谱节拍类型
 *@param
 **/
- (BOOL)checkMeasureDurationUnitWithStaffBeatsType:(LxMcStaffBeatsType)beatsType
                                          clefType:(LxMcState)clefType;
{
    CGFloat staffUnit = beatsType / 4.f;
    if (beatsType <= LxMcStaffBeats4_4) {
       staffUnit = beatsType / 4.f;
    }else{
        switch (beatsType) {
            case lxMcStaffBeats3_8:
                staffUnit = 3.f / 8.f;
                break;
                
            default:
                break;
        }
    }
    CGFloat clefDurationUnit = 0;
    if (clefType != LxMcMcLowClef) {
        for (LxMcNoteView *noteView in self.clefUpNoteViewArray) {
            clefDurationUnit += [noteView durationUnit];
        }
        if (clefDurationUnit < staffUnit) {
            return NO;
        }
    }
    clefDurationUnit = 0;
    if (clefType != LxMcMcHighClef) {
        for (LxMcNoteView *noteView in self.clefDoNoteViewArray) {
            clefDurationUnit += [noteView durationUnit];
        }
        if (clefDurationUnit < staffUnit) {
            return NO;
        }    }
    return YES;
}
- (void)checkMinNodeUnit
{
    MusicNodeType minType = MusicNodeQuarter;
    for (LxMcNoteView *noteView in self.clefUpNoteViewArray) {
        if (noteView.noteType > minType) {
            minType = noteView.noteType;
        }
    }
    for (LxMcNoteView *noteView in self.clefDoNoteViewArray) {
        if (noteView.noteType > minType) {
            minType = noteView.noteType;
        }
    }
}
/**
 *@description 重新对小节内的音符做偏移
 **/
- (void)lx_allElementsAddOffsetX:(CGFloat)offset_x
{
    for (LxMcNoteView *noteView in self.clefUpNoteViewArray) {
        [UIView transitionWithView:noteView
                          duration:0.5
                           options:UIViewAnimationOptionTransitionCrossDissolve
                        animations:^{
                            noteView.centerX += offset_x;
                            if (noteView.strengthView) {
                                CGPoint strPoint = noteView.strengthView.center;
                                strPoint.x = noteView.centerX;
                                noteView.strengthView.center = strPoint;
                            }
                        } completion:^(BOOL finished) {
                            
                        }];
    }
    for (LxMcNoteView *noteView in self.clefDoNoteViewArray) {
        [UIView transitionWithView:noteView
                          duration:0.5
                           options:UIViewAnimationOptionTransitionCrossDissolve
                        animations:^{
                            noteView.centerX += offset_x;
                            if (noteView.strengthView) {
                                CGPoint strPoint = noteView.strengthView.center;
                                strPoint.x = noteView.centerX;
                                noteView.strengthView.center = strPoint;
                            }
                        } completion:^(BOOL finished) {
                            
                        }];
    }
}

/** 是否为第一个出现的对应midita **/
- (BOOL)lx_firstAppeargNoteView:(LxMcNoteView *)noteView;{
    if (noteView.isUpClef) {
        for (LxMcNoteView *tempNoteView in self.clefUpNoteViewArray) {
            if (tempNoteView.miditag == noteView.miditag ) {
                if (tempNoteView == noteView) {
                    return YES;
                }
                return NO;
                
            }
        }
    }else{
        for (LxMcNoteView *tempNoteView in self.clefDoNoteViewArray) {
            if (tempNoteView.miditag == noteView.miditag ) {
                if (tempNoteView == noteView) {
                    return YES;
                }
                return NO;
            }
        }
    }
    return NO;
}

#pragma mark  --  lazy
- (NSMutableArray<LxMcNoteView *> *)clefUpStrengthArray {
    if (!_clefUpStrengthArray) {
        _clefUpStrengthArray = [[NSMutableArray alloc] init];
    }
    return _clefUpStrengthArray;
}

- (NSMutableArray<LxMcNoteView *> *)clefDoStrengthArray {
    if (!_clefDoStrengthArray) {
        _clefDoStrengthArray = [[NSMutableArray alloc] init];
    }
    return _clefDoStrengthArray;
}

- (BOOL)ypj_isFullWithStaffLine:(LxMcStaffBeatsType)beatsType isUpClef:(BOOL)isUpClef{
    BOOL isFull = YES;
    for (CALayer *layer in self.clefPhoneNameLayerArray) {
        NSNumber *isDefault = [layer valueForKey:@"isDefault"];
        if (![isDefault isEqualToNumber:@(1)]) {
            isFull = NO;
        }
    }
    return isFull;
}

- (void)dealloc
{
    debugLog(@"%s",__func__);
}

@end
