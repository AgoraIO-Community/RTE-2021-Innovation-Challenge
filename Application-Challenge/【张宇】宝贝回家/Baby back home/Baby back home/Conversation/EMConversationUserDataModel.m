//
//  EMConversationUserDataModel.m
//  EaseIM
//
//  Created by 娜塔莎 on 2020/12/6.
//  Copyright © 2020 娜塔莎. All rights reserved.
//

#import "EMConversationUserDataModel.h"

@implementation EMConversationUserDataModel

- (instancetype)initWithEaseId:(NSString*)easeId conversationType:(EMConversationType)type
{
    if (self = [super init]) {
        _easeId = easeId;
        _showName = easeId;
        if (type == EMConversationTypeChat) {
            if ([easeId isEqualToString:EMSYSTEMNOTIFICATIONID]) {
                _showName = @"系统通知";
            }
        }
        if(type == EMConversationTypeGroupChat) {
            EMGroup* group = [EMGroup groupWithId:easeId];
            _showName = [group groupName];
        }
        _defaultAvatar = [self _getDefaultAvatarImage:easeId conversationType:type];
    }
    return self;
}

- (UIImage*)_getDefaultAvatarImage:(NSString*)easeId conversationType:(EMConversationType)type
{
    if (type == EMConversationTypeChat) {
        if ([easeId isEqualToString:EMSYSTEMNOTIFICATIONID]) {
            return [UIImage imageNamed:@"systemNotify"];
        }
        return [UIImage imageNamed:@"defaultAvatar"];
    }
    if (type == EMConversationTypeGroupChat) {
        return [UIImage imageNamed:@"groupConversation"];
    }
    if (type == EMConversationTypeChatRoom) {
        return [UIImage imageNamed:@"chatroomConversation"];
    }
    return [UIImage imageNamed:@"defaultAvatar"];
}

@end
