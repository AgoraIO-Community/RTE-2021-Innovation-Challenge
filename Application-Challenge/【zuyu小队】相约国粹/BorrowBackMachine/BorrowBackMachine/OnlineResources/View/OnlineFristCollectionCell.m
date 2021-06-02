//
//  OnlineFristCollectionCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/7.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineFristCollectionCell.h"
#import "zuyu.h"
@interface OnlineFristCollectionCell()
{
    UIImageView *_imageView;
    UILabel     *_bookName;
    UILabel     *_writerName;
    UILabel     *_sunmerLabel;

}

@end
@implementation OnlineFristCollectionCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) { 
        
        // width * 0.9 . 160.
        float imageWidth = WIDTH/3.5 - 20;
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10,imageWidth , 140)];
        
        [self addSubview:_imageView];
        
        _typeImage = [[UIImageView alloc] initWithFrame:CGRectMake(imageWidth - 40, 100, 40, 40)];
        
        [_imageView addSubview:_typeImage];
        
        _bookName = [[UILabel alloc] initWithFrame:CGRectMake(imageWidth + 20, 10,frame.size.width - imageWidth - 30 , 40)];
        
        [self addSubview:_bookName];
        
        _sunmerLabel = [[UILabel alloc] initWithFrame:CGRectMake(imageWidth + 20, 50, frame.size.width - imageWidth - 30, frame.size.height - 60)];
        
        _sunmerLabel.numberOfLines = 0;
        
        [self addSubview:_sunmerLabel];
        
        
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

- (void)setSummerStr:(NSString *)summerStr
{
    _sunmerLabel.text = summerStr;
}
@end
