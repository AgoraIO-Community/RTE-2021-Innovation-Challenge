//
//  QHGameManagerProtocol.h
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/23.
//

#ifndef QHGameManagerProtocol_h
#define QHGameManagerProtocol_h

#import <Foundation/Foundation.h>

@protocol QHGameManagerProtocol <NSObject>

+ (instancetype)createWith:(NSDictionary *)data;

- (NSString *)check:(NSString *)c;
- (void)remove:(NSString *)a;
- (BOOL)hasAnswerIsOver;
- (NSString *)getQuestionTitle;
- (NSInteger)nextQuestion;
- (NSString *)questionSubTitle:(NSInteger)idx;

// Test
- (NSString *)getAnswer;

@end

#endif /* QHGameManagerProtocol_h */
