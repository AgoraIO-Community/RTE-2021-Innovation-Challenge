//
//  CKMixAudioTool.m
//  SmartPiano
//
//  Created by xy on 2018/5/25.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "CKMixAudioTool.h"
#import <AVFoundation/AVFoundation.h>
#import "ExtAudioConverter.h"
@interface CKMixAudioTool ()

@property (nonatomic, strong) AVAudioPlayer *audioPlayer;

@property (nonatomic, copy) CompletedBlock completed;

@end

@implementation CKMixAudioTool

- (AVAssetExportSession *)mixMainAudio:(NSString *)mainPath mixAudio:(NSString *)mixPath toPath:(NSString *)toPath completed:(void (^)(NSError *error))completed {
//    mixPath = [[NSBundle mainBundle] pathForResource:@"渐强" ofType:@"mp3"];
    NSString *desPath = [toPath stringByReplacingOccurrencesOfString:@"mp3" withString:@"m4a"];
    if ([[NSFileManager defaultManager] fileExistsAtPath:desPath]) {
        [[NSFileManager defaultManager] removeItemAtPath:desPath error:nil];
    }
    if (mainPath.length > 0 && mixPath.length > 0) {
        //音频文件合并器
        AVMutableComposition *mixComposition = [AVMutableComposition composition];
        //设置音频轨道
        AVMutableCompositionTrack *track = [mixComposition addMutableTrackWithMediaType:AVMediaTypeAudio preferredTrackID:kCMPersistentTrackID_Invalid];
        AVURLAsset *mainAudioAsset = [[AVURLAsset alloc] initWithURL:[NSURL fileURLWithPath:mainPath] options:nil];
        CMTimeRange mainRang = CMTimeRangeMake(kCMTimeZero, mainAudioAsset.duration);
        BOOL success = [track insertTimeRange:mainRang ofTrack:[[mainAudioAsset tracksWithMediaType:AVMediaTypeAudio] firstObject] atTime:kCMTimeZero error:nil];
        if (success) {
            debugLog(@"主音频加入成功");
        }
        //设置音频轨道
        AVMutableCompositionTrack *mixTrack = [mixComposition addMutableTrackWithMediaType:AVMediaTypeAudio preferredTrackID:kCMPersistentTrackID_Invalid];
        AVURLAsset *mixAudioAsset = [[AVURLAsset alloc] initWithURL:[NSURL fileURLWithPath:mixPath] options:nil];
        CMTimeRange mixRang = CMTimeRangeMake(kCMTimeZero, mixAudioAsset.duration);
        success = [mixTrack insertTimeRange:mixRang ofTrack:[[mixAudioAsset tracksWithMediaType:AVMediaTypeAudio] firstObject] atTime:kCMTimeZero error:nil];
        if (success) {
            debugLog(@"副音频加入成功");
        }
        AVAssetExportSession *assetExport = [[AVAssetExportSession alloc] initWithAsset:mixComposition presetName:AVFileTypeAppleM4A];
        assetExport.outputURL = [NSURL fileURLWithPath:desPath];
        assetExport.outputFileType = @"com.apple.m4a-audio";
        assetExport.shouldOptimizeForNetworkUse = YES;
        [assetExport exportAsynchronouslyWithCompletionHandler:^{
            dispatch_async(dispatch_get_main_queue(), ^{
                if (completed) {
                    completed(assetExport.error);
                    
                }
            });
        }];
        return assetExport;
    }
    return nil;
}

