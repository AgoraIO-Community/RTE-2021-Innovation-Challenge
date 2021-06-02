//
//  PushToGroupTool.h
//  BorrowBackMachine
//
//  Created by zuyu on 2021/5/25.
//  Copyright © 2021 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PushToGroupTool : NSObject
 

+(void)groupBookID:(NSString *)bookID withTitle:(NSString *)title withVC:(UIViewController *)vc;
@end

NS_ASSUME_NONNULL_END
