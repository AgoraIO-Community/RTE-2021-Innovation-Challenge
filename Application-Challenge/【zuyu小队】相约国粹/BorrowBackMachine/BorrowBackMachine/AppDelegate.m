//
//  AppDelegate.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "AppDelegate.h"
#import "zuyu.h"
#import "IQKeyboardManager.h"
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <HyphenateChat/HyphenateChat.h>
#import "EaseIMHelper.h"
#import "EMNotificationHelper.h"
#import "SingleCallController.h"
#import "ConferenceController.h"
#import "UserInfoStore.h"



@interface AppDelegate ()<EaseCallDelegate>

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    
    [ZuyuPlaceholderImage loadZuyuPlaceholder];
    
    [self DesignNavigationAndTabBar];
    [AMapServices sharedServices].apiKey = GAODEMAPKEY;



    [IQKeyboardManager sharedManager].enable = YES; // 控制整个功能是否启用
    [IQKeyboardManager sharedManager].shouldResignOnTouchOutside = YES;
    
    [self listenNetWorkingStatus];
    
    EMOptions *options = [EMOptions optionsWithAppkey:@"1134210330040458#xygc"];
    // apnsCertName是证书名称，可以先传nil，等后期配置apns推送时在传入证书名称
    options.apnsCertName = nil;
    [[EMClient sharedClient] initializeSDKWithOptions:options];
    
    
    [EaseIMKitManager initWithEMOptions:options];
    [EaseIMHelper shareHelper];
    [EMNotificationHelper shared];
    [SingleCallController sharedManager];
    [ConferenceController sharedManager];
    [[UserInfoStore sharedInstance] loadInfosFromLocal];
    
    
    EaseCallConfig* config = [[EaseCallConfig alloc] init];
    config.agoraAppId = @"e187a8def9164dcc859790c72de4ddb0";
    config.enableRTCTokenValidate = false;
    [EaseCallManager.sharedManager.getEaseCallConfig setUsers:[NSMutableDictionary dictionary]];
    [[EaseCallManager sharedManager] initWithConfig:config delegate:self];
    
    
    
    
    if ([ISLOGIN integerValue]) {
        NSDictionary *parameter = [[NSDictionary alloc] initWithObjectsAndKeys:[[NSUserDefaults standardUserDefaults] objectForKey:@"account"],@"phone",
                     [[NSUserDefaults standardUserDefaults] objectForKey:@"password"],@"pass",
                     nil];
        [[EMClient sharedClient] loginWithUsername:[parameter objectForKey:@"phone"] password:[parameter objectForKey:@"pass"]  completion:^(NSString *aUsername, EMError *aError) {
            if (!aError) {
                // 设置自动登录
                NSLog(@"登录成功-----");
            } else {
                NSLog(@"登录失败----%@", aError.errorDescription);
            }
        }];
    }
   
    // Override point for customization after application launch.
    return YES;
}

- (void)callDidEnd:(NSString*)aChannelName reason:(EaseCallEndReason)aReason time:(int)aTm type:(EaseCallType)aCallType
{
    NSString* msg = @"";
    switch (aReason) {
        case EaseCallEndReasonHandleOnOtherDevice:
            msg = @"已在其他端处理";
            break;
        case EaseCallEndReasonBusy:
            msg = @"对方忙";
            break;
        case EaseCallEndReasonRefuse:
            msg = @"拒绝通话";
            break;
        case EaseCallEndReasonCancel:
            msg = @"您已取消呼叫";
            break;
        case EaseCallEndReasonRemoteCancel:
            msg = @"通话已取消";
            break;
        case EaseCallEndReasonRemoteNoResponse:
            msg = @"对方超时未响应";
            break;
        case EaseCallEndReasonNoResponse:
            msg = @"未接听";
            break;
        case EaseCallEndReasonHangup:
            msg = [NSString stringWithFormat:@"通话已结束，通话时长：%d秒",aTm];
            break;
        default:
            break;
    }
    if([msg length] > 0)
       [self showHint:msg];
}


// 异常回调
- (void)callDidOccurError:(EaseCallError *)aError
{
    

    
}

