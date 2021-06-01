//
//  BBMessageModel.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/14.
//

#import "BBMessageModel.h"

@implementation BBMessageModel

- (void)setValue:(id)value forUndefinedKey:(NSString *)key{
    if ([key isEqualToString:@"name"]) {
        self.pName = value;
    }
    
    if ([key isEqualToString:@"gender"]) {
        self.pGender = value;
    }
}

@end
