//
//  EMConversationsViewController.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/1/8.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMConversationsViewController.h"
#import "EMChatViewController.h"
#import "EMRealtimeSearch.h"
#import "PellTableViewSelect.h"
#import "EMSearchResultController.h"
#import "EMInviteGroupMemberViewController.h"
#import "EMCreateGroupViewController.h"
#import "EMInviteFriendViewController.h"
#import "EMNotificationViewController.h"
#import "EMConversationUserDataModel.h"
#import "UserInfoStore.h"
#import "EMContactsViewController.h"

@interface EMConversationsViewController() <EaseConversationsViewControllerDelegate, EMSearchControllerDelegate>

@property (nonatomic, strong) UIButton *addImageBtn;
@property (nonatomic, strong) EMInviteGroupMemberViewController *inviteController;
@property (nonatomic, strong) EaseConversationsViewController *easeConvsVC;
@property (nonatomic, strong) EaseConversationViewModel *viewModel;
@property (nonatomic, strong) UINavigationController *resultNavigationController;
@property (nonatomic, strong) EMSearchResultController *resultController;
@end

@implementation EMConversationsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshTableView) name:CHAT_BACKOFF object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshTableView) name:GROUP_LIST_FETCHFINISHED object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshTableView) name:USERINFO_UPDATE object:nil];
    [self _setupSubviews];
    if (![EMDemoOptions sharedOptions].isFirstLaunch) {
        [EMDemoOptions sharedOptions].isFirstLaunch = YES;
        [[EMDemoOptions sharedOptions] archive];
        [self refreshTableViewWithData];
    }
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)pushToContactVC{
    EMContactsViewController * vc = [[EMContactsViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)_setupSubviews
{
    self.view.backgroundColor = [UIColor clearColor];
    UILabel *titleLabel = [[UILabel alloc] init];
    titleLabel.text = @"会话";
    titleLabel.textColor = [UIColor blackColor];
    titleLabel.font = [UIFont systemFontOfSize:18];
    [self.view addSubview:titleLabel];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.view).offset(EMVIEWTOPMARGIN + 35);
        make.height.equalTo(@25);
    }];
    
    self.addImageBtn = [[UIButton alloc]init];
    [self.addImageBtn setImage:[UIImage imageNamed:@"icon-add"] forState:UIControlStateNormal];
    [self.addImageBtn addTarget:self action:@selector(moreAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.addImageBtn];
    [self.addImageBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.equalTo(@35);
        make.centerY.equalTo(titleLabel);
        make.right.equalTo(self.view).offset(-16);
    }];
    
    UIButton * contactBtn = [[UIButton alloc]init];
    [contactBtn setImage:[UIImage imageNamed:@"contact"] forState:UIControlStateNormal];
    [contactBtn addTarget:self action:@selector(pushToContactVC) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:contactBtn];
    [contactBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.equalTo(@35);
        make.centerY.equalTo(titleLabel);
        make.left.equalTo(self.view).offset(40);
    }];
    
    self.viewModel = [[EaseConversationViewModel alloc] init];
    self.viewModel.canRefresh = YES;
    self.viewModel.badgeLabelPosition = EMAvatarTopRight;
    
    self.easeConvsVC = [[EaseConversationsViewController alloc] initWithModel:self.viewModel];
    self.easeConvsVC.delegate = self;
    [self addChildViewController:self.easeConvsVC];
    [self.view addSubview:self.easeConvsVC.view];
    [self.easeConvsVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(titleLabel.mas_bottom).offset(15);
        make.left.equalTo(self.view);
        make.right.equalTo(self.view);
        make.bottom.equalTo(self.view);
    }];
    [self _updateConversationViewTableHeader];
}

