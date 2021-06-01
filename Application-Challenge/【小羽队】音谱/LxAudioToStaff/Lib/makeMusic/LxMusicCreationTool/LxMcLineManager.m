//
//  LxMcLineManager.m
//  SmartPiano
//
//  Created by DavinLee on 2019/4/30.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcLineManager.h"
#import "LxMcNoteView.h"
#import "LxMcLineLayer.h"
#import "LxMcNoteView+Layout.h"

@interface LxMcLineManager()
/**   存放高音谱的所有连线音符   NSArray<NSArray< LxMcnoteView*>*>*    **/
@property (strong, nonatomic) NSMutableArray <NSMutableArray<LxMcNoteView *> *>* UpClefNotes;
/**   存放低音谱的所有连线音符   NSArry<NSArray< LxMcNoteView*>*>*    **/
@property (strong, nonatomic) NSMutableArray <NSMutableArray<LxMcNoteView *> *>* downClefNotes;
/**   存放所有高音谱连音线    **/
@property (strong, nonatomic) NSMutableArray <LxMcLineLayer *>* upClefLineLayers;
/**   存放所有低音谱连音线    **/
@property (strong, nonatomic) NSMutableArray <LxMcLineLayer *>* downClefLineLayers;
@end

@implementation LxMcLineManager
#pragma mark - ************************ Call_Function ************************
/**   添加连线音符（起始音符，终止音符）    **/
- (void)lx_addLineMusicNoteView:(LxMcNoteView *)noteView{
    NSMutableArray <LxMcNoteView *>* notes = [self lineNotesWithUpClef:noteView.isUpClef];
    [notes addObject:noteView];
    
    if (notes.count >= 2 && [self.delegate respondsToSelector:@selector(sectionNotesWithFirstLineNote:endLineNote:)]) {
        LxMcNoteView *firstLineNote = notes.firstObject.staffIndex > notes.lastObject.staffIndex ? notes.lastObject : notes.firstObject;
        LxMcNoteView *endLineNote = notes.firstObject.staffIndex > notes.lastObject.staffIndex ? notes.firstObject : notes.lastObject;
        NSMutableArray <LxMcNoteView *>*sectionNotes = [self.delegate sectionNotesWithFirstLineNote:firstLineNote endLineNote:endLineNote];
        NSMutableArray <LxMcNoteView *>*deleteNotes = [NSMutableArray array];
        for (LxMcNoteView *noteView in sectionNotes) {
            if (noteView.isUpClef != noteView.isUpClef) {
                [deleteNotes addObject:noteView];
            }
        }
        [sectionNotes removeObjectsInArray:deleteNotes];
        [self tieLineWithNotes:sectionNotes];
    }
}
/**  添加中间连线音符     **/
- (void)lx_insertLineMusicNoteView:(LxMcNoteView *)noteView{
    
}
/**   删除音符（包括中间音符）    **/
- (void)lx_deleteMusicNoteView:(LxMcNoteView *)noteView{

}
/**   移除所有连线    **/
- (void)lx_removeAllLine{
    
}
/**   取消音符的插入或添加    **/
- (void)lx_cancelNoteOperate{
    NSMutableArray *notes = self.UpClefNotes.lastObject;
    if (notes.count == 1) {
        [notes removeAllObjects];
    }else{
        notes = self.downClefNotes.lastObject;
        if (notes.count == 1) {
            [notes removeAllObjects];
        }
    }
}
#pragma mark - ************************Function************************
/**   进行两个音的连线    **/
- (void)tieLineWithNotes:(NSMutableArray <LxMcNoteView *>*)notes{
    LxMcNoteView *firstNote = notes.firstObject;
    LxMcNoteView *lastNote = notes.lastObject;
    BOOL upLineDirection = NO;
    if ((firstNote.headDirection == LxMcNoteHead_left_down || firstNote.headDirection == LxMcNoteHead_right_down) &&
        (lastNote.headDirection == LxMcNoteHead_left_down || lastNote.headDirection == LxMcNoteHead_right_down)
        ) {
        upLineDirection = YES;
        for (LxMcNoteView *noteView in notes) {
            if (noteView.headDirection == LxMcNoteHead_left_up ||
                noteView.headDirection == LxMcNoteHead_right_up) {
                upLineDirection = NO;
            }
        }
    }
    /**   计算区间音符    **/
    
    
    
    /**    计算起始、终点、控制点1、控制点2   **/
    CGPoint startPoint = [firstNote lx_getLinePointWithLineUpDirection:upLineDirection];
    CGPoint endPoint = [lastNote lx_getLinePointWithLineUpDirection:upLineDirection];
    if (upLineDirection) {
        startPoint.y += 5;
        endPoint.y += 5;
    }else{
        startPoint.y -= 10;
        endPoint.y -= 10;
    }
    
    CGPoint controlPoint1 = CGPointZero;
    CGPoint controlPoint2 = CGPointZero;
    
    CGFloat radius = MIN(65, 15 + notes.count / 20.f * 50.f);
    
    /**   根据容器音符内容增加弧度    **/
    NSInteger maxMidi  = 0;
    NSInteger minMidi = 1000;
    for (LxMcNoteView *noteView in notes) {
        
    }
    
    controlPoint1.x = (endPoint.x - startPoint.x) / 6.f + startPoint.x;
    controlPoint1.y = startPoint.y + (upLineDirection ? radius : - radius);
    controlPoint2.x = (endPoint.x - startPoint.x) / 6.f * 5.f + startPoint.x;
    controlPoint2.y = endPoint.y + (upLineDirection ? radius : - radius);
    
    LxMcLineLayer *lineLayer = [LxMcLineLayer lx_defaultLineLayer];
    lineLayer.position = CGPointMake(startPoint.x - CGRectGetMinX(firstNote.frame), startPoint.y - CGRectGetMinY(firstNote.frame));
    
    UIBezierPath *path = [UIBezierPath bezierPath];
    [path moveToPoint:CGPointZero];
    [path addCurveToPoint:CGPointMake(endPoint.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      endPoint.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)
            controlPoint1:CGPointMake(controlPoint1.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      controlPoint1.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)
            controlPoint2:CGPointMake(controlPoint2.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      controlPoint2.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)];
    /**   计算两个控制点的扩展    **/
    CGFloat tangentLength = 5;
    CGFloat sinA = fabs(endPoint.y - startPoint.y) / hypot(fabs(endPoint.y - startPoint.y), fabs(endPoint.x - startPoint.x));
    
    CGPoint controlpoint1_c = CGPointMake(controlPoint1.x + sinA * tangentLength, controlPoint1.y + sqrt(ldexpf(tangentLength, 2) - ldexpf(sinA *tangentLength, 2)) * (upLineDirection == YES ? 1 : -1) );
    CGPoint controlpoint2_c = CGPointMake(controlPoint2.x + sinA * tangentLength, controlPoint2.y + sqrt(ldexpf(tangentLength, 2) - ldexpf(sinA *tangentLength, 2)) * (upLineDirection == YES ? 1 : -1) );
    [path addCurveToPoint:CGPointMake(startPoint.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      startPoint.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)
            controlPoint1:CGPointMake(controlpoint2_c.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      controlpoint2_c.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)
            controlPoint2:CGPointMake(controlpoint1_c.x - CGRectGetMinX(firstNote.frame) - lineLayer.position.x,
                                      controlpoint1_c.y - CGRectGetMinY(firstNote.frame) - lineLayer.position.y)];
    lineLayer.path = path.CGPath;
    [lineLayer strokeEnd];
    
    [firstNote.layer addSublayer:lineLayer];
}
#pragma mark - ************************GetMethod************************
- (NSMutableArray <NSMutableArray<LxMcNoteView *> *>*)UpClefNotes{
    if (!_UpClefNotes) {
        _UpClefNotes = [[NSMutableArray alloc] init];
    }
    return  _UpClefNotes;
}

- (NSMutableArray <NSMutableArray<LxMcNoteView *> *>*)downClefNotes{
    if (!_downClefNotes) {
        _downClefNotes = [[NSMutableArray alloc] init];
    }
    return _downClefNotes;
}

- (NSMutableArray <LxMcNoteView *>*)lineNotesWithUpClef:(BOOL)isUpClef{
    NSMutableArray <NSMutableArray<LxMcNoteView *> *>* noteGroups = isUpClef == YES ? self.UpClefNotes : self.downClefNotes;
    if (noteGroups.lastObject != nil &&
        noteGroups.lastObject.count < 2)
        return noteGroups.lastObject;
    else{
        NSMutableArray *notes = [[NSMutableArray alloc] init];
        [noteGroups addObject:notes];
        return notes;
    }
}


@end
