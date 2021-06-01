//
//  QHViewUtil.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/21.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

#define QHCOLOR_RGBA(R/*红*/, G/*绿*/, B/*蓝*/, A/*透明*/) \
[UIColor colorWithRed:R/255.f green:G/255.f blue:B/255.f alpha:A]

@interface QHViewUtil : NSObject

+ (void)fullScreen:(UIView *)subView;

+ (void)fullScreen:(UIView *)subView edgeInsets:(UIEdgeInsets)edgeInsets;

@end

NS_ASSUME_NONNULL_END
