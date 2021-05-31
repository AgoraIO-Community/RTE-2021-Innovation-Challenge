//
//  LxMcStaffLineView.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/31.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcStaffLineView.h"
#import "CAShapeLayer+Default.h"
#import "CALayer+Default.h"
#import "UIImage+Default.h"
#import "LxMcNoteView.h"
#import "LxMcStaffLineView+MeasureLayout.h"
#import "LxMcMeasureModel.h"
#import "LxMcNoteView+Bubble.h"
#import "UIImage+RTTint.h"
#import "LxMcLineManager.h"
#import "LxMcStaffLineView+Default.h"
//#import "MidiPlay.h"
#import "LxAudioToStaff-Swift.h"
#define defultScale 1.0

@interface LxMcStaffLineView ()<LxMcLineManagerDelegate>
{
    CGPoint _dragOffsetPoint;/** 拖拽音符起始偏移距离 **/
}
/**
 *@description 当前获取手势交互音符
 **/
@property (weak, nonatomic) LxMcNoteView *dragNoteView;

/**
 *@description 移除强度标记对应的音符
 **/
@property (weak, nonatomic) LxMcNoteView *strengthNoteView;
/**
 *@description 播放音频计时器
 **/
@property (strong, nonatomic) CADisplayLink *link;
/** 当前播放时间轴 **/
@property (assign, nonatomic) NSTimeInterval playPosition;
/**
 *@description 存放所有低音谱小节元素
 **/
/** 播放midi **/
@property (nonatomic, strong) MidiPlay *midiValuePlay;
/**
 高音谱标识
 */
@property (nonatomic, weak) CALayer *clefUpLayer;
/**
 低音谱标识
 */
@property (nonatomic, weak) CALayer *clefDoLayer;
/**
 首小节线
 */
@property (nonatomic, weak) CAShapeLayer *firstLineLayer;
/**
 *@description 同音连线，不同音连线管理
 **/
@property (strong, nonatomic) LxMcLineManager *lineManager;
/**
 *@description 当前连音线点击音符
 **/
@property (weak, nonatomic) LxMcNoteView *currentLineNote;
@end

@implementation LxMcStaffLineView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        [mUserDefaults setFloat:defultScale forKey:spaceScaleKey];
        [mUserDefaults synchronize];
//        self.editMode = LxMcEditModeMove;
        _isPlay = NO;
    }
    return self;
}

- (void)resetMidiPlay{
    self.midiValuePlay = [[MidiPlay alloc] init];
    [self.midiValuePlay loadPatch:self.midiValuePlay.gmMarimba channel:0];
}

#pragma mark - CallFunction
- (void)lx_defaultStaffLineViewWithStaffType:(LxMcStaffManageType)staffType
                                   beatsType:(LxMcStaffBeatsType)beatsType
{
    self.delayPlayTime = 0;
    _staffManageType = staffType;
    _staffBeatsType = beatsType;
    _highlightedStaffLineArray = [NSMutableArray array];
    _additionLineArrayInfo = [[NSMutableDictionary alloc] init];
    _ClefMeasureModelArray = [[NSMutableArray alloc] init];
    /** 五线谱摆放 **/
    [self configStaffLine];
    /** midi设置 **/
    self.midiValuePlay = [[MidiPlay alloc] init];
    [self.midiValuePlay loadPatch:self.midiValuePlay.gmMarimba channel:0];
   
    [self resetBeatsType:beatsType];
    
    self.lineManager = [[LxMcLineManager alloc] init];
    self.lineManager.delegate = self;
//    /** 添加小节 **/
//    for (int i = 0; i < 4; i ++) {
//        [self measureAdd];
//    }
//    [self setUpStaticNotes];
}

- (void)addNodeWithNoeView:(LxMcNoteView *)noteView
          superOffsetPoint:(CGPoint)offsetPoint {
    [self addNodeWithNoeView:noteView superOffsetPoint:offsetPoint miditag:0];
}

- (void)addNodeWithNoeView:(LxMcNoteView *)noteView
          superOffsetPoint:(CGPoint)offsetPoint miditag:(NSInteger)miditag {
    self.ClefMeasureModelArray.firstObject.isMoveEditMode = NO;
    self.dragNoteView = noteView;
    if (self.modelNodeType < 50) {
        noteView.headDirection = LxMcNoteHead_left_down;
    }else{
        [noteView createStaffUI];
    }
    [noteView changeCandyUI:self.candy];

    if (self.editMode != LxMcEditModeMove ) {
        //获取小节最后的位置
        LxMcMeasureModel *model = self.ClefMeasureModelArray.firstObject;
        noteView.x = model.measureBeginOffsetX + noteView.frame.size.width + 20;
    }

    //设置Y坐标 黑键逻辑也在内
    [self setVerticalFrameWithNoteView:noteView miditag:miditag];
    
    if (noteView.alter != 0) {
        miditag = noteView.miditag;
    }
    
//    //非对应谱表 按钮编辑 直接退出
//    if (self.isEditClefUp != noteView.isUpClef && noteView.isRest == NO) {
//        return;
//    }
    
    CGPoint touchPoint = CGPointMake(self.currentInsertIndex * kMcNoteHeadWidth + self.currentInsertIndex * kMcNoteMinBaseSpace + _originAvalibleOffsetX + kMcNoteMinBaseSpace,noteView.rightHeadPosition.y);
                                     noteView.rightHeadPosition = touchPoint;
    noteView.center = touchPoint;
    BOOL locateSuccess = [self checkNoteView:noteView superOffsetPoint:CGPointZero touchPoint:touchPoint miditag:miditag];
    if (locateSuccess) {
        [self lx_dragHighlightLineWithNoeView:self.dragNoteView
                             superOffsetPoint:CGPointZero
                                      miditag:miditag];
        
        self.dragNoteView.isLocatedStaff = YES;
  
        LxMcMeasureModel *model = self.ClefMeasureModelArray.firstObject;

        if (model.measureEndOffsetX  > model.measureBeginOffsetX + [self defaultMeasureScreenWidth]) {
            if ([_delegate respondsToSelector:@selector(scrollTox:)]) {
                [_delegate scrollTox:CGRectGetMinX(self.dragNoteView.frame) - mScreenWidth / 2.f];
            }
        }
    }
    
    [self clearHighlightLine];
    self.ClefMeasureModelArray.firstObject.isMoveEditMode = YES;
}


- (CGPoint)lx_dragHighlightLineWithNoeView:(LxMcNoteView *)noteView
                          superOffsetPoint:(CGPoint)offsetPoint
                                   miditag:(NSInteger)miditag{
    CGPoint realHeadPoint = CGPointMake(noteView.center.x + offsetPoint.x + [noteView judgePointToCenter].x,
                                        noteView.center.y + offsetPoint.y + [noteView judgePointToCenter].y);
    /** 上加、下加线处理 **/
    [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:realHeadPoint];
//    if (miditag != noteView.miditag) {
    
        /** 五线高亮操作 **/
        NSArray *prepareHighlightLineArray = [self midiToHighlightLineLayer][@(miditag)];
        [self clearHighlightLine];
        
        noteView.miditag = miditag;
        LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:realHeadPoint];
        [self setAlter:noteView withMeasureModel:currentModel];
        debugLog((@"\n%@\ntag = %ld  alter = %ld\n%@\n"),@"✅✅✅✅✅✅✅✅✅✅✅",miditag,noteView.alter,@"✅✅✅✅✅✅✅✅✅✅✅");
        /** 播放midi音 **/
        if (!noteView.isRest && noteView.miditag > 0) {
            [self midiplayWithTag:noteView.miditag + noteView.alter];
            [self performSelector:@selector(midiStopWithTag:) withObject:[NSNumber numberWithInteger:noteView.miditag + noteView.alter] afterDelay:0.1];
        }
        /** 低音谱上加二线与低音谱下面相同 **/
        if (prepareHighlightLineArray.count > 0) {
            if (((miditag == 62 || miditag == 64) && !noteView.isUpClef) ||
                ((miditag == 59 || miditag == 57) && noteView.isUpClef) ||
                noteView.isRest) {
                debugLog(@"低音谱上加二线与低音谱下面相同");
            }else
            {
                for (CALayer *lineLayer in prepareHighlightLineArray) {
                    [self reSetLineLayer:lineLayer rude:kMcStaffLineRude * 3];
                }
                [self.highlightedStaffLineArray addObjectsFromArray:prepareHighlightLineArray];
            }
        }
//    }
    /** 上加、下加线处理 **/
//    [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:realHeadPoint];
    return realHeadPoint;
}


