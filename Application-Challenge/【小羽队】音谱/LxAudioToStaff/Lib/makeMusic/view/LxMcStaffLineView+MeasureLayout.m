//
//  LxMcStaffLineView+MeasureLayout.m
//  SmartPiano
//
//  Created by DavinLee on 2018/2/28.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcStaffLineView+MeasureLayout.h"
#import "LxMcNoteView+Bubble.h"
#import "LxMcMeasureModel.h"
#import "YPNodeTypeModel.h"
#import "CAShapeLayer+Default.h"

#import "CATextLayer+Default.h"
#import <pop/POP.h>
@implementation LxMcStaffLineView (MeasureLayout)
#pragma mark - CallFunction

/**
 *@description 添加小节
 **/
- (void)measureAdd
{
//    if (self.ClefMeasureModelArray.count < 8) {
        
        LxMcMeasureModel *measureModel = [LxMcMeasureModel lx_defaultMeasureModel];
        measureModel.measureIndex = self.ClefMeasureModelArray.count;
        _currentEndLineOffsetX += [self defaultMeasureWidth];
        measureModel.measureEndOffsetX = _currentEndLineOffsetX;
        if (self.ClefMeasureModelArray.count > 0) {
            LxMcMeasureModel *lastModel = [self.ClefMeasureModelArray lastObject];
            measureModel.measureBeginOffsetX = lastModel.measureEndOffsetX;
        }else
        {
            measureModel.measureBeginOffsetX = _originAvalibleOffsetX;
        }
        [self.ClefMeasureModelArray addObject:measureModel];
        measureModel.measureIndex = self.ClefMeasureModelArray.count - 1;
        CAShapeLayer *measureLayer = [self defaultMeasureLineLayerWithOffsetX:_currentEndLineOffsetX];
        [self.layer addSublayer:measureLayer];
        measureModel.measureLayer = measureLayer;
        
        CATextLayer *measureIndexLayer = [self measureIndexLayerWithIndex:measureModel.measureIndex + 1];
        measureIndexLayer.position = CGPointMake(_currentEndLineOffsetX, CGRectGetHeight(self.frame)/2.f - 7 * kMcStaffSpace);
        [self.layer addSublayer:measureIndexLayer];
        measureModel.measureIndexLayer = measureIndexLayer;
        [self stafflineLayerReLayoutWidth];
        [self.delegate reSetScrollViewContent];
    
        measureLayer.opacity = 0;
        measureIndexLayer.opacity = 0;
        
        //重置八度音区
        [self resetOctave];
//    }
}
/**
 *@description 删除小节
 **/
- (void)measureDelete
{
    if (self.ClefMeasureModelArray.count > 4) {
        
        LxMcMeasureModel *deleteModel = [self.ClefMeasureModelArray lastObject];
        
        NSMutableArray <LxMcNoteView *>*deleteNotes = [deleteModel allNoteViewsArray];
        for (LxMcNoteView *deleNote in deleteNotes) {
            [self clearAdditionLineArrayWithNoteView:deleNote];
        }
        [deleteModel lx_clearAllElements];
        
        LxMcMeasureModel *measureModel = self.ClefMeasureModelArray[self.ClefMeasureModelArray.count - 2];
        _currentEndLineOffsetX = measureModel.measureEndOffsetX;
        [self.ClefMeasureModelArray removeLastObject];
        [self stafflineLayerReLayoutWidth];
        [self.delegate reSetScrollViewContent];
    }
}

/**
 *@description 检测音符放置
 **/
- (BOOL)checkNoteView:(LxMcNoteView *)noteView superOffsetPoint:(CGPoint)offsetPoint {
    return [self checkNoteView:noteView superOffsetPoint:offsetPoint touchPoint:CGPointZero];
}
- (BOOL)checkNoteView:(LxMcNoteView *)noteView superOffsetPoint:(CGPoint)offsetPoint touchPoint:(CGPoint)touchPoint {
    return [self checkNoteView:noteView superOffsetPoint:offsetPoint touchPoint:touchPoint miditag:0];
}

- (BOOL)checkNoteView:(LxMcNoteView *)noteView superOffsetPoint:(CGPoint)offsetPoint touchPoint:(CGPoint)touchPoint miditag:(NSInteger)miditag {
    
    CGPoint realHeadPoint = CGPointMake(noteView.center.x + offsetPoint.x + [noteView judgePointToCenter].x,
                                        noteView.center.y + offsetPoint.y + [noteView judgePointToCenter].y);
//    debugLog(@"实际判断位置x = %f,y = %f",realHeadPoint.x,realHeadPoint.y);
    if (miditag == 0) {
        miditag = [self miditagWithRealPoint:realHeadPoint
                                dragNoteView:noteView];
    }
    
    //(休止符不走 拆分小节没有更改音区范围无法插入)
    if ((!CGPointEqualToPoint(touchPoint, CGPointZero) || noteView.isKeyboardInput) && !noteView.isRest) {
        CGRect effectiveFrame = noteView.isUpClef == YES ? self.highOctaveView.frame : self.lowOctaveView.frame;
        effectiveFrame = CGRectMake(effectiveFrame.origin.x, effectiveFrame.origin.y + 5 * kMcStaffScale, effectiveFrame.size.width, effectiveFrame.size.height + 5 * kMcStaffScale);
        if (!CGRectContainsPoint(effectiveFrame, noteView.rightHeadPosition)) {
            [noteView removeFromSuperview];
            POPBasicAnimation *alphaAni = [POPBasicAnimation animationWithPropertyNamed:kPOPViewAlpha];
            alphaAni.fromValue = @(1.0);
            alphaAni.toValue = @(0.0);
            alphaAni.repeatCount = 3;
            alphaAni.duration = 1.0;
            [self.highOctaveView pop_addAnimation:alphaAni forKey:@"alphaHigh"];
            [self.lowOctaveView pop_addAnimation:alphaAni forKey:@"alphaLow"];
            [self removeStrengthWithNoteView:noteView];
            return NO;
        }
    }
    
    noteView.miditag = miditag;
    
    [noteView checkDirection];
    LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:realHeadPoint];
    if (currentModel && miditag > 0) {
        BOOL canLocate = [currentModel judgeAndAddNoteView:noteView
                                                 staffLine:self.staffBeatsType];
        if (canLocate) {
            if ((!CGPointEqualToPoint(touchPoint, CGPointZero) || self.isOutNote /** 外部移入时touchpoint为空 **/ || noteView.isKeyboardInput /** 钢琴输入时touchpoint为空 **/
                 ) &&
                canLocate) {
                if (!noteView.isLocatedStaff && noteView.superview) {/** 通过从外部移入的,提前重新定位，避免动画起始位置有问题 **/
                    CGRect frame = [noteView.superview convertRect:noteView.frame toView:self];
                    noteView.frame = frame;
                }
                [noteView removeFromSuperview];
                [self addSubview:noteView];
                [self lx_reLayoutAllelementsInMeasure:currentModel];
                noteView.measureIndex = currentModel.measureIndex;
                
            }
            [self setAlter:noteView withMeasureModel:currentModel];
        }else if(noteView.isLocatedStaff)/** 放置过音符，但是重新放置的音谱已无法加入该时值的音符 **/
        {
            
            noteView.isLocatedStaff = NO;
            [UIView transitionWithView:noteView
                              duration:0.5
                               options:UIViewAnimationOptionTransitionCrossDissolve
                            animations:^{
                                noteView.alpha = 0;
                            } completion:^(BOOL finished) {
                                [noteView removeFromSuperview];
                                [self removeStrengthWithNoteView:noteView];
                                [self setAlter:noteView withMeasureModel:currentModel];
                            }];
        }
        
        return canLocate;
    }else
    {
        if (noteView.isLocatedStaff && noteView.miditag == 0 /** 已经放置在五线谱且移动至无法判断区域 **/) {
            noteView.isLocatedStaff = NO;
            [UIView transitionWithView:noteView
                              duration:0.5
                               options:UIViewAnimationOptionTransitionCrossDissolve
                            animations:^{
                                noteView.alpha = 0;
                            } completion:^(BOOL finished) {
                                [noteView removeFromSuperview];
                                [self removeStrengthWithNoteView:noteView];
                                [self setAlter:noteView withMeasureModel:currentModel];
                            }];
        }else// if(noteView.isLocatedStaff)/** 放置过音符，但是重新放置的音谱已无法加入该时值的音符 **/
        {
            noteView.isLocatedStaff = NO;
            [UIView transitionWithView:noteView
                              duration:0.5
                               options:UIViewAnimationOptionTransitionCrossDissolve
                            animations:^{
                                noteView.alpha = 0;
                            } completion:^(BOOL finished) {
                                [noteView removeFromSuperview];
                                [self removeStrengthWithNoteView:noteView];
                                [self setAlter:noteView withMeasureModel:currentModel];
                            }];
        }
        return  NO;
    }
    
    return YES;
    
}

