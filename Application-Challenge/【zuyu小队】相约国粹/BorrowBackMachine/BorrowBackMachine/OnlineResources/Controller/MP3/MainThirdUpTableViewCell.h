//
//  MainThirdUpTableViewCell.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/4/29.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainThirdMP3ViewController.h"
@interface MainThirdUpTableViewCell : UITableViewCell
@property (strong, nonatomic) NSMutableArray *dataArray;

//@property (nonatomic, strong) MainThirdViewController* mainCon;

@property (nonatomic, strong) UIImageView *downImage;

@property (nonatomic, strong) UIButton *downLoadButton;

@property (nonatomic, strong) UILabel *isDownloadLable;

@property (nonatomic, strong) UIImageView *image;

@property (nonatomic, strong) UILabel *listLable;

@end
