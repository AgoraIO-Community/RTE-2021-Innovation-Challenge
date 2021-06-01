//
//  MBProgressHUD+Helper.h
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/25.
//

#import <MBProgressHUD/MBProgressHUD.h>

NS_ASSUME_NONNULL_BEGIN

@interface MBProgressHUD (Helper)

+ (MBProgressHUD *)lx_showHudWithTitle:(NSString *)title
                        hideCompletion:(nullable void(^)(void))completionBlock;
/** Lx description   加载显示文本  **/
+ (MBProgressHUD *)lx_showLoadHudWithTitle:(nullable NSString *)title
                            hideCompletion:(nullable void(^)(void))completionBlock;


+ (void)hideHud;
@end

NS_ASSUME_NONNULL_END
