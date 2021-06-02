//
//  DownListTableViewCell.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/8/25.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DownListTableViewCell : UITableViewCell
@property (strong, nonatomic)  UILabel *titleLabel;
@property (strong, nonatomic)  UILabel *isDownloadLable;
@property (strong, nonatomic)  UILabel *sizeLable;

@property (strong, nonatomic)  UIButton *downloadBtn;
@property (strong, nonatomic)  UIImageView *image;

@property (nonatomic, copy) void(^downloadCallBack)();

@end
