//
//  LogOutCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "LogOutCell.h"
@interface LogOutCell()
{
    UILabel *_nameLabel;
}
@end
@implementation LogOutCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _headImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, 65, 65)];
        
        
        [self addSubview:_headImage];
        
        _nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(90, 20, WIDTH - 100, 45)];
        
        _nameLabel.font = [UIFont systemFontOfSize:20];
        
        [self addSubview:_nameLabel];
        
        UILabel *lionLable = [[UILabel alloc] initWithFrame:CGRectMake(0, 93, WIDTH, 7)];
        lionLable.backgroundColor = LIONCOLOR;
        
        [self addSubview:lionLable];
        
    }
    
    return self;
}


-(void)setNameText:(NSString *)nameText
{
    _nameLabel.text = nameText;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
