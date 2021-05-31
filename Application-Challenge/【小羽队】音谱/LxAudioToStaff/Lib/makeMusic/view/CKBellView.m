//
//  CKBellView.m
//  SmartPiano
//
//  Created by xy on 2018/5/24.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "CKBellView.h"
#import "CKMixAudioTool.h"

#define phonographHeight 53

@interface CKBellView ()

/** 三册动物 **/
@property (nonatomic,weak) UIImageView *animalView;
/** 三册喇叭 **/
@property (nonatomic, weak) UIImageView *trumpetView;
/**
 左边的铃铛抖动
 */
@property (nonatomic, weak) UIImageView *moveBellLeft;

/**
 右边的铃铛抖动
 */
@property (nonatomic, weak) UIImageView *moveBellRight;
/**
 冰块
 */
@property (nonatomic, weak) UIImageView *iceImageView;

/**
 一册留声机的杆子
 */
@property (nonatomic, weak) UIImageView *poleView;

/**
 一册留声机
 */
@property (nonatomic, weak) UIImageView *phonographView;

@property (nonatomic, strong) CKMixAudioTool *audioTool;

@end

@implementation CKBellView

- (instancetype)init {
    if (self = [super init]) {
        self.size = CGSizeMake(120, 110);
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(playPauseTap)];
        [self addGestureRecognizer:tap];
        [self initUI];
        [self playPauseTap];
    }
    return self;
}

- (void)initUI {
    CGPoint center = CGPointMake(self.width * 0.5, self.height * 0.5);

        self.center = CGPointMake(mScreenWidth * 0.5 - 30, 130);
        self.phonographView.center = CGPointMake(center.x, center.y + 10);
        self.poleView.center = CGPointMake(center.x - 75, center.y - 15);
        //暂时不需要
//        UIImageView *handImageView = [[UIImageView alloc] initWithImage:mImageByName(@"write_hands")];
//        handImageView.center = CGPointMake(center.x - 75, center.y - 25);
//        [self addSubview:handImageView];
    
}

- (void)bellMove {
    [UIView animateWithDuration:0.5 delay:0 options:0  animations:^{
        self.moveBellLeft.transform = CGAffineTransformMakeRotation(-0.1);
        self.moveBellRight.transform = CGAffineTransformMakeRotation(-0.1);
    } completion:^(BOOL finished){
        [UIView animateWithDuration:0.5 delay:0 options:UIViewAnimationOptionRepeat|UIViewAnimationOptionAutoreverse  animations:^{
            self.moveBellLeft.transform = CGAffineTransformMakeRotation(0.1);
            self.moveBellRight.transform = CGAffineTransformMakeRotation(0.1);
        } completion:^(BOOL finished) {
        }];
    }];
}

- (void)bellEnd {
    [self.moveBellRight.layer removeAllAnimations];
    [self.moveBellLeft.layer removeAllAnimations];
}
- (void)phonographPlay {
    [UIView animateWithDuration:0.25 animations:^{
        self.poleView.transform = CGAffineTransformMakeRotation(-M_2_PI);
    }];
}

- (void)phonographStop {
    [UIView animateWithDuration:0.25 animations:^{
        self.poleView.transform = CGAffineTransformMakeRotation(0);
        
    }];
}

- (void)phonographPosition:(BOOL)positionOut {
    [UIView animateWithDuration:0.25 animations:^{
        if (self.audioPlayed) {
            self.phonographView.height = phonographHeight - 3;
            
        }else {
            self.phonographView.height = phonographHeight - 15;
        }
    }];
}

- (void)play:(void(^)(void))playEndBlock{
    self.playing = YES;
    self.audioTool = [[CKMixAudioTool alloc] init];
    ckWeakSelf
    [self.audioTool playAudio:self.path completed:^{
        [weakSelf bellEnd];
        weakSelf.playing = NO;
        if (playEndBlock) {
            playEndBlock();
        }
    }];
    self.audioTool.volume = self.audioPlayed ? 0.5 : 0.0;
    if (self.audioPlayed) {

            [self phonographPlay];
    }
    
}

- (void)stop {
    [self.audioTool stop];
    self.playing = NO;

        [self phonographStop];
}

#pragma mark  --  events respone
- (void)playPauseTap {
    self.audioPlayed = !self.audioPlayed;
    self.audioTool.volume = self.audioPlayed ? 0.5 : 0.0;

        if (self.playing) {
            if (self.audioPlayed) {
                [self phonographPosition:self.audioPlayed];
                [self phonographPlay];
            }else {
                [self phonographStop];
            }
        }else {
            [self phonographPosition:self.audioPlayed];
        }
}

#pragma mark --  getter
- (UIImageView *)moveBellLeft {
    if (!_moveBellLeft) {
        UIImageView *bellView = [[UIImageView alloc] initWithImage:mImageByName(@"write_obbligato02_ruding")];
        [self addSubview:bellView];
        _moveBellLeft = bellView;
        _moveBellLeft.hidden = YES;
    }
    return _moveBellLeft;
}

- (UIImageView *)moveBellRight {
    if (!_moveBellRight) {
        UIImageView *bellView = [[UIImageView alloc] initWithImage:mImageByName(@"write_obbligato04_ruding")];
        [self addSubview:bellView];
        _moveBellRight = bellView;
        _moveBellRight.hidden = YES;
    }
    return _moveBellRight;
}

- (UIImageView *)animalView {
    if (!_animalView) {
        UIImageView *bellView = [[UIImageView alloc] initWithImage:mImageByName(@"write_bell_rudin2")];
        [self addSubview:bellView];
        _animalView = bellView;
    }
    return _animalView;
}

- (UIImageView *)trumpetView {
    if (!_trumpetView) {
        UIImageView *bellView = [[UIImageView alloc] initWithImage:mImageByName(@"write_ice_rudin2")];
        [self addSubview:bellView];
        _trumpetView = bellView;
    }
    return _trumpetView;
}

- (UIImageView *)iceImageView {
    if (!_iceImageView) {
        UIImageView *iceView = [[UIImageView alloc] initWithImage:mImageByName(@"write_ice_ruding")];
        [self addSubview:iceView];
        _iceImageView = iceView;
    }
    return _iceImageView;
}

- (UIImageView *)poleView {
    if (!_poleView) {
        UIImageView *poleView = [[UIImageView alloc] initWithImage:mImageByName(@"write_aid")];
        poleView.layer.anchorPoint = CGPointZero;
        [self addSubview:poleView];
        _poleView = poleView;
    }
    return _poleView;
}

- (UIImageView *)phonographView {
    if (!_phonographView) {
        UIImageView *bellImageView = [[UIImageView alloc] initWithImage:mImageByName(@"write_bell")];
        bellImageView.contentMode = UIViewContentModeBottom;
        bellImageView.clipsToBounds = YES;
        bellImageView.height -= 15;
        [self addSubview:bellImageView];
        _phonographView = bellImageView;
    }
    return _phonographView;
}

- (NSString *)path {
    if (!_path) {
        _path = [[NSBundle mainBundle] pathForResource:@"MMaccompany1" ofType:@"mp3"];
    }
    return _path;
}

@end
