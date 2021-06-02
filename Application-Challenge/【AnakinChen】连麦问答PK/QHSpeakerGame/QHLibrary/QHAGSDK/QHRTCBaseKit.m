//
//  QHRTCBaseKit.m
//  SohuVideoLiveSDK
//
//  Created by Anakin chen on 2020/4/26.
//  Copyright © 2020 ... All rights reserved.
//

#import "QHRTCBaseKit.h"

@implementation QHRTCBaseKit

+ (instancetype)createWith:(UIView *)superV {
    return nil;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _state = QHRTCKitStateIdle;
        _bPush = NO;
    }
    return self;
}

- (void)setupWithLiveConfig:(NSDictionary *)cfgDic {}
- (BOOL)config:(NSDictionary *)dic { return NO; }
- (BOOL)join { return NO; }
- (void)leave {}
- (void)addPush:(NSString *)streamUrl {
    _streamUrl = [streamUrl copy];
}
- (BOOL)push { return NO; }
- (void)unpush {}
- (void)resetFilterConfig {}
- (BOOL)switchCamera { return NO; }
- (void)refreshNickName {}
- (void)refreshLayer {}
- (void)enterBackground:(BOOL)bEnter {}

@end
