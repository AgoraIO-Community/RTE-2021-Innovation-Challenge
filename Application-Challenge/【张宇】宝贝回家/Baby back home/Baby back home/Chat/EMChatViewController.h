//
//  EMChatViewController.h
//  EaseIM
//
//  Created by 娜塔莎 on 2020/11/27.
//  Copyright © 2020 娜塔莎. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface EMChatViewController : UIViewController
@property (nonatomic, strong) EMConversation *conversation;
@property (nonatomic, strong) EaseChatViewController *chatController;

- (instancetype)initWithConversationId:(NSString *)conversationId conversationType:(EMConversationType)conType;
//本地通话记录
- (void)insertLocationCallRecord:(NSNotification*)noti;

- (NSArray *)formatMessages:(NSArray<EMMessage *> *)aMessages;

@end

NS_ASSUME_NONNULL_END
