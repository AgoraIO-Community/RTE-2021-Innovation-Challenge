//
//  OnlineCollctionHeaderButtonsView.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ResouceClassModel.h"

@protocol CollctionHeaderButtonsViewDelegate <NSObject>

@optional
-(void)HeaderButtonsClick:(ResouceClassModel *)model;
@end

@interface OnlineCollctionHeaderButtonsView : UIView

@property(nonatomic,assign) id <CollctionHeaderButtonsViewDelegate> delegate;
@property(nonatomic,strong) NSArray *dataArray;
@end
