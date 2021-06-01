//
//  QHChatBaseView.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/20.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "QHChatBaseProtocol.h"
#import "QHChatBaseConfig.h"
#import "QHChatBaseModel.h"
#import "QHChatBaseUtil.h"
#import "QHChatBaseBuffer.h"

NS_ASSUME_NONNULL_BEGIN

@class QHChatBaseView;

@protocol QHChatBaseViewDelegate <NSObject>

@optional

- (void)chatView:(QHChatBaseView *)view didSelectRowWithData:(NSDictionary *)chatData;

- (void)chatView:(QHChatBaseView *)view didDeselectRowWithData:(NSDictionary *)chatData;

- (void)chatView:(QHChatBaseView *)view didLongSelectRowWithData:(NSDictionary *)chatData;

@end

@interface QHChatBaseView : UIView <QHChatBaseViewProtocol>

@property (nonatomic, strong, readonly) UITableView *mainTableV;
@property (nonatomic, strong, readonly) QHChatBaseBuffer *buffer;
@property (nonatomic, strong) QHChatBaseConfig *config;
@property (nonatomic, weak) id<QHChatBaseViewDelegate> delegate;

+ (instancetype)createChatViewToSuperView:(UIView *)superView;
+ (instancetype)createChatViewToSuperView:(UIView *)superView withConfig:(QHChatBaseConfig *)config;

- (void)insertChatData:(NSArray<NSDictionary *> *)data;

- (void)clearChatData;

- (void)scrollToBottom;

@end

NS_ASSUME_NONNULL_END
