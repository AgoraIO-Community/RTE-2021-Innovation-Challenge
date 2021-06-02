//
//  MainThirdTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/4/29.
//  Copyright © 2016年 zuyu. All rights reserved.
//


#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)
#import "MainThirdTableViewCell.h"

@implementation MainThirdTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
        
        _titleLable = [[UILabel alloc ] initWithFrame:CGRectMake(15, 10, sizes.width, 30)];
        
        
        [self addSubview:_titleLable];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(15, 50, sizes.width, 30)];
        
        
        _nameLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_nameLable];
        
        _introduceLable = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, sizes.width-20, 100)];
        
        
        
        //        _introduceLable.lineBreakMode = NSLineBreakByWordWrapping; //这个是关键
        _introduceLable.numberOfLines = 0;  //这个是关键
        //        CGSize size = [_introduceLable.text sizeWithFont:NULL constrainedToSize:CGSizeMake(310.0f,CGFLOAT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
        //
        //        CGRect newFrame = _introduceLable.frame;
        //        newFrame.size.height = size.height;
        //        _introduceLable.frame = newFrame;
        //        [_introduceLable sizeToFit];
        
        _introduceLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_introduceLable];
        
        UILabel *blackLable = [[UILabel alloc] initWithFrame:CGRectMake(0, HEIGHT/8 + 40 ,WIDTH, 0.3)];
        
        blackLable.backgroundColor =[UIColor lightGrayColor];
        
        [self addSubview:blackLable];
        
    }
    
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

@end

