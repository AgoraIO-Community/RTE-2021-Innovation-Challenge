//
//  SKTextureAtlas+Default.h
//  SmartPiano
//
//  Created by DavinLee on 2017/12/26.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <SpriteKit/SpriteKit.h>

@interface SKTextureAtlas (Default)
/**
 *@description 获取对应图集内的所有纹理
 *@param atlasName 图集名称
 *@param prefixName 纹理前缀
 *@param count  纹理数量
 **/
+ (NSArray *)lx_getTexturesWithAtlasName:(NSString *)atlasName
                              prefixName:(NSString *)prefixName
                                   count:(NSInteger)count;
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
                                 toIndex:(NSInteger)to;

/** LxInterface 获取对应图集纹理
 */
+ (SKTexture *)lx_getTextureWithAtlasName:(NSString *)atlasName
                               textureName:(NSString *)textureName;


/**
 *@description 获取对应图集内的所有纹理
 *@param prefixName 纹理前缀
 *@param from 起始index
 *@param to 结束index
 **/
- (NSArray *)lx_getTexturesWithPrefixName:(NSString *)prefixName
                               fromIndex:(NSInteger)from
                                 toIndex:(NSInteger)to;

/** LxInterface 获取对应图集纹理
 */
- (SKTexture *)lx_getTextureWithTextureName:(NSString *)textureName;
@end
