//
//  QHTKRoomChatVIewUtil+Room.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import "QHTKRoomChatVIewUtil.h"

NS_ASSUME_NONNULL_BEGIN

@interface QHTKRoomChatVIewUtil (Room)

+ (NSMutableAttributedString *)toHello:(NSDictionary *)data;
+ (NSMutableAttributedString *)toWelcome:(NSDictionary *)data;

@end

NS_ASSUME_NONNULL_END
