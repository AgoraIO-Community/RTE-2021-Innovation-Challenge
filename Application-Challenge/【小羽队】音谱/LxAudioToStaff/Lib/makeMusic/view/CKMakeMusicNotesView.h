//
//  CKMakeMusicNotesView.h
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LxMcNoteView+Bubble.h"

typedef void(^BubbleTapBlock)(LxMcNoteView *seletedNote);

typedef void(^BubblePanBlock)(UIPanGestureRecognizer *pan);

@interface CKMakeMusicNotesView : UIView


/**
 选中音符的index
 */
@property (nonatomic, assign) NSInteger seletedIndex;

/**
 需要显示音符的数据源
 */
@property (nonatomic, strong) NSArray *dataSource;

@property (nonatomic, copy) BubbleTapBlock block;

@property (nonatomic, copy) BubblePanBlock panMoveBlock;
/**
 重置所有音符动画
 */
- (void)resetNoteViewAnimation;

/**
 冰块透明度改变
 */
- (void)iceViewAlpha:(CGFloat)alpha;

/**
 音名添加
 */
- (void)initABCNameArray:(NSArray *)nameArray;

@end
