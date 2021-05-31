//
//  CKMixAudioTool.h
//  SmartPiano
//
//  Created by xy on 2018/5/25.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^CompletedBlock)(void);

@interface CKMixAudioTool : NSObject

@property (nonatomic, assign) CGFloat volume;

/**
 合并音频
 @param mainPath 主音频的文件地址
 @param mixPath 混合音频文件地址
 @param toPath 保存地址
 */
- (AVAssetExportSession*)mixMainAudio:(NSString *)mainPath mixAudio:(NSString *)mixPath toPath:(NSString *)toPath completed:(void (^)(NSError *error))completed;
-(void)audioMerge:(NSArray *)dataSource destUrl:(NSURL *)destUrl compleBlock:(void(^)(NSError *error))compleBlock;
/**
 开始播放某个音频

 @param path 音频地址
 @param completed 播放完成
 */
- (void)playAudio:(NSString *)path completed:(CompletedBlock)completed;

/**
 开始播放
 */
- (void)play;

/**
 暂停播放
 */
- (void)pause;

/**
 取消播放
 */
- (void)stop;

@end
