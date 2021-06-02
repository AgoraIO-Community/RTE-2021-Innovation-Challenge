//
//  QHRTCKitManager.m
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/14.
//  Copyright © 2020 ... All rights reserved.
//

#import "QHRTCKitManager.h"

#import "QHAGRTCKit.h"

@interface QHRTCKitManager () <QHRTCBaseKitDelegate>

@property (nonatomic, strong) QHRTCBaseKit *kit;

@property (nonatomic, strong) NSDictionary *configDic;
@property (nonatomic, strong) NSMutableDictionary *userInfoDic;
@property (nonatomic, strong) NSArray *micOrder;

@property (nonatomic, weak) id<QHRTCKitManagerDelegate> delegate;

@end

@implementation QHRTCKitManager

- (void)dealloc {
    _kit = nil;
    _configDic = nil;
    _userInfoDic = nil;
    
    _streamUrl = nil;
    _liveConfig = nil;
    #if DEBUG
        NSLog(@"%s", __FUNCTION__);
    #endif
}

+ (instancetype)create:(QHRTCKitType)type superV:(UIView *)view config:(id)cfg delegate:(id<QHRTCKitManagerDelegate>) delegate {
    QHRTCKitManager *this = [QHRTCKitManager new];
    this.delegate = delegate;
    this.liveConfig = cfg;
    [this p_setup:type superV:view];
    return this;
}

#pragma mark - Public

- (BOOL)checkAndSetConfig:(NSDictionary *)dic {
    [self p_logInfo:[NSString stringWithFormat:@"%s-%@", __func__, dic]];
    _configDic = [dic copy];
    return [_kit config:_configDic];
}

- (BOOL)join {
    [self p_logInfo:[NSString stringWithFormat:@"%s", __func__]];
    _kit.bPush = NO;
    return [_kit join];
}

- (void)leave {
    [self p_logInfo:[NSString stringWithFormat:@"%s", __func__]];
    [_kit leave];
}

- (BOOL)switchCamera {
    [self p_logInfo:[NSString stringWithFormat:@"%s", __func__]];
    return [_kit switchCamera];
}

//- (BOOL)push {
//    [self p_logInfo:[NSString stringWithFormat:@"%s", __func__]];
//    return [_kit push];
//}

- (void)insertUserInfos:(NSDictionary<id, NSString *> *)dic {
    [self.userInfoDic addEntriesFromDictionary:dic];
    [_kit refreshNickName];
}

- (void)refreshMicOrder:(NSArray *)micOrder {
    self.micOrder = [micOrder copy];
    [_kit refreshLayer];
}

- (void)enterBackground:(BOOL)bEnter {
    [_kit enterBackground:bEnter];
}

- (void)open4AudioCallback:(BOOL)bOpen {
    _kit.bAudioCallback = bOpen;
}

#pragma mark - QHRTCKitSourceProtocol

- (void)logInfo:(NSString *)log {
    [self p_logInfo:log];
}
- (id)getLiveConfig {
    return self.liveConfig;
}
- (NSDictionary *)getUserInfoDic {
    return _userInfoDic;
}
- (NSArray *)getMicOrderArr {
    return _micOrder;
}
- (CGFloat)getPreViewTopY {
    return _preViewTopY;
}
- (BOOL)isLandspace {
    return NO;
}

#pragma mark - Private

- (void)p_setup:(QHRTCKitType)type superV:(UIView *)view {
    _userInfoDic = [NSMutableDictionary new];
    if (type == QHRTCKitTypeAG) {
        _kit = [QHAGRTCKit createWith:view];
    }
    NSInteger cd = 1;//YES ? 1 : 0;
    [_kit setupWithLiveConfig: @{@"cameraDirection": @(cd)}];
    _kit.sourceProtocol = self;
    _kit.delegate = self;
    _preViewTopY = -1.f;
}

- (void)p_logInfo:(NSString *)log {
    if ([self.delegate respondsToSelector:@selector(logRTCKitManager:content:)]) {
        BOOL bShow = [self.delegate logRTCKitManager:self content:log];
        if (bShow) {
            NSLog(@"agrtc>%@", log);
        }
    }
    else {
        #if DEBUG
        NSLog(@"agrtc>%@", log);
        #endif
    }
}

#pragma mark - QHRTCBaseKitDelegate

- (void)getAudioKit:(id)kit buffer:(CMSampleBufferRef)buffer {
    [self.delegate getAudioRTCKitManager:self buffer:buffer];
}

@end
