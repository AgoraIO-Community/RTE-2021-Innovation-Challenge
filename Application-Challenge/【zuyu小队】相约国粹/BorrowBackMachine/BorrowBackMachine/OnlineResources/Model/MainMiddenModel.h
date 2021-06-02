//
//  MainMiddenModel.h
//  CNCLibraryScan
//
//  Created by zuyu on 2018/9/17.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MainMiddenModel : NSObject
@property (strong, nonatomic)  NSString *lableTitle;
@property (strong, nonatomic)  NSString *imageName;
@property (strong, nonatomic)  NSString *resourceType;
@property (strong, nonatomic)  NSString *ResourceUrl;

@property (strong, nonatomic)  NSString *MenuID;

@property (strong, nonatomic)  NSString *Author;
@property (strong, nonatomic)  NSString *BookTypeName;

@property (strong, nonatomic)  NSString *CollectCount;

@property (strong, nonatomic)  NSString *ViewCount;

@property (strong, nonatomic)  NSString *VolumeCount;

@property (strong, nonatomic)  NSString *Summary;
@property (strong, nonatomic)  NSString *ID;

@end
