//
//  LxLibrary.m
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/21.
//

#import "LxLibrary.h"

@interface LxLibrary()


@end
@implementation LxLibrary
#pragma mark - ************************GetMethod************************
/** Lx description   单例方法  **/
+ (LxLibrary *)sharedInstance{
    static LxLibrary *_sharedInstance = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedInstance = [[LxLibrary alloc] init];
    });
    return _sharedInstance;
}



@end
