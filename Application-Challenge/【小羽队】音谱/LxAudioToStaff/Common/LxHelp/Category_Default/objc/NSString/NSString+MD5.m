//
//  NSString+MD5.m
//  MRMD5
//
//  Created by MrXir on 2017/7/19.
//  Copyright © 2017年 MrXir. All rights reserved.
//

#import "NSString+MD5.h"

#import <CommonCrypto/CommonDigest.h>

@implementation NSString (MD5)

- (NSString *)md5Hash
{
    const char* input = [self UTF8String];
    
    CC_MD5_CTX md5HashContext;
    
    CC_MD5_Init(&md5HashContext);
    
    /**
     *
     
     CC_MD5_Update(&md5HashContext, input, (CC_LONG)[self length]); // This is wrong!
     CC_MD5_Update(&md5HashContext, input, (CC_LONG)strlen(input)); // This is right.
     
     @  我发现 github 上存在一些MD5加密工具都有一处错误, 那就是
     CC_MD5_Update 这个方法的第三个参数, 它们直接使用了 'self.length' 作为参数,
     这里其实应该使用 'strlen([self UTF8String])' 作为参数,
     因为一个是 NSString 的长度, 另一个是 char * 的长度.
     许多歪过朋友一直使用也没有发现问题可能是因为没有碰到对中文或emoji进行MD5的情况.
     
     *
     */
    
    CC_MD5_Update(&md5HashContext, input, (CC_LONG)strlen(input));
    
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    
    CC_MD5_Final(digest, &md5HashContext);
    
    NSMutableString *md5HashString = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for (NSInteger i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [md5HashString appendFormat:@"%02x", digest[i]];
    }
    
    return md5HashString;
}

+ (NSString *)md5HashWithFile:(NSString *)filePath
{
    NSFileHandle *handle = [NSFileHandle fileHandleForReadingAtPath:filePath];
    if (handle == nil) return nil;
    
    NSData *fileData = [[NSData alloc] initWithData:[handle readDataOfLength:4096]];
    const char* input = [fileData bytes];
    
    CC_MD5_CTX md5HashContext;
    
    CC_MD5_Init (&md5HashContext);
    
    BOOL done = NO;
    
    while (!done) {
        
        CC_MD5_Update (&md5HashContext, input, (CC_LONG)[fileData length]);
        
        if ([fileData length] == 0) {
            done = YES;
        }
        
    }
    
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    
    CC_MD5_Final (digest, &md5HashContext);
    
    NSMutableString *md5HashString = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for (NSInteger i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [md5HashString appendFormat:@"%02x", digest[i]];
    }
    
    return md5HashString;
}

@end
