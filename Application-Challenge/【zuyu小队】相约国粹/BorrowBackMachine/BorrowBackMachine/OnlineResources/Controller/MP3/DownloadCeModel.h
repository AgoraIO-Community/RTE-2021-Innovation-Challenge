//
//  DownloadCeModel.h
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/12.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DownloadCeModel : NSObject
@property(nonatomic,strong) NSString *BookID;
@property(nonatomic,strong) NSString *BookName;
@property(nonatomic,strong) NSString *BookType;
@property(nonatomic,strong) NSString *CoverImageUrl;
@property(nonatomic,strong) NSString *HasContent;
@property(nonatomic,strong) NSString *ID;
@property(nonatomic,strong) NSString *Name;
@property(nonatomic,strong) NSString *ResourceDuration;
@property(nonatomic,strong) NSString *ResourceSize;
@property(nonatomic,strong) NSString *ResourceType;
@property(nonatomic,strong) NSString *ResourceUrl;
@property(nonatomic,strong) NSString *ViewCount;
@property(nonatomic,strong) NSString *VolumnNo;

@end
