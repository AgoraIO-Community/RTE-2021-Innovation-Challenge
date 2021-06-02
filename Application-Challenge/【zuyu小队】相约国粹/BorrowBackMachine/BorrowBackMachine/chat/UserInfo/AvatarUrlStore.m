//
//  AvatarUrlStore.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/22.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "AvatarUrlStore.h"
static NSString* kBaseAvatarUrl = @"https://download-sdk.oss-cn-beijing.aliyuncs.com/downloads/IMDemo/avatar/";

@interface AvatarUrlStore()
@property (nonatomic,strong) NSDictionary* dicAvatarUrl;
@end

static AvatarUrlStore *avatarUrlStoreInstance = nil;

@implementation AvatarUrlStore

+ (instancetype)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        avatarUrlStoreInstance = [[AvatarUrlStore alloc] init];
    });
    return avatarUrlStoreInstance;
}

- (void)fetchListFromServer
{
    NSURL* url = [NSURL URLWithString:[kBaseAvatarUrl stringByAppendingString:@"headImage.conf"]];
    NSData* data = [NSData dataWithContentsOfURL:url];
    if(data) {
        NSError *jsonError = nil;
        NSDictionary *dictFromData = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&jsonError];
        if(dictFromData) {
            NSDictionary* dic = [dictFromData objectForKey:@"headImageList"];
            NSMutableDictionary* mutableDic = [NSMutableDictionary dictionary];
            for (NSString* key in dic) {
                NSString* value = [dic objectForKey:key];
                [mutableDic setObject:[kBaseAvatarUrl stringByAppendingString:value] forKey:key];
            }
            self.dicAvatarUrl = [mutableDic copy];
        }
    }
}
- (NSDictionary*)getAvatarUrlList
{
    return self.dicAvatarUrl;
}

- (NSDictionary*)dicAvatarUrl
{
    if(!_dicAvatarUrl) {
        _dicAvatarUrl = [NSDictionary dictionary];
    }
    return _dicAvatarUrl;
}
@end