- (CGPoint)lx_dragHighlightLineWithNoeView:(LxMcNoteView *)noteView
                          superOffsetPoint:(CGPoint)offsetPoint;
{
    CGPoint realHeadPoint = CGPointMake(noteView.center.x + offsetPoint.x + [noteView judgePointToCenter].x,
                                        noteView.center.y + offsetPoint.y + [noteView judgePointToCenter].y);
    NSInteger miditag = [self miditagWithRealPoint:realHeadPoint
                                      dragNoteView:noteView];
    
    if (miditag != noteView.miditag) {
       
        /** 五线高亮操作 **/
        NSArray *prepareHighlightLineArray = [self midiToHighlightLineLayer][@(miditag)];
        [self clearHighlightLine];
        
        noteView.miditag = miditag;
        LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:realHeadPoint];
        [self setAlter:noteView withMeasureModel:currentModel];
         debugLog((@"\n%@\ntag = %ld  alter = %ld\n%@\n"),@"✅✅✅✅✅✅✅✅✅✅✅",miditag,noteView.alter,@"✅✅✅✅✅✅✅✅✅✅✅");
        /** 播放midi音 **/
        if (!noteView.isRest && noteView.miditag > 0) {
            [self midiplayWithTag:noteView.miditag + noteView.alter];
            [self performSelector:@selector(midiStopWithTag:) withObject:[NSNumber numberWithInteger:noteView.miditag + noteView.alter] afterDelay:0.1];
        }
        /** 低音谱上加二线与低音谱下面相同 **/
        if (prepareHighlightLineArray.count > 0) {
            if (((miditag == 62 || miditag == 64) && !noteView.isUpClef) ||
                ((miditag == 59 || miditag == 57) && noteView.isUpClef) ||
                noteView.isRest) {
                debugLog(@"低音谱上加二线与低音谱下面相同");
            }else
            {
                for (CALayer *lineLayer in prepareHighlightLineArray) {
                    [self reSetLineLayer:lineLayer rude:kMcStaffLineRude * 3];
                }
                [self.highlightedStaffLineArray addObjectsFromArray:prepareHighlightLineArray];
            }
        }
        
        
    }
    /** 上加、下加线处理 **/
    [self checkAdditionLineAndRelayoutWithNoteView:noteView realHeadPoint:realHeadPoint];
    return realHeadPoint;
}
/** 去除所有高亮五线谱 **/
- (void)clearHighlightLine
{
    if (self.highlightedStaffLineArray.count > 0) {
        for (CALayer *lineLayer in self.highlightedStaffLineArray) {
            [self reSetLineLayer:lineLayer rude:kMcStaffLineRude];
        }
        [self.highlightedStaffLineArray removeAllObjects];
    }
}

/**
 *@description 拍号更改
 **/
- (void)resetBeatsType:(LxMcStaffBeatsType)beatsType
{
    if (beatsType == _staffBeatsType) {
        return;
    }
    _staffBeatsType = beatsType;
    NSString *beatsImage = nil;
    switch (_staffBeatsType) {
        case LxMcStaffBeats2_4:
            beatsImage = @"write_page42_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page42_ruding@2x"];
            break;
        case LxMcStaffBeats3_4:
            beatsImage = @"write_page43_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page43_ruding@2x"];
            break;
        case LxMcStaffBeats4_4:
            beatsImage = @"write_page44_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page44_ruding@2x"];
            break;
        case lxMcStaffBeats3_8:
        beatsImage = @"write_page38_rudin2@2x";
            break;
        default:
            break;
    }
    [self.clefUpBeatsLayer setScaleImage:beatsImage];
    [self.clefDoBeatsLayer setScaleImage:beatsImage];
    [self resetDefaultStaffLineView];
    
}

- (void)stopPlay
{
    if (_link) {
        [_link invalidate];
        _isPlay = NO;
        _link = nil;
    }
}

#pragma mark - playNotesAbout
/**
 *@description 开始播放
 **/
