//
//  BBAddShadowTool.h
//  Baby back home
//
//  Created by zhangyu on 2021/5/28.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BBAddShadowTool : NSObject
+ (void)addShadowToView:(UIView *)theView;
+ (void)addShadowToViewOnlyBottom:(UIView *)theView;
@end

NS_ASSUME_NONNULL_END
