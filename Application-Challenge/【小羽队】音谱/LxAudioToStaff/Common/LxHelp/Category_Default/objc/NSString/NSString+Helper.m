//
//  NSString+Helper.m
//  svgtest2
//
//  Created by 李翔 on 2017/4/25.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import "NSString+Helper.h"

@implementation NSString (Helper)
#pragma mark - GetMethod
//字符转数组
- (NSMutableArray *)lx_getArray;
{
    if (self.length > 0) {
        NSData *data = [self dataUsingEncoding:NSUTF8StringEncoding];
        NSError *error ;
        NSArray *array = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions
                                                           error:&error];
        if (array.count >0 ) {
            return [NSMutableArray arrayWithArray:array];
        }
    }
    return [NSMutableArray new];
}

//字符转字典
- (NSMutableDictionary *)lx_getDictionary;
{
    if (self.length > 0) {
        NSData *data = [self dataUsingEncoding:NSUTF8StringEncoding];
        NSError *error ;
        NSDictionary *info = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions
                                                           error:&error];
        if (info) {
            return [NSMutableDictionary dictionaryWithDictionary:info];
        }
    }
    return [NSMutableDictionary new];
}

/** Lx description   确保文件路径不包含"/"  **/
+ (NSString *)lx_checkPathSymbolWithStr:(NSString *)str{
    if ([str containsString:@"/"]) {
        return [str stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
    }else{
        return str;
    }
}

//获取本地cache目录
+ (NSString *)lx_cacheFolderPath
{
     return [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
}

/**
 *@description 获取字符串的内容大小
 **/
- (CGSize)lx_textSizeWithFont:(UIFont *)font MaxSize:(CGSize)maxSize
{
    
    NSDictionary *dic = @{NSFontAttributeName : font};

    CGRect rect = [self boundingRectWithSize:maxSize
                                     options:NSStringDrawingUsesLineFragmentOrigin
                                  attributes:dic
                                     context:nil];
    return rect.size;
}
/** 确保没有导致崩溃的字符 **/
- (NSString *)lx_SafeStrWithStr
{
    NSString *tempStr = [self copy];
    if (tempStr == nil || [tempStr isKindOfClass:[NSNull class]]) {
        debugLog(@"出现了空字符");
        tempStr = @"";
    }else if (![self isKindOfClass:[NSString class]]){
        tempStr = [NSString stringWithFormat:@"%ld",(long)self.integerValue];
    }
    return tempStr;
}

/** *********************************************   进制转换  *********************************************  **/
/** 十六进制转十进制 **/
- (NSInteger)lx_decimalFromHexadecimal{
//    NSString * temp10 = [NSString stringWithFormat:@"%lu",strtoul([self UTF8String],0,16)];
     NSString * dec = [NSString stringWithFormat:@"%ld",strtoul([[NSString stringWithFormat:@"0x%@",self] UTF8String],0,16)];
    return [dec integerValue];
}
+ (NSString *)lx_reCalculateByteLength:(double)byteLength{
    double convertedValue = byteLength;
       int multiplyFactor = 0;
       NSArray *tokens = [NSArray arrayWithObjects:@"bytes",@"KB",@"MB",@"GB",@"TB",nil];
    
       while (convertedValue > 1024) {
           convertedValue /= 1024;
           multiplyFactor++;
       }
    
       return [NSString stringWithFormat:@"%4.2f%@",convertedValue, [tokens objectAtIndex:multiplyFactor]];
    
}

+ (NSString *)lx_currentDateStr{
    NSDate *currentDate = [NSDate date];//获取当前时间，日期
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];// 创建一个时间格式化对象
    [dateFormatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];//设定时间格式,这里可以设置成自己需要的格式
    NSString *dateString = [dateFormatter stringFromDate:currentDate];//将时间转化成字符串
    return dateString;
}

- (NSString *)lx_firstLetter
{
    //转成了可变字符串
    NSMutableString *str = [NSMutableString stringWithString:self];
    //先转换为带声调的拼音
    CFStringTransform((CFMutableStringRef)str,NULL, kCFStringTransformMandarinLatin,NO);
    //再转换为不带声调的拼音
    CFStringTransform((CFMutableStringRef)str,NULL, kCFStringTransformStripDiacritics,NO);
    //转化为大写拼音
    NSString *pinYin = [str capitalizedString];
    //获取并返回首字母
    return [pinYin substringToIndex:1];
}
@end
