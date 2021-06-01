//
//  QHChatBaseNewDataView.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/25.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseNewDataView.h"

@interface QHChatBaseNewDataView ()

@property (nonatomic, strong) UILabel *titleL;
@property (nonatomic) NSInteger currentDataCount;

@end

@implementation QHChatBaseNewDataView

#pragma mark - Public

+ (instancetype)createViewToSuperView:(UIView *)superView {
    QHChatBaseNewDataView *view = [[self alloc] init];
    [superView addSubview:view];
    
    view.translatesAutoresizingMaskIntoConstraints = NO;
    NSDictionary *viewsDict = NSDictionaryOfVariableBindings(view);
    [superView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[view(26)]-0-|" options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    [superView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:[view(100)]" options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    [superView addConstraint:[NSLayoutConstraint constraintWithItem:view attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:superView attribute:NSLayoutAttributeCenterX multiplier:1 constant:0]];
    
    [view p_setup];
    
    return view;
}

#pragma mark - Private

- (void)p_setup {
    self.backgroundColor = [UIColor whiteColor];
    self.layer.cornerRadius = 13;
    self.clipsToBounds = YES;
    self.hidden = YES;
    
    UILabel *titleL = [[UILabel alloc] initWithFrame:CGRectZero];
    titleL.textAlignment = NSTextAlignmentCenter;
    titleL.textColor = [UIColor orangeColor];
    titleL.font = [UIFont systemFontOfSize:12];
    //        titleL.text = [NSString stringWithFormat:@"+%lu条新消息", (unsigned long)self.chatDatasTempArray.count];
    [self addSubview:titleL];
    
    titleL.translatesAutoresizingMaskIntoConstraints = NO;
    NSDictionary *subViewsDict = NSDictionaryOfVariableBindings(titleL);
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[titleL]-0-|" options:NSLayoutFormatAlignAllBaseline metrics:0 views:subViewsDict]];
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[titleL]-0-|" options:NSLayoutFormatAlignAllBaseline metrics:0 views:subViewsDict]];
    
    _titleL = titleL;
    titleL = nil;
}

#pragma mark - QHChatBaseNewDataViewProtcol

- (void)show {
    if (self.isHidden == YES) {
        self.hidden = NO;
    }
}

- (BOOL)hide {
    if (self.isHidden == NO) {
        self.hidden = YES;
        return YES;
    }
    return NO;
}

- (void)update:(id)data {
    if ([data isKindOfClass:[NSNumber class]] == YES) {
        [self show];
        NSInteger newDataCount = [data integerValue];
        if (newDataCount != _currentDataCount) {
            _currentDataCount = newDataCount;
            _titleL.text = [NSString stringWithFormat:@"+%lu条新消息", (unsigned long)_currentDataCount];
        }
    }
}

@end