- (void)playNoteViewsWithQueueNoteS {
    [self lx_getPlayQueueNoteArray];
    if ([_delegate respondsToSelector:@selector(reScrollToTop)]) {
        [_delegate reScrollToTop];
    }
    _link = [CADisplayLink displayLinkWithTarget:self selector:@selector(handleDisplay:)];
    _link.frameInterval = 1;
    [_link addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
    self.playPosition = 0;
    _isPlay = YES;
}
- (void)handleDisplay:(CADisplayLink *)link
{
    self.playPosition += link.duration;
    NSMutableArray *playNotes = [NSMutableArray array];

    if (self.queuePlayNotes.count > 0) {
        for (LxMcNoteView *noterView in self.queuePlayNotes) {
            if (noterView.playEventPosition + self.delayPlayTime <= self.playPosition) {
                [playNotes addObject:noterView];
            }else
            {
                break;
            }
        }
        CGFloat scrollWidth = [_delegate scrollView].width;
        if (playNotes.count) {
//            CGFloat positionX = ( [self.queuePlayNotes firstObject].center.x - scrollWidth / 2.f) * (self.playPosition / [self.queuePlayNotes firstObject].playEventPosition);
            CGFloat offset_X = MAX(0, MIN(CGRectGetWidth(self.frame) - scrollWidth ,[self.queuePlayNotes firstObject].center.x - scrollWidth / 2.f));
//            debugLog(@"当前音符%p,x =- %f 当前时间%f",[self.queuePlayNotes firstObject],[self.queuePlayNotes firstObject].center.x,positionX);
            [UIView animateWithDuration:[self.queuePlayNotes firstObject].playEventPosition - self.playPosition animations:^{
                [self.delegate scrollTox:offset_X];
                [self.delegate reSetScrollViewContent];
            }];
        }
        for (LxMcNoteView *noteView in playNotes) {
            [self midiplayWithNoteView:noteView];
            [self.queuePlayNotes removeObject:noteView];
        }
    }
    else
    {
        if ([_delegate respondsToSelector:@selector(playEnd)]) {
            [_delegate playEnd];
            _isPlay = NO;
        }
        [_link invalidate];
        _link = nil;
    }
}
/** 播放或关闭midi **/
- (void)midiplayWithNoteView:(LxMcNoteView *)noteView
{
    [noteView showPlayState:YES candy:self.candy];
    [self performSelector:@selector(midiStopWithNoteView:)
               withObject:noteView
               afterDelay:[noteView durationUnit] * kMcNoteWholeDuration - 0.05];
    if (noteView.isRest) {
        return;
    }
    [self.midiValuePlay mstart:noteView.miditag + noteView.alter velocity:80 channel:0];
 
    debugLog(@"播放midi%ld 力度:%ld",noteView.miditag + noteView.alter,noteView.strength == 0 ? 80 :noteView.strength);
}

- (void)midiStopWithNoteView:(LxMcNoteView *)noteView
{
    [noteView showPlayState:NO candy:self.candy];
    if (noteView.isRest) {
        return;
    }
    [self.midiValuePlay mstop:noteView.miditag + noteView.alter channel:0];
}

#pragma mark - 附加线部分
/** 检查上加、下加线并重新布局 **/
- (void)checkAdditionLineAndRelayoutWithNoteView:(LxMcNoteView *)noteView realHeadPoint:(CGPoint )realHeadPoint
{
    if (noteView.isRest) {
        return;
    }
    NSInteger checkAddLineCount = [noteView caculateRightAdditionStaffLineCount];
    NSMutableArray *additionLineArray  = [self additionLineArrayWithNoteView:noteView];
    CALayer *clefCenterLineLayer = noteView.isUpClef ? self.stafflineLayerArray[2] : self.stafflineLayerArray[7];//高低音谱的中间线
    if (checkAddLineCount != noteView.additionStaffLineCount) {
        debugLog(@"附加%ld",checkAddLineCount);
        for (CALayer *layer in additionLineArray) {
            [layer removeFromSuperlayer];
            [self.stafflineLayerArray removeObject:layer];
        }
        [additionLineArray removeAllObjects];
        for (int i = 0 ; i < fabs(checkAddLineCount); i ++) {
            CALayer *additionLayer = [self defaultStaffLineLayerWithWidth:1.5 * kMcNoteHeadWidth];
            additionLayer.anchorPoint = CGPointMake(0.5, 0.5);
            [CATransaction begin];
            [CATransaction setAnimationDuration:0];
            if (checkAddLineCount > 0) {
                additionLayer.position = CGPointMake(realHeadPoint.x + noteView.judgePointToCenter.x,CGRectGetMidY(clefCenterLineLayer.frame) + i * kMcStaffSpace + 3 * kMcStaffSpace);
            }else
            {
                additionLayer.position = CGPointMake(realHeadPoint.x + noteView.judgePointToCenter.x,CGRectGetMidY(clefCenterLineLayer.frame) - i * kMcStaffSpace - 3 * kMcStaffSpace);
            }
            [CATransaction commit];
            [self.layer addSublayer:additionLayer];
            [additionLineArray addObject:additionLayer];
            [self.stafflinePlusLayerArray addObject:additionLayer];
        }
        noteView.additionStaffLineCount = checkAddLineCount;
    }else
    {
        // debugLog(@"数组数量%ld",additionLineArray.count);
        for (CALayer *layer in additionLineArray) {
            layer.anchorPoint = CGPointMake(0.5, 0.5);
            [CATransaction begin];
            [CATransaction setAnimationDuration:0];
            if (checkAddLineCount > 0) {
                layer.position = CGPointMake(realHeadPoint.x + noteView.judgePointToCenter.x,layer.position.y);
            }else
            {
                layer.position = CGPointMake(realHeadPoint.x + noteView.judgePointToCenter.x,layer.position.y);
            }
            [CATransaction commit];
        }
    }
}

/** 获取对应音符的附加线数组 **/
- (NSMutableArray *)additionLineArrayWithNoteView:(LxMcNoteView *)noteView
{
    NSMutableArray *additionLineArray = self.additionLineArrayInfo[[NSString stringWithFormat:@"%p",noteView]];
    if (additionLineArray == nil) {
        additionLineArray = [[NSMutableArray alloc] init];
        [self.additionLineArrayInfo setObject:additionLineArray forKey:[NSString stringWithFormat:@"%p",noteView]];
    }
    return additionLineArray;
}
/** 清除附加线 **/
- (void)clearAdditionLineArrayWithNoteView:(LxMcNoteView *)noteView
{
    NSMutableArray *additionLineArray = self.additionLineArrayInfo[[NSString stringWithFormat:@"%p",noteView]];
    if (additionLineArray.count > 0) {
        for (CALayer *additionLayer in additionLineArray) {
            [additionLayer removeFromSuperlayer];
        }
        [additionLineArray removeAllObjects];
        [self.additionLineArrayInfo removeObjectForKey:[NSString stringWithFormat:@"%p",noteView]];
    }
    
}
/** 重新布局音符的附加线 **/
- (void)relayoutAdditionLineWithNoteView:(LxMcNoteView *)noteView
{
    NSMutableArray *additionLineArray = self.additionLineArrayInfo[[NSString stringWithFormat:@"%p",noteView]];
    for (CALayer *layer in additionLineArray) {
        [CATransaction begin];
        [CATransaction setAnimationDuration:0];
        layer.position = CGPointMake(noteView.center.x + [noteView judgePointToCenter].x, layer.position.y);
        [CATransaction commit];
    }
}
#pragma mark - Function
/** 五线谱摆放 **/
- (void)configStaffLine
{
    _originAvalibleOffsetX = 0;
    
    _originAvalibleOffsetX += 18;
    /** 五线部分 **/
    _stafflineLayerArray = [[NSMutableArray alloc] initWithCapacity:10];
    CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
    CGFloat offsetY = centerY - 7 * kMcStaffSpace;
    for (int i = 0; i < 5; i ++) {
        CALayer *lineLayer = [self defaultStaffLineLayerWithWidth:CGRectGetWidth(self.frame)];
        
        [_stafflineLayerArray addObject:lineLayer];
        lineLayer.frame = CGRectMake(_originAvalibleOffsetX, offsetY - CGRectGetHeight(lineLayer.frame) / 2.f, CGRectGetWidth(lineLayer.frame), CGRectGetHeight(lineLayer.frame));
        [self.layer addSublayer:lineLayer];
        offsetY += kMcStaffSpace;
    }
    
    offsetY = centerY + 3 * kMcStaffSpace;
    for (int i = 0; i < 5 ; i ++) {
        CALayer *lineLayer = [self defaultStaffLineLayerWithWidth:CGRectGetWidth(self.frame)];
        [_stafflineLayerArray addObject:lineLayer];
        lineLayer.frame = CGRectMake(_originAvalibleOffsetX, offsetY - CGRectGetHeight(lineLayer.frame) / 2.f, CGRectGetWidth(lineLayer.frame),CGRectGetHeight(lineLayer.frame));
        [self.layer addSublayer:lineLayer];
        offsetY += kMcStaffSpace;
    }
    
    switch (self.staffClefState) {
        case LxMcMcBothClef:
            
            break;
        default:
            break;
    }
    /** 首小节线 **/
    CAShapeLayer *zeroUpMeasureLineLayer = [CAShapeLayer lx_defaultLineLayerWithDirection:LxShapeLayerDirectionVer width:kMcStaffMeasureLineRude height:kMcStaffSpace * 14.f - kMcStaffLineRude * 2 /** 减去一个线粗，因为layer绘图，前后各多了一半的线粗 **/];
    zeroUpMeasureLineLayer.strokeColor = kMcStaffLineColor;
    zeroUpMeasureLineLayer.position = CGPointMake(_originAvalibleOffsetX,centerY - 7 * kMcStaffSpace + kMcStaffLineRude);
    
    [self.layer addSublayer:zeroUpMeasureLineLayer];
    self.firstLineLayer = zeroUpMeasureLineLayer;
    
    /** 高音谱 **/
    [self originOffsetXAdd:kMcNoteMinBaseSpace];
    /** Lx description   添加大谱表  **/
    UIImage *grandStaffImage = [UIImage imageNamed:@"LxMcGrandStaff@2x"];
    CALayer *grandStaffLayer = [CALayer layer];
    grandStaffLayer.contents = (id)grandStaffImage.CGImage;
    grandStaffLayer.frame = CGRectMake(0, self.firstLineLayer.frame.origin.y, grandStaffImage.size.width / grandStaffImage.size.height * kMcStaffSpace * 14, kMcStaffSpace * 14);
    [self.layer addSublayer:grandStaffLayer];
    self.grandStaffLayer = grandStaffLayer;
    
    
    UIImage *clefUpImage = [UIImage imageNamed:@"write_clef_up_rudin@2x"];
    CALayer *clefUp = [CALayer layer];
    [clefUp lx_setImage:clefUpImage];
    clefUp.anchorPoint = CGPointMake(0.5, 0.5);
    clefUp.frame = CGRectMake(0, 0, kMcStaffSpace * 3, clefUpImage.size.height / clefUpImage.size.width * kMcStaffSpace * 3);
    clefUp.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(clefUp.frame)/2.f, centerY - 4 * kMcStaffSpace);
    [self.layer addSublayer:clefUp];
    self.clefUpLayer = clefUp;
    /** 低音谱 **/
    
    UIImage *clefDoImage = [UIImage imageNamed:@"write_clef_do_rudin@2x"];
    CALayer *clefDo = [CALayer layer];
    [clefDo lx_setImage:clefDoImage];
    clefDo.anchorPoint = CGPointMake(0.5,0.5);
    clefDo.frame = CGRectMake(0, 0, clefDoImage.size.width / clefDoImage.size.height * kMcStaffSpace * 9, kMcStaffSpace * 9);
    clefDo.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(clefDo.frame)/2.f, centerY + 5 * kMcStaffSpace);
    [self.layer addSublayer:clefDo];
    self.clefDoLayer = clefDo;
    [self originOffsetXAdd:CGRectGetWidth(clefUp.frame)];
    
    [self majorImageName];
    
    /** 高音谱拍号设置 **/
    [self originOffsetXAdd:kMcNoteMinBaseSpace];
    NSString *beatsImage ;
    switch (_staffBeatsType) {
        case LxMcStaffBeats2_4:
            beatsImage = @"write_page42_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page42_ruding@2x"];
            break;
        case LxMcStaffBeats3_4:
            beatsImage = @"write_page43_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page43_ruding@2x"];
            break;
        case LxMcStaffBeats4_4:
            beatsImage = @"write_page44_ruding@2x";//[UIImage lx_imageFromBundleWithName:@"write_page44_ruding@2x"];
            break;
        case lxMcStaffBeats3_8:
            beatsImage = @"write_page38_rudin2@2x";
            break;
        default:
            break;
    }
    UIImage *bImage = [UIImage imageNamed:beatsImage];
    CALayer *beatsUpLayer = [CALayer layer];
    [beatsUpLayer lx_setImage:bImage];
    beatsUpLayer.anchorPoint = CGPointMake(0.5,0.5);
    beatsUpLayer.frame = CGRectMake(0, 0,bImage.size.width / bImage.size.height * kMcStaffSpace * 4, kMcStaffSpace * 4);
    beatsUpLayer.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(beatsUpLayer.frame)/2.f, centerY - 5 * kMcStaffSpace);
    [self.layer addSublayer:beatsUpLayer];
    self.clefUpBeatsLayer = beatsUpLayer;
    /** 低音谱拍号设置 **/
    CALayer *beatsDoLayer = [CALayer layer];
    [beatsDoLayer lx_setImage:bImage];
    beatsDoLayer.anchorPoint = CGPointMake(0.5, 0.5);
    beatsDoLayer.frame = CGRectMake(0, 0,bImage.size.width / bImage.size.height * kMcStaffSpace * 4, kMcStaffSpace * 4);
    beatsDoLayer.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(beatsDoLayer.frame)/2.f, centerY + 5 * kMcStaffSpace);
    [self.layer addSublayer:beatsDoLayer];
    [self originOffsetXAdd:CGRectGetWidth(beatsDoLayer.frame)];
    [self originOffsetXAdd:2 * kMcNoteMinBaseSpace];
    self.clefDoBeatsLayer = beatsDoLayer;
    
    UIPanGestureRecognizer *pan  = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(handlepanGesture:)];
    [self addGestureRecognizer:pan];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapGesture:)];
    tap.numberOfTapsRequired = 1;
    tap.numberOfTouchesRequired = 1;
    [self addGestureRecognizer:tap];
    
    self.modelNodeType = MusicNodeQuarter;
    
    //当前初始高低音谱编辑
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
}

