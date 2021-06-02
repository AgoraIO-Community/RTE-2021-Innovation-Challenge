//
//  EMAccountViewController.m
//  EaseIM
//
//  Update by zhangchong on 2020/6/30.
//  Copyright © 2018 XieYajie. All rights reserved.
//

#import "EMAccountViewController.h"

#import "EMDemoOptions.h"
#import "UserInfoStore.h"
#import "SelectAvatarViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface EMAccountViewController ()

@property (nonatomic, strong) UIImageView *headerView;
@property (nonatomic) EMUserInfo* userInfo;

@end

@implementation EMAccountViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    [self _setupSubviews];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userInfoUpdated) name:USERINFO_UPDATE object:nil];
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
}

#pragma mark - Subviews

- (void)_setupSubviews
{
    [self addPopBackLeftItem];
    self.title = @"个人资料";

    self.tableView.tableFooterView = [[UIView alloc] init];
    self.tableView.backgroundColor = kColor_LightGray;
    self.tableView.estimatedSectionHeaderHeight = 0;
    self.tableView.estimatedSectionFooterHeight = 0;
    
    self.headerView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"defaultAvatar"]];
    self.headerView.frame = CGRectMake(0, 0, 36, 36);
    self.headerView.userInteractionEnabled = YES;
    [self userInfoUpdated];
    [[UserInfoStore sharedInstance] fetchUserInfosFromServer:@[[EMClient sharedClient].currentUsername]];
}

-(void)userInfoUpdated
{
    dispatch_async(dispatch_get_main_queue(), ^{
        {
            self.userInfo = [[UserInfoStore sharedInstance] getUserInfoById:[EMClient sharedClient].currentUsername];
            if(!self.userInfo) {
                [[[EMClient sharedClient] userInfoManager] fetchUserInfoById:@[[EMClient sharedClient].currentUsername] completion:^(NSDictionary *aUserDatas, EMError *aError) {
                    if(!aError) {
                        self.userInfo = [aUserDatas objectForKey:[EMClient sharedClient].currentUsername];
                        if(self.userInfo && self.userInfo.avatarUrl) {
                            NSURL* url = [NSURL URLWithString:self.userInfo.avatarUrl];
                            [self.headerView sd_setImageWithURL:url completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                                                    
                            }];
                        }
                    }
                    
                }];
            }else{
                if(self.userInfo.avatarUrl.length > 0) {
                    NSURL* url = [NSURL URLWithString:self.userInfo.avatarUrl];
                    if(url) {
                        [self.headerView sd_setImageWithURL:url completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                                                
                        }];
                    }
                }
                
            }
        }
        if(self.view.window)
            [self.tableView reloadData];
    });
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//    NSInteger count = 0;
//    switch (section) {
//        case 0:
//            count = 2;
//            break;
//        case 1:
//        {
//            count = 1;
//            break;
//        }
//        default:
//            break;
//    }
//
//    return count;
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"UITableViewCell"];
    
    // Configure the cell...
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"UITableViewCell"];
        cell.detailTextLabel.textColor = [UIColor grayColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    cell.textLabel.textColor = [UIColor blackColor];
    cell.detailTextLabel.text = @"";
    cell.accessoryView = nil;
    cell.accessoryType = UITableViewCellAccessoryNone;
    if (section == 0) {
        if (row == 0) {
            cell.textLabel.text = @"头像";
            cell.accessoryView = self.headerView;
        } else if (row == 1) {
//            cell.textLabel.text = @"环信ID";
//            cell.detailTextLabel.text = [EMClient sharedClient].currentUsername;
            cell.textLabel.text = @"昵称";
            if(self.userInfo)
                cell.detailTextLabel.text = self.userInfo.nickName;
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
    }
//    else if (section == 1) {
//        cell.textLabel.text = @"昵称";
//        cell.detailTextLabel.text = [EMClient sharedClient].pushManager.pushOptions.displayName;
//        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
//    }
    
    return cell;
}

#pragma mark - Table view delegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 66;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 44;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
//    if (section == 1) {
//        UILabel *label = [[UILabel alloc] init];
//        label.font = [UIFont systemFontOfSize:13];
//        label.textColor = [UIColor lightGrayColor];
//        label.text = @"     iOS APNS推送时使用的显示昵称";
//        return label;
//    }
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.section == 0) {
        if(indexPath.row == 0) {
            [self changeAvatar];
        }else
        if (indexPath.row == 1) {
            [self changeNikeNameAction];
        }
    }
    
}

#pragma mark - Action

- (void)_updateNikeName:(NSString *)aName
{
    //设置推送设置
    [self showHint:@"修改昵称..."];
    __weak typeof(self) weakself = self;
    [[EMClient sharedClient].pushManager updatePushDisplayName:aName completion:^(NSString * _Nonnull aDisplayName, EMError * _Nonnull aError) {
        if (!aError) {
            if (weakself.updateAPNSNicknameCompletion) {
                weakself.updateAPNSNicknameCompletion();
            }
            [weakself.tableView reloadData];
            [weakself hideHud];
        } else {
            [EMAlertController showErrorAlert:aError.errorDescription];
        }
    }];
    [[[EMClient sharedClient] userInfoManager] updateOwnUserInfo:aName withType:EMUserInfoTypeNickName completion:^(EMUserInfo* aUserInfo,EMError *aError) {
            if(!aError) {
                [[UserInfoStore sharedInstance] setUserInfo:aUserInfo forId:[EMClient sharedClient].currentUsername];
                [[NSNotificationCenter defaultCenter] postNotificationName:USERINFO_UPDATE  object:nil];
            }else{
                dispatch_sync(dispatch_get_main_queue(), ^{
                    [weakself showHint:[NSString stringWithFormat:@"修改昵称失败：%@",aError.errorDescription]];
                });
            }
            }];
}

- (void)changeNikeNameAction
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"更改昵称" message:nil preferredStyle:UIAlertControllerStyleAlert];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
        textField.placeholder = @"请输入昵称";
        if(self.userInfo)
            textField.text = self.userInfo.nickName;
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:cancelAction];
    
    __weak typeof(self) weakself = self;
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        UITextField *textField = alertController.textFields.firstObject;
        [weakself _updateNikeName:textField.text];
    }];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)changeAvatar
{
    SelectAvatarViewController* selectAvatarVC = [[SelectAvatarViewController alloc] initWithCurrentAvatar:self.userInfo.avatarUrl];
    [self.navigationController pushViewController:selectAvatarVC animated:YES];
}

@end
