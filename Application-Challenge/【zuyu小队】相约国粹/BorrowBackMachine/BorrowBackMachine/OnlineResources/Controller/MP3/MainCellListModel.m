//
//  MainCellListModel.m
//  CNCLibraryScan
//
//  Created by zuyu on 2018/9/17.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MainCellListModel.h"

@implementation MainCellListModel


-(void)setImageName:(NSString *)imageName
{
    if ([imageName isKindOfClass:[NSNull class]]) {
        _imageName = @"";
    }else{
        _imageName = imageName;
    }
}

-(void)setAuthor:(NSString *)author
{
    if ([author isKindOfClass:[NSNull class]] || author == nil || [author isEqualToString:@"(null)"] || !author.length) {
        _author = @"";
    }else{
        _author = author;
    }
}

-(void)setVolumeCount:(NSString *)VolumeCount
{
    if ([VolumeCount isKindOfClass:[NSNull class]] || VolumeCount == nil || [VolumeCount isEqualToString:@"(null)"] || !VolumeCount.length) {
        _VolumeCount = @"";
    }else{
        _VolumeCount = VolumeCount;
    }
}

-(void)setCreatedOn:(NSString *)CreatedOn
{
    _CreatedOn = [CreatedOn stringByReplacingOccurrencesOfString:@"T" withString:@"  "];
}

@end
