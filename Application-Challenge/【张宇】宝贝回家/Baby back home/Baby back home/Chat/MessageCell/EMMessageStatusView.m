//
//  EMMessageStatusView.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/1/25.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMMessageStatusView.h"
#import "LoadingCALayer.h"
#import "OneLoadingAnimationView.h"

@interface EMMessageStatusView()

@property (nonatomic, strong) UILabel *label;
@property (nonatomic, strong) UIActivityIndicatorView *activityView;

@property (nonatomic, strong) NSTimer *timer;

@end

@implementation EMMessageStatusView

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.hidden = YES;
        self.backgroundColor = [UIColor clearColor];
    }
    
    return self;
}

#pragma mark - Subviews

- (UILabel *)label
{
    if (_label == nil) {
        _label = [[UILabel alloc] init];
        _label.textColor = [UIColor grayColor];
        _label.font = [UIFont systemFontOfSize:13];
    }
    
    return _label;
}

#pragma mark - Public

- (void)setSenderStatus:(EMMessageStatus)aStatus
            isReadAcked:(BOOL)aIsReadAcked
{
    if (aStatus == EMMessageStatusDelivering) {
        self.hidden = NO;
        [_label removeFromSuperview];
    } else if (aStatus == EMMessageStatusFailed) {
        self.hidden = NO;
        [_label removeFromSuperview];
    } else if (aStatus == EMMessageStatusSucceed) {
        self.hidden = NO;
        self.label.text = aIsReadAcked ? @"已读" : nil;
        [self addSubview:self.label];
        [self.label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
    } else {
        self.hidden = YES;
        [_label removeFromSuperview];
    }
}

@end
