//
//  QHRTCKitProtocol.h
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/22.
//  Copyright © 2020 ... All rights reserved.
//

#import <AVFoundation/AVFoundation.h>

#ifndef QHRTCKitProtocol_h
#define QHRTCKitProtocol_h

#define kQHRTCKitJoinNotification @"kQHRTCKitJoinNotification"
#define kQHRTCKitFirstVideoFrameNotification @"kQHRTCKitFirstVideoFrameNotification"
#define kQHRTCKitPushNotification @"kQHRTCKitPushNotification"

// agora
#define kQHRTCKitOccurWarningNotification @"kQHRTCKitOccurWarningNotification"
#define kQHRTCKitOccurErrorNotification @"kQHRTCKitOccurErrorNotification"
#define kQHRTCKitRejoinNotification @"kQHRTCKitRejoinNotification"
// 上行质量、下行质量
// userInfo = @{@"txQuality": @(txQuality), @"rxQuality": @(rxQuality)};
// [AgoraNetworkQuality 常量](https://docs.agora.io/cn/Video/API%20Reference/oc/Constants/AgoraNetworkQuality.html)
#define kQHRTCKitNetworkQualityNotification @"kQHRTCKitNetworkQualityNotification"

@protocol QHRTCKitProtocol <NSObject>

@optional

- (void)config:(NSDictionary *)dic;
- (void)join;
- (void)leave;

@end

@protocol QHRTCKitSourceProtocol <NSObject>

- (void)logInfo:(NSString *)log;
- (id)getLiveConfig;
- (NSDictionary *)getUserInfoDic;
- (NSArray *)getMicOrderArr;
- (CGFloat)getPreViewTopY;
- (BOOL)isLandspace;

@end

@protocol QHRTCBaseKitDelegate <NSObject>

- (void)getAudioKit:(id)kit buffer:(CMSampleBufferRef)buffer;

@end

@protocol QHRTCKitManagerDelegate <NSObject>

@optional

- (BOOL)logRTCKitManager:(id)manager content:(NSString *)content;
- (void)getAudioRTCKitManager:(id)manager buffer:(CMSampleBufferRef)buffer;

@end

#endif /* QHRTCKitProtocol_h */