- (void)majorImageName {
    
    NSString *majorImage = nil;
    CGFloat highSetOffY = 0, lowSetOffY = 0;
    CGFloat centerY = CGRectGetHeight(self.frame) * 0.5;
    switch (_staffManageType) {
        case LxMcStaffManageNormal:
            break;
        case LxMcStaffManageFmajor:
            [self originOffsetXAdd:20];
            majorImage = @"write_FMajorTag_ruding@2x";
            highSetOffY = centerY - 5.5 * kMcStaffSpace;
            lowSetOffY = centerY + 5.5 * kMcStaffSpace;
            break;
        case LxMcStaffManageGmajor:
        case LxMcStaffManageEchordMajor:{
            [self originOffsetXAdd:20];
            majorImage = @"write_GMajorTag_ruding@2x";
            highSetOffY = centerY - 7 * kMcStaffSpace;
            lowSetOffY = centerY + 4 * kMcStaffSpace;
        }
            break;
        case LxMcStaffManageDmajor:
            majorImage = @"write_Dmajor@2x";
            highSetOffY = centerY - 6 * kMcStaffSpace - 10;
            lowSetOffY = centerY + 4 * kMcStaffSpace;
            break;
        case LxMcStaffManageAchordMajor:
            break;
            default:
            break;
    }
    if (majorImage) {
        //高音谱调性标识
        CALayer *majorHigLayer = [CALayer layer];
        [majorHigLayer setScaleImage:majorImage];
        majorHigLayer.anchorPoint = CGPointMake(0.5,0.5);
        majorHigLayer.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(majorHigLayer.frame)/2.f, highSetOffY);
        [self.layer addSublayer:majorHigLayer];
        
        //低音谱调性标识
        CALayer *majorLowLayer = [CALayer layer];
        [majorLowLayer setScaleImage:majorImage];
        majorLowLayer.anchorPoint = CGPointMake(0.5,0.5);
        majorLowLayer.position = CGPointMake(_originAvalibleOffsetX + CGRectGetWidth(majorLowLayer.frame)/2.f, lowSetOffY);
        [self.layer addSublayer:majorLowLayer];
        [self originOffsetXAdd:CGRectGetWidth(majorLowLayer.frame)];
    }
}


- (void)setVerticalFrameWithNoteView:(LxMcNoteView *)noteView
                             miditag:(NSInteger)miditag {
    
    //高低音特殊按键
//    if ([[self lhmiditagArray] containsObject:@(miditag)]) {
//        if (noteView.isUpClef) {
//            noteView.isUpClef = YES;
//            NSInteger i = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(miditag)] - 1;
//            CGFloat lineY = CGRectGetHeight(self.frame)/2.f - kMcStaffSpace - i * kMcStaffSpace/2;
//            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
//            noteView.isHeadOnLine = i%2 ? YES:NO;
//            return;
//        }else {
//            noteView.isUpClef = NO;
//            NSInteger i = [self.lowMiditagArray indexOfObject:@(miditag)];
//            CGFloat lineY = CGRectGetHeight(self.frame)/2.f + kMcStaffSpace + i * kMcStaffSpace/2;
//            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
//            noteView.isHeadOnLine = i%2 ? YES:NO;
//            return;
//        }
//    }
    
    //当前曲谱类型 含高音谱,低音谱
    if (_staffClefState == LxMcMcBothClef) {
        //高音谱
        if ([self.highMiditagArray containsObject:@(miditag)] && noteView.isUpClef) {
            noteView.isUpClef = YES;
            NSInteger i = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(miditag)] - 1;
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f - kMcStaffSpace - i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
        }
        
//        //高音黑键处理
//        NSString *obj = [NSString stringWithFormat:@"%ld",miditag];
//        if ([[[self highBlackkeyMiditagDict] allKeys] containsObject:obj]) {
//            noteView.isUpClef = YES;
//            NSInteger number = [[[self highBlackkeyMiditagDict] valueForKey:obj] integerValue];
//            NSInteger i = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(number)] - 1;
//            CGFloat lineY = CGRectGetHeight(self.frame)/2.f - kMcStaffSpace - i * kMcStaffSpace/2;
//            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
//            noteView.isHeadOnLine = i%2 ? YES:NO;
//            //黑键miditag处理 保持输出输入一致
//            noteView.miditag = number;
//            noteView.alter = miditag - number;
//        }
        
        //低音谱
        if ([self.lowMiditagArray containsObject:@(miditag)] && !noteView.isUpClef) {
            noteView.isUpClef = NO;
            NSInteger i = [self.lowMiditagArray indexOfObject:@(miditag)];
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f + kMcStaffSpace + i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
        }
        
//        //低音黑键处理
//        NSString *obj1 = [NSString stringWithFormat:@"%ld",miditag];
//        if ([[[self lowBlackkeyMiditagDict] allKeys] containsObject:obj1]) {
//            noteView.isUpClef = NO;
//            NSInteger number = [[[self lowBlackkeyMiditagDict] valueForKey:obj1] integerValue];
//            NSInteger i = [self.lowMiditagArray indexOfObject:@(number)] ;
//            CGFloat lineY = CGRectGetHeight(self.frame)/2.f + kMcStaffSpace + i * kMcStaffSpace/2;
//            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
//            noteView.isHeadOnLine = i%2 ? YES:NO;
//
//            //黑键miditag处理 保持输出输入一致
//            noteView.miditag = number;
//            noteView.alter = miditag - number;
//        }
    }
    
    //当前曲谱类型 含高音谱
    if (_staffClefState == LxMcMcHighClef) {
        //高音谱
        if ([self.highMiditagArray containsObject:@(miditag)]) {
            noteView.isUpClef = YES;
            NSInteger i = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(miditag)] - 1;
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f - kMcStaffSpace - i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
        }

        //高音黑键处理
        NSString *obj = [NSString stringWithFormat:@"%ld",miditag];
        if ([[[self highBlackkeyMiditagDict] allKeys] containsObject:obj]) {
            noteView.isUpClef = YES;
            NSInteger number = [[[self highBlackkeyMiditagDict] valueForKey:obj] integerValue];
            NSInteger i = self.highMiditagArray.count - [self.highMiditagArray indexOfObject:@(number)] - 1;
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f - kMcStaffSpace - i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
            
            //黑键miditag处理 保持输出输入一致
            noteView.miditag = number;
            noteView.alter = miditag - number;
        }
    }
    
    //当前曲谱类型 含低音谱
    if (_staffClefState == LxMcMcLowClef) {
        //低音谱
        if ([self.lowMiditagArray containsObject:@(miditag)]) {
            noteView.isUpClef = NO;
            NSInteger i = [self.lowMiditagArray indexOfObject:@(miditag)] ;
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f + kMcStaffSpace + i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
        }
        //低音黑键处理
        NSString *obj = [NSString stringWithFormat:@"%ld",miditag];
        if ([[[self lowBlackkeyMiditagDict] allKeys] containsObject:obj]) {
            noteView.isUpClef = NO;
            NSInteger number = [[[self lowBlackkeyMiditagDict] valueForKey:obj] integerValue];
            NSInteger i = [self.lowMiditagArray indexOfObject:@(number)] ;
            CGFloat lineY = CGRectGetHeight(self.frame)/2.f + kMcStaffSpace + i * kMcStaffSpace/2;
            noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, lineY);
            noteView.isHeadOnLine = i%2 ? YES:NO;
            
            //黑键miditag处理 保持输出输入一致
            noteView.miditag = number;
            noteView.alter = miditag - number;
        }
    }
    /** 如果是休止符，修正y坐标 **/
    if (noteView.isRest) {
        CALayer *clefCenterLayer = noteView.isUpClef ? self.stafflineLayerArray[2] : self.stafflineLayerArray[7];
        noteView.rightHeadPosition = CGPointMake(noteView.frame.origin.x, CGRectGetMidY(clefCenterLayer.frame));
    }

}


