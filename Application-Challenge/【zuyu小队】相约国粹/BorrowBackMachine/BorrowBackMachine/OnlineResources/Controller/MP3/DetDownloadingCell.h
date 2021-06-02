//
//  DetDownloadingCell.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/22.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZFDownloadManager.h"

typedef void(^ZFBtnClickBlock)(void);

@interface DetDownloadingCell : UITableViewCell

@property (nonatomic, strong) UIImageView *isXuanImage;

@property (strong, nonatomic)  UILabel *fileNameLabel;
@property (strong, nonatomic)  UIProgressView *progress;

@property (strong, nonatomic)  UILabel *progressLabel;
@property (strong, nonatomic)  UILabel *speedLabel;
@property (strong, nonatomic)  UIButton *downloadBtn;


/** 下载按钮点击回调block */
@property (nonatomic, copy  ) ZFBtnClickBlock  btnClickBlock;
/** 下载信息模型 */
@property (nonatomic, strong) ZFFileModel      *fileInfo;
/** 该文件发起的请求 */
@property (nonatomic,retain ) ZFHttpRequest    *request;


//全部继续下载
- (void)clickDownload2:(UIButton *)sender;

//全部暂停
- (void)clickDownload3:(UIButton *)sender;
@end
