//
//  BookselfModel.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BookselfModel : NSObject
@property(nonatomic,strong) NSString *Id;
@property(nonatomic,strong) NSString *BookCode;
@property(nonatomic,strong) NSString *BookName;
@property(nonatomic,strong) NSString *InteBookId;
@property(nonatomic,strong) NSString *EquipmentId;
@property(nonatomic,strong) NSString *PositionCode;
@property(nonatomic,strong) NSString *BookStatus;
@property(nonatomic,strong) NSString *BookStatusName;
@property(nonatomic,strong) NSString *IsBespeak;
@property(nonatomic,strong) NSString *BorrowNum;
@property(nonatomic,strong) NSString *BespeakNum;
@property(nonatomic,strong) NSString *CreateTime;
@property(nonatomic,strong) NSString *CreateId;
@property(nonatomic,strong) NSString *LogoUrl;
@property(nonatomic,strong) NSString *Author;




@end
