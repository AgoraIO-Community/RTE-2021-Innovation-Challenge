//
//  EMMineViewController.m
//  EaseIM
//
//  Updated by zhangchong on 2020/6/10.
//  Copyright © 2018 XieYajie. All rights reserved.
//

#import "EMMineViewController.h"

#import "EMAvatarNameCell+UserInfo.h"

#import "EMAccountViewController.h"
#import "EMSecurityViewController.h"
#import "EMSettingsViewController.h"
#import "EMAboutHuanXinViewController.h"
#import "EMDeveloperServiceViewController.h"
#import "EMOpinionFeedbackViewController.h"
#import "UserInfoStore.h"

@interface EMMineViewController ()

@property (nonatomic, strong) EMAvatarNameCell *userCell;

@property (nonatomic, strong) UIButton *suspendCardBtn;

@property (nonatomic, strong) UIImageView *avatarView;

@property (nonatomic, strong) UILabel *nameLabel;

@property (nonatomic, strong) UILabel *detailLabel;

@property (nonatomic, strong) UIWindow *window;

@property (nonatomic, strong) UIView *backView;

@property (nonatomic, strong) UILabel *funLabel;

@end

@implementation EMMineViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userInfoUpdated) name:USERINFO_UPDATE object:nil];
    self.showRefreshHeader = NO;
    // Do any additional setup after loading the view.
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
    [self _setupSubviews];
    
}


- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.window = nil;
    self.backView = nil;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

#pragma mark - Subviews

- (void)_setupSubviews
{
    self.view.backgroundColor = [UIColor whiteColor];
    
    UILabel *titleLabel = [[UILabel alloc] init];
    titleLabel.text = @"我";
    titleLabel.textColor = [UIColor blackColor];
    titleLabel.font = [UIFont systemFontOfSize:18];
    [self.view addSubview:titleLabel];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.view).offset(EMVIEWTOPMARGIN + 35);
        make.height.equalTo(@25);
    }];
    
    self.userCell = [[EMAvatarNameCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"EMAvatarNameCell"];
    self.userCell.selectionStyle = UITableViewCellSelectionStyleNone;
    self.userCell.accessoryType = UITableViewCellAccessoryNone;
    self.userCell.nameLabel.font = [UIFont systemFontOfSize:18];
    self.userCell.detailLabel.font = [UIFont systemFontOfSize:15];
    self.userCell.detailLabel.textColor = [UIColor grayColor];
    self.userCell.avatarView.image = [UIImage imageNamed:@"defaultAvatar"];
    self.userCell.nameLabel.text = [EMClient sharedClient].currentUsername;
    //self.userCell.detailLabel.text = [EMClient sharedClient].pushManager.pushOptions.displayName;
    [self userInfoUpdated];
    [self.userCell.avatarView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.userCell.contentView.mas_left).offset(28);
        make.centerY.equalTo(self.userCell.contentView);
        make.width.height.equalTo(@50);
    }];
    
    self.tableView.backgroundColor = kColor_LightGray;
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.scrollEnabled = NO;
    self.tableView.tableFooterView = [[UIView alloc] init];

    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(titleLabel.mas_bottom).offset(15);
        make.left.equalTo(self.view);
        make.right.equalTo(self.view);
        make.bottom.equalTo(self.view);
    }];
    
    //延时加载window,注意我们需要在rootWindow创建完成之后再创建这个悬浮的视图
    //[self performSelector:@selector(floatCard) withObject:nil afterDelay:0.1];
    
    EMUserInfo* userInfo = [[UserInfoStore sharedInstance] getUserInfoById:[EMClient sharedClient].currentUsername];
    if(!userInfo && [EMClient sharedClient].currentUsername.length > 0) {
        [[UserInfoStore sharedInstance] fetchUserInfosFromServer:@[[EMClient sharedClient].currentUsername]];
    }else{
        [self userInfoUpdated];
    }
}

-(void)userInfoUpdated
{
    __weak typeof(self) weakself = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        {
            if(weakself.view.window) {
                [weakself.userCell refreshUserInfo:[EMClient sharedClient].currentUsername];
                [weakself.tableView reloadData];
            }
        }
    });
}

