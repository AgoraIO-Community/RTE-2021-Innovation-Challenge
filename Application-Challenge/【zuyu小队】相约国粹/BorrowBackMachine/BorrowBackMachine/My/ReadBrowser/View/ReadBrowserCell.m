//
//  ReadBrowserCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/7.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ReadBrowserCell.h"

@interface ReadBrowserCell()
{
    UIImageView *_image;
    UILabel     *_title;
}
@end

@implementation ReadBrowserCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(5, 5, 70, 70)];
        
        [self.contentView addSubview:_image];
        
        _title = [[UILabel alloc] initWithFrame:CGRectMake(100, 10, WIDTH - 100, 60)];
        
        [self.contentView addSubview:_title];
        
    }
    
    return self;
}


-(void)setImageName:(NSString *)imageName
{
    _image.image = [UIImage imageNamed:imageName];
}

-(void)setTitleStr:(NSString *)titleStr
{
    _title.text = titleStr;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
