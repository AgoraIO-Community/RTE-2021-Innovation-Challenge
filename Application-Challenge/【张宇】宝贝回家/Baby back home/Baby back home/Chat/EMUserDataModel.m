//
//  EMUserDataModel.m
//  EaseIM
//
//  Created by 娜塔莎 on 2020/12/3.
//  Copyright © 2020 娜塔莎. All rights reserved.
//

#import "EMUserDataModel.h"

@implementation EMUserDataModel

- (instancetype)initWithEaseId:(NSString *)easeId
{
    if (self = [super init]) {
        _easeId = easeId;
        _defaultAvatar = [UIImage imageNamed:@"defaultAvatar"];
    }
    return self;
}

@end
