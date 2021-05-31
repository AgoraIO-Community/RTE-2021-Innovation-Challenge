//
//  LxSpeechManager.m
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/30.
//

#import "LxSpeechManager.h"
#import <AgoraRtcKit/AgoraMediaIO.h>
#import "NSString+Helper.h"
@interface LxSpeechManager()<AVAudioRecorderDelegate,AgoraRtcEngineDelegate,AgoraAudioFrameDelegate>

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
@property (strong,nonatomic) AgoraRtcEngineKit *agoraKit;
@property (strong, nonatomic) NSMutableArray *bestTransStrs;
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
    
    
//    [agoraKit enableAudio];
    
   
//    NSArray *supportLocals = [SFSpeechRecognizer supportedLocales];
//    for (NSLocale *loc in supportLocals) {
//        debugLog(loc.localeIdentifier);
//    }

    
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
    [self startEngine];
    [self initEngine];
    
//    AVAudioFormat *recordingFormat = [[self.audioEngine inputNode] outputFormatForBus:0];
//    [[self.audioEngine inputNode] installTapOnBus:0 bufferSize:1024 format:recordingFormat block:^(AVAudioPCMBuffer * _Nonnull buffer, AVAudioTime * _Nonnull when) {
//        [self.recognitionRequest appendAudioPCMBuffer:buffer];
//    }];
//    [self.audioEngine prepare];
//    [self.audioEngine startAndReturnError:nil];
    
}

/** Lx description   停止录制  **/
- (void)lx_stopRecordWithTransBlock:(TransStringBlock)block{
    self.transBLock = [block copy];
    [self releaseEngine];
    [self stopEngine];
    
    
//    [self test];
}

#pragma mark - ************************Function************************
- (void)startEngine{
    AgoraRtcEngineConfig *config = [[AgoraRtcEngineConfig alloc] init];
    config.appId = @"7df06986a1ab443b8509c25bc9eff83c";
    config.areaCode = 4294967295;
    
    AgoraLogConfig *logConfig = [[AgoraLogConfig alloc] init];
    logConfig.level = AgoraLogLevelInfo;
    config.logConfig = logConfig;
    
    AgoraRtcEngineKit *agoraKit = [AgoraRtcEngineKit sharedEngineWithConfig:config delegate:self];
    [agoraKit disableVideo];
    
    [agoraKit setChannelProfile:AgoraChannelProfileLiveBroadcasting];
    [agoraKit setClientRole:AgoraClientRoleBroadcaster];
    [agoraKit setAudioFrameDelegate:self];
    [agoraKit setRecordingAudioFrameParametersWithSampleRate:44100 channel:1 mode:AgoraAudioRawFrameOperationModeReadWrite samplesPerCall:4410];
    [agoraKit setMixedAudioFrameParametersWithSampleRate:44100 samplesPerCall:4410];
    [agoraKit setPlaybackAudioFrameParametersWithSampleRate:44100 channel:1 mode:AgoraAudioRawFrameOperationModeReadWrite samplesPerCall:4410];
    [agoraKit setDefaultAudioRouteToSpeakerphone:YES];
    self.agoraKit = agoraKit;
    
   NSInteger code = [agoraKit joinChannelByToken:@"0067df06986a1ab443b8509c25bc9eff83cIABA7MVVPbq+YyTigelZoVX/QE9J9WjIA2OE0pDstX5ldbfv3IMAAAAAEADEZWnphIS2YAEAAQCDhLZg" channelId:@"1" info:nil uid:0 options:[[AgoraRtcChannelMediaOptions alloc]init]];
    debugLog(@"加入房间成功%ld",(long)code);
}

- (void)stopEngine{
    
//    [self.agoraKit stopAudioRecording];
//    [self.agoraKit stopChannelMediaRelay];
//    [self.agoraKit stopAllEffects];
    [self.agoraKit leaveChannel:^(AgoraChannelStats * _Nonnull stat) {
//        [self performSelector:@selector(test) withObject:nil afterDelay:1];
//        [self test];
        dispatch_async(dispatch_get_main_queue(), ^{
            [self test];
        });
        
    }];
    self.agoraKit.delegate = nil;
    self.agoraKit = nil;
    
}

