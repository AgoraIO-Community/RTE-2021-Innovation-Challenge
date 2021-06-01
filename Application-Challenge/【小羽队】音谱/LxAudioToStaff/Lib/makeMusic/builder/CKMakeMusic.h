//
//  CKMakeMusic.h
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CKMakeMusicView.h"
#import "LxMcStaffLineView+MeasureLayout.h"
#import "CKNoteModel.h"

//当前五线谱距离的比例
#define spaceScale 1

@interface CKMakeMusic : NSObject
@property (nonatomic, strong) NSArray *leftNotesArray;
@property (nonatomic, strong) NSArray *rightNotesArray;


/**
 创建五线谱
 */
- (LxMcStaffLineView *)tabsView;

/**
 创建可用音符音符
 */
- (CKMakeMusicNotesView *)leftNotesView;
- (CKMakeMusicNotesView *)rightNotesView;

/**
 显示伴奏
 */
- (CKBellView *)bellView;

/**
 创建作曲游戏界面
 */
- (CKMakeMusicView *)makeMusicView;

/**
 选择四小节按钮
 */
- (UIButton *)fourButton;
/**
 选择八小节按钮
 */
- (UIButton *)eightButton;

- (UIButton *)cutButton;

- (UIButton *)addButton;

/**
 节拍选择器
 */
- (UIView *)beatView:(LxMcStaffBeatsType)beatsType;

/**
 节拍选择器<全功能版>
 */
- (UIView *)beatViewAllFuntion:(LxMcStaffBeatsType)beatsType;

/**
 糖果曲谱切换按钮
 */
- (UIButton *)changeCandyButton;

/**
 所有固定音符数组

 */
- (NSArray *)staticNoteArray;

/**
 获取左边固定音符数组
 */
- (NSArray *)getLeftNotesArray;

/**
获取右边固定音符数组
 */
- (NSArray *)getRightNotesArray;

@end
