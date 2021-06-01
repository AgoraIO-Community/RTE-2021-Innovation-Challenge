//
//  LxMcNoteView.m
//  SmartPiano
//
//  Created by DavinLee on 2018/2/2.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcNoteView.h"
#import "CALayer+Default.h"
#import "LxMcStaffHeader.h"
#import "UIColor+Default.h"
#import "UIImage+RTTint.h"
#import "UIImage+Default.h"
#import "UIImageView+Default.h"
#import "UIView+Default.h"
#import "LxMcNoteView+Bubble.h"
#import <pop/POP.h>
#import "UIImage+Default.h"
#import "UIImage+YYAdd.h"
#import "NSString+Helper.h"
#import "NSDictionary+Helper.h"
#define kNoteBoeyOffsetY kMcStaffSpace * 0.3
@interface LxMcNoteView ()
/******************************************************音符部件***************************************************************/

@property (nonatomic, weak) CALayer *showLayer;

@end
@implementation LxMcNoteView

//- (instancetype)init {
//    if (self = [super init]) {
//        self.strength = 80;
//    }
//    return self;
//}

#pragma mark - SetMethod
- (void)setIsHeadOnLine:(BOOL)isHeadOnLine
{
    _isHeadOnLine = isHeadOnLine;
    if (_isDot) {
        self.layerDot.position = CGPointMake(self.layerNoteHead.position.x + kMcNoteHeadWidth * 0.73, self.layerNoteHead.position.y - (self.isHeadOnLine ? kMcNoteHeadHeight / 2.f : 0));
    }
}

- (void)setTempMajorType:(LxNoteTempMajorType)tempMajorType{
    if (self.isRest) {
        return;
    }
    
    _tempMajorType = tempMajorType;
    [_layerTempMajor removeFromSuperlayer];
    if (_layerTempMajor) {
        [_layerTempMajor removeFromSuperlayer];
        _layerTempMajor = nil;
    }
    switch (tempMajorType) {
        case LxNoteTempMajorNormal:
        {
            self.alter = 0;
        }
            break;
        case LxNoteTempMajorPit:{
            self.alter = -1;
            [self.layer addSublayer:self.layerTempMajor];
        }
            break;
        case LxNoteTempMajorShape:{
            self.alter = 1;
            [self.layer addSublayer:self.layerTempMajor];
        }
            break;
            
        default:
            break;
    }
    
}
#pragma mark - GetMethod
- (CALayer *)layerTempMajor{
    if (!_layerTempMajor) {
        _layerTempMajor = [CALayer layer];
        NSString *imageName ;
        CGFloat centerScaleY = 0;
        switch (_tempMajorType) {
            case LxNoteTempMajorShape:
            {
                imageName = @"write_GMajorTag_ruding@2x";
                centerScaleY = 0.5;
            }
                break;
                case LxNoteTempMajorPit:
            {
                imageName = @"write_FMajorTag_ruding@2x";
                centerScaleY = 0.7;
            }
                break;
                case LxNoteTempMajorNormal:
            {
                imageName = nil;
            }
                break;
            default:
                break;
        }
        if (imageName) {
            UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
            [_layerTempMajor lx_setImageScale:image];
            _layerTempMajor.position = CGPointMake(CGRectGetMinX(_layerNoteHead.frame) - image.size.width + 2, CGRectGetMidY(_layerNoteHead.frame) - (0.5 - centerScaleY) * image.size.height);
            
        }
    }
    return _layerTempMajor;
}

