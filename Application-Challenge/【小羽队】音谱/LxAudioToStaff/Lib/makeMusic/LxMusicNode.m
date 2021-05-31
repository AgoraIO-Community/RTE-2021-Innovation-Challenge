//
//  LxMusicNode.m
//  SmartPiano
//
//  Created by 李翔 on 2017/4/6.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "LxMusicNode.h"
#import "CALayer+Default.h"
#import <AsyncDisplayKit/AsyncDisplayKit.h>
#import "CALayer+Default.h"
#import "UIImage+Default.h"
#import "UIColor+Default.h"
@implementation LxMusicNode

#pragma mark - SetMethod
- (void)setType:(NSString *)type
{
    _type = type;
    if ([type isEqualToString:@"quarter"])
        _nodeType = MusicNodeQuarter;
    else if ([type isEqualToString:@"rest"])
        _nodeType = MusicNodeRest;
    else if ([type isEqualToString:@"half"])
        _nodeType = MusicNodeHalf;
    else if ([type isEqualToString:@"whole"])
        _nodeType = MusicNodeWhole;
    else if ([type isEqualToString:@"eighth"])
        _nodeType = MusicNodeEighth;
    else if ([type isEqualToString:@"16th"])
        _nodeType = MusicNode16th;
    else if ([type isEqualToString:@"32nd"])
        _nodeType = MusicNode32nd;
    else
        _nodeType= MusicNodeRest;
}

- (void)setStep:(NSString *)step
{
    _step = step;
      [self checkAndSetMidi];
   
}

- (void)setOctave:(NSInteger)octave
{
    _octave = octave;
    [self checkAndSetMidi];
   
}


/**设置弹奏状态*/
- (void)setPlayStatus:(MusicNodeStatus)playStatus
{
    
    UIColor *fillColor ;
    switch (playStatus) {
        case MusicNodePlayNormal:
        {
            fillColor = [UIColor blackColor];
        }
            break;
            case MusicNodePlayError:
        {
            fillColor = lx_UIColorFrom_Rgb(237, 61, 61);
        }
            break;
            case MusicNodePlayRight:
        {
            if (self.errorPlayed) {
                fillColor = lx_UIColorFrom_Rgb(237, 61, 61);
            }else if (self.playScoreGrade == NotePlayGradeThree ||
                self.nodeType == MusicNodeRest ||
                self.forceGreeShown) {
                fillColor = lx_UIColorFrom_Rgb(68, 207, 68);
            }else
            {
                fillColor = lx_UIColorFrom_HexStr(@"#f8e900");
            }
        }
            break;
        case MusicNodePlayPrepare:{
            if (playStatus != _playStatus && _playStatus != MusicNodePlayNormal) {
                fillColor = [UIColor blackColor];
                
                if (_lyricsNode) {
                    _lyricsNode.attributedText = [[NSAttributedString alloc] initWithString:_lyrics attributes:@{NSFontAttributeName : [UIFont boldSystemFontOfSize:30],
                                                                                                                                NSForegroundColorAttributeName:fillColor}];
                }
               
            }
        }break;
            
        default:
            break;
    }
    _playStatus = playStatus;
    if (playStatus != MusicNodePlayPrepare) {
        if (_lyricsNode) {
            _lyricsNode.attributedText = [[NSAttributedString alloc] initWithString:_lyrics attributes:@{NSFontAttributeName : [UIFont boldSystemFontOfSize:30],
                                                                                                                        NSForegroundColorAttributeName:fillColor}];
        }
    
    }
}


- (void)setAllPartHide:(BOOL)hide
{


 
}


#pragma mark - GetMethod
- (NSMutableArray <CAShapeLayer *>*)auxiliaryLayers
{
    if (!_auxiliaryLayers) {
        _auxiliaryLayers = [NSMutableArray new];
    }
    return _auxiliaryLayers;
}

