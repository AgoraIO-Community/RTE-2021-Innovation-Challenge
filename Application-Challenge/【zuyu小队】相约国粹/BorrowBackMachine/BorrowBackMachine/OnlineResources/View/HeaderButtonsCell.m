//
//  HeaderButtonsCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "HeaderButtonsCell.h"
#import "UIImageView+WebCache.h"

@interface HeaderButtonsCell()

{
    UIImageView *_imageView;
    UILabel     *_label;
}
@end
@implementation HeaderButtonsCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
  
    if (self) {
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(15, 5, WIDTH/5 - 30, 50)];
        
        [self addSubview:_imageView];
        
        _label = [[UILabel alloc] initWithFrame:CGRectMake(0,  50,frame.size.width, 30)];
        _label.textAlignment = NSTextAlignmentCenter;
        
        [self addSubview:_label];
    }
    
    return self;
}


-(void)setImageName:(NSString *)imageName
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:imageName] placeholderImage:[UIImage imageNamed:@""]];
}


-(void)setTitle:(NSString *)title
{
    _label.text = title;
}
@end