/** 根据符头相对位置获取tag值 **/
- (NSInteger)miditagWithRealPoint:(CGPoint)point
                     dragNoteView:(LxMcNoteView *)noteView
{
    
    NSInteger midiTag = 0;
    CGFloat limitSpaceUnit = kMcStaffSpace / 3.f;
    NSInteger limitLineCount = 9;/** 高音谱上加二线和低音谱下加二线之间 **/
    if (point.x < _originAvalibleOffsetX || /** 横向小于起始位置 **/
        point.y > CGRectGetHeight(self.frame) / 2.f + limitLineCount * kMcStaffSpace || /** 超过低音谱下加二线 **/
        point.y < CGRectGetHeight(self.frame) / 2.f - limitLineCount * kMcStaffSpace || /** 低于高音谱上加二线 **/
        (point.y > CGRectGetHeight(self.frame) / 2.f - 0.5 * kMcStaffSpace && point.y < CGRectGetMidY(self.frame) + 0.5 * kMcStaffSpace)/** 位于中间线附近 **/) {
        noteView.rightHeadPosition = CGPointZero;
    }else if (self.staffClefState == LxMcMcHighClef && point.y > CGRectGetHeight(self.frame) / 2.f - 0.5 * kMcStaffSpace)
    {
        noteView.rightHeadPosition = CGPointZero;
    }else if (self.staffClefState == LxMcMcLowClef && point.y < CGRectGetMidY(self.frame) + 0.5 * kMcStaffSpace)
    {
        noteView.rightHeadPosition = CGPointZero;
    }
    else
    {
       
        CGFloat limitLineOffsetUnit = kMcStaffSpace / 3.f;/** 针对线的判断上下比例 **/
        debugLog(@"%f",CGRectGetHeight(self.frame)/2.f);
        if (point.y > CGRectGetHeight(self.frame)/2.f) {//属于低音谱
            noteView.isUpClef = NO;
            NSMutableArray *miditagArray = [NSMutableArray arrayWithArray:self.lowMiditagArray];
            for (int i = (int)limitLineCount; i > 0; i --) {
                CGFloat lineY = CGRectGetHeight(self.frame)/2.f + i * kMcStaffSpace;
                if (point.y < lineY + kMcStaffSpace / 2.f &&
                    point.y > lineY - kMcStaffSpace / 2.f) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY);
                    noteView.isHeadOnLine = YES;
                    break;
                }
                if (i == 1) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY);
                    noteView.isHeadOnLine = YES;
                    break;
                }
                [miditagArray removeLastObject];
                lineY = CGRectGetHeight(self.frame)/2.f + (i - 1) * kMcStaffSpace;
                if (point.y >= lineY + limitLineOffsetUnit) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY + kMcStaffSpace / 2.f);
                    noteView.isHeadOnLine = NO;
                    break;
                }
                [miditagArray removeLastObject];
            }
        }else
        {
            noteView.isUpClef = YES;
            NSMutableArray *miditagArray = [NSMutableArray arrayWithArray:self.highMiditagArray];
            for (int i = 1; i <= limitLineCount; i ++) {
                CGFloat lineY = CGRectGetHeight(self.frame)/2.f - i * kMcStaffSpace ;
                if (point.y < lineY + kMcStaffSpace / 2.f &&
                    point.y > lineY - kMcStaffSpace / 2.f) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY);
                    noteView.isHeadOnLine = YES;
                    break;
                }
                if (i == 9) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY);
                    noteView.isHeadOnLine = YES;
                    break;
                }
                [miditagArray removeLastObject];
                lineY = CGRectGetHeight(self.frame)/2.f - (i + 1) * kMcStaffSpace;
                if (point.y >= lineY + limitLineOffsetUnit ) {
                    midiTag = [[miditagArray lastObject] integerValue];
                    noteView.rightHeadPosition = CGPointMake(point.x, lineY + kMcStaffSpace / 2.f);
                    noteView.isHeadOnLine = NO;
                    break;
                }
                [miditagArray removeLastObject];
            }
        }
    }
    /** 如果是休止符，修正y坐标 **/
    if (noteView.isRest) {
        CALayer *clefCenterLayer = noteView.isUpClef ? self.stafflineLayerArray[2] : self.stafflineLayerArray[7];
        noteView.rightHeadPosition = CGPointMake(point.x, CGRectGetMidY(clefCenterLayer.frame));
    }
    debugLog(@"选定midi = %ld",midiTag);
    return midiTag;
}
/** 设置五线谱元素粗细 **/
- (void)reSetLineLayer:(CALayer *)lineLayer rude:(CGFloat)rude
{
    CGRect frame = lineLayer.frame;
    frame.origin.y = CGRectGetMidY(frame) - rude / 2.f;
    frame.size.height = rude;
    lineLayer.frame = frame;
}
/** 播放或关闭midi **/
- (void)midiplayWithTag:(NSInteger)miditag
{
    debugLog(@"播放miditag = %ld",miditag);
//    [self.midiValuePlay playMidiValue:miditag velocity:80 channel:0];
    [self.midiValuePlay mstart:miditag velocity:80 channel:0];
}

- (void)midiStopWithTag:(NSNumber *)miditag
{
    [self.midiValuePlay mstop:miditag.integerValue channel:0];
}

- (void)resetOctave {
    self.highOctaveView.width = self.width - self.highOctaveView.x - [self defaultMeasureEndWidth];
    self.lowOctaveView.width = self.width - self.lowOctaveView.x - [self defaultMeasureEndWidth];
}

/**
 设置当前音符需要升或降音的值
 */
- (void)setAlter:(LxMcNoteView *)noteView withMeasureModel:(LxMcMeasureModel *)measuremodel{
    
    if (noteView.isRest) {
        return;
    }
    
    //TODO 黑键
    switch (_staffManageType) {
        case LxMcStaffManageNormal:{
        }
            break;
        case LxMcStaffManageFmajor:{
            if ([[self FMajorMiditag] containsObject:@(noteView.miditag)]) {
                noteView.alter = -1;
            }else{
                noteView.alter = 0;
            }
        }
            break;
        case LxMcStaffManageGmajor:{
            if ([[self GMajorMiditag] containsObject:@(noteView.miditag)]) {
                noteView.alter = 1;
//                noteView.tempMajorType = LxNoteTempMajorShape;
            }else{
                noteView.alter = 0;
//                noteView.tempMajorType = LxNoteTempMajorNormal;
            }
        }
            break;
            case LxMcStaffManageEchordMajor:
        {
            if ([[self GMajorMiditag] containsObject:@(noteView.miditag)]) {
                noteView.alter = 1;
            }else{
                /** 同一小节只能第一个音符出现临时升号，其他不用显示，但实际升号**/
                if (noteView.isUpClef) {
                    for (LxMcNoteView *tempView in measuremodel.clefUpNoteViewArray) {
                        if ([[self eChordMajorMiditag] containsObject:@(tempView.miditag)] && !tempView.isRest) {
                            if ([measuremodel lx_firstAppeargNoteView:tempView]) {
                                tempView.alter = 1;
                            }
                            tempView.tempMajorType = LxNoteTempMajorShape;
                        }else{
                            tempView.tempMajorType = LxNoteTempMajorNormal;
                        }
                    }
                }else{
                    for (LxMcNoteView *tempView in measuremodel.clefDoNoteViewArray) {
                        if ([[self eChordMajorMiditag] containsObject:@(tempView.miditag)] && !tempView.isRest) {
                            if ([measuremodel lx_firstAppeargNoteView:tempView]) {
                                tempView.alter = 1;
                            }
                            tempView.tempMajorType = LxNoteTempMajorShape;
                        }else{
                            tempView.tempMajorType = LxNoteTempMajorNormal;
                        }
                    }
                }
            }
        }
            break;
        case LxMcStaffManageDmajor:{
            if ([[self DMajorMiditag] containsObject:@(noteView.miditag)]) {
                noteView.alter = 1;
            }else{
                noteView.alter = 0;
            }
        }
            break;
        case LxMcStaffManageAchordMajor: {
            {
                if (noteView.isUpClef) {
                    for (LxMcNoteView *tempView in measuremodel.clefUpNoteViewArray) {
                        if ([[self aChordMajorMiditag] containsObject:@(tempView.miditag)]) {
                            if ([measuremodel lx_firstAppeargNoteView:tempView]) {
                                tempView.alter = 1;
                            }
                            tempView.tempMajorType = LxNoteTempMajorShape;
                        }else{
                            tempView.tempMajorType = LxNoteTempMajorNormal;
                        }
                    }
                }else{
                    for (LxMcNoteView *tempView in measuremodel.clefDoNoteViewArray) {
                        if ([[self aChordMajorMiditag] containsObject:@(tempView.miditag)]) {
                            if ([measuremodel lx_firstAppeargNoteView:tempView]) {
                                tempView.alter = 1;
                            }
                            tempView.tempMajorType = LxNoteTempMajorShape;
                        }else{
                            tempView.tempMajorType = LxNoteTempMajorNormal;
                        }
                    }
                }
            }
            break;
            default:
            break;
        }
    }
}

/**   获取点击位置音符    **/
- (LxMcNoteView *)noteViewWithTouchPoint:(CGPoint)touchPoint{
    LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:touchPoint];
    LxMcNoteView *touchView = nil;
    for (LxMcNoteView *noteView in [currentModel allNoteViewsArray]) {
        BOOL contains = CGRectContainsPoint(CGRectMake(CGRectGetMinX(noteView.frame) - 10, CGRectGetMinY(noteView.frame), CGRectGetWidth(noteView.frame) + 20, CGRectGetHeight(noteView.frame) + 20), touchPoint);
        if (contains && ![self.staticNotesArray containsObject:noteView])
        {
            touchView = noteView;
            if (noteView.noteType >= 50) {//如果是强度标记,则找出对应的音符
                NSArray *array = noteView.isUpClef ? currentModel.clefUpNoteViewArray : currentModel.clefDoNoteViewArray;
                for (LxMcNoteView *strengthNoteView in array) {
                    if (strengthNoteView.centerX == touchView.centerX) {
                        self.strengthNoteView = strengthNoteView;
                    }
                }
            }
            break;
        }
    }
    return touchView;
}

