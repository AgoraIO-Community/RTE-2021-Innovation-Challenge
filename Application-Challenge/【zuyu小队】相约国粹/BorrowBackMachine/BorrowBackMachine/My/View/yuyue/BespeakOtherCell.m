//
//  BespeakOtherCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/30.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BespeakOtherCell.h"
#import "UIImageView+WebCache.h"
#import "zuyu.h"
@interface BespeakOtherCell()
{
    UIImageView *_imageView;
    UILabel     *_titleLabel;
    UILabel     *_timeLabel;
    UILabel     *_deviceLabel;
    UILabel     *_formLabel;

}
@end

@implementation BespeakOtherCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
   
    if (self) {
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 110, 160)];
        
        [self.contentView addSubview:_imageView];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 10, WIDTH - 140 , 40)];
        
        [self.contentView addSubview:_titleLabel];
        
        _timeLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 50, WIDTH - 140 , 40)];
        
        [self.contentView addSubview:_timeLabel];
        
        _deviceLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 90, WIDTH - 140 , 40)];
        
        [self.contentView addSubview:_deviceLabel];
        
        _formLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 130, WIDTH - 140 , 40)];
        
        [self.contentView addSubview:_formLabel];
        
        
        _imageView.backgroundColor = [UIColor redColor];
        _titleLabel.text = @"三国演义";
        _timeLabel.text = @"2018年09月30日11:20:24";
        _timeLabel.font = [UIFont systemFontOfSize:16];
        _timeLabel.numberOfLines = 0;
        _deviceLabel.text = @"设备名称 - 贼牛逼的设备";
        _deviceLabel.font = [UIFont systemFontOfSize:16];
        _formLabel.text = @"所属设备 - 24小时借还机";
        _formLabel.font = [UIFont systemFontOfSize:16];

    }
  
    return self;
}

//
//@property(nonatomic,strong) NSString *imageUrl;
//@property(nonatomic,strong) NSString *title;
//@property(nonatomic,strong) NSString *time;
//@property(nonatomic,strong) NSString *deviceName;
//@property(nonatomic,strong) NSString *deviceForm;

- (void)setImageUrl:(NSString *)imageUrl
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:imageUrl] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:3]];;
}


-(void)setTitle:(NSString *)title
{
    _titleLabel.text = title;
}

- (void)setTime:(NSString *)time
{
    _timeLabel.text = time;
}

-(void)setDeviceName:(NSString *)deviceName
{
    _deviceLabel.text =[NSString stringWithFormat:@"设备名称 - %@",deviceName];

}
- (void)setDeviceForm:(NSString *)deviceForm
{
    _formLabel.text =  [NSString stringWithFormat:@"设备地址 - %@",deviceForm];

}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
