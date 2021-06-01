//
//  QHTKRoomChatVIewUtil.m
//  QHChatDemo
//
//  Created by Anakin chen on 2019/11/8.
//  Copyright © 2019 Chen Network Technology. All rights reserved.
//

#import "QHTKRoomChatVIewUtil.h"

#import "QHChatBaseUtil.h"

@implementation QHTKRoomChatVIewUtil

+ (NSMutableAttributedString *)toChat:(NSDictionary *)data isAnchor:(BOOL)anchor isCurrentUser:(BOOL)user {
    NSDictionary *body = data[@"body"];
    NSString *n = body[@"n"];
    NSString *c = body[@"c"];
    NSString *formatString = nil;
    if (anchor == YES) {
        formatString = @"<font color='#FF55A2'>%@：</font><font color='#FF509F'>%@</font>";
    }
    else if (user == YES) {
        formatString = @"<font color='#4FCCFF'>%@：</font><font color='#4FCCFF'>%@</font>";
    }
    else {
        formatString = @"<font color='#FFFFFF'>%@：</font><font color='#FFFFFF'>%@</font>";
    }
    NSString *contentString = [NSString stringWithFormat:formatString, n, c];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toNotice:(NSDictionary *)data {
    NSString *c = data[@"c"];
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>%@</font>", c];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

@end
