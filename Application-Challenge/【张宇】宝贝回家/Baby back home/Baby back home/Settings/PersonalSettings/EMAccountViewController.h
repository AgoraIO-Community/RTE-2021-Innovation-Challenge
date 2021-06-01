//
//  EMAccountViewController.h
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2018/12/24.
//  Copyright © 2018 XieYajie. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface EMAccountViewController : UITableViewController

@property (nonatomic, copy) void (^updateAPNSNicknameCompletion)(void);

@end

NS_ASSUME_NONNULL_END
