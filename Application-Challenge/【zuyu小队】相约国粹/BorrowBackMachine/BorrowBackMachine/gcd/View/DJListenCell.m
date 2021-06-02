//
//  DJListenCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DJListenCell.h"
@interface DJListenCell()
{
    UILabel *_titleLabel;
}

@end
@implementation DJListenCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(15, 10, WIDTH - 70, 80)];
        
        _titleLabel.numberOfLines = 0;
        
        [self addSubview:_titleLabel];
        
        UIImageView * image = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH - 55, 30, 40, 40)];
        
        image.image = [UIImage imageNamed:@"DJMP3TypeLogo"];
        
        [self addSubview:image];
        
    }
    return self;
    
}

- (void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
