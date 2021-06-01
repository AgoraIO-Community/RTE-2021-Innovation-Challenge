//
//  LxPreciseTimeDelay.m
//  SmartPiano
//
//  Created by DavinLee on 2020/12/1.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import "LxPreciseTimeDelay.h"
#import <mach/mach_time.h>
#import <pthread.h>

#define kSpinLockTime 0.01
//#define kAnalyzeTiming // Uncomment to display timing discrepancy reports

static LxPreciseTimeDelay *__sharedInstance = nil;

static NSString *kTimeKey = @"time";
static NSString *kTargetKey = @"target";
static NSString *kSelectorKey = @"selector";
static NSString *kArgumentKey = @"argument";
#if NS_BLOCKS_AVAILABLE
static NSString *kBlockKey = @"block";
#endif

@interface LxPreciseTimeDelay ()
- (void)scheduleAction:(SEL)action target:(id)target inTimeInterval:(NSTimeInterval)timeInterval;
- (void)scheduleAction:(SEL)action target:(id)target context:(id)context inTimeInterval:(NSTimeInterval)timeInterval;
- (void)cancelAction:(SEL)action target:(id)target;
- (void)cancelAction:(SEL)action target:(id)target context:(id)context;
#if NS_BLOCKS_AVAILABLE
- (void)scheduleBlock:(void (^)(void))block inTimeInterval:(NSTimeInterval)timeInterval;
#endif
- (void)addSchedule:(NSDictionary*)schedule;
void thread_signal(int signal);
void *thread_entry(void* argument);
- (void)thread;
@end

@implementation LxPreciseTimeDelay

+ (void)lx_scheduleAction:(SEL)action target:(id)target inTimeInterval:(NSTimeInterval)timeInterval {
    if ( !__sharedInstance ) __sharedInstance = [[LxPreciseTimeDelay alloc] init];
    [__sharedInstance scheduleAction:action target:target inTimeInterval:timeInterval];
}
+ (void)lx_scheduleAction:(SEL)action target:(id)target context:(id)context inTimeInterval:(NSTimeInterval)timeInterval {
    if ( !__sharedInstance ) __sharedInstance = [[LxPreciseTimeDelay alloc] init];
    [__sharedInstance scheduleAction:action target:target context:context inTimeInterval:timeInterval];
}
+ (void)lx_cancelAction:(SEL)action target:(id)target {
    if ( !__sharedInstance ) __sharedInstance = [[LxPreciseTimeDelay alloc] init];
    [__sharedInstance cancelAction:action target:target];
}
+ (void)lx_cancelAction:(SEL)action target:(id)target context:(id)context {
    if ( !__sharedInstance ) __sharedInstance = [[LxPreciseTimeDelay alloc] init];
    [__sharedInstance cancelAction:action target:target context:context];
}
#if NS_BLOCKS_AVAILABLE
+ (void)lx_scheduleBlock:(void (^)(void))block inTimeInterval:(NSTimeInterval)timeInterval {
    if ( !__sharedInstance ) __sharedInstance = [[LxPreciseTimeDelay alloc] init];
    [__sharedInstance scheduleBlock:block inTimeInterval:timeInterval];
}
#endif

- (id)init {
    if ( !(self = [super init]) ) return nil;
    
    struct mach_timebase_info timebase;
    mach_timebase_info(&timebase);
    timebase_ratio = ((double)timebase.numer / (double)timebase.denom) * 1.0e-9;
    
    events = [[NSMutableArray alloc] init];
    condition = [[NSCondition alloc] init];
    
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    struct sched_param param;
    param.sched_priority = sched_get_priority_max(SCHED_FIFO);
    pthread_attr_setschedparam(&attr, &param);
    pthread_attr_setschedpolicy(&attr, SCHED_FIFO);
    pthread_create(&thread, &attr, thread_entry, (__bridge void*)self);
    
    return self;
}

- (void)scheduleAction:(SEL)action target:(id)target inTimeInterval:(NSTimeInterval)timeInterval {
    [self addSchedule:[NSDictionary dictionaryWithObjectsAndKeys:
                       NSStringFromSelector(action), kSelectorKey,
                       target, kTargetKey,
                       [NSNumber numberWithUnsignedLongLong:mach_absolute_time() + (timeInterval / timebase_ratio)], kTimeKey,
                       nil]];
}

- (void)scheduleAction:(SEL)action target:(id)target context:(id)context inTimeInterval:(NSTimeInterval)timeInterval {
    [self addSchedule:[NSDictionary dictionaryWithObjectsAndKeys:
                       NSStringFromSelector(action), kSelectorKey,
                       target, kTargetKey,
                       [NSNumber numberWithUnsignedLongLong:mach_absolute_time() + (timeInterval / timebase_ratio)], kTimeKey,
                       context, kArgumentKey,
                       nil]];
}

