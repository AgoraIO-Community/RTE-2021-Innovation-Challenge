//
//  RevisePhoneNumController.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/12/26.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "RevisePhoneNumController.h"
#import "zuyu.h"
#import "RebindPhoneVC.h"

@interface RevisePhoneNumController ()<NavgationViewDelegate>
{
    UITextField *_phoneNumField;
}
@end

@implementation RevisePhoneNumController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createUI];
    // Do any additional setup after loading the view.
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = @"更改手机";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
}




-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

 

#pragma createUI

-(void)createUI
{
    _phoneNumField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.1 , 120, WIDTH * 0.8 , 40)];
    
    _phoneNumField.backgroundColor = [UIColor whiteColor];
    
    _phoneNumField.layer.borderWidth = 0.6f;
    
    _phoneNumField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _phoneNumField.keyboardType = UIKeyboardTypeNumberPad;
    
    _phoneNumField.placeholder = @"请输入旧手机号";
    [self.view addSubview:_phoneNumField];
    
//
//    UILabel *_phoneNumLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 100, WIDTH * 0.2, 40)];
//
//    _phoneNumLable.text = @"手机号 :";
//
//    [self.view addSubview:_phoneNumLable];
//
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(WIDTH *0.1, 240, WIDTH * 0.8, 45);
    [button setTitle:@"下一步" forState:UIControlStateNormal];
    button.backgroundColor = COLOR;
    [button addTarget:self action:@selector(buttonClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
    
}

-(void)buttonClick:(UIButton *)button
{
    if ([_phoneNumField.text isEqualToString:PhoneNum]) {
        
        RebindPhoneVC *vc = [[RebindPhoneVC alloc] init];
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
//        NSLog(@"验证成功");
    }else if (!_phoneNumField.text.length){
        [ZuyuAlertShow alertShow:@"请输入旧手机号" viewController:self];
    }else{
        [ZuyuAlertShow alertShow:@"输入的手机号与旧手机号不一致" viewController:self];
    }
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
