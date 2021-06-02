//
//  ThirdMp3ButtonTableViewCell.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/5/8.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol mp3CellDownloadDelegate <NSObject>

@optional
-(void)downloadMP3;
@end


@interface ThirdMp3ButtonTableViewCell : UITableViewCell
@property (nonatomic, strong) UILabel *nameLable;
@property (nonatomic, strong) UILabel *readLable;
@property (nonatomic, strong) UILabel *sizeLable;
@property (nonatomic, strong) UILabel *downLoadButton;
@property (nonatomic, strong) UIButton *collectButton;
@property (nonatomic, strong) UIImageView *image;
@property (nonatomic, strong) UIButton *downImage;

@property (assign)  NSInteger tagi;

@property(nonatomic,assign) id <mp3CellDownloadDelegate> delegate;


@end
