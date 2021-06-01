//
//  BBMessageModel.h
//  Baby back home
//
//  Created by zhangyu on 2021/5/14.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BBMessageModel : NSObject
@property(copy,nonatomic)NSString * messageid;
@property(copy,nonatomic)NSString * userid;
@property(copy,nonatomic)NSString * title;
@property(copy,nonatomic)NSString * des;
@property(copy,nonatomic)NSString * address;
@property(copy,nonatomic)NSString * phoneNum;
@property(copy,nonatomic)NSString * imgName;
@property(assign,nonatomic)NSInteger timestamp;
@property(copy,nonatomic)NSString * pName;
@property(copy,nonatomic)NSString * pGender;

@end

NS_ASSUME_NONNULL_END
