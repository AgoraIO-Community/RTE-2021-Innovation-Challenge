//
//  QHAGRTCKit.m
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/14.
//  Copyright © 2020 ... All rights reserved.
//

#import "QHAGRTCKit.h"

#import <AgoraRtcKit/AgoraRtcEngineKit.h>
#import <Masonry/Masonry.h>

#import "AgoraMediaDataPlugin.h"
#import "QHAppContext.h"

#define kVideoViewTag 888

#define kTestUidsCount self.allUids.count
#define kTest4Self NO

@interface QHAGRTCKit () <AgoraRtcEngineDelegate, AgoraVideoDataPluginDelegate, AgoraAudioDataPluginDelegate>

@property (nonatomic, strong) NSString *token;
@property (nonatomic) NSUInteger hostId;
@property (nonatomic, strong) NSString *nickName;

@property (nonatomic, strong) AgoraRtcEngineKit *agoraKit;
@property (nonatomic) AgoraClientRole clientRole;
@property (nonatomic, strong) AgoraLiveTranscoding *transcoding;
@property (nonatomic, strong) AgoraMediaDataPlugin *agoraMediaDataPlugin;

@property (nonatomic, strong) UIView *contentV;
@property (nonatomic, strong) NSMutableArray<NSNumber *> *allUids;
@property (nonatomic, strong) NSMutableDictionary<NSNumber *, UIView *> *allVideos;

@property (nonatomic) BOOL bEnterBackground;
@property (nonatomic, strong) NSTimer *retryTimer;

@end

@implementation QHAGRTCKit

- (void)dealloc {
    [self p_closeTimer4RetryAddPublishStreamUrl];
    #if DEBUG
        NSLog(@"%s", __FUNCTION__);
    #endif
}

#pragma mark - QHRTCKitProtocol

+ (instancetype)createWith:(UIView *)superV {
    QHAGRTCKit *this = [QHAGRTCKit new];
    this.contentV = [UIView new];
    this.contentV.backgroundColor = [UIColor clearColor];
    [superV addSubview:this.contentV];
    [this.contentV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.bottom.right.equalTo(superV);
    }];
    return this;
}

- (void)setupWithLiveConfig:(NSDictionary *)cfgDic {
    [self p_setup:cfgDic];
}

- (BOOL)config:(NSDictionary *)dic {
    NSString *token = dic[@"token"];
    NSUInteger hostId = [dic[@"hostId"] unsignedIntegerValue];
    NSUInteger userId = [dic[@"userId"] unsignedIntegerValue];
    NSString *channel = dic[@"channel"];
    NSString *nickName = dic[@"nickName"];
    self.token = token;
    self.hostId = hostId;
    self.userId = userId;
    self.channel = channel;
    self.nickName = nickName;
    
    return YES;
}

- (BOOL)join {
    return [self p_joinChannel];
}

- (void)leave {
    [self p_leaveChannel];
}

- (void)addPush:(NSString *)streamUrl {
}

- (BOOL)push {
    return NO;
}

- (void)unpush {
//    [self p_addPush:nil];
}

- (BOOL)switchCamera {
    return [_agoraKit switchCamera] == 0 ? YES : NO;
}

- (void)refreshNickName { }

- (void)refreshLayer {
    if (self.state != QHRTCKitStateIdle) {
        [self p_previewLayer];
        if (self.bPush) {
            [self p_pushLayer];
            [self.agoraKit setLiveTranscoding:self.transcoding];
        }
    }
}

- (void)enterBackground:(BOOL)bEnter {
    _bEnterBackground = bEnter;
    [_agoraKit muteLocalVideoStream:bEnter];
}

#pragma mark - Private

- (void)p_setup:(NSDictionary *)cfgDic {
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayAndRecord
             withOptions:AVAudioSessionCategoryOptionDefaultToSpeaker
                   error:nil];
    [session setActive:YES error:nil];
    _allUids = [NSMutableArray new];
    _allVideos = [NSMutableDictionary new];
    self.state = QHRTCKitStateIdle;
    _bEnterBackground = NO;
    
    [self p_initializeAgoraEngine:cfgDic];
    [self p_initializeMediaDataPlugin];
    [self p_previewLayer];
}

