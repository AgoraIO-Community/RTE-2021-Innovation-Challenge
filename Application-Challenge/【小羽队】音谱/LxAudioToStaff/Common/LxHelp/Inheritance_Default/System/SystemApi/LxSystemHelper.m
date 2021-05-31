//
//  LxSystemHelper.m
//  SmartPiano
//
//  Created by DavinLee on 2018/5/11.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "LxSystemHelper.h"
#import <sys/sysctl.h>
#import <sys/utsname.h>

@implementation LxSystemHelper
/** Lx description   获取设备原始型号  **/
+ (NSString *)lx_deviceName{
    static dispatch_once_t one;
    static NSString *model;
    dispatch_once(&one, ^{
        size_t size;
        sysctlbyname("hw.machine", NULL, &size, NULL, 0);
        char *machine = malloc(size);
        sysctlbyname("hw.machine", machine, &size, NULL, 0);
        model = [NSString stringWithUTF8String:machine];
        free(machine);
    });
    return model;
}
@end