- (NSMapTable *)addToImageNodeTable
{
    if (!_addToImageNodeTable) {
        _addToImageNodeTable = [NSMapTable mapTableWithKeyOptions:NSPointerFunctionsStrongMemory
                                                     valueOptions:NSPointerFunctionsWeakMemory];
    }
    return _addToImageNodeTable;
}



/**
 *@description 获取弹奏等级评语
 **/
- (NSString *)playGradeString
{

    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
    });
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 2 * NSEC_PER_SEC), dispatch_get_global_queue(0, 0), ^{
        
    });
    
    NSString *appraise ;
    switch (self.playGrade) {
        case NotePlayFreePremature:
           appraise = @"过早";
            break;
            case notePlayFreeLate:
            appraise = @"过晚";
            break;
            case NotePlayFreeStandard:
            appraise = @"标准";
            break;
        default:
            appraise = @"过早";
            break;
    }
    return appraise;
}
/**
 *@description 获取弹奏力度评语
 **/
- (NSString *)playDymicString
{
    NSString *appraise ;
    switch (self.playDymic) {
        case NotePlayDymicLight:
            appraise = @"过轻";
            break;
        case NotePlayDymicHeavy:
            appraise = @"过重";
            break;
        case NotePlayFreeStandard:
            appraise = @"标准";
            break;
        default:
            appraise = @"过轻";
            break;
    }
    return appraise;
}
/**
 *@description 获取抬起评语
 **/
- (NSString *)playDurationString
{
    NSString *appraise ;
    switch (self.playDuration) {
        case NotePlayFreePremature:
            appraise = @"过短";
            break;
        case notePlayFreeLate:
            if(_tenuto == 1)
            {
                appraise = @"过短";
            }
            else
            {
                appraise = @"过长";
            }
            break;
        case NotePlayFreeStandard:
            appraise = @"标准";
            break;
        default:
            appraise = @"过短";
            break;
    }
    return appraise;
}

- (NSInteger)midiTag
{
    if (_alter > 0 ) {
        return _midiTag + _alter;
    }else if (_alter < 0 )
    {
        return _midiTag + _alter;
    }else
    {
        return _midiTag;
    }
}

#pragma mark - midi相关计算
- (void)checkAndSetMidi
{
    if (_step && _octave > 0) {
       _midiTag =  [LxMusicNode miditagWithStep:_step octave:_octave];
    }
}

+ (NSInteger)miditagWithStep:(NSString *)step octave:(NSInteger)octave
{
    return (octave - 1) * 12 + 23 + [LxMusicNode stepStrToInteger:step];
}

+ (NSInteger)stepStrToInteger:(NSString *)step
{
    NSDictionary *dic = @{@"C":@(1),
                          @"D":@(3),
                          @"E":@(5),
                          @"F":@(6),
                          @"G":@(8),
                          @"A":@(10),
                          @"B":@(12)
                          };
    return [dic[step] integerValue];
}

+ (NSInteger)octaveWithMidiTag:(NSInteger)midiTag;
{
    if (midiTag >= 96)
        return 7;
    else if (midiTag >= 84)
        return 6;
    else if (midiTag >= 72)
        return 5;
    else if (midiTag >= 60)
        return 4;
    else if (midiTag >= 48)
        return 3;
    else if (midiTag >= 36)
        return 2;
    else if (midiTag >= 24)
        return 1;
    else
        return 4;
}

+ (NSString *)stepWithMiditag:(NSInteger)midiTag{
    NSDictionary *dic = @{@(1) : @"C",
                          @(3) : @"D",
                          @(5) : @"E",
                          @(6) : @"F",
                          @(8) : @"G",
                          @(10) : @"A",
                          @(12) : @"B"};
    NSInteger octave = [LxMusicNode octaveWithMidiTag:midiTag];
    NSInteger stepKey = midiTag - (octave - 1) * 12 - 23;
    NSString *step = [dic objectForKey:@(stepKey)];
    if (!step) {
       step = [LxMusicNode stepWithMiditag:midiTag + 1];
    }
    
    return step;
}



