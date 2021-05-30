//
//  QHTKRoomChatVIewUtil.h
//  QHChatDemo
//
//  Created by Anakin chen on 2019/11/8.
//  Copyright © 2019 Chen Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface QHTKRoomChatVIewUtil : NSObject

+ (NSMutableAttributedString *)toChat:(NSDictionary *)data isAnchor:(BOOL)anchor isCurrentUser:(BOOL)user;

+ (NSMutableAttributedString *)toNotice:(NSDictionary *)data;

@end

NS_ASSUME_NONNULL_END
