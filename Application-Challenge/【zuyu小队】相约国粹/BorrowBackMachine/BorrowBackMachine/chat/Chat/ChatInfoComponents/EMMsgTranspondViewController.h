//
//  EMMsgTranspondViewController.h
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/2/20.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMRefreshTableViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface EMMsgTranspondViewController : EMRefreshTableViewController

@property (nonatomic, copy) void (^doneCompletion)(NSString *aUsername);

@end

NS_ASSUME_NONNULL_END
