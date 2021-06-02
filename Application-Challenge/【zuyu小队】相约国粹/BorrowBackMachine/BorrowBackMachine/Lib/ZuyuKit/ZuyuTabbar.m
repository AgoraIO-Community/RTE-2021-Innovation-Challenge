//
//  ZuyuTabbar.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/4/13.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuTabbar.h"
#import "OnlineResouceVC.h"
#import "EntityResourcesVC.h"
#import "ReadBrowserVC.h"
#import "MyInformationVC.h"
#import "MapForMachineVC.h"
#import "PartyBuildingVC.h"
#import "ChatListVC.h"
#import "EMHomeViewController.h"
#import "ChatVC.h"
@implementation ZuyuTabbar


+(NSArray *)zuyuBarinit
{
    
    
    UINavigationController *firstNav = [[UINavigationController alloc] initWithRootViewController:[[OnlineResouceVC alloc] init]];
    
    
    
//    EaseConversationViewModel *viewModel = [[EaseConversationViewModel alloc] init];

//    ChatListVC *easeConvsVC = [[ChatListVC alloc] initWithModel:viewModel];
//
//    UINavigationController *secondNav = [[UINavigationController alloc] initWithRootViewController:easeConvsVC];
    
//    EMConversationsViewController

    UINavigationController *secondNav = [[UINavigationController alloc] initWithRootViewController:[[ChatVC alloc] init]];

    UINavigationController *firs3tNav = [[UINavigationController alloc] initWithRootViewController:[[EMHomeViewController alloc] init]];

    
    
    
//    UINavigationController *secondNav = [[UINavigationController alloc] initWithRootViewController:[[EntityResourcesVC alloc] init]];
    
//    UINavigationController *mapNav = [[UINavigationController alloc] initWithRootViewController:[[MapForMachineVC alloc] init]];
//
//        UINavigationController *thirdNav = [[UINavigationController alloc] initWithRootViewController:[[PartyBuildingVC alloc] init]];
    
    UINavigationController *forthNav = [[UINavigationController alloc] initWithRootViewController:[[MyInformationVC alloc] init]];
    
    
    
    // -------------------------  OnlineResouceVC  ------------------------------
    
    [firstNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
    
    firstNav.title = @"电子图书";
    
    firstNav.tabBarItem.image = [UIImage imageNamed:@"bar2"];
    
    firstNav.navigationBar.barTintColor = COLOR;
    
    firstNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected2"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    //------
    [firs3tNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
    
    firs3tNav.title = @"通讯录";
    
    firs3tNav.tabBarItem.image = [UIImage imageNamed:@"bar1"];
    
    firs3tNav.navigationBar.barTintColor = COLOR;
    
    firs3tNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected1"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    
    // ------------------------- EntityResourcesVC ----------------------------
    
    [secondNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
    
    secondNav.title = @"书友聚集";
    
    secondNav.tabBarItem.image = [UIImage imageNamed:@"bar1"];
    
    secondNav.navigationBar.barTintColor = COLOR;
    
    secondNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected1"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    
    // ------------------------- MapForMachineVC --------------------------------
//    [mapNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
//
//    mapNav.title = @"借还机";
//
//    mapNav.tabBarItem.image = [UIImage imageNamed:@"bar4"];
//
//    mapNav.navigationBar.barTintColor = COLOR;
//
//    mapNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected4"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    
    // ------------------------- ReadBrowserVC --------------------------------
//
//    [thirdNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
//
//    thirdNav.title = @"党建";
//
//    thirdNav.tabBarItem.image = [UIImage imageNamed:@"bar3"];
//
//    thirdNav.navigationBar.barTintColor = COLOR;
//
//    thirdNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected3"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    
    
    // ------------------------- MyInformationVC --------------------------------
    
    [forthNav.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName:COLOR} forState:UIControlStateSelected];
    
    forthNav.title = @"我的";
    
    forthNav.tabBarItem.image = [UIImage imageNamed:@"bar5"];
    
    forthNav.navigationBar.barTintColor = COLOR;
    
    forthNav.tabBarItem.selectedImage = [[UIImage imageNamed:@"barselected5"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    NSArray *tabbarArray = [NSArray arrayWithObjects:firstNav,secondNav,firs3tNav, forthNav, nil];
    
    return tabbarArray;
    
}
@end