- (void)p_initializeAgoraEngine:(NSDictionary *)cfgDic {
    // 输入 App ID 并初始化 AgoraRtcEngineKit 类。
    self.agoraKit = [AgoraRtcEngineKit sharedEngineWithAppId:[QHAppContext context].AGAppId delegate:self];
    
    [self.agoraKit setChannelProfile:AgoraChannelProfileLiveBroadcasting];
    
    self.clientRole = AgoraClientRoleBroadcaster;
    // 设置用户角色。
    [self.agoraKit setClientRole:self.clientRole];
    
    AgoraBeautyOptions *options = [[AgoraBeautyOptions alloc] init];
    options.lighteningContrastLevel = AgoraLighteningContrastNormal;
    options.rednessLevel = 0.5;
    options.smoothnessLevel = 0.5;
    options.lighteningContrastLevel = 0.5;
    [self.agoraKit setBeautyEffectOptions:YES options:options];
    
    [self.agoraKit enableVideo];
    [self.agoraKit setAudioProfile:AgoraAudioProfileDefault scenario:AgoraAudioScenarioGameStreaming];
    [self.agoraKit enableInEarMonitoring:NO];
    
    AgoraCameraCapturerConfiguration *c = [AgoraCameraCapturerConfiguration new];
    c.preference = AgoraCameraCaptureOutputPreferencePerformance;
    if (cfgDic != nil && [cfgDic[@"cameraDirection"] intValue] == 1) {
        c.cameraDirection = AgoraCameraDirectionRear;
    }
    else {
        c.cameraDirection = AgoraCameraDirectionFront;
    }
    [self.agoraKit setCameraCapturerConfiguration:c];
}

- (void)p_initializeMediaDataPlugin {
    self.agoraMediaDataPlugin = [AgoraMediaDataPlugin mediaDataPluginWithAgoraKit:self.agoraKit];
    // Register video observer
    ObserverVideoType videoType = ObserverVideoTypeCaptureVideo;
    [self.agoraMediaDataPlugin registerVideoRawDataObserver:videoType];
    ObserverAudioType aType = ObserverAudioTypeRecordAudio;
    [self.agoraMediaDataPlugin registerAudioRawDataObserver:aType];
}

- (NSMutableArray *)p_layer:(NSUInteger)count x:(CGFloat)x y:(CGFloat)y w:(CGFloat)w h:(CGFloat)h {
    NSMutableArray *rects = [NSMutableArray new];
    if (count <= 1) {
        CGRect rect1 = CGRectMake(0, 0, w, h);
        [rects addObject:[NSValue valueWithCGRect:rect1]];
    }
    else if (count <= 2) {
        CGRect rect1 = CGRectMake(x,         y, w/2.0, h);
        CGRect rect2 = CGRectMake(x + w/2.0, y, w/2.0, h);
        [rects addObjectsFromArray:@[[NSValue valueWithCGRect:rect1], [NSValue valueWithCGRect:rect2]]];
    }
    return rects;
}

- (NSMutableArray *)p_layer4horizontal:(NSUInteger)count x:(CGFloat)x y:(CGFloat)y w:(CGFloat)w h:(CGFloat)h {
    NSMutableArray *rects = [NSMutableArray new];
    if (count <= 1) {
        CGRect rect1 = CGRectMake(0, 0, w, h);
        [rects addObject:[NSValue valueWithCGRect:rect1]];
    }
    else if (count == 2) {
        CGFloat w1 = w / 2.0;//h * (222.5 / 375);
        CGFloat x1 = x;//x + (w - w1 * 2) / 2;
        CGRect rect1 = CGRectMake(x1,      y, w1, h);
        CGRect rect2 = CGRectMake(x1 + w1, y, w1, h);
        [rects addObjectsFromArray:@[[NSValue valueWithCGRect:rect1], [NSValue valueWithCGRect:rect2]]];
    }
    return rects;
}

