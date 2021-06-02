//
//  QHSpeakerManager.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/17.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface QHSpeakerManager : NSObject

@property (nonatomic, strong) NSMutableArray *players;
@property (nonatomic) BOOL canReady;

- (void)add:(NSArray *)ar;
- (NSString *)check;
- (void)remove:(NSString *)a;

- (void)start;
- (void)joinlocal;
- (NSString *)nextPlayer;
- (NSInteger)nextQuestion;
- (void)dealMsg:(NSDictionary *)msg;
- (BOOL)hasAnswerIsOver;
- (NSString *)getQuestionTitle;
- (NSString *)getQuestionSubTitle:(NSInteger)idx;

- (void)initSorce;
- (void)addSorce:(NSString *)userName;
- (NSString *)win;

- (NSString *)getAnswer;
- (NSString *)testCheck:(NSString *)testStr;

@end

NS_ASSUME_NONNULL_END