- (void)removeStrengthWithNoteView:(LxMcNoteView *)noteView
{
    if (noteView.strengthView) {
        [noteView.strengthView removeFromSuperview];
        LxMcMeasureModel *meaSureModel = self.ClefMeasureModelArray[MIN(self.ClefMeasureModelArray.count - 1, noteView.measureIndex)];
        if ([meaSureModel.clefDoStrengthArray containsObject:noteView.strengthView]) {
            [meaSureModel.clefDoStrengthArray removeObject:noteView.strengthView];
        }else if ([meaSureModel.clefUpStrengthArray containsObject:noteView.strengthView])
        {
            [meaSureModel.clefUpStrengthArray removeObject:noteView.strengthView];
        }
    }
}
/** 去除所有元素 **/
- (void)resetDefaultStaffLineView
{
    for (int i = 0;i < self.ClefMeasureModelArray.count; i ++) {
        LxMcMeasureModel *deleteModel = self.ClefMeasureModelArray[i];
        
        NSMutableArray <LxMcNoteView *>*deleteNotes = [deleteModel allNoteViewsArray];
        for (LxMcNoteView *deleNote in deleteNotes) {
            [self clearAdditionLineArrayWithNoteView:deleNote];
        }
        [deleteModel lx_clearAllElements];
    }
    [self.ClefMeasureModelArray removeAllObjects];
    _currentEndLineOffsetX = _originAvalibleOffsetX;
}
- (BOOL)checkMeasuresRight
{
    BOOL measuresRight = YES;
    for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray) {
        if (![measureModel checkMeasureDurationUnitWithStaffBeatsType:_staffBeatsType clefType:self.staffClefState]) {
            measuresRight = NO;
            break;
        }
    }
    
    return measuresRight;
}
/**
 *@description 获取缩放后滚动视图大小
 *@param clefType 当前高低音谱模式
 *@param staffScale 针对曲谱缩放比例
 **/
- (CGFloat)lx_getScrollHeightWithClefType:(LxMcState)clefType
                               staffScale:(CGFloat)staffScale
{
    self.staffClefState = clefType;
    CGFloat staffHeight = 0;
    switch (clefType) {
        case LxMcMcBothClef:
        {
            staffHeight = 485 * staffScale;
        }
            break;
        case LxMcMcHighClef:
        {
            staffHeight = (3 + 4 + 3) * kMcStaffSpace * staffScale;
        }
            break;
        case LxMcMcLowClef:
        {
            staffHeight = (3 + 4 + 3) * kMcStaffSpace * staffScale;
        }
            break;
        default:
            break;
    }
    
    return staffHeight;
}

/**
 *@description 获取缩放后stafflineview的中心y位置
 **/
- (CGFloat)lx_getStaffCenterYWithClefType:(LxMcState)clefType
                               staffScale:(CGFloat)staffScale
{
    CGFloat centerY = 0;
    CGFloat staffHeight = [self lx_getScrollHeightWithClefType:clefType
                                                    staffScale:staffScale];
    switch (clefType) {
        case LxMcMcBothClef:
        {
            centerY = staffHeight / 2.f;
            
        }
            break;
        case LxMcMcHighClef:
        {
            CGFloat highClefCenterLineY = staffHeight - 5 * kMcStaffSpace * staffScale;
            centerY = staffHeight / 2.f + (CGRectGetHeight(self.frame)/2.f - highClefCenterLineY);
            
        }
            break;
        case LxMcMcLowClef:
        {
            CGFloat highClefCenterLineY = staffHeight + 5 * kMcStaffSpace * staffScale;
            centerY = staffHeight / 2.f - (highClefCenterLineY - CGRectGetHeight(self.frame)/2.f);
        }
            break;
        default:
            break;
    }
    return centerY;
}
/**
 *@description 更改曲谱的高低音谱显示(需要五线谱父视图更改视图的大小）
 *@return 返回修改视图后的父视图高度
 **/

- (CGFloat)lx_changeClefType:(LxMcState)clefType {
    return [self lx_changeClefType:clefType staffScale:1.0 animationBlock:nil completeBlock:nil];
}

- (CGFloat)lx_changeClefType:(LxMcState)clefType
                  staffScale:(CGFloat)staffScale
              animationBlock:(void(^)(void))animationBlock
               completeBlock:(void(^)(void))completeBlock;
{
    self.staffClefState = clefType;
    CGFloat staffHeight = 0;
    CGFloat centerY = 0;
    switch (clefType) {
        case LxMcMcBothClef:
        {
            staffHeight = 485 * staffScale;
            centerY = staffHeight / 2.f;
            
        }
            break;
        case LxMcMcHighClef:
        {
            staffHeight = (3 + 5 + 3) * kMcStaffSpace * staffScale;
            CALayer *highCenterLayer = self.stafflineLayerArray[2];
            centerY = staffHeight / 2.f + (CGRectGetHeight(self.frame)/2.f - highCenterLayer.position.y);
            
        }
            break;
        case LxMcMcLowClef:
        {
            staffHeight = (3 + 5 + 3) * kMcStaffSpace * staffScale;
            CALayer *lowCenterLayer = self.stafflineLayerArray[7];
            centerY = staffHeight / 2.f - (lowCenterLayer.position.y - CGRectGetHeight(self.frame)/2.f);
        }
            break;
        default:
            break;
    }
    //TODO:暂时去掉
//    self.isEditClefUp = NO;
//    //当前初始高低音谱编辑
//    switch (self.staffClefState) {
//        case LxMcMcBothClef:
//            self.isEditClefUp = YES;
//            break;
//        case LxMcMcHighClef:
//            self.isEditClefUp = YES;
//            break;
//        case LxMcMcLowClef:
//            self.isEditClefUp = NO;
//            break;
//
//        default:
//            break;
//    }
    
    if (animationBlock) {
        [UIView transitionWithView:self
                          duration:0.6
                           options:UIViewAnimationOptionTransitionCrossDissolve
                        animations:^{
                            CGRect superFrame = self.superview.frame;
                            superFrame.size.height = staffHeight;
                            superFrame.origin.y = self.superview.centerY - staffHeight / 2.f;
                            self.superview.frame = superFrame;
                            self.centerY = centerY;
                            if (animationBlock) {
                                animationBlock();
                            }
                        } completion:^(BOOL finished) {
                            if (completeBlock) {
                                completeBlock();
                            }
                        }];
    }else {
        CGRect superFrame = self.superview.frame;
        superFrame.size.height = staffHeight;
        superFrame.origin.y = self.superview.centerY - staffHeight / 2.f;
        self.superview.frame = superFrame;
        self.centerY = centerY;
    }
    return staffHeight;
}
/** 删除某个已放置的音符 **/
- (void)lx_deleteNote:(LxMcNoteView *)noteView
{
    [noteView removeFromSuperview];
    [self clearAdditionLineArrayWithNoteView:noteView];
    if (self.ClefMeasureModelArray.count > noteView.measureIndex) {
        LxMcMeasureModel *model = self.ClefMeasureModelArray[noteView.measureIndex];
        if ([model.clefUpNoteViewArray containsObject:noteView]) {
            [model.clefUpNoteViewArray removeObject:noteView];
        }else if ([model.clefDoNoteViewArray containsObject:noteView])
        {
            [model.clefDoNoteViewArray removeObject:noteView];
        }
        if (self.ClefMeasureModelArray.count > noteView.measureIndex)
        {
            [self lx_reLayoutAllelementsInMeasure:self.ClefMeasureModelArray[noteView.measureIndex]];
        }
    }
}

