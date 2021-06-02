//
//  KeyChainStore.h
//  Created by zuyu on 17/4/20.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface KeyChainStore : NSObject
+ (void)save:(NSString *)service data:(id)data;
+ (id)load:(NSString *)service;
+ (void)deleteKeyData:(NSString *)service;
@end
