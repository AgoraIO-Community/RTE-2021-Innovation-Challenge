//
//  QHTKRoomChatContentViewCell.m
//  QHChatDemo
//
//  Created by Anakin chen on 2019/11/8.
//  Copyright © 2019 Chen Network Technology. All rights reserved.
//

#import "QHTKRoomChatContentViewCell.h"

#import "QHViewUtil.h"

@implementation QHTKRoomChatContentViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    return self;
}

#pragma mark - Public

// 使用预测 Cell 高度，更新约束会导致预测失效
- (void)cellUpdateConstraints {
//    [self.contentL updateConstraints];
}

#pragma mark - Private

- (void)setup {
    self.backgroundColor = [UIColor clearColor];
    self.contentView.backgroundColor = [UIColor clearColor];
    [self p_addContentView];
    [self p_addContentLabel];
    [self addTapGesture];
}

- (void)p_addContentView {
    _contentV = [[UIView alloc] initWithFrame:CGRectZero];
    _contentV.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:_contentV];
    
    UIView *subView = _contentV;
    subView.translatesAutoresizingMaskIntoConstraints = NO;
    NSDictionary *viewsDict = NSDictionaryOfVariableBindings(subView);
    
    NSString *lcString = [NSString stringWithFormat:@"|-%f-[subView]->=%f-|", TKQHCHAT_LC_CONTENT_EDGEINSETS.left, TKQHCHAT_LC_CONTENT_EDGEINSETS.right];
    [subView.superview addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:lcString options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    NSString *lcString2 = [NSString stringWithFormat:@"V:|-%f-[subView]-%f-|", TKQHCHAT_LC_CONTENT_EDGEINSETS.top, TKQHCHAT_LC_CONTENT_EDGEINSETS.bottom];
    [subView.superview addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:lcString2 options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    
    UIView *bgV = [[UIView alloc] initWithFrame:CGRectZero];
    bgV.backgroundColor = [UIColor colorWithWhite:0 alpha:0.3];
    bgV.layer.masksToBounds = true;
    bgV.layer.cornerRadius = 10;
    [subView addSubview:bgV];
    [QHViewUtil fullScreen:bgV];
    
}

- (void)p_addContentLabel {
    self.contentL = [[UILabel alloc] initWithFrame:CGRectZero];
    self.contentL.font = [UIFont systemFontOfSize:13];
    self.contentL.numberOfLines = 0;
    self.contentL.lineBreakMode = NSLineBreakByWordWrapping;
    self.contentL.backgroundColor = [UIColor clearColor];
    [_contentV addSubview:self.contentL];
    [QHViewUtil fullScreen:self.contentL edgeInsets:TKQHCHAT_LC_CONTENT_TEXT_EDGEINSETS];
}

@end
