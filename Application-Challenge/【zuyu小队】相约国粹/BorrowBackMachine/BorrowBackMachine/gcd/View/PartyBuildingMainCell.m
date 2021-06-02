//
//  PartyBuildingMainCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/16.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "PartyBuildingMainCell.h"
@interface PartyBuildingMainCell()
{
    UIImageView *_bgImageView;
    UIImageView *_centerImageView;
    UILabel     *_titleLabel;
}
@end
@implementation PartyBuildingMainCell


- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        _bgImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        
        [self addSubview:_bgImageView];
        
        _centerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(35, 25, frame.size.width - 70, frame.size.height - 70)];
        
        [self addSubview:_centerImageView];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_centerImageView.frame), frame.size.width, 35)];
        
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        
        
        [self addSubview:_titleLabel];
    }
    return self;
}

-(void)setBgImage:(NSString *)bgImage
{
    _bgImageView.image = [UIImage imageNamed:bgImage];
}


-(void)setCenterImage:(NSString *)centerImage
{
    _centerImageView.image = [UIImage imageNamed:centerImage];
}

-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}

@end
