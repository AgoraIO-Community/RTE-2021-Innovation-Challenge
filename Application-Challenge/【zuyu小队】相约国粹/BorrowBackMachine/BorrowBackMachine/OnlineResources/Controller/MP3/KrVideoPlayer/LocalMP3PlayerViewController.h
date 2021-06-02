//
//  LocalMP3PlayerViewController.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/8.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KrVideoPlayerController.h"
@interface LocalMP3PlayerViewController : UIViewController
@property (nonatomic, strong) KrVideoPlayerController  *videoController;
@property (strong, nonatomic)  NSArray *nameArray;
@property (strong, nonatomic)  NSArray *URLArray;
@property (assign)  NSInteger indexRow;



@end
