//
//  LxLibrary.h
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/21.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LxLibrary : NSObject
/** Lx description   单例方法  **/
+ (LxLibrary *)sharedInstance;
#pragma mark - ************************持有属性************************
@property (weak, nonatomic) UIWindow *keyWindow;

@end

NS_ASSUME_NONNULL_END
