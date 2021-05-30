//
//  QHTKRoomChatVIewUtil+Room.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import "QHTKRoomChatVIewUtil+Room.h"

#import "QHChatBaseUtil.h"

@implementation QHTKRoomChatVIewUtil (Room)

+ (NSMutableAttributedString *)toHello:(NSDictionary *)data {
    NSString *n = data[@"n"];
    NSString *c = data[@"c"];
    NSString *formatString = @"<font color='#4FCCFF'>%@：</font><font color='#4FCCFF'>%@</font>";
    NSString *contentString = [NSString stringWithFormat:formatString, n, c];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toWelcome:(NSDictionary *)data {
    NSString *n = data[@"n"];
    NSString *formatString = @"<font color='#FFFFFF'>欢迎</font><font color='#4FCCFF'> %@ </font><font color='#FFFFFF'>进入房间</font>";
    NSString *contentString = [NSString stringWithFormat:formatString, n];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

@end
