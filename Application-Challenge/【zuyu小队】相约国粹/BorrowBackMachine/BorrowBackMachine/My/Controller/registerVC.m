//
//  registerVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/25.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "registerVC.h"
#import "OnlineNavgationView.h"
#import "JKCountDownButton.h"
#import "zuyu.h"
#import <SMS_SDK/SMSSDK.h>

@interface registerVC ()<NavgationViewDelegate,UITextFieldDelegate>
{
    UIView      *_registerView;
    UITextField *_nameField;
    UITextField *_passWordField;
    UITextField *_surePassWordField;
    UITextField *_personIDField;
    UITextField *_phoneNumField;
    UITextField *_verifyField;
    JKCountDownButton *_countDownCode;
    NSInteger _isSend;
    NSString *_SessionId;
}
@end

@implementation registerVC

- (void)viewDidLoad {
    [super viewDidLoad];
    _isSend = 0;
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
    view.titleStr = @"注册";
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
    
    _nameField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 90, WIDTH/2 , 40)];
    
    _nameField.layer.borderWidth = 0.6f;

    _nameField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _nameField.backgroundColor = [UIColor whiteColor];
    
    _nameField.placeholder = @"请输入账号";
    [_registerView addSubview:_nameField];
    
    UILabel *nameLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 90, WIDTH * 0.17, 40)];
   
    nameLable.text = @"账号 :";
    
    nameLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:nameLable];
    
    _passWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 150, WIDTH/2 , 40)];
    
    _passWordField.backgroundColor = [UIColor whiteColor];
   
    _passWordField.layer.borderWidth = 0.6f;
    
    _passWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
  
    _passWordField.secureTextEntry = YES;
    
    _passWordField.placeholder = @"请输入密码";
    
    [_registerView addSubview:_passWordField];
    
    UILabel *passWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 150, WIDTH * 0.2, 40)];
    
    passWordLable.text = @"密码 :";
    
    passWordLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:passWordLable];
    
    
    _surePassWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 210, WIDTH/2 , 40)];
    
    _surePassWordField.backgroundColor = [UIColor whiteColor];
    
    _surePassWordField.layer.borderWidth = 0.6f;
    
    _surePassWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _surePassWordField.secureTextEntry = YES;

    _surePassWordField.placeholder = @"请确认密码";
    [_registerView addSubview:_surePassWordField];
    
    UILabel *surePassWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 210, WIDTH * 0.2, 40)];
    
    surePassWordLable.text = @"确认密码 :";
    
    [surePassWordLable setAdjustsFontSizeToFitWidth:YES];
    
    surePassWordLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:surePassWordLable];
    
