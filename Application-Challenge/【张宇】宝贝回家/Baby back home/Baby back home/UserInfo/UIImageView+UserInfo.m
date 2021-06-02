//
//  UIImageView+UserInfo.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/31.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "UIImageView+UserInfo.h"
#import "UserInfoStore.h"
#import <SDWebImage/UIImageView+WebCache.h>

@implementation UIImageView (UserInfo)
-(void)showUserInfoAvatar:(NSString*)aUid
{
    EMUserInfo* userInfo = [[UserInfoStore sharedInstance] getUserInfoById:aUid];
    if(userInfo && userInfo.avatarUrl.length > 0) {
        NSURL* url = [NSURL URLWithString:userInfo.avatarUrl];
        if(url) {
            [self sd_setImageWithURL:url completed:nil];
        }
    }
}
@end
