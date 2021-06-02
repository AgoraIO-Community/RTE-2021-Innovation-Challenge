//
//  MainThirdMP3ViewController.h
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/5/5.
//  Copyright © 2016年 zuyu. All rights reserved.
//
#import "KrVideoPlayerController.h"
#import "BaceViewController.h"
#import <UIKit/UIKit.h>

@interface MainThirdMP3ViewController : BaceViewController
@property (strong, nonatomic) NSMutableArray *dataArray;
@property (strong, nonatomic)  NSString *tableListID;
@property (nonatomic, strong) NSMutableArray *parametersArray;
@property (nonatomic, strong) KrVideoPlayerController  *videoController;
@property (strong, nonatomic)  NSString *thirdTitle;
@property (strong, nonatomic) NSMutableArray *bookHomeArray;

@property (strong, nonatomic) NSMutableArray *downLoadListArray;

@property (strong, nonatomic)  NSString *bookHomeTypeString;
@property (strong, nonatomic)  NSString *MenuID;

@property (assign)  NSInteger butTag;

@property (strong, nonatomic)  NSString *testString;

@property (strong, nonatomic)  NSString *writer;

@property (strong, nonatomic)  NSString *viewCount;

@property (strong, nonatomic)  NSString *imageName;

@property (strong, nonatomic)  NSString *BookTypeName;

@property (strong, nonatomic)  NSString *VolumeCount;

@property (strong, nonatomic)  NSString *Summary;

@property (strong, nonatomic)  NSString *BookID;

@property (assign, nonatomic)   BOOL isHot;



@end