#pragma mark -Function
- (UIColor *)curationColor
{
    
    NSDictionary * colors = nil;
  
        colors = @{@"C":[[UIColor redColor] colorWithAlphaComponent:0.35],
                   @"D":[[UIColor orangeColor] colorWithAlphaComponent:0.35],
                   @"E":[[UIColor yellowColor] colorWithAlphaComponent:0.45],
                   @"F":[[UIColor greenColor] colorWithAlphaComponent:0.35],
                   @"G":[[UIColor cyanColor] colorWithAlphaComponent:0.35],
                   @"A":[[UIColor blueColor] colorWithAlphaComponent:0.35],
                   @"B":[[UIColor purpleColor] colorWithAlphaComponent:0.35],//紫色
                   };
   
    UIColor *color = [colors objectForKey:self.step];
    return color;
}




#pragma mark - CallFunction
- (void)lx_resetStatusWithSameMidiNode:(LxMusicNode *)sameNode{
    self.playGrade = sameNode.playGrade;
    self.playScore = sameNode.playScore;
    self.playDuration = sameNode.playDuration;
    self.playDurationScore = sameNode.playDurationScore;
    self.playScoreGrade = sameNode.playScoreGrade;
    self.playStatus = sameNode.playStatus;
}
/**
 *@description 获取实际非升降调前的miditag
 **/
- (NSInteger)realMiditag{
    return _midiTag;
}
//获取UI类型
+ (NoteLayerType )getUItypeWithStr:(NSString *)noteType
{
    if ([noteType isEqualToString:@"note"])
        return NoteLayerNote;
    else if ([noteType isEqualToString:@"rest"])
        return NoteLayerRest;
    else if ([noteType isEqualToString:@"dot"])
        return NoteLayerDot;
    return NoteLayerNote;
}



- (CAShapeLayer *)colorLayer
{
    if (!_colorLayer) {
        CAShapeLayer *layer = [CAShapeLayer layer];
        layer.frame = CGRectMake(CGRectGetMidX(self.nodeLayer.frame), CGRectGetMinY(self.nodeLayer.frame), self.colorLayerWidth, CGRectGetHeight(self.nodeLayer.frame));
        UIBezierPath *path = [UIBezierPath bezierPathWithRoundedRect:layer.bounds cornerRadius:4.f];
        layer.path = path.CGPath;
        
        layer.strokeColor = [self curationColor].CGColor;
        layer.lineWidth = 2;
        layer.fillColor = [UIColor clearColor].CGColor;
        _colorLayer = layer;
    }
    
    
    return _colorLayer;
}

- (void)reset
{
    _playStatus = MusicNodePlayNormal;
    _showInKeyBoard = NO;
    _playScore = 0;
    _playDymicScore = 0;
    _errorPlayed = NO;
    _playDurationScore = 0;
    _playGrade = NotePlayFreeZero;
    _playDymic = NotePlayDymicZero;
    _playDuration = NotePlayFreeZero;
    self.pressTime = 0;
    self.putTime = 0;
    //此处重新生成图片，避免多次图片的主题色更改导致无法改变颜色问题
    UIImage *nodeImage = [_nodeLayer lx_snapshotImage];
   
    if (_lyricsNode) {
        _lyricsNode.attributedText = [[NSAttributedString alloc] initWithString:_lyrics attributes:@{NSFontAttributeName : [UIFont boldSystemFontOfSize:30],
                                                                                                     NSForegroundColorAttributeName:[UIColor blackColor]}];
    }
}

+ (CGFloat)scoreTransPlayDownWithGrade:(NotePlayGradeType)grade
{
    CGFloat score = 0;
    switch (grade) {
        case NotePlayGradeOne:
            score = 40;
            break;
            case NotePlayGradeTwo:
            score = 80;
            break;
            case NotePlayGradeThree:
            score = 100;
            break;
        default:
            break;
    }
    return score;
}