- (CALayer *)playStarLayer
{
    if (!_playStarLayer) {
        _playStarLayer = [CALayer layer];
        _playStarLayer.backgroundColor = [UIColor clearColor].CGColor;
        _playStarLayer.frame = CGRectZero;
        _playStarLayer.opacity = 0.0;
        [_playStarLayer lx_setImage:mImageByName(@"42_youxi_dongwu_liang@2x")];
        _playStarLayer.anchorPoint = CGPointMake(0.5, 0.5);
        [self.layer addSublayer:_playStarLayer];
    }
    return _playStarLayer;
}
- (CALayer *)layerNoteHead
{
    if (!_layerNoteHead) {
        _layerNoteHead = [CALayer layer];
        _layerNoteHead.backgroundColor = [UIColor clearColor].CGColor;
        _layerNoteHead.frame = CGRectZero;
        _layerNoteHead.anchorPoint = CGPointMake(0.5, 0.5);
        [self.layer addSublayer:_layerNoteHead];
    }
    return _layerNoteHead;
}
- (CALayer *)layerNoteBody
{
    if (!_layerNoteBody) {
        _layerNoteBody = [CALayer layer];
        if (!_isRest) {
            _layerNoteBody.backgroundColor = mRGBColor(41.f, 41.f, 41.f).CGColor;
        }
        _layerNoteBody.frame = CGRectMake(0, 0, kMcStaffNoteBodyRude, 3 * kMcStaffSpace + kNoteBoeyOffsetY);
        _layerNoteBody.anchorPoint = CGPointMake(0.5, 0.5);
        [self.layer addSublayer:_layerNoteBody];
    }
    return _layerNoteBody;
}

- (CAShapeLayer *)layerDot
{
    if (!_layerDot) {
        _layerDot = [CAShapeLayer layer];
        CGFloat height = kMcDotHeight;//kMcStaffNoteBodyRude * kMcStaffScale;
        _layerDot.frame = CGRectMake(0, 0, height, height);
        _layerDot.cornerRadius = height * 0.5;
        _layerDot.masksToBounds = YES;
     
        _layerDot.contents = (id)[UIImage imageWithColor:[UIColor blackColor] size:CGSizeMake(height, height)].CGImage;
        _layerDot.anchorPoint = CGPointMake(0.5, 0.5);
        [self.layer addSublayer:_layerDot];
        
    }
    return _layerDot;
}

-  (void)setIsDot:(BOOL)isDot {
    _isDot = isDot;

}

- (CALayer *)layerNoteBodyAuxiliary
{
    if (!_layerNoteBodyAuxiliary) {
        _layerNoteBodyAuxiliary = [CALayer layer];
        _layerNoteBodyAuxiliary.backgroundColor = [UIColor clearColor].CGColor;
        if (_noteType >= MusicNodeEighth && !_isRest && _noteType != MusicNodeLoading) {
            if (self.noteType > LxMcNoteEighth) {
                           [_layerNoteBodyAuxiliary lx_setImage:[[UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"write_staffNoteAuxiliary_%ld@2x",(long)_noteType]] rt_tintedImageWithColor:[UIColor blackColor]]];
            }else{
                           [_layerNoteBodyAuxiliary lx_setImageWithImageName:[NSString stringWithFormat:@"write_staffNoteAuxiliary_%ld@2x",(long)_noteType]];
            }

        }
        _layerNoteBodyAuxiliary.anchorPoint = CGPointMake(0, 0);
        [self.layer addSublayer:_layerNoteBodyAuxiliary];
    }
    return _layerNoteBodyAuxiliary;
}
/** 获取相对于曲谱的判断点 **/
- (CGPoint)judgePoint
{
    
    return CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
//    CGPoint headToCenterPoint = [self judgePointToCenter];
//    return CGPointMake(self.center.x + headToCenterPoint.x, self.center.y + headToCenterPoint.y);
}
/** 获取符头中心与图形中心的偏移 **/
- (CGPoint)judgePointToCenter
{
    CGPoint offsetPoint = CGPointZero;
    if (self.isRest || self.noteType == MusicNodeWhole) {
        return offsetPoint;/** 休止符统一以中心点为基准  **/
    }
//    switch (self.headDirection) {
//        case LxMcNoteHead_center:
//            offsetPoint = CGPointZero;
//            break;
//        case LxMcNoteHead_left_down:
//            offsetPoint = CGPointMake( 0,  kMcStaffSpace * 1.5f);
//            break;
//        case LxMcNoteHead_left_up:
//            offsetPoint = CGPointMake( 0, - kMcStaffSpace * 1.5f);
//            break;
//        case LxMcNoteHead_right_down:
//            offsetPoint = CGPointMake(0,  kMcStaffSpace * 1.5f);
//            break;
//        case LxMcNoteHead_right_up:
//            offsetPoint = CGPointMake(0, - kMcStaffSpace * 1.5f);
//            break;
//    }
    return offsetPoint;
}
 - (UIImageView *)selectedBoarderView
{
    if (!_selectedBoarderView) {
        _selectedBoarderView = [[UIImageView alloc] init];
        [_selectedBoarderView lx_imageViewWithImageName:@"write_whole_spectrum_ruding@2x"];
        _selectedBoarderView.center = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
        [self addSubview:_selectedBoarderView];
        _selectedBoarderView.userInteractionEnabled = NO;
        [_selectedBoarderView lx_zoomScale:[self.zoom_scale floatValue]
                    scalePriScaleDirection:LxViewScalePriCenter];
    }
    return _selectedBoarderView;
}

