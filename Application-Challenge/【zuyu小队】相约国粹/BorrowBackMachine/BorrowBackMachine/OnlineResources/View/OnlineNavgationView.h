//
//  OnlineNavgationView.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol NavgationViewDelegate <NSObject>

@optional
-(void)navSearch;
-(void)navScan;
-(void)navCreateCode;
-(void)navPop;



@end
@interface OnlineNavgationView : UIView
@property (nonatomic,assign) id <NavgationViewDelegate> delegate;
@property (nonatomic,assign) BOOL scanHidden;
@property (nonatomic,assign) BOOL createCodeHidden;
@property (nonatomic,assign) BOOL searchBtnHidden;
@property (nonatomic,assign) BOOL backBtnHidden;
@property (nonatomic,assign) BOOL titleLableHidden;

@property (nonatomic,strong) NSString *titleStr;
@property (nonatomic,strong) NSString *rightItmeImage;



@end
