//
//  LxSpeechManager.m
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/30.
//

#import "LxSpeechManager.h"
//#import <AgoraRtcKit/AgoraMediaIO.h>
#import "NSString+Helper.h"
@interface LxSpeechManager()<AVAudioRecorderDelegate>

//<AgoraRtcEngineDelegate,AgoraAudioFrameDelegate>

///** Lx description   系统录制音频  **/
//@property (strong, nonatomic) AVAudioRecorder *audioRecorer;
@property (strong, nonatomic) AVAudioEngine *audioEngine;
/** Lx description   识别管理类  **/
@property (strong, nonatomic) SFSpeechRecognizer *speechRecognizer;
/** Lx description   识别request  **/
@property (strong,nonatomic) SFSpeechAudioBufferRecognitionRequest *recognitionRequest;
/** Lx description   当前录音文件名称  **/
@property (strong, nonatomic) NSString *currentFileName;

@end

@implementation LxSpeechManager
#pragma mark - ************************GetMethod************************
/** Lx description   单例方法  **/
+ (LxSpeechManager *)sharedInstance{
    static LxSpeechManager *_sharedInstance = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedInstance = [[LxSpeechManager alloc] init];
    });
    return _sharedInstance;
}

#pragma mark - ************************CallFunction************************
- (void)lx_checkSpeechAuthBlock:(void(^)(SFSpeechRecognizerAuthorizationStatus status))block{
    
//    AgoraRtcEngineKit *test = [AgoraRtcEngineKit sharedEngineWithAppId:@"7df06986a1ab443b8509c25bc9eff83c" delegate:self];
//    AgoraAudioRecordingConfiguration *recordConfig = [[AgoraAudioRecordingConfiguration alloc] init];
//    NSString *recoredPath = [NSString stringWithFormat:@"%@/record.m4a",kResourceCachePath];
//    recordConfig.filePath = recoredPath;
//    recordConfig.recordingQuality = AgoraAudioRecordingQualityHigh;
//   int deleteSet = [test setAudioFrameDelegate:self];
//    debugLog(@"设置录音代理成功%d",deleteSet);
//
//   int recordStartSuccess = [test startAudioRecordingWithConfig:recordConfig];
//    debugLog(@"开启录制成功 %d",recordStartSuccess);
   
    NSArray *supportLocals = [SFSpeechRecognizer supportedLocales];
    for (NSLocale *loc in supportLocals) {
        debugLog(loc.localeIdentifier);
    }

    
    NSLocale *chineseLocal = [NSLocale localeWithLocaleIdentifier:@"zh-CN"];
    self.speechRecognizer = [[SFSpeechRecognizer alloc] initWithLocale:chineseLocal];
    self.speechRecognizer.delegate = self;
 
//   NSArray *supports = [SFSpeechRecognizer supportedLocales];
//    for (NSLocale *tempLocal in supports) {
//        debugLog(tempLocal.localeIdentifier);
//    }
    [SFSpeechRecognizer requestAuthorization:^(SFSpeechRecognizerAuthorizationStatus status) {
        if (block) {
            block(status);
        }
    }];
}

/** Lx description   检查麦克风授权  **/
- (void)lx_checkAudioAuthBlock:(void(^)(BOOL authed))block{
   
    [AVCaptureDevice requestAccessForMediaType:AVMediaTypeAudio completionHandler:^(BOOL granted) {
        if (block) {
            block(granted);
        }
//        if (granted) {
//            AVAudioSession *session = [AVAudioSession sharedInstance];
//            NSError *error;
//            if (![session setCategory:AVAudioSessionCategoryPlayAndRecord error:&error]) {
//                [MBProgressHUD lx_showHudWithTitle:error.description hideCompletion:nil];
//            }
//            if (![session setActive:YES error:&error]) {
//                [MBProgressHUD lx_showHudWithTitle:error.description hideCompletion:nil];
//            }
//        }
        
    }];
}

/** Lx description   开始录制  **/
- (void)lx_startRecord{
    
    [self initEngine];
    
    AVAudioFormat *recordingFormat = [[self.audioEngine inputNode] outputFormatForBus:0];
    [[self.audioEngine inputNode] installTapOnBus:0 bufferSize:1024 format:recordingFormat block:^(AVAudioPCMBuffer * _Nonnull buffer, AVAudioTime * _Nonnull when) {
        [self.recognitionRequest appendAudioPCMBuffer:buffer];
    }];
    [self.audioEngine prepare];
    [self.audioEngine startAndReturnError:nil];
    
}

/** Lx description   停止录制  **/
- (void)lx_stopRecordWithTransBlock:(TransStringBlock)block{
    self.transBLock = [block copy];
    [self releaseEngine];
    
//    [self performSelector:@selector(test) withObject:nil afterDelay:3];
    NSString *a;
    [self test];
}

- (void)test{
    [[AVAudioSession sharedInstance] setActive:NO error:nil];
    [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategorySoloAmbient mode:AVAudioSessionModeDefault options:AVAudioSessionCategoryOptionDuckOthers error:nil];
    [[AVAudioSession sharedInstance] setActive:YES error:nil];
}

