//
//  NSArray+Helper.m
//  svgtest2
//
//  Created by 李翔 on 2017/4/25.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import "NSArray+Helper.h"
#import <objc/runtime.h>
@implementation NSObject (Swizzling)

+ (BOOL)gl_swizzleMethod:(SEL)origSel withMethod:(SEL)altSel {
    Method origMethod = class_getInstanceMethod(self, origSel);
    Method altMethod = class_getInstanceMethod(self, altSel);
    if (!origMethod || !altMethod) {
        return NO;
    }
    class_addMethod(self,
                    origSel,
                    class_getMethodImplementation(self, origSel),
                    method_getTypeEncoding(origMethod));
    class_addMethod(self,
                    altSel,
                    class_getMethodImplementation(self, altSel),
                    method_getTypeEncoding(altMethod));
    method_exchangeImplementations(class_getInstanceMethod(self, origSel),
                                   class_getInstanceMethod(self, altSel));
    return YES;
}

+ (BOOL)gl_swizzleClassMethod:(SEL)origSel withMethod:(SEL)altSel {
    return [object_getClass((id)self) gl_swizzleMethod:origSel withMethod:altSel];
}
@end
@implementation NSArray (Helper)
#pragma mark - GetMethod
//获取jsonStr
- (NSString *)lx_JsonString;
{
    NSError *error;
    NSData *data = [NSJSONSerialization dataWithJSONObject:self
                                                   options:NSJSONWritingPrettyPrinted
                                                     error:&error];
    if (error) {
        return @"error";
    }
    NSString *jsonStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    return jsonStr;
}

//确认所有元素为指定类型
- (BOOL)lx_checkElementsMicClass:(Class)aClass
{
    for (id object in self) {
        if (![object isKindOfClass:aClass]) {
            return NO;
        }
    }
    return YES;
}
/**
 *@description 获取打乱的一个数组元素
 **/
- (NSMutableArray *)lx_randomElements
{
    
    NSMutableArray * tempArray = [NSMutableArray arrayWithArray:[self sortedArrayUsingComparator:^NSComparisonResult(id str1, id str2) {
        int seed = arc4random_uniform(2);
        if (seed) {
            return [str1 compare:str2];
        } else {
            return [str2 compare:str1];
        }
    }]];
    return tempArray;
}
/**
 *@description 获取元素，若超限，则返回nil
 **/
- (id)lx_objectForIndex:(NSInteger)index
{
    if (index > self.count || self.count == 0) {
        debugLog(@"出现超限数组index = %ld  数组内容%@",index,self);
        return nil;
    }else
    {
        return self[index];
    }
}

+ (void)load{
    
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        
        
        
        // NSArray 是一个类簇,具体有三个子类__NSArray0,__NSSingleObjectArrayI,__NSArrayI,
        
        // 还有一个__NSPlaceholderArray是占位的,不实际使用
        
        
        
        // 对__NSArray0,__NSSingleObjectArrayI来说,下面三种调用的同一个方法objectAtIndex
        
        
        
        /** 对__NSArrayI,__NSArrayM来说,objectAtIndex 和 objectAtIndexedSubscript 有不同的实现,
         
         array[22]调用了objectAtIndexedSubscript */
        
        //        [array objectAtIndex:22];
        
        //        [array objectAtIndexedSubscript:22];
        
        //        array[22];
        
        
        
        [objc_getClass("__NSArray0") gl_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(emptyObjectIndex:)];
        
        [objc_getClass("__NSSingleObjectArrayI") gl_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(singleObjectIndex:)];
        
        
        
        [objc_getClass("__NSArrayI") gl_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(safe_arrObjectIndex:)];
        
        [objc_getClass("__NSArrayI") gl_swizzleMethod:@selector(objectAtIndexedSubscript:) withMethod:@selector(safe_objectAtIndexedSubscript:)];
        
        
        
        
        
        
        
        [objc_getClass("__NSArrayM") gl_swizzleMethod:@selector(objectAtIndex:) withMethod:@selector(safeObjectIndex:)];
        
        [objc_getClass("__NSArrayM") gl_swizzleMethod:@selector(objectAtIndexedSubscript:) withMethod:@selector(mutableArray_safe_objectAtIndexedSubscript:)];
        
        
        
        [objc_getClass("__NSArrayM") gl_swizzleMethod:@selector(insertObject:atIndex:) withMethod:@selector(safeInsertObject:atIndex:)];
        
        [objc_getClass("__NSArrayM") gl_swizzleMethod:@selector(addObject:) withMethod:@selector(safeAddObject:)];
        
        
        
        
        
    });
    
    
    
}





- (id)emptyObjectIndex:(NSInteger)index {
    
    debugLog(@"__NSArray0 取一个空数组 objectAtIndex , 崩溃") ;
    
    return nil;
    
}

- (id)singleObjectIndex:(NSInteger)index {
    
    if (index >= self.count || index < 0) {
        
        debugLog(@"__NSSingleObjectArrayI 取一个不可变单元素数组时越界 objectAtIndex , 崩溃") ;
        
        return nil;
        
    }
    
    return [self singleObjectIndex:index];
    
}



- (id)safe_arrObjectIndex:(NSInteger)index{
    
    
    
    if (index >= self.count || index < 0) {
        
        debugLog(@"__NSArrayI 取不可变数组时越界 objectAtIndex , 崩溃") ;
        
        return nil;
        
    }
    
    return [self safe_arrObjectIndex:index];
    
    
    
}

- (id)safe_objectAtIndexedSubscript:(NSInteger)index{
    
    
    
    if (index >= self.count || index < 0) {
        
        debugLog(@"__NSArrayI 取不可变数组时越界 objectAtIndexedSubscript , 崩溃") ;
        
        return nil;
        
    }
    
    return [self safe_objectAtIndexedSubscript:index];
    
    
    
}

- (id)mutableArray_safe_objectAtIndexedSubscript:(NSInteger)index{
    
    
    
    if (index >= self.count || index < 0) {
        
        debugLog(@"__NSArrayM 取可变数组时越界 objectAtIndexedSubscript , 崩溃") ;
        
        return nil;
        
    }
    
    return [self mutableArray_safe_objectAtIndexedSubscript:index];
    
    
    
}





- (id)safeObjectIndex:(NSInteger)index{

    if (index >= self.count || index < 0) {

        debugLog(@"__NSArrayM 取可变数组时越界 objectAtIndex , 崩溃") ;
        
        return nil;
        
    }
    
    return [self safeObjectIndex:index];
    
    
    
}







- (void)safeInsertObject:(id)object atIndex:(NSUInteger)index{
    
    
    
    if (index>self.count) {
        
        debugLog(@"__NSArrayM 添加元素越界 insertObject:atIndex: , 崩溃") ;
        
        return ;
        
    }
    
    if (object == nil) {
        
        debugLog(@"__NSArrayM 添加空元素 insertObject:atIndex: , 崩溃") ;
        
        return ;
        
    }
    
    
    
    [self safeInsertObject:object atIndex:index];
    
    
    
}

- (void)safeAddObject:(id)object {
    
    
    
    if (object == nil) {
        
        debugLog(@"__NSArrayM 添加空元素 addObject , 崩溃") ;
        
        return ;
        
    }
    
    
    
    [self safeAddObject:object];
    
    
    
}

@end
