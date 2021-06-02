//
//  BookselfModel.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BookselfModel.h"

@implementation BookselfModel
-(void)setCreateTime:(NSString *)CreateTime
{
    _CreateTime =  [[CreateTime stringByReplacingOccurrencesOfString:@"T" withString:@" "]  substringToIndex:CreateTime.length - 4];
    
}
-(void)setLogoUrl:(NSString *)LogoUrl
{
    _LogoUrl = [NSString stringWithFormat:@"%@%@",FILE,LogoUrl];
    _LogoUrl = [_LogoUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

}
@end