//- (void)callDidJoinChannel:(NSString * _Nonnull)aChannelName uid:(NSUInteger)aUid {
//
//    NSString *rtcToken =
//    @"00679ebf53957d34f0b89d2b03fe6450384IAD3YuKZZVIta4xoaiyYjY9EZK24t81bRzA/nF5dzEtngJoWRH4AAAAAEAB+PjEWgrywYAEAAQCCvLBg";
//
//    [[EaseCallManager sharedManager] setRTCToken:rtcToken channelName:aChannelName uid:aUid];
//
//}

- (void)callDidJoinChannel:(NSString*_Nonnull)aChannelName uid:(NSUInteger)aUid
{
    [self _fetchUserMapsFromServer:aChannelName];
}

- (void)_fetchUserMapsFromServer:(NSString*)aChannelName
{
    // 这里设置映射表，设置头像，昵称
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config
                                                          delegate:nil
                                                     delegateQueue:[NSOperationQueue mainQueue]];

    NSString* strUrl = [NSString stringWithFormat:@"http://a1.easemob.com/channel/mapper?userAccount=%@&channelName=%@&appkey=%@",[EMClient sharedClient].currentUsername,aChannelName,[EMClient sharedClient].options.appkey];
    NSString*utf8Url = [strUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL* url = [NSURL URLWithString:utf8Url];
    NSMutableURLRequest* urlReq = [[NSMutableURLRequest alloc] initWithURL:url];
    [urlReq setValue:[NSString stringWithFormat:@"Bearer %@",[EMClient sharedClient].accessUserToken ] forHTTPHeaderField:@"Authorization"];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:urlReq completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if(data) {
            NSDictionary* body = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
            NSLog(@"mapperBody:%@",body);
            if(body) {
                NSString* resCode = [body objectForKey:@"code"];
                if([resCode isEqualToString:@"RES_0K"]) {
                    NSString* channelName = [body objectForKey:@"channelName"];
                    NSDictionary* result = [body objectForKey:@"result"];
                    NSMutableDictionary<NSNumber*,NSString*>* users = [NSMutableDictionary dictionary];
                    for (NSString* strId in result) {
                        NSString* username = [result objectForKey:strId];
                        NSNumber* uId = [NSNumber numberWithInteger:[strId integerValue]];
                        [users setObject:username forKey:uId];
                        EMUserInfo* info = [[UserInfoStore sharedInstance] getUserInfoById:username];
                        if(info && (info.avatarUrl.length > 0 || info.nickName > 0)) {
                            EaseCallUser* user = [EaseCallUser userWithNickName:info.nickName image:[NSURL URLWithString:info.avatarUrl]];
                            [[[EaseCallManager sharedManager] getEaseCallConfig] setUser:username info:user];
                        }
                    }
                    [[EaseCallManager sharedManager] setUsers:users channelName:channelName];
                }
            }
        }
    }];

    [task resume];
}


- (void)callDidReceive:(EaseCallType)aType inviter:(NSString * _Nonnull)user ext:(NSDictionary * _Nullable)aExt {
    
}



- (void)multiCallDidInvitingWithCurVC:(UIViewController * _Nonnull)vc excludeUsers:(NSArray<NSString *> * _Nullable)users ext:(NSDictionary * _Nullable)aExt {
    
}


- (void)remoteUserDidJoinChannel:(NSString * _Nonnull)aChannelName uid:(NSInteger)aUid username:(NSString * _Nullable)aUserName {
    
}

