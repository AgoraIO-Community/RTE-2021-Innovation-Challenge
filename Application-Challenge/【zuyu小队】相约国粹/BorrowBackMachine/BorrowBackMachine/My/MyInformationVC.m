//
//  MyInformationVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MyInformationVC.h"
#import "MyInformationCell.h"
#import "LogOutCell.h"
#import "registerVC.h"
#import "OnlineNavgationView.h"
#import "findPasswordVC.h"
#import "zuyu.h"
#import "MySettingVC.h"
#import "CreateCodeVC.h"
#import "BespeakVC.h"
#import "OtherVC.h"
#import "ReadBrowserVC.h"
#import "MyMessageVC.h"
#import "ImproveMessageVC.h"
#import "UILabel+YBAttributeTextTapAction.h"
#import "TiaoKuanVC.h"


@interface MyInformationVC ()<UITableViewDelegate,UITableViewDataSource,NavgationViewDelegate,YBAttributeTapActionDelegate>
{
    NSArray     *_myListArray;
    NSArray     *_ListImageArray;

    UITableView *_tableView;
    NSInteger    _islog;

    UIView      *_registerView;
    UITextField *_nameField;
    UITextField *_passWordField;
    
    
    UIView *_NOLoginView;
    UIView *_loginView;
    
    NSString *_userName;
    NSString *_userHeadImage;
    NSString *_cardID;
    NSString *_phoneNum;

    
}
@end

@implementation MyInformationVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _islog = [ISLOGIN integerValue];

    _loginView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT - 44)];
    _NOLoginView = [[UIView alloc] initWithFrame:self.view.frame];
    
    [self.view addSubview:_loginView];
    [self.view addSubview:_NOLoginView];

    [self createTableView];
    [self createLoginView];
    
    _NOLoginView.hidden = _islog;
    _loginView.hidden = !_islog;

    // Do any additional setup after loading the view.
}

#pragma mark - No Login type

-(void)createLoginView
{
//    [self createNavgation];
    [self createRegisterView];
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = YES;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleLableHidden = NO;
    view.titleStr = @"登录";
    [_NOLoginView addSubview:view];
}


