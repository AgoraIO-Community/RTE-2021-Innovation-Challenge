//
//  NSString+Helper.h
//  svgtest2
//
//  Created by 李翔 on 2017/4/25.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface NSString (Helper)
/***********************************get************************************///
/**
 *@description 获取数组
 **/
- (NSMutableArray *)lx_getArray;

/**N
 *@description 获取字典
 **/
- (NSMutableDictionary *)lx_getDictionary;

+ (NSString *)lx_checkPathSymbolWithStr:(NSString *)str;

/** Lx description   获取当前时间  **/
+ (NSString *)lx_currentDateStr;
/**
 *@description 获取本地cache目录
 **/
+ (NSString *)lx_cacheFolderPath;
/** 确保没有导致崩溃的字符 **/
- (NSString *)lx_SafeStrWithStr;

/***********************************文本************************************///
/**
 *@description 获取字符串的内容大小
 **/
- (CGSize)lx_textSizeWithFont:(UIFont *)font MaxSize:(CGSize)maxSize;
/** *********************************************   进制转换  *********************************************  **/
/** 十六进制转十进制 **/
- (NSInteger)lx_decimalFromHexadecimal;
/** Lx description   获取文件大小（b ->kb ->mb)  **/
+ (NSString *)lx_reCalculateByteLength:(double)byteLength;
/** Lx description   获取汉字首字母  **/
- (NSString *)lx_firstLetter;
@end
