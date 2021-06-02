//
//  ChatListVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/25.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import "ChatListVC.h"
#import "ConversationVC.h"
#import "EMChatViewController.h"
@interface ChatListVC ()<EaseConversationsViewControllerDelegate>

@end

@implementation ChatListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.delegate = self;
    // Do any additional setup after loading the view.
}
 

- (void)easeTableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    EaseConversationCell *cell = (EaseConversationCell*)[tableView cellForRowAtIndexPath:indexPath];
    
//    [[NSNotificationCenter defaultCenter] postNotificationName:CHAT_PUSHVIEWCONTROLLER object:cell.model];
    [self handlePushChatController:cell.model];

}


- (void)handlePushChatController:(id)aNotif
{
    id object = aNotif;
    EMConversationType type = -1;
    NSString *conversationId = nil;
    if ([object isKindOfClass:[NSString class]]) {
        conversationId = (NSString *)object;
        type = EMConversationTypeChat;
    } else if ([object isKindOfClass:[EMGroup class]]) {
        EMGroup *group = (EMGroup *)object;
        conversationId = group.groupId;
        type = EMConversationTypeGroupChat;
    } else if ([object isKindOfClass:[EMChatroom class]]) {
        EMChatroom *chatroom = (EMChatroom *)object;
        conversationId = chatroom.chatroomId;
        type = EMConversationTypeChatRoom;
    } else if ([object isKindOfClass:[EaseConversationModel class]]) {
        EaseConversationModel *model = (EaseConversationModel *)object;
        conversationId = model.easeId;
        type = model.type;
    }
    
    
    
    EMChatViewController *controller = [[EMChatViewController alloc]initWithConversationId:conversationId conversationType:type];
    

    
    
    
//    EaseChatViewModel *viewModel = [[EaseChatViewModel alloc]init];
//
//    ConversationVC *vc = [[ConversationVC alloc] init];
//    vc.aType = type;
//    vc.conID = conversationId;
    controller.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:controller animated:YES];
    
}



//- (id<EaseUserDelegate>)easeUserDelegateAtConversationId:(NSString *)conversationId
//                                        conversationType:(EMConversationType)type
//{
//    NSLog(@"&&&&&&&&&&");
//}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
