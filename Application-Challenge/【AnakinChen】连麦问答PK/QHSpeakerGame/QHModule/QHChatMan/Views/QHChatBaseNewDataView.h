//
//  QHChatBaseNewDataView.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/25.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "QHChatBaseView.h"

NS_ASSUME_NONNULL_BEGIN

@interface QHChatBaseNewDataView : UIView <QHChatBaseNewDataViewProtcol>

+ (instancetype)createViewToSuperView:(UIView *)superView;

@end

NS_ASSUME_NONNULL_END
