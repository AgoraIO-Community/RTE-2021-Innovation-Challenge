//
//  ZuyuJsonRead.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/24.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuJsonRead.h"

@implementation ZuyuJsonRead

+(NSString *)jsonRead:(NSDictionary *)dic WithKey:(NSString *)key
{
    if (dic && key) {
        NSString *str = [NSString stringWithFormat:@"%@",[dic objectForKey:key]];
        return str;
    }
    
    return @"";
}
@end
