//
//  BBDataFromJson.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/24.
//

#import "BBDataFromJson.h"
#import "BBMessageModel.h"
@implementation BBDataFromJson

+ (NSArray *)getdatafromJson{
    NSMutableArray * arr = [NSMutableArray array];
    NSString * filepath = [[NSBundle mainBundle] pathForResource:@"babybackhomejson" ofType:@"txt"];
    NSString * result = [NSString stringWithContentsOfFile:filepath encoding:(NSUTF8StringEncoding) error:nil];
    NSData *jsonData = [result dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary * dic = [NSJSONSerialization JSONObjectWithData:jsonData
                          
                                                         options:NSJSONReadingMutableContainers
                          
                                                           error:nil];
    NSArray * resultArr = dic[@"people"];
    for (int i = 0 ; i < resultArr.count; i++) {
        BBMessageModel * model = [[BBMessageModel alloc] init];
        [model setValuesForKeysWithDictionary:resultArr[i]];
        [arr addObject:model];
    }
    
    return arr;
}



@end
