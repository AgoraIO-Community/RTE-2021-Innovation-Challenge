//
//  DownloadCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DownloadCell.h"

@interface DownloadCell()
{
    UIImageView *_imageView;
    UILabel     *_titleLabel;
    UILabel     *_countLabel;
}
@end

@implementation DownloadCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10,frame.size.width - 60 , frame.size.width - 60)];
        
        [self addSubview:_imageView];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0,frame.size.width - 50 , frame.size.width, 20)];
        
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        
        [self addSubview:_titleLabel];
        
        _countLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, frame.size.height - 25, frame.size.width, 15)];
        
        _countLabel.textAlignment = NSTextAlignmentCenter;
        
        _countLabel.textColor = [UIColor lightGrayColor];
        
        _countLabel.font = [UIFont systemFontOfSize:14];
        
        [self addSubview:_countLabel];
    }
    return self;
}

-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}

-(void)setImage:(NSString *)image
{
    _imageView.image = [UIImage imageNamed:image];
}

-(void)setCount:(NSString *)count
{
    _countLabel.text = [NSString stringWithFormat:@"(%@本)",count];
}

@end
