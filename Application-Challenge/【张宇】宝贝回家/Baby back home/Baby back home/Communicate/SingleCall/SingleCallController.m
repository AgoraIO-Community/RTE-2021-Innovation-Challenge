//
//  SingleCallController.m
//  EMiOS_IM
//
//  Created by XieYajie on 22/11/2016.
//  Copyright © 2016 XieYajie. All rights reserved.
//

#import "SingleCallController.h"

#import "EMGlobalVariables.h"
#import "EMChatViewController.h"

static SingleCallController *callManager = nil;

@interface SingleCallController()


@end


@implementation SingleCallController

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self _initManager];
    }
    
    return self;
}

+ (instancetype)sharedManager
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        callManager = [[SingleCallController alloc] init];
    });
    
    return callManager;
}

- (void)dealloc
{
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:CALL_MAKE1V1 object:nil];
}

#pragma mark - private

- (void)_initManager
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleMakeSingleCall:) name:CALL_MAKE1V1 object:nil];
}

#pragma mark - NSNotification

//主叫方
- (void)handleMakeSingleCall:(NSNotification*)notify
{
    if (!notify.object) {
        return;
    }
    EaseCallType aType = (EaseCallType)[[notify.object objectForKey:CALL_TYPE] integerValue];
    _chatter = [notify.object valueForKey:CALL_CHATTER] ;
    EMConversation* conversation = [[[EMClient sharedClient] chatManager] getConversationWithConvId:_chatter];
    NSString*msgId = [conversation latestMessage].messageId;
    [[EaseCallManager sharedManager] startSingleCallWithUId:_chatter type:aType ext:nil completion:^(NSString * _Nullable callId, EaseCallError * _Nullable aError) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1000), dispatch_get_main_queue(), ^{
            [conversation loadMessagesStartFromId:msgId count:50 searchDirection:msgId?EMMessageSearchDirectionDown:EMMessageSearchDirectionUp completion:^(NSArray *aMessages, EMError *aError) {
                if(aMessages.count)
                    [self insertLocationCallRecord:aMessages];
            }];
        });
    }];
//    [[EaseCallManager sharedManager] startSingleCallWithUId:_chatter type:aType ext:nil completion:^(NSString * callId, EaseCallError * aError) {
//            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1000), dispatch_get_main_queue(), ^{
//                [conversation loadMessagesStartFromId:msgId count:50 searchDirection:msgId?EMMessageSearchDirectionDown:EMMessageSearchDirectionUp completion:^(NSArray *aMessages, EMError *aError) {
//                    if(aMessages.count)
//                        [self insertLocationCallRecord:aMessages];
//                }];
//            });
//    }];
    return;
}

#pragma mark - public

//插入本地通话记录
- (void)insertLocationCallRecord:(NSArray*)messages
{
    [[NSNotificationCenter defaultCenter] postNotificationName:EMCOMMMUNICATE_RECORD object:@{@"msg":messages}];//刷新页面
   // EMMessage *message = [[EMMessage alloc] initWithConversationID:callSession.remoteName from:from to:to body:body ext:ext];
//    message.direction = [from isEqualToString:[[EMClient sharedClient] currentUsername]] ? EMMessageDirectionSend : EMMessageDirectionReceive;
//    message.chatType = EMChatTypeChat;
//    [conversation appendMessage:message error:nil];
//    [[NSNotificationCenter defaultCenter] postNotificationName:EMCOMMMUNICATE_RECORD object:@{@"msg":message}];//刷新页面
}

//发送通话记录
- (void)sendCallRecord:(NSString *)missedCallDirection
{
    //通话类型
//    NSString *callTypeStr;
//    if (callType == EMCallTypeVoice) {
//        callTypeStr = EMCOMMUNICATE_TYPE_VOICE;
//    } else if (callType == EMCallTypeVideo) {
//        callTypeStr = EMCOMMUNICATE_TYPE_VIDEO;
//    }
//    //主叫方发送通话信息
//    if ([self.callDirection isEqualToString:EMCOMMUNICATE_DIRECTION_CALLINGPARTY]) {
//        if (self.callDurationTime) {
//            [[NSNotificationCenter defaultCenter] postNotificationName:EMCOMMMUNICATE object:@{EMCOMMUNICATE_TYPE:callTypeStr,EMCOMMUNICATE_DURATION_TIME:self.callDurationTime,EMCOMMUNICATE_MISSED_CALL:@""}];
//        } else {
//            [[NSNotificationCenter defaultCenter] postNotificationName:EMCOMMMUNICATE object:@{EMCOMMUNICATE_TYPE:callTypeStr,EMCOMMUNICATE_DURATION_TIME:@"",EMCOMMUNICATE_MISSED_CALL:missedCallDirection}];
//        }
//    }
//    self.callDurationTime = nil;
}

@end
