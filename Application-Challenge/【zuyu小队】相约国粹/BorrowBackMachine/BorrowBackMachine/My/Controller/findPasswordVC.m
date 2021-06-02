//
//  findPasswordVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/26.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "findPasswordVC.h"
#import "OnlineNavgationView.h"
#import "AgainPasswordVC.h"
#import "zuyu.h"
@interface findPasswordVC ()<NavgationViewDelegate>
{
    UIView      *_registerView;
    UITextField *_nameField;
    UITextField *_passWordField;
    NSString *_userID;
}

@end

@implementation findPasswordVC

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
    
    [_registerView addSubview:_nameField];
    
    UILabel *nameLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 90, WIDTH * 2, 40)];
    
    nameLable.text = @"手机号 :";
    
    nameLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:nameLable];
    
    
    _passWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 150, WIDTH/2 , 40)];
    
    _passWordField.backgroundColor = [UIColor whiteColor];
    
    _passWordField.layer.borderWidth = 0.6f;
    
    _passWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    [_registerView addSubview:_passWordField];
    
    UILabel *passWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 150, WIDTH * 0.2, 40)];
    
    passWordLable.text = @"身份证 :";
    
    passWordLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:passWordLable];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH * 0.13, 280, WIDTH * 0.74, 45);
    
    button.backgroundColor = RGBA(178, 14, 6, 1);
    
    [button setTitle:@"下一步" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(nextClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_registerView addSubview:button];
    
}

-(void)nextClick:(UIButton *)button
{
    
    if (_nameField.text.length && _passWordField.text.length) {
        
    
    
    MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:_nameField.text,@"PhoneNumber",
                                _passWordField.text,@"CardNo",nil];
    
    [manager POST:PORT(@"Account/GetUserByAccount") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            _userID = [ZuyuJsonRead jsonRead:[responseObject objectForKey:@"Data"] WithKey:@"Id"];
            
            [_hud hideAnimated:YES];
            
            
            
            AgainPasswordVC *vc = [[AgainPasswordVC alloc] init];
            
            vc.userID = _userID;
            
            
            [vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:vc animated:YES];
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
        MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"手机号和身份证号不可为空", @"HUD message title");
        _hud.label.numberOfLines = 0;
        [_hud hideAnimated:YES afterDelay:1];    }
    
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
