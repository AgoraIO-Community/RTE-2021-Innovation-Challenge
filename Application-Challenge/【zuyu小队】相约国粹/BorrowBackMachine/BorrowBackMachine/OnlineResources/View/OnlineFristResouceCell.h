//
//  OnlineFristResouceCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/7.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainCellListModel.h"
@protocol OnlineFristResouceCellDelegate <NSObject>

@optional
-(void)hotRecommendCollectionDidSelect:(MainCellListModel *)model;

-(void)hotRecommendMoreClick;

-(void)fristCellLoadResouceError:(NSInteger )type;
@end
@interface OnlineFristResouceCell : UITableViewCell
@property(nonatomic,strong) NSArray *dataArray;
@property(nonatomic,strong) UIPageControl  *pageControl;
@property(nonatomic,assign) id <OnlineFristResouceCellDelegate> delegate;

@end
