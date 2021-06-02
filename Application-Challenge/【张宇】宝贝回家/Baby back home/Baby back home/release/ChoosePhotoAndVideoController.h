//
//  ChoosePhotoAndVideoController.h
//  Baby back home
//
//  Created by zhangyu on 2021/5/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ChoosePhotoAndVideoController : UIViewController

@property(nonatomic,copy) void(^completeChoosePic) (NSArray * arr);

@end

NS_ASSUME_NONNULL_END
