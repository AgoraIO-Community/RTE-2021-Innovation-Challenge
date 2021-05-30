//
//  QHTableRootViewController.m
//  QHTableDemo
//
//  Created by chen on 17/3/13.
//  Copyright © 2017年 chen. All rights reserved.
//

#import "QHLoginViewController.h"

#import "QHAppContext.h"

#import <HyphenateChat/HyphenateChat.h>

#import "QHGameRoomViewController.h"
#import "MBProgressHUD+Add.h"

@interface QHLoginViewController ()

@property (weak, nonatomic) IBOutlet UITextField *userNameTV;
@property (weak, nonatomic) IBOutlet UITextField *passwordTV;
@property (weak, nonatomic) IBOutlet UIButton *loginBtn;
@property (weak, nonatomic) IBOutlet UISegmentedControl *hostSeg;

@end

@implementation QHLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.title = @"连麦问答PK";
    [self p_setting];
}

#pragma mark - Private

- (void)p_goRoom {
    [QHAppContext context].isHost = (self.hostSeg.selectedSegmentIndex == 1);
    if (![[QHAppContext context] hasSetting]) {
        [MBProgressHUD showError:@"请确认配置填写后重启App即可！" toView:nil];
        return;
    }
    self.userNameTV.enabled = NO;
    self.passwordTV.enabled = NO;
    if (self.userNameTV.text.length == 0 || self.passwordTV.text.length == 0) {
        self.userNameTV.enabled = YES;
        self.passwordTV.enabled = YES;
        [MBProgressHUD showError:@"请输入登录的环信账号和密码！" toView:nil];
        return;
    }
    self.loginBtn.enabled = NO;
    [self.loginBtn setTitle:@"登录ing" forState:UIControlStateNormal];
    NSString *name = self.userNameTV.text;
    NSString *password = self.passwordTV.text;
    [[EMClient sharedClient] loginWithUsername:name password:password completion:^(NSString *aUsername, EMError *aError) {
        
        self.userNameTV.enabled = YES;
        self.passwordTV.enabled = YES;
        [self.loginBtn setTitle:@"登录" forState:UIControlStateNormal];
        
        if (!aError) {
            [QHAppContext context].curUser = name;
            QHGameRoomViewController *vc = [[QHGameRoomViewController alloc] init];
            [vc.navigationItem setTitle:name];
            [self.navigationController pushViewController:vc animated:YES];
        }
        else {
            [MBProgressHUD showError:aError.errorDescription toView:nil];
        }
        self.loginBtn.enabled = YES;
    }];
}

- (void)p_setting {
    [[QHAppContext context] readSetting];
    if (![[QHAppContext context] hasSetting]) {
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"提醒" message:@"需自行配置SDK信息，可联系我获取，再填写正确信息后重启App才生效！暂未开发后台支持，如有不便请谅解。" preferredStyle:UIAlertControllerStyleAlert];
        [alertController addAction: [UIAlertAction actionWithTitle:@"马上填写" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self goSettingAction:nil];
        }]];
        [self.navigationController presentViewController:alertController animated:YES completion:nil];
    }
    else {
        [[QHAppContext context] doSetting];
    }
}

#pragma mark - Action

- (IBAction)loginAction:(UIButton *)sender {
    [_userNameTV resignFirstResponder];
    [_passwordTV resignFirstResponder];
    [self p_goRoom];
}

- (IBAction)goSettingAction:(id)sender {
    NSURL * url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
    if ([[UIApplication sharedApplication] canOpenURL:url]) {
        [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
        }];
    }
}

@end