- (void)test{
    
//    [[AVAudioSession sharedInstance] setActive:NO error:nil];
    [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategorySoloAmbient mode:AVAudioSessionModeDefault options:AVAudioSessionCategoryOptionDuckOthers error:nil];
    if (self.transBLock) {
        self.transBLock(self.bestTransStrs);
    }
//    [[AVAudioSession sharedInstance] setActive:YES error:nil];
}

#pragma mark - ************************Function************************
- (void)initEngine{
//    if (!self.audioEngine) {
//        self.audioEngine = [[AVAudioEngine alloc] init];
//    }
    
//    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
//
//    [audioSession setCategory:AVAudioSessionCategoryPlayAndRecord mode:AVAudioSessionModeMeasurement options:AVAudioSessionCategoryOptionDuckOthers error:nil];
//    [audioSession setActive:YES withOptions:AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation error:nil];
    
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
    
    [self.speechRecognizer recognitionTaskWithRequest:self.recognitionRequest delegate:self];
}

- (void)releaseEngine {
//    [[self.audioEngine inputNode] removeTapOnBus:0];
//    [self.audioEngine stop];
//    [self.audioEngine reset];
    
    [self.recognitionRequest endAudio];
    self.recognitionRequest = nil;
}
//#pragma mark - ************************RtcRecordDelegate************************
- (BOOL)onRecordAudioFrame:(AgoraAudioFrame* _Nonnull)frame{

//    debugLog(@"录制buffer大小%@",frame.buffer);
    AVAudioFormat *audioFormat = [[AVAudioFormat alloc] initWithCommonFormat:AVAudioPCMFormatInt16 sampleRate:44100 channels:1 interleaved:NO];
    AVAudioPCMBuffer *PCMBuffer = [[AVAudioPCMBuffer alloc] initWithPCMFormat:audioFormat frameCapacity:(UInt32)frame.buffer.length / audioFormat.streamDescription->mBytesPerFrame];
    PCMBuffer.frameLength = PCMBuffer.frameCapacity;
    [frame.buffer getBytes:*PCMBuffer.int16ChannelData length:frame.buffer.length];
//    debugLog(@"转换buffer后= %@",PCMBuffer);
    if (PCMBuffer) {
        [self.recognitionRequest appendAudioPCMBuffer:PCMBuffer];
    }
    
    
//    let audioFormat = AVAudioFormat(commonFormat: AVAudioCommonFormat.PCMFormatFloat32, sampleRate: 8000, channels: 1, interleaved: false)  // given NSData audio format
//        var PCMBuffer = AVAudioPCMBuffer(PCMFormat: audioFormat, frameCapacity: UInt32(data.length) / audioFormat.streamDescription.memory.mBytesPerFrame)
//        PCMBuffer.frameLength = PCMBuffer.frameCapacity
//        let channels = UnsafeBufferPointer(start: PCMBuffer.floatChannelData, count: Int(PCMBuffer.format.channelCount))
//        data.getBytes(UnsafeMutablePointer<Void>(channels[0]) , length: data.length)
//        return PCMBuffer
//    [self.recognitionRequest appendAudioPCMBuffer:nil];
    return YES;
}

- (BOOL)onMixedAudioFrame:(AgoraAudioFrame * _Nonnull)frame {
    return YES;
}


- (BOOL)onPlaybackAudioFrame:(AgoraAudioFrame * _Nonnull)frame {
    return YES;
}


- (BOOL)onPlaybackAudioFrameBeforeMixing:(AgoraAudioFrame * _Nonnull)frame uid:(NSUInteger)uid {
    return YES;
}

- (BOOL)isSpeakerphoneEnabled{
    return YES;
}

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
    transStr = [transStr stringByReplacingOccurrencesOfString:@"？" withString:@""];
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
    self.bestTransStrs = letters;
    
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
