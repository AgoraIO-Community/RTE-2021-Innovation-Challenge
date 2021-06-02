//
//  ImproveMessageVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ImproveMessageVC.h"
#import "zuyu.h"
#import "JKCountDownButton.h"
#import <SMS_SDK/SMSSDK.h>


@interface ImproveMessageVC ()<NavgationViewDelegate>
{
    UIView      *_registerView;
    UITextField *_nameField;
    UITextField *_passWordField;
    UITextField *_personIDField; 
    UITextField *_phoneNumField;
    UITextField *_verifyField;
    JKCountDownButton *_countDownCode;
    NSInteger _isSend;
    NSString *_SessionId;

}
@end

@implementation ImproveMessageVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createRegisterView];
    // Do any additional setup after loading the view.
}

-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"完善资料";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


-(void)createRegisterView
{
    _registerView = [[UIView  alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    _registerView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"login_bg.png"]];
    
    
    [self.view addSubview:_registerView];
    
    _passWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 100, WIDTH/2 , 40)];
    
    _passWordField.backgroundColor = [UIColor whiteColor];
    
    _passWordField.layer.borderWidth = 0.6f;
    
    _passWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _passWordField.secureTextEntry = YES;
    
    [_registerView addSubview:_passWordField];
    
    UILabel *passWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 100, WIDTH * 0.2, 40)];
    
    passWordLable.text = @"新密码 :";
    
    
    
    [_registerView addSubview:passWordLable];
    
    
    
    _phoneNumField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 160, WIDTH/2 , 40)];
    
    _phoneNumField.backgroundColor = [UIColor whiteColor];
    
    _phoneNumField.layer.borderWidth = 0.6f;
    
    _phoneNumField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _phoneNumField.keyboardType = UIKeyboardTypeNumberPad;
    
    [_registerView addSubview:_phoneNumField];
    
    
    UILabel *_phoneNumLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 160, WIDTH * 0.2, 40)];
    
    _phoneNumLable.text = @"手机号 :";
    
    [_registerView addSubview:_phoneNumLable];
    
        _verifyField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 220, WIDTH/4 - 5 , 40)];
    
        _verifyField.backgroundColor = [UIColor whiteColor];
    
        _verifyField.layer.borderWidth = 0.6f;
    
        _verifyField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
        _verifyField.keyboardType = UIKeyboardTypeNumberPad;
    
        [_registerView addSubview:_verifyField];
    
        UILabel *verifyLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 220, WIDTH * 0.17, 40)];
    
        verifyLable.text = @"验证码 :";
    
        [_registerView addSubview:verifyLable];
    
    
//        --------------------    倒计时按钮      ---------------------
    
        _countDownCode = [JKCountDownButton buttonWithType:UIButtonTypeCustom];
        _countDownCode.frame = CGRectMake(CGRectGetMaxX(_verifyField.frame) + 10, CGRectGetMinY(_verifyField.frame), WIDTH/4 - 5, 40);
        [_countDownCode setTitle:@"发送验证码" forState:UIControlStateNormal];
    
        _countDownCode.backgroundColor = RGBA(178, 14, 6, 1);
    
        [_registerView addSubview:_countDownCode];
    
        _countDownCode.titleLabel.font = [UIFont systemFontOfSize:14];
    
    [_countDownCode countDownButtonHandler:^(JKCountDownButton*sender, NSInteger tag) {
        
        _isSend = 1;
        
        sender.enabled = NO;
        
        
        [sender countDownChanging:^NSString *(JKCountDownButton *countDownButton,NSUInteger second) {
            NSString *title = [NSString stringWithFormat:@"剩余%zd秒",second];
            return title;
        }];
        [sender countDownFinished:^NSString *(JKCountDownButton *countDownButton, NSUInteger second) {
            countDownButton.enabled = YES;
            return @"点击重新获取";
            
        }];
        
        if (_phoneNumField.text.length) {
            NSString *str = [_phoneNumField.text substringToIndex:1];
            
            if (_phoneNumField.text.length == 11 && [str isEqualToString:@"1"]) {
                NSLog(@"----------->>>>>>>>>>>>>获取验证码");
                [self getVerity:_phoneNumField.text];
            }else{
                [ZuyuAlertShow alertShow:@"请输入正确的手机号" viewController: self];
                [sender stopCountDown];
                _isSend = 0;
                sender.enabled = YES;
            }
        }else{
            [ZuyuAlertShow alertShow:@"请填写手机号" viewController: self];
            _isSend = 0;
            sender.enabled = YES;
        }
         
    }];
     
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH * 0.1, 450, WIDTH * 0.8, 45);
    
    button.backgroundColor = RGBA(178, 14, 6, 1);
    
    [button setTitle:@"完成" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(registerClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_registerView addSubview:button];
    
}


