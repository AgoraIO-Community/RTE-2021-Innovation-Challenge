//
//  DJNewsCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/21.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DJNewsCell.h"

@interface DJNewsCell()
{
    UIImageView *_imageView;
    UILabel     *_titleLabel;
    UILabel     *_timeLabel;
}
@end

@implementation DJNewsCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 20, 40,60)];
        
        _imageView.image = [UIImage imageNamed:@"DJNewsImage"];
        
//        _imageView.backgroundColor = [UIColor redColor];
        
        [self addSubview:_imageView];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(60, 10, WIDTH - 80, 40)];
        _titleLabel.numberOfLines = 0;
        
        [self addSubview:_titleLabel];
        
        _timeLabel = [[UILabel alloc] initWithFrame:CGRectMake(60, 50, WIDTH - 80, 40)];
        _timeLabel.numberOfLines = 0;
        
        [self addSubview:_timeLabel];
        
        
    }
    
    return self;
}


-(void)setTime:(NSString *)time
{
    _timeLabel.text = time;
}

-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
