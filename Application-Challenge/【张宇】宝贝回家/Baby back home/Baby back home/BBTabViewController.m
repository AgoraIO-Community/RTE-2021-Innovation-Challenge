//
//  BBTabViewController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/12.
//

#import "BBTabViewController.h"
#import "JJSNavViewController.h"
#import "HomeViewController.h"
#import "FindViewController.h"
#import "MineViewController.h"
#import "ReleaseViewController.h"
#import "EMContactsViewController.h"
#import "EMNotificationViewController.h"
#import "BBTabBar.h"
#import "EMMineViewController.h"
#import "EMConversationsViewController.h"
#import "EMRemindManager.h"
#import "BBBlockViewController.h"

#define BlueGreenColor [UIColor colorWithRed:19 / 255.0 green:62 / 255.0 blue:81 / 255.0 alpha:1.0]

@interface BBTabViewController ()<UITabBarControllerDelegate,EMChatManagerDelegate, EaseIMKitManagerDelegate>
@property (nonatomic) BOOL isViewAppear;
@property (nonatomic,strong)BBTabBar    *tabBar;
@property (nonatomic, strong) EMConversationsViewController *conversationsController;


@end

@implementation BBTabViewController
@dynamic tabBar;
//设置单例
+(instancetype)sharedMainTabBarController{
    static BBTabViewController *tabBarController = nil;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        tabBarController = [[self alloc]init];
    });
    return tabBarController;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.delegate = self;
    self.tabBar = [[BBTabBar alloc] init];
    [self.tabBar.centerBtn addTarget:self action:@selector(buttonAction) forControlEvents:UIControlEventTouchDown];
    //设置背景颜色不透明
    self.tabBar.translucent = NO;
    //利用KVC,将自定义tabBar,赋给系统tabBar
    [self setValue:self.tabBar forKeyPath:@"tabBar"];
    
    [self initVC];
    
    //监听消息接收，主要更新会话tabbaritem的badge
    [[EMClient sharedClient].chatManager addDelegate:self delegateQueue:nil];
    [EaseIMKitManager.shared addDelegate:self];
    
}

- (void)buttonAction{
    //关联中间按钮
    [self presentViewController:[[ReleaseViewController alloc] init] animated:YES completion:nil];
    self.selectedIndex = 0;
    //播放动画
}


//tabbar选择时的代理
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController{
    if (tabBarController.selectedIndex == 2){
        //选中中间的按钮
        [self presentViewController:[[ReleaseViewController alloc] init] animated:YES completion:nil];
        self.selectedIndex = 0;
    }else {
        [self.tabBar.centerBtn.layer removeAllAnimations];
    }
}

-(void)initVC{
    
    HomeViewController *fish = [[HomeViewController alloc] init];
    fish.tabBarItem.title = @"首页";
    fish.tabBarItem.image = [UIImage imageNamed:@"首页-0"];
    fish.tabBarItem.selectedImage = [[UIImage imageNamed:@"首页-0"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)];
    NSDictionary *dictHome = [NSDictionary dictionaryWithObject:[UIColor colorWithRed:55/255.0 green:218/255.0 blue:156/255.0 alpha:1] forKey:NSForegroundColorAttributeName];
    [fish.tabBarItem setTitleTextAttributes:dictHome forState:(UIControlStateSelected)];
   
    FindViewController *pond = [[FindViewController alloc] init];
    pond.tabBarItem.title = @"寻找";
    pond.tabBarItem.image = [UIImage imageNamed:@"寻找-0"];
    pond.tabBarItem.selectedImage = [[UIImage imageNamed:@"寻找-0"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)];

    [pond.tabBarItem setTitleTextAttributes:dictHome forState:(UIControlStateSelected)];
//    pond.tabBarItem.selectedImage = [UIImage imageNamed:@"理财-1"];
    //不设置图片,先占位
    BBBlockViewController *release = [[BBBlockViewController alloc] init];
    
    EMConversationsViewController * messageVC = [[EMConversationsViewController alloc] init];
    messageVC.tabBarItem.title = @"消息";
    messageVC.tabBarItem.image = [UIImage imageNamed:@"消息-0"];
    [messageVC.tabBarItem setTitleTextAttributes:dictHome forState:(UIControlStateSelected)];
    messageVC.tabBarItem.selectedImage = [[UIImage imageNamed:@"消息-0"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)];
    self.conversationsController = messageVC;

    EMMineViewController *manager = [[EMMineViewController alloc] init];
    manager.tabBarItem.title = @"我的";
    manager.tabBarItem.image = [UIImage imageNamed:@"我的-0"];
    [manager.tabBarItem setTitleTextAttributes:dictHome forState:(UIControlStateSelected)];
    manager.tabBarItem.selectedImage = [[UIImage imageNamed:@"我的-0"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)];

    NSArray *itemArrays   = @[fish,pond,release,messageVC,manager];
    self.viewControllers  = itemArrays;
    self.tabBar.tintColor = [UIColor blackColor];
    

    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(jumpToFinanceVC) name:@"jumpToFinanceVC" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(jumpToPersonVC) name:@"jumpToPersonVC" object:nil];
    
    
    // Do any additional setup after loading the view.
    
}

