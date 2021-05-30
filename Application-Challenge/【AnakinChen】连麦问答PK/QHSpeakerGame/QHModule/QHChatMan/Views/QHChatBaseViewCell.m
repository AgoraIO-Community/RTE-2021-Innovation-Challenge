//
//  QHChatBaseViewCell.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/23.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseViewCell.h"

#import "QHViewUtil.h"

@interface QHChatBaseViewCell ()

//@property (nonatomic, strong, readwrite) UILabel *contentL;
//@property (nonatomic, strong, readwrite) UITextView *contentTV;

@end

@implementation QHChatBaseViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setup];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

#pragma mark - Public

- (void)makeContent:(UIEdgeInsets)edgeInsets {
    [QHViewUtil fullScreen:_contentL edgeInsets:edgeInsets];
}

- (void)addTapGesture {
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGestureAction:)];
    [self addGestureRecognizer:tap];
    self.backgroundColor = [UIColor clearColor];
    tap = nil;
}

#pragma mark - Private

- (void)setup {
    [self p_addContentLabel];
    [self addTapGesture];
}

- (void)p_addContentLabel {
    _contentL = [[UILabel alloc] initWithFrame:CGRectZero];
    _contentL.font = [UIFont systemFontOfSize:13];
    _contentL.numberOfLines = 0;
    _contentL.lineBreakMode = NSLineBreakByWordWrapping;
    _contentL.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:_contentL];
//    [QHViewUtil fullScreen:_contentL];
}

//- (void)p_addContentTextView {
//    _contentTV = [UITextView new];
//    _contentTV.font = [UIFont systemFontOfSize:13];
//    _contentTV.editable = NO;
//    _contentTV.scrollEnabled = NO;
//    _contentTV.textContainerInset = UIEdgeInsetsZero;
//    _contentTV.textContainer.lineFragmentPadding = 0;
//    [self.contentView addSubview:_contentTV];
//    [QHViewUtil fullScreen:_contentTV];
//}

#pragma mark - Action

- (void)tapGestureAction:(id)sender {
    if (_contentL.attributedText == nil) {
        return;
    }
    if (self.frame.size.width <= _contentL.attributedText.size.width) {
        if ([self.delegate respondsToSelector:@selector(selectViewCell:)]) {
            [self.delegate selectViewCell:self];
        }
    }
    else {
        UITapGestureRecognizer *tap = sender;
        CGPoint touchPoint = [tap locationInView:_contentL];
        if (_contentL.attributedText.size.width < touchPoint.x) {
            if ([self.delegate respondsToSelector:@selector(deselectViewCell:)]) {
                [self.delegate deselectViewCell:self];
            }
        }
        else {
            if ([self.delegate respondsToSelector:@selector(selectViewCell:)]) {
                [self.delegate selectViewCell:self];
            }
        }
    }
}

@end
