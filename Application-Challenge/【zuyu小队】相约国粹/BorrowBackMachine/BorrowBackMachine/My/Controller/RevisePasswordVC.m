//
//  RevisePasswordVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/15.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "RevisePasswordVC.h"
#import "zuyu.h"
#import "MyInformationVC.h"

@interface RevisePasswordVC ()<NavgationViewDelegate>
{
    UITextField *_oldPassWord;
    UITextField *_newPassWord;
    UITextField *_ensuresPassWord;
}
@end

@implementation RevisePasswordVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createTextFeild];
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
    view.titleStr = @"修改密码";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


-(void)createTextFeild
{
    _oldPassWord = [[UITextField alloc] initWithFrame:CGRectMake(30, 100, WIDTH - 60 , 48)];
    _oldPassWord.layer.borderColor = LIONCOLOR.CGColor;
    
    _oldPassWord.layer.borderWidth = 1.5f;
    
    _oldPassWord.layer.cornerRadius = 10;
    
    _oldPassWord.placeholder = @"请输入旧密码";
    
    _oldPassWord.leftView = [[UIView alloc] initWithFrame:CGRectMake(1, 7, 10, 1)];
    
    _oldPassWord.leftViewMode = UITextFieldViewModeAlways;
    
    _oldPassWord.secureTextEntry = YES;
    
    [self.view addSubview:_oldPassWord];
    
    
    _newPassWord = [[UITextField alloc] initWithFrame:CGRectMake(30, 170, WIDTH - 60 , 48)];
    _newPassWord.layer.borderColor = LIONCOLOR.CGColor;
    
    _newPassWord.layer.borderWidth = 1.5f;
    
    _newPassWord.layer.cornerRadius = 10;
    
    _newPassWord.placeholder = @"请输入新密码";
    
    _newPassWord.leftView = [[UIView alloc] initWithFrame:CGRectMake(1, 7, 10, 1)];
    
    _newPassWord.leftViewMode = UITextFieldViewModeAlways;
    
    _newPassWord.secureTextEntry = YES;
    
    [self.view addSubview:_newPassWord];
    
    
    
    _ensuresPassWord = [[UITextField alloc] initWithFrame:CGRectMake(30, 240, WIDTH - 60 , 48)];
    _ensuresPassWord.layer.borderColor = LIONCOLOR.CGColor;
    
    _ensuresPassWord.layer.borderWidth = 1.5f;
    
    _ensuresPassWord.layer.cornerRadius = 10;
    
    _ensuresPassWord.placeholder = @"确认新密码";
    
    _ensuresPassWord.leftView = [[UIView alloc] initWithFrame:CGRectMake(1, 7, 10, 1)];
    
    _ensuresPassWord.leftViewMode = UITextFieldViewModeAlways;
    
    _ensuresPassWord.secureTextEntry = YES;
    
    [self.view addSubview:_ensuresPassWord];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(30, HEIGHT - 80, WIDTH - 60, 50);
    
    button.backgroundColor = COLOR;
    
    [button setTitle:@"确定" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(editPassWord:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
}

-(void)editPassWord:(UIButton *)button
{
    
    NSLog(@"--------------------------------\n%@\n%@\n%@\n",_oldPassWord.text,_newPassWord.text,_ensuresPassWord.text);
    
    if (!_oldPassWord.text.length ) {
        [self alertShow:@"旧密码栏不能为空"];
    }else if (!_newPassWord.text.length){
        [self alertShow:@"新密码栏不能为空"];
    }else if (!_ensuresPassWord.text.length){
        [self alertShow:@"确认密码栏不能为空"];
    }else if (_newPassWord.text.length > 20){
        [self alertShow:@"新密码栏不能超过20个字符"];
    }else if (![_ensuresPassWord.text isEqualToString:_newPassWord.text]){
        [self alertShow:@"两次密码输入不一致"];
    }else if (_newPassWord.text.length < 6){
        [self alertShow:@"新密码栏不能小于6个字符"];
    }else{
        [self editPasswordForNet];
    }
    
}

-(void)editPasswordForNet
{
    MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"修改中...", @"HUD loading title");
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] initWithObjectsAndKeys:_oldPassWord.text,@"OldPassword" ,
                                       _newPassWord.text,@"NewPassword",
                                       _ensuresPassWord.text,@"TrueNewPassword",
                                       nil];
    
    [manager.requestSerializer setValue:HeaderField forHTTPHeaderField:@"Authorization"];
    
    [manager POST:PORT(@"Account/UpdatePassword") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSInteger isDone = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue;
        
        if (isDone) {
            
            [[NSUserDefaults standardUserDefaults] setObject:_newPassWord.text forKey:@"mima"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"修改成功,请从新登录", @"HUD message title");
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                
                [_hud hideAnimated:YES ];

                [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
                [[NSUserDefaults standardUserDefaults] synchronize];

                NSArray *temArray = self.navigationController.viewControllers;
                //
                for(UIViewController *temVC in temArray)
                {
                    if ([temVC isKindOfClass:[MyInformationVC class]])
                    {
                        [self.navigationController popToViewController:temVC animated:NO];
                        
                    }
                    
                }
                
            });
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            NSArray *temArray = self.navigationController.viewControllers;
            //
            for(UIViewController *temVC in temArray)
            {
                if ([temVC isKindOfClass:[MyInformationVC class]])
                {
                    [self.navigationController popToViewController:temVC animated:NO];
                    
                }
                
            }
        }else{
            NSString *message = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Message"]];
            
            NSString *text;
            
            if ([message isEqualToString:@"密码不正确"]) {
                text = @"原始密码错误";
            }else{
                
                text = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Message"]];
            }
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(text, @"HUD message title");
            // Move to bottm center.
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                
                [_hud hideAnimated:YES ];
                
            });
        }
        
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
     
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                [self editPasswordForNet];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                [_hud hideAnimated:YES ];
                
            }];
            
            [_hud hideAnimated:YES ];
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [_hud hideAnimated:YES ];
            });
            
        }
        NSLog(@"--->>>>>>>>>>>>>>>>>>>>>>>\n%@",error);
    }];
    
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

-(void)alertShow:(NSString *)message
{
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:message
                                                    message:nil
                                                   delegate:self
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    
    [alert show];
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
