//
//  ZuyuTokenRefresh.h
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/29.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ZuyuTokenRefresh : NSObject<UIAlertViewDelegate>


+ (void)tokenRefreshSuccess:(void (^)(NSURLSessionDataTask * _Nonnull dataTask, id _Nullable responseObject))success failure:(void (^)(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error))failure;


@end
 
