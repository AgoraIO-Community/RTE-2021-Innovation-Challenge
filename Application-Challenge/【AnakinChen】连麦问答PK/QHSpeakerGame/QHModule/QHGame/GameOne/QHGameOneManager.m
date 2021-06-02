//
//  QHGameOneManager.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/18.
//

#import "QHGameOneManager.h"

#import "QHAppContext.h"

@interface QHGameOneManager ()

@property (nonatomic, strong) NSString *q;
@property (nonatomic, strong) NSMutableArray *a;

@end

@implementation QHGameOneManager

+ (instancetype)createWith:(NSDictionary *)data {
    return [QHGameOneManager new];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self p_setup];
    }
    return self;
}

- (NSString *)check:(NSString *)c {
    if (c == nil) {
        return nil;
    }
    NSString *res = nil;
    for (NSString *aa in self.a) {
        if ([c containsString:aa]) {
            res = aa;
            break;
        }
    }
    if (res != nil) {
        [self.a removeObject:res];
    }
    return res;
}

- (void)remove:(NSString *)a {
    [self.a removeObject:a];
}

- (BOOL)hasAnswerIsOver {
    return self.a.count == 0;
}

- (NSString *)getQuestionTitle {
    return self.q;
}
- (NSInteger)nextQuestion {
    return 0;
}
- (NSString *)questionSubTitle:(NSInteger)idx {
    return 0;
}

#pragma mark - Private

- (void)p_setup {
    NSDictionary *q = [self p_readQ];
    self.q = q[@"t"];
    self.a = [NSMutableArray arrayWithArray:q[@"a"]];
}

- (NSDictionary *)p_readQ {
    NSString *path = [[NSBundle mainBundle] pathForResource:@"G1_Q" ofType:@"plist"];
    NSDictionary *qs = [NSDictionary dictionaryWithContentsOfURL:[NSURL fileURLWithPath:path]];
    return qs[[QHAppContext context].questionId];
}

#pragma mark - Test

- (NSString *)getAnswer {
    return self.a.firstObject;
}

@end
