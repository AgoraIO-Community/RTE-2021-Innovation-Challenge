//
//  MainSecondViewClassCellModel.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/11/2.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "MainSecondViewClassCellModel.h"

@implementation MainSecondViewClassCellModel
-(void)setImageName:(NSString *)imageName
{
   
    
    if ([imageName isEqualToString:@"<null>"] || [imageName isKindOfClass:[NSNull class]]) {
        _imageName = @"";
    }
}
@end
