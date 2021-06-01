//
//  CKMakeMusicView.h
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CKMakeMusicNotesView.h"
#import "LxMcStaffLineView.h"
#import "MackMusicVcScrollBar.h"
#import "CKBellView.h"

typedef void(^NoteStaffPlayCompleted)(void);
typedef void(^beatsBtnClickedBlock)(void);


@interface CKMakeMusicView : UIView

@property (nonatomic, assign) BOOL opreation;

/** 添加按钮 **/
@property (nonatomic, weak) UIButton *addButton;
/** 减少按钮 **/
@property (nonatomic, weak) UIButton *cutButton;

/**
 糖果音符和普音符切换
 */
@property (nonatomic, weak) UIButton *changeCandyButton;
/**
 滚动条
 */
@property (nonatomic, weak) MackMusicVcScrollBar *scrollBar;

/**
 创建五线谱
 */
@property (nonatomic, weak) LxMcStaffLineView *tabsView;

/**
 创建可用音符音符
 */
@property (nonatomic, weak) CKMakeMusicNotesView *leftNotesView;
@property (nonatomic, weak) CKMakeMusicNotesView *rightNotesView;
//- (void)initNotesView;

/**
 显示有伴奏
 */
@property (nonatomic, weak) CKBellView *bellView;

/**
 显示节拍选择
 */
@property (nonatomic, weak) UIView *beatView;

/** 选择八小节 **/
@property (nonatomic, weak) UIButton *eightButton;
/** 选择四小节 **/
@property (nonatomic, weak) UIButton *fourButton;

@property (nonatomic, copy) NoteStaffPlayCompleted playCompleted;

@property (nonatomic, copy) beatsBtnClickedBlock beatsBtnClick;
/** 是否处于放大模式 **/
@property (assign, nonatomic) BOOL isZoomOut;
/**
 添加小节
 @param count 小节数
 */
- (void)addSections:(NSInteger)count;

/**
 刷新UI
 */
- (void)reloadUI;

/**
 视图放大缩小
 */
- (BOOL)zoomScale:(void(^)(void))completed;

/**
 滚动传值
 */
- (void)scrollTox:(CGFloat)scrollX;

/**
设置节拍
 */
- (void)setMetronomeOpen:(BOOL)isOpen;


@end