- (void)_updateConversationViewTableHeader {
    self.easeConvsVC.tableView.tableHeaderView = [[UIView alloc] initWithFrame:CGRectZero];
    self.easeConvsVC.tableView.tableHeaderView.backgroundColor = [UIColor colorWithRed:242.0/255.0 green:242.0/255.0 blue:242.0/255.0 alpha:1];
    UIControl *control = [[UIControl alloc] initWithFrame:CGRectZero];
    control.clipsToBounds = YES;
    control.layer.cornerRadius = 18;
    control.backgroundColor = UIColor.whiteColor;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(searchButtonAction)];
    [control addGestureRecognizer:tap];
    
    [self.easeConvsVC.tableView.tableHeaderView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.easeConvsVC.tableView);
        make.width.equalTo(self.easeConvsVC.tableView);
        make.top.equalTo(self.easeConvsVC.tableView);
        make.height.mas_equalTo(52);
    }];
    
    [self.easeConvsVC.tableView.tableHeaderView addSubview:control];
    [control mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_offset(36);
        make.top.equalTo(self.easeConvsVC.tableView.tableHeaderView).offset(8);
        make.bottom.equalTo(self.easeConvsVC.tableView.tableHeaderView).offset(-8);
        make.left.equalTo(self.easeConvsVC.tableView.tableHeaderView.mas_left).offset(17);
        make.right.equalTo(self.easeConvsVC.tableView.tableHeaderView).offset(-16);
    }];
    
    UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"search"]];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectZero];
    label.font = [UIFont systemFontOfSize:16];
    label.text = @"搜索";
    label.textColor = [UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:1];
    [label setContentCompressionResistancePriority:UILayoutPriorityRequired forAxis:UILayoutConstraintAxisHorizontal];
    UIView *subView = [[UIView alloc] init];
    [subView addSubview:imageView];
    [subView addSubview:label];
    [control addSubview:subView];
    
    [imageView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(15);
        make.left.equalTo(subView);
        make.top.equalTo(subView);
        make.bottom.equalTo(subView);
    }];
    
    [label mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(imageView.mas_right).offset(3);
        make.right.equalTo(subView);
        make.top.equalTo(subView);
        make.bottom.equalTo(subView);
    }];
    
    [subView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(control);
    }];
}

