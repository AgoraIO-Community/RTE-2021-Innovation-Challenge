//
//  QHRTCKitManager.h
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/14.
//  Copyright © 2020 ... All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import <AgoraRtcKit/AgoraEnumerates.h>

#import "QHRTCKitProtocol.h"
#import "QHRTCBaseKit.h"

/*
 1、加入成功\失败
 2、推流成功\失败
 3、连麦过程中的错误状态
 */

typedef enum : NSUInteger {
    QHRTCRoleHost = 0,
    QHRTCRoleGuest,
    QHRTCRoleAudience,
} QHRTCRole;

typedef enum : NSUInteger {
    QHRTCKitTypeAG,
} QHRTCKitType;

NS_ASSUME_NONNULL_BEGIN

@interface QHRTCKitManager : NSObject <QHRTCKitSourceProtocol>

@property (nonatomic) QHRTCRole role;
@property (nonatomic, strong) NSString *streamUrl;
@property (nonatomic, strong) id liveConfig;
@property (nonatomic) CGFloat preViewTopY;

+ (instancetype)create:(QHRTCKitType)type superV:(UIView *)view config:(nullable id)cfg delegate:(id<QHRTCKitManagerDelegate>) delegate;
/*
 Config 校验和设置配置前最好先设置上面的所有属性
 一般设置一次，需要再 join 之前调用
 
 1、QHRTCKitTypeAG 配置如下：
 @{@"token": @"", @"channel": @"", @"hostId": @(0), @"userId": @(0)}
 2、
 */
- (BOOL)checkAndSetConfig:(NSDictionary *)dic;
- (BOOL)join;
- (void)leave;
- (BOOL)switchCamera;
/*
 添加用户的 uid：nickName
 1、QHRTCKitTypeAG
 key 是使用 NSNumber，即 @(uid)
 */
- (void)insertUserInfos:(NSDictionary<id, NSString *> *)dic;
// 同上 key
- (void)refreshMicOrder:(NSArray *)micOrder;
- (void)enterBackground:(BOOL)bEnter;

- (void)open4AudioCallback:(BOOL)bOpen;

@end

NS_ASSUME_NONNULL_END
