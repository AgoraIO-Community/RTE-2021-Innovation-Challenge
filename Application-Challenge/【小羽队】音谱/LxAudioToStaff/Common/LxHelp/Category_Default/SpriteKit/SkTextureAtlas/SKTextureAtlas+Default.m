//
//  SKTextureAtlas+Default.m
//  SmartPiano
//
//  Created by DavinLee on 2017/12/26.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "SKTextureAtlas+Default.h"

@implementation SKTextureAtlas (Default)
#pragma mark - GetMethod
/**
 *@description 获取对应图集内的所有纹理
 *@param atlasName 图集名称
 *@param prefixName 纹理前缀
 *@param count  纹理数量
 **/
+ (NSArray *)lx_getTexturesWithAtlasName:(NSString *)atlasName
                              prefixName:(NSString *)prefixName
                                   count:(NSInteger)count
{
   
    @autoreleasepool {
        SKTextureAtlas *atlas = [SKTextureAtlas atlasNamed:atlasName];
        NSMutableArray *textures = [[NSMutableArray alloc] initWithCapacity:count];
        for (int i = 0; i < count; i ++) {
            SKTexture *texture = [atlas textureNamed:[NSString stringWithFormat:@"%@%d",prefixName,i]];
            [textures addObject:texture];
        }
         return [NSArray arrayWithArray:textures];
    }

}

/**
 *@description 获取对应图集内的所有纹理
 *@param atlasName 图集名称
 *@param prefixName 纹理前缀
 *@param from 起始index
 *@param to 结束index
 **/
+ (NSArray *)lx_getTexturesWithAtlasName:(NSString *)atlasName
                              prefixName:(NSString *)prefixName
                               fromIndex:(NSInteger)from
                                 toIndex:(NSInteger)to{
    @autoreleasepool {
            SKTextureAtlas *atlas = [SKTextureAtlas atlasNamed:atlasName];
            NSMutableArray *textures = [[NSMutableArray alloc] initWithCapacity:(to - from) + 1];
            for (int i = (int)from; i < (int)(to - from + 1); i ++) {
                SKTexture *texture = [atlas textureNamed:[NSString stringWithFormat:@"%@%d@2x",prefixName,i]];
                [textures addObject:texture];
            }
            return [NSArray arrayWithArray:textures];
    }
    
}

/** LxInterface 获取对应图集纹理
 */
+ (SKTexture *)lx_getTextureWithAtlasName:(NSString *)atlasName
                               textureName:(NSString *)textureName{
    @autoreleasepool {
        SKTextureAtlas *atlas = [SKTextureAtlas atlasNamed:atlasName];
              return [atlas textureNamed:textureName];
    }
   
    
}

/**
 *@description 获取对应图集内的所有纹理
 *@param prefixName 纹理前缀
 *@param from 起始index
 *@param to 结束index
 **/
- (NSArray *)lx_getTexturesWithPrefixName:(NSString *)prefixName
                               fromIndex:(NSInteger)from
                                  toIndex:(NSInteger)to{
    @autoreleasepool {
            NSMutableArray *textures = [[NSMutableArray alloc] initWithCapacity:(to - from) + 1];
            for (int i = (int)from; i < to  + 1; i ++) {
                SKTexture *texture = [self textureNamed:[NSString stringWithFormat:@"%@%d@2x",prefixName,i]];
                [textures addObject:texture];
            }
            return [NSArray arrayWithArray:textures];
    }
   
}

/** LxInterface 获取对应图集纹理

 */
- (SKTexture *)lx_getTextureWithTextureName:(NSString *)textureName{
     return [self textureNamed:textureName];
}

@end
