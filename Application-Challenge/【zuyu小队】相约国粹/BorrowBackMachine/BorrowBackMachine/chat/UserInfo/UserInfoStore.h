//
//  UserInfoStore.h
//  EaseIM
//
//  Created by lixiaoming on 2021/3/18.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface UserInfoStore : NSObject
+(instancetype _Nonnull ) alloc __attribute__((unavailable("call sharedInstance instead")));
+(instancetype _Nonnull ) new __attribute__((unavailable("call sharedInstance instead")));
-(instancetype _Nonnull ) copy __attribute__((unavailable("call sharedInstance instead")));
-(instancetype _Nonnull ) mutableCopy __attribute__((unavailable("call sharedInstance instead")));
+ (instancetype)sharedInstance;
/**
 * 设置单个用户的用户属性
 */
- (void)setUserInfo:(EMUserInfo*)aUserInfo forId:(NSString*)aUserId;
/**
 * 批量设置用户属性
 */
- (void)addUserInfos:(NSArray<EMUserInfo*>*)aUserInfos;
/**
 * 根据用户ID获取用户属性
 */
- (EMUserInfo*)getUserInfoById:(NSString*)aUserId;
/**
 * 从本地加载所有用户属性
 */
- (void)loadInfosFromLocal;
/**
 * 从服务器拉取用户属性
 */
- (void)fetchUserInfosFromServer:(NSArray<NSString*>*)aUids;
@end

NS_ASSUME_NONNULL_END
