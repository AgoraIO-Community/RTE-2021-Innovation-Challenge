//
//  ConferenceController.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 23/11/2016.
//  Copyright © 2016 XieYajie. All rights reserved.
//

#import "ConferenceController.h"

#import "SingleCallController.h"

#import "EMGlobalVariables.h"

#import "UserInfoStore.h"

static ConferenceController *confManager = nil;

@interface ConferenceController()

@property (strong, nonatomic) UINavigationController *confNavController;

@end


@implementation ConferenceController

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
        confManager = [[ConferenceController alloc] init];
    });
    
    return confManager;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

#pragma mark - public

- (void)communicateConference:(EMConversation *)conversation rootController:(UIViewController *)controller
{
    ConfInviteType inviteType = ConfInviteTypeGroup;
    if (conversation.type == EMChatTypeChatRoom)
        inviteType = ConfInviteTypeChatroom;
    [self inviteMemberWithInviteType:inviteType conversationId:conversation.conversationId chatType:(EMChatType)conversation.type popFromController:controller];
}

#pragma mark - private

- (void)_initManager
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleMakeConference:) name:CALL_MAKECONFERENCE object:nil];
}

#pragma mark - conference

- (void)inviteMemberWithInviteType:(ConfInviteType)aInviteType
                  conversationId:(NSString *)aConversationId
                        chatType:(EMChatType)aChatType
               popFromController:(UIViewController *)aController
{
    ConfInviteUsersViewController *controller = [[ConfInviteUsersViewController alloc] initWithType:aInviteType isCreate:YES excludeUsers:@[[EMClient sharedClient].currentUsername] groupOrChatroomId:aConversationId];
    
    [controller setDoneCompletion:^(NSArray *aInviteUsers) {
        gIsCalling = YES;
        for (NSString* strId in aInviteUsers) {
            EMUserInfo* info = [[UserInfoStore sharedInstance] getUserInfoById:strId];
            if(info && (info.avatarUrl.length > 0 || info.nickName > 0)) {
                EaseCallUser* user = [EaseCallUser userWithNickName:info.nickName image:[NSURL URLWithString:info.avatarUrl]];
                [[[EaseCallManager sharedManager] getEaseCallConfig] setUser:strId info:user];
            }
        }
        [[EaseCallManager sharedManager] startInviteUsers:aInviteUsers ext:@{@"groupId":aConversationId} completion:^(NSString * callId, EaseCallError * aError) {
            
        }];
    }];
    self.confNavController = [[UINavigationController alloc] initWithRootViewController:controller];
    self.confNavController.modalPresentationStyle = UIModalPresentationFullScreen;
    [aController presentViewController:self.confNavController animated:YES completion:nil];
}

#pragma mark - NSNotification
//会议发起人
- (void)handleMakeConference:(NSNotification *)aNotif
{
    NSDictionary *dic = aNotif.object;
    id model = [dic objectForKey:CALL_MODEL];
    
    NSString *conversationId = nil;
    ConfInviteType inviteType = ConfInviteTypeGroup;
    EMChatType chatType = EMChatTypeChat;
    if ([model isKindOfClass:[EMConversation class]]) {
        EMConversation *conversation = (EMConversation *)model;
        conversationId = conversation.conversationId;
        chatType =(EMChatType)conversation.type;
        if (conversation.type == EMChatTypeChatRoom)
            inviteType = ConfInviteTypeChatroom;
    }
    
    UIViewController *controller = [dic objectForKey:NOTIF_NAVICONTROLLER];
    if (controller == nil) {
        UIWindow *window = [[UIApplication sharedApplication] keyWindow];
        controller = window.rootViewController;
    }
    
    [self inviteMemberWithInviteType:inviteType
                    conversationId:conversationId
                          chatType:chatType
                 popFromController:controller];
}

@end
