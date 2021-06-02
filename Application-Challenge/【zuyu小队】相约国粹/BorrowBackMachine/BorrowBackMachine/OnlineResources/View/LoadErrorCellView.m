//
//  LoadErrorCellView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "LoadErrorCellView.h"

@implementation LoadErrorCellView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
        
        UIImageView * _cImage = [[UIImageView alloc] init];
        
        _cImage.frame = CGRectMake(10, frame.size.height / 2 - 100, 200, 171);

        _cImage.image = [UIImage imageNamed:@"CellLoadError"];
        
        [self addSubview:_cImage];
        
        UILabel *_cLabel = [[UILabel alloc] init];
        
        _cLabel.frame = CGRectMake(250, frame.size.height/2 - 20, WIDTH - 300, 40);

        _cLabel.textColor = [UIColor lightGrayColor];
        
        _cLabel.numberOfLines = 0;
        
        _cLabel.text = @"图书加载失败\n点击重新加载";
        
        _cLabel.font = [UIFont systemFontOfSize:15];
        
        [self addSubview:_cLabel];
        
    }
    return self;
}

@end