#pragma mark - staffNoteHandleAction
- (void)handlepanGesture:(UIPanGestureRecognizer *)gesture
{
    //琴键输入模式不支持拖拽
    if (self.editMode == LxMcEditModeKeyborad) {
        return;
    }
    CGPoint touchPoint = [gesture locationInView:self];
    switch (gesture.state) {
        case UIGestureRecognizerStateBegan:
        {
            self.ClefMeasureModelArray.firstObject.isMoveEditMode = YES;
            _cannotBtnAction = YES;
            
            if ([gesture.view isKindOfClass:[LxMcNoteView class]]) {
                LxMcNoteView *panNoteView = [[LxMcNoteView alloc] init];
                panNoteView.noteType = self.modelNodeType;
                panNoteView.isRest = self.modelRest;
                panNoteView.isDot = self.modelDot;
                if (self.modelNodeType < 50) {
                    panNoteView.headDirection = LxMcNoteHead_left_down;
                }else{
                    [panNoteView createStaffUI];
                }
                [panNoteView changeCandyUI:self.candy];

                panNoteView.center = CGPointMake(touchPoint.x - [panNoteView judgePointToCenter].x, touchPoint.y - [panNoteView judgePointToCenter].y);
                [self addSubview:panNoteView];
                self.dragNoteView = panNoteView;
                
            }else {
                LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:touchPoint];
                LxMcNoteView *touchView = [self noteViewWithTouchPoint:touchPoint];
                if(touchView)
                {
                    self.dragNoteView = touchView;
                    
                    if (touchView.noteType >= 50) {
                        touchView.center = touchPoint;
                    }else if(touchView.noteType != MusicNodeWhole) {
                        touchView.center = CGPointMake(touchPoint.x - [touchView judgePointToCenter].x, touchView.y - [touchView judgePointToCenter].y);
                    }
                    _dragOffsetPoint = CGPointMake(touchPoint.x - touchView.centerX, touchPoint.y - touchView.centerY);
                    self.dragNoteView.startPoint = CGPointMake(self.dragNoteView.center.x + [self.dragNoteView judgePoint].x, self.dragNoteView.center.y + [self.dragNoteView judgePoint].y);
                    
                    if ([currentModel.clefUpNoteViewArray containsObject:touchView]) {
                        [currentModel.clefUpNoteViewArray removeObject:touchView];
                    }else
                    {
                        [currentModel.clefDoNoteViewArray removeObject:touchView];
                    }
                    
                    [currentModel checkMinNodeUnit];
                    [self lx_reLayoutAllelementsInMeasure:currentModel];
                    
                }
            }
            
        }
            break;
        case UIGestureRecognizerStateChanged:
        {
            if (self.dragNoteView.noteType >= 50) {//不是强度标记就不需要判断线和符头的位置
                self.dragNoteView.center = touchPoint;
            }else {
                [self lx_dragHighlightLineWithNoeView:self.dragNoteView
                                     superOffsetPoint:CGPointZero];
                if (self.dragNoteView.miditag > 0 && !self.dragNoteView.isRest) {/** 方向上的判断 **/
                    if ([self.dragNoteView checkDirection]) {
                        self.dragNoteView.center = CGPointMake(touchPoint.x - [self.dragNoteView judgePointToCenter].x, touchPoint.y - [self.dragNoteView judgePointToCenter].y);/** 保证符头在手指操作位置 **/
                        _dragOffsetPoint = CGPointMake(touchPoint.x - self.dragNoteView.centerX, touchPoint.y - self.dragNoteView.centerY);
                    }
                }
                self.dragNoteView.center = CGPointMake(touchPoint.x - _dragOffsetPoint.x, touchPoint.y - _dragOffsetPoint.y);
            }
            
        }
            break;
        case UIGestureRecognizerStateEnded:
        case UIGestureRecognizerStateCancelled:
            case UIGestureRecognizerStateFailed:
        {
            _cannotBtnAction = NO;
            if (!_dragNoteView) {
                return;
            }
            if (self.dragNoteView.noteType >= 50) {//放置强弱标记
                [self setStrength:self.dragNoteView touchPoint:touchPoint];
            }else {//放置音符
                NSLog(@"%@",NSStringFromCGPoint(touchPoint));
                BOOL locateSuccess = [self checkNoteView:self.dragNoteView superOffsetPoint:CGPointZero touchPoint:touchPoint];
                if (locateSuccess) {
                    [self lx_dragHighlightLineWithNoeView:self.dragNoteView
                                         superOffsetPoint:CGPointZero];
                    self.dragNoteView.isLocatedStaff = YES;
                }else
                {
                    if (self.dragNoteView.miditag > 0) {
                        self.dragNoteView.center = CGPointMake(self.dragNoteView.startPoint.x - [self.dragNoteView judgePoint].x, self.dragNoteView.startPoint.y - [self.dragNoteView judgePoint].y);
                        self.dragNoteView.miditag = [self miditagWithRealPoint:self.dragNoteView.startPoint dragNoteView:self.dragNoteView];
                        [self.dragNoteView checkDirection];
                    }
                    [self clearAdditionLineArrayWithNoteView:self.dragNoteView];
                }
                [self clearHighlightLine];
            }
            _dragOffsetPoint = CGPointZero;
            self.dragNoteView = nil;
        }
            break;
            
        default:
            debugLog(@"其他手势");
            _cannotBtnAction = NO;
            break;
    }
}
- (void)handleTapGesture:(UITapGestureRecognizer *)gesture
{
//    CGPoint touchPoint = [gesture locationInView:self];
//    CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
//    CGRect  upRect = CGRectMake(0, self.y, self.width, centerY);
//    CGRect  doRect = CGRectMake(0, self.y+centerY, self.width, centerY);
//    //点击高音谱位置
//    if (CGRectContainsPoint(upRect, touchPoint)) {
//        self.isEditClefUp  = YES;
//    }
//    //点击低音谱位置
//    if (CGRectContainsPoint(doRect, touchPoint)) {
//        self.isEditClefUp  = NO;
//    }
    //琴键输入模式不支持拖拽
    if (self.editMode == LxMcEditModeKeyborad) {
        return;
    }
    switch (gesture.state) {
        case UIGestureRecognizerStateBegan:
        {
            self.ClefMeasureModelArray.firstObject.isMoveEditMode = YES;
            _cannotBtnAction = YES;
        }
            break;
        case UIGestureRecognizerStateEnded:
        {
//            self.backgroundColor = [UIColor lx_randomColor];
            CGPoint touchPoint = [gesture locationInView:self];
            LxMcNoteView *tapNoteView = [self noteViewWithTouchPoint:touchPoint];
            debugLog(@"手势点击位置x = %f, y = %f",touchPoint.x,touchPoint.y);
            if (tapNoteView &&
                [tapNoteView isKindOfClass:[LxMcNoteView class]] &&
                self.lineTapState) {
                [self.lineManager lx_addLineMusicNoteView:tapNoteView];
            }else{
                tapNoteView = [[LxMcNoteView alloc] init];
                tapNoteView.noteType = self.modelNodeType;
                tapNoteView.isRest = self.modelRest;
                tapNoteView.isDot = self.modelDot;
                tapNoteView.headDirection = LxMcNoteHead_center;
                [tapNoteView changeCandyUI:self.candy];
                if (self.modelNodeType >= 50) {//放置强弱标记
                    [self setStrength:tapNoteView touchPoint:touchPoint];
                }else {
                    tapNoteView.center = CGPointMake(touchPoint.x - [tapNoteView judgePointToCenter].x, touchPoint.y - [tapNoteView judgePointToCenter].y);
                    BOOL locateSuccess =  [self checkNoteView:tapNoteView
                                             superOffsetPoint:CGPointZero
                                                   touchPoint:touchPoint];
                    [tapNoteView checkDirection];
                    if (locateSuccess) {
                        tapNoteView.isLocatedStaff = YES;
                        if (!tapNoteView.isRest && tapNoteView.miditag > 0) {
                            [self midiplayWithTag:tapNoteView.miditag + tapNoteView.alter];
                            [self performSelector:@selector(midiStopWithTag:) withObject:[NSNumber numberWithInteger:tapNoteView.miditag + tapNoteView.alter] afterDelay:0.1];
                        }
                        /** 上加、下加线处理 **/
                        [self checkAdditionLineAndRelayoutWithNoteView:tapNoteView realHeadPoint:CGPointMake(tapNoteView.centerX + [tapNoteView judgePointToCenter].x, tapNoteView.centerY + [tapNoteView judgePointToCenter].y)];
                        debugLog(@"点击tag= %ld",(long)tapNoteView.miditag);
                        LxMcMeasureModel *model = self.ClefMeasureModelArray.firstObject;

                        if (model.measureEndOffsetX  > model.measureBeginOffsetX + [self defaultMeasureScreenWidth]) {
                            if ([_delegate respondsToSelector:@selector(reSetScrollViewContent)]) {
                                [_delegate reSetScrollViewContent];
                            }
                            if ([_delegate respondsToSelector:@selector(scrollTox:)]) {
                                [_delegate scrollTox:CGRectGetMinX(tapNoteView.frame) - mScreenWidth /2.f];
                            }
                        }
                    }
                }
            }
           
            _cannotBtnAction = NO;
        }
            break;
            
        default:
            _cannotBtnAction = NO;
            break;
    }
}

