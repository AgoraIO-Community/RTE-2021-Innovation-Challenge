//
//  MoreHotRecommendCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainCellListModel.h"

@protocol MoreHotRecommendCellDelegate <NSObject>

@optional

-(void)collectionViewDidSelectItemAtIndexPath:(MainCellListModel *)model withType:(NSInteger )type;

@end

@interface MoreHotRecommendCell : UITableViewCell
@property(nonatomic,strong) NSArray *dataArray;
@property(nonatomic,strong) NSIndexPath *index;
@property(nonatomic,assign) id <MoreHotRecommendCellDelegate> delegate;
@property(nonatomic,strong) NSString *title;

@end
