//
//  QHChatBaseModel.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/21.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "QHChatBaseConfig.h"

NS_ASSUME_NONNULL_BEGIN

@interface QHChatBaseModel : NSObject

@property (nonatomic, copy, readonly) NSDictionary *originChatDataDic;
@property (nonatomic, strong) NSAttributedString *chatAttributedText;
@property (nonatomic) QHChatCellConfig cellConfig;
@property (nonatomic) CGFloat cellHeight;

- (instancetype)initWithChatData:(NSDictionary *)data;

@end

NS_ASSUME_NONNULL_END
