//
//  QHTKRoomChatContentViewCell.h
//  QHChatDemo
//
//  Created by Anakin chen on 2019/11/8.
//  Copyright © 2019 Chen Network Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "QHChatBaseViewCell.h"

NS_ASSUME_NONNULL_BEGIN

#define TKQHCHAT_LC_CONTENT_EDGEINSETS UIEdgeInsetsMake(4, 0, 0, 0)
#define TKQHCHAT_LC_CONTENT_TEXT_EDGEINSETS UIEdgeInsetsMake(1, 3, 1, 3)

@interface QHTKRoomChatContentViewCell : QHChatBaseViewCell

@property (nonatomic, strong) UIView *contentV;

- (void)cellUpdateConstraints;

@end

NS_ASSUME_NONNULL_END
