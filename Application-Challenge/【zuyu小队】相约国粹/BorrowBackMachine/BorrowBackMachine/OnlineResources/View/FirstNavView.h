//
//  FirstNavView.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/2.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol FirstNavViewDelegate <NSObject>

@optional
-(void)firstForSearch;
-(void)firstForScan;
-(void)firstForCode;



@end
@interface FirstNavView : UIView
@property(nonatomic,assign) id <FirstNavViewDelegate> delegate;
@end