- (void)callDidRequestRTCTokenForAppId:(NSString *)aAppId channelName:(NSString *)aChannelName account:(NSString *)aUserAccount uid:(NSInteger)aAgoraUid
{
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config
                                                          delegate:nil
                                                     delegateQueue:[NSOperationQueue mainQueue]];
    
    NSString* strUrl = [NSString stringWithFormat:@"http://a1.easemob.com/token/rtcToken?userAccount=%@&channelName=%@&appkey=%@",[EMClient sharedClient].currentUsername,aChannelName,[EMClient sharedClient].options.appkey];
    NSString*utf8Url = [strUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL* url = [NSURL URLWithString:utf8Url];
    NSMutableURLRequest* urlReq = [[NSMutableURLRequest alloc] initWithURL:url];
    [urlReq setValue:[NSString stringWithFormat:@"Bearer %@",[EMClient sharedClient].accessUserToken ] forHTTPHeaderField:@"Authorization"];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:urlReq completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if(data) {
            NSDictionary* body = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
            NSLog(@"%@",body);
            if(body) {
                NSString* resCode = [body objectForKey:@"code"];
                if([resCode isEqualToString:@"RES_0K"]) {
                    NSString* rtcToken = [body objectForKey:@"accessToken"];
//                    [[EaseCallManager sharedManager] setRTCToken:rtcToken channelName:aChannelName];
                }
            }
        }
        
        
    }];

    [task resume];
}


- (void)callDidRequestRTCTokenForAppId:(NSString *)aAppId channelName:(NSString *)aChannelName account:(NSString *)aUserAccount
{
    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config
                                                          delegate:nil
                                                     delegateQueue:[NSOperationQueue mainQueue]];

    NSString* strUrl = [NSString stringWithFormat:@"http://a1.easemob.com/token/rtcToken?userAccount=%@&channelName=%@&appkey=%@",[EMClient sharedClient].currentUsername,aChannelName,[EMClient sharedClient].options.appkey];
    NSString*utf8Url = [strUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL* url = [NSURL URLWithString:utf8Url];
    NSMutableURLRequest* urlReq = [[NSMutableURLRequest alloc] initWithURL:url];
    [urlReq setValue:[NSString stringWithFormat:@"Bearer %@",[EMClient sharedClient].accessUserToken ] forHTTPHeaderField:@"Authorization"];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:urlReq completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if(data) {
            NSDictionary* body = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
            NSLog(@"%@",body);
            if(body) {
                NSString* resCode = [body objectForKey:@"code"];
                if([resCode isEqualToString:@"RES_0K"]) {
                    NSString* rtcToken = [body objectForKey:@"accessToken"];
//                    [[EaseCallManager sharedManager] setRTCToken:rtcToken channelName:aChannelName];
                }
            }
        }
        
        
    }];

    [task resume];
}




- (void)showHint:(NSString *)hint
{
    UIWindow *win = [[[UIApplication sharedApplication] windows] firstObject];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:win animated:YES];
    hud.userInteractionEnabled = NO;
    // Configure for text only and offset down
    hud.mode = MBProgressHUDModeText;
    hud.label.text = hint;
    hud.margin = 10.f;
    CGPoint offset = hud.offset;
    offset.y = 180;
    hud.offset = offset;
    hud.removeFromSuperViewOnHide = YES;
    [hud hideAnimated:YES afterDelay:2];
}





#pragma mark 网络监听

-(void)listenNetWorkingStatus{
    
    //1:创建网络监听者
    
    AFNetworkReachabilityManager *mgr = [AFNetworkReachabilityManager sharedManager];
    
    
    [mgr setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
        
        
        switch (status) {
                
            case AFNetworkReachabilityStatusUnknown:
                
                NSLog(@"未知网络");
                
                break;
                
            case AFNetworkReachabilityStatusNotReachable:
                
                [ZuyuAlertShow alertShow:@"无网络信号" viewController:nil];
                NSLog(@"不可达的网络(未连接)");
                
                break;
                
            case AFNetworkReachabilityStatusReachableViaWiFi:
                
                NSLog(@"wifi的网络");
                
                break;
                
            case AFNetworkReachabilityStatusReachableViaWWAN:
                
                NSLog(@"蜂窝网络");
                
                break;
                
            default:
                
                break;
                
        }
        
         [[NSNotificationCenter defaultCenter] postNotificationName:@"netWorkChange" object:@(status)];
        
    }];
    
    [mgr startMonitoring];
    
}

-(void)dealloc{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"netWorkChange" object:nil];
}
#pragma mark - Tabbar
-(void)DesignNavigationAndTabBar
{
    UITabBarController * tabBarController = [[UITabBarController alloc] init];
    tabBarController.viewControllers = [ZuyuTabbar zuyuBarinit];
    self.window.rootViewController = tabBarController;
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end
