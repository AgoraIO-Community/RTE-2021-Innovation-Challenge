//
//  ClassMainViewController.h
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/14.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "BaceViewController.h"

@interface ClassMainViewController : BaceViewController
@property(nonatomic,strong) NSString *classID;
@property(nonatomic,strong) NSString *titles;
@property(nonatomic,assign) NSInteger buttonTag;
@property(nonatomic,assign) BOOL isLocal;

@end
