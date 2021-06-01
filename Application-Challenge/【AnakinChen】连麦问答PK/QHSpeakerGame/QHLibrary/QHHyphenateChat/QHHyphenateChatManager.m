//
//  QHHyphenateChatManager.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import "QHHyphenateChatManager.h"

#import "QHAppContext.h"

#import <HyphenateChat/HyphenateChat.h>

#import "QHHyphenateChatManagerDefine.h"

@interface QHHyphenateChatManager ()

@end

@implementation QHHyphenateChatManager

- (void)p_setup {
}

- (void)p_cmdSend:(QHHCAction)action msg:(NSDictionary *)msg {
    NSString *from = [[EMClient sharedClient] currentUsername];
    NSString *roomId = [QHAppContext context].EMRoomId;
    EMCmdMessageBody *body = [[EMCmdMessageBody alloc] initWithAction:[NSString stringWithFormat:@"%lu", (unsigned long)action]];
    EMMessage *message = [[EMMessage alloc] initWithConversationID:roomId from:from to:roomId body:body ext:msg];
    message.chatType = EMChatTypeChatRoom;
    [[EMClient sharedClient].chatManager sendMessage:message progress:^(int progress) {
    } completion:^(EMMessage *message, EMError *error) {
    }];
}

- (NSDictionary *)join {
    NSDictionary *msg = @{@"op": @(QHHCActionJoin), @"n": [EMClient sharedClient].currentUsername, @"host": @([QHAppContext context].isHost)};
    [self p_cmdSend:QHHCActionJoin msg:msg];
    return msg;
}

- (NSDictionary *)began:(NSString *)qkey players:(NSArray *)ps {
    NSDictionary *msg = @{@"op": @(QHHCActionBegan), @"host": [EMClient sharedClient].currentUsername, @"t": qkey, @"ps": ps};
    [self p_cmdSend:QHHCActionBegan msg:msg];
    return msg;
}

- (NSDictionary *)result:(NSString *)an {
    NSDictionary *msg = @{@"op": @(QHHCActionResult), @"n": [EMClient sharedClient].currentUsername, @"an": an ? : @"", @"res": @(an != nil)};
    [self p_cmdSend:QHHCActionResult msg:msg];
    return msg;
}

- (NSDictionary *)nextplay:(NSString *)user questionIdx:(NSInteger)idx {
    NSDictionary *msg = @{@"op": @(QHHCActionNext), @"nextP": user, @"nextQ": @(idx)};
    [self p_cmdSend:QHHCActionNext msg:msg];
    return msg;
}

- (NSDictionary *)end {
    NSDictionary *msg = @{@"op": @(QHHCActionEnd)};
    [self p_cmdSend:QHHCActionEnd msg:msg];
    return msg;
}

- (NSDictionary *)end4Win:(NSString *)userName {
    NSDictionary *msg = @{@"op": @(QHHCActionEnd4Win), @"n": userName ? : @""};
    [self p_cmdSend:QHHCActionEnd4Win msg:msg];
    return msg;
}

- (NSDictionary *)sayHello {
    NSString *c = @"你好呀^_^";
    NSDictionary *msg = @{@"op": @(QHHCActionChat), @"c": c, @"n": [EMClient sharedClient].currentUsername};
    [self p_cmdSend:QHHCActionChat msg:msg];
    return msg;
}

- (NSDictionary *)welcome {
    NSDictionary *msg = @{@"op": @(QHHCActionWelcome), @"n": [EMClient sharedClient].currentUsername};
    [self p_cmdSend:QHHCActionWelcome msg:msg];
    return msg;
}

+ (NSString *)id2json:(id)data {
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    NSString *jsonTemp = [jsonString stringByReplacingOccurrencesOfString:@"\n" withString:@""];
    return jsonTemp;
}

@end
