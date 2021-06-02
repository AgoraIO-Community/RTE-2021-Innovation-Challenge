//
//  PushToGroupTool.m
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/25.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import "PushToGroupTool.h"

@implementation PushToGroupTool
+(void)groupBookID:(NSString *)bookID withTitle:(NSString *)title withVC:(UIViewController *)vc
{
    
    if (![ISLOGIN integerValue]) {
        [self showHint:@"请先登录"];
        return;
    }
    
    NSDictionary *param = [NSDictionary dictionaryWithObjectsAndKeys:bookID,@"key", nil];
     
    [[AFHTTPSessionManager manager] POST:@"http://121.42.12.84/api/v1/keyConfig" parameters:param progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            NSString *groupID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"value"]] ;
            
            if ([groupID isEqualToString:@"0"]) {//创建群聊
                [self createGroup:vc withTitle:title bookID:bookID];
            }else{//加入群聊
                [self pushToChatVC:groupID withVC:vc];
            }
            
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
        }];
    
    

}

+(void)addGroupID:(NSString *)key groupid:(NSString *)value
{
    NSDictionary *param = [NSDictionary dictionaryWithObjectsAndKeys:key,@"key",
                           value,@"value",nil];
    [[AFHTTPSessionManager manager] POST:@"http://121.42.12.84/api/v1/saddKey" parameters:param progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
        }];
    
    
}


+(void)createGroup:(UIViewController *)vc withTitle:(NSString *)title bookID:(NSString *)bookID
{
    
    EMGroupOptions *setting = [[EMGroupOptions alloc] init]; // 群组属性选项
        setting.maxUsersCount = 500; // 群组的最大成员数(含群主、管理员，默认是200，最大3000)
        setting.IsInviteNeedConfirm = NO; //邀请群成员时，是否需要发送邀请通知.若NO，被邀请的人自动加入群组
        setting.style = EMGroupStylePublicOpenJoin;// 创建不同类型的群组，这里需要才传入不同的类型
        setting.ext = @"群组扩展信息"; // 扩展信息

    
    [[EMClient sharedClient].groupManager createGroupWithSubject:title description:@"" invitees:@[] message:@"" setting:setting completion:^(EMGroup *aGroup, EMError *aError) {
        if(!aError){
            NSLog(@"创建群组成功 -- %@",aGroup.groupId);
            [self addGroupID:bookID groupid:aGroup.groupId];
            [self showHint:@"加入成功,因为你是第一个加入该群的人,你现在是群主"];

        } else {
            NSLog(@"创建群组失败的原因 --- %@", aError.errorDescription);
        }
    }];
    
}


+(void)pushToChatVC:(NSString *)gid withVC:(UIViewController *)vc
{
    
    [[EMClient sharedClient].groupManager joinPublicGroup:gid completion:^(EMGroup *aGroup, EMError *aError) {
        
        if(!aError){
            NSLog(@"加入群组成功 -- %@",aGroup.groupId);
            [self showHint:@"加入成功,可在通讯录中查看"];

        } else {
            
            NSLog(@"加入群组失败的原因 --- %@", aError.errorDescription);
        }
        
        if (aError.code == EMErrorGroupAlreadyJoined) {
            [self showHint:@"您已加入该群聊"];
        }
    }];
    
}

+ (void)showHint:(NSString *)hint
{
    UIWindow *win = [[[UIApplication sharedApplication] windows] firstObject];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:win animated:YES];
    hud.userInteractionEnabled = NO;
    // Configure for text only and offset down
    hud.mode = MBProgressHUDModeText;
    hud.label.text = hint;
    hud.label.numberOfLines = 0;
    hud.margin = 10.f;
    CGPoint offset = hud.offset;
    offset.y = 180;
    hud.offset = offset;
    hud.removeFromSuperViewOnHide = YES;
    [hud hideAnimated:YES afterDelay:2];
}

@end
