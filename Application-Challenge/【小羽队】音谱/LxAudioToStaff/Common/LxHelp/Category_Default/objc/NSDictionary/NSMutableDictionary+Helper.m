//
//  NSMutableDictionary+Helper.m
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/24.
//

#import "NSMutableDictionary+Helper.h"

@implementation NSMutableDictionary (Helper)
/**
 *@description 加入元素（避免空值或Null)
 **/
- (void)lx_setSafityObject:(id)object forKey:(NSString *)key{

        if (object != nil && ![object isKindOfClass:[NSNull class]]) {
            [self setObject:object forKey:key];
        }
}
@end
