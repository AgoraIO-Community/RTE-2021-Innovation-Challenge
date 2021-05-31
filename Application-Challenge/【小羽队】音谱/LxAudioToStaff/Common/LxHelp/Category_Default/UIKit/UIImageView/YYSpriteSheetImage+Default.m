//
//  YYSpriteSheetImage+Default.m
//  SmartPiano
//
//  Created by DavinLee on 2018/2/7.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "YYSpriteSheetImage+Default.h"

@implementation YYSpriteSheetImage (Default)
/**
 *@description 获取默认设置精灵图片
 *@param image 图集
 *@param spriteCount 图集元素数量
 *@param spriteDirection 图集方向
 *@param duration 动画总时间
 *@param loopCount 动画次数
 **/
+ (YYSpriteSheetImage *)lx_defaultSpriteImageWithImage:(UIImage *)image
                                           spriteCount:(NSInteger)spriteCount
                                       spriteDirection:(LxSpriteImageCutDirection)spriteDirection
                                              duration:(NSTimeInterval)duration
                                             loopCount:(NSInteger)loopCount
{
    CGFloat elementWidth;
    CGFloat elementHeight;
    CGFloat horOffset;
    CGFloat verOffset;
    switch (spriteDirection) {
        case LxSpriteImageCutHor:
        {
            elementWidth = image.size.width / spriteCount;
            elementHeight = image.size.height;
            horOffset = elementWidth;
            verOffset = 0;
        }
            break;
            case LxSpriteImageCutVer:
        {
            elementWidth = image.size.width;
            elementHeight = image.size.height / spriteCount;
            horOffset = 0;
            verOffset = elementHeight;
        }
            break;
            
        default:
            break;
    }
    NSMutableArray *contentRects = [[NSMutableArray alloc] initWithCapacity:spriteCount];
    NSMutableArray *durations = [[NSMutableArray alloc] initWithCapacity:spriteCount];
    CGPoint origin = CGPointZero;
    for (int i = 0 ; i < spriteCount; i ++) {
        CGRect rect;
        rect.size = CGSizeMake(elementWidth, elementHeight);
        rect.origin = origin;
        [contentRects addObject:[NSValue valueWithCGRect:rect]];
        [durations addObject:[NSNumber numberWithFloat:duration / spriteCount]];
        origin.x += horOffset;
        origin.y += verOffset;
    }
    YYSpriteSheetImage *sheetImage = [[YYSpriteSheetImage alloc] initWithSpriteSheetImage:image
                                                                             contentRects:contentRects
                                                                           frameDurations:durations
                                                                                loopCount:loopCount];
    
    return sheetImage;
}

+ (YYSpriteSheetImage *)lx_defaultSpriteImageWithImage:(UIImage *)image
                                              horCount:(NSInteger)horCount
                                               verCout:(NSInteger)verCout
                                       spriteDirection:(LxSpriteImageCutDirection)spriteDirection
                                              duration:(NSTimeInterval)duration
                                             loopCount:(NSInteger)loopCount {
    CGFloat elementWidth;
    CGFloat elementHeight;
    CGFloat horOffset;
    CGFloat verOffset;
    NSInteger spriteCount = verCout * horCount;
    elementWidth = image.size.width / horCount;
    elementHeight = image.size.height / verCout;
    switch (spriteDirection) {
        case LxSpriteImageCutHor:
        {
            
            horOffset = elementWidth;
            verOffset = 0;
        }
            break;
        case LxSpriteImageCutVer:
        {
            horOffset = 0;
            verOffset = elementHeight;
        }
            break;
            
        default:
            break;
    }
    NSMutableArray *contentRects = [[NSMutableArray alloc] initWithCapacity:spriteCount];
    NSMutableArray *durations = [[NSMutableArray alloc] initWithCapacity:spriteCount];
    CGPoint origin = CGPointZero;
    for (int i = 0 ; i < spriteCount; i ++) {
        CGRect rect;
        rect.size = CGSizeMake(elementWidth, elementHeight);
        rect.origin = origin;
        [contentRects addObject:[NSValue valueWithCGRect:rect]];
        [durations addObject:[NSNumber numberWithFloat:duration / spriteCount]];
        origin.x += horOffset;
        origin.y += verOffset;
        if (origin.x >= image.size.width) {
            origin.x = 0;
            origin.y += elementHeight;
        }
    }
    YYSpriteSheetImage *sheetImage = [[YYSpriteSheetImage alloc] initWithSpriteSheetImage:image
                                                                             contentRects:contentRects
                                                                           frameDurations:durations
                                                                                loopCount:loopCount];
    
    return sheetImage;
}


@end
