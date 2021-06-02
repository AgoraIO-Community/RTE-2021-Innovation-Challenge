//
//  NetworkErrorView.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol NetworkErrorViewDeleagete <NSObject>

@optional
-(void)refreshLoadResouce;

@end
@interface NetworkErrorView : UIView

@property(nonatomic,assign) id <NetworkErrorViewDeleagete> delegate;

@end
