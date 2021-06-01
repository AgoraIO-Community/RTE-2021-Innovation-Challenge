//
//  QHTKRoomChatVIewUtil+Game.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import "QHTKRoomChatVIewUtil+Game.h"

#import "QHChatBaseUtil.h"

@implementation QHTKRoomChatVIewUtil (Game)

+ (NSMutableAttributedString *)toJoinGame:(NSDictionary *)data {
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>%@ 加入游戏</font>", data[@"n"]];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toBeganGame:(NSDictionary *)data {
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>题目：%@，准备开始游戏</font>", data[@"t"]];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toResultGame:(NSDictionary *)data {
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>%@ 回答：%@</font>", data[@"n"], [data[@"res"] boolValue] ? data[@"an"] : @"失败"];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toNextPlayerGame:(NSDictionary *)data {
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>轮到 %@ 作答</font>", data[@"nextP"]];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

+ (NSMutableAttributedString *)toEndGame:(NSDictionary *)data {
    NSString *contentString = [NSString stringWithFormat:@"<font color='#C9FCFF'>游戏结束</font>"];
    NSMutableAttributedString *chatData = [NSMutableAttributedString new];
    [chatData appendAttributedString:[QHChatBaseUtil toHTML:contentString]];
    
    return chatData;
}

@end
