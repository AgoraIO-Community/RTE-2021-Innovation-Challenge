//
//  QHGameTwoManager.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/23.
//

#import "QHGameTwoManager.h"

@interface QHGameTwoManager ()

@property (nonatomic, strong) NSString *q;
@property (nonatomic, strong) NSMutableArray *a;
@property (nonatomic) NSInteger aIdx;

@end

@implementation QHGameTwoManager

+ (instancetype)createWith:(NSDictionary *)data {
    return [QHGameTwoManager new];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self p_setup];
    }
    return self;
}

- (NSString *)check:(NSString *)c {
    NSLog(@"语音：%@", c);
    if (c == nil || [self hasAnswerIsOver]) {
        return nil;
    }
    NSString *res = nil;
    NSArray *a = [self.a objectAtIndex:self.aIdx];
    if ([c containsString:a[1]]) {
        res = [a[1] copy];
        self.aIdx += 1;
    }
    return res;
}

- (void)remove:(NSString *)a {
}

- (BOOL)hasAnswerIsOver {
    return self.aIdx >= self.a.count;
}

- (NSString *)getQuestionTitle {
    return self.q;
}

- (NSInteger)nextQuestion {
    return self.aIdx;
}

- (NSString *)questionSubTitle:(NSInteger)idx {
    self.aIdx = idx;
    if ([self hasAnswerIsOver]) {
        return nil;
    }
    NSArray *a = [self.a objectAtIndex:self.aIdx];
    return a[0];
}

#pragma mark - Private

- (void)p_setup {
    NSDictionary *q = [self p_readQ];
    self.q = q[@"t"];
    self.a = [NSMutableArray arrayWithArray:q[@"a"]];
    self.aIdx = 0;
}

- (NSDictionary *)p_readQ {
    NSString *path = [[NSBundle mainBundle] pathForResource:@"G2_Q2" ofType:@"plist"];
    NSDictionary *qs = [NSDictionary dictionaryWithContentsOfURL:[NSURL fileURLWithPath:path]];
    return qs;
}

#pragma mark - Test

- (NSString *)getAnswer {
    if ([self hasAnswerIsOver]) {
        return nil;
    }
    NSArray *a = [self.a objectAtIndex:self.aIdx];
    return a[1];
}

@end
