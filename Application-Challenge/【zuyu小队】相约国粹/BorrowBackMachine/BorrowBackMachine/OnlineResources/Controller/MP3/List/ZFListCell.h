//
//  ZFListCell.h
//  ZFDownload
//
//  Created by 任子丰 on 16/5/16.
//  Copyright © 2016年 任子丰. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ZFListCell : UITableViewCell
@property (strong, nonatomic)  UILabel *titleLabel;
@property (strong, nonatomic)  UIButton *downloadBtn;
@property (nonatomic, copy) void(^downloadCallBack)();
@end