#pragma mark - CallFunction
- (NSString *)nodeTypeString{
    switch (self.noteType) {
        case MusicNodeWhole:
            return @"whole";
            break;
            case MusicNodeHalf:
            return @"half";
            break;
            case MusicNodeEighth:
            return @"eighth";
            break;
            case MusicNode16th:
            return @"16th";
            break;
        case MusicNodeQuarter:
            return @"quarter";
            break;
        default:
            return @"whole";
            break;
    }
}

- (CALayer *)getImageLayer
{
    CALayer *layer = [CALayer layer];
    layer.frame = self.bounds;
//    UIImage *bigImage = [[WeLibraryAPI sharedInstance] loadBundleFileWithPath:[NSString stringWithFormat:@"notePart_da_%ld",(long)self.type]];
    layer.contents  = self.layer.contents;//(id)[self.image CGImage];
    
    return layer;
}
/**
 *@description  获取音符viewSize
 **/
+ (CGSize)defaultNoteViewSize
{
//    return CGSizeMake(45, 100);// V1.0
    return CGSizeMake(58.f / 49.f * kMcStaffSpace, kMcStaffSpace);
}
/**
 *@description 检查音符方向并更改
 *@return 是否有方向修改
 **/
- (BOOL)checkDirection
{
    LxMcNoteHeadDirection direction;
   
    if (self.isUpClef) {
        if (self.miditag >= 71) {
            direction = LxMcNoteHead_right_up;
        }else
        {
            direction = LxMcNoteHead_left_down;
        }
    }else
    {
        if (self.miditag >= 50) {
            direction = LxMcNoteHead_right_up;
        }else
        {
            direction = LxMcNoteHead_left_down;
        }
    }
    if (_isRest || _noteType == LxMcNoteWhole) {
        direction = LxMcNoteHead_center;
    }
    if (direction != self.headDirection && self.noteType != MusicNodeWhole) {
        self.headDirection = direction;
        [self relayoutStaffUI];
        return YES;
    }else
    {
        return NO;
    }
}
- (void)createStaffUI
{
    self.additionStaffLineCount = 0;
    CGRect frame = self.frame;
    self.image = nil;
    frame.size = [LxMcNoteView defaultNoteViewSize];
   
    self.frame = frame;
    

    self.playStarLayer.opacity = 0;
    self.playStarLayer.position = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
    if (self.isRest) {
        [self.layerNoteHead setScaleImage:[NSString stringWithFormat:@"write_staffNote_%ld_rest@2x",(long)self.noteType]];
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
        //单独判断(2分附点休止符)
        if (self.noteType == MusicNodeHalf && self.isDot ) {
              [self.layerNoteHead setScaleImage:@"write_staffNote_2_dot_rest@2x"];
        }
    }else if (self.noteType >= 50 && self.noteType <= 500 && self.noteType != MusicNodeLoading && !self.isABC) {
        [self.layerNoteHead setScaleImage:[NSString stringWithFormat:@"write_strength_%ld@2x", (long)self.noteType]];
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame) / 2.f);
    }else if(self.noteType == MusicNodeLoading && !self.isABC)
    {
        [self.layerNoteHead setScaleImage:@"write_quaterHead@2x"];
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f + 1.5 * kMcStaffSpace);
        [self relayoutStaffUI];
        
    }else if(self.noteType == MusicNodeWhole && !self.isABC)
    {
        
        UIImage *headImage = [UIImage imageNamed:@"write_quaterHead@2x"];
            [self.layerNoteHead lx_setImage:headImage];
//        self.headDirection = LxMcNoteHead_center;
//        [self.layerNoteHead setScaleImage:@"write_wholeNote@2x"];
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame) / 2.f);
        [self relayoutStaffUI];
    }else if(self.noteType == MusicNodeShowABC || self.isABC){
        //双模式作曲游戏
        switch (self.noteType) {
            case MusicNodeShowABC:{
                //音名 C D E F G A B
                [self.layerNoteHead setScaleImage:[NSString stringWithFormat:@"%@_%@@2x", self.isTouchABC? @"move":@"touch",self.ABCName]];
                self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame) / 2.f);
                }
                break;

            default:{
                [self.layerNoteHead setScaleImage:[NSString stringWithFormat:@"move_%ld@2x",(long)self.noteType]];
                self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame) / 2.f);
            }
                break;
        }
    }else
    {
//        if(self.noteType == MusicNodeHalf ) {
//            [self.layerNoteHead setScaleImage:@"write_halfHead@2x"];
//        }else {
        
    
        UIImage *headImage = [[UIImage imageNamed:@"write_quaterHead@2x"] rt_tintedImageWithColor:kMcStaffLineColor level:1];
            [self.layerNoteHead lx_setImage:headImage];
//        }

//        if (_noteType >= MusicNodeEighth && !_isRest && _noteType != MusicNodeLoading) {
//            if (self.noteType > LxMcNoteEighth) {
//                [_layerNoteBodyAuxiliary lx_setImage:[[UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"write_staffNoteAuxiliary_%ld@2x",(long)_noteType]] rt_tintedImageWithColor:[UIColor blackColor]]];
//            }else{
//                [_layerNoteBodyAuxiliary lx_setImageWithImageName:[NSString stringWithFormat:@"write_staffNoteAuxiliary_%ld@2x",(long)_noteType]];
//            }
//        }
        
//        self.layerNoteBody.backgroundColor = [UIColor blackColor].CGColor;
//        self.layerNoteBody.opacity = 1.0;
//        self.layerNoteBody.contents = nil;
//        self.layerNoteBody.frame = CGRectMake(0, 0, kMcStaffNoteBodyRude, 3 * kMcStaffSpace + kNoteBoeyOffsetY);
        [self relayoutStaffUI];
    }
    

    
    //为loading音符不需要显示无关UI
    BOOL isshow = self.noteType == MusicNodeLoading? YES:NO;
    _layerNoteBody.hidden =
    _layerDot.hidden =
    _layerNoteBodyAuxiliary.hidden =
    _playStarLayer.hidden = isshow;
    
    
    _layerDot.hidden = !_isDot;
    
    if (self.noteType == MusicNodeWhole) {
        _layerNoteBody.hidden = YES;
    }
    
    //二分附点休止符不加附点layer
    if (self.noteType == MusicNodeHalf && self.isDot && self.isRest) {
        _layerDot.hidden = YES;
    }
    
    [self lx_resetSelecteState:NO];
}

