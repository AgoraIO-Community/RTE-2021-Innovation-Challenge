//
//  QHChatBaseViewCell.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/23.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol QHChatBaseViewCellDelegate <NSObject>

@optional

- (void)selectViewCell:(UITableViewCell *)viewCell;

- (void)deselectViewCell:(UITableViewCell *)viewCell;

@end

@interface QHChatBaseViewCell : UITableViewCell

@property (nonatomic, strong) UILabel *contentL;
//@property (nonatomic, strong, readonly) UITextView *contentTV;

@property (nonatomic, weak) id<QHChatBaseViewCellDelegate> delegate;

- (void)makeContent:(UIEdgeInsets)edgeInsets;

// 创建时候会调用，进行 UI & data 的初始化，子类可以重写，无需在init时候调用
- (void)setup;
// 重写 setup 时，如果需要手势，需主动添加，默认 setup 会添加手势 & contentL
- (void)addTapGesture;

@end

NS_ASSUME_NONNULL_END
