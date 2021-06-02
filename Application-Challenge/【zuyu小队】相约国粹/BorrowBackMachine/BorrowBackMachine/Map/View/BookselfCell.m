//
//  BookselfCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//



#import "BookselfCell.h"
#import "UIImageView+WebCache.h"
#import "zuyu.h"
@interface BookselfCell()
{
    UIImageView *_imageView;
    UILabel     *_nameLabel;
    UILabel     *_autuorLabel;
    UILabel     *_formLabel;
    
}
@end
@implementation BookselfCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        UIView *outView = [[UIView alloc] initWithFrame:CGRectMake(5, 5, WIDTH/2 - 10, 140)];
        [self addSubview:outView];
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(5, 5, WIDTH/4 - 10, 130)];
        [outView addSubview:_imageView];
        
        _nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(CGRectGetMaxX(_imageView.frame) + 5, 5, WIDTH/4, 40)];
        
        _nameLabel.numberOfLines = 0;
        
        [outView addSubview:_nameLabel];
        
        _autuorLabel = [[UILabel alloc] initWithFrame:CGRectMake(CGRectGetMaxX(_imageView.frame) + 5, 40, WIDTH/4, 40)];
        
        _autuorLabel.numberOfLines = 0;

        [outView addSubview:_autuorLabel];
        
        _formLabel = [[UILabel alloc] initWithFrame:CGRectMake(CGRectGetMaxX(_imageView.frame) + 5, 80, WIDTH/4, 50)];
        
        _formLabel.numberOfLines = 0;
        
        [outView addSubview:_formLabel];

        
    }
    return self;
}

-(void)setName:(NSString *)name
{
    _nameLabel.text = name;
}

-(void)setForm:(NSString *)form
{
    _formLabel.text = form;
}

-(void)setWriter:(NSString *)writer
{
    _autuorLabel.text = writer;
}

-(void)setIamgeUrl:(NSString *)iamgeUrl
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:iamgeUrl] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:3]];
}


@end
