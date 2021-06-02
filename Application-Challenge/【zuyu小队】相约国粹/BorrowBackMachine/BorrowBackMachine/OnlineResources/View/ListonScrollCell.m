//
//  ListonScrollCell.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ListonScrollCell.h"
#import "SDCycleScrollView.h"
#import "LoadErrorCellView.h"
@interface ListonScrollCell()<SDCycleScrollViewDelegate>
{
    SDCycleScrollView * cycleScrollView;
    LoadErrorCellView *_errorView;
}

@end
@implementation ListonScrollCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}


-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15, 6, 10, 28)];
        
        imageV.image = [UIImage imageNamed:@"shu"];
        
        [self.contentView addSubview:imageV];
        
        UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(33, 5, WIDTH - 200, 30)];
        
        titleLabel.text = @"精彩收听";
        
        [self.contentView addSubview:titleLabel];
        
        UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, 180)];
        
       [self.contentView addSubview:demoContainerView];
        
        cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 180) delegate:self placeholderImage:[UIImage imageNamed:@""]];
       
        cycleScrollView.pageControlAliment = SDCycleScrollViewPageContolAlimentRight;
        cycleScrollView.currentPageDotColor = [UIColor whiteColor];
        
        [demoContainerView addSubview:cycleScrollView];
         
        
        UILabel *lion = [[UILabel alloc] initWithFrame:CGRectMake(0, 220, WIDTH, 10)];
        
        lion.backgroundColor = LIONCOLOR;
        
        [self.contentView addSubview:lion];
        
        _errorView = [[LoadErrorCellView alloc] initWithFrame:CGRectMake(0, 40, WIDTH, CGRectGetMaxY(lion.frame) - 40)];
        _errorView.hidden = YES;
        
        
        UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(errorViewTouchUpInsind:)];
        
        [_errorView addGestureRecognizer:tapGesturRecognizer];
        [self.contentView addSubview:_errorView];
    }
    
    return self;
}


-(void)errorViewTouchUpInsind:(id)tap
{
    [self.delegate listonScrollCellResouceLoadError:_errorView.tag];
}


-(void)setDataArray:(NSMutableArray *)dataArray
{
    
    if (dataArray.count) {
        NSMutableArray *imageArray = [NSMutableArray array];
        NSMutableArray *titleArray = [NSMutableArray array];
        
        for (MainCellListModel *model in dataArray) {
            [imageArray addObject:model.imageName];
            [titleArray addObject:model.name];
        }
        
        
        cycleScrollView.imageURLStringsGroup = imageArray;
        cycleScrollView.titlesGroup          = titleArray;
        
        _dataArray = dataArray;
        _errorView.hidden = YES;
    }else{
        _errorView.hidden = NO;
    }
    
}


- (void)cycleScrollView:(SDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index
{
    [self.delegate listonScrollCellViewDidSelectItemAtIndexPath:_dataArray[index]];
}



-(void)setIndex:(NSIndexPath *)index
{
    _errorView.tag = index.row;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