- (void)createCandyNote {
//    if (self.isRest) {
//        return;
//    }
    self.additionStaffLineCount = 0;
    CGRect frame = self.frame;
    self.image = nil;
    frame.size = [LxMcNoteView defaultNoteViewSize];
    self.frame = frame;
    self.playStarLayer.opacity = 0;
    if (self.isRest) {
        [self.layerNoteHead setScaleImage:[NSString stringWithFormat:@"write_staffNote_%ld_rest@2x",(long)self.noteType]];
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
    }else if (self.noteType == MusicNodeWhole) {
        [self.layerNoteHead setScaleImage:@"write_candy_halfHead@2x"];
        self.headDirection = LxMcNoteHead_center;
        self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame) / 2.f, CGRectGetHeight(self.frame) / 2.f);
        self.layerNoteBody.backgroundColor = [UIColor clearColor].CGColor;
        [self relayoutStaffUI];
    }else if (self.noteType == MusicNodeLoading){
        [self.layerNoteHead setScaleImage:@"write_candy_fourHead@2x"];
        self.layerNoteBody.backgroundColor = [UIColor clearColor].CGColor;
        self.layerNoteBody.frame = CGRectMake(0, 0, kMcStaffNoteBodyRude, 3 * kMcStaffSpace + kNoteBoeyOffsetY);
    }else {
        if (self.noteType == MusicNodeHalf) {
            [self.layerNoteHead setScaleImage:@"write_candy_halfHead@2x"];
        }else
        {
            [self.layerNoteHead setScaleImage:@"write_candy_fourHead@2x"];
        }
        [self.layerNoteBody setScaleImage:@"write_candy_body@2x"];
        self.layerNoteBody.backgroundColor = [UIColor clearColor].CGColor;
        self.layerNoteBody.frame = CGRectMake(0, 0, kMcStaffNoteBodyRude, 3 * kMcStaffSpace + kNoteBoeyOffsetY);
    }
    if (self.isDot) {
        self.layerDot.contents = (id)[mImageByName(@"write_point") CGImage];
        self.layerDot.path = nil;
    }
    [self relayoutStaffUI];
}

