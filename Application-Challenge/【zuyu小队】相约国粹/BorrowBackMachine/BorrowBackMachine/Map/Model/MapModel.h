//
//  MapModel.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MapModel : NSObject
@property(nonatomic,assign) float jingdu;
@property(nonatomic,assign) float weidu;
@property(nonatomic,strong) NSString *ID;
@property(nonatomic,strong) NSString *title;
@property(nonatomic,strong) NSString *subtitle;
@property(nonatomic,strong) NSString *Id;
@property(nonatomic,strong) NSString *EquipmentTypeId;
@property(nonatomic,strong) NSString *EquipmentTypeName;
@property(nonatomic,strong) NSString *EquipmentName;
@property(nonatomic,strong) NSString *EquipmentCode;
@property(nonatomic,strong) NSString *EquipmentAddress;
@property(nonatomic,strong) NSString *Remark;
@property(nonatomic,strong) NSString *Status;
@property(nonatomic,strong) NSString *StatusName;
@property(nonatomic,strong) NSString *ControlBoardComPort;
@property(nonatomic,strong) NSString *RfidComPort;
@property(nonatomic,strong) NSString *CreateTime;
@property(nonatomic,strong) NSString *CreateId;





@end