- (void)_setupSearchResultController
{
    __weak typeof(self) weakself = self;
    self.resultController.tableView.rowHeight = 70;
    self.resultController.tableView.rowHeight = UITableViewAutomaticDimension;
    [self.resultController setCellForRowAtIndexPathCompletion:^UITableViewCell *(UITableView *tableView, NSIndexPath *indexPath) {
        NSString *cellIdentifier = @"EaseConversationCell";
        EaseConversationCell *cell = (EaseConversationCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (cell == nil) {
            cell = [EaseConversationCell tableView:tableView cellViewModel:weakself.viewModel];
        }
        
        NSInteger row = indexPath.row;
        EaseConversationModel *model = [weakself.resultController.dataArray objectAtIndex:row];
        cell.model = model;
        return cell;
    }];
    [self.resultController setCanEditRowAtIndexPath:^BOOL(UITableView *tableView, NSIndexPath *indexPath) {
        return YES;
    }];
    [self.resultController setTrailingSwipeActionsConfigurationForRowAtIndexPath:^UISwipeActionsConfiguration *(UITableView *tableView, NSIndexPath *indexPath) {
        EaseConversationModel *model = [weakself.resultController.dataArray objectAtIndex:indexPath.row];
        UIContextualAction *deleteAction = [UIContextualAction contextualActionWithStyle:UIContextualActionStyleDestructive
                                                                                   title:@"删除会话"
                                                                                 handler:^(UIContextualAction * _Nonnull action, __kindof UIView * _Nonnull sourceView, void (^ _Nonnull completionHandler)(BOOL))
        {
            [weakself.resultController.tableView setEditing:NO];
            int unreadCount = [[EMClient sharedClient].chatManager getConversationWithConvId:model.easeId].unreadMessagesCount;
            [[EMClient sharedClient].chatManager deleteConversation:model.easeId isDeleteMessages:YES completion:^(NSString *aConversationId, EMError *aError) {
                if (!aError) {
                    [weakself.resultController.dataArray removeObjectAtIndex:indexPath.row];
                    [weakself.resultController.tableView reloadData];
                    if (unreadCount > 0 && weakself.deleteConversationCompletion) {
                        weakself.deleteConversationCompletion(YES);
                    }
                }
            }];
        }];
        UIContextualAction *topAction = [UIContextualAction contextualActionWithStyle:UIContextualActionStyleNormal
                                                                                title:!model.isTop ? @"置顶" : @"取消置顶"
                                                                              handler:^(UIContextualAction * _Nonnull action, __kindof UIView * _Nonnull sourceView, void (^ _Nonnull completionHandler)(BOOL))
        {
            [weakself.resultController.tableView setEditing:NO];
            [model setIsTop:!model.isTop];
            [weakself.easeConvsVC refreshTable];
        }];
        UISwipeActionsConfiguration *actions = [UISwipeActionsConfiguration configurationWithActions:@[deleteAction,topAction]];
        actions.performsFirstActionWithFullSwipe = NO;
        return actions;
    }];
    [self.resultController setDidSelectRowAtIndexPathCompletion:^(UITableView *tableView, NSIndexPath *indexPath) {
        NSInteger row = indexPath.row;
        EaseConversationModel *model = [weakself.resultController.dataArray objectAtIndex:row];
        weakself.resultController.searchBar.text = @"";
        [weakself.resultController.searchBar resignFirstResponder];
        weakself.resultController.searchBar.showsCancelButton = NO;
        [weakself searchBarCancelButtonAction:nil];
        [weakself.resultNavigationController dismissViewControllerAnimated:YES completion:nil];
        //系统通知
        if ([model.easeId isEqualToString:EMSYSTEMNOTIFICATIONID]) {
            EMNotificationViewController *controller = [[EMNotificationViewController alloc] initWithStyle:UITableViewStylePlain];
            [weakself.navigationController pushViewController:controller animated:YES];
            return;
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:CHAT_PUSHVIEWCONTROLLER object:model.easeId];
    }];
}

- (void)refreshTableView
{
    dispatch_async(dispatch_get_main_queue(), ^{
        if(self.view.window)
            [self.easeConvsVC refreshTable];
    });
}

- (void)refreshTableViewWithData
{
    __weak typeof(self) weakself = self;
    [[EMClient sharedClient].chatManager getConversationsFromServer:^(NSArray *aCoversations, EMError *aError) {
        if (!aError && [aCoversations count] > 0) {
            [weakself.easeConvsVC.dataAry removeAllObjects];
            [weakself.easeConvsVC.dataAry addObjectsFromArray:aCoversations];
            [weakself.easeConvsVC refreshTable];
        }
    }];
}

#pragma mark - searchButtonAction

- (void)searchButtonAction
{
    if (self.resultNavigationController == nil) {
        self.resultController = [[EMSearchResultController alloc] init];
        self.resultController.delegate = self;
        self.resultNavigationController = [[UINavigationController alloc] initWithRootViewController:self.resultController];
        [self.resultNavigationController.navigationBar setBackgroundImage:[[UIImage imageNamed:@"navBarBg"] stretchableImageWithLeftCapWidth:10 topCapHeight:10] forBarMetrics:UIBarMetricsDefault];
        [self _setupSearchResultController];
    }
    [self.resultController.searchBar becomeFirstResponder];
    self.resultController.searchBar.showsCancelButton = YES;
    self.resultNavigationController.modalPresentationStyle = 0;
    [self presentViewController:self.resultNavigationController animated:YES completion:nil];
}

#pragma mark - moreAction

- (void)moreAction
{
    [PellTableViewSelect addPellTableViewSelectWithWindowFrame:CGRectMake(self.view.bounds.size.width-160, self.addImageBtn.frame.origin.y, 145, 104) selectData:@[@"创建群组",@"添加好友"] images:@[@"icon-创建群组",@"icon-添加好友"] locationY:30 - (22 - EMVIEWTOPMARGIN) action:^(NSInteger index){
        if(index == 0) {
            [self createGroup];
        } else if (index == 1) {
            [self addFriend];
        }
    } animated:YES];
}

//创建群组
- (void)createGroup
{
    self.inviteController = nil;
    self.inviteController = [[EMInviteGroupMemberViewController alloc] init];
    __weak typeof(self) weakself = self;
    [self.inviteController setDoneCompletion:^(NSArray * _Nonnull aSelectedArray) {
        EMCreateGroupViewController *createController = [[EMCreateGroupViewController alloc] initWithSelectedMembers:aSelectedArray];
        createController.inviteController = weakself.inviteController;
        [createController setSuccessCompletion:^(EMGroup * _Nonnull aGroup) {
            [[NSNotificationCenter defaultCenter] postNotificationName:CHAT_PUSHVIEWCONTROLLER object:aGroup];
        }];
        [weakself.navigationController pushViewController:createController animated:YES];
    }];
    
    UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:self.inviteController];
    navController.modalPresentationStyle = 0;
    [self presentViewController:navController animated:YES completion:nil];
}

//添加好友
- (void)addFriend
{
    EMInviteFriendViewController *controller = [[EMInviteFriendViewController alloc] init];
    [self.navigationController pushViewController:controller animated:YES];
}

#pragma mark - EMSearchControllerDelegate

- (void)searchBarWillBeginEditing:(UISearchBar *)searchBar
{
    self.resultController.searchKeyword = nil;
}

- (void)searchBarCancelButtonAction:(UISearchBar *)searchBar
{
    [[EMRealtimeSearch shared] realtimeSearchStop];
    
    if ([self.resultController.dataArray count] > 0) {
        [self.resultController.dataArray removeAllObjects];
    }
    [self.resultController.tableView reloadData];
    [self.easeConvsVC refreshTabView];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [self.view endEditing:YES];
}

