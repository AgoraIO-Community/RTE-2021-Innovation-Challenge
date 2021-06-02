//
//  LocalMP3forthViewController.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/9.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KrVideoPlayerController.h"

@interface LocalMP3forthViewController : UIViewController
@property (nonatomic, strong) KrVideoPlayerController  *videoController;
@property (strong, nonatomic)  NSArray *dataArray;
@property (strong, nonatomic)  NSArray *nameArray;
@property (assign,nonatomic)   NSInteger chooseID;

@end
