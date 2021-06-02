//
//  MainMP4ListCell.m
//  CNCLibraryScan
//
//  Created by zuyu on 2018/1/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MainMP4ListCell.h"

@implementation MainMP4ListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier WithTag:(NSInteger )tag
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _text = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, WIDTH/2, 60)];
        
        _text.numberOfLines = 0;
        
        [self.contentView addSubview:_text];
        
        _downLoadText = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH/2 + 15, 14, 70, 30)];
        
        _downLoadText.textAlignment = NSTextAlignmentCenter;
        
        [self.contentView addSubview:_downLoadText];
        
        
        UIButton *downLoad = [UIButton buttonWithType:UIButtonTypeCustom];
        
        downLoad.frame = CGRectMake(WIDTH - 100, 10, 100, 40);
        
        [downLoad setTitle:@"下载" forState:UIControlStateNormal];
        
        [downLoad setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        
        [downLoad addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
        
        downLoad.tag = tag + 500;
        
        [self.contentView addSubview:downLoad];
        
    }
    
    return self;
    
}


-(void)click:(UIButton *)button
{
    [self.delegate download:button.tag - 500];
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