//漂浮名片
- (void)floatCard
{
    CAGradientLayer *gl = [CAGradientLayer layer];
    gl.frame = CGRectMake(0,0,[UIScreen mainScreen].bounds.size.width,70);
    gl.startPoint = CGPointMake(0.76, 0.84);
    gl.endPoint = CGPointMake(0.26, 0.14);
    gl.colors = @[(__bridge id)[UIColor colorWithRed:90/255.0 green:93/255.0 blue:208/255.0 alpha:1.0].CGColor, (__bridge id)[UIColor colorWithRed:4/255.0 green:174/255.0 blue:240/255.0 alpha:1.0].CGColor];
    gl.locations = @[@(0), @(1.0f)];
    self.backView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 70)];
    [self.backView.layer addSublayer:gl];
    
    UILabel *tabLabel = [[UILabel alloc]init];
    tabLabel.text = @"我";
    tabLabel.font = [UIFont systemFontOfSize:18.0];
    tabLabel.textAlignment = NSTextAlignmentCenter;
    tabLabel.textColor = [UIColor whiteColor];
    [self.backView addSubview:tabLabel];
    [tabLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.backView.mas_bottom).offset(-10);
        make.centerX.equalTo(self.backView);
        make.width.equalTo(@40);
        make.height.equalTo(@20);
    }];
    /*
    //悬浮按钮
    self.suspendCardBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [_suspendCardBtn setBackgroundColor:[UIColor whiteColor]];
    _suspendCardBtn.layer.cornerRadius = 6;
    _suspendCardBtn.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.5].CGColor;
    _suspendCardBtn.layer.shadowOffset = CGSizeMake(0,2);
    _suspendCardBtn.layer.shadowOpacity = 1;
    _suspendCardBtn.layer.shadowRadius = 5;
    [_suspendCardBtn addTarget:self action:@selector(suspendBtnClick) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:_suspendCardBtn];
    [_suspendCardBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.equalTo(@71);
        make.top.equalTo(self.backView).offset(38);
        make.left.equalTo(self.backView).offset(16);
        make.right.equalTo(self.backView).offset(-16);
    }];
    [self _setupCardView];*/
    
    //悬浮按钮所处的顶端UIWindow
    self.window = [[UIWindow alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 70)];
    //使得新建window在最顶端
    _window.windowLevel = UIWindowLevelAlert + 1;
    _window.backgroundColor = [UIColor clearColor];
    [_window addSubview:_backView];
    //显示window
    [_window makeKeyAndVisible];

}
/*
//名片
- (void)_setupCardView
{
    _avatarView = [[UIImageView alloc] init];
    [self.suspendCardBtn addSubview:_avatarView];
    [_avatarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.suspendCardBtn).offset(8);
        make.left.equalTo(self.suspendCardBtn).offset(15);
        make.bottom.equalTo(self.suspendCardBtn).offset(-8);
        make.width.equalTo(self.avatarView.mas_height).multipliedBy(1);
    }];
       
    _detailLabel = [[UILabel alloc] init];
    _detailLabel.backgroundColor = [UIColor clearColor];
    _detailLabel.font = [UIFont systemFontOfSize:15];
    _detailLabel.textColor = [UIColor grayColor];
    [self.suspendCardBtn addSubview:_detailLabel];
    [_detailLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.avatarView.mas_right).offset(8);
        make.right.equalTo(self.suspendCardBtn).offset(-15);
        make.bottom.equalTo(self.suspendCardBtn).offset(-8);
    }];
       
    _nameLabel = [[UILabel alloc] init];
    _nameLabel.numberOfLines = 2;
    _nameLabel.backgroundColor = [UIColor clearColor];
    _nameLabel.textColor = [UIColor blackColor];
    _nameLabel.font = [UIFont systemFontOfSize:18];
    [self.suspendCardBtn addSubview:_nameLabel];
    [_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.suspendCardBtn).offset(8);
        make.left.equalTo(self.avatarView.mas_right).offset(8);
        make.right.equalTo(self.suspendCardBtn).offset(-15);
        make.bottom.equalTo(self.detailLabel.mas_top);
    }];
    
    self.nameLabel.font = [UIFont systemFontOfSize:18];
    self.detailLabel.font = [UIFont systemFontOfSize:15];
    self.detailLabel.textColor = [UIColor grayColor];
    self.avatarView.image = [UIImage imageNamed:@"defaultAvatar"];
    self.nameLabel.text = [EMClient sharedClient].currentUsername;
    self.detailLabel.text = [EMClient sharedClient].pushOptions.displayName;
}

- (void)suspendBtnClick
{
    EMAccountViewController *controller = [[EMAccountViewController alloc] initWithStyle:UITableViewStyleGrouped];
    [self.navigationController pushViewController:controller animated:YES];
}*/

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSInteger count = 0;
    switch (section) {
        case 0:
            count = 1;
            break;
        case 1:
            count = 1;
            break;
        case 2:
            count = 1;
            break;
        default:
            break;
    }
    
    return count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"UITableViewCell"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
