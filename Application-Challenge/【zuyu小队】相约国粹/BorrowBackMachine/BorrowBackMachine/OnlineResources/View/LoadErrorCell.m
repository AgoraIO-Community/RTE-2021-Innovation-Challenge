//
//  LoadErrorCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "LoadErrorCell.h"
#import "LoadErrorCellView.h"
@interface LoadErrorCell()
{
}
@end
@implementation LoadErrorCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        LoadErrorCellView *errorView = [[LoadErrorCellView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 300)];
        UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(errorViewTouchUpInsind:)];
        
        [errorView addGestureRecognizer:tapGesturRecognizer];
        [self.contentView addSubview:errorView];
    }
    
    return self;
}

-(void)errorViewTouchUpInsind:(id)tap
{
    [self.delegate loadErrorCellRefresh];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
