//
//  EMMessageCell.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/1/25.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMMessageCell.h"

#import "EMMessageStatusView.h"
#import "EMMsgPicMixTextBubbleView.h"
#import "UserInfoStore.h"
#import <UIImageView+WebCache.h>
#import "UIImageView+UserInfo.h"

@interface EMMessageCell()

@property (nonatomic, strong) UIImageView *avatarView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) EMMessageStatusView *statusView;

@end

@implementation EMMessageCell

- (instancetype)initWithDirection:(EMMessageDirection)aDirection
                             type:(EMMessageType)aType
                          msgView:(nonnull EMMsgBubbleView *)aMsgView

{
    NSString *identifier = [EMMessageCell cellIdentifierWithDirection:aDirection type:aType];
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    if (self) {
        _direction = aDirection;
        self.msgView = aMsgView;
        [self _setupViewsWithType:aType];
    }
    
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

#pragma mark - Class Methods

+ (NSString *)cellIdentifierWithDirection:(EMMessageDirection)aDirection
                                     type:(EMMessageType)aType
{
    NSString *identifier = @"EMMsgCellDirectionSend";
    if (aDirection == EMMessageDirectionReceive) {
        identifier = @"EMMsgCellDirectionRecv";
    }
    if (aType == EMMessageTypePictMixText) {
        return [NSString stringWithFormat:@"%@PictMixText", identifier];
    }
    return identifier;
}

#pragma mark - Subviews

- (void)_setupViewsWithType:(EMMessageType)aType
{
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    self.backgroundColor = [UIColor clearColor];
    
    _avatarView = [[UIImageView alloc] init];
    _avatarView.contentMode = UIViewContentModeScaleAspectFit;
    _avatarView.backgroundColor = [UIColor clearColor];
    _avatarView.userInteractionEnabled = YES;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(messageAvatarDidSelected:)];
    [_avatarView addGestureRecognizer:tap];
    _avatarView.layer.cornerRadius = 8;
    [self.contentView addSubview:_avatarView];
    if (self.direction == EMMessageDirectionSend) {
        [_avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.contentView).offset(15);
            make.right.equalTo(self.contentView).offset(-2*componentSpacing);
            make.width.height.equalTo(@(avatarLonger));
        }];
    } else {
        [_avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.contentView).offset(15);
            make.left.equalTo(self.contentView).offset(2*componentSpacing);
            make.width.height.equalTo(@(avatarLonger));
        }];
        
        _nameLabel = [[UILabel alloc] init];
        _nameLabel.font = [UIFont systemFontOfSize:13];
        _nameLabel.textColor = [UIColor grayColor];
        if (_model.message.chatType != EMChatTypeChat) {
            [self.contentView addSubview:_nameLabel];
            [_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(self.avatarView);
                make.left.equalTo(self.avatarView.mas_right).offset(8);
                make.right.equalTo(self.contentView).offset(-componentSpacing);
            }];
        }
    }
    
//    self.msgView = [self _getBubbleViewWithType];
    self.msgView.userInteractionEnabled = YES;
    self.msgView.clipsToBounds = YES;
    [self.contentView addSubview:_msgView];
    if(self.direction == EMMessageDirectionSend) {
        [_msgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.contentView).with.offset(60);
            make.top.equalTo(self.contentView).with.offset(10);
            make.right.equalTo(self.avatarView.mas_left).offset(-10);
            make.height.equalTo(@120);
            make.bottom.equalTo(self.contentView).with.offset(-10);
        }];
    }else{
        [_msgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.avatarView.mas_right).with.offset(10);
            make.top.equalTo(self.contentView).with.offset(10);
            make.right.equalTo(self.contentView).offset(-60);
            make.height.equalTo(@120);
            make.bottom.equalTo(self.contentView).with.offset(-10);
        }];
    }

    _statusView = [[EMMessageStatusView alloc] init];
    [self.contentView addSubview:_statusView];
    if (self.direction == EMMessageDirectionSend) {
        [_statusView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.msgView.mas_centerY);
            make.right.equalTo(self.msgView.mas_left).offset(-5);
            make.height.equalTo(@(componentSpacing * 2));
        }];
    } else {
        _statusView.backgroundColor = [UIColor redColor];
        _statusView.clipsToBounds = YES;
        _statusView.layer.cornerRadius = 4;
        [_statusView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.msgView);
            make.left.equalTo(self.msgView.mas_right).offset(5);
            make.width.height.equalTo(@8);
        }];
    }
}

//- (EMMsgPicMixTextBubbleView *)_getBubbleViewWithType
//{
//    self.msgView = [[EMMsgPicMixTextBubbleView alloc]init];
//    if (self.direction == EMMessageDirectionSend) {
//        self.msgView.image = [[UIImage imageNamed:@"msg_bg_send"] stretchableImageWithLeftCapWidth:15 topCapHeight:15];
//    } else {
//        self.msgView.image = [[UIImage imageNamed:@"msg_bg_recv"] stretchableImageWithLeftCapWidth:15 topCapHeight:15];
//    }
//    if (self.msgView) {
//        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(bubbleViewTapAction:)];
//        [self.msgView addGestureRecognizer:tap];
//    }
//
//    return self.msgView;
//}

#pragma mark - Setter

- (void)setModel:(EaseMessageModel *)model
{
    _model = model;
    if (self.msgView) {
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(bubbleViewTapAction:)];
        [self.msgView addGestureRecognizer:tap];
    }
    //[self.msgView setModel:_model];
    if (model.direction == EMMessageDirectionSend) {
        [self.statusView setSenderStatus:model.message.status isReadAcked:model.message.isReadAcked];
    } else {
        self.nameLabel.text = model.message.from;
        if (model.type == EMMessageTypePictMixText) {
            if ([((EMTextMessageBody *)model.message.body).text isEqualToString:EMCOMMUNICATE_CALLED_MISSEDCALL])
                self.statusView.hidden = model.message.isReadAcked;
            else self.statusView.hidden = YES;
        }
    }
    _avatarView.image = [UIImage imageNamed:@"defaultAvatar"];
    [_avatarView showUserInfoAvatar:model.message.from];
}

#pragma mark - Action

//头像点击
- (void)messageAvatarDidSelected:(UITapGestureRecognizer *)aTap
{
    if (aTap.state == UIGestureRecognizerStateEnded) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(messageAvatarDidSelected:)]) {
            [self.delegate messageAvatarDidSelected:_model];
        }
    }
}
//气泡点击
- (void)bubbleViewTapAction:(UITapGestureRecognizer *)aTap
{
    if (aTap.state == UIGestureRecognizerStateEnded) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(messageCellDidSelected:)]) {
            [self.delegate messageCellDidSelected:self];
        }
    }
}

@end
