//
//  YYSpriteSheetImage+Default.h
//  SmartPiano
//
//  Created by DavinLee on 2018/2/7.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <YYKit/YYKit.h>
typedef NS_ENUM(NSInteger,LxSpriteImageCutDirection)
{
    LxSpriteImageCutHor = 0,
    LxSpriteImageCutVer = 1,
};
@interface YYSpriteSheetImage (Default)
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
                                             loopCount:(NSInteger)loopCount;

+ (YYSpriteSheetImage *)lx_defaultSpriteImageWithImage:(UIImage *)image
                                              horCount:(NSInteger)horCount
                                               verCout:(NSInteger)verCout
                                       spriteDirection:(LxSpriteImageCutDirection)spriteDirection
                                              duration:(NSTimeInterval)duration
                                             loopCount:(NSInteger)loopCount ;

@end
