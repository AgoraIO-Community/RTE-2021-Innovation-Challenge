//
//  EntityEasyMachineModel.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/23.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface EntityEasyMachineModel : NSObject
@property(nonatomic,strong) NSString *CreateId;
@property(nonatomic,strong) NSString *CreateTime;
@property(nonatomic,strong) NSString *Id;
@property(nonatomic,strong) NSString *SchoolId;
@property(nonatomic,strong) NSString *VenueAddress;
@property(nonatomic,strong) NSString *VenueName;
@property(nonatomic,assign) float  jingdu;
@property(nonatomic,assign) float  weidu;



@end
