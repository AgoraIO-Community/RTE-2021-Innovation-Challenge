//
//  DownloadData.h
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/12.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DownloadData : NSObject
@property(nonatomic,strong) NSString *Author;
@property(nonatomic,strong) NSString *BookType;
@property(nonatomic,strong) NSString *BookTypeName;
@property(nonatomic,strong) NSString *Class1;
@property(nonatomic,strong) NSString *Class2;
@property(nonatomic,strong) NSString *CollectCount;
@property(nonatomic,strong) NSString *CoverImageUrl;
@property(nonatomic,strong) NSString *CreatedOn;
@property(nonatomic,strong) NSString *HasContent;
@property(nonatomic,strong) NSString *ID;
@property(nonatomic,strong) NSString *MenuID;
@property(nonatomic,strong) NSString *Name;
@property(nonatomic,strong) NSString *RecommendType;
@property(nonatomic,strong) NSString *ResourceType;
@property(nonatomic,strong) NSString *ResourceUrl;
@property(nonatomic,strong) NSString *Summary;
@property(nonatomic,strong) NSString *Tag;
@property(nonatomic,strong) NSString *ViewCount;
@property(nonatomic,strong) NSString *VolumeCount;
@property(nonatomic,strong) NSArray *Volumes;

@end
