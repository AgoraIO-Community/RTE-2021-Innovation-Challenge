//
//  TiaoKuanVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/30.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "TiaoKuanVC.h"
#import "zuyu.h"
@interface TiaoKuanVC ()<NavgationViewDelegate>

@end

@implementation TiaoKuanVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    

    // Do any additional setup after loading the view.
}
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = @"隐私政策";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
