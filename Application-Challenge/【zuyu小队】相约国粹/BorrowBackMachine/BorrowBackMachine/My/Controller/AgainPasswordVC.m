//
//  AgainPasswordVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/26.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "AgainPasswordVC.h"
#import "OnlineNavgationView.h"
#import "MyInformationVC.h"
#import "zuyu.h"

@interface AgainPasswordVC ()<NavgationViewDelegate>
{
    UIView      *_registerView;
    UITextField *_nameField;
    UITextField *_passWordField;
}

@end

@implementation AgainPasswordVC

- (void)viewDidLoad {
    
    [super viewDidLoad];
    [self createNavgation];
    [self createRegisterView];
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
    view.titleStr = @"找回密码";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


-(void)createRegisterView
{
    _registerView = [[UIView  alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT - 74)];
    image.image = [UIImage imageNamed:@"login_bg.png"];
    
    [_registerView addSubview:image];
    
    
    [self.view addSubview:_registerView];
    
    _nameField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 90, WIDTH/2 , 40)];
    
    _nameField.layer.borderWidth = 0.6f;
    
    _nameField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _nameField.backgroundColor = [UIColor whiteColor];
    
    _nameField.secureTextEntry = YES;
    
    [_registerView addSubview:_nameField];
    
    UILabel *nameLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 90, WIDTH * 0.2, 40)];
    
    nameLable.text = @"密码 :";
    
    nameLable.font = [UIFont systemFontOfSize:17];
    
    [_registerView addSubview:nameLable];
    
    
    _passWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 150, WIDTH/2 , 40)];
    
    _passWordField.backgroundColor = [UIColor whiteColor];
    
    _passWordField.layer.borderWidth = 0.6f;
    
    _passWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _passWordField.secureTextEntry = YES;
    
    [_registerView addSubview:_passWordField];
    
    UILabel *passWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 150, WIDTH * 0.25, 40)];
    
    passWordLable.text = @"确认密码 :";
    
    passWordLable.font = [UIFont systemFontOfSize:17];
    
    [_registerView addSubview:passWordLable];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH * 0.13, 280, WIDTH * 0.74, 45);
    
    button.backgroundColor = RGBA(178, 14, 6, 1);
    
    [button setTitle:@"确定" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(reviseClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_registerView addSubview:button];
    
}

-(void)reviseClick:(UIButton *)button
{
    
    if (_nameField.text.length && _passWordField.text.length) {
        MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        _hud.label.text = NSLocalizedString(@"重置中...", @"HUD loading title");
        
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
        
        NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:_userID,@"UserId",
                                   _nameField.text,@"Password",
                                   _passWordField.text,@"TurePassword",nil];
        
        [manager POST:PORT(@"Account/ResetPassword") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
                
                
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString(@"密码重置成功,请重新登录", @"HUD message title");
                _hud.label.numberOfLines = 0;
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    [_hud hideAnimated:YES ];
                    
                    for (UIViewController *controller in self.navigationController.viewControllers) {
                        if ([controller isKindOfClass:[MyInformationVC class]]) {
                            MyInformationVC *vc =(MyInformationVC *)controller;
                            [self.navigationController popToViewController:vc animated:YES];
                        }
                    }
                    
                });
                
            }else{
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
                [_hud hideAnimated:YES afterDelay:1];
            }
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
            _hud.label.numberOfLines = 0;
            [_hud hideAnimated:YES afterDelay:1];
            NSLog(@"%@",error);
        }];
        
    }else{
        
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"密码/确认密码不可为空"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        
        [alert show];
    }
    
    
    
    
    
    
    
    
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