#pragma mark - ************************Function************************
- (void)initEngine{
    if (!self.audioEngine) {
        self.audioEngine = [[AVAudioEngine alloc] init];
    }
    
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    
    [audioSession setCategory:AVAudioSessionCategoryPlayAndRecord mode:AVAudioSessionModeMeasurement options:AVAudioSessionCategoryOptionDuckOthers error:nil];
    [audioSession setActive:YES withOptions:AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation error:nil];
    
    if (self.recognitionRequest) {
        [self.recognitionRequest endAudio];
        self.recognitionRequest = nil;
    }
    self.recognitionRequest = [[SFSpeechAudioBufferRecognitionRequest alloc] init];
    self.recognitionRequest.shouldReportPartialResults = NO; // 实时翻译
    if (@available(iOS 13, *)) {
        self.recognitionRequest.requiresOnDeviceRecognition = NO;
    } else {
      
    }
//    self.recognitionRequest.interactionIdentifier = @"多瑞米发唆拉稀1234567";
    
    [self.speechRecognizer recognitionTaskWithRequest:self.recognitionRequest delegate:self];
}

- (void)releaseEngine {
    [[self.audioEngine inputNode] removeTapOnBus:0];
    [self.audioEngine stop];
    [self.audioEngine reset];
    
    [self.recognitionRequest endAudio];
    self.recognitionRequest = nil;
}
//#pragma mark - ************************RtcRecordDelegate************************
//- (BOOL)onRecordAudioFrame:(AgoraAudioFrame* _Nonnull)frame{
//
//    debugLog(@"录制buffer大小%ld",(long)frame.bytesPerSample);
//    return YES;
//}
//
//- (BOOL)onMixedAudioFrame:(AgoraAudioFrame * _Nonnull)frame {
//    return YES;
//}
//
//
//- (BOOL)onPlaybackAudioFrame:(AgoraAudioFrame * _Nonnull)frame {
//    return YES;
//}
//
//
//- (BOOL)onPlaybackAudioFrameBeforeMixing:(AgoraAudioFrame * _Nonnull)frame uid:(NSUInteger)uid {
//    return YES;
//}
//
//- (BOOL)isSpeakerphoneEnabled{
//    return YES;
//}

#pragma mark - ************************SpeechDelegate************************

- (void)speechRecognizer:(SFSpeechRecognizer *)speechRecognizer availabilityDidChange:(BOOL)available{
    debugLog(@"speech可用:%d",available);
}
//当开始检测音频源中的语音时首先调用此方法
- (void)speechRecognitionDidDetectSpeech:(SFSpeechRecognitionTask *)task{
    debugLog(@"开始链接speech");
}
//当识别出一条可用的信息后 会调用
/*
需要注意，apple的语音识别服务会根据提供的音频源识别出多个可能的结果 每有一条结果可用 都会调用此方法
*/
- (void)speechRecognitionTask:(SFSpeechRecognitionTask *)task didHypothesizeTranscription:(SFTranscription *)transcription{
    for (SFTranscriptionSegment *segment in transcription.segments) {
        debugLog(@"speech翻译：%@",segment.substring);
    }
   
}
//当识别完成所有可用的结果后调用
- (void)speechRecognitionTask:(SFSpeechRecognitionTask *)task didFinishRecognition:(SFSpeechRecognitionResult *)recognitionResult{
    debugLog(@"speech结束，best:%@",recognitionResult.bestTranscription.formattedString);
    
    NSString *transStr = recognitionResult.bestTranscription.formattedString;
    transStr = [transStr stringByReplacingOccurrencesOfString:@"," withString:@""];
    transStr = [transStr stringByReplacingOccurrencesOfString:@"。" withString:@""];
    transStr = [transStr stringByReplacingOccurrencesOfString:@" " withString:@""];
    transStr = [transStr stringByReplacingOccurrencesOfString:@"，" withString:@""];
    debugLog(@"去除多余字符后:%@",transStr);
    NSMutableArray <NSString *>*letters = [[NSMutableArray alloc] init];
    for (int i = 0; i < transStr.length; i ++) {
        NSString *subStr = [transStr substringWithRange:NSMakeRange(i, 1)];
        NSString *letter = [subStr lx_firstLetter];
        if (letter) {
            if ([letter isEqualToString:@"N"]) {
                [letters addObject:@"L"];
            }else if ([letter isEqualToString:@"H"]){
                [letters addObject:@"F"];
            }
            else if ([letter isEqualToString:@"Q"]){
                [letters addObject:@"X"];
            }else if ([letter isEqualToString:@"B"]){
                [letters addObject:@"F"];
            }
            else{
                [letters addObject:letter];
            }
            
            debugLog(@"转化字母%@",letter);
        }
    }
    if (self.transBLock) {
        self.transBLock(letters);
    }
//    for (SFTranscription *trans in recognitionResult.transcriptions) {
//        for (SFTranscriptionSegment *segment in trans.segments) {
//            debugLog(@"speech翻译：%@",segment.substring);
//        }
//    }
}
//当不再接受音频输入时调用 即开始处理语音识别任务时调用
- (void)speechRecognitionTaskFinishedReadingAudio:(SFSpeechRecognitionTask *)task{
    debugLog(@"speech开始识别");
}
//当语音识别任务被取消时调用
- (void)speechRecognitionTaskWasCancelled:(SFSpeechRecognitionTask *)task{
    debugLog(@"speech识别取消");
}
//语音识别任务完成时被调用
- (void)speechRecognitionTask:(SFSpeechRecognitionTask *)task didFinishSuccessfully:(BOOL)successfully{
    debugLog(@"speech识别成功%d",successfully);
}


@end
