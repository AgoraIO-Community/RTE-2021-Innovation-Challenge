//
//  QHLinkMicAuthorizationView.h
//
//  Created by Anakin chen on 2020/2/24.
//  Copyright © 2020 ... All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^AuthorizationBlock)(BOOL bAuthorization);

@interface QHLinkMicAuthorizationView : UIView


/**
 创建 摄像头 & 麦克风 授权页，如果都授权了，则不创建，直接回调 block，
 而未授权的，会在都授权之后消失时回调 block
 */
+ (instancetype)creatIn:(UIView *)superView authorization:(AuthorizationBlock)block;


/**
 隐藏 返回按钮
 */
- (void)hiddenClose:(BOOL)bHidden;

@end

NS_ASSUME_NONNULL_END
