//
//  QHAudioConverter.h
//  QHAudioConverterMan
//
//  Created by Anakin chen on 2021/4/12.
//

#import <Foundation/Foundation.h>
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

NS_ASSUME_NONNULL_BEGIN

@class QHAudioConverter;

@protocol QHAudioConverterDelegate <NSObject>

- (void)decodeResult:(QHAudioConverter *)ac bufferList:(AudioBufferList)bl;

@end

@interface QHAudioConverter : NSObject

@property (nonatomic, weak) id<QHAudioConverterDelegate> delegate;

- (void)close;
- (void)decodeAudioSamepleBuffer:(CMSampleBufferRef)sampleBuffer;

@end

NS_ASSUME_NONNULL_END