//键盘按下计分
- (NotePlayGradeType)notePlayGradeWithContentOffsetX:(CGFloat)offset_x baseCheckWidth:(CGFloat)baseWidth
{
    self.pressTime = [[NSDate date] timeIntervalSince1970];
    NotePlayGradeType playScoreGrade = NotePlayGradeZero;
    if (offset_x >= _x - 1.5 * baseWidth && offset_x <= _x - baseWidth * 1.2f) {
        playScoreGrade = NotePlayGradeOne;
        self.playGrade = NotePlayFreePremature;
    }else if (offset_x >= _x - baseWidth * 1.2f && offset_x < _x - baseWidth * 0.85f)
    {
        playScoreGrade = NotePlayGradeTwo;
        self.playGrade = NotePlayFreePremature;
    }else if (offset_x >= _x - baseWidth * 0.15f && offset_x <= _x + baseWidth*0.5)
    {
        playScoreGrade = NotePlayGradeThree;
        self.playGrade = NotePlayFreeStandard;
    }else if (offset_x > _x + baseWidth*1.1f && offset_x <= _x + baseWidth * 1.7)
    {
        playScoreGrade = NotePlayGradeTwo;
        self.playGrade = notePlayFreeLate;
    }else if (offset_x > _x + baseWidth*1.7f && offset_x <= _x + baseWidth * 2.5)
    {
        playScoreGrade = NotePlayGradeOne;
        self.playGrade = notePlayFreeLate;
    }
    debugLog(@"弹奏等级%ld",playScoreGrade);
    self.playScoreGrade = playScoreGrade;
    self.playScore = [LxMusicNode scoreTransPlayDownWithGrade:playScoreGrade];
    return playScoreGrade;
}

- (NSTimeInterval)notePlayCutPutUpWithStaffPlayScale:(CGFloat)playScale{
    NSTimeInterval stanrdDuration = self.all_duration > 0 ? self.all_duration * 1.1 : self.duration;
       
       self.putTime = [[NSDate date] timeIntervalSince1970];
       NSInteger score = 0;
       if (self.staccato) {
           if ((self.putTime - self.pressTime) / (stanrdDuration / playScale) < 0.5) {
               score = 100;
               self.playDuration = NotePlayFreeStandard;
           }else{
               score = 40;
               self.playDuration = notePlayFreeLate;
           }
           self.playDurationScore = score;
           return score;;
       }
    if(self.tenuto)
    {
        if ((self.putTime - self.pressTime) / (stanrdDuration / playScale) > 0.7) {
            score = 100;
            self.playDuration = NotePlayFreeStandard;
        }else{
            score = 40;
            self.playDuration = notePlayFreeLate;
        }
        self.playDurationScore = score;
        return score;;
    }
       score = MAX(0, 100 *(self.putTime - self.pressTime) / (stanrdDuration / playScale));
    NSLog(@"抬起时值分数%ld",(long)score);
       switch (self.nodeType) {
           case MusicNodeQuarter:
           {
               if (score < 20) {
                   
                   self.playDuration = NotePlayFreePremature;
               }else if(score <= 45){
                   self.playDuration = NotePlayFreePremature;
               }
               else if (score > 45 && score < 110)
               {
                   self.playDuration = NotePlayFreeStandard;
                   score = 100;
               }else if (score < 120)
               {
                   self.playDuration = notePlayFreeLate;
                    score = 80;
               }else
               {
                   score = 0;
                   self.playDuration = notePlayFreeLate;
               }
           }
               break;
           case MusicNodeHalf:
           {
               if (score < 30) {
                   score = 30;
                   self.playDuration = NotePlayFreePremature;
               }else if (score <= 50)
               {
                   self.playDuration = NotePlayFreePremature;
               }
               else if ( score <= 125)
               {
                   self.playDuration = NotePlayFreeStandard;
                   score = 100;
               }
               else if (score < 150)
               {
                   self.playDuration = notePlayFreeLate;
                   score = 60;
               }else{
                   score = 0;
                   self.playDuration = notePlayFreeLate;
               }
           }
               break;
           case MusicNodeWhole:
           {
                if (score < 55)
               {
                   self.playDuration = NotePlayFreePremature;
                    
               }else if (score < 110)
               {
                   score = 100;
                   self.playDuration = NotePlayFreeStandard;
               }
               else if (score < 150){
                   score = 0;
                   self.playDuration = notePlayFreeLate;
               }
               else
               {
                   self.playDuration = notePlayFreeLate;
                   score = 0;
               }
           }
           case MusicNodeEighth:
           {
               if (score < 20) {
                   score = 60;
                   self.playDuration = NotePlayFreePremature;
               }else if (score < 40)
               {
                   self.playDuration = NotePlayFreePremature;
                    score = 70;
               }else if (score < 130)
               {
                   self.playDuration = NotePlayFreeStandard;
                    score = 100;
               }else if (score < 140)
               {
                   self.playDuration = notePlayFreeLate;
                    score = 80;
               }else
               {
                   score = 0;
                   self.playDuration = notePlayFreeLate;
               }
           }
               break;
           case MusicNode16th:
           {
               if (score < 20) {
                   score = 60;
                   self.playDuration = NotePlayFreePremature;
               }else if (score < 35)
               {
                   self.playDuration = NotePlayFreePremature;
                   score = 70;
               }else if (score < 115)
               {
                   self.playDuration = NotePlayFreeStandard;
                   score = 100;
               }else if (score < 130)
               {
                   self.playDuration = notePlayFreeLate;
                   score = 80;
               }else
               {
                   score = 0;
                   self.playDuration = notePlayFreeLate;
               }
           }
               
           default:
               break;
       }
       self.playDurationScore = score;

       return score;
}


