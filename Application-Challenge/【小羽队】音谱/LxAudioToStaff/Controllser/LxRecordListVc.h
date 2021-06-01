//
//  LxRecordListVc.h
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/31.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
typedef void (^RecordListVcSelectedName)(NSString *recordName);
@interface LxRecordListVc : UIViewController

@property (strong, nonatomic, nullable) RecordListVcSelectedName block;

@end

NS_ASSUME_NONNULL_END
