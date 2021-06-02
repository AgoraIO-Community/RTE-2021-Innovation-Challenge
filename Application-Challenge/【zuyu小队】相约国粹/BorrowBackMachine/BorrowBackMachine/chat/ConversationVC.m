//
//  ConversationVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/25.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import "ConversationVC.h"

@interface ConversationVC ()

@property(nonatomic,strong) EaseChatViewController *chatController;
@end

@implementation ConversationVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    EaseChatViewModel *viewModel = [[EaseChatViewModel alloc]init];
    self.chatController = [EaseChatViewController initWithConversationId:self.conID
                                                  conversationType:self.aType
                                                      chatViewModel:viewModel];
    [self addChildViewController:self.chatController];
    [self.view addSubview:self.chatController.view];
    self.chatController.view.frame = self.view.bounds;
    
   self.conversation = [EMClient.sharedClient.chatManager getConversationWithConvId:self.conID];
    
    [[EMClient sharedClient].chatManager ackConversationRead:_conversation.conversationId completion:nil];

    
    [self loadData:YES];
    // Do any additional setup after loading the view.
}

- (void)loadData:(BOOL)isScrollBottom
{
    __weak typeof(self) weakself = self;
    void (^block)(NSArray *aMessages, EMError *aError) = ^(NSArray *aMessages, EMError *aError) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [weakself.chatController refreshTableViewWithData:aMessages  isInsertBottom:NO  isScrollBottom:isScrollBottom];
        });
    };
    
    
    [self.conversation loadMessagesStartFromId:self.conID count:50 searchDirection:EMMessageSearchDirectionUp completion:block];
}

//loadMoreMessageData
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
