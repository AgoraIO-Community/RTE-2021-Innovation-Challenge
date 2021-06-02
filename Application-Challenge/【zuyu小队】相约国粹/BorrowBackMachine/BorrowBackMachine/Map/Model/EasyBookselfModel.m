//
//  EasyBookselfModel.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/23.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EasyBookselfModel.h"

@implementation EasyBookselfModel
-(void)setLogoUrl:(NSString *)LogoUrl
{
    _LogoUrl = [NSString stringWithFormat:@"%@%@",FILE,LogoUrl];
    _LogoUrl = [_LogoUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}
@end
