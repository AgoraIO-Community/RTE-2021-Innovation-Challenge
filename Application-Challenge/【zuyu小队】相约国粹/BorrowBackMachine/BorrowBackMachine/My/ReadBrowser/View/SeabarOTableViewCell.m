//
//  SeabarOTableViewCell.m
//  redAndYellow
//
//  Created by zuyu on 16/6/14.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "SeabarOTableViewCell.h"

@implementation SeabarOTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
    
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, WIDTH-20, 30)];
        
        [self addSubview:_nameLable];
        
        _typeLable = [[UILabel alloc] initWithFrame:CGRectMake(10, 45, WIDTH-20, 30)];
        
        [self addSubview:_typeLable];
        
    }
    
    return self;
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
