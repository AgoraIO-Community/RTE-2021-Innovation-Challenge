//
//  NSMutableDictionary+Helper.h
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/24.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSMutableDictionary (Helper)
/**
 *@description 加入元素（避免空值或Null)
 **/
- (void)lx_setSafityObject:(id)object forKey:(NSString *)key;
@end

NS_ASSUME_NONNULL_END
