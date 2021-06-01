//
//  LxVideoPlayNode.h
//  SmartPiano
//
//  Created by DavinLee on 2020/7/2.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import <SpriteKit/SpriteKit.h>
@class AVPlayerItem;

NS_ASSUME_NONNULL_BEGIN

@interface LxVideoPlayNode : SKVideoNode

@property (strong, nonatomic) AVPlayerItem *testPlayerItem;

@property (assign, nonatomic) BOOL testPaused;

+ (LxVideoPlayNode *)lx_defaultWithPath:(NSString *)path;

- (void)setupNotificationName:(NSString *)name avplayer:(AVPlayer *)player;
@end

NS_ASSUME_NONNULL_END
