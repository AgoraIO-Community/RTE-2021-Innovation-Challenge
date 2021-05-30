//
//  QHChatBaseModel.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/21.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseModel.h"

@interface QHChatBaseModel ()

@property (nonatomic, copy, readwrite) NSDictionary *originChatDataDic;

@end

@implementation QHChatBaseModel

- (instancetype)initWithChatData:(NSDictionary *)data {
    self = [super init];
    if (self) {
        _originChatDataDic = data;
        _cellHeight = 0;
    }
    return self;
}

@end
