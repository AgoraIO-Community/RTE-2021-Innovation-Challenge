//
//  OnlineResouceCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/1.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainCellListModel.h"

@protocol OnlineResouceCellDelegate <NSObject>

@optional

-(void)moreClickThing:(NSInteger )type;
-(void)pageClickThing:(NSInteger )type;
-(void)resouceLoadErrorTouchUpInsind:(NSInteger )type;
-(void)collectionViewDidSelectItemAtIndexPath:(MainCellListModel *)model withType:(NSInteger )type;

@end

@interface OnlineResouceCell : UITableViewCell
@property(nonatomic,strong) NSArray *dataArray;
@property(nonatomic,strong) NSIndexPath *index;
@property(nonatomic,assign) id <OnlineResouceCellDelegate> delegate;
 
@end
