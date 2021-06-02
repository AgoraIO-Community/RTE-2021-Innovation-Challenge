//
//  MainThirdUpTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/4/29.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "MainThirdUpTableViewCell.h"
#import "MainThirdMP4Modle.h"


@implementation MainThirdUpTableViewCell
{
    UIButton *_button;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(NSMutableArray *)dataArray
{
    
    if (_dataArray == nil) {
        _dataArray = [[NSMutableArray alloc] init];
        
    }
    
    return _dataArray;
}



-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
            
        self.image = [[UIImageView alloc] initWithFrame:CGRectMake(sizes.width/20, self.frame.size.height/3, self.frame.size.height/3, self.frame.size.height/3)];
        
        self.image.image = [UIImage imageNamed:@"stop_btn_hover"];
        
        [self addSubview:self.image];
        
        self.listLable = [[UILabel alloc] initWithFrame:CGRectMake(50, self.frame.size.height/4, WIDTH / 2 - 25, self.frame.size.height/2)];
                
        [self addSubview:self.listLable];
        
        
        
        _isDownloadLable = [[UILabel alloc] init];
        
        _isDownloadLable.frame = CGRectMake(WIDTH / 2 + 35, self.frame.size.height/4 , 100, self.frame.size.height/2);
        
        
        [self addSubview:_isDownloadLable];
        
        _downLoadButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downLoadButton.frame = CGRectMake(WIDTH/10 * 8 - 20  , self.frame.size.height/4 ,WIDTH/5 + 20 ,self.frame.size.height/2);
        
        [_downLoadButton setTitle:@"下载" forState:UIControlStateNormal];
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH/5 + 20 - self.frame.size.height/2, 0, self.frame.size.height/2, self.frame.size.height/2)];
        
        imageV.image = [UIImage imageNamed:@"down_item"];
        
        [_downLoadButton addSubview:imageV];
                
        [_downLoadButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        
        [self addSubview:_downLoadButton];
        
    
        
        UILabel *blackLable = [[UILabel alloc] initWithFrame:CGRectMake(0, self.frame.size.height ,WIDTH, 0.3)];
        
        blackLable.backgroundColor =[UIColor lightGrayColor];
        
        [self addSubview:blackLable];
    }

    return self;

}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
