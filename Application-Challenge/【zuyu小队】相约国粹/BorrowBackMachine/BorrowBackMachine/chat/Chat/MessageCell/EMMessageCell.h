//
//  EMMessageCell.h
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/1/25.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EMMsgPicMixTextBubbleView.h"
#import "EMMsgBubbleView.h"

#define avatarLonger 40
#define componentSpacing 10

NS_ASSUME_NONNULL_BEGIN

@protocol EMMessageCellDelegate;
@interface EMMessageCell : UITableViewCell

@property (nonatomic, weak) id<EMMessageCellDelegate> delegate;
@property (nonatomic, strong) EMMsgBubbleView *msgView;
@property (nonatomic) EMMessageDirection direction;
@property (nonatomic, strong) EaseMessageModel *model;

- (instancetype)initWithDirection:(EMMessageDirection)aDirection
                             type:(EMMessageType)aType
                          msgView:(EMMsgBubbleView*)aMsgView;

@end


@protocol EMMessageCellDelegate <NSObject>

@optional
- (void)messageCellDidSelected:(EMMessageCell *)aCell;
- (void)messageAvatarDidSelected:(EaseMessageModel *)model;

@end

NS_ASSUME_NONNULL_END
