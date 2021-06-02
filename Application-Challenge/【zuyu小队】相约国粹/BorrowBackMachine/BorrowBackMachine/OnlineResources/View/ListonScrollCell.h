//
//  ListonScrollCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainCellListModel.h"

@protocol ListonScrollCellDelegate <NSObject>

@optional

-(void)listonScrollCellViewDidSelectItemAtIndexPath:(MainCellListModel *)model;

-(void)listonScrollCellResouceLoadError:(NSInteger )type;

@end


@interface ListonScrollCell : UITableViewCell
@property(nonatomic,strong) NSIndexPath *index;
@property(nonatomic,strong) NSMutableArray *dataArray;
@property(nonatomic,assign) id <ListonScrollCellDelegate> delegate;
@end
