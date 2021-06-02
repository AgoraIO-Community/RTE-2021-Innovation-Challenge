//
//  ZuyuTokenRefresh.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/29.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuTokenRefresh.h"
#import "AFNetworking.h"
#import "MBProgressHUD.h"
#import "UUID.h"
#import "AppDelegate.h"


@implementation ZuyuTokenRefresh

+ (void)tokenRefreshSuccess:(void (^)(NSURLSessionDataTask * _Nonnull dataTask, id _Nullable responseObject))success failure:(void (^)(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error))failure
{
    
       NSDictionary *parameter = [[NSDictionary alloc] initWithObjectsAndKeys:ZHANGHAO,@"UserName",MIMA,@"Password",[UUID getUUID],@"Uuid", nil];
    
        [[AFHTTPSessionManager manager] POST:PORT(@"Authorize/Login") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            NSInteger IsDone = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue;
            if (IsDone) {
                NSString *Token    = [NSString stringWithFormat:@"%@",[[responseObject objectForKey:@"Data"] objectForKey:@"Token"]];
                
                [[NSUserDefaults standardUserDefaults] setObject:Token forKey:@"Token"];
                
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                success(task, responseObject);
                
            }else{
                
                
            }
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"网络错误"
                                                            message:nil
                                                           delegate:self
                                                  cancelButtonTitle:@"好的"
                                                  otherButtonTitles:nil];
            
            [alert show];
            
            failure(task,error);
            
            NSLog(@"  <<<<<<<<<<<<<< -----   %@",error)
        }];
    
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
 {
        NSString *btnTitle = [alertView buttonTitleAtIndex:buttonIndex];
         if ([btnTitle isEqualToString:@"OK"]) {
             NSLog(@"你点击了退出");
             }
     
    
 }
//监听点击事件 代理方法
//- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
//{
//    NSString *btnTitle = [alertView buttonTitleAtIndex:buttonIndex];
//    if ([btnTitle isEqualToString:@"OK"]) {
//
//
//
//    }
//}
@end
