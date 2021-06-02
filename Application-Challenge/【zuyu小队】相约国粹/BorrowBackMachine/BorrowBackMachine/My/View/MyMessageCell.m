//
//  MyMessageCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MyMessageCell.h"
#import "UIImageView+WebCache.h"
@interface MyMessageCell()
{
    UIImageView *_imageView;
    UILabel     *_titleLabel;
}
@end

@implementation MyMessageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier withIndexRow:(NSInteger )row
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        if (row) {
            
            _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, WIDTH/2, 60)];
            
            [self addSubview:_titleLabel];
            
            
        }else{
            _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
            [self addSubview:_imageView];
        }
        
        
        UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.5, 10, WIDTH/2 - 10, 60)];
        
        lable.text = @"点击修改     >";
        
        lable.textColor = [UIColor lightGrayColor];
        
        lable.textAlignment = NSTextAlignmentRight;
        
        [self addSubview:lable];
        
    }
    
    return self;
}


-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}

-(void)setImage:(NSString *)image
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:image] placeholderImage:[UIImage imageNamed:@"logoImage"]];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