- (void)setNodeType:(MusicNodeType)nodeType {
    if (self.noteType == MusicNodeLoading &&
        nodeType == MusicNodeWhole) {/**   四分音符切换为全音符时调整位置    **/
        self.centerY += [self judgePointToCenter].y;
        self.headDirection = LxMcNoteHead_center;
    }
    _noteType = nodeType;
    [self createStaffUI];
    //糖果状态下 切换对应音符
    [self changeCandyUI:_candy];
    switch (nodeType) {
        case MusicNodeLoading:
            _layerNoteBody.opacity = 0;
            break;
        case MusicNodeWhole:
            _layerNoteBody.opacity = 0;
            break;
        default:
            _layerNoteBody.opacity = 1;
            break;
    }
}

- (void)changeCandyUI:(BOOL)candy {
    if (self.noteType >= MusicNodeStrengthPP && self.noteType != MusicNodeLoading) {
        return;
    }
    self.layerDot.opacity = self.isDot;
    self.candy = candy;
    if (candy) {
        [self createCandyNote];
    }else {
        [self createStaffUI];
        if (self.isDot) {
            self.layerDot.contents = (id)[UIImage imageWithColor:[UIColor blackColor] size:CGSizeMake(kMcDotHeight, kMcDotHeight)].CGImage;
        }
    }
}

- (void)clearAllStaffUI
{
    [_layerNoteHead removeFromSuperlayer];
    _layerNoteHead = nil;
    [_layerNoteBody removeFromSuperlayer];
    _layerNoteBody = nil;
    [_layerDot removeFromSuperlayer];
    _layerDot = nil;
    [_layerNoteBodyAuxiliary removeFromSuperlayer];
    _layerNoteBodyAuxiliary = nil;
    _isUpClef = NO;
    _additionStaffLineCount = 0;
    _rightHeadPosition = CGPointZero;
    _isHeadOnLine = NO;
    _isLocatedStaff = NO;
    
    _miditag = 0;
    _alter = 0;
}
/**
 *@description 获取当前音符所在位置的正确上加。下加线数量
 **/
- (NSInteger)caculateRightAdditionStaffLineCount
{
    if (self.isUpClef) {
        switch (self.miditag) {
            case 81:
            case 82://黑键
            case 83:
            {
                return -1;
            }
                break;
            case 84:
            case 85://黑键
            {
                return -2;
            }
                break;
            case 59:
            case 60:
            case 61://黑键
            {
                return 1;
            }
                break;
            case 58://黑键
            case 57:
            {
                return 2;
            }
                break;
            default:
            {
                return 0;
            }
                break;
        }
    }else
    {
        switch (self.miditag) {
            case 60:
            case 61://黑键
            case 62:
            {
                return -1;
            }
                break;
            case 63://黑键
            case 64:
            {
                return -2;
            }
                break;
            case 40:
            case 39://黑键
            case 38:
            {
                return 1;
            }
                break;
                
            case 37:
            {
                return 2;
            }
                break;
            default:
            {
                return 0;
            }
                break;
        }
    }
}


/**
 *@description 获取音符所占拍数，暂时只管4拍
 **/
- (CGFloat)durationUnit
{
    //如果是等待音符 直接按默认四分音符输出
    if (self.noteType == MusicNodeLoading ) {
        return 1.f / MusicNodeQuarter;
    }
    
    CGFloat durationUnit = 1.f / self.noteType;
    if (self.isDot) {
        durationUnit *= 1.5;
    }
    
    return durationUnit;
}
/**
 *@description 显示播放效果
 **/
