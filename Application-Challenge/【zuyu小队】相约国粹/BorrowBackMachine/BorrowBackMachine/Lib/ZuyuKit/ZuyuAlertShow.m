//
//  ZuyuAlertShow.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/26.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuAlertShow.h"

@implementation ZuyuAlertShow
#pragma mark - alertShow
+(void)alertShow:(NSString *)message viewController:(UIViewController *)vc
{
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message
                                                    message:nil
                                                   delegate:vc
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    
    [alert show];
}
@end
