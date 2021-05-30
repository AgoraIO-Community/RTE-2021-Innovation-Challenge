//
//  QHHyphenateChatManager.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface QHHyphenateChatManager : NSObject

- (NSDictionary *)join;
- (NSDictionary *)began:(NSString *)qkey players:(NSArray *)ps;
- (NSDictionary *)result:(NSString *)an;
- (NSDictionary *)nextplay:(NSString *)user questionIdx:(NSInteger)idx;
- (NSDictionary *)end;
- (NSDictionary *)end4Win:(NSString *)userName;

- (NSDictionary *)sayHello;
- (NSDictionary *)welcome;

@end

NS_ASSUME_NONNULL_END
