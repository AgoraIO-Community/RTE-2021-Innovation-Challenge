//
//  AppDelegate.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/12.
//

#import "AppDelegate.h"
#import "ViewController.h"
#import "BBTabViewController.h"
#import <HyphenateChat/HyphenateChat.h>
#import "EMNotificationHelper.h"
#import "JJSNavViewController.h"
#import "UserInfoStore.h"
#import "EMLoginViewController.h"

#import "EaseIMHelper.h"
#import "SingleCallController.h"
#import "ConferenceController.h"
#import "EMGlobalVariables.h"
#import <MBProgressHUD/MBProgressHUD.h>
@interface AppDelegate ()<EaseCallDelegate>

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
     
    self.window.frame=CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width,  [UIScreen mainScreen].bounds.size.height);
    EMOptions *options = [EMOptions optionsWithAppkey:@"1118210413091291#weatherducation"];
    // apnsCertName是证书名称，可以先传nil，等后期配置apns推送时在传入证书名称
    options.apnsCertName = nil;
    [[EMClient sharedClient] initializeSDKWithOptions:options];
    [self.window makeKeyAndVisible];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(loginStateChange:) name:ACCOUNT_LOGIN_CHANGED object:nil];
    
//    EMOptions *options = [EMOptions optionsWithAppkey:@"1118210413091291#weatherducation"];
//    // apnsCertName是证书名称，可以先传nil，等后期配置apns推送时在传入证书名称
//    options.apnsCertName = nil;
//    [[EMClient sharedClient] initializeSDKWithOptions:options];
    
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"loginState"]){
        [[NSNotificationCenter defaultCenter] postNotificationName:ACCOUNT_LOGIN_CHANGED object:@(YES)];
    } else {
        [[NSNotificationCenter defaultCenter] postNotificationName:ACCOUNT_LOGIN_CHANGED object:@(NO)];
    }
    
    
    
//    [EaseIMKitManager initWithEMOptions:options];
//    [EaseIMHelper shareHelper];
//    [EMNotificationHelper shared];
//    [SingleCallController sharedManager];
//    [ConferenceController sharedManager];
//    [[UserInfoStore sharedInstance] loadInfosFromLocal];
//
//
//    EaseCallConfig* config = [[EaseCallConfig alloc] init];
//    config.agoraAppId = @"f42b8f75b7104e03a8c767d37574045c";
//    config.enableRTCTokenValidate = NO;
//    [EaseCallManager.sharedManager.getEaseCallConfig setUsers:[NSMutableDictionary dictionary]];
//    [[EaseCallManager sharedManager] initWithConfig:config delegate:self];
    
    return YES;
}

- (void)loginStateChange:(NSNotification *)aNotif
{
    UINavigationController *navigationController = nil;
    
    BOOL loginSuccess = [aNotif.object boolValue];
    if (loginSuccess) {//登录成功加载主窗口控制器
        navigationController = (UINavigationController *)self.window.rootViewController;
        if (!navigationController || (navigationController && ![navigationController.viewControllers[0] isKindOfClass:[BBTabViewController class]])) {
            BBTabViewController *homeController = [BBTabViewController sharedMainTabBarController];
            navigationController = [[UINavigationController alloc] initWithRootViewController:homeController];
        }
        

        EMOptions *options = [EMOptions optionsWithAppkey:@"1118210413091291#weatherducation"];
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
        config.agoraAppId = @"f42b8f75b7104e03a8c767d37574045c";
        config.enableRTCTokenValidate = NO;
        [EaseCallManager.sharedManager.getEaseCallConfig setUsers:[NSMutableDictionary dictionary]];
        [[EaseCallManager sharedManager] initWithConfig:config delegate:self];
        
        
        
    } else {//登录失败加载登录页面控制器
        EMLoginViewController *controller = [[EMLoginViewController alloc] init];
        navigationController = [[UINavigationController alloc] initWithRootViewController:controller];
    }
    

    navigationController.view.backgroundColor = [UIColor whiteColor];
    self.window.rootViewController = navigationController;
    
    [[UINavigationBar appearance] setTitleTextAttributes:
     [NSDictionary dictionaryWithObjectsAndKeys:[UIColor blackColor], NSForegroundColorAttributeName, [UIFont systemFontOfSize:18], NSFontAttributeName, nil]];
    [[UITableViewHeaderFooterView appearance] setTintColor:kColor_LightGray];
    
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


//- (void)callDidReceive:(EaseCallType)aType inviter:(NSString * _Nonnull)user ext:(NSDictionary * _Nullable)aExt {
//
//}



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


// 振铃时增加回调
- (void)callDidReceive:(EaseCallType)aType inviter:(NSString*_Nonnull)username ext:(NSDictionary*)aExt
{
    EMUserInfo* info = [[UserInfoStore sharedInstance] getUserInfoById:username];
        NSLog(@"%@--%@,--%@",info,info.avatarUrl,info.nickName);

    if(info && (info.avatarUrl.length > 0 || info.nickName > 0)) {
        EaseCallUser* user = [EaseCallUser userWithNickName:info.nickName image:[NSURL URLWithString:info.avatarUrl]];
        [[[EaseCallManager sharedManager] getEaseCallConfig] setUser:username info:user];
    }
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



@end
