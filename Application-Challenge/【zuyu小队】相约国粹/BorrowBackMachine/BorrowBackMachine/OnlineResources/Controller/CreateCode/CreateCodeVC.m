//
//  CreateCodeVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "CreateCodeVC.h"
#import "QRCodeImage.h"
#import "zuyu.h"
@interface CreateCodeVC ()<NavgationViewDelegate>
@property ( nonatomic, strong, nullable) UIImageView *qrImageView; //
@property ( nonatomic, strong) UIImageView *BGimage; //

@end

@implementation CreateCodeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createBackGroudView];
    [self requestData];
    // Do any additional setup after loading the view.
}

#pragma mark - createBGView
-(void)createBackGroudView
{
    _BGimage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    _BGimage.image = [UIImage imageNamed:@"createCodeBGI"];
    
    [self.view addSubview:_BGimage];
    
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 120, WIDTH, 30)];
    
    lable.font = [UIFont systemFontOfSize:20];
    
    lable.text = USERNAME;
    
    lable.textAlignment = NSTextAlignmentCenter;
    
    [_BGimage addSubview:lable];
    
}


-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = @"我的二维码";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - 生成二维码
-(void)createCode:(NSString *)message
{
    NSLog(@"%@",message);
    QRCodeImage *qrCodeImage = [QRCodeImage codeImageWithString:message size:190];
    UIImageView *qrImageView = [[UIImageView alloc]initWithImage:qrCodeImage];
    qrImageView.frame = CGRectMake(WIDTH/2 - 95, 150, 190, 190);
    [_BGimage addSubview:qrImageView];
    self.qrImageView = qrImageView;
}

#pragma mark - get Code Message
-(void)requestData
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    [manager.requestSerializer setValue:HeaderField forHTTPHeaderField:@"Authorization"];
    [manager POST:PORT(@"Account/CreateQRCode") parameters:nil progress:^(NSProgress * _Nonnull uploadProgress) {
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
        NSString *message = [ZuyuJsonRead jsonRead:responseObject WithKey:@"Data"];
            [self createCode:message];
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            [self.navigationController popViewControllerAnimated:YES];
        }

    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        if (responses.statusCode == 401) {
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                [self requestData];
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
            }];
        }
        
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
