//
//  QHTKRoomChatVIewUtil+Game.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import "QHTKRoomChatVIewUtil.h"

NS_ASSUME_NONNULL_BEGIN

@interface QHTKRoomChatVIewUtil (Game)

+ (NSMutableAttributedString *)toJoinGame:(NSDictionary *)data;
+ (NSMutableAttributedString *)toBeganGame:(NSDictionary *)data;
+ (NSMutableAttributedString *)toResultGame:(NSDictionary *)data;
+ (NSMutableAttributedString *)toNextPlayerGame:(NSDictionary *)data;
+ (NSMutableAttributedString *)toEndGame:(NSDictionary *)data;

@end

NS_ASSUME_NONNULL_END
