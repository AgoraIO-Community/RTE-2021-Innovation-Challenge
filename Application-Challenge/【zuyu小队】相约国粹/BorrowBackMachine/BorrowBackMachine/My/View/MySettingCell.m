//
//  MySettingCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/28.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MySettingCell.h"
@interface MySettingCell()
{
    UILabel *_titleLabel;
}

@end
@implementation MySettingCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, WIDTH/2, 50)];
        
        _titleLabel.font = [UIFont systemFontOfSize:18];

        
        [self.contentView addSubview:_titleLabel];
        
        UIImageView *rightImage = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH - 40, 24, 22, 22)];
        
        rightImage.image = [UIImage imageNamed:@"myInformationRight"];
        
        [self.contentView addSubview:rightImage];
        
        UILabel *lionLable = [[UILabel alloc] initWithFrame:CGRectMake(0, 69, WIDTH, 1)];
        
        lionLable.backgroundColor = LIONCOLOR;
        
        [self.contentView addSubview:lionLable];
    }
    
    return self;
}

-(void)setText:(NSString *)text
{
    _titleLabel.text = text;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
