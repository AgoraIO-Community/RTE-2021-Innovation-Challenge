//
//  LxVideoPlayNode.m
//  SmartPiano
//
//  Created by DavinLee on 2020/7/2.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import "LxVideoPlayNode.h"
#import <AVFoundation/AVFoundation.h>
@interface LxVideoPlayNode()


@end


@implementation LxVideoPlayNode
+ (LxVideoPlayNode *)lx_defaultWithPath:(NSString *)path{
    AVPlayerItem *item = [[AVPlayerItem alloc] initWithURL:[NSURL fileURLWithPath:path]];
    AVPlayer *avp = [[AVPlayer alloc] initWithPlayerItem:item];
    LxVideoPlayNode *videoNode = (LxVideoPlayNode *)[[LxVideoPlayNode alloc] initWithAVPlayer:avp];
    [videoNode setupNotificationName:@"aaa" avplayer:avp];
//    videoNode.testPlayerItem = item;
    return videoNode;
    
}

- (void)setupNotificationName:(NSString *)name avplayer:(AVPlayer *)player{
    [mNotificationCenter addObserver:self selector:@selector(playEnded:) name:AVPlayerItemDidPlayToEndTimeNotification object:[player currentItem]];
    self.testPlayerItem = player.currentItem;
    
}

- (void)playEnded:(NSNotification *)not{
    AVPlayerItem *p = [not object];
    if (p == self.testPlayerItem) {
        dispatch_async(dispatch_get_main_queue(), ^{
          [p seekToTime:kCMTimeZero];
            [self play];
        });
    }
}


- (void)play{
    [super play];
    self.testPaused = NO;
}

- (void)pause{
    [super pause];
    self.testPaused = YES;
}

- (void)dealloc{
    debugLog(@"%s",__func__);
    [mNotificationCenter removeObserver:self];
}
@end