- (void)showPlayState:(BOOL)playState candy:(BOOL)candy
{
    if (candy) {//糖果音符效果
        [self.playStarLayer lx_setImage:[UIImage imageNamed:@"42_youxi_dongwu_liang@2x"]];
        self.playStarLayer.position = self.layerNoteHead.position;
        POPBasicAnimation *ani = [POPBasicAnimation animationWithPropertyNamed:kPOPLayerOpacity];
        ani.fromValue = @(self.playStarLayer.opacity);
        ani.toValue = @((CGFloat)playState);
        [self.playStarLayer pop_addAnimation:ani forKey:@"alpha"];
        ckWeakSelf
        [ani setCompletionBlock:^(POPAnimation *anim, BOOL finished) {
            [weakSelf.playStarLayer pop_removeAllAnimations];
        }];
    }else {
        
        [self.playStarLayer setScaleImage:@"write_halo_ruding@2x"];
        self.playStarLayer.position = self.layerNoteHead.position;
//        self.playStarLayer.frame = CGRectInset(self.layerNoteHead.frame, -5, -5);
        self.playStarLayer.opacity = playState ? 1 : 0;
        
        UIColor *color = playState ? [UIColor lx_rbgColorWithR:242.f G:230.f B:119.f alpha:1] : kMcStaffLineColor;
        if (self.isRest) {
            self.layerNoteBody.opacity = 0;
            if (self.noteType == MusicNodeHalf&&self.isDot) {
                [self.layerNoteHead lx_setImageScale:[[UIImage imageNamed:@"write_staffNote_2_dot_rest@2x"] rt_tintedImageWithColor:color level:1]];
            }else{
                [self.layerNoteHead lx_setImageScale:[[UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"write_staffNote_%ld_rest@2x",self.noteType]] rt_tintedImageWithColor:color level:1]];
            }
        }else
        {
            if (self.noteType == MusicNodeHalf) {
                [self.layerNoteHead lx_setImageScale:[[UIImage lx_imageFromBundleWithName:@"write_halfHead@2x"] rt_tintedImageWithColor:color level:1]];
            }
            else if(self.noteType == MusicNodeWhole)
            {
                self.layerNoteBody.opacity = 0;
                [self.layerNoteHead lx_setImageScale:[[UIImage lx_imageFromBundleWithName:@"write_wholeNote@2x"] rt_tintedImageWithColor:color level:1]];
            }
            else
            {
                [self.layerNoteHead lx_setImage:[[UIImage lx_imageFromBundleWithName:@"write_quaterHead@2x"] rt_tintedImageWithColor:color level:1]];
                if (self.noteType == LxMcNoteEighth) {
                    [self.layerNoteBodyAuxiliary lx_setImageScale:[[UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"write_staffNoteAuxiliary_%ld@2x",(long)_noteType]] rt_tintedImageWithColor:color level:1]];
                    [self relayoutStaffUI];
                }
            }
            if (self.isDot) {
                _layerDot.contents = (id)[UIImage imageWithColor:color size:CGSizeMake(kMcDotHeight, kMcDotHeight)].CGImage;
            }
        }
//        self.layerNoteBody.backgroundColor = color.CGColor;
//        self.layerNoteBody.backgroundColor = color.CGColor;
    }
    
}

- (NSString *)reSetImageName:(NSString *)noteName{
    NSString *tempName = [noteName copy];

    return tempName;
}

- (void)setEventDuration:(CGFloat)eventDuration {
    _eventDuration = eventDuration;
}