- (NSInteger)notePlayPutUpWIthStaffplayScale:(CGFloat)playScale;
//键盘抬起计分

{
    
   
    
    NSTimeInterval stanrdDuration = self.all_duration > 0 ? self.all_duration : self.duration;
    
    self.putTime = [[NSDate date] timeIntervalSince1970];
    NSInteger score = 0;
    if (self.staccato) {
        if ((self.putTime - self.pressTime) / (stanrdDuration / playScale) < 0.5) {
            score = 100;
            self.playDuration = NotePlayFreeStandard;
        }else{
            score = 40;
            self.playDuration = notePlayFreeLate;
        }
        self.playDurationScore = score;
        return score;;
    }
    if(self.tenuto)
    {
        if ((self.putTime - self.pressTime) / (stanrdDuration / playScale) > 0.7) {
            score = 100;
            self.playDuration = NotePlayFreeStandard;
        }else{
            score = 40;
            self.playDuration = notePlayFreeLate;
        }
        self.playDurationScore = score;
        return score;;
    }
    score = MAX(0, 100 *(self.putTime - self.pressTime) / (stanrdDuration / playScale));
    NSLog(@"抬起时值分数%ld",score);
    switch (self.nodeType) {
        case MusicNodeQuarter:
        {
            if (score < 30) {
                score = 55;
                self.playDuration = NotePlayFreePremature;
            }else if(score <= 58){
                self.playDuration = NotePlayFreePremature;
            }
            else if (score > 58 && score < 110)
            {
                self.playDuration = NotePlayFreeStandard;
                score = 100;
            }else if (score > 30 && score < 70)
            {
                self.playDuration = NotePlayFreePremature;
                score = 60;
            }else if (score < 120)
            {
                self.playDuration = notePlayFreeLate;
                 score = 80;
            }else
            {
                score = 0;
                self.playDuration = notePlayFreeLate;
            }
        }
            break;
        case MusicNodeHalf:
        {
            if (score < 40) {
                score = 40;
                self.playDuration = NotePlayFreePremature;
            }else if (score <= 65)
            {
                self.playDuration = NotePlayFreePremature;
            }
            else if ( score <= 125)
            {
                self.playDuration = NotePlayFreeStandard;
                score = 100;
            }
            else if (score < 150)
            {
                self.playDuration = notePlayFreeLate;
                score = 60;
            }else{
                score = 0;
                self.playDuration = notePlayFreeLate;
            }
        }
            break;
        case MusicNodeWhole:
        {
             if (score < 78)
            {
                self.playDuration = NotePlayFreePremature;
                 
            }else if (score < 110)
            {
                score = 100;
                self.playDuration = NotePlayFreeStandard;
            }
            else if (score < 150){
                score = 0;
                self.playDuration = notePlayFreeLate;
            }
            else
            {
                self.playDuration = notePlayFreeLate;
                score = 0;
            }
        }
        case MusicNodeEighth:
        {
            if (score < 20) {
                score = 60;
                self.playDuration = NotePlayFreePremature;
            }else if (score < 40)
            {
                self.playDuration = NotePlayFreePremature;
                 score = 70;
            }else if (score < 130)
            {
                self.playDuration = NotePlayFreeStandard;
                 score = 100;
            }else if (score < 140)
            {
                self.playDuration = notePlayFreeLate;
                 score = 80;
            }else
            {
                score = 0;
                self.playDuration = notePlayFreeLate;
            }
        }
            break;
        case MusicNode16th:
        {
            if (score < 20) {
                score = 60;
                self.playDuration = NotePlayFreePremature;
            }else if (score < 35)
            {
                self.playDuration = NotePlayFreePremature;
                score = 70;
            }else if (score < 115)
            {
                self.playDuration = NotePlayFreeStandard;
                score = 100;
            }else if (score < 130)
            {
                self.playDuration = notePlayFreeLate;
                score = 80;
            }else
            {
                score = 0;
                self.playDuration = notePlayFreeLate;
            }
        }
            
        default:
            break;
    }
    self.playDurationScore = score;

    return score;
}

