//
//  EasyBookselfModel.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/23.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface EasyBookselfModel : NSObject
@property(nonatomic,strong) NSString *Id;
@property(nonatomic,strong) NSString *BookCode;
@property(nonatomic,strong) NSString *BookName;
@property(nonatomic,strong) NSString *Author;
@property(nonatomic,strong) NSString *PublishCompany;
@property(nonatomic,strong) NSString *LogoUrl;
@property(nonatomic,strong) NSString *Describe;
@property(nonatomic,strong) NSString *InteBookId;
@property(nonatomic,strong) NSString *EquipmentId;
@property(nonatomic,strong) NSString *EquipmentName;
@property(nonatomic,strong) NSString *EquipmentCode;
@property(nonatomic,strong) NSString *PositionCode;
@property(nonatomic,strong) NSString *BookStatus;
@property(nonatomic,strong) NSString *IsBespeak;
@property(nonatomic,strong) NSString *BorrowNum;
@property(nonatomic,strong) NSString *BespeakNum;

@end
