//
//  LxAvPlayerLayer.h
//  PianoBridgeHD
//
//  Created by DavinLee on 2017/9/21.
//  Copyright © 2017年 Mason. All rights reserved.
//

#import <AVFoundation/AVFoundation.h>

typedef NS_ENUM(NSInteger,LxplayerFunction)
{
    LxplayerPause,
    LxplayerPlay,
    LxplayerReset,
};

@interface LxAvPlayerLayer : AVPlayerLayer

/**
 *@description 获取实例对象
 *@param path 本地视频文件路径
 **/
+ (LxAvPlayerLayer *)playerLayerWithPath:(NSString *)path;


/**
 *@description 开始播放
 **/
- (void)play;

/**
 *@description 暂停播放
 **/
- (void)pause;

/**
 *@description 重置
 **/
- (void)reset;
/**
 *@description 设置播放速率
 **/
- (void)seekRate:(float)rate;

@end
