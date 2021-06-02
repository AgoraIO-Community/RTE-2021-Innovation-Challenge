//
//  ThirdMp3ButtonTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/5/8.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)
#import "ThirdMp3ButtonTableViewCell.h"
#import "MBProgressHUD.h"

@implementation ThirdMp3ButtonTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.tagi = 0;
    // Initialization code
}



-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    
    if (self) {
        
        float cellHeight = 70;
        
        self.image = [[UIImageView alloc] initWithFrame:CGRectMake(sizes.width/20, cellHeight/3/2 + 12, cellHeight/3, cellHeight/3)];
        
        
        self.image.image = [UIImage imageNamed:@"stop_btn_hover"];
        
//        [self addSubview:self.image];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(30,  10, WIDTH / 2, 50)];
        
        [self addSubview:_nameLable];
        
        _readLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH/2 + 20, cellHeight/2 - 10, sizes.width / 5, 20)];
        
        _readLable.text = @"111M";
        
        _readLable.textAlignment = 1;
        
        _readLable.textColor = [UIColor lightGrayColor];
        
        _readLable.font = [UIFont systemFontOfSize:15];
        
//        _readLable.backgroundColor = [UIColor lightGrayColor];
        
        [self addSubview:_readLable];
        
        _collectButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _collectButton.frame = CGRectMake(sizes.width/2 + sizes.width/10 , self.frame.size.height/4 , sizes.width/6, self.frame.size.height/2);
        
        
        _downLoadButton = [[UILabel alloc]init];
        
        _downLoadButton.frame = CGRectMake(sizes.width/2 + sizes.width/5  + sizes.width/6 - 60, cellHeight/2 - 15 , 80, 30);
        
        
//        暂时隐藏
        [self addSubview:_downLoadButton];
        
        _downImage = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downImage.frame = CGRectMake(sizes.width/2 + sizes.width/5  + sizes.width/6 , cellHeight/4, cellHeight/2, cellHeight/2);
        
        [_downImage setImage:[UIImage imageNamed:@"down_item"] forState:UIControlStateNormal];
        
        [_downImage addTarget:self action:@selector(downLoad) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:_downImage];
        
        UILabel *blackLable = [[UILabel alloc ] initWithFrame:CGRectMake(0, 0, sizes.width, 0.5f)];
        
        blackLable.backgroundColor = [UIColor colorWithRed:205.0/255.0f green:201.0/255.0f blue:201.0/255.0f alpha:0.8f];
        
        [self addSubview:blackLable];
        
        if (WIDTH == 320) {
            _nameLable.font = [UIFont systemFontOfSize:12];
            _readLable.font = [UIFont systemFontOfSize:12];
            _downLoadButton.font = [UIFont systemFontOfSize:12];
            _downImage.frame = CGRectMake(sizes.width/2 + sizes.width/5  + sizes.width/6 + 20, cellHeight/3/2 + 10, cellHeight/3, cellHeight/3);
        }
        

        
        
        
    }
    return self;
}


-(void)downLoad
{
    [self.delegate downloadMP3];
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
