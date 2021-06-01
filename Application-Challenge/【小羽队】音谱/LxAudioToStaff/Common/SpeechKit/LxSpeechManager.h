//
//  LxSpeechManager.h
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/30.
//

#import <Foundation/Foundation.h>
#import <Speech/Speech.h>
NS_ASSUME_NONNULL_BEGIN
typedef void (^TransStringBlock)(NSMutableArray <NSString *>* strs);
@interface LxSpeechManager : NSObject<SFSpeechRecognizerDelegate,
SFSpeechRecognitionTaskDelegate>

@property (copy, nonatomic) TransStringBlock transBLock;

/** Lx description   单例方法  **/
+ (LxSpeechManager *)sharedInstance;
/** Lx description   检查授权  **/
- (void)lx_checkSpeechAuthBlock:(void(^)(SFSpeechRecognizerAuthorizationStatus status))block;
/** Lx description   检查麦克风授权  **/
- (void)lx_checkAudioAuthBlock:(void(^)(BOOL authed))block;
/** Lx description   开始录制  **/
- (void)lx_startRecord;
/** Lx description   停止录制  **/
- (void)lx_stopRecordWithTransBlock:(TransStringBlock)block;
@end

NS_ASSUME_NONNULL_END
