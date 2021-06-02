//
//  QHSpeakerManager.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/17.
//

#import "QHSpeakerManager.h"

#import "QHAppContext.h"

#import "QHHyphenateChatManagerDefine.h"
#import "QHGameManagerProtocol.h"
#import "QHGameTwoManager.h"

@interface QHSpeakerManager ()

@property (nonatomic, strong) id <QHGameManagerProtocol> gameManager;
@property (nonatomic, strong) NSMutableString *content;
@property (nonatomic, strong) NSMutableDictionary *sorceDic;

@end

@implementation QHSpeakerManager

- (instancetype)init {
    self = [super init];
    if (self) {
        [self p_setup];
    }
    return self;
}

- (NSString *)check {
    NSString *res = [_gameManager check:self.content];
    _content = [NSMutableString new];
    return res;
}

- (void)remove:(NSString *)a {
    [_gameManager remove:a];
}

/*
 (
    {
         "{\"sn\":1,\"ls\":true,\"bg\":0,\"ed\":0,\"ws\":[{\"bg\":24,\"cw\":[{\"sc\":0.0,\"w\":\"\U6625\U5929\"}]},{\"bg\":0,\"cw\":[{\"sc\":0.0,\"w\":\"\U3002\"}]}]}" = 100;
     }
 )
 */
- (void)add:(NSArray *)ar {
    NSMutableString *c = [NSMutableString new];
    for (NSDictionary *a in ar) {
        for (NSString *key in a.allKeys) {
            NSData *jsonData = [key dataUsingEncoding:NSUTF8StringEncoding];
            NSError *err;
            NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:&err];
            if (err) {
                NSLog(@"json解析失败：%@",err);
            }
            else {
                NSArray *ws = dic[@"ws"];
                for (NSDictionary *wws in ws) {
                    NSArray *cw = wws[@"cw"];
                    for (NSDictionary *ccw in cw) {
                        [c appendString:ccw[@"w"]];
                    }
                }
            }
        }
    }
    if (c.length > 0) {
        [self.content appendString:c];
    }
}

// ---

- (void)start {
    self.content = nil;
    self.content = [NSMutableString new];
}

- (void)joinlocal {
    if ([self.players containsObject:[QHAppContext context].curUser]) {
        return;
    }
    [self.players addObject:[QHAppContext context].curUser];
}

- (void)join:(NSString *)user {
    if ([self.players containsObject:user]) {
        return;
    }
    [self.players addObject:user];
}

- (NSString *)nextPlayer {
    NSInteger idx = [self.players indexOfObject:[QHAppContext context].curUser] + 1;
    return idx >= self.players.count ? self.players[0] : self.players[idx];
}

- (NSInteger)nextQuestion {
    return [self.gameManager nextQuestion];
}

- (NSString *)getQuestionSubTitle:(NSInteger)idx {
    return [self.gameManager questionSubTitle:idx];
}

- (void)dealMsg:(NSDictionary *)msg {
    NSInteger type = [msg[@"op"] integerValue];
    switch (type) {
        case QHHCActionJoin: {
            if ([QHAppContext context].isHost && self.players.count < 2) {
                [self join:msg[@"n"]];
            }
            else if ([msg[@"host"] boolValue]) {
                _canReady = YES;
            }
        }
            break;
        case QHHCActionBegan: {
            self.players = [NSMutableArray arrayWithArray:msg[@"ps"]];
        }
            break;
        case QHHCActionResult: {
            if ([msg[@"res"] boolValue] == YES) {
                NSString *a = msg[@"an"];
                if (![a isEqualToString:@"失败"]) {
                    [self remove:a];
                }
            }
        }
            break;
        case QHHCActionNext: {
        }
            break;
        case QHHCActionEnd: {
            _canReady = NO;
        }
            break;
        default:
            break;
    }
}

- (BOOL)hasAnswerIsOver {
    return [self.gameManager hasAnswerIsOver];
}

- (NSString *)getQuestionTitle {
    return [self.gameManager getQuestionTitle];
}

- (void)initSorce {
    self.sorceDic = [NSMutableDictionary new];
    for (NSString *s in self.players) {
        [self.sorceDic setValue:@(0) forKey:s];
    }
}

- (void)addSorce:(NSString *)userName {
    self.sorceDic[userName] = @([self.sorceDic[userName] integerValue] + 1);
}

- (NSString *)win {
    NSInteger maxSorce = 0;
    NSString *userName = nil;
    for (NSString *s in self.players) {
        maxSorce = MAX(maxSorce, [self.sorceDic[s] integerValue]);
    }
    if (maxSorce <= 0) {
        return nil;
    }
    for (NSString *s in self.players) {
        if ([self.sorceDic[s] integerValue] == maxSorce) {
            if (userName == nil) {
                userName = s;
            }
            else {
                return nil;
            }
        }
    }
    return userName;
}

#pragma mark - Private

- (void)p_setup {
    _gameManager = [QHGameTwoManager createWith:nil];
    _players = [NSMutableArray new];
    _content = [NSMutableString new];
    _canReady = NO;
}

#pragma mark -

#pragma mark - Test

- (NSString *)getAnswer {
    NSString *a = [self.gameManager getAnswer];
    return a;
}

- (NSString *)testCheck:(NSString *)testStr {
    NSString *res = [_gameManager check:testStr];
    _content = [NSMutableString new];
    return res;
}

@end
