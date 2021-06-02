//
//  MySettingVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/28.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MySettingVC.h"
#import "zuyu.h"
#import "MySettingCell.h"
#import "OpinionBackVC.h"
#import "AboutUsViewController.h"
#import "RevisePasswordVC.h"
#import "TiaoKuanVC.h"

@interface MySettingVC ()<NavgationViewDelegate,UITableViewDelegate,UITableViewDataSource,UIAlertViewDelegate>
{
    UITableView *_tableView;
    NSMutableArray *_dataArray;
}
@end

@implementation MySettingVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createTableView];
    [self createLogOutButton];
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
    view.titleStr = @"设置";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);

}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - 退出登陆
-(void)createLogOutButton
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(0, 370 + 74 , WIDTH, 60);
    
    [button setTitle:@"退出登录" forState: UIControlStateNormal];
    
    [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    button.backgroundColor = [UIColor whiteColor];
    
    [button addTarget:self action:@selector(logoutClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
}

-(void)logoutClick:(UIButton *)button
{
    
    [[EMClient sharedClient] logout:YES completion:^(EMError *aError) {
        if (!aError) {
            NSLog(@"退出登录成功");
        } else {
            NSLog(@"退出登录失败的原因---%@", aError.errorDescription);
        }
    }];
    [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    [self.navigationController popViewControllerAnimated:YES];
    
}


#pragma mark - TableView

-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH,369) style:UITableViewStylePlain];
    _tableView.delegate = self;
    _tableView.dataSource = self;
    
    [self.view addSubview:_tableView];
    
    _dataArray = [NSMutableArray arrayWithObjects:@"账号管理",
                  @"清理缓存",
                  @"",
                  @"意见反馈",
                  @"关于",
                  @"隐私政策",
                  @"",nil];
    
}




-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 2 || indexPath.row == 6) {
        return 10;
    }return 70;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (indexPath.row == 2 || indexPath.row == 6) {
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        cell.backgroundColor = RGBA(246, 245, 242, 1);
        return cell;
    }
    
    MySettingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MySettingCell"];
    
    if (cell == nil) {
        cell = [[MySettingCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MySettingCell"];
    }
    
    cell.text = _dataArray[indexPath.row];
    
    return cell;
    
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.row == 0) {
        //账号管理
        RevisePasswordVC *vc = [[RevisePasswordVC alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController: vc animated:YES];
        
    }else if (indexPath.row == 1){
        UIAlertView *WXinstall=[[UIAlertView alloc]initWithTitle:@"清除缓存" message:@"是否确定清理所有缓存?" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];//一般在if判断中加入
        
        [WXinstall show];
    }else if (indexPath.row == 3){
        
        OpinionBackVC *vc = [[OpinionBackVC alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController: vc animated:YES];
        
    }else if (indexPath.row == 4){
        //关于
        AboutUsViewController *vc = [[AboutUsViewController alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
    }else if (indexPath.row == 5){
        
        TiaoKuanVC *vc = [[TiaoKuanVC alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}


//监听点击事件 代理方法
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSString *btnTitle = [alertView buttonTitleAtIndex:buttonIndex];
    if ([btnTitle isEqualToString:@"取消"]) {
        NSLog(@"你点击了取消");
    }else if ([btnTitle isEqualToString:@"确定"] ) {
        
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
        
        // Set the annular determinate mode to show task progress.
        hud.mode = MBProgressHUDModeAnnularDeterminate;
        hud.label.text = NSLocalizedString(@"清理中...", @"HUD loading title");
        
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0), ^{
            // Do something useful in the background and update the HUD periodically.
            [self doSomeWorkWithProgress];
            dispatch_async(dispatch_get_main_queue(), ^{
                [hud hideAnimated:YES];
            });
        });
    }
}


- (void)doSomeWorkWithProgress {
    float progress = 0.0f;
    while (progress < 1.0f) {
        progress += 0.01f;
        dispatch_async(dispatch_get_main_queue(), ^{
            // Instead we could have also passed a reference to the HUD
            // to the HUD to myProgressTask as a method parameter.
            [MBProgressHUD HUDForView:self.navigationController.view].progress = progress;
        });
        usleep(33000);
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
