//
//  TypeSrerchCell.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/20.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "TypeSrerchCell.h"

@implementation TypeSrerchCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
    
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(20, 15, 40, 40)];
        
        [self.contentView addSubview:_image];
        
        _text = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH - 70, 15, 60, 40)];
        
        _text.textColor = [UIColor lightGrayColor];
        
        [self.contentView addSubview:_text];
        
        _title = [[UILabel alloc] initWithFrame:CGRectMake(80, 15, 160, 40)];
        
        [self.contentView addSubview:_title];
        
    }
    
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
