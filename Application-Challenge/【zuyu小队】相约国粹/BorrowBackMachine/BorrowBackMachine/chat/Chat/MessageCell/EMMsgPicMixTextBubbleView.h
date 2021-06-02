//
//  EMMsgPicMixTextBubbleView.h
//  EaseIM
//
//  Created by 娜塔莎 on 2019/11/22.
//  Copyright © 2019 娜塔莎. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EMMsgBubbleView.h"

NS_ASSUME_NONNULL_BEGIN

@interface EMMsgPicMixTextBubbleView : EMMsgBubbleView

@property (nonatomic, strong) UILabel *textLabel;
@property (nonatomic, strong) UIButton *textImgBtn;

@end

NS_ASSUME_NONNULL_END
