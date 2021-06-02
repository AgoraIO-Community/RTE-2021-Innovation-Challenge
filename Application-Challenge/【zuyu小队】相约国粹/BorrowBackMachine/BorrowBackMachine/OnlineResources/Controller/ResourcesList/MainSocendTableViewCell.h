//
//  MainSocendTableViewCell.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/4/27.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainSocendTableViewCell : UITableViewCell
@property (nonatomic, strong) UIImageView *image;
@property (nonatomic, strong) UIImageView *classTypeSign;

@property (nonatomic, strong)  UILabel *nameLable;
@property (nonatomic, strong) UILabel *countryLable;
@property (nonatomic, strong) UILabel *collectLable;
@property (nonatomic, strong) UILabel *summaryLable;
@property (nonatomic, strong) UIButton *collectButton;
@property (nonatomic, strong)  UILabel *countLable;
@property (nonatomic, strong)  UILabel *lionLabel;

@end
