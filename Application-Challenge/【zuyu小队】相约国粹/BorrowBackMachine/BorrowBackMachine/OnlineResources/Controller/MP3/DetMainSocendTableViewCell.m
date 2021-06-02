//
//  DetMainSocendTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/22.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import "DetMainSocendTableViewCell.h"

@implementation DetMainSocendTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
        
        
        _isXuanImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 35, 30, 30)];
        
        //        _image.backgroundColor =[UIColor redColor];
        
        [self addSubview:_isXuanImage];
        
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, sizes.width/5, sizes.height/7)];
        
        _image.image = [UIImage imageNamed:@"buttonTest"];
        
        [self addSubview:_image];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4, 15, sizes.width/2, 20)];
        
        _nameLable.text = @"01雨果";
        
        [self addSubview:_nameLable];
        
        _countryLable = [[UILabel alloc] initWithFrame:CGRectMake( sizes.width/4, sizes.height/18, sizes.width/2, 20)];
        
        
        _countryLable.text = @"外国名著 | 吕艳荣/";
        
        _countryLable.font = [UIFont systemFontOfSize:14];
        
        _countryLable.textColor = [UIColor grayColor];
        
        [self addSubview:_countryLable];
        
        _collectLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width - 100 , sizes.height/18, 80, 20)];
        
        _collectLable.text = @"17人气";
        
        _collectLable.font = [UIFont boldSystemFontOfSize:16];
        
        _collectLable.textColor = [UIColor orangeColor];
        
        _collectLable.textAlignment = NSTextAlignmentRight;
        
        [self addSubview:_collectLable];
        
        _summaryLable = [[UILabel alloc] initWithFrame:CGRectMake( sizes.width/4, sizes.height/7/4 *2, sizes.width/4 * 3 - 40, sizes.width/7)];
        
        _summaryLable.numberOfLines = 1;
        
        _summaryLable.font = [UIFont systemFontOfSize:15];
        
        
        [self addSubview:_summaryLable];
        
    
        
    }
    
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
