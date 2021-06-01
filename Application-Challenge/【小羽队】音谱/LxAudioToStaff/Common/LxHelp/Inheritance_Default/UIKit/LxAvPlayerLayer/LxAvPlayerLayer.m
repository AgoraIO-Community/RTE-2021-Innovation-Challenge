//
//  LxAvPlayerLayer.m
//  PianoBridgeHD
//
//  Created by DavinLee on 2017/9/21.
//  Copyright © 2017年 Mason. All rights reserved.
//

#import "LxAvPlayerLayer.h"
@implementation LxAvPlayerLayer

#pragma mark - GetMethod
/** 获取实例对象 **/
+ (LxAvPlayerLayer *)playerLayerWithPath:(NSString *)path
{
    AVPlayer *player = [AVPlayer playerWithURL:[NSURL fileURLWithPath:path]];
    LxAvPlayerLayer *playerLayer = [LxAvPlayerLayer new];
    playerLayer.player = player;
    playerLayer.videoGravity = kCAGravityResizeAspectFill;
    return playerLayer;
}

- (instancetype) init
{
    if (self == [super init]) {
        
    }
    return self;
}
/** 打开item所有可加速或减速或回放的功能（canplayFast为只读 **/
- (void)enableAudioTracks:(BOOL)enable inPlayerItem:(AVPlayerItem*)playerItem
{
    for (AVPlayerItemTrack *track in playerItem.tracks)
    {
        if ([track.assetTrack.mediaType isEqual:AVMediaTypeAudio])
        {
            track.enabled = enable;
        }
    }
}


#pragma mark - CallFunction
/** 播放 **/
- (void)play
{
    [self.player play];
}

/** 暂停 **/
- (void)pause
{
    [self.player pause];
}

/** 重置 **/
- (void)reset
{
    [self.player pause];
    [self.player seekToTime:kCMTimeZero];
}
/** 设置播放速率 **/
- (void)seekRate:(float)rate
{
    [self enableAudioTracks:NO inPlayerItem:self.player.currentItem];
    [self.player setRate:rate];
}
@end
