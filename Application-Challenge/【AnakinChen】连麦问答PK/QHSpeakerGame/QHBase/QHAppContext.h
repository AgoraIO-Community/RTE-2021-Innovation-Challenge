//
//  QHAppContext.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/22.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface QHAppContext : NSObject

// 声网AppId
@property (nonatomic, strong) NSString *AGAppId;
// 声网频道名
@property (nonatomic, strong) NSString *AGChannelId;
// 声网临时Token
@property (nonatomic, strong) NSString *AGToken;
// 环信AppKey
@property (nonatomic, strong) NSString *EMAppkey;
// 环信聊天室ID
@property (nonatomic, strong) NSString *EMRoomId;
// 讯飞AppId
@property (nonatomic, strong) NSString *IFlyAppId;

@property (nonatomic, copy) NSString *curUser;
//@property (nonatomic, strong) NSString *curPassword;
@property (nonatomic, strong) NSString *gameHost;
@property (nonatomic) BOOL isHost;

@property (nonatomic) NSInteger curId;
@property (nonatomic) NSInteger hostId;

@property (nonatomic, strong) NSString *questionId;

+ (instancetype)context;

- (void)readSetting;
- (BOOL)hasSetting;
- (void)doSetting;

@end

NS_ASSUME_NONNULL_END