#pragma mark - 放置相关
/** 查找当前所在位置的小节 **/
- (LxMcMeasureModel *)searchMeasureModelWithHeadRealPoint:(CGPoint)realPoint
{
//    for (int i = 0 ; i < self.ClefMeasureModelArray.count; i ++) {
//        LxMcMeasureModel *model = self.ClefMeasureModelArray[i];
//        if (realPoint.x <= model.measureEndOffsetX && realPoint.x > _originAvalibleOffsetX) {
//            //添加最大小节数
//            model.maxSection = self.maxSection;
//            //编辑模式为拖拽
//            model.isMoveEditMode = self.editMode == LxMcEditModeMove ? YES:NO;
//            return model;
//        }
//    }
    return self.ClefMeasureModelArray.firstObject;
//    return nil;
}

- (void)measureAddForNote:(BOOL)isAdd {
    //琴键输入音符 --> 移除小节线
    if (self.editMode == LxMcEditModeKeyborad ) {
        
        LxMcMeasureModel *measureModel = [LxMcMeasureModel lx_defaultMeasureModel];
        measureModel.measureBeginOffsetX = _originAvalibleOffsetX;
        measureModel.measureEndOffsetX = _currentEndLineOffsetX;
        
        NSMutableArray *upTempArray = [NSMutableArray array];
        NSMutableArray *doTempArray = [NSMutableArray array];
        NSMutableArray *upStrengTempArray = [NSMutableArray array];
        NSMutableArray *doStrengTempArray = [NSMutableArray array];
        
        for (LxMcMeasureModel *model in self.ClefMeasureModelArray) {
            [model.measureLayer removeFromSuperlayer];
            [model.measureIndexLayer removeFromSuperlayer];
            //取高音谱表
            for (LxMcNoteView *noteView in model.clefUpNoteViewArray) {
                if (!noteView.isRest) {
                    [upTempArray addObject:noteView];
                }else {
                    [noteView removeFromSuperview];
                    [self clearAdditionLineArrayWithNoteView:noteView];
                    [self lx_reLayoutAllelementsInMeasure:model];
                }
            }
            //取低音谱表
            for (LxMcNoteView *noteView in model.clefDoNoteViewArray) {
                if (!noteView.isRest) {
                    [doTempArray addObject:noteView];
                }else {
                    [noteView removeFromSuperview];
                    [self clearAdditionLineArrayWithNoteView:noteView];
                    [self lx_reLayoutAllelementsInMeasure:model];
                }
            }
            
            //移除所有的加强符号
            for (LxMcNoteView *noteView in model.clefDoStrengthArray) {
                [upStrengTempArray addObject:noteView];
            }
            
            for (LxMcNoteView *noteView in model.clefUpStrengthArray) {
                [doStrengTempArray addObject:noteView];
            }
            
        }
        
        measureModel.clefUpNoteViewArray = upTempArray;
        measureModel.clefDoNoteViewArray = doTempArray;
        measureModel.clefUpStrengthArray = upStrengTempArray;
        measureModel.clefDoStrengthArray = doStrengTempArray;
        
        if (measureModel.clefDoNoteViewArray.count == 0 && measureModel.clefUpNoteViewArray.count == 0) {
            _currentEndLineOffsetX = measureModel.measureBeginOffsetX + [self defaultMeasureWidth];
            measureModel.measureEndOffsetX = _currentEndLineOffsetX;
        }
        
        CAShapeLayer *measureLayer = [self defaultMeasureLineLayerWithOffsetX:measureModel.measureEndOffsetX];
        [self.layer addSublayer:measureLayer];
        measureModel.measureLayer = measureLayer;
        measureModel.measureIndex = 0;
        
        CATextLayer *measureIndexLayer = [self measureIndexLayerWithIndex:measureModel.measureIndex + 1];
        measureIndexLayer.position = CGPointMake(measureModel.measureEndOffsetX, CGRectGetHeight(self.frame)/2.f - 7 * kMcStaffSpace);
        [self.layer addSublayer:measureIndexLayer];
        measureModel.measureIndexLayer = measureIndexLayer;
        
        [self stafflineLayerReLayoutWidth];
        [self.delegate reSetScrollViewContent];
        
        self.ClefMeasureModelArray = [@[measureModel] mutableCopy];
        
        for (LxMcMeasureModel *model in self.ClefMeasureModelArray) {
            [self lx_reLayoutAllelementsInMeasure:model];
        }
    }
    //拖拽输入音符 --> 添加小节线
    if (self.editMode == LxMcEditModeMove ) {
        NSMutableArray *tempArray = [NSMutableArray array];
        LxMcMeasureModel *mainMeasureModel = self.ClefMeasureModelArray[0];
        [mainMeasureModel.measureLayer removeFromSuperlayer];
        [mainMeasureModel.measureIndexLayer removeFromSuperlayer];
        
        debugLog(@"拖拽模式__高音谱音符数量_%lu,低音谱音符数量_%lu,",(unsigned long)mainMeasureModel.clefUpNoteViewArray.count,(unsigned long)mainMeasureModel.clefDoNoteViewArray.count);
        
        NSMutableArray *clefUpNoteViewArray;
        NSMutableArray *clefDoNoteViewArray;
        //高音谱表 小节拆分
        if (mainMeasureModel.clefUpNoteViewArray.count > 0) {
            clefUpNoteViewArray = [self segmentationSectionWithNoteViews:mainMeasureModel.clefUpNoteViewArray];
        }
        
        //低音谱表 小节拆分
        if (mainMeasureModel.clefDoNoteViewArray.count > 0) {
            clefDoNoteViewArray = [self segmentationSectionWithNoteViews:mainMeasureModel.clefDoNoteViewArray];
        }
        
        NSMutableArray *moreNoteViewArray = clefUpNoteViewArray.count >= clefDoNoteViewArray.count? clefUpNoteViewArray:clefDoNoteViewArray;
        
        for (int i = 0; i < moreNoteViewArray.count; i++) {
            LxMcMeasureModel *measureModel = [LxMcMeasureModel lx_defaultMeasureModel];
            
            if (i == 0) {
                //初始化位置
                _currentEndLineOffsetX = _originAvalibleOffsetX;
            }
            
            //小节偏移位置
            _currentEndLineOffsetX += [self defaultMeasureWidth];
            measureModel.measureEndOffsetX = _currentEndLineOffsetX;
            if (tempArray.count > 0) {
                LxMcMeasureModel *lastModel = [tempArray lastObject];
                measureModel.measureBeginOffsetX = lastModel.measureEndOffsetX;
            }else {
                measureModel.measureBeginOffsetX = _originAvalibleOffsetX;
            }
            
            measureModel.measureIndex = i;
            
            CAShapeLayer *measureLayer = [self defaultMeasureLineLayerWithOffsetX:measureModel.measureEndOffsetX];
            [self.layer addSublayer:measureLayer];
            measureModel.measureLayer = measureLayer;
            
            CATextLayer *measureIndexLayer = [self measureIndexLayerWithIndex:measureModel.measureIndex + 1];
            measureIndexLayer.position = CGPointMake(measureModel.measureEndOffsetX, CGRectGetHeight(self.frame)/2.f - 7 * kMcStaffSpace);
            [self.layer addSublayer:measureIndexLayer];
            measureModel.measureIndexLayer = measureIndexLayer;
            
            [self stafflineLayerReLayoutWidth];
            [self.delegate reSetScrollViewContent];
            
            if (!(i > clefUpNoteViewArray.count - 1) && clefUpNoteViewArray) {
                measureModel.clefUpNoteViewArray = clefUpNoteViewArray[i];
            }
            
            if (!(i > clefDoNoteViewArray.count - 1) && clefDoNoteViewArray) {
                measureModel.clefDoNoteViewArray = clefDoNoteViewArray[i];
            }
            
            [tempArray addObject:measureModel];
        }
        
        self.ClefMeasureModelArray = tempArray;
        //不够最大小节数 补足!
        int count = (int)self.maxSection - (int)tempArray.count;
        
        if (count == self.maxSection) {
            _currentEndLineOffsetX = _originAvalibleOffsetX;
        }
        
        for (int i = 0; i<count; i++) {
            [self measureAdd];
        }
        
        //预先刷新下布局 不刷后面添加的休止符位置容易出错
        for ( LxMcMeasureModel *model in self.ClefMeasureModelArray) {
            [self lx_reLayoutAllelementsInMeasure:model];
        }
        
        CGFloat  beat = [self noteBeatsForStaffBeatsType:self.staffBeatsType];
        NSMutableArray *restNodeArray = [NSMutableArray arrayWithArray:@[@(MusicNode32nd),@(MusicNode16th),@(MusicNodeEighth),@(MusicNodeQuarter),@(MusicNodeHalf)]];
        //遍历添加休止符
        for (LxMcMeasureModel *model in self.ClefMeasureModelArray) {
            //高音谱休止符添加
            CGFloat upDurtion = [self sectionBeatFullWithMeasureModelNoteViewArray:model.clefUpNoteViewArray beats:beat];
            if (upDurtion != 0 && upDurtion > 0.f && model.clefUpNoteViewArray .count > 0) {
                NSMutableArray *effectArray = [NSMutableArray array];
                NSArray *array = [self calculatRestNodeWithDurtion:upDurtion useNodeArray:effectArray datasourceArray:[restNodeArray mutableCopy]];
                LxMcNoteView *lastNoteV = model.clefUpNoteViewArray.lastObject;
                CGFloat clefUpRight = lastNoteV.right;
                for (NSNumber *typeNumber in array) {
                    CGPoint touchPoint = CGPointMake(clefUpRight , 71.75);
                    LxMcNoteView * noteRestView = [LxMcNoteView lx_defaultNoteViewWithNoteType:[typeNumber integerValue] isRest:YES isDot:NO];
                    noteRestView.isUpClef = YES;
                    noteRestView.x = touchPoint.x;
                    [self addNodeWithNoeView:noteRestView superOffsetPoint:touchPoint];
                    if (![model.clefUpNoteViewArray containsObject:noteRestView]) {
                        debugLog(@"高音休止符%ld,第%ld小节",(long)noteRestView.noteType,(long)model.measureIndex);
                        [model.clefUpNoteViewArray addObject:noteRestView];
                    }
                    clefUpRight = noteRestView.right;
                }
            };
            //低音谱休止符添加
            CGFloat doDurtion = [self sectionBeatFullWithMeasureModelNoteViewArray:model.clefDoNoteViewArray beats:beat];
            if (doDurtion != 0 && doDurtion > 0.f && model.clefDoNoteViewArray.count > 0) {
                NSMutableArray *effectArray = [NSMutableArray array];
                NSArray *array = [self calculatRestNodeWithDurtion:doDurtion useNodeArray:effectArray datasourceArray:[restNodeArray mutableCopy]];
                LxMcNoteView *lastNoteV = model.clefDoNoteViewArray.lastObject;
                CGFloat clefDoRight = lastNoteV.right;
                for (NSNumber *typeNumber in array) {
                    CGPoint touchPoint = CGPointMake(clefDoRight + 5, 243);
                    LxMcNoteView * noteRestView = [LxMcNoteView lx_defaultNoteViewWithNoteType:[typeNumber integerValue] isRest:YES isDot:NO];
                    noteRestView.isUpClef = NO;
                    noteRestView.x = touchPoint.x;
                    noteRestView.y = touchPoint.y;
                    [self addNodeWithNoeView:noteRestView superOffsetPoint:touchPoint];
                    if (![model.clefDoNoteViewArray containsObject:noteRestView]) {
                        debugLog(@"低音休止符%ld,第%ld小节",(long)noteRestView.noteType,(long)model.measureIndex);
                        [model.clefDoNoteViewArray addObject:noteRestView];
                    }
                    clefDoRight = noteRestView.right;
                }
            };
            
            
            //            for (LxMcNoteView *v in model.clefUpNoteViewArray) {
            //                v.backgroundColor = colorArray[index];
            //            }
            //
            //            for (LxMcNoteView *v in model.clefDoNoteViewArray) {
            //                v.backgroundColor = colorArray[index];
            //            }
            //
            
            [self lx_reLayoutAllelementsInMeasure:model];
        }
    }
    //清除加亮小节线
    [self clearHighlightLine];
    
    if ([self.delegate respondsToSelector:@selector(reScrollToTop)]) {
        [self.delegate reScrollToTop];
    }
    
    //刷新糖果效果
    self.candy = self.candy;
    //重置八度音区
    [self resetOctave];
}