-(void)createRegisterView
{
    
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = YES;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleLableHidden = NO;
    view.titleStr = @"登录";
    [_NOLoginView addSubview:view];
    
    _registerView = [[UIView  alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT - 74)];
    image.image = [UIImage imageNamed:@"login_bg.png"];
    
    [_registerView addSubview:image];
    
    
    [_NOLoginView addSubview:_registerView];
    
    _nameField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.3 , 90, WIDTH * 0.6 , 40)];
    
    _nameField.layer.borderWidth = 0.6f;
    
    _nameField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _nameField.backgroundColor = [UIColor whiteColor];
    
    _nameField.placeholder = @" 请输入账号";
    
    [_registerView addSubview:_nameField];
    
    UILabel *nameLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 90, WIDTH * 0.2, 40)];
    
    nameLable.text = @"帐号 :";
    
    nameLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:nameLable];
    
    _passWordField = [[UITextField alloc] initWithFrame:CGRectMake(WIDTH*0.3 , 150, WIDTH * 0.6 , 40)];
    
    _passWordField.backgroundColor = [UIColor whiteColor];
    
    _passWordField.layer.borderWidth = 0.6f;
    
    _passWordField.secureTextEntry = YES;
    
    _passWordField.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    _passWordField.placeholder = @" 请输入密码";
    [_registerView addSubview:_passWordField];
    
    UILabel *passWordLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH *0.1, 150, WIDTH * 0.2, 40)];
    
    passWordLable.text = @"密码 :";
    
    passWordLable.font = [UIFont systemFontOfSize:19];
    
    [_registerView addSubview:passWordLable];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH * 0.1, 280, WIDTH * 0.8, 45);
    
    button.backgroundColor = RGBA(178, 14, 6, 1);
    
    [button setTitle:@"登录" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(loginClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_registerView addSubview:button];
    
    UIButton *forgetButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    forgetButton.frame = CGRectMake(15 , 340, WIDTH * 0.37, 35);
    
    [forgetButton setTitle:@"忘记密码" forState:UIControlStateNormal];
    
    [forgetButton addTarget:self action:@selector(forgetPassword:) forControlEvents:UIControlEventTouchUpInside];
    
    [forgetButton setTitleColor:[UIColor lightGrayColor] forState:UIControlStateNormal];
    
    [_registerView addSubview:forgetButton];
    
    
    UIButton *regButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    regButton.frame = CGRectMake(WIDTH * 0.61 , 340, WIDTH * 0.37, 35);
    
    [regButton setTitle:@"注册账号" forState:UIControlStateNormal];
    
    [regButton addTarget:self action:@selector(regClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [regButton setTitleColor:[UIColor lightGrayColor] forState:UIControlStateNormal];
    
    [_registerView addSubview:regButton];
    
//    [self xieyiButton];
    
    
    
}
#pragma mark - 同意协议按钮.
-(void)xieyiButton
{
    NSString *labelString = @"登录即代表阅读并同意 隐私条款";
    NSMutableAttributedString * attrStr = [[NSMutableAttributedString alloc] initWithString:labelString];
    [attrStr addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14] range:NSMakeRange(0, labelString.length)];
    [attrStr addAttribute:NSForegroundColorAttributeName value:[UIColor blueColor] range:NSMakeRange(labelString.length-4, 4)];
    UILabel *ybLabel1 = [[UILabel alloc] initWithFrame:CGRectMake(10, HEIGHT - 170, self.view.bounds.size.width - 20, 40)];
    ybLabel1.numberOfLines = 1;
    ybLabel1.attributedText = attrStr;
    ybLabel1.textAlignment = NSTextAlignmentCenter;
    [_registerView addSubview:ybLabel1];
    [ybLabel1 yb_addAttributeTapActionWithStrings:@[@"隐私条款"] delegate:self];
}
//点击协议
- (void)yb_attributeTapReturnString:(NSString *)string range:(NSRange)range index:(NSInteger)index
{
//    NSString *message = [NSString stringWithFormat:@"点击了“%@”字符\nrange: %@\nindex: %ld",string,NSStringFromRange(range),index];
//    NSLog(@"%@",message);
    
    TiaoKuanVC *vc = [[TiaoKuanVC alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
}


-(void)forgetPassword:(UIButton *)button
{
    findPasswordVC *vc = [[findPasswordVC alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
    
}

-(void)regClick:(UIButton *)button
{
    
    registerVC *vc = [[registerVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
}


#pragma mark - 登陆

-(void)loginClick:(UIButton *)button
{
    
    
    if (!_nameField.text.length && button.tag != 758) {
        [ZuyuAlertShow alertShow:@"账号不能为空" viewController:self];
    }else if (!_passWordField.text.length && button.tag != 758) {
        [ZuyuAlertShow alertShow:@"密码不能为空" viewController:self];
    }else{
        
        [[[UIApplication sharedApplication] keyWindow] endEditing:YES];

        MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        _hud.label.text = NSLocalizedString(@"登录中...", @"HUD loading title");
        
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
        
        NSDictionary *parameter = [[NSDictionary alloc] initWithObjectsAndKeys:_nameField.text,@"phone",
                                   _passWordField.text,@"pass",
                                   nil];
        
        
        
        if (button.tag == 758) {
            parameter = [[NSDictionary alloc] initWithObjectsAndKeys:[[NSUserDefaults standardUserDefaults] objectForKey:@"account"],@"phone",
                         [[NSUserDefaults standardUserDefaults] objectForKey:@"password"],@"pass",
                         nil];
        }
        
        [manager POST:@"http://121.42.12.84/api/v1/login" parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if (![NSString stringWithFormat:@"%@",[responseObject objectForKey:@"err"]].integerValue) {
             
                NSString *userID = [[responseObject objectForKey:@"info"] objectForKey:@"user_id"];
                NSString *userName = [[responseObject objectForKey:@"info"] objectForKey:@"username"];
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString(@"登陆成功", @"HUD message title");
                [_hud hideAnimated:YES afterDelay:1];
                
                    _userName = userName;
                    _userHeadImage = [ZuyuJsonRead jsonRead:nil WithKey:@"HeadImgUrl"];
                    
                    _cardID = @"";
                    _phoneNum = @"";
                
                [[EMClient sharedClient] loginWithUsername:[parameter objectForKey:@"phone"] password:[parameter objectForKey:@"pass"]  completion:^(NSString *aUsername, EMError *aError) {
                    if (!aError) {
                        // 设置自动登录
                        NSLog(@"登录成功-----");
                    } else {
                        NSLog(@"登录失败----%@", aError.errorDescription);
                    }
                }];
                    
                
                    
                [[NSUserDefaults standardUserDefaults] setObject:userID forKey:@"userID"];
                [[NSUserDefaults standardUserDefaults] setObject:userName forKey:@"userName"];
                [[NSUserDefaults standardUserDefaults] setObject:_nameField.text forKey:@"account"];
                [[NSUserDefaults standardUserDefaults] setObject:_passWordField.text forKey:@"password"];

                    
                    [_tableView reloadData];
                    
                    _loginView.hidden = NO;
                    _NOLoginView.hidden = YES;
                    _islog = 1;
                    _nameField.text = @"";
                    _passWordField.text = @"";
                    
                    [_hud hideAnimated:YES ];
                    
                    [[NSUserDefaults standardUserDefaults] setObject:@"1" forKey:@"isLogin"];
                    [[NSUserDefaults standardUserDefaults] synchronize];
                
            }else{
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"info"], @"HUD message title");
                [_hud hideAnimated:YES afterDelay:1];
            }

        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误", @"HUD message title");
            [_hud hideAnimated:YES afterDelay:1];
        }];
    }
  
}


#pragma mark - login type
-(void)createTableView
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = YES;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = NO;
    view.titleLableHidden = NO;
    view.rightItmeImage = @"mySetting.png";
    view.titleStr = @"我的";
    [_loginView addSubview:view];
    
    _myListArray = [NSArray arrayWithObjects:
                    @"我的二维码",
                    @"我的下载",
                    nil];
    
    _ListImageArray = [NSArray arrayWithObjects:
                       @"myLogo2",
                       @"myLogo1",
                       @"myLogo6",
                       @"myLogo3",
                       @"myLogo4",
                       @"myLogo5",
                       
                       @"myLogo7",nil];
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74 - 44)];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.separatorStyle = UITableViewCellSelectionStyleNone;
    [_loginView addSubview:_tableView];
    
}

#pragma mark - 更多设置
-(void)navCreateCode
{
    MySettingVC *vc = [[MySettingVC alloc] init];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
//    NSLog(@" -->>>>>>>>setting");
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (!indexPath.row) {
        return 100;
    }
    
    return 70;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _myListArray.count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (!indexPath.row) {
        LogOutCell *cell = [tableView dequeueReusableCellWithIdentifier:@"LogOutCell"];
        if (cell == nil) {
            cell = [[LogOutCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"LogOutCell"];
        }
        
        cell.nameText = _userName;
        
        [cell.headImage sd_setImageWithURL:[NSURL URLWithString:_userHeadImage] placeholderImage:[UIImage imageNamed:@"logoImage"]];
        
        return cell;
    }
    
    MyInformationCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MyInformationCell"];
    
    if (cell == nil) {
        cell = [[MyInformationCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MyInformationCell"];
    }
    
    cell.title = _myListArray[indexPath.row - 1];
    
    cell.image = _ListImageArray[indexPath.row - 1];
    
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.row == 0) {
        
        MyMessageVC *vc = [[MyMessageVC alloc] init];
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (indexPath.row == 1) {
        
        CreateCodeVC *vc = [[CreateCodeVC alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (indexPath.row == 2) {
        
        ReadBrowserVC *vc = [[ReadBrowserVC alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
  
    }else if (indexPath.row == 3){
        BespeakVC *vc = [[BespeakVC alloc] init];
        
        vc.navTitle = @"已预约";
        
        vc.type = @"1";
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
    }else if (indexPath.row == 4){
        OtherVC *vc = [[OtherVC alloc] init];
        vc.NavTitle = @"借书记录";
        vc.type = 0;
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (indexPath.row == 5){
        
        BespeakVC *vc = [[BespeakVC alloc] init];
        vc.navTitle = @"预约记录";
        vc.type = @"0";
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (indexPath.row == 6){
        
        OtherVC *vc = [[OtherVC alloc] init];
        vc.NavTitle = @"未还记录";
        vc.type = 4;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (indexPath.row == 7){
        OtherVC *vc = [[OtherVC alloc] init];
        vc.NavTitle = @"超期记录";
        vc.type = 3;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    
}


-(void)getMyMessage
{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    
    NSDictionary * parameter = [[NSDictionary alloc] initWithObjectsAndKeys:[[NSUserDefaults standardUserDefaults] objectForKey:@"account"],@"phone",
                     [[NSUserDefaults standardUserDefaults] objectForKey:@"password"],@"pass",
                     nil];
    
    [manager POST:@"http://121.42.12.84/api/v1/login" parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if (![NSString stringWithFormat:@"%@",[responseObject objectForKey:@"err"]].integerValue) {
         
            NSString *userID = [[responseObject objectForKey:@"info"] objectForKey:@"user_id"];
            NSString *userName = [[responseObject objectForKey:@"info"] objectForKey:@"username"];
          
                _userName = userName;
         
                
                
            [[NSUserDefaults standardUserDefaults] setObject:userID forKey:@"userID"];
            [[NSUserDefaults standardUserDefaults] setObject:userName forKey:@"userName"];
                
                [_tableView reloadData];
                
                _loginView.hidden = NO;
                _NOLoginView.hidden = YES;
                _islog = 1;
                _nameField.text = @"";
                _passWordField.text = @"";
                            
        }else{
        }

    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
    }];

    
//    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
//    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
//
//    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
//
//    [manager POST:PORT(@"Account/GetMyInfo") parameters:nil progress:^(NSProgress * _Nonnull uploadProgress) {
//
//    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
//
//        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
//
//            NSDictionary *userDic = [responseObject objectForKey:@"Data"];
//            _userName = [ZuyuJsonRead jsonRead:userDic WithKey:@"ReaderNick"];
//            _userHeadImage = [ZuyuJsonRead jsonRead:userDic WithKey:@"HeadImgUrl"];
//            _userHeadImage = [_userHeadImage stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            _userHeadImage = [NSString stringWithFormat:@"%@%@",FILE,_userHeadImage];
//            _cardID = [ZuyuJsonRead jsonRead:userDic WithKey:@"CardNo"];
//            _phoneNum = [ZuyuJsonRead jsonRead:userDic WithKey:@"PhoneNumber"];
//
//            [[NSUserDefaults standardUserDefaults] setObject:_cardID forKey:@"cardID"];
//            [[NSUserDefaults standardUserDefaults] setObject:_phoneNum forKey:@"phoneNum"];
//            [[NSUserDefaults standardUserDefaults] setObject:_userName forKey:@"userName"];
//            [[NSUserDefaults standardUserDefaults] setObject:_userHeadImage forKey:@"userHeadImage"];
//            [[NSUserDefaults standardUserDefaults] synchronize];
//            [_tableView reloadData];
//
//        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
//            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
//            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
//            [[NSUserDefaults standardUserDefaults] synchronize];
//            _NOLoginView.hidden = NO;
//            _loginView.hidden = YES;
//            [_tableView reloadData];
//        }else{
//
//        }
//
//    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
//        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
//
//        if (responses.statusCode == 401) {
//
//            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
//
//                [self getMyMessage];
//
//            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
//
//            }];
//        }
//    }];
}

#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    _islog = [ISLOGIN integerValue];
    _NOLoginView.hidden = _islog;
    _loginView.hidden = !_islog;
    if (_islog) {
        [self getMyMessage];
    }
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
