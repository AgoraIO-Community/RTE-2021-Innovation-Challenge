//
//  QHAudioConverter.m
//  QHAudioConverterMan
//
//  Created by Anakin chen on 2021/4/12.
//

/*
 单纯处理 PCM 的 采样率 和 采样精度 转换，如 48000 f32le 转 16000 f16le。
 主要针对目前采集的音频转为语音识别需要的pcm格式
 */

#import "QHAudioConverter.h"

@interface QHAudioConverter ()

@property (nonatomic, strong) dispatch_queue_t decoderQueue;
@property (nonatomic, strong) dispatch_queue_t callbackQueue;

@property (nonatomic, unsafe_unretained) AudioConverterRef audioConverter;

@property (nonatomic, strong) NSMutableData *buffer;
@property (nonatomic) unsigned char *pcmbuffer;
@property (nonatomic) UInt32 pcmbufferSize;
@property (nonatomic) UInt32 needPcmbufferSize;

@property (nonatomic) dispatch_semaphore_t converterSemaphore;

@property (nonatomic) AudioStreamBasicDescription in_ASBDes;
@property (nonatomic) AudioStreamBasicDescription out_ASBDes;

@end

@implementation QHAudioConverter

- (instancetype)init {
    self = [super init];
    if (self) {
        [self p_setup];
    }
    return self;
}

- (void)close {
    AudioConverterDispose(self.audioConverter);
    if (self.pcmbuffer != NULL) {
        free(self.pcmbuffer);
    }
}

- (void)decodeAudioSamepleBuffer:(CMSampleBufferRef)sampleBuffer {
    if (dispatch_semaphore_wait(_converterSemaphore, DISPATCH_TIME_NOW) != 0) {
        return;
    }
    __weak typeof(self) weakSelf = self;
    dispatch_async(_decoderQueue, ^{
        @autoreleasepool {
            NSError *error = nil;
            OSStatus status;
            
            AudioStreamBasicDescription inputAduioDes =* CMAudioFormatDescriptionGetStreamBasicDescription(CMSampleBufferGetFormatDescription(sampleBuffer));
            self.in_ASBDes = inputAduioDes;
            
            if (!self.audioConverter) {
                 [self p_setupEncoder];
            }
            
            //3.获取CMBlockBuffer, 这里面保存了PCM数据
            CMBlockBufferRef blockBuffer = CMSampleBufferGetDataBuffer(sampleBuffer);
            CFRetain(blockBuffer);
            size_t s;
            char *c;
            status = CMBlockBufferGetDataPointer(blockBuffer, 0, NULL, &s, &c);
            
            if (status != kCMBlockBufferNoErr) {
                error = [NSError errorWithDomain:NSOSStatusErrorDomain code:status userInfo:nil];
                NSLog(@"数据获取失败: %@", error);
                return;
            }
            
            [self.buffer appendBytes:c length:s];
            
            UInt32 bytesPerSample = self.out_ASBDes.mBytesPerFrame;
            UInt32 outputBytes = (UInt32)(bytesPerSample * (s / inputAduioDes.mBytesPerFrame) / (inputAduioDes.mSampleRate / self.out_ASBDes.mSampleRate));
            if (self.pcmbufferSize < outputBytes) {
                if (self.pcmbuffer != NULL) {
                    free(self.pcmbuffer);
                }
                self.pcmbuffer = (unsigned char*)malloc(outputBytes);
                self.pcmbufferSize = outputBytes;
            }
            memset(self.pcmbuffer, 0, outputBytes);
            unsigned char *outputBuffer = self.pcmbuffer;
            
            AudioBufferList outAudioBufferList = {0};
            outAudioBufferList.mNumberBuffers = 1;
            outAudioBufferList.mBuffers[0].mNumberChannels = (uint32_t)1;
            outAudioBufferList.mBuffers[0].mDataByteSize = outputBytes;
            outAudioBufferList.mBuffers[0].mData = outputBuffer;
            UInt32 outputDataPacketSize = outputBytes/bytesPerSample;
            
            status = AudioConverterFillComplexBuffer(self->_audioConverter, inputDataProc, (__bridge void * _Nullable)(self), &outputDataPacketSize, &outAudioBufferList, NULL);
            NSLog(@"chen6>>status>>%lu", (unsigned long)status);
            if (status == noErr) {
                dispatch_async(weakSelf.callbackQueue, ^{
                    //...
                    [self.delegate decodeResult:self bufferList:outAudioBufferList];
                });
            }
            else {
                error = [NSError errorWithDomain:NSOSStatusErrorDomain code:status userInfo:nil];
            }
            if (error) {
                NSLog(@"chen6>>error>>编码失败: %@",error);
            }
            dispatch_semaphore_signal(self.converterSemaphore);
        }
    });
}

