//
//  BookSelfNavView.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/1.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol BookSelfNavViewDelegate <NSObject>

@optional
-(void)BookSelfNavViewPop;
-(void)BookSelfNavSearch:(NSString *)text;

@end


@interface BookSelfNavView : UIView
@property(nonatomic,assign) id <BookSelfNavViewDelegate> delegate;
@end