/**
 *@description 获取弹奏力度的评分分数
 *@param dymicPress 按下力度
 *@return 弹奏力度对应分数
 **/
- (NSInteger)noteplayDimicWithValue:(NSInteger)dymicPress
{
    int offsetDym = 20;
    NSInteger score = 0;
    if (dymicPress >= (self.dynamics - offsetDym) && dymicPress <= (dymicPress + offsetDym)) {
        self.playDymic = NotePlayDymicStandard;
         score = 100;
    }else if (dymicPress >= (self.dynamics - 2 * offsetDym) && dymicPress <= (self.dynamics - offsetDym)) {
        self.playDymic = NotePlayDymicLight;
         score = 70;
    }else if (dymicPress <= (self.dynamics + 2 * offsetDym) && dymicPress >= (self.dynamics + offsetDym))
    {
        self.playDymic = NotePlayDymicHeavy;
         score = 70;
    }else if (dymicPress >= (self.dynamics - 3 * offsetDym) && dymicPress <= (self.dynamics - 2 * offsetDym))
    {
        self.playDymic = NotePlayDymicLight;
         score = 40;
    }else if (dymicPress <= (self.dynamics + 3 * offsetDym) && dymicPress >= (self.dynamics + 2 * offsetDym))
    {
        self.playDymic = NotePlayDymicHeavy;
    }else if (dymicPress > self.dynamics)
    {
        self.playDymic = NotePlayDymicHeavy;
    }else
    {
        self.playDymic = NotePlayDymicLight;
    }
    self.playDymicScore = score;
    return score;
}

/** 根据音阶，音名设置miditag **/
- (void)lx_calculateMidiTag{
    NSDictionary *dic = @{@"C":@(1),
                          @"D":@(3),
                          @"E":@(5),
                          @"F":@(6),
                          @"G":@(8),
                          @"A":@(10),
                          @"B":@(12)
                          };
    NSNumber *number = dic[self.step];
    NSInteger ret = (self.octave - 1)*12+23+number.integerValue+self.alter;
    self.midiTag = ret;
}

- (void)dealloc
{
//    debugLog(@"%s",__func__);
}

@end