- (void)cancelAction:(SEL)action target:(id)target {
    [condition lock];
    NSDictionary *originalNextEvent = [events count] > 0 ? [events objectAtIndex:0] : nil;
    [events filterUsingPredicate:[NSPredicate predicateWithFormat:@"%K != %@ AND %K != %@", kTargetKey, target, kSelectorKey, NSStringFromSelector(action)]];
    BOOL mustSignal = originalNextEvent != ([events count] > 0 ? [events objectAtIndex:0] : nil);
    [condition signal];
    [condition unlock];
    if ( mustSignal ) {
        pthread_kill(thread, SIGALRM); // Interrupt thread if it's performing a mach_wait_until
    }
}

- (void)cancelAction:(SEL)action target:(id)target context:(id)context {
    [condition lock];
    NSDictionary *originalNextEvent = [events count] > 0 ? [events objectAtIndex:0] : nil;
    [events filterUsingPredicate:[NSPredicate predicateWithFormat:@"%K != %@ AND %K != %@ AND %K != %@", kTargetKey, target, kSelectorKey, NSStringFromSelector(action), kArgumentKey, context]];
    BOOL mustSignal = originalNextEvent != ([events count] > 0 ? [events objectAtIndex:0] : nil);
    [condition signal];
    [condition unlock];
    if ( mustSignal ) {
        pthread_kill(thread, SIGALRM); // Interrupt thread if it's performing a mach_wait_until
    }
}

#if NS_BLOCKS_AVAILABLE
- (void)scheduleBlock:(void (^)(void))block inTimeInterval:(NSTimeInterval)timeInterval {
    [self addSchedule:[NSDictionary dictionaryWithObjectsAndKeys:
                       [block copy], kBlockKey,
                       [NSNumber numberWithUnsignedLongLong:mach_absolute_time() + (timeInterval / timebase_ratio)], kTimeKey,
                       nil]];
}
#endif

- (void)addSchedule:(NSDictionary*)schedule {
    [condition lock];
    [events addObject:schedule];
    [events sortUsingDescriptors:[NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:kTimeKey ascending:YES]]];
    BOOL mustSignal = [events count] > 1 && [events objectAtIndex:0] == schedule;
    [condition signal];
    [condition unlock];
    if ( mustSignal ) {
        pthread_kill(thread, SIGALRM); // Interrupt thread if it's performing a mach_wait_until and new schedule is earlier
    }
}

void *thread_entry(void* argument) {
    [(__bridge LxPreciseTimeDelay*)argument thread];
    return NULL;
}

void thread_signal(int signal) {
    // Ignore
}

- (void)thread {
    signal(SIGALRM, thread_signal);
    [condition lock];

    while ( 1 ) {
        while ( [events count] == 0 ) {
            [condition wait];
        }
        NSDictionary *nextEvent = [events objectAtIndex:0];
        NSTimeInterval time = [[nextEvent objectForKey:kTimeKey] unsignedLongLongValue] * timebase_ratio;
        
        [condition unlock];
        
        mach_wait_until((uint64_t)((time - kSpinLockTime) / timebase_ratio));
        
        if ( (double)(mach_absolute_time() * timebase_ratio) >= time-kSpinLockTime ) {
            
            // Spin lock until it's time
            uint64_t end = time / timebase_ratio;
            while ( mach_absolute_time() < end );
            
#ifdef kAnalyzeTiming
            double discrepancy = (double)(mach_absolute_time()*timebase_ratio) - time;
            printf("LxPreciseTimeDelay fired: %lfs time discrepancy\n", discrepancy);
#endif
            
            // Perform action
#if NS_BLOCKS_AVAILABLE
            void (^block)(void) = [nextEvent objectForKey:kBlockKey];
            if ( block ) {
                block();
            } else {
#endif
            id target = [nextEvent objectForKey:kTargetKey];
            SEL selector = NSSelectorFromString([nextEvent objectForKey:kSelectorKey]);
            if ( [nextEvent objectForKey:kArgumentKey] ) {
                [target performSelector:selector withObject:[nextEvent objectForKey:kArgumentKey]];
            } else {
                [target performSelector:selector];
            }
#if NS_BLOCKS_AVAILABLE
            }
#endif
            
            [condition lock];
            [events removeObject:nextEvent];
        } else {
            [condition lock];
        }
    }
}

@end

