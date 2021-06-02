//
//  MyInformationCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/21.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MyInformationCell.h"

@interface MyInformationCell()
{
    UIImageView *_titleImage;
    UILabel     *_titleLable;
}
@end

@implementation MyInformationCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _titleImage = [[UIImageView alloc] initWithFrame:CGRectMake(30, 22.5, 25, 25)];
        
        [self.contentView addSubview:_titleImage];
        
        _titleLable = [[UILabel alloc] initWithFrame:CGRectMake(80, 14, WIDTH - 100, 40)];
        
        _titleLable.font = [UIFont systemFontOfSize:18];
        
        [self.contentView addSubview:_titleLable];
        
        UIImageView *rightImage = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH - 40, 24, 22, 22)];
        
        rightImage.image = [UIImage imageNamed:@"myInformationRight"];
        
        [self.contentView addSubview:rightImage];
        
        UILabel *lionLable = [[UILabel alloc] initWithFrame:CGRectMake(0, 69, WIDTH, 1)];
        
        lionLable.backgroundColor = LIONCOLOR;
        
        [self.contentView addSubview:lionLable];
        
    }
    
    return self;
}



-(void)setImage:(NSString *)image
{
    _titleImage.image = [UIImage imageNamed:image];
}

-(void)setTitle:(NSString *)title
{
    _titleLable.text = title;
}



- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
