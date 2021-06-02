//
//  QHRTCBaseKit.h
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/26.
//  Copyright © 2020 ... All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "QHRTCKitProtocol.h"

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    QHRTCKitStateIdle,
    QHRTCKitStateJoining,
    QHRTCKitStateJoined,
    QHRTCKitStatePush,
    QHRTCKitStateError,
} QHRTCKitState;

@interface QHRTCBaseKit : NSObject <QHRTCKitProtocol>

@property (nonatomic) QHRTCKitState state;

@property (nonatomic, weak) id<QHRTCKitSourceProtocol> sourceProtocol;
@property (nonatomic) BOOL bPush;

@property (nonatomic) NSUInteger userId;
@property (nonatomic, strong) NSString *channel;
@property (nonatomic, strong) NSString *streamUrl;

@property (nonatomic, weak) id<QHRTCBaseKitDelegate> delegate;
@property (nonatomic) BOOL bAudioCallback;

+ (instancetype)createWith:(UIView *)superV;
/*
 @"cameraDirection": @(1), // 1 后置，0 前置
 */
- (void)setupWithLiveConfig:(nullable NSDictionary *)cfgDic;
- (BOOL)config:(NSDictionary *)dic;
- (BOOL)join;
- (void)leave;
- (void)addPush:(NSString *)streamUrl;
- (BOOL)push;
- (void)unpush;
- (void)resetFilterConfig;
- (BOOL)switchCamera;
- (void)refreshNickName;
- (void)refreshLayer;
- (void)enterBackground:(BOOL)bEnter;

@end

NS_ASSUME_NONNULL_END
