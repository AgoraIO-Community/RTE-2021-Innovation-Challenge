//
//  EntityResourcesModel.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/21.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface EntityResourcesModel : NSObject
@property(nonatomic,strong) NSString *BookName;
@property(nonatomic,strong) NSString *BookWriter;
@property(nonatomic,strong) NSString *Id;
@property(nonatomic,strong) NSString *PublishCompany;
@property(nonatomic,strong) NSString *LogoUrl;
@property(nonatomic,strong) NSString *Describe;
@property(nonatomic,strong) NSString *BespeakCount;
@property(nonatomic,strong) NSString *BorrowCount;
@property(nonatomic,strong) NSString *ResourceUrl;
@property(nonatomic,strong) NSString *CreateTime;
@property(nonatomic,strong) NSString *CreateId;


@end
