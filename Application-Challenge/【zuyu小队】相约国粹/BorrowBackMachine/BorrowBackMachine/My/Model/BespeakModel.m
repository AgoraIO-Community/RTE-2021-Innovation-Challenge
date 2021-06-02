//
//  BespeakModel.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BespeakModel.h"

@implementation BespeakModel

- (void)setBookLogoUrl:(NSString *)BookLogoUrl
{
    _BookLogoUrl = [NSString stringWithFormat:@"%@%@",FILE,BookLogoUrl];
    
    _BookLogoUrl = [_BookLogoUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

}
@end
