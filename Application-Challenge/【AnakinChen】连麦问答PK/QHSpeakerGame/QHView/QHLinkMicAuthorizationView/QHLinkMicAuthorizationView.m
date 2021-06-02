//
//  QHLinkMicAuthorizationView.m
//
//  Created by Anakin chen on 2020/2/24.
//  Copyright © 2020 ... All rights reserved.
//

#import "QHLinkMicAuthorizationView.h"

#import <AVFoundation/AVFoundation.h>

#import <Masonry/Masonry.h>

@interface QHLinkMicAuthorizationView ()

@property (nonatomic, copy) AuthorizationBlock authorizationBlock;

@property (nonatomic) AVAuthorizationStatus videoStatus;
@property (nonatomic) AVAuthorizationStatus audioStatus;

@property (nonatomic, strong) UIButton *closeBtn;
@property (nonatomic, strong) UIImageView *videoIV;
@property (nonatomic, strong) UIButton *videoBtn;
@property (nonatomic, strong) UIImageView *audioIV;
@property (nonatomic, strong) UIButton *audioBtn;

@end

@implementation QHLinkMicAuthorizationView

+ (instancetype)creatIn:(UIView *)superView authorization:(AuthorizationBlock)block {
    AVAuthorizationStatus videoStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    AVAuthorizationStatus audioStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    if (videoStatus == AVAuthorizationStatusAuthorized && audioStatus == AVAuthorizationStatusAuthorized) {
        block(YES);
        return nil;
    }
    
    QHLinkMicAuthorizationView *view = [QHLinkMicAuthorizationView new];
    view.authorizationBlock = block;
    view.videoStatus = videoStatus;
    view.audioStatus = audioStatus;
    [superView addSubview:view];
    [view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.bottom.right.equalTo(superView);
    }];
    [view setup];
    return view;
}

- (void)hiddenClose:(BOOL)bHidden {
    _closeBtn.hidden = bHidden;
}

#pragma mark - Private

- (void)setup {
    self.backgroundColor = [UIColor blackColor];
    [self p_addView];
    [self p_setupAuthorizationStatus:YES];
}

- (void)p_addView {
    UIView *contentV = [UIView new];
    contentV.backgroundColor = [UIColor clearColor];
    [self addSubview:contentV];
    
    UILabel *titleL = [UILabel new];
    titleL.backgroundColor = [UIColor clearColor];
    titleL.textColor = [UIColor whiteColor];
    titleL.font = [UIFont systemFontOfSize:20];
    titleL.text = @"允许以下权限可立刻进行游戏连麦";
    [contentV addSubview:titleL];
    [titleL mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.equalTo(contentV);
    }];
    
    UIImage *image = [UIImage imageNamed:@"qh_icon_album_select"];
    
    UIButton *videoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    videoBtn.backgroundColor = [UIColor clearColor];
    videoBtn.titleLabel.font = [UIFont systemFontOfSize:17];
    [videoBtn setTitle:@"开启相机权限" forState:UIControlStateNormal];
    [videoBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [videoBtn setTitleColor:[UIColor grayColor] forState:UIControlStateDisabled];
    [contentV addSubview:videoBtn];
    [videoBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(titleL.mas_bottom).mas_offset(76);
        make.centerX.equalTo(contentV);
    }];
    [videoBtn addTarget:self action:@selector(videoAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIImageView *videoIV = [[UIImageView alloc] initWithImage:image];
    [contentV addSubview:videoIV];
    [videoIV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(videoBtn);
        make.right.equalTo(videoBtn.mas_left).mas_offset(-16);
    }];
    
    UIButton *audioBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    audioBtn.backgroundColor = [UIColor clearColor];
    audioBtn.titleLabel.font = [UIFont systemFontOfSize:17];
    [audioBtn setTitle:@"开启麦克风权限" forState:UIControlStateNormal];
    [audioBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [audioBtn setTitleColor:[UIColor grayColor] forState:UIControlStateDisabled];
    [contentV addSubview:audioBtn];
    [audioBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(videoBtn.mas_bottom).mas_offset(42);
        make.centerX.equalTo(contentV);
    }];
    [audioBtn addTarget:self action:@selector(audioAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIImageView *audioIV = [[UIImageView alloc] initWithImage:image];
    [contentV addSubview:audioIV];
    [audioIV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(audioBtn);
        make.right.equalTo(audioBtn.mas_left).mas_offset(-16);
    }];
    
    [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self);
        make.width.equalTo(titleL);
        make.bottom.equalTo(audioBtn).mas_offset(20);
    }];
    
    UIButton *closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [closeBtn setImage:[UIImage imageNamed:@"qh_linkmic_icon_closed"] forState:UIControlStateNormal];
    [self addSubview:closeBtn];
    [closeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self).mas_offset(16);
        make.left.equalTo(self).mas_offset(36);
    }];
    [closeBtn addTarget:self action:@selector(closeAction) forControlEvents:UIControlEventTouchUpInside];
    
    _videoBtn = videoBtn;
    _videoIV = videoIV;
    _audioBtn = audioBtn;
    _audioIV = audioIV;
    _closeBtn = closeBtn;
}

- (void)p_setupAuthorizationStatus:(BOOL)bInit {
    if (_videoStatus == AVAuthorizationStatusAuthorized) {
        _videoBtn.enabled = NO;
    }
    else {
        _videoBtn.enabled = YES;
    }
    
    if (_audioStatus == AVAuthorizationStatusAuthorized) {
        _audioBtn.enabled = NO;
    }
    else {
        _audioBtn.enabled = YES;
    }
    
    _videoIV.hidden = _videoBtn.isEnabled;
    _audioIV.hidden = _audioBtn.isEnabled;
    
    if (bInit == NO) {
        [self p_removeView:bInit];
    }
}

- (void)p_removeView:(BOOL)bRemove {
    BOOL bAuthorization = NO;
    if (_videoStatus == AVAuthorizationStatusAuthorized && _audioStatus == AVAuthorizationStatusAuthorized) {
        bAuthorization = YES;
    }
    if (bRemove || bAuthorization) {
        [self removeFromSuperview];
        self.authorizationBlock(bAuthorization);
    }
}

- (void)p_authorization:(BOOL)bVideo {
    AVAuthorizationStatus status = (bVideo == YES ? _videoStatus : _audioStatus);
    if (status == AVAuthorizationStatusDenied) {
        NSURL * url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
        if ([[UIApplication sharedApplication] canOpenURL:url]) {
            [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
            }];
        }
    }
    else {
        AVMediaType type = (bVideo == YES ? AVMediaTypeVideo : AVMediaTypeAudio);
        [AVCaptureDevice requestAccessForMediaType:type completionHandler:^(BOOL granted) {
            if (granted == YES) {
                if (bVideo) {
                    self.videoStatus = AVAuthorizationStatusAuthorized;
                }
                else {
                    self.audioStatus = AVAuthorizationStatusAuthorized;
                }
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self p_setupAuthorizationStatus:NO];
                });
            }
            else {
                if (bVideo) {
                    self.videoStatus = AVAuthorizationStatusDenied;
                }
                else {
                    self.audioStatus = AVAuthorizationStatusDenied;
                }
            }
        }];
    }
}

#pragma mark - Action

- (void)videoAction {
    [self p_authorization:YES];
}

- (void)audioAction {
    [self p_authorization:NO];
}

- (void)closeAction {
    [self p_removeView:YES];
}

@end
