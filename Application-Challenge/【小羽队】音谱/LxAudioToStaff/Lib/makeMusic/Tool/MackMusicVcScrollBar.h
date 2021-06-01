//
//  MackMusicVcScrollBar.h
//  SmartPiano
//
//  Created by 李翔 on 17/2/20.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol MakeMusicScrollBarDelegate <NSObject>

- (void)resetOriginX:(CGFloat)offset_x;

@end

@interface MackMusicVcScrollBar : UIImageView

@property (weak, nonatomic) id<MakeMusicScrollBarDelegate> delegate;
/**
 *滚动条
 */
@property (strong, nonatomic) UIImageView *progressView;
/**
 *是否可以拖动
 */
@property (assign, nonatomic) BOOL canDrag;
/**
 *滚动条初始
 */
- (void)setupDefault;
/**
 *重新设置最大移动长度，
 */
- (void)resetScrollBarWithMaxOffset:(CGFloat)maxOffsetX
                        initOffsetX:(CGFloat)initOffsetX
                     currentOriginX:(CGFloat)originX;
/**
 *根据当前实际页面的origin.x 重新布置进度条的位置
 */
- (void)updateProgressViewxWithCurrentPageOriginX:(CGFloat)originX;

@end
