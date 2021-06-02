//
//  QHAppContext.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/22.
//

#import "QHAppContext.h"

#import <HyphenateChat/HyphenateChat.h>
#import <iflyMSC/iflyMSC.h>

#define K_Q_KEY @"Q3"

@interface QHAppContext ()

@end

@implementation QHAppContext

+ (instancetype)context {
    static QHAppContext *this = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        this = [QHAppContext new];
        [this p_setup];
    });
    return this;
}

- (void)readSetting {
    [self p_readSetting];
}

- (BOOL)hasSetting {
    if (_AGAppId == nil || _AGChannelId == nil || _AGToken == nil || _EMAppkey == nil || _EMRoomId == nil || _IFlyAppId == nil) {
        return NO;
    }
    return YES;
}

- (void)doSetting {
    [self p_initOtherLibrary];
}

#pragma mark - Private

- (void)p_setup {
    [self p_readSetting];
    _curId = CFAbsoluteTimeGetCurrent();
    _hostId = 100880066;
    _questionId = K_Q_KEY;
}

- (void)p_readSetting {
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *jsonConfig = [userDefaults objectForKey:@"JSONConfig"];
    if (jsonConfig != nil && jsonConfig.length > 0) {
        NSData *jsonData = [jsonConfig dataUsingEncoding:NSUTF8StringEncoding];
        NSError *err;
        NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:&err];
        if (err == nil) {
            // 声网AppId
            self.AGAppId = [dic objectForKey:@"AGAppId"];
            // 声网频道名
            self.AGChannelId = [dic objectForKey:@"AGChannelId"];
            // 声网临时Token
            self.AGToken = [dic objectForKey:@"AGToken"];
            // 环信AppKey
            self.EMAppkey = [dic objectForKey:@"EMAppkey"];
            // 环信聊天室ID
            self.EMRoomId = [dic objectForKey:@"EMRoomId"];
            // 讯飞AppId
            self.IFlyAppId = [dic objectForKey:@"IFlyAppId"];
            return;
        }
        else {
            NSLog(@"json解析失败：%@",err);
        }
    }
    // 声网AppId
    self.AGAppId = [userDefaults objectForKey:@"AGAppId"];
    // 声网频道名
    self.AGChannelId = [userDefaults objectForKey:@"AGChannelId"];
    // 声网临时Token
    self.AGToken = [userDefaults objectForKey:@"AGToken"];
    // 环信AppKey
    self.EMAppkey = [userDefaults objectForKey:@"EMAppkey"];
    // 环信聊天室ID
    self.EMRoomId = [userDefaults objectForKey:@"EMRoomId"];
    // 讯飞AppId
    self.IFlyAppId = [userDefaults objectForKey:@"IFlyAppId"];
    /*
     // 声网AppId
     self.AGAppId = @"";
     // 声网频道名
     self.AGChannelId = @"";
     // 声网临时Token
     self.AGToken = @"";
     // 环信AppKey
     self.EMAppkey = @"";
     // 环信聊天室ID
     self.EMRoomId = @"";
     // 讯飞AppId
     self.IFlyAppId = @"";
     */
}

- (void)p_initOtherLibrary {
    // appkey替换成自己在环信管理后台注册应用中的appkey
    EMOptions *options = [EMOptions optionsWithAppkey:self.EMAppkey];
    // apnsCertName是证书名称，可以先传nil，等后期配置apns推送时在传入证书名称
    options.apnsCertName = nil;
    [[EMClient sharedClient] initializeSDKWithOptions:options];
    
    //Appid是应用的身份信息，具有唯一性，初始化时必须要传入Appid。
    NSString *initString = [[NSString alloc] initWithFormat:@"appid=%@", self.IFlyAppId];
    [IFlySpeechUtility createUtility:initString];
}

@end
