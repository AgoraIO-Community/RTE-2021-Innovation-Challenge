//
//  BaceViewController.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BaceViewController.h"
#import "AFNetworking.h"
#import "zuyu.h"

@interface BaceViewController ()

@end

@implementation BaceViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 0.1)];
    [self.view addSubview:lable];
    [self UIBarButtonBackItemSet];

    // Do any additional setup after loading the view.
}

#pragma mark - 导航栏返回按钮颜色设置 及 文字变成<
//  导航栏返回按钮颜色设置 及 文字变成<
-(void)UIBarButtonBackItemSet
{
    UIBarButtonItem *btn_back = [[UIBarButtonItem alloc]init];
    btn_back.title = @"";
    self.navigationItem.backBarButtonItem = btn_back;
    self.navigationController.navigationBar.barStyle = UIStatusBarStyleDefault;
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
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
