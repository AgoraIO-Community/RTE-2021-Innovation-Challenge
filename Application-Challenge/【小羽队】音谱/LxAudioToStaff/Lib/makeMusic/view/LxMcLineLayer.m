//
//  LxMcLineLayer.m
//  SmartPiano
//
//  Created by DavinLee on 2019/4/30.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcLineLayer.h"

@implementation LxMcLineLayer
#pragma mark - ************************CallFunction************************
/**   获取默认layer    **/
+ (LxMcLineLayer *)lx_defaultLineLayer{
    LxMcLineLayer *layer = [LxMcLineLayer layer];
    layer.backgroundColor = [UIColor clearColor].CGColor;
    layer.fillColor = [UIColor blackColor].CGColor;
    layer.strokeColor = [UIColor blackColor].CGColor;
    return layer;
}



@end
