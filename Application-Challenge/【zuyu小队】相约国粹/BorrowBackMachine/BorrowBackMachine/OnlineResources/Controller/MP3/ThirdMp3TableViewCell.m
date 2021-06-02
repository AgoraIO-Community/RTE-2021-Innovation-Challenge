//
//  ThirdMp3TableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/5/6.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import "ThirdMp3TableViewCell.h"

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

@implementation ThirdMp3TableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
        
        UIImageView *bgImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT/2)];
        
        bgImage.image = [UIImage imageNamed:@"hot_recommended_list_bg"];
        
        [self addSubview:bgImage];
        
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, sizes.width/4 + 10 , sizes.height/5)];
        
        _image.image = [UIImage imageNamed:@"buttonTest.png"];
        
        [self addSubview:_image];
        
        
        _titleLable = [[UILabel alloc ] initWithFrame:CGRectMake(sizes.width/4 + 35, 15, sizes.width * 0.7 - 35, 45)];
        
        _titleLable.numberOfLines = 0;;
        
        [self addSubview:_titleLable];
        
        _tepyLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4 + 35, sizes.height/10, sizes.width/3, 20)];
        
        _tepyLable.text = @"类别:";
        
        _tepyLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_tepyLable];
        
        _countLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4 * 3, sizes.height/10, sizes.width/3, 20)];
        
        
        _countLable.text = @"章节 : ";
        
        _countLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_countLable];
        
        
        _writerLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4 + 35, sizes.height/10 + 30, sizes.width/3, 20)];
        
        _writerLable.text = @"作者:";
        
        _writerLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_writerLable];
        
        _popularLable = [[UILabel alloc] initWithFrame:CGRectMake(sizes.width/4 * 3, sizes.height/10 + 30, sizes.width/3, 20)];
        
        
        _popularLable.text = @"人气 : ";
        
        _popularLable.textColor = [UIColor lightGrayColor];
        
        [self addSubview:_popularLable];
        
        _introduceLable = [[UILabel alloc] initWithFrame:CGRectMake(15, sizes.height/5 + 30, sizes.width - 30, 180)];
        
        _introduceLable.numberOfLines = 0;
        
        _introduceLable.textColor = [UIColor lightGrayColor];
        
        //        _introduceLable.text = @"赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 钱 孙 李 周 吴 郑 王 赵 钱 李 周! 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 钱 孙 李 周 吴 郑 王 赵 钱 李 周 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 赵 钱 孙 李 周 吴 郑 王 钱 孙 李 周 吴 郑 王 赵 钱 李 周! 王 赵 钱 孙 李 周 ";
        //
        //        CGSize maximumSize =CGSizeMake(300,9999);
        //        NSString*dateString =_introduceLable.text;
        
        
        //        UIFont*dateFont =[UIFont fontWithName:@"Helvetica" size:14];
        //        CGSize dateStringSize =[dateString sizeWithFont:dateFont
        //                                      constrainedToSize:maximumSize
        //                                          lineBreakMode:_introduceLable.lineBreakMode];
        //        CGRect dateFrame =CGRectMake(15, sizes.height/5 + 30, sizes.width - 30, dateStringSize.height + dateString.length / 3.5);
        //        _introduceLable.frame = dateFrame;
        //
        //        _introduceLable.backgroundColor = [UIColor yellowColor];
        //
        //        _introduceLable.textAlignment = NSTextAlignmentNatural;
        
        [self addSubview:_introduceLable];
        
        _collectButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _collectButton.frame = CGRectMake(15, sizes.height/5 + 130, (sizes.width - 30)/2, 100);
        
        //        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, (sizes.width - 30)/2, 100)];
        //
        //        imageView.image = [UIImage imageNamed:@"img_scound_no_colleant"];
        //
        //        [_collectButton addSubview:imageView];
        
        [_collectButton setImage:[UIImage imageNamed:@"img_scound_no_colleant"] forState:UIControlStateNormal];
        
        //        [_collectButton setTitle:@"收藏" forState:UIControlStateNormal];
        
        [_collectButton setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        [self addSubview:_collectButton];
        
        _downLoadButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downLoadButton.frame = CGRectMake(15 + (sizes.width - 30)/2, sizes.height/5 + 130, (sizes.width - 30)/2, 100);
        
        [_downLoadButton setImage:[UIImage imageNamed:@"img_down_books"] forState:UIControlStateNormal];
        
        [_downLoadButton setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        [self addSubview:_downLoadButton];
        
        if (WIDTH == 320 ) {
            
            _collectButton.frame = CGRectMake(15, sizes.height/5 + 130 - 40, (sizes.width - 30)/2, 100);
            
            _downLoadButton.frame = CGRectMake(15 + (sizes.width - 30)/2, sizes.height/5 + 130 - 40, (sizes.width - 30)/2, 100);
            
        }
        
    }
    return self;
}
 

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}

@end