- (void)searchTextDidChangeWithString:(NSString *)aString
{
    self.resultController.searchKeyword = aString;
    
    __weak typeof(self) weakself = self;
    [[EMRealtimeSearch shared] realtimeSearchWithSource:self.easeConvsVC.dataAry searchText:aString collationStringSelector:@selector(showName) resultBlock:^(NSArray *results) {
         dispatch_async(dispatch_get_main_queue(), ^{
             if ([weakself.resultController.dataArray count] > 0) {
                 [weakself.resultController.dataArray removeAllObjects];
             }
            [weakself.resultController.dataArray addObjectsFromArray:results];
            [weakself.resultController.tableView reloadData];
        });
    }];
}
   
#pragma mark - EaseConversationsViewControllerDelegate

- (id<EaseUserDelegate>)easeUserDelegateAtConversationId:(NSString *)conversationId conversationType:(EMConversationType)type
{
    EMConversationUserDataModel *userData = [[EMConversationUserDataModel alloc]initWithEaseId:conversationId conversationType:type];
    if(type == EMConversationTypeChat) {
        if (![conversationId isEqualToString:EMSYSTEMNOTIFICATIONID]) {
            EMUserInfo* userInfo = [[UserInfoStore sharedInstance] getUserInfoById:conversationId];
            if(userInfo) {
                if([userInfo.nickName length] > 0) {
                    userData.showName = userInfo.nickName;
                }
                if([userInfo.avatarUrl length] > 0) {
                    userData.avatarURL = userInfo.avatarUrl;
                }
            }else{
                [[UserInfoStore sharedInstance] fetchUserInfosFromServer:@[conversationId]];
            }
        }
    }
    return userData;
}

- (NSArray<UIContextualAction *> *)easeTableView:(UITableView *)tableView trailingSwipeActionsForRowAtIndexPath:(NSIndexPath *)indexPath actions:(NSArray<UIContextualAction *> *)actions
{
    NSMutableArray<UIContextualAction *> *array = [[NSMutableArray<UIContextualAction *> alloc]init];
    __weak typeof(self) weakself = self;
    UIContextualAction *deleteAction = [UIContextualAction contextualActionWithStyle:UIContextualActionStyleNormal
                                                                               title:@"删除"
                                                                             handler:^(UIContextualAction * _Nonnull action, __kindof UIView * _Nonnull sourceView, void (^ _Nonnull completionHandler)(BOOL))
    {
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:@"确认删除？" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *clearAction = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [tableView setEditing:NO];
            [self _deleteConversation:indexPath];
        }];
        [clearAction setValue:[UIColor colorWithRed:245/255.0 green:52/255.0 blue:41/255.0 alpha:1.0] forKey:@"_titleTextColor"];
        [alertController addAction:clearAction];
        
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style: UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            [tableView setEditing:NO];
        }];
        [cancelAction  setValue:[UIColor blackColor] forKey:@"_titleTextColor"];
        [alertController addAction:cancelAction];
        alertController.modalPresentationStyle = 0;
        [weakself presentViewController:alertController animated:YES completion:nil];
    }];
    deleteAction.backgroundColor = [UIColor redColor];
    [array addObject:deleteAction];
    [array addObject:actions[1]];
    return [array copy];
}

- (void)easeTableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    EaseConversationCell *cell = (EaseConversationCell*)[tableView cellForRowAtIndexPath:indexPath];
    //系统通知
    if ([cell.model.easeId isEqualToString:EMSYSTEMNOTIFICATIONID]) {
        EMNotificationViewController *controller = [[EMNotificationViewController alloc] initWithStyle:UITableViewStylePlain];
        [self.navigationController pushViewController:controller animated:YES];
        return;
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:CHAT_PUSHVIEWCONTROLLER object:cell.model];
}


#pragma mark - Action

//删除会话
- (void)_deleteConversation:(NSIndexPath *)indexPath
{
    __weak typeof(self) weakSelf = self;
    NSInteger row = indexPath.row;
    EaseConversationModel *model = [self.easeConvsVC.dataAry objectAtIndex:row];
    int unreadCount = [[EMClient sharedClient].chatManager getConversationWithConvId:model.easeId].unreadMessagesCount;
    [[EMClient sharedClient].chatManager deleteConversation:model.easeId
                                           isDeleteMessages:YES
                                                 completion:^(NSString *aConversationId, EMError *aError) {
        if (!aError) {
            [weakSelf.easeConvsVC.dataAry removeObjectAtIndex:row];
            [weakSelf.easeConvsVC refreshTabView];
            if (unreadCount > 0 && weakSelf.deleteConversationCompletion) {
                weakSelf.deleteConversationCompletion(YES);
            }
        }
    }];
}

@end
