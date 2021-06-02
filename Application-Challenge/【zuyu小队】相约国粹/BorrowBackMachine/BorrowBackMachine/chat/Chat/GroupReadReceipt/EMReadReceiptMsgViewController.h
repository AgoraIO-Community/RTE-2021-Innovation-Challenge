//
//  readReceiptMsgViewController.h
//  EaseIM
//
//  Created by 娜塔莎 on 2019/10/23.
//  Copyright © 2019 娜塔莎. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol EMReadReceiptMsgDelegate;
@interface EMReadReceiptMsgViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, weak) id<EMReadReceiptMsgDelegate> delegate;

@property (strong, nonatomic) UITableView *memberTableView;

@property (strong, nonatomic) UILabel *countLable;

- (instancetype)initWithMessage:(EMMessage *)message groupId:(NSString *)groupId;
@end

@protocol EMReadReceiptMsgDelegate <NSObject>

- (void)sendReadReceiptMsg:(NSString *)msg;

@end

NS_ASSUME_NONNULL_END
