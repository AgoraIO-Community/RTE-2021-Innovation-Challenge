//
//  CKBellView.h
//  SmartPiano
//
//  Created by xy on 2018/5/24.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CKBellView : UIView

/**
 是否播放伴奏
 */
@property (nonatomic, assign) BOOL audioPlayed;
/**
 伴奏播放中
 */
@property (nonatomic, assign) BOOL playing;
/**
 伴奏音乐的文件名称
 */
@property (nonatomic, copy) NSString *path;

/**
 开始播放伴奏
 */
- (void)play:(void(^)(void))playEndBlock;
/**
 停止播放伴奏
 */
- (void)stop;

@end
