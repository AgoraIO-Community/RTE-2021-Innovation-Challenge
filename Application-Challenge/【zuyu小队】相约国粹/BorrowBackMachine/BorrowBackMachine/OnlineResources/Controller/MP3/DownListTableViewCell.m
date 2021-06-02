
//
//  DownListTableViewCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/8/25.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import "DownListTableViewCell.h"

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

@implementation DownListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    //    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (self) {
        
        _image = [[UIImageView alloc] initWithFrame:CGRectMake(10, 30, 20, 20)];
        
        _image.image = [UIImage imageNamed:@"downlistNo.jpg"];
        
        [self addSubview:_image];
        
        _titleLabel =[[UILabel alloc] initWithFrame:CGRectMake(40, 10, WIDTH/3 - 10 , 60)];
        
        _titleLabel.numberOfLines = 0;
        
        [self addSubview:_titleLabel];
        
        _sizeLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH / 3 + WIDTH/10 *1.5 , 0, 70, 80)];

        [self addSubview:_sizeLable];
        
        _downloadBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downloadBtn.frame = CGRectMake(WIDTH - 60, 10, 50, 30);
        
        _downloadBtn.hidden = YES;
        
        [_downloadBtn setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        [_downloadBtn addTarget:self action:@selector(downloadAction:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:_downloadBtn];
        
        
        _isDownloadLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH - 80, 20, 60, 40)];
        
//        _isDownloadLable.backgroundColor = [UIColor lightGrayColor];
        
        _isDownloadLable.text = @"1";
        
        [self addSubview:_isDownloadLable];
        
    }
    
    return self;
}
- (void)downloadAction:(UIButton *)sender {
    if (self.downloadCallBack) { self.downloadCallBack(); }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}


#pragma mark - decodeURI
-(NSString *)decodeString:(NSString*)encodedString

{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
    NSString *decodedString  = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                     (__bridge CFStringRef)encodedString,
                                                                                                                     CFSTR(""),
                                                                                                                     CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}


@end
