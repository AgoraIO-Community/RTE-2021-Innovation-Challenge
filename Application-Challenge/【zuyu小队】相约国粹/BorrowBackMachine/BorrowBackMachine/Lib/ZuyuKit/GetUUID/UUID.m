//
//  UUID.m
//  Created by zuyu on 17/4/20.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "UUID.h"
#import "KeyChainStore.h"

@implementation UUID
+(NSString *)getUUID
{
    NSString * strUUID = (NSString *)[KeyChainStore load:@"com.company.app.usernamepassword"];

    //首次执行该方法时，uuid为空
    if ([strUUID isEqualToString:@""] || !strUUID)
    {
        //生成一个uuid的方法
        CFUUIDRef uuidRef = CFUUIDCreate(kCFAllocatorDefault);
        
        strUUID = (NSString *)CFBridgingRelease(CFUUIDCreateString (kCFAllocatorDefault,uuidRef));
        
        //将该uuid保存到keychain
        [KeyChainStore save:@"com.company.app.usernamepassword" data:strUUID];
        
    }
    return strUUID;
}
@end
