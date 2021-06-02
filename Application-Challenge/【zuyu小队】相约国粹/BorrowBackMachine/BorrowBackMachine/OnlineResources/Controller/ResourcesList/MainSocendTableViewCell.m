//
//  MainSocendTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/4/27.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import "MainSocendTableViewCell.h"

@implementation MainSocendTableViewCell

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
         
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, sizes.width/5, sizes.height/7)];
        
        _image.image = [UIImage imageNamed:@"buttonTest"];
        
        [self.contentView addSubview:_image];
        
        _classTypeSign = [[UIImageView alloc] initWithFrame:CGRectMake(sizes.width/5 - 28, sizes.height/7 - 28, 25, 25)];
        
        [_image addSubview:_classTypeSign];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4, 20, sizes.width * 0.7, 20)];
        
        _nameLable.text = @"01雨果";
        
        [self.contentView addSubview:_nameLable];
        
        _countryLable = [[UILabel alloc] initWithFrame:CGRectMake( sizes.width/4, sizes.height/18, sizes.width/2, 20)];
        
        
        _countryLable.text = @"外国名著 | 吕艳荣/";
        
        _countryLable.font = [UIFont systemFontOfSize:14];
        
        _countryLable.textColor = [UIColor grayColor];

        [self.contentView addSubview:_countryLable];
        
        _collectLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width - 100 , sizes.height/17, 80, 20)];
        
        _collectLable.text = @"17人气";
        
        _collectLable.font = [UIFont boldSystemFontOfSize:16];
        
        _collectLable.textColor = [UIColor orangeColor];
        
        _collectLable.textAlignment = NSTextAlignmentRight;
        
        [self.contentView addSubview:_collectLable];

        _summaryLable = [[UILabel alloc] initWithFrame:CGRectMake( sizes.width/4, sizes.height/7/4 *2 + 10, sizes.width/4 * 3 - 40, sizes.width/7)];
        
        _summaryLable.numberOfLines = 1;
        
        _summaryLable.font = [UIFont systemFontOfSize:15];
        [self.contentView addSubview:_summaryLable];

        
        _countLable = [[UILabel alloc] initWithFrame:CGRectMake(100, 60, WIDTH /2, 30)];
        
        [self.contentView addSubview:_countLable];
        
        _collectButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _collectButton.frame = CGRectMake(sizes.width - 80 , sizes.height/7, 80, 20);
        
        [_collectButton setTitle:@"收藏" forState:UIControlStateNormal];
        
        [_collectButton setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        _collectButton.hidden = YES;
        
        
        [self.contentView addSubview:_collectButton];
        
        _lionLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, HEIGHT/6-0.8, WIDTH, 0.8)
                              ];
        
        _lionLabel.backgroundColor = [UIColor lightGrayColor];
        
        [self.contentView addSubview:_lionLabel];

    }
    
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}

@end
