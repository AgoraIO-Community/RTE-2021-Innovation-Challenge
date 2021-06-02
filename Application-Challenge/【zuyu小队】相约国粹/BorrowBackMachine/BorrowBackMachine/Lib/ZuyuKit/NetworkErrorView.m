//
//  NetworkErrorView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "NetworkErrorView.h"

@implementation NetworkErrorView


-(instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
        
        UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH/2 - 100, 100, 200, 200)];
//        image.backgroundColor = [UIColor orangeColor];
        image.image = [UIImage imageNamed:@"loadError"];
        
        [self addSubview:image];
        
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(50, CGRectGetMaxY(image.frame) + 10, WIDTH - 100, 30)];
        
        label.textAlignment = NSTextAlignmentCenter;
        
        label.text = @"网络竟然崩溃了";
        
        label.textColor = [UIColor lightGrayColor];
        
        [self addSubview:label];
        
        UILabel *label2 = [[UILabel alloc] initWithFrame:CGRectMake(50, CGRectGetMaxY(label.frame) + 1, WIDTH - 100, 30)];
        
        label2.textAlignment = NSTextAlignmentCenter;
        
        label2.text = @"别紧张,试试看刷新界面~";
        
        label2.font = [UIFont systemFontOfSize:15];
        
        label2.textColor = [UIColor lightGrayColor];
        
        [self addSubview:label2];
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        button.frame = CGRectMake(WIDTH/2 - 60, CGRectGetMaxY(label2.frame) + 11, 120, 45);
        
        [button setImage:[UIImage imageNamed:@"refreshButton"] forState:UIControlStateNormal];
        
        [button addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:button];
    }
    
    return self;
}

-(void)click:(UIButton *)button
{
    [self.delegate refreshLoadResouce];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