- (NSMutableArray *)segmentationSectionWithNoteViews:(NSMutableArray *)noteViews {
    NSMutableArray *tempArray = [NSMutableArray array];
    NSMutableArray *noteTempArray = [NSMutableArray array];
    //拍数
    CGFloat beat = [self noteBeatsForStaffBeatsType:self.staffBeatsType];
    CGFloat noteTypeBeats = 0.f;
    
    //拆分高音谱
    for (int i = 0; i < noteViews.count; i++) {
        
        LxMcNoteView *view = noteViews[i];
        //超过最大小节数
        if (tempArray.count < self.maxSection) {
            [noteTempArray addObject:view];
        }else {
            NSInteger index = [noteViews indexOfObject:view];
            NSArray *array = [noteViews subarrayWithRange:NSMakeRange(index,noteViews.count - index)];
            for (LxMcNoteView *noteView in array) {
                [noteView removeFromSuperview];
                [self clearAdditionLineArrayWithNoteView:noteView];
                [noteViews removeObject:noteView];
            }
            break;
        }
        
        noteTypeBeats = noteTypeBeats + [self noteTypeBeatsForNoteType:view.noteType isDot:view.isDot];
        
        //累计拍数 == 单位小节拍数  添加小节
        if (noteTypeBeats == beat) {
            [tempArray addObject:[noteTempArray mutableCopy]];
            [noteTempArray removeAllObjects];
            noteTypeBeats = 0.f;
        }else if (noteTypeBeats < beat) {
            //累计拍数 < 单位小节拍数
            CGFloat nextNotetype = 0.f;
            if (i < noteViews.count - 1) {
                //获取下个小节
                LxMcNoteView *nextView = noteViews[i+1];
                nextNotetype = [self noteTypeBeatsForNoteType:nextView.noteType isDot:nextView.isDot];
            }
            
            //遍历完 剩余不足一拍直接补足
            if (i == noteViews.count - 1) {
                if (noteTempArray.count > 0) {
                    [tempArray addObject:[noteTempArray mutableCopy]];
                    [noteTempArray removeAllObjects];
                    noteTypeBeats = 0.f;
                }
            }
            
            if (noteTypeBeats + nextNotetype > beat) {
                [tempArray addObject:[noteTempArray mutableCopy]];
                [noteTempArray removeAllObjects];
                noteTypeBeats = 0.f;
            }
        }else {
            [tempArray addObject:[noteTempArray mutableCopy]];
            [noteTempArray removeAllObjects];
            noteTypeBeats = 0.f;
        }
    }
    return tempArray;
}

