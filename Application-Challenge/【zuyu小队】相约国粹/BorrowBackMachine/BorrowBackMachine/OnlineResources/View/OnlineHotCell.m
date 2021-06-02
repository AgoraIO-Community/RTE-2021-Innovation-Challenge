//
//  OnlineHotCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineHotCell.h"
#import "UIImageView+WebCache.h"
#import "zuyu.h"
@interface OnlineHotCell()
{
    UIImageView *_imageView;
    UILabel     *_bookName;
    UILabel     *_writerName;
}

@end
@implementation OnlineHotCell


- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        float imageWidth = WIDTH/4 - 20;
        float jvLeft = (frame.size.width - imageWidth) / 2;
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(jvLeft, 10,imageWidth , WIDTH/4 * 1.3 - 20)];
        
        [self addSubview:_imageView];
        
        _typeImage = [[UIImageView alloc] initWithFrame:CGRectMake(imageWidth - 30, WIDTH/4 * 1.3 - 20 - 30, 30, 30)];
        
        [_imageView addSubview:_typeImage];
        
        _bookName = [[UILabel alloc] initWithFrame:CGRectMake(2, CGRectGetMaxY(_imageView.frame) + 5, frame.size.width - 4, 22)];
        _bookName.textAlignment = NSTextAlignmentCenter;
        
        [self addSubview:_bookName];
        
        _writerName = [[UILabel alloc] initWithFrame:CGRectMake(2, CGRectGetMaxY(_bookName.frame), frame.size.width - 4, 22)];
        
        _writerName.textAlignment = NSTextAlignmentCenter;

        [self addSubview:_writerName];
        
    }
    
    return self;
}

-(void)setBookImage:(NSString *)bookImage
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:bookImage] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:1]];
}

-(void)setBookNameStr:(NSString *)bookNameStr
{
    _bookName.text = bookNameStr;
}

-(void)setWriterNameStr:(NSString *)writerNameStr
{
    _writerName.text = writerNameStr;
}

@end
