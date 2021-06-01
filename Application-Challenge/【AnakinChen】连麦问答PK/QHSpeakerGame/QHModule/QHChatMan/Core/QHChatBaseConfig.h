//
//  QHChatBaseConfig.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/23.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

// [iOS笔记—对象的结构体属性单个修改方式 - csdn_hhg的博客 - CSDN博客](https://blog.csdn.net/csdn_hhg/article/details/69388824)
struct QHChatCellConfig {
    // 字体大小
    CGFloat fontSize;
    // 公屏的实际高度，用于计算行数，转屏或者修改公屏显示区域时需重新赋值
    CGFloat cellWidth;
    // 一定要设置 行距 不为0，因为 UILabel 绘制本身是有上下空白处
    CGFloat cellLineSpacing;
};
typedef struct QHChatCellConfig QHChatCellConfig;

NS_ASSUME_NONNULL_BEGIN

@interface QHChatBaseConfig : NSObject

@property (nonatomic, strong) NSString *fontName;

// 只对 QHChatBaseViewCell & 默认计算的高度 有效，自定义的需要自己获取计算
@property (nonatomic) QHChatCellConfig cellConfig;
// 只对 QHChatBaseViewCell 有效
@property (nonatomic) UIEdgeInsets cellEdgeInsets;

// 数据池大小 & 清空数据多少，建议保证 chatCountDelete 小于 chatCountMax
@property (nonatomic) NSInteger chatCountMax;
@property (nonatomic) NSInteger chatCountDelete;

// 刷新的帧率（默认 0.2s）
@property (nonatomic) CGFloat chatReloadDuration;

// 继承 QHChatBaseViewCell 的 长按手势开关 & 长按时长（小于等于 0 为默认值）
@property (nonatomic) BOOL bLongPress;
@property (nonatomic) NSTimeInterval minimumPressDuration;

// 公屏在没有满屏时由下而上显示
@property (nonatomic) BOOL bOpenScorllFromBottom;

@property (nonatomic) BOOL hasUnlock;

// YES：使用 UITableViewAutomaticDimension，NO：使用 自计算 的高度，默认 YES
@property (nonatomic) BOOL bAutoCellHeight;
// 默认 NO，打开 YES：可通过协议实现与上一个内容比较，来控制是否替换
@property (nonatomic) BOOL bInsertReplace;

- (BOOL)isEqualToCellConfig:(QHChatCellConfig)cellConfig;

@end

NS_ASSUME_NONNULL_END
