//
//  ZuyuBanner.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuBanner.h"
#import "AFNetworking.h"
#import "zuyu.h"
@implementation ZuyuBanner


+(void)getBannerwithType:(NSString *)type count:(NSString *)top imageArray:(void (^)(NSArray *))success
{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:type,@"Type",
                               top,@"Top",nil];
    
    [manager POST:PORT(@"AppBanner/GetTopList") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSMutableArray  *imageArray = [NSMutableArray array];
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [responseObject objectForKey:@"Data"]) {
                NSString *url = [ZuyuJsonRead jsonRead:itme WithKey:@"BannerUrl"];
                url = [NSString stringWithFormat:@"%@%@",FILE,url];
                [imageArray addObject:url];
            }
            
            success(imageArray);
            
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
    }];
}
@end