- (NSMutableArray *)calculatRestNodeWithDurtion:(CGFloat)durtion useNodeArray:(NSMutableArray *)useNodeArray datasourceArray:(NSMutableArray *)datasourceArray {
    CGFloat lastNodeDurtion = 1.0f/[datasourceArray.lastObject intValue];
    CGFloat a = durtion - lastNodeDurtion;
    if (a == 0) {
        [useNodeArray addObject:datasourceArray.lastObject];
        return useNodeArray;
    }else if(a > 0) {
        [useNodeArray addObject:datasourceArray.lastObject];
        return [self calculatRestNodeWithDurtion:a useNodeArray:useNodeArray datasourceArray:datasourceArray];
    }else {
        [datasourceArray removeLastObject];
        return [self calculatRestNodeWithDurtion:durtion useNodeArray:useNodeArray datasourceArray:datasourceArray];
    }
}

- (void)setStaticNotes:(LxMcMeasureModel *)measureModel {
    CGFloat firstX = measureModel.measureBeginOffsetX + [self defaultMeasureBeginWidth];
    CGFloat endX = measureModel.measureEndOffsetX - [self defaultNotesCenterSpace] - [self defaultMeasureEndWidth];
    if (measureModel.clefUpNoteViewArray.count > 0) {
        //高音谱默认音符
        for (LxMcNoteView *noteView in measureModel.clefUpNoteViewArray) {
            CGFloat addOringX = firstX;
            if (noteView.fixedPosition.end) {
                addOringX = endX;
            }
            
            [noteView checkDirection];
            NSInteger index = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(noteView.miditag)] + 1;
            CGFloat offSetY = CGRectGetHeight(self.frame) / 2.f - index * kMcStaffSpace * 0.5 - [noteView judgePointToCenter].y;
            noteView.center = CGPointMake(addOringX, offSetY);
            noteView.rightHeadPosition = CGPointMake(addOringX, offSetY + [noteView judgePointToCenter].y);
            
            /** 上加、下加线处理 **/
            [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:CGPointMake(noteView.centerX + [noteView judgePointToCenter].x, noteView.centerY + [noteView judgePointToCenter].y)];
            [self addSubview:noteView];
        }
    }
    if (measureModel.clefDoNoteViewArray.count > 0) {
        //低音谱默认音符
        for (LxMcNoteView *noteView in measureModel.clefDoNoteViewArray) {
            CGFloat addOringX = firstX;
            if (noteView.fixedPosition.end) {
                addOringX = endX;
            }
            
            [noteView checkDirection];
            NSInteger index = [self.lowMiditagArray indexOfObject:@(noteView.miditag)] + 2;
            CGFloat offSetY = CGRectGetHeight(self.frame) / 2.f + index * kMcStaffSpace * 0.5 - [noteView judgePointToCenter].y;
            noteView.center = CGPointMake(addOringX, offSetY);
            noteView.rightHeadPosition = CGPointMake(addOringX, offSetY + [noteView judgePointToCenter].y);
            
            /** 上加、下加线处理 **/
            [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:CGPointMake(noteView.centerX + [noteView judgePointToCenter].x, noteView.centerY + [noteView judgePointToCenter].y)];
            [self addSubview:noteView];
        }
    }
}

/**
 *@description 重新对小节内高音谱和低音谱音符布局
 **/
- (void)lx_reLayoutAllelementsInMeasure:(LxMcMeasureModel *)measureModel;
{
//    [self reCaculateEventDurationWithNoteViewArray:measureModel.clefUpNoteViewArray];
//    [self reCaculateEventDurationWithNoteViewArray:measureModel.clefDoNoteViewArray];
//
//    NSMutableArray <LxMcNoteView *>*moreNotesArray = [self compareIsMostNoteViewArray:measureModel.clefUpNoteViewArray otherArray:measureModel.clefDoNoteViewArray] ? measureModel.clefUpNoteViewArray : measureModel.clefDoNoteViewArray;
//    NSMutableArray <LxMcNoteView *>*lessNotesArray = [self compareIsMostNoteViewArray:measureModel.clefUpNoteViewArray otherArray:measureModel.clefDoNoteViewArray] ? measureModel.clefDoNoteViewArray : measureModel.clefUpNoteViewArray;
//
//    [self relayoutMoreElementsClefWithNoteViewArray:moreNotesArray withMeasureModel:measureModel];
//    [self relayoutLessElementsClefWithNoteViewArray:lessNotesArray moreElementsArray:moreNotesArray withMeasureModel:measureModel];
    [self reCaculateEventDurationWithNoteViewArray:measureModel.clefUpNoteViewArray];
    [self reCaculateEventDurationWithNoteViewArray:measureModel.clefDoNoteViewArray];
    NSMutableArray <LxMcNoteView *>*moreNotesArray = measureModel.clefUpNoteViewArray.count > measureModel.clefDoNoteViewArray.count ? measureModel.clefUpNoteViewArray : measureModel.clefDoNoteViewArray;
    NSMutableArray <LxMcNoteView *>*lessNotesArray = measureModel.clefUpNoteViewArray.count > measureModel.clefDoNoteViewArray.count ? measureModel.clefDoNoteViewArray : measureModel.clefUpNoteViewArray;
    [self relayoutMoreElementsClefWithNoteViewArray:moreNotesArray withMeasureModel:measureModel];
    [self relayoutLessElementsClefWithNoteViewArray:lessNotesArray moreElementsArray:moreNotesArray withMeasureModel:measureModel];
    

}

/** 是否为最多音符的数组(YES 首个数组最大) **/
- (BOOL)compareIsMostNoteViewArray:(NSMutableArray<LxMcNoteView*> *)array1 otherArray:(NSMutableArray<LxMcNoteView*> *)array2
{
    CGFloat ax1 = [self reCaculateMeasureWidthWithNoteViewArray:array1];
    CGFloat ax2 = [self reCaculateMeasureWidthWithNoteViewArray:array2];
    debugLog(@"ax1 %f   ax2 %f",ax1,ax2);
    return ax1 > ax2;
}

