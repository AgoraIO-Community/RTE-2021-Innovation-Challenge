//
//  ConversationVC.h
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/25.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ConversationVC : UIViewController
@property(nonatomic,strong) NSString *conID;
@property(nonatomic)  EMConversationType aType;
@property (nonatomic, strong) EMConversation *conversation;

@end

NS_ASSUME_NONNULL_END
