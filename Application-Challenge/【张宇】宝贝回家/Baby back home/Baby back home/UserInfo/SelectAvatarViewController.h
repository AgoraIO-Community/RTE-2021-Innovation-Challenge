//
//  SelectAvatarViewController.h
//  EaseIM
//
//  Created by lixiaoming on 2021/3/22.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EMRefreshViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface SelectAvatarViewController : EMRefreshViewController
- (instancetype)initWithCurrentAvatar:(NSString*)aAvatarUrl;
@end

NS_ASSUME_NONNULL_END
