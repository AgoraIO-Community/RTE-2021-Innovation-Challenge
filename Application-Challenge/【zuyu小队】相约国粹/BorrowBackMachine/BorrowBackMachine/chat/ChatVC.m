//
//  ChatVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/26.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import "ChatVC.h"
#import "EMConversationsViewController.h"

@interface ChatVC ()

@end

@implementation ChatVC

- (void)viewDidLoad {
    [super viewDidLoad];
    EMConversationsViewController *vc = [[EMConversationsViewController alloc] init];
    [self.view addSubview:vc.view];
    [self addChildViewController:vc];
    // Do any additional setup after loading the view.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