- (void)p_previewLayer {
    [self p_orderUids];
    for (NSNumber *uidN in self.allUids) {
        UIView *videoV = self.allVideos[uidN];
        if (videoV == nil) {
            videoV = [UIView new];
            videoV.backgroundColor = [UIColor clearColor];
            [self.contentV addSubview:videoV];
            [self.allVideos setObject:videoV forKey:uidN];
            {
                UIView *v = [UIView new];
                v.backgroundColor = [UIColor clearColor];
                v.tag = kVideoViewTag;
                [videoV addSubview:v];
            }
        }
    }
    NSUInteger count = kTestUidsCount;
    CGFloat w = 0;
    CGFloat h = 0;
    CGFloat x = 0;
    CGFloat y = 0;
    NSMutableArray *rects;
    if ([self.sourceProtocol isLandspace]) {
        CGFloat scw = MAX([UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
        CGFloat sch = MIN([UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
        w = round(count <= 1 ? scw : MIN(scw, sch * (16.f / 9.f)));
        h = sch;
        x = (scw - w) / 2.f;
        rects = [self p_layer4horizontal:count x:x y:y w:w h:h];
    }
    else {
        w = [UIScreen mainScreen].bounds.size.width;
        h = round(count == 1 ? [UIScreen mainScreen].bounds.size.height : 335.0/375.0*[UIScreen mainScreen].bounds.size.width);
        y = round([self.sourceProtocol getPreViewTopY]);
        if (y < 0) {
            y =
            round(102.0/667.0*[UIScreen mainScreen].bounds.size.height);
        }
        rects = [self p_layer:count x:x y:y w:w h:h];
    }
    
    count = self.allUids.count;
    if (count >= 1) {
//        CGRect rect = [rects[0] CGRectValue];
    }
    for (int i = 0; i < MIN(rects.count, count); i++) {
        NSNumber *uidN = self.allUids[i];
        UIView *videoV = self.allVideos[uidN];
        NSValue *value = rects[i];
        CGRect rect = [value CGRectValue];
        videoV.frame = rect;
        {
            UIView *v = [videoV viewWithTag:kVideoViewTag];
            v.frame = videoV.bounds;
        }
    }
}

- (void)p_pushLayer {
    NSUInteger count = kTestUidsCount;
    CGFloat w = 0;
    CGFloat h = 0;
    CGFloat x = 0;
    CGFloat y = 0;
    NSMutableArray *rects;
    if ([self.sourceProtocol isLandspace]) {
        w = self.transcoding.size.width;
        h = self.transcoding.size.height;
//        x = (self.transcoding.size.width - w) / 2.f;
        rects = [self p_layer4horizontal:count x:x y:y w:w h:h];
    }
    else {
        w = self.transcoding.size.width;
        h = round(count == 1 ? self.transcoding.size.height : 335.0/375.0*self.transcoding.size.width);
        y = round(102.0/667.0*self.transcoding.size.height);
        rects = [self p_layer:count x:x y:y w:w h:h];
    }
    
    NSMutableArray *transcodingUsers = [NSMutableArray new];
    for (int i = 0; i < MIN(rects.count, count); i++) {
        NSNumber *uidN = self.allUids[i];
        NSValue *value = rects[i];
        CGRect rect = [value CGRectValue];
        
        AgoraLiveTranscodingUser *user = [[AgoraLiveTranscodingUser alloc] init];
        user.uid = [uidN unsignedIntegerValue];
        user.rect = rect;
        user.audioChannel = 0;
        [transcodingUsers addObject:user];
    }
    self.transcoding.transcodingUsers = transcodingUsers;
}

- (void)p_setupLocalVideo {
    UIView *video = self.allVideos[@(self.userId)];
    video.hidden = NO;
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = self.userId;
    videoCanvas.view = [video viewWithTag:kVideoViewTag];
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    if (kTest4Self) {
        videoCanvas.mirrorMode = AgoraVideoMirrorModeDisabled;
    }
    // 设置本地视图。
    [self.agoraKit setupLocalVideo:videoCanvas];
}

- (void)p_setupRemoteVideo:(NSUInteger)uid {
    UIView *videoV = self.allVideos[@(uid)];
    videoV.hidden = NO;
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = uid;
    videoCanvas.view = [videoV viewWithTag:kVideoViewTag];
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    // 设置远端视图。
    [self.agoraKit setupRemoteVideo:videoCanvas];
}

- (void)p_removeRemoteVideo:(NSUInteger)uid {
    UIView *remoteV = self.allVideos[@(uid)];
    if (remoteV != nil) {
        remoteV.hidden = NO;
        AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
        videoCanvas.uid = uid;
        videoCanvas.view = nil;
        videoCanvas.renderMode = AgoraVideoRenderModeHidden;
        [self.agoraKit setupRemoteVideo:videoCanvas];
        [remoteV removeFromSuperview];
    }
}

- (BOOL)p_joinChannel {
    if (self.state != QHRTCKitStateIdle) {
        return NO;
    }
    self.state = QHRTCKitStateJoining;
    
    if ([self.allUids containsObject:@(self.userId)] == NO) {
        [self.allUids addObject:@(self.userId)];
        self.agoraMediaDataPlugin.videoDelegate = self;
        self.agoraMediaDataPlugin.audioDelegate = self;
        [self p_previewLayer];
        [self p_setupLocalVideo];
        [self.agoraKit startPreview];
        
        [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-startPreview", __func__]];
    }
    int code = [self.agoraKit joinChannelByToken:self.token channelId:self.channel info:nil uid:self.userId joinSuccess:nil];
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%ld", __func__, (long)code]];
    if (code != 0) {
        self.state = QHRTCKitStateIdle;
    }
    return code == 0 ? YES : NO;
}

- (void)p_leaveChannel {
    self.bPush = NO;
    if (self.streamUrl != nil) {
        [self.agoraKit removePublishStreamUrl:self.streamUrl];
    }
    [self p_closeTimer4RetryAddPublishStreamUrl];
    [self.agoraMediaDataPlugin deregisterVideoRawDataObserver:ObserverVideoTypeCaptureVideo];
    self.agoraMediaDataPlugin.videoDelegate = nil;
    self.agoraMediaDataPlugin.audioDelegate = nil;
    [self.agoraKit setupLocalVideo:nil];
    // 离开频道。
    if (self.state != QHRTCKitStateIdle) {
        [self.agoraKit leaveChannel:nil];
    }
    [self.agoraKit stopPreview];
    
    for (NSNumber *uidN in self.allUids) {
        [self p_removeRemoteVideo:[uidN unsignedIntegerValue]];
        UIView *videoV = self.allVideos[uidN];
        [videoV removeFromSuperview];
    }
    [self.allUids removeAllObjects];
    [self.allVideos removeAllObjects];
    
    self.state = QHRTCKitStateIdle;
}

- (void)p_orderUids {
    NSArray *micOrderArr = [self.sourceProtocol getMicOrderArr];
    if (micOrderArr != nil && micOrderArr.count > 0) {
        [self.allUids sortUsingComparator:^NSComparisonResult(NSNumber *obj1, NSNumber *obj2) {
            if ([micOrderArr containsObject:obj2] == NO) {
                return NSOrderedAscending;
            }
            if ([micOrderArr containsObject:obj1] == NO) {
                return NSOrderedDescending;
            }
            if ([micOrderArr indexOfObject:obj1] > [micOrderArr indexOfObject:obj2]) {
                return NSOrderedDescending;
            }
            return NSOrderedAscending;
        }];
    }
    
}

- (void)p_refreshWhenRmoteChange {
    if (self.bPush) {
        [self p_pushLayer];
        [self.agoraKit setLiveTranscoding:self.transcoding];
    }
}

- (void)p_closeTimer4RetryAddPublishStreamUrl {
    [_retryTimer invalidate];
    _retryTimer = nil;
}

#pragma mark - AgoraRtcEngineDelegate

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurWarning:(AgoraWarningCode)warningCode {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%ld", __func__, (long)warningCode]];
    if (warningCode == 104) {
        self.state = QHRTCKitStateJoining;
    }
    NSError *error = [NSError errorWithDomain:@"AgoraOccurWarning" code:warningCode userInfo:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitOccurWarningNotification object:error];
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurError:(AgoraErrorCode)errorCode {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%ld", __func__, (long)errorCode]];
    NSError *error = [NSError errorWithDomain:@"AgoraOccurError" code:errorCode userInfo:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitOccurErrorNotification object:error];
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didApiCallExecute:(NSInteger)error api:(NSString * _Nonnull)api result:(NSString * _Nonnull)result {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%ld-%@-%@", __func__, (long)error, api, result]];
}

// local

- (void)rtcEngine:(AgoraRtcEngineKit *_Nonnull)engine didJoinChannel:(NSString *_Nonnull)channel withUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%@-%ld", __func__, (unsigned long)uid, channel, (long)elapsed]];
    self.state = QHRTCKitStateJoined;
    [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitJoinNotification object:nil];
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didRejoinChannel:(NSString * _Nonnull)channel withUid:(NSUInteger)uid elapsed:(NSInteger) elapsed {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%@-%ld", __func__, (unsigned long)uid, channel, (long)elapsed]];
    self.state = QHRTCKitStateJoined;
    [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitRejoinNotification object:nil];
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didLeaveChannelWithStats:(AgoraChannelStats * _Nonnull)stats {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu", __func__, (unsigned long)stats]];
}

// remote

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didJoinedOfUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%ld", __func__, (unsigned long)uid, (long)elapsed]];
    if (self.allUids.count >= 2) {
        [self.sourceProtocol logInfo:@"超出 6个人 的忽略"];
        return;
    }
    NSNumber *uidN = [NSNumber numberWithUnsignedInteger:uid];
    if ([self.allUids containsObject:uidN] == NO) {
        if (uid == _hostId) {
            [self.allUids insertObject:uidN atIndex:0];
        }
        else {
            [self.allUids addObject:uidN];
        }
        [self p_orderUids];
        
        [self p_refreshWhenRmoteChange];
    }
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOfflineOfUid:(NSUInteger)uid reason:(AgoraUserOfflineReason)reason {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%ld", __func__, (unsigned long)uid, (long)reason]];
    NSNumber *uidN = [NSNumber numberWithUnsignedInteger:uid];
    if ([self.allUids containsObject:uidN] == YES) {
        [self p_removeRemoteVideo:uid];
        [self.allUids removeObject:uidN];
        [self.allVideos removeObjectForKey:uidN];
        
        [self p_previewLayer];
        [self p_refreshWhenRmoteChange];
    }
}

// media

- (void)rtcEngine:(AgoraRtcEngineKit *_Nonnull)engine firstLocalVideoFrameWithSize:(CGSize)size elapsed:(NSInteger)elapsed {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%ld", __func__, (long)elapsed]];
    [self p_previewLayer];
    [self p_setupLocalVideo];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitFirstVideoFrameNotification object:nil];
}

- (void)rtcEngine:(AgoraRtcEngineKit *_Nonnull)engine remoteVideoStateChangedOfUid:(NSUInteger)uid state:(AgoraVideoRemoteState)state reason:(AgoraVideoRemoteStateReason)reason elapsed:(NSInteger)elapsed {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%lu-%lu-%ld", __func__, (unsigned long)uid, (unsigned long)state, (unsigned long)reason, (long)elapsed]];
    if (state == AgoraVideoRemoteStateStarting) {
        NSNumber *uidN = [NSNumber numberWithUnsignedInteger:uid];
        if ([self.allUids containsObject:uidN] == YES) {
            [self p_previewLayer];
            [self p_setupRemoteVideo:uid];
        }
    }
}

// push

- (void)rtcEngine:(AgoraRtcEngineKit *_Nonnull)engine rtmpStreamingChangedToState:(NSString *_Nonnull)url state:(AgoraRtmpStreamingState)state errorCode:(AgoraRtmpStreamingErrorCode)errorCode {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%lu-%lu", __func__, (unsigned long)state, (unsigned long)errorCode]];
    if (state == AgoraRtmpStreamingStateRunning) {
        self.state = QHRTCKitStatePush;
        [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitPushNotification object:nil];
    }
    else if (state == AgoraRtmpStreamingStateFailure) {
//        self.state = QHRTCKitStateJoined;
        NSError *error = [NSError errorWithDomain:@"rtmp_fail" code:errorCode userInfo:nil];
        [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitPushNotification object:error];
    }
}

// [定制方案：旁路推流重推逻辑优化](https://docs.qq.com/doc/DVmpBVW1XTkFNSUhQ)
// 推流重试优化一
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine streamPublishedWithUrl:(NSString * _Nonnull)url errorCode:(AgoraErrorCode)errorCode {
    [self.sourceProtocol logInfo:[NSString stringWithFormat:@"%s-%@-%lu-%d", __func__, url, errorCode, self.bPush]];
    if (self.bPush) {
        switch (errorCode) {
            case AgoraErrorCodeFailed:
            case AgoraErrorCodeTimedOut:
            case AgoraErrorCodePublishStreamInternalServerError: {
                [self.agoraKit removePublishStreamUrl:self.streamUrl];
            }
                break;
            case AgoraErrorCodePublishStreamNotFound: {
                [self.agoraKit addPublishStreamUrl:self.streamUrl transcodingEnabled:YES];
            }
                break;
            default:
                break;
        }
    }
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine streamUnpublishedWithUrl:(NSString * _Nonnull)url {
    if (self.bPush) {
        [self.agoraKit addPublishStreamUrl:self.streamUrl transcodingEnabled:YES];
    }
}

#pragma mark - networkQuality

- (void)rtcEngine:(AgoraRtcEngineKit *_Nonnull)engine networkQuality:(NSUInteger)uid txQuality:(AgoraNetworkQuality)txQuality rxQuality:(AgoraNetworkQuality)rxQuality {
    if (uid == 0) {
        NSDictionary *userInfo = @{@"txQuality": @(txQuality), @"rxQuality": @(rxQuality)};
        [[NSNotificationCenter defaultCenter] postNotificationName:kQHRTCKitNetworkQualityNotification object:nil userInfo:userInfo];
    }
}

#pragma mark - AgoraVideoDataPluginDelegate

- (AgoraVideoRawData *)mediaDataPlugin:(AgoraMediaDataPlugin *)mediaDataPlugin didCapturedVideoRawData:(AgoraVideoRawData *)videoRawData {
    return videoRawData;
}

#pragma mark - AgoraAudioDataPluginDelegate

- (AgoraAudioRawData * _Nonnull)mediaDataPlugin:(AgoraMediaDataPlugin * _Nonnull)mediaDataPlugin didRecordAudioRawData:(AgoraAudioRawData * _Nonnull)audioRawData {
    
    if (self.bAudioCallback) {
        AgoraAudioRawData *a = [AgoraAudioRawData new];
        a.samples = audioRawData.samples;
        a.bytesPerSample = audioRawData.bytesPerSample;
        a.channels = audioRawData.channels;
        a.samplesPerSec = audioRawData.samplesPerSec;
        a.bufferSize = audioRawData.bufferSize;
        a.renderTimeMs = audioRawData.renderTimeMs;
        char *b = malloc(audioRawData.bufferSize);
        memcpy(b, audioRawData.buffer, audioRawData.bufferSize);
        a.buffer = b;
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            CMSampleBufferRef buf = [self createAudioSample:a];
            if (buf) {
                [self.delegate getAudioKit:self buffer:buf];
            }
        });
    }
    
    return audioRawData;
}

- (AudioStreamBasicDescription)getAudioFormat:(AgoraAudioRawData * _Nonnull)audioRawData {
    UInt32 bytesPerSample = audioRawData.bytesPerSample;//sizeof(SInt16);
    UInt32 channels = audioRawData.channels;

    AudioStreamBasicDescription format;
    format.mSampleRate = audioRawData.samplesPerSec;
    format.mFormatID = kAudioFormatLinearPCM;
    format.mFormatFlags = kLinearPCMFormatFlagIsNonInterleaved | kLinearPCMFormatFlagIsSignedInteger | kAudioFormatFlagIsPacked;
//    format.mFormatFlags = kLinearPCMFormatFlagIsNonInterleaved | kAudioFormatFlagIsFloat | kAudioFormatFlagIsPacked;
    format.mBytesPerPacket = bytesPerSample;
    format.mFramesPerPacket = 1;
    format.mBytesPerFrame = bytesPerSample;
    format.mChannelsPerFrame = channels;
    format.mBitsPerChannel = 8 * bytesPerSample;
    format.mReserved = 0;

    return format;
}

- (CMSampleBufferRef)createAudioSample:(AgoraAudioRawData * _Nonnull)audioRawData {
    int channels = audioRawData.channels;

    AudioBufferList audioBufferList;
    audioBufferList.mNumberBuffers = 1;
    audioBufferList.mBuffers[0].mNumberChannels = channels;
    audioBufferList.mBuffers[0].mDataByteSize = audioRawData.bufferSize;
    char* b = malloc(audioRawData.bufferSize);
    memcpy(b, audioRawData.buffer, audioRawData.bufferSize);
    audioBufferList.mBuffers[0].mData = b;

    AudioStreamBasicDescription asbd = [self getAudioFormat:audioRawData];
    CMSampleBufferRef buff = NULL;
    static CMFormatDescriptionRef format = NULL;
    CMTime time = CMTimeMake(audioRawData.bufferSize/audioRawData.bytesPerSample, audioRawData.samplesPerSec);
    CMSampleTimingInfo timing = {CMTimeMake(1, audioRawData.samplesPerSec), time, kCMTimeInvalid };

    OSStatus error = 0;
    if (format == NULL)
        error = CMAudioFormatDescriptionCreate(kCFAllocatorDefault, &asbd, 0, NULL, 0, NULL, NULL, &format);

    error = CMSampleBufferCreate(kCFAllocatorDefault, NULL, false, NULL, NULL, format, audioRawData.samples, 1, &timing, 0, NULL, &buff);
 
    if ( error ) {
        NSLog(@"CMSampleBufferCreate returned error: %ld", (long)error);
        return NULL;
    }
 
    error = CMSampleBufferSetDataBufferFromAudioBufferList(buff, kCFAllocatorDefault, kCFAllocatorDefault, 0, &audioBufferList);
    if ( error ) {
        NSLog(@"CMSampleBufferSetDataBufferFromAudioBufferList returned error: %ld", (long)error);
        return NULL;
    }
    return buff;
}

@end