#pragma mark - Private

- (void)p_setup {
    _decoderQueue = dispatch_queue_create("com.aac.decoder.queue", DISPATCH_QUEUE_SERIAL);
    _callbackQueue = dispatch_queue_create("com.acc.callback.queue", DISPATCH_QUEUE_SERIAL);
    
    _buffer = [NSMutableData new];
    _converterSemaphore = dispatch_semaphore_create(1);
    
    self.out_ASBDes = [self p_outputAduioDes];
}

- (void)p_setupEncoder {
    AudioStreamBasicDescription inputAduioDes = self.in_ASBDes;
    AudioStreamBasicDescription outputAudioDes = self.out_ASBDes;
    
    // create an audio converter
    AudioConverterRef audioConverter;
    OSStatus acCreationResult = AudioConverterNew(&inputAduioDes, &outputAudioDes, &audioConverter);
    printf("创建 audio converter %p (status: %d)\n", audioConverter, acCreationResult);
    _audioConverter = audioConverter;
    
//    AudioClassDescription hardwareClassDesc = [self p_converterClassDescriptionWithManufacturer:kAppleHardwareAudioCodecManufacturer];
//    AudioClassDescription softwareClassDesc = [self p_converterClassDescriptionWithManufacturer:kAppleSoftwareAudioCodecManufacturer];
//
//    AudioClassDescription classDescs[] = {hardwareClassDesc, softwareClassDesc};
//    OSStatus ret = AudioConverterNewSpecific(&inputAduioDes, &outputAudioDes, sizeof(classDescs), classDescs, &_audioConverter);
//    if (ret != noErr) {
//        return;
//    }
}

#pragma mark - Util

- (AudioClassDescription)p_converterClassDescriptionWithManufacturer:(OSType)manufacturer {
    AudioClassDescription desc;
    // Decoder
    desc.mType = kAudioDecoderComponentType;
    desc.mSubType = kAudioFormatMPEG4AAC;
    desc.mManufacturer = manufacturer;
    return desc;
}

- (AudioStreamBasicDescription)p_outputAduioDes {
    UInt32 bytesPerSample = sizeof(SInt16);
    UInt32 channels = 1;
    
    AudioStreamBasicDescription audioDes ={0};
    audioDes.mSampleRate = 16000;
    audioDes.mFormatID = kAudioFormatLinearPCM;
    audioDes.mFormatFlags = kLinearPCMFormatFlagIsNonInterleaved | kLinearPCMFormatFlagIsSignedInteger | kAudioFormatFlagIsPacked;
    audioDes.mBytesPerPacket = bytesPerSample;
    audioDes.mFramesPerPacket = 1;
    audioDes.mBytesPerFrame = bytesPerSample;
    audioDes.mChannelsPerFrame = channels;
    audioDes.mBitsPerChannel = 8 * bytesPerSample;
    audioDes.mReserved = 0;
    
    return audioDes;
}

#pragma mark - Action

static OSStatus inputDataProc(AudioConverterRef inAudioConverter, UInt32 *ioNumberDataPackets, AudioBufferList *ioData, AudioStreamPacketDescription **outDataPacketDescription, void *inUserData) {
    QHAudioConverter *thisSelf = (__bridge QHAudioConverter *)(inUserData);
    UInt32 needSize = (*ioNumberDataPackets) * 2;
    
    if (thisSelf.buffer.length < needSize) {
        return -501;
    }
    thisSelf.needPcmbufferSize = needSize;
    NSData *b = [thisSelf.buffer subdataWithRange:NSMakeRange(0, needSize)];
    
    ioData->mBuffers[0].mData = (char *)b.bytes;
    ioData->mBuffers[0].mDataByteSize = needSize;
    ioData->mBuffers[0].mNumberChannels = 1;
    
    if (thisSelf.buffer.length > needSize) {
        thisSelf.buffer = [[NSMutableData alloc] initWithBytes:(char *)thisSelf.buffer.bytes + needSize length:thisSelf.buffer.length - needSize];
    }
    else {
        thisSelf.buffer = [NSMutableData new];
    }
    
    return noErr;
}

@end