#pragma mark - Function
- (void)relayoutStaffUI {
    switch (self.headDirection) {
        case LxMcNoteHead_left_down:
        {
            [CATransaction begin];
            [CATransaction setAnimationDuration:0];
//            self.layerNoteHead.position = CGPointMake(CGRectGetWidth(self.frame)/2.f, CGRectGetHeight(self.frame)/2.f);
            self.layerNoteHead.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetWidth(self.frame));
//            self.layerNoteBody.position = CGPointMake(CGRectGetMaxX(self.layerNoteHead.frame) - kMcStaffNoteBodyRude, CGRectGetHeight(self.frame)  / 2.f - 0.5 * kMcStaffSpace + kNoteBoeyOffsetY / 2.f);
            self.layerDot.position = CGPointMake(self.layerNoteHead.position.x + kMcNoteHeadWidth * 0.73, self.layerNoteHead.position.y - (self.isHeadOnLine ? kMcNoteHeadHeight / 2.f : 0));
//            self.layerNoteBodyAuxiliary.transform = CATransform3DMakeScale(1, 1, 1);
//            self.layerNoteBodyAuxiliary.position = CGPointMake(CGRectGetMaxX(self.layerNoteBody.frame), CGRectGetMinY(self.layerNoteBody.frame));
            self.playStarLayer.position = self.layerNoteHead.position;
            [CATransaction commit];
            
        }
            break;
            case LxMcNoteHead_left_up:
        {
            self.layerNoteHead.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetWidth(self.frame));
//            self.layerNoteBody.position = CGPointMake(CGRectGetMaxX(self.layerNoteHead.frame) - kMcStaffNoteBodyRude, CGRectGetHeight(self.frame) / 2.f - 0.5 * kMcStaffSpace - kNoteBoeyOffsetY / 2.f);
        }
            break;
        case LxMcNoteHead_right_up:
        {
            [CATransaction begin];
            [CATransaction setAnimationDuration:0];
            self.layerNoteHead.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetWidth(self.frame));
//            self.layerNoteBody.position = CGPointMake(CGRectGetMinX(self.layerNoteHead.frame) + kMcStaffNoteBodyRude, CGRectGetHeight(self.frame) / 2.f + 0.5 * kMcStaffSpace - kNoteBoeyOffsetY / 2.f);
            self.layerDot.position = CGPointMake(self.layerNoteHead.position.x + kMcNoteHeadWidth * 0.73, self.layerNoteHead.position.y - (self.isHeadOnLine ? kMcNoteHeadHeight / 2.f : 0));
//            self.layerNoteBodyAuxiliary.transform = CATransform3DMakeScale(1, -1, 1);
//             self.layerNoteBodyAuxiliary.position = CGPointMake(CGRectGetMaxX(self.layerNoteBody.frame), CGRectGetMaxY(self.layerNoteBody.frame) );
            self.playStarLayer.position = self.layerNoteHead.position;
            [CATransaction commit];
        }
            break;
        case LxMcNoteHead_right_down:
        {
            self.layerNoteHead.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetWidth(self.frame));
//             self.layerNoteBody.position = CGPointMake(CGRectGetMinX(self.layerNoteHead.frame) + kMcStaffNoteBodyRude, CGRectGetHeight(self.frame) / 2.f - 0.5 * kMcStaffSpace + kNoteBoeyOffsetY / 2.f);
        }
            break;
        default:
            break;
    }

}

- (void)dealloc
{
    debugLog(@"%s",__func__);
}

- (CALayer *)showLayer {
    if (!_showLayer) {
        CALayer *showLayer = [[CALayer alloc]init];
        showLayer.opacity = 0;
//        showLayer.anchorPoint = CGPointMake(0.5, 0.5);
        [showLayer lx_setImage:[UIImage imageNamed:@"42_youxi_dongwu_liang@2x"]];
        [self.layer insertSublayer:showLayer below:self.layerNoteHead];
        _showLayer = showLayer;
    }
    return _showLayer;
}

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
{
    if (CGRectContainsPoint(CGRectMake(self.x - 10.f, self.y - 10.f, CGRectGetWidth(self.frame ) + 20.f, CGRectGetHeight(self.frame) + 20), point) ||
        CGRectContainsPoint(self.frame, point)) {
        return self;
    }
    else{
        return [super hitTest:point withEvent:event];
    }
}

- (NSString *)lx_Json{
    NSString *infoStr = nil;
    NSDictionary *info = @{
        @"noteType" : @(self.noteType),
        @"isUpClef" : @(self.isUpClef),
        @"miditag"  : @(self.miditag),
    };
    
    infoStr = [info lx_JsonString];
    return infoStr;
}

+ (LxMcNoteView *)lx_noteViewWithDic:(NSDictionary *)info{
    LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:[info[@"noteType"] integerValue]
                                                                   isRest:NO
                                                                    isDot:NO];
    noteView.isUpClef = [info[@"isUpClef"] boolValue];
    noteView.miditag = [info[@"miditag"] integerValue];
    return noteView;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