/** 重新布局元素较多的音谱内音符 **/
- (void)relayoutMoreElementsClefWithNoteViewArray:(NSMutableArray *)noteViewArray withMeasureModel:(LxMcMeasureModel *)measureModel
{
    CGFloat oriX = measureModel.measureBeginOffsetX + [self defaultMeasureBeginWidth];
    CGFloat addOringX = oriX;
    /** 得到音谱内最小单位的音符类型 **/
    MusicNodeType minUnitNodeType = measureModel.minNodeType;
    //加载音符 设置成默认四分音符
    if (minUnitNodeType == MusicNodeLoading) {
        minUnitNodeType = MusicNodeQuarter;
    }
    
    for (LxMcNoteView *noteView in noteViewArray) {
        if (noteView.noteType > minUnitNodeType && noteView.noteType != MusicNodeLoading) {
            minUnitNodeType = noteView.noteType;
        }
    }
    LxMcNoteView *endNoteView = nil;
    /** 开始排布 **/
    for ( int i = 0; i < noteViewArray.count; i ++) {
        LxMcNoteView *noteView = [noteViewArray objectAtIndex:i];
        CGFloat offSetX = addOringX;
        if (noteView.fixedPosition.fixed) {
            if (noteView.fixedPosition.end) {
                offSetX = measureModel.measureEndOffsetX - [self defaultNotesCenterSpace] - [self defaultMeasureEndWidth];
                endNoteView = noteView;
            }else {
                offSetX = oriX;
            }
        }
        [UIView animateWithDuration:0.5
                         animations:^{
                             noteView.center = CGPointMake(offSetX - [noteView judgePointToCenter].x, noteView.rightHeadPosition.y - [noteView judgePointToCenter].y);
                             if (noteView.strengthView) {
                                 CGPoint strPoint = noteView.strengthView.center;
                                 strPoint.x = noteView.centerX;
                                 noteView.strengthView.center = strPoint;
                             }
                             if (noteView.noteType == MusicNodeWhole && noteView.isRest && noteView.noteType!= MusicNodeLoading) {
                                 noteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
                             }
                             noteView.rightHeadPosition = CGPointMake(noteView.center.x + [noteView judgePointToCenter].x, noteView.center.y + [noteView judgePointToCenter].y);
                             if (noteView.additionStaffLineCount != 0) {
                                 [self relayoutAdditionLineWithNoteView:noteView];
                             }
                         }];
        CGFloat spaceScale = noteView.durationUnit / (1.f / minUnitNodeType);
        addOringX += spaceScale * [self defaultNotesCenterSpace];
        /** 上加、下加线处理 **/
        [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:CGPointMake(noteView.centerX + [noteView judgePointToCenter].x, noteView.centerY + [noteView judgePointToCenter].y)];
    }
    //* 检测是否需要增加小节宽度 *
    if (addOringX + [self defaultMeasureEndWidth] > measureModel.measureEndOffsetX + 5 ) {
        [self resetMeasureWidthWithOffsetX:addOringX + [self defaultMeasureEndWidth] - measureModel.measureEndOffsetX
                              measureModel:measureModel];
    }else if (addOringX + [self defaultMeasureEndWidth] < measureModel.measureEndOffsetX - 10 && [measureModel checkMeasureDurationUnitWithStaffBeatsType:_staffBeatsType clefType:self.staffClefState])
    {
        [self resetMeasureWidthWithOffsetX:addOringX + [self defaultMeasureEndWidth] - measureModel.measureEndOffsetX
                              measureModel:measureModel];
    }
    if (endNoteView) {//小节结束默认音符位置调整
        [UIView animateWithDuration:0.25 animations:^{
            CGFloat spaceScale = endNoteView.durationUnit / (1.f / minUnitNodeType);
            CGFloat offSetX = measureModel.measureEndOffsetX - [self defaultNotesCenterSpace] * spaceScale - [self defaultMeasureEndWidth];
            endNoteView.centerX = offSetX;
            [self checkAdditionLineAndRelayoutWithNoteView:endNoteView realHeadPoint:CGPointMake(endNoteView.centerX + [endNoteView judgePointToCenter].x, endNoteView.centerY + [endNoteView judgePointToCenter].y)];
        }];
    }
}


///** 重新布局元素较少的音谱内音符 **/
//- (void)relayoutLessElementsClefWithNoteViewArray:(NSMutableArray <LxMcNoteView *>*)noteViewArray
//                                moreElementsArray:(NSMutableArray <LxMcNoteView *>*)moreNoteViewArray
//                                 withMeasureModel:(LxMcMeasureModel *)measureModel
//{
//    CGFloat oriX = measureModel.measureBeginOffsetX + [self defaultMeasureBeginWidth];
//    CGFloat addOringX = oriX;
//    /** 得到音谱内最小单位的音符类型 **/
//    MusicNodeType minUnitNodeType = measureModel.minNodeType;
//    //加载音符 设置成默认四分音符
//    if (minUnitNodeType == MusicNodeLoading) {
//        minUnitNodeType = MusicNodeQuarter;
//    }
//
//    for (LxMcNoteView *noteView in noteViewArray) {
//        if (noteView.noteType > minUnitNodeType && noteView.noteType != MusicNodeLoading) {
//            minUnitNodeType = noteView.noteType;
//        }
//    }
//    LxMcNoteView *endNoteView = nil;
//
//    /** 开始排布 **/
//    for ( int i = 0; i < noteViewArray.count; i ++) {
//        LxMcNoteView *noteView = [noteViewArray objectAtIndex:i];
//        CGFloat offSetX = addOringX;
//        if (noteView.fixedPosition.fixed) {
//            if (noteView.fixedPosition.end) {
//                offSetX = measureModel.measureEndOffsetX - [self defaultNotesCenterSpace] - [self defaultMeasureEndWidth];
//                endNoteView = noteView;
//            }else {
//                offSetX = oriX;
//            }
//        }
//        [UIView animateWithDuration:0.5
//                         animations:^{
//
//                             //                             debugLog(@"较多_第%ld个小节,序列%lu/%lu, 音符%ld,附点%d,center%@",(long)measureModel.measureIndex,(unsigned long)[noteViewArray indexOfObject:noteView]+1,(unsigned long)noteViewArray.count,(long)noteView.noteType,noteView.isRest,NSStringFromCGPoint(noteView.center));
//                             if (noteView.strengthView) {
//                                 CGPoint strPoint = noteView.strengthView.center;
//                                 strPoint.x = noteView.centerX;
//                                 noteView.strengthView.center = strPoint;
//                             }
//                             if (noteView.noteType == MusicNodeWhole && noteView.isRest && noteView.noteType!= MusicNodeLoading) {
//                                 noteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
//                             }
//                             noteView.rightHeadPosition = CGPointMake(noteView.center.x + [noteView judgePointToCenter].x, noteView.center.y + [noteView judgePointToCenter].y);
//                             if (noteView.additionStaffLineCount != 0) {
//                                 [self relayoutAdditionLineWithNoteView:noteView];
//                             }
//                         }];
//        CGFloat spaceScale = noteView.durationUnit / (1.f / minUnitNodeType);
//        addOringX += spaceScale * [self defaultNotesCenterSpace];
//        /** 上加、下加线处理 **/
//        [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:CGPointMake(noteView.centerX + [noteView judgePointToCenter].x, noteView.centerY + [noteView judgePointToCenter].y)];
//    }
//
//}