//    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    self.funLabel = [[UILabel alloc]init];
    self.funLabel.userInteractionEnabled = NO;
    [cell.contentView addSubview:self.funLabel];
    [self.funLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(cell.contentView.mas_centerX);
        make.centerY.equalTo(cell.contentView);
       }];
    
    if (section == 0) {
        if (row == 0) {
            return self.userCell;
        }
    }
    if (section == 1) {
        if (row == 0) {
            cell.textLabel.text = @"注册时间";
            cell.detailTextLabel.text = @"2021.05.01";
        }
    }else{
        if (row == 0) {
            self.funLabel.text = @"退出登录";
        }
    }
    self.funLabel.textColor = [UIColor redColor];
    self.funLabel.font = [UIFont systemFontOfSize:19.0];
    self.funLabel.textAlignment = NSTextAlignmentLeft;
    cell.separatorInset = UIEdgeInsetsMake(0, 16, 0, 16);
    return cell;
}

#pragma mark - Table view delegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 0) return 70;
    return 66;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 0) return 0;
    return 16;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    __weak typeof(self) weakself = self;
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    if (section == 0) {
        if (row == 0) {
            EMAccountViewController *controller = [[EMAccountViewController alloc] initWithStyle:UITableViewStyleGrouped];
            [controller setUpdateAPNSNicknameCompletion:^{
                weakself.userCell.detailLabel.text = [EMClient sharedClient].pushManager.pushOptions.displayName;
                [weakself.tableView reloadData];
            }];
            [self.navigationController pushViewController:controller animated:YES];
        }
    } else if (section == 1) {
        
//        if (row == 0) {
//            EMSettingsViewController *settingsController = [[EMSettingsViewController alloc]init];
//            [self.navigationController pushViewController:settingsController animated:YES];
//        } else if (row == 3) {
//            EMOpinionFeedbackViewController *opinionController = [[EMOpinionFeedbackViewController alloc]init];
//            [self.navigationController pushViewController:opinionController animated:YES];
//        } else if (row == 1) {
//            EMAboutHuanXinViewController *aboutHuanXin = [[EMAboutHuanXinViewController alloc]init];
//            [self.navigationController pushViewController:aboutHuanXin animated:YES];
//        } else if (row == 2) {
//            EMDeveloperServiceViewController *developerServiceController = [[EMDeveloperServiceViewController alloc]init];
//            [self.navigationController pushViewController:developerServiceController animated:YES];
//        }
    }else{
        [self logoutAction];
    }
}

#pragma mark - scrollview delegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == self.tableView) {
        NSIndexPath * indexPath = [NSIndexPath indexPathForRow:1 inSection:0];
        CGRect rectInTableView = [self.tableView rectForRowAtIndexPath:indexPath];
        CGRect rectInSuperview = [self.tableView convertRect:rectInTableView toView:[self.tableView superview]];
        if (rectInSuperview.origin.y < 70) {
            self.backView.alpha = 0.7;
        } else {
            self.backView.alpha = 1.0;
        }
    }
}

- (void)logoutAction
{
    __weak typeof(self) weakself = self;
    [self showHudInView:self.view hint:@"退出..."];
    [[EMClient sharedClient] logout:YES completion:^(EMError *aError) {
        [weakself hideHud];
        if (aError) {
            [EMAlertController showErrorAlert:aError.errorDescription];
        } else {
            EMDemoOptions *options = [EMDemoOptions sharedOptions];
            options.isAutoLogin = NO;
            options.loggedInUsername = @"";
            [options archive];
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"loginState"];
            [[NSNotificationCenter defaultCenter] postNotificationName:ACCOUNT_LOGIN_CHANGED object:@NO];
        }
    }];
}

@end
