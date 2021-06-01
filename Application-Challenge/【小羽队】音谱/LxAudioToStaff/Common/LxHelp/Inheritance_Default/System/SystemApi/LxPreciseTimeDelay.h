//
//  LxPreciseTimeDelay.h
//  SmartPiano
//
//  Created by DavinLee on 2020/12/1.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LxPreciseTimeDelay : NSObject
{
    double timebase_ratio;
    
    NSMutableArray *events;
    NSCondition *condition;
    pthread_t thread;
}
+ (void)lx_scheduleAction:(SEL)action target:(id)target inTimeInterval:(NSTimeInterval)timeInterval;
+ (void)lx_scheduleAction:(SEL)action target:(id)target context:(id)context inTimeInterval:(NSTimeInterval)timeInterval;
+ (void)lx_cancelAction:(SEL)action target:(id)target;
+ (void)lx_cancelAction:(SEL)action target:(id)target context:(id)context;
#if NS_BLOCKS_AVAILABLE
+ (void)lx_scheduleBlock:(void (^)(void))block inTimeInterval:(NSTimeInterval)timeInterval;
#endif
@end

NS_ASSUME_NONNULL_END