- (void)setStrength:(LxMcNoteView *)preSetNoteView touchPoint:(CGPoint)touchPoint {
    CGFloat centerY = CGRectGetHeight(self.frame) * 0.5;
    LxMcMeasureModel *currentModel = [self searchMeasureModelWithHeadRealPoint:touchPoint];
    //获取高音谱数据
    NSArray *searchArray = currentModel.clefUpNoteViewArray;
    NSMutableArray *strengthArray = currentModel.clefUpStrengthArray;
    CGFloat setY = CGRectGetHeight(self.frame) * 0.5;
    preSetNoteView.isUpClef = YES;
    if (touchPoint.y > centerY) {//当前触摸位置为低音谱，修改为低音谱数据
        searchArray = currentModel.clefDoNoteViewArray;
        strengthArray = currentModel.clefDoStrengthArray;
        setY += 9 * kMcStaffSpace;
        preSetNoteView.isUpClef = NO;
    }
    BOOL exist = NO;
    
    for (LxMcNoteView *noteView in searchArray) {
        CGRect strengthFrame = CGRectMake(noteView.centerX - preSetNoteView.width * 0.5, setY - preSetNoteView.height * 0.5, preSetNoteView.width, preSetNoteView.height);
        if ((CGRectIntersectsRect(noteView.frame, self.dragNoteView.frame) ||
             CGRectContainsPoint(strengthFrame, touchPoint)) &&
            !noteView.isRest) {
            if (noteView.strength != 0) {//移除之前的强度标记
                for (LxMcNoteView *strengthView in strengthArray) {
                    if (strengthView.centerX == noteView.centerX) {
                        [UIView animateWithDuration:0.25 animations:^{
                            strengthView.alpha = 0.0;
                        } completion:^(BOOL finished) {
                            [strengthView removeFromSuperview];
                        }];
                        [strengthArray removeObject:strengthView];
                        break;
                    }
                }
            }
            preSetNoteView.center = touchPoint;
            [preSetNoteView removeFromSuperview];
            exist = YES;
            noteView.strength = preSetNoteView.noteType;
            noteView.strengthView = preSetNoteView;
            [self addSubview:preSetNoteView];
            [strengthArray addObject:preSetNoteView];
            [UIView animateWithDuration:0.25 animations:^{
                preSetNoteView.center = CGPointMake(noteView.centerX, setY - 3);
            }];
            break;
        }
    }
    //添加或修改强度标记失败
    if (!exist) {
        if (self.strengthNoteView) {
            self.strengthNoteView.strength = 0;
        }
        [UIView animateWithDuration:0.25 animations:^{
            preSetNoteView.alpha = 0.0;
        } completion:^(BOOL finished) {
            [preSetNoteView removeFromSuperview];
        }];
    }
}
#pragma mark - ************************LineManageDelegate************************
- (NSMutableArray <LxMcNoteView *>* _Nonnull)sectionNotesWithFirstLineNote:(LxMcNoteView *)firstLineNote endLineNote:(LxMcNoteView *)endLineNote{
    NSMutableArray <LxMcNoteView *>* notes = [self lx_sectionNotesWithFirstNote:firstLineNote endNote:endLineNote];
    NSMutableArray *deleNotes = [NSMutableArray array];
    /**   去除非当前连音音谱的音符    **/
    for (LxMcNoteView *note in notes) {
        if (note.isUpClef != firstLineNote.isUpClef) {
            [deleNotes addObject:note];
        }
    }
    [notes removeObjectsInArray:deleNotes];
    return notes;
}

#pragma mark - SetMethod
- (void)originOffsetXAdd:(CGFloat)offset_x_add
{
    _originAvalibleOffsetX += offset_x_add;
    _currentEndLineOffsetX = _originAvalibleOffsetX;/** 在基础上多加两个的差距 **/
}

- (void)setStaticNotesArray:(NSArray<LxMcNoteView *> *)staticNotesArray{
    _staticNotesArray = staticNotesArray;
    [self setUpStaticNotes];
}

- (void)setUpStaticNotes {
    ///移除之前的默认音符
//    for (LxMcNoteView *noteView in self.staticNotesArray) {
//        noteView.additionStaffLineCount = 0;
//        [noteView changeCandyUI:self.candy];
//        [noteView removeFromSuperview];
//    }
//    //重新布局新的默认音符
//    NSInteger index = 0;
//    for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray ) {
//        for (LxMcNoteView *noteView in self.staticNotesArray) {
//            if (index == noteView.measureIndex && noteView.fixedPosition.showInSectionsCount == self.sectionIndex) {
//                if (noteView.isUpClef) {
//                    [measureModel.clefUpNoteViewArray addObject:noteView];
//                }else {
//                    [measureModel.clefDoNoteViewArray addObject:noteView];
//                }
//            }
//            [self setAlter:noteView withMeasureModel:measureModel];
//        }
//        [self setStaticNotes:measureModel];
//        index ++;
//    }
}

- (void)setIsEditClefUp:(BOOL)isEditClefUp {
    _isEditClefUp = isEditClefUp;

//    加亮对应的高音谱符号图片
    if (isEditClefUp) {
        [self.clefUpLayer setScaleImage:@"write_clef_up_rudin_l@2x"];
        [self.clefDoLayer setScaleImage:@"write_clef_do_rudin@2x"];
    }else {
        [self.clefUpLayer setScaleImage:@"write_clef_up_rudin@2x"];
        [self.clefDoLayer setScaleImage:@"write_clef_do_rudin_l@2x"];
    }
}

- (void)setCandy:(BOOL)candy {
    _candy = candy;
    if (candy) {
        [self.clefUpLayer setScaleImage:@"write_clef_candyUp@2x"];
        self.firstLineLayer.strokeColor = [UIColor lx_colorWithHexString:@"#e6cc72"].CGColor;
        for (CALayer *lineLayer in _stafflineLayerArray) {
            lineLayer.backgroundColor = [UIColor lx_colorWithHexString:@"#e6cc72"].CGColor;
        }
        for (CALayer *lineLayer in _stafflinePlusLayerArray) {
            lineLayer.backgroundColor = [UIColor lx_colorWithHexString:@"#e6cc72" ].CGColor;
        }
        for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray) {
            measureModel.measureLayer.strokeColor = [UIColor lx_colorWithHexString:@"#e6cc72"].CGColor;
            measureModel.measureIndexLayer.foregroundColor = [UIColor lx_colorWithHexString:@"#e6cc72" ].CGColor;
        }
        self.measureEndLayer.strokeColor = [UIColor lx_colorWithHexString:@"#e6cc72"].CGColor;
    }else {
        [self.clefUpLayer setScaleImage:@"write_clef_up_rudin@2x"];
        self.firstLineLayer.strokeColor = kMcStaffLineColor;
        for (CALayer *lineLayer in _stafflineLayerArray) {
            lineLayer.backgroundColor = kMcStaffLineColor;
        }
        for (CALayer *lineLayer in _stafflinePlusLayerArray) {
            lineLayer.backgroundColor = kMcStaffLineColor;
        }
        for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray) {
            measureModel.measureLayer.strokeColor = kMcStaffLineColor;
            measureModel.measureIndexLayer.foregroundColor = kMcStaffLineColor;
        }
        self.measureEndLayer.strokeColor = kMcStaffLineColor;
    }
    //设置高音谱符号颜色
    if (!candy) {
        self.isEditClefUp = self.isEditClefUp;
    }
    
    for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray) {
        for (LxMcNoteView *noteView in [measureModel allNoteViewsArray]) {
            [noteView changeCandyUI:candy];
        }
    }
}

- (void)setLineTapState:(BOOL)lineTapState{
    _lineTapState = lineTapState;
    if (lineTapState) {
        
    }else{
        
    }
}

#pragma mark - GetMethod
- (NSMutableArray<CALayer *> *)stafflinePlusLayerArray {
    if (!_stafflinePlusLayerArray) {
        _stafflinePlusLayerArray = [NSMutableArray array];
    }
    return _stafflinePlusLayerArray;
}

- (CAShapeLayer *)measureEndLayer
{
    if (!_measureEndLayer) {
        _measureEndLayer = [CAShapeLayer layer];
        _measureEndLayer.lineCap = kCALineCapSquare;
        UIBezierPath *path = [UIBezierPath bezierPath];
        _measureEndLayer.lineWidth = kMcStaffMeasureLineRude * 2;
        [path moveToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, - 7 * kMcStaffSpace + _measureEndLayer.lineWidth / 2.f)];
        if (self.staffClefState != LxMcMcBothClef) {
            [path addLineToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f,- 3 * kMcStaffSpace - _measureEndLayer.lineWidth / 2.f)];
            
            [path moveToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, 3 * kMcStaffSpace + _measureEndLayer.lineWidth / 2.f)];
        }
      
        [path addLineToPoint:CGPointMake(kMcStaffMeasureLineRude / 2.f, 7 * kMcStaffSpace - _measureEndLayer.lineWidth / 2.f)];
        _measureEndLayer.path = path.CGPath;
        _measureEndLayer.strokeColor = kMcStaffLineColor;
        [self.layer addSublayer:_measureEndLayer];
        return _measureEndLayer;
    }
    return _measureEndLayer;
}

