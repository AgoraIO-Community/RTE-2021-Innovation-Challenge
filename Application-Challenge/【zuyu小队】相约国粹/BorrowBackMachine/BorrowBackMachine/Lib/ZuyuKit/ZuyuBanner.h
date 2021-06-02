//
//  ZuyuBanner.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZuyuBanner : NSObject
+(void)getBannerwithType:(NSString *)type count:(NSString *)top imageArray:(void (^)(NSArray *array))success;
@end
