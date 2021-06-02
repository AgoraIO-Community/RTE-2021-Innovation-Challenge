//
//  ScanLoginVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ScanLoginVC.h"
#import "zuyu.h"

@interface ScanLoginVC ()<NavgationViewDelegate>

@end

@implementation ScanLoginVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createBackImage];
    // Do any additional setup after loading the view.
}
#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"扫描结果";
    [self.view addSubview:view];
}
-(void)navPop
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - createUI
-(void)createBackImage
{
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    image.image = [UIImage imageNamed:@"scanLoginBGI"];
    
    [self.view addSubview:image];
    
    UIImageView *headImage = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH * 0.4, 120, WIDTH * 0.2, WIDTH * 0.2)];
    
    [headImage sd_setImageWithURL:[NSURL URLWithString:HEADIMAGE] placeholderImage:[UIImage imageNamed:@"logoImage"]];
    
    headImage.layer.cornerRadius = WIDTH * 0.1;

    headImage.layer.masksToBounds = YES;
    
    [self.view addSubview:headImage];
    
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(headImage.frame) + 20, WIDTH, 40)];
    
    lable.textAlignment = NSTextAlignmentCenter;
    
    lable.textColor = [UIColor lightGrayColor];
    
    lable.text = @"将登录中创自助借还机,请确认是否本人操作";
    
    [self.view addSubview:lable];
    
    UIButton *loginButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    loginButton.frame = CGRectMake(20, CGRectGetMaxY(lable.frame) + 40, WIDTH - 40, 40);
    
    [loginButton setTitle:@"允许登录中创自助借还机" forState:UIControlStateNormal];
    
    loginButton.backgroundColor = COLOR;
    
    [loginButton addTarget:self action:@selector(loginClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:loginButton];
    
    
    UIButton *popButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    popButton.frame = CGRectMake(20, CGRectGetMaxY(loginButton.frame) + 20, WIDTH - 40, 40);
    
    [popButton setTitle:@"取消" forState:UIControlStateNormal];
    
    popButton.backgroundColor = RGBA(186, 156, 129, 1);
    
    [popButton addTarget:self action:@selector(popClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:popButton];
}


-(void)popClick:(UIButton *)button
{
    [self dismissViewControllerAnimated:YES completion:nil];
}



#pragma mark - login

-(void)loginClick:(UIButton *)button
{
//    Account/LoginByScanQrCode
    
    MBProgressHUD * hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    hud.label.text = NSLocalizedString(@"登录中...", @"HUD loading title");
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
//
    
    [manager POST:PORT(@"Account/LoginByScanQrCode") parameters:_parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]] integerValue]){
            
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"登录成功", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [hud hideAnimated:YES ];
                [self dismissViewControllerAnimated:YES completion:nil];


            });
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            
        }else{
            
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"上传失败", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [hud hideAnimated:YES ];
            });
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self loginClick:button];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"登录失败", @"HUD message title");
            [hud hideAnimated:YES afterDelay:1];
            
        }
        
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
