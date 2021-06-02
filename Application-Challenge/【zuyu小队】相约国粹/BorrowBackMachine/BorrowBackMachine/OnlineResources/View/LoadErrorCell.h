//
//  LoadErrorCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol LoadErrorCellDelegate <NSObject>

@optional
-(void)loadErrorCellRefresh;
@end

@interface LoadErrorCell : UITableViewCell
@property(nonatomic,assign) id<LoadErrorCellDelegate> delegate;

@end
