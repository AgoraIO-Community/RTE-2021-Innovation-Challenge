//
//  NSArray+Helper.h
//  svgtest2
//
//  Created by 李翔 on 2017/4/25.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSArray (Helper)
/**
 *@description 获取字符
 **/
- (NSString *)lx_JsonString;
/**
 *@description 确认所有元素为指定类型
 **/
- (BOOL)lx_checkElementsMicClass:(Class)aClass;
/**
 *@description 获取打乱的一个数组元素
 **/
- (NSMutableArray *)lx_randomElements;
/**
 *@description 获取元素，若超限，则返回nil
 **/
- (id)lx_objectForIndex:(NSInteger)index;

@end