//    _personIDField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 270, WIDTH/2 , 40)];
//
//    _personIDField.delegate = self;
//    _personIDField.backgroundColor = [UIColor whiteColor];
//
//    _personIDField.layer.borderWidth = 0.6f;
//
//    _personIDField.layer.borderColor = [UIColor lightGrayColor].CGColor;
//
//    _personIDField.placeholder = @"请输入身份证号";
//    [_registerView addSubview:_personIDField];
//
//    UILabel *personIDLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 270, WIDTH * 0.2, 40)];
//
//    personIDLable.text = @"身份证 :";
//
//    personIDLable.font = [UIFont systemFontOfSize:19];
//
//    [_registerView addSubview:personIDLable];
//
//
//    _phoneNumField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 330, WIDTH/2 , 40)];
//
//    _phoneNumField.backgroundColor = [UIColor whiteColor];
//
//    _phoneNumField.delegate = self;
//    _phoneNumField.layer.borderWidth = 0.6f;
//
//    _phoneNumField.layer.borderColor = [UIColor lightGrayColor].CGColor;
//
//    _phoneNumField.keyboardType = UIKeyboardTypeNumberPad;
//
//    _phoneNumField.placeholder = @"请输入手机号";
//    [_registerView addSubview:_phoneNumField];
//
//
//    UILabel *_phoneNumLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 330, WIDTH * 0.2, 40)];
//
//    _phoneNumLable.text = @"手机号 :";
//
//    _phoneNumLable.font = [UIFont systemFontOfSize:19];
//
//    [_registerView addSubview:_phoneNumLable];
//
//    _verifyField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.35 , 390, WIDTH/4 - 5 , 40)];
//
//    _verifyField.backgroundColor = [UIColor whiteColor];
//
//    _verifyField.layer.borderWidth = 0.6f;
//
//    _verifyField.layer.borderColor = [UIColor lightGrayColor].CGColor;
//
//    _verifyField.keyboardType = UIKeyboardTypeNumberPad;
//
//    [_registerView addSubview:_verifyField];
//
//    UILabel *verifyLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 390, WIDTH * 3, 40)];
//
//    verifyLable.text = @"验证码 :";
//
//    verifyLable.font = [UIFont systemFontOfSize:19];
//
//    [_registerView addSubview:verifyLable];
//
//
////        --------------------    倒计时按钮      ---------------------
//
//    _countDownCode = [JKCountDownButton buttonWithType:UIButtonTypeCustom];
//    _countDownCode.frame = CGRectMake(CGRectGetMaxX(_verifyField.frame) + 10, CGRectGetMinY(_verifyField.frame), WIDTH/4 - 5, 40);
//    [_countDownCode setTitle:@"发送验证码" forState:UIControlStateNormal];
//
//    _countDownCode.backgroundColor = RGBA(178, 14, 6, 1);
//
//    [_registerView addSubview:_countDownCode];
//
//    _countDownCode.titleLabel.font = [UIFont systemFontOfSize:14];
//
//    [_countDownCode countDownButtonHandler:^(JKCountDownButton*sender, NSInteger tag) {
//
//        _isSend = 1;
//
//        sender.enabled = NO;
//
//
//        [sender countDownChanging:^NSString *(JKCountDownButton *countDownButton,NSUInteger second) {
//            NSString *title = [NSString stringWithFormat:@"剩余%zd秒",second];
//            return title;
//        }];
//        [sender countDownFinished:^NSString *(JKCountDownButton *countDownButton, NSUInteger second) {
//            countDownButton.enabled = YES;
//            return @"点击重新获取";
//
//        }];
//
//        if (_phoneNumField.text.length) {
//            NSString *str = [_phoneNumField.text substringToIndex:1];
//
//            if (_phoneNumField.text.length == 11 && [str isEqualToString:@"1"]) {
//                NSLog(@"----------->>>>>>>>>>>>>获取验证码");
//                [self getVerity:_phoneNumField.text];
//            }else{
//                [ZuyuAlertShow alertShow:@"请输入正确的手机号" viewController: self];
//                [sender stopCountDown];
//                _isSend = 0;
//                sender.enabled = YES;
//            }
//        }else{
//            [ZuyuAlertShow alertShow:@"请填写手机号" viewController: self];
//            sender.enabled = YES;
//
//        }
//
//    }];
//
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH * 0.1, 510, WIDTH * 0.8, 45);
    
    button.backgroundColor = RGBA(178, 14, 6, 1);
    
    [button setTitle:@"注册" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(registerClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_registerView addSubview:button];

}


- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if (textField == _phoneNumField) {
        //这里的if时候为了获取删除操作,如果没有次if会造成当达到字数限制后删除键也不能使用的后果.
        if (range.length == 1 && string.length == 0) {
            return YES;
        }
        //so easy
        else if (_phoneNumField.text.length >= 11) {
            _phoneNumField.text = [textField.text substringToIndex:11];
            return NO;
        }
    }
    
    if (textField == _personIDField) {
        if (range.length == 1 && string.length == 0) {
            return YES;
        }
        //so easy
        else if (_personIDField.text.length >= 18) {
            _personIDField.text = [textField.text substringToIndex:18];
            return NO;
        }
    }
    return YES;
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
#pragma mark - 注册事件
-(void)registerClick:(UIButton *)button
{ 
    
    if (!_nameField.text.length) {
        [ZuyuAlertShow alertShow:@"账号不能为空" viewController: self];
    }else if (!_passWordField.text.length){
        [ZuyuAlertShow alertShow:@"密码不能为空" viewController: self];
    }else if(![_surePassWordField.text isEqualToString:_passWordField.text]){
        [ZuyuAlertShow alertShow:@"两次密码输入不一致" viewController: self];
    }else{
        [self registerCount];
    }
    
}


-(void)registerCount
{
    
    MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    _hud.label.text = NSLocalizedString(@"注册中...", @"HUD loading title");
    
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
        NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:_nameField.text,@"phone",
                                    _passWordField.text,@"pass",
                                    nil];
    
        [manager POST:@"http://121.42.12.84/api/v1/reg" parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if (![NSString stringWithFormat:@"%@",[responseObject objectForKey:@"err"]].integerValue) {
                
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString(@"注册成功", @"HUD message title");
                
                
                [[EMClient sharedClient] registerWithUsername:_nameField.text password:_passWordField.text completion:^(NSString *aUsername, EMError *aError) {
                    if (!aError) {
                        NSLog(@"注册成功");
                    } else {
                        NSLog(@"注册失败的原因---%@", aError.errorDescription);
                    }
                }];
                
                
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [_hud hideAnimated:YES ];
                    [self.navigationController popViewControllerAnimated:YES];
                });
            }else{
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"info"], @"HUD message title");
                _hud.label.numberOfLines = 0;
                [_hud hideAnimated:YES afterDelay:1];
            }
            
            
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
          
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
            _hud.label.numberOfLines = 0;
            [_hud hideAnimated:YES afterDelay:1];
            NSLog(@"%@",error);
        }];
    
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