-(void)audioMerge:(NSArray *)dataSource destUrl:(NSURL *)destUrl compleBlock:(void(^)(NSError *error))compleBlock{
    AVMutableComposition *mixComposition = [AVMutableComposition composition];
    
    // 设置音频合并音轨
    AVMutableCompositionTrack *compositionAudioTrack = [mixComposition addMutableTrackWithMediaType:AVMediaTypeAudio preferredTrackID:kCMPersistentTrackID_Invalid];
    
    NSError *error = nil;
    //    for (NSURL *sourceURL in dataSource) {
    //音频文件资源
    AVURLAsset *audioAsset = [[AVURLAsset alloc] initWithURL:[NSURL fileURLWithPath:[dataSource firstObject]] options:nil];

    //需要合并的音频文件的区间
    CMTimeRange audio_timeRange = CMTimeRangeMake(kCMTimeZero, audioAsset.duration);
    // ofTrack 音频文件内容
    
    BOOL success = [compositionAudioTrack insertTimeRange:audio_timeRange ofTrack:[[audioAsset tracksWithMediaType:AVMediaTypeAudio] firstObject] atTime:kCMTimeZero error:&error];
    
    /** 设置音量 **/
    AVMutableAudioMixInputParameters *parameter = [AVMutableAudioMixInputParameters audioMixInputParametersWithTrack:compositionAudioTrack];
    [parameter setVolume:0.4 atTime:kCMTimeZero];

    if (!success) {
        debugLog(@"Error: %@",error);
    }
    //    }
    if (dataSource.count > 1) {
        // 设置音频合并音轨
        AVMutableCompositionTrack *compositionAudioTrack2 = [mixComposition addMutableTrackWithMediaType:AVMediaTypeAudio preferredTrackID:kCMPersistentTrackID_Invalid];
        
        NSError *error2 = nil;
        //    for (NSURL *sourceURL in dataSource) {
        //音频文件资源
        AVURLAsset *audioAsset2 = [[AVURLAsset alloc] initWithURL:[NSURL fileURLWithPath:[dataSource lastObject]] options:nil];
        //需要合并的音频文件的区间
        CMTimeRange audio_timeRange2 = CMTimeRangeMake(kCMTimeZero, audioAsset2.duration);
        // ofTrack 音频文件内容
        
        BOOL success2 = [compositionAudioTrack2 insertTimeRange:audio_timeRange2 ofTrack:[[audioAsset2 tracksWithMediaType:AVMediaTypeAudio] firstObject] atTime:kCMTimeZero error:&error2];
        
        if (!success2) {
            debugLog(@"Error: %@",error2);
        }
    }
    
    // presetName 与 outputFileType 要对应 导出合并的音频
    AVAssetExportSession* assetExportSession = [[AVAssetExportSession alloc] initWithAsset:mixComposition presetName:AVAssetExportPresetAppleM4A];
    assetExportSession.outputURL = destUrl;
    assetExportSession.outputFileType = @"com.apple.m4a-audio";
    assetExportSession.shouldOptimizeForNetworkUse = YES;
    WF;
    [assetExportSession exportAsynchronouslyWithCompletionHandler:^{
        dispatch_async(dispatch_get_main_queue(), ^{
          
            NSString *desPath = destUrl.path;
            if (!error) {
                [weakSelf converWav:[destUrl path]  toMp3:[desPath stringByReplacingOccurrencesOfString:@"m4a" withString:@"mp3"] successBlock:^{
                    if (compleBlock) {
                        compleBlock(nil);
                    }
                }];
            }else
            {
                if (compleBlock) {
                    compleBlock(error);
                }
            }
           
//            [weakSelf playAudioWithPath:destUrl];
            debugLog(@"%@",assetExportSession.error);
        });
        int exportStatus = assetExportSession.status;
        switch (exportStatus) {
                
            case AVAssetExportSessionStatusFailed: {
                
                NSError *exportError = assetExportSession.error;
                debugLog (@"AVAssetExportSessionStatusFailed: %@", exportError);
                break;
            }
            case AVAssetExportSessionStatusCompleted: {
                debugLog (@"AVAssetExportSessionStatusCompleted--");
                break;
            }
            case AVAssetExportSessionStatusUnknown: {
                debugLog (@"AVAssetExportSessionStatusUnknown");
                break;
            }
            case AVAssetExportSessionStatusExporting: {
                debugLog (@"AVAssetExportSessionStatusExporting");
                break;
            }
            case AVAssetExportSessionStatusCancelled: {
                debugLog (@"AVAssetExportSessionStatusCancelled");
                break;
            }
            case AVAssetExportSessionStatusWaiting: {
                debugLog (@"AVAssetExportSessionStatusWaiting");
                break;
            }
            default: { debugLog (@"didn't get export status"); break;}
        }
        
    }];
}

- (void)converWav:(NSString *)wavPath toMp3:(NSString *)mp3Path successBlock:(void(^)(void))block{
    [[NSFileManager defaultManager] removeItemAtPath:mp3Path error:nil];
    ExtAudioConverter* converter = [[ExtAudioConverter alloc] init];
    //converter.inputFile =  @"/Users/lixing/Desktop/playAndRecord.caf";
    converter.inputFile =  wavPath;
    //output file extension is for your convenience
    converter.outputFile = mp3Path;
    
    //TODO:some option combinations are not valid.
    //Check them out
    converter.outputSampleRate = 44100;
    converter.outputNumberChannels = 2;
    converter.outputBitDepth = BitDepth_32;
    converter.outputFormatID = kAudioFormatMPEGLayer3;
    converter.outputFileType = kAudioFileMP3Type;
    [converter convert];
    if (block) {
        block();
    }
    
    
    //    ExtAudioConverter* converter2 = [[ExtAudioConverter alloc] init];
    //    //converter.inputFile =  @"/Users/lixing/Desktop/playAndRecord.caf";
    //    converter2.inputFile =  wavPath;
    //    //output file extension is for your convenience
    //    converter2.outputFile = mp3Path;
    //
    //    //TODO:some option combinations are not valid.
    //    //Check them out
    //    converter2.outputSampleRate = 8000;
    //    converter2.outputNumberChannels = 1;
    //    converter2.outputBitDepth = BitDepth_32;
    //    converter2.outputFormatID = kAudioFormatAppleIMA4;
    //    converter2.outputFileType = kAudioFileMP3Type;
    //    [converter2 convert];
    
}


- (void)playAudio:(NSString *)path completed:(CompletedBlock)completed {
    self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL fileURLWithPath:path] error:nil];
    [self.audioPlayer prepareToPlay];
    self.volume = 0.5;
    [self play];
    self.completed = completed;
    [self performSelector:@selector(playerAudioEnd) withObject:nil afterDelay:self.audioPlayer.duration];
}

- (void)playerAudioEnd {
    if (self.completed) {
        self.completed();
    }
}

- (void)play {
    [self.audioPlayer play];
}

- (void)pause {
    [self.audioPlayer pause];
}

- (void)stop {
    [self.audioPlayer stop];
    self.audioPlayer = nil;
}

#pragma mark  --  setter
- (void)setVolume:(CGFloat)volume {
    _volume = volume;
    self.audioPlayer.volume = volume;
}

@end
