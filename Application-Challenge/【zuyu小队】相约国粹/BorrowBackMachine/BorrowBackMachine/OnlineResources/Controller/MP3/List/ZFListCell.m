//
//  ZFListCell.m
//  ZFDownload
//
//  Created by 任子丰 on 16/5/16.
//  Copyright © 2016年 任子丰. All rights reserved.
//

#import "ZFListCell.h"

@implementation ZFListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _titleLabel =[[UILabel alloc] initWithFrame:CGRectMake(10, 10, 240, 60)];
        
        _titleLabel.numberOfLines = 0;
        
        [self addSubview:_titleLabel];
        
        _downloadBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downloadBtn.frame = CGRectMake(300, 10, 50, 30);
        
        [_downloadBtn setTitle:@"下载" forState:UIControlStateNormal];
        
        [_downloadBtn setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        [_downloadBtn addTarget:self action:@selector(downloadAction:) forControlEvents:UIControlEventTouchUpInside];
        
        
        
        [self addSubview:_downloadBtn];
        
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

@end