-(void)registerClick:(UIButton *)button
{
    
    NSString *num = @"";

    if (_phoneNumField.text.length) {
        num = [_phoneNumField.text substringToIndex:1];
    }
    
    if (!_passWordField.text.length) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"新密码不能为空"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        
        [alert show];
    }else if(!_phoneNumField.text.length){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"手机号不能为空"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        
        [alert show];
    }else if (!_verifyField.text.length){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"验证码不能为空"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        [alert show];

    }else if (_phoneNumField.text.length != 11){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"手机号输入有误"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        [alert show];
        
    }else if (![num isEqualToString:@"1"]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"手机号输入有误"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        [alert show];
        
    }else if(!_isSend){
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"请发送验证码"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"好的"
                                              otherButtonTitles:nil];
        [alert show];
    }else{
        
        [SMSSDK commitVerificationCode:_verifyField.text phoneNumber:_phoneNumField.text zone:@"86" result:^(NSError *error) {
            if (!error){
                [self registerCount];
                
            }else{
                [ZuyuAlertShow alertShow:@"验证码错误" viewController:self];
            }
        }];
    }
}

-(void)registerCount
{
    MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:       _phoneNumField.text,@"Phone",
                               _passWordField.text,@"NewPassword" ,nil];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    
    [manager POST:PORT(@"Account/BindingIdentity") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"完善资料成功,请重新登录", @"HUD message title");
            _hud.label.numberOfLines = 0;
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                
                [_hud hideAnimated:YES];
                [self.navigationController popViewControllerAnimated:YES];
            });
            
            
        }else{
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
            [_hud hideAnimated:YES afterDelay:1];
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"网络失败,请稍后再试", @"HUD message title");
        _hud.label.numberOfLines = 0;
        [_hud hideAnimated:YES afterDelay:1];
    }];
    
}


#pragma mark - 获取验证码
-(void)getVerity:(NSString *)mobNum
{
    
    MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    NSString *headNum = [mobNum substringToIndex:3];
    NSString *footNum = [mobNum substringFromIndex:7];
    NSString *num     = [NSString stringWithFormat:@"%@****%@",headNum,footNum];
    NSString *title   = [NSString stringWithFormat:@"验证码正在发送至%@中",num];
    
    _hud.label.text = NSLocalizedString(title, @"HUD loading title");
    [SMSSDK getVerificationCodeByMethod:SMSGetCodeMethodSMS phoneNumber:mobNum zone:@"86"  result:^(NSError *error) {
        
        if (!error)
        {
            _isSend = YES;
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"发送成功", @"HUD message title");
            [_hud hideAnimated:YES afterDelay: 1];
            [_countDownCode startCountDownWithSecond:60];
            [_countDownCode countDownChanging:^NSString *(JKCountDownButton *countDownButton,NSUInteger second) {
                NSString *title = [NSString stringWithFormat:@"剩余%zd秒",second];
                return title;
            }];
            [_countDownCode countDownFinished:^NSString *(JKCountDownButton *countDownButton, NSUInteger second) {
                countDownButton.enabled = YES;
                return @"点击重新获取";
            }];
            // 请求成功
        }
        else
        {
            _isSend = NO;
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"发送失败", @"HUD message title");
            [_hud hideAnimated:YES afterDelay: 1];
            _countDownCode.enabled = YES;
            // error
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
