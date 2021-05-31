//
//  NSString+MD5.h
//  MRMD5
//
//  Created by MrXir on 2017/7/19.
//  Copyright © 2017年 MrXir. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (MD5)

- (NSString *)md5Hash;

+ (NSString *)md5HashWithFile:(NSString *)filePath;

@end
