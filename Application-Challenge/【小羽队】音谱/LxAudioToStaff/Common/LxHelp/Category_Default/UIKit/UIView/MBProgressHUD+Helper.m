//
//  MBProgressHUD+Helper.m
//  MonkeySpeed
//
//  Created by DavinLee on 2021/5/25.
//

#import "MBProgressHUD+Helper.h"
#import "UIColor+Default.h"
#import "CALayer+Default.h"
#import "UIFont+Default.h"
@implementation MBProgressHUD (Helper)

+ (MBProgressHUD *)lx_showHudWithTitle:(NSString *)title
                        hideCompletion:(nullable void(^)(void))completionBlock
{
    [MBProgressHUD hideHud];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:LxLibrary.sharedInstance.keyWindow animated:YES];
    
    hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
    hud.bezelView.backgroundColor = [UIColor lx_colorWithHexString:mAppThemeColrHex];
    hud.contentColor = [UIColor whiteColor];
    hud.label.textColor = [UIColor whiteColor];
    hud.label.font = [UIFont lx_pingFangSCofSize:21];
    hud.detailsLabel.textColor = [UIColor whiteColor];
    hud.animationType = MBProgressHUDAnimationFade;
    hud.detailsLabel.text = title;
    hud.detailsLabel.font = [UIFont lx_pingFangSCofSize:19];
    
    hud.removeFromSuperViewOnHide = YES;
    hud.mode = MBProgressHUDModeText;
    [hud.bezelView.layer lx_shadowWithCornerRadius:3
                                       shadowColor:[UIColor blackColor]
                                           opacity:0.2
                                            radius:3
                                            offset:CGSizeZero];
    hud.bezelView.layer.masksToBounds = NO;
    [hud showAnimated:YES];
    if (completionBlock) {
        [hud setCompletionBlock:^{
            dispatch_async(dispatch_get_main_queue(), ^{
                completionBlock();
            });
        }];
    }
    [hud hideAnimated:YES afterDelay:1.0];
    return hud;
}


/** Lx description   加载显示文本  **/
+ (MBProgressHUD *)lx_showLoadHudWithTitle:(nullable NSString *)title
                            hideCompletion:(nullable void(^)(void))completionBlock;{
    
    [MBProgressHUD hideHud];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:LxLibrary.sharedInstance.keyWindow animated:YES];
    hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
    hud.bezelView.backgroundColor = [UIColor lx_colorWithHexString:mAppThemeColrHex];
    hud.contentColor  = [UIColor whiteColor];
    if (title) {
        hud.label.textColor = [UIColor whiteColor];
        hud.detailsLabel.text = title;
        
    }
    hud.removeFromSuperViewOnHide = YES;
    hud.animationType = MBProgressHUDAnimationFade;
    hud.mode = MBProgressHUDModeIndeterminate;
    [hud showAnimated:YES];
    
    [hud.bezelView.layer lx_shadowWithCornerRadius:3
                                       shadowColor:[UIColor blackColor]
                                           opacity:0.2
                                            radius:3
                                            offset:CGSizeZero];
    hud.bezelView.layer.masksToBounds = NO;
    if (completionBlock) {
        [hud setCompletionBlock:^{
            dispatch_async(dispatch_get_main_queue(), ^{
                completionBlock();
            });
        }];
    }
    
    return hud;
}

+ (void)hideHud{
    [MBProgressHUD hideHUDForView:LxLibrary.sharedInstance.keyWindow animated:YES];
}
@end