- (UIView *)highOctaveView {
    if (!_highOctaveView) {
        int a = 0;
        int b = 0;
        if (_isAllVersion) {
            a = 5;
            b = 4;
        }
        UIView *highOctaveView = [[UIView alloc] initWithFrame:CGRectMake(_originAvalibleOffsetX, mScreenHeight - kMcStaffSpace * _h_tCount, 0, kMcStaffSpace * (4 + _h_tCount + _h_bCount + a))];
        highOctaveView.layer.borderWidth = 2.0;
        highOctaveView.layer.borderColor = _isAllVersion ? [UIColor clearColor].CGColor:[UIColor redColor].CGColor;
        highOctaveView.userInteractionEnabled = NO;
        CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
        highOctaveView.y = centerY - (6 + _h_tCount + b) * kMcStaffSpace; //- 5 * kMcStaffScale;
        highOctaveView.alpha = 0.0;
        [self addSubview:highOctaveView];
        [self bringSubviewToFront:highOctaveView];
        _highOctaveView = highOctaveView;
    }
    return _highOctaveView;
}

- (UIView *)lowOctaveView {
    if (!_lowOctaveView) {
        int a = 0;
        int b = 0;
        if (_isAllVersion) {
            a = 5;
            b = 4;
        }
        UIView *lowOctaveView = [[UIView alloc] initWithFrame:CGRectMake(_originAvalibleOffsetX, mScreenHeight - _l_tCount * kMcStaffSpace, 0, kMcStaffSpace * (4 + _l_tCount + _l_bCount + a))];
        lowOctaveView.layer.borderWidth = 2.0;
        lowOctaveView.layer.borderColor = _isAllVersion ? [UIColor clearColor].CGColor:[UIColor redColor].CGColor;
        lowOctaveView.userInteractionEnabled = NO;
        lowOctaveView.alpha = 0.0;
        CGFloat centerY = CGRectGetHeight(self.frame)/2.f;
        lowOctaveView.y = centerY + (4 + _l_tCount - b) * kMcStaffSpace;// + 0.5 * kMcStaffScale;
        [self addSubview:lowOctaveView];
        _lowOctaveView = lowOctaveView;
    }
    return _lowOctaveView;
}

- (CALayer *)defaultStaffLineLayerWithWidth:(CGFloat)width
{
    CALayer *layer = [CALayer layer];
    layer.backgroundColor = self.candy ? [UIColor lx_colorWithHexString:@"#e6cc72"].CGColor : kMcStaffLineColor;//e6cc72
    layer.frame = CGRectMake(0, 0, width, kMcStaffLineRude);
    return layer;
}

/** 获取midi对应的应高亮五线谱元素 **/
- (NSDictionary *)midiToHighlightLineLayer
{
    return @{@(41) : @[self.stafflineLayerArray[9]],
             @(43) : @[self.stafflineLayerArray[9]],
             @(45) : @[self.stafflineLayerArray[9],
                       self.stafflineLayerArray[8]],
             @(47) : @[self.stafflineLayerArray[8]],
             @(48) : @[self.stafflineLayerArray[8],
                       self.stafflineLayerArray[7]],
             @(50) : @[self.stafflineLayerArray[7]],
             @(52) : @[self.stafflineLayerArray[7],
                       self.stafflineLayerArray[6]],
             @(53) : @[self.stafflineLayerArray[6]],
             @(55) : @[self.stafflineLayerArray[6],
                       self.stafflineLayerArray[5]],
             @(57) : @[self.stafflineLayerArray[5]],
             @(59) : @[self.stafflineLayerArray[5]],
             
             
             @(62) : @[self.stafflineLayerArray[4]],
             @(64) : @[self.stafflineLayerArray[4]],
             @(65) : @[self.stafflineLayerArray[4],
                       self.stafflineLayerArray[3]],
             @(67) : @[self.stafflineLayerArray[3]],
             @(69) : @[self.stafflineLayerArray[3],
                       self.stafflineLayerArray[2]],
             @(71) : @[self.stafflineLayerArray[2]],
             @(72) : @[self.stafflineLayerArray[2],
                       self.stafflineLayerArray[1]],
             @(74) : @[self.stafflineLayerArray[1]],
             @(76) : @[self.stafflineLayerArray[1],
                       self.stafflineLayerArray[0]],
             @(77) : @[self.stafflineLayerArray[0]],
             @(79) : @[self.stafflineLayerArray[0]],
             };
}

- (NSDictionary *)lowBlackkeyMiditagDict {
    
    return @{@"65":@"64",
             @"63":@"62",
             @"61":@"60",
//             @"0":@"59",
             @"58":@"57",
             @"56":@"55",
             @"54":@"53",
//             @"46":@"52",
             @"51":@"50",
             @"49":@"48",
//             @"39":@"47",
             @"46":@"45",
             @"44":@"43",
             @"42":@"41",
//             @"61":@"40",
             @"39":@"38",
//             @"61":@"37",
             };
}

- (NSDictionary *)highBlackkeyMiditagDict {
    return @{@"85":@"84",
//             @"":@"83",
             @"82":@"81",
             @"80":@"79",
             @"78":@"77",
//             @"":@"76",
             @"75":@"74",
             @"73":@"72",
//             @"":@"71",
             @"70":@"69",
             @"68":@"67",
             @"66":@"65",
//             @"":@"64",
             @"63":@"62",
             @"61":@"60",
//             @"":@"59",
             @"58":@"57",
             };
}

- (NSArray *)highMiditagArray {
    if (!_highMiditagArray) {
        _highMiditagArray =@[@(84),
                             @(83),
                             @(81),
                             @(79),
                             @(77),
                             @(76),
                             @(74),
                             @(72),
                             @(71),
                             @(69),
                             @(67),
                             @(65),
                             @(64),
                             @(62),
                             @(60),
                             @(59),
                             @(57)];
        
    }
    return _highMiditagArray;
}

//高低音谱特殊mid
- (NSArray *)lhmiditagArray {
    return @[@(65),
             @(64),
             @(62),
             @(60),
             @(59),
             @(57)
             ];
}

- (NSArray *)lowMiditagArray {
    if (!_lowMiditagArray) {
        _lowMiditagArray = @[@(64),
                             @(62),
                             @(60),
                             @(59),
                             @(57),
                             @(55),
                             @(53),
                             @(52),
                             @(50),
                             @(48),
                             @(47),
                             @(45),
                             @(43),
                             @(41),
                             @(40),
                             @(38),
                             @(37)];
    }
    return _lowMiditagArray;
}

/**
 *@description 获取所有音符，按播放序列排序
 **/
- (NSMutableArray <LxMcNoteView *>*)lx_getPlayQueueNoteArray {
    NSMutableArray <LxMcNoteView *>*totalNotesArray = [[NSMutableArray alloc] init];
    NSTimeInterval playPosition = 0;
    for (LxMcMeasureModel *measureModel in self.ClefMeasureModelArray) {
        NSArray <LxMcNoteView *>*measureNoteViews = [[measureModel allPlayNoteViewsArray] sortedArrayUsingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
            LxMcNoteView *note1 = (LxMcNoteView *)obj1;
            LxMcNoteView *note2 = (LxMcNoteView *)obj2;
            if (note1.eventDuration < note2.eventDuration) {
                return NSOrderedAscending;
            }else
            {
                return NSOrderedDescending;
            }
            
        }];
        for (LxMcNoteView *noteView in measureNoteViews) {
            noteView.playEventPosition = [noteView eventDuration] * kMcStaffWholeDuration + playPosition;
        }
        [totalNotesArray addObjectsFromArray:measureNoteViews];
        playPosition += [LxMcStaffLineView durationUnitWithBeatasType:_staffBeatsType] * kMcStaffWholeDuration;
    }
    [_queuePlayNotes removeAllObjects];
    _queuePlayNotes = totalNotesArray;
    NSInteger index = 0;
    for (LxMcNoteView *noteView in _queuePlayNotes) {
        noteView.staffIndex = index;
        index ++;
    }
    return totalNotesArray;
}


/**
 F大调所有B音降半音的miditag值
 */
- (NSArray *)FMajorMiditag {
    return @[@23, @35, @47, @59, @71, @83, @95];
}
/**
 G大调所有F音升半音的miditag值
 */
- (NSArray *)GMajorMiditag {
    return @[@29, @41, @53, @65, @77, @89, @101];
}
/** a小调所有升半音的miditag值 **/
- (NSArray *)aChordMajorMiditag{
    return @[@(43),@(55),@(67),@(79),@(91)];
}
/** e小调所有升半音的miditag值 **/
- (NSArray *)eChordMajorMiditag{
    return @[@(38),@(50),@(62),@(74),@(86),@(98)];
}
/**
 D大调所有F音和C音升半音的miditag值
 */
- (NSArray *)DMajorMiditag {
    return @[@29, @41, @53, @65, @77, @89, @101,
             @24, @36, @48, @60, @72, @84, @96,];
}
/**
 normal所有F音和C音升半音的miditag值
 */
- (NSArray *)NormalMajorMiditag {
    return @[@(22),@(25),@(27),@(30),@(32),@(34),
             @(37),@(39),@(42),@(44),@(46),@(49),
             @(51),@(54),@(56),@(58),@(61),@(63),
             @(66),@(68),@(70),@(73),@(75),@(78),
             @(80),@(82),@(85),@(87),@(90),@(92),
             @(94),@(97),@(99),@(102),@(104),@(106)];
}

/** 获取一横屏宽度 **/
- (CGFloat)defaultMeasureScreenWidth
{
    return MIN(mScreenWidth, mScreenHeight);
}


@end