/** 重新布局元素较少的音谱内音符 **/
- (void)relayoutLessElementsClefWithNoteViewArray:(NSMutableArray <LxMcNoteView *>*)noteViewArray
                                moreElementsArray:(NSMutableArray <LxMcNoteView *>*)moreNoteViewArray
                                 withMeasureModel:(LxMcMeasureModel *)measureModel
{
    CGFloat firstMoreNoteViewCenterX = [moreNoteViewArray firstObject].center.x;
    for (LxMcNoteView *lessNoteView in noteViewArray) {
        if (lessNoteView.fixedPosition.fixed && lessNoteView.fixedPosition.end) {//小节末尾默认音符位置调整
            /** 得到音谱内最小单位的音符类型 **/
            MusicNodeType minUnitNodeType = measureModel.minNodeType;
            //加载音符 设置成默认四分音符
            if (minUnitNodeType == MusicNodeLoading) {
                minUnitNodeType = MusicNodeQuarter;
            }
            for (LxMcNoteView *noteView in noteViewArray) {
                if (noteView.noteType > minUnitNodeType && noteView.noteType!= MusicNodeLoading) {
                    minUnitNodeType = noteView.noteType;
                }
            }
            CGFloat spaceScale = lessNoteView.durationUnit / (1.f / minUnitNodeType);
            CGFloat offSetX = measureModel.measureEndOffsetX - [self defaultNotesCenterSpace] * spaceScale - [self defaultMeasureEndWidth];
            [UIView animateWithDuration:0.25 animations:^{
                lessNoteView.centerX = offSetX;
                if (lessNoteView.strengthView) {
                    CGPoint strPoint = lessNoteView.strengthView.center;
                    strPoint.x = lessNoteView.centerX;
                    lessNoteView.strengthView.center = strPoint;
                }
            }];
        }else {
            for (int i = 0; i < moreNoteViewArray.count ; i ++) {
                LxMcNoteView *moreNoteView = moreNoteViewArray[i];
                if (lessNoteView.eventDuration == moreNoteView.eventDuration) {
                    [UIView animateWithDuration:0.5
                                     animations:^{
                                         lessNoteView.center = CGPointMake(moreNoteView.center.x, lessNoteView.rightHeadPosition.y - [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.strengthView) {
                                             CGPoint strPoint = lessNoteView.strengthView.center;
                                             strPoint.x = lessNoteView.centerX;
                                             lessNoteView.strengthView.center = strPoint;
                                         }
                                         lessNoteView.rightHeadPosition = CGPointMake(lessNoteView.center.x + [lessNoteView judgePointToCenter].x, lessNoteView.center.y + [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.additionStaffLineCount != 0) {
                                             [self relayoutAdditionLineWithNoteView:lessNoteView];
                                         }
                                         if (lessNoteView.noteType == MusicNodeWhole && lessNoteView.isRest && lessNoteView.noteType!= MusicNodeLoading) {
                                             lessNoteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
                                         }
                                     }];
                    break;
                }else if(moreNoteView.eventDuration > lessNoteView.eventDuration)
                {
                    [UIView animateWithDuration:0.5
                                     animations:^{
                                         lessNoteView.center = CGPointMake((moreNoteView.center.x - firstMoreNoteViewCenterX) * (lessNoteView.eventDuration / moreNoteView.eventDuration) + firstMoreNoteViewCenterX, lessNoteView.rightHeadPosition.y - [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.strengthView) {
                                             CGPoint strPoint = lessNoteView.strengthView.center;
                                             strPoint.x = lessNoteView.centerX;
                                             lessNoteView.strengthView.center = strPoint;
                                         }
                                         lessNoteView.rightHeadPosition = CGPointMake(lessNoteView.center.x + [lessNoteView judgePointToCenter].x, lessNoteView.center.y + [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.additionStaffLineCount != 0) {
                                             [self relayoutAdditionLineWithNoteView:lessNoteView];
                                         }
                                         if (lessNoteView.noteType == MusicNodeWhole && lessNoteView.isRest && lessNoteView.noteType!= MusicNodeLoading) {
                                             lessNoteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
                                         }
                                     }];
                    break;
                }else if (lessNoteView.eventDuration > moreNoteView.eventDuration && lessNoteView == noteViewArray.lastObject && moreNoteView == moreNoteViewArray.lastObject){
                    [UIView animateWithDuration:0.5
                                     animations:^{
                                         lessNoteView.center = CGPointMake((moreNoteView.center.x - firstMoreNoteViewCenterX) * (lessNoteView.eventDuration / moreNoteView.eventDuration) + firstMoreNoteViewCenterX, lessNoteView.rightHeadPosition.y - [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.strengthView) {
                                             CGPoint strPoint = lessNoteView.strengthView.center;
                                             strPoint.x = lessNoteView.centerX;
                                             lessNoteView.strengthView.center = strPoint;
                                         }
                                         lessNoteView.rightHeadPosition = CGPointMake(lessNoteView.center.x + [lessNoteView judgePointToCenter].x, lessNoteView.center.y + [lessNoteView judgePointToCenter].y);
                                         if (lessNoteView.additionStaffLineCount != 0) {
                                             [self relayoutAdditionLineWithNoteView:lessNoteView];
                                         }
                                         if (lessNoteView.noteType == MusicNodeWhole && lessNoteView.isRest && lessNoteView.noteType!= MusicNodeLoading) {
                                             lessNoteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
                                         }
                                     }];
                    break;
                    
                }
                
//                else {
//
//                    [UIView animateWithDuration:0.5
//                                     animations:^{
//                                         lessNoteView.center = CGPointMake((moreNoteView.center.x - firstMoreNoteViewCenterX) * (lessNoteView.eventDuration / moreNoteView.eventDuration) + firstMoreNoteViewCenterX, lessNoteView.rightHeadPosition.y - [lessNoteView judgePointToCenter].y);
//                                         if (lessNoteView.strengthView) {
//                                             CGPoint strPoint = lessNoteView.strengthView.center;
//                                             strPoint.x = lessNoteView.centerX;
//                                             lessNoteView.strengthView.center = strPoint;
//                                         }
//                                         lessNoteView.rightHeadPosition = CGPointMake(lessNoteView.center.x + [lessNoteView judgePointToCenter].x, lessNoteView.center.y + [lessNoteView judgePointToCenter].y);
//                                         if (lessNoteView.additionStaffLineCount != 0) {
//                                             [self relayoutAdditionLineWithNoteView:lessNoteView];
//                                         }
//                                         if (lessNoteView.noteType == MusicNodeWhole && lessNoteView.isRest && lessNoteView.noteType!= MusicNodeLoading) {
//                                             lessNoteView.centerX = (measureModel.measureEndOffsetX - measureModel.measureBeginOffsetX) / 2.f + measureModel.measureBeginOffsetX;
//                                         }
//                                     }];
//
//                }
            }
        }
    }
}

/** 对音谱内的所有音符计算event值 **/
- (void)reCaculateEventDurationWithNoteViewArray:(NSMutableArray <LxMcNoteView *>*)noteViewArray
{
    CGFloat eventDuration = 0;
    for (LxMcNoteView *noteView in noteViewArray) {
        noteView.eventDuration = eventDuration;
        eventDuration += [noteView durationUnit];
    }
}

/**
 *@description 获取小节总时长单位
 **/
+ (CGFloat)durationUnitWithBeatasType:(LxMcStaffBeatsType)beatSType
{
    if (beatSType <= LxMcStaffBeats4_4) {
        return beatSType / 4.f;
    }else{
        switch (beatSType) {
            case lxMcStaffBeats3_8:
                return 3.f / 8.f;
                break;
            default:
                return 1;
                break;
        }
    }
}

- (CGFloat)noteTypeBeatsForNoteType:(MusicNodeType)noteType isDot:(BOOL)isDot {
    CGFloat scale = isDot? 1.5f:1.f;
    switch (noteType) {
        case MusicNodeWhole:
        {
            return 1.f * scale;
        }
            break;
        case MusicNodeHalf:
        {
            return 1.f/2.f * scale;
        }
            break;
        case MusicNodeQuarter:
        {
            return 1.f/4.f * scale;
        }
            break;
        case MusicNodeEighth:
        {
            return 1.f/8.f * scale;
        }
            break;
        case MusicNode16th:
        {
            return 1.f/16.f * scale;
        }
            break;
        case MusicNode32nd:
        {
            return 1.f/32.f * scale;
        }
            break;
            
        default:
        {
            return 0.f * scale;
        }
            break;
    }
}

- (CGFloat)sectionBeatFullWithMeasureModelNoteViewArray:(NSMutableArray *)noteViewArray beats:(CGFloat)beats{
    
    if (!noteViewArray || noteViewArray.count == 0) {
        return beats;
    }
    
    CGFloat totalBeats = 0.0f;
    for (LxMcNoteView *view in noteViewArray) {
        CGFloat beatsT = [self noteTypeBeatsForNoteType:view.noteType isDot:view.isDot];
        totalBeats = totalBeats + beatsT;
    }
    return beats - totalBeats;
}

- (CGFloat)noteBeatsForStaffBeatsType:(LxMcStaffBeatsType)staffBeatsType {
    
    switch (staffBeatsType) {
        case LxMcStaffBeatsNone:
            return 0.0f;
            break;
        case LxMcStaffBeats2_4:
            return 2.f/4.f;
            break;
        case LxMcStaffBeats3_4:
            return 3.f/4.f;
            break;
        case LxMcStaffBeats4_4:
            return 4.f/4.f;
            break;
        case lxMcStaffBeats3_8:
            return 3.f/8.f;
            break;
            
        default:
            break;
    }
}


/** 调整对应小节的宽度，并重新将之后的所有小节偏移 **/
- (void)resetMeasureWidthWithOffsetX:(CGFloat)offset_x measureModel:(LxMcMeasureModel *)measureModel
{
    measureModel.measureEndOffsetX += offset_x;
    measureModel.measureLayer.position = CGPointMake(measureModel.measureLayer.position.x + offset_x, measureModel.measureLayer.position.y);
    measureModel.measureIndexLayer.position = CGPointMake(measureModel.measureIndexLayer.position.x + offset_x, measureModel.measureIndexLayer.position.y);
    NSInteger index = [self.ClefMeasureModelArray indexOfObject:measureModel];
    if (index + 1 < self.ClefMeasureModelArray.count) {
        CGFloat lastModelEndOffsetX = measureModel.measureEndOffsetX;
        for (int i = (int)index + 1; i < self.ClefMeasureModelArray.count; i ++) {
            LxMcMeasureModel *nextModel = self.ClefMeasureModelArray[i];
            nextModel.measureBeginOffsetX += offset_x;
            nextModel.measureEndOffsetX += offset_x;
            lastModelEndOffsetX = nextModel.measureEndOffsetX;
            nextModel.measureLayer.position = CGPointMake(nextModel.measureLayer.position.x + offset_x, nextModel.measureLayer.position.y);
            nextModel.measureIndexLayer.position = CGPointMake(nextModel.measureIndexLayer.position.x + offset_x, nextModel.measureIndexLayer.position.y);
            [nextModel lx_allElementsAddOffsetX:offset_x];
            [self relayoutAdditionLineLayerWithMeasureModel:nextModel];
        }
    }
    _currentEndLineOffsetX = [self.ClefMeasureModelArray lastObject].measureEndOffsetX;
    [self stafflineLayerReLayoutWidth];
    [self.delegate reSetScrollViewContent];
    [self resetOctave];
}

- (void)relayoutAdditionLineLayerWithMeasureModel:(LxMcMeasureModel *)measureModel
{
    for (LxMcNoteView *noteView in measureModel.clefUpNoteViewArray) {
        [self relayoutAdditionLineWithNoteView:noteView];
    }
    for (LxMcNoteView *noteView in measureModel.clefDoNoteViewArray) {
        [self relayoutAdditionLineWithNoteView:noteView];
    }
}

#pragma mark - ********************  曲谱音符整体放大缩小相关  ********************
- (void)resetStaffLinesAndMeasureLinesZoomScale:(CGFloat)zoomScale
{
    
}

#pragma mark - Function
- (void)stafflineLayerReLayoutWidth
{
    CGRect staffFrame = self.frame;
    staffFrame.size.width = _currentEndLineOffsetX + kMcStaffMeasureLineRude * 4 + self.measureEndLayer.lineWidth;
    self.frame = staffFrame;
    for (CALayer *lineLayer in self.stafflineLayerArray) {
        CGRect frame = lineLayer.frame;
        frame.size.width = CGRectGetWidth(self.frame);
        lineLayer.frame = frame;
    }
    CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
    self.measureEndLayer.position = CGPointMake(staffFrame.size.width - self.measureEndLayer.lineWidth + 1, centerY);
}

#pragma mark -GetMethod
/** 获取正常小节宽 **/
- (CGFloat)defaultMeasureWidth
{
    NSInteger spaceCount = 0;
    if (_staffBeatsType <= LxMcStaffBeats4_4) {
        //spaceCount = _staffBeatsType;
        //TODO 按最大输出
        spaceCount = 4;
    }else{
        switch (_staffBeatsType) {
            case lxMcStaffBeats3_8:
                spaceCount = 2;
                break;
                
            default:
                spaceCount = 4;
                break;
        }
    }
    return [self defaultMeasureBeginWidth] +
    [self defaultNotesCenterSpace] * spaceCount +
    [self defaultMeasureEndWidth];
}

/** 获取小节末尾宽 **/
- (CGFloat)defaultMeasureEndWidth
{
    return 0;
}
/** 获取小节起始宽 **/
- (CGFloat)defaultMeasureBeginWidth
{
    return 0;
}
/** 音符与音符中心的基本距离 **/
- (CGFloat)defaultNotesCenterSpace
{
    return kMcNoteMinBaseSpace * 2.5;
}
/** 获取小节线 **/
- (CAShapeLayer *)defaultMeasureLineLayerWithOffsetX:(CGFloat)offset_x
{
    CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
    CAShapeLayer *measureLineLayer = [CAShapeLayer layer];
    measureLineLayer.lineCap = kCALineCapSquare;
    UIBezierPath *path = [UIBezierPath bezierPath];
    measureLineLayer.lineWidth = kMcStaffMeasureLineRude;
    [path moveToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, - 7 * kMcStaffSpace + kMcStaffLineRude )];
    if (self.staffClefState != LxMcMcBothClef) {
        [path addLineToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f,- 3 * kMcStaffSpace - kMcStaffLineRude)];
        
        [path moveToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, 3 * kMcStaffSpace + kMcStaffLineRude)];
    }
    [path addLineToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, 7 * kMcStaffSpace - kMcStaffLineRude)];
    measureLineLayer.path = path.CGPath;
    measureLineLayer.strokeColor = kMcStaffLineColor.CGColor;
    measureLineLayer.position = CGPointMake(offset_x - CGRectGetWidth(measureLineLayer.frame)/2.f, centerY);
    return measureLineLayer;
}
/** 获取小节数layer **/
- (CATextLayer *)measureIndexLayerWithIndex:(NSInteger)index
{
    CATextLayer *indexLayer = [CATextLayer lx_getDefaultTextLayerWithFontSize:20];
    indexLayer.anchorPoint = CGPointMake(0.5, 0.5);
    indexLayer.string = [NSString stringWithFormat:@"%ld",index];
    debugLog(@"%@",[NSString stringWithFormat:@"%ld",index]);
    indexLayer.frame = CGRectMake(0, 0, 45, 45);
    indexLayer.foregroundColor = kMcStaffLineColor.CGColor;
    return indexLayer;
}

/** 根据音符计算小节宽度 **/
- (CGFloat)reCaculateMeasureWidthWithNoteViewArray:(NSMutableArray <LxMcNoteView *>*)noteViewArray
{
    CGFloat oriX = [self defaultMeasureBeginWidth];
    CGFloat addOringX = oriX;
    /** 得到音谱内最小单位的音符类型 **/
    MusicNodeType minUnitNodeType = noteViewArray.firstObject.noteType;
    
    for (LxMcNoteView *noteView in noteViewArray) {
        MusicNodeType nodeType = noteView.noteType;
        //加载音符 设置成默认四分音符
        if (noteView.noteType == MusicNodeLoading) {
            nodeType = MusicNodeQuarter;
        }
        if (nodeType > minUnitNodeType && noteView.noteType != MusicNodeLoading) {
            minUnitNodeType = nodeType;
        }
    }
    
    for (LxMcNoteView *noteView in noteViewArray) {
        CGFloat spaceScale = noteView.durationUnit / (1.f / minUnitNodeType);
        addOringX += spaceScale * [self defaultNotesCenterSpace];
        debugLog(@"index%lu_spaceScale___%f____%f",(unsigned long)[noteViewArray indexOfObject:noteView],spaceScale,addOringX);
    }
    
    return addOringX;
}
@end