#pragma mark - EMChatManagerDelegate

- (void)messagesDidReceive:(NSArray *)aMessages
{
    for (EMMessage *msg in aMessages) {
        if (msg.body.type == EMMessageBodyTypeText && [((EMTextMessageBody *)msg.body).text isEqualToString:EMCOMMUNICATE_CALLINVITE]) {
            //通话邀请
            EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:msg.conversationId type:EMConversationTypeGroupChat createIfNotExist:YES];
            if ([((EMTextMessageBody *)msg.body).text isEqualToString:EMCOMMUNICATE_CALLINVITE]) {
                [conversation deleteMessageWithId:msg.messageId error:nil];
                continue;
            }
        }
    }
    if (self.isViewAppear) {
        [self _loadConversationTabBarItemBadge];
    }
}

//　收到已读回执
- (void)messagesDidRead:(NSArray *)aMessages
{
    [self _loadConversationTabBarItemBadge];
}

- (void)conversationListDidUpdate:(NSArray *)aConversationList
{
    [self _loadConversationTabBarItemBadge];
}

- (void)onConversationRead:(NSString *)from to:(NSString *)to
{
    [self _loadConversationTabBarItemBadge];
}

#pragma mark - EaseIMKitManagerDelegate

- (void)conversationsUnreadCountUpdate:(NSInteger)unreadCount
{
    __weak typeof(self) weakself = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        weakself.conversationsController.tabBarItem.badgeValue = unreadCount > 0 ? @(unreadCount).stringValue : nil;
    });
    [EMRemindManager updateApplicationIconBadgeNumber:unreadCount];
}

#pragma mark - Private

- (void)_loadConversationTabBarItemBadge
{
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSInteger unreadCount = 0;
    for (EMConversation *conversation in conversations) {
        unreadCount += conversation.unreadMessagesCount;
    }
    self.conversationsController.tabBarItem.badgeValue = unreadCount > 0 ? @(unreadCount).stringValue : nil;
    [EMRemindManager updateApplicationIconBadgeNumber:unreadCount];
}



- (void)jumpToHomeVC{
    self.selectedIndex = 0;
}
- (void)jumpToFinanceVC{
    self.selectedIndex = 1;
}
- (void)jumpToPersonVC{
    self.selectedIndex = 3;
}
-(void)addTabChildView:(UIViewController *)vc andImage:(NSString *)image andHighLightimg:(NSString *)highImg andSetTitle:(NSString *)title{
//    vc.navigationItem.title = title;
    vc.tabBarItem.title = title;
    
   
    JJSNavViewController * nav = [[JJSNavViewController alloc]initWithRootViewController:vc];
   
    [vc.tabBarItem setImage:[UIImage imageNamed:image]];
    

    [vc.tabBarItem setSelectedImage:[[UIImage imageNamed:highImg] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    
    [self addChildViewController:nav];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//选择tabBar的下标
- (void)setSelectedIndex:(NSUInteger)selectedIndex{
    [super setSelectedIndex:selectedIndex];
    
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
