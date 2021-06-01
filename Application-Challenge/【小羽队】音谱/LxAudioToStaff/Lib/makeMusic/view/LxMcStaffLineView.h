//
//  LxMcStaffLineView.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/31.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LxMcStaffHeader.h"
#import "LxMcClefExchangeBtn.h"
@class LxMcNoteView,LxMcMeasureModel;
@protocol LxMcStaffLineViewDelegate <NSObject>
@optional

/** 重新设置滚动视图的内容和偏移 **/
- (void)reSetScrollViewContent;
/**
 *@description 播放完成
 **/
- (void)playEnd;
/**
 *@description 滚动至起始位置
 **/
- (void)reScrollToTop;
/**
 *@description 滚动至末尾位置
 **/
- (void)reScrollToDown;
/**
 *@description 获取滚动的宽度
 **/
- (UIScrollView *)scrollView;
/**
 *@description 设置滚动偏移X
 **/
- (void)scrollTox:(CGFloat)scrollX;

@end

@interface LxMcStaffLineView : UIView
{
    LxMcStaffBeatsType _staffBeatsType;/** 曲谱节拍类型 **/

    CGFloat _originAvalibleOffsetX;/** 自调号部分后开始的originx **/
    CGFloat _currentEndLineOffsetX;/** 当前终止线位置 **/
}


/**
 末尾最后一个音符
 */
@property (nonatomic, strong) LxMcNoteView *highEndNoteView;
@property (nonatomic, strong) LxMcNoteView *lowEndNoteView;
@property (nonatomic, strong) NSArray<LxMcNoteView *> *staticNotesArray;

@property (nonatomic, strong) NSArray *highMiditagArray;
@property (nonatomic, strong) NSArray *lowMiditagArray;
@property (nonatomic, assign) BOOL isAllVersion;
/**
 *@description 代理
 **/
@property (weak, nonatomic) id<LxMcStaffLineViewDelegate> delegate;
/** 曲谱大调类型 **/
@property (assign, nonatomic) LxMcStaffManageType staffManageType;
/**当前显示曲谱类型 **/
@property (assign, nonatomic) LxMcState staffClefState;
/**当前曲谱小节显示上限 **/
@property (assign, nonatomic) LxMcMaxSection maxSection;
/**当前显示曲谱编辑输入类型(琴键输入 拖拽) **/
@property (assign, nonatomic) LxMcEditMode editMode;
/**曲谱编辑大小音谱类型(YES:高音谱编辑 NO:低音谱d编辑) **/
@property (assign, nonatomic) BOOL isEditClefUp;
/**曲谱播放状态 **/
@property (assign, nonatomic) BOOL isPlay;
/** 存放所有五线谱元素 **/
@property (strong, nonatomic) NSMutableArray <CALayer *>* stafflineLayerArray;
/** 存放所有上下加线元素 **/
@property (strong, nonatomic) NSMutableArray <CALayer *>* stafflinePlusLayerArray;
/** 存放所有小节线元素 **/
@property (strong, nonatomic) NSMutableArray <NSMutableArray <CAShapeLayer *>*> *measureLineArray;
/** 存放已高亮的五线谱元素 **/
@property (strong, nonatomic) NSMutableArray <CALayer *>* highlightedStaffLineArray;
/**
 *@description 存放所有小节元素
 **/
@property (strong, nonatomic) NSMutableArray <LxMcMeasureModel *>* ClefMeasureModelArray;

/** Lx description   标记当前加入音符所在纵列  **/
@property (assign,nonatomic) NSInteger currentInsertIndex;
/**
 *@description 终止线
 **/
@property (strong, nonatomic) CAShapeLayer *measureEndLayer;
/**
 *@description 存放所有上加、下加线的数组    key(音符指针） value(下加、上加线数组）
 **/
@property (strong, nonatomic) NSMutableDictionary *additionLineArrayInfo;
/**
 *@description 曲谱节拍类型
 **/
@property (assign, nonatomic) LxMcStaffBeatsType staffBeatsType;
/**
 *@description 高音谱拍号
 **/
@property (strong, nonatomic) CALayer *clefUpBeatsLayer;
/**
 *@description 低音谱拍号
 **/
@property (strong, nonatomic) CALayer *clefDoBeatsLayer;
/**
 *@description 是否可以点击按钮操作
 **/
@property (assign, nonatomic, readonly) BOOL cannotBtnAction;
/**
 *@description 设置外部移入时的判断规避
 **/
@property (assign, nonatomic) BOOL isOutNote;
/**
 *@description 播放音符队列
 **/
@property (strong, nonatomic) NSMutableArray <LxMcNoteView *>*queuePlayNotes;

/** 音符响应播放延迟时间 **/
@property (assign, nonatomic) CGFloat delayPlayTime;

/**
 一个八音区开始miditag
 */
@property (nonatomic, assign) NSInteger highOctaveIndex;
@property (nonatomic, assign) NSInteger lowOctaveIndex;
@property (nonatomic, weak) UIView *highOctaveView;
@property (nonatomic, weak) UIView *lowOctaveView;

/** 高低音谱针对一个八度上下增加的可放置区间 正负值为对应曲谱的中间第三根线为准 **/
@property (assign, nonatomic) CGFloat h_tCount;
@property (assign, nonatomic) CGFloat h_bCount;
@property (assign, nonatomic) CGFloat l_tCount;
@property (assign, nonatomic) CGFloat l_bCount;
/******************************************************点击放置相关***************************************************************/
/**
 *@description 当前是否连线状态标志位
 **/
@property (assign, nonatomic) BOOL lineTapState;
/**
 *@description 点击放置音符类型
 **/
@property (assign, nonatomic) MusicNodeType modelNodeType;
/**
 *@description 点击放置音符是否休止
 **/
@property (assign, nonatomic) BOOL modelRest;
/**
 *@description 点击放置音符是否附点
 **/
@property (assign, nonatomic) BOOL modelDot;
/**
 五线谱当前是几小节
 */
@property (nonatomic, assign) NSInteger sectionIndex;
/**
 休止符数组
 */
@property (nonatomic, weak) NSMutableArray *restNodeViewArray;
/** Lx description   大谱表  **/
@property (nonatomic, strong) CALayer *grandStaffLayer;

/**
 *@description 初始化五线谱类型(确保view。frame为正确位置)
 *@param staffType 曲谱大调类型
 *@param beatsType 节拍类型
 *@param frame 位置信息
 **/
- (void)lx_defaultStaffLineViewWithStaffType:(LxMcStaffManageType)staffType
                                   beatsType:(LxMcStaffBeatsType)beatsType;
/**
 *@description 拖拽音符与五线谱交互
 *@param noteView 拖动的音符
 *@param offsetPoint 偏移量
 *@param return 获取在五线谱中实际符头的位置，以添加至五线谱中
 **/
- (CGPoint)lx_dragHighlightLineWithNoeView:(LxMcNoteView *)noteView
      superOffsetPoint:(CGPoint)offsetPoint;

- (void)addNodeWithNoeView:(LxMcNoteView *)noteView
          superOffsetPoint:(CGPoint)offsetPoint;

- (void)addNodeWithNoeView:(LxMcNoteView *)noteView
          superOffsetPoint:(CGPoint)offsetPoint miditag:(NSInteger)miditag;

/******************************************************UI设置相关***************************************************************/

/**
 糖果音符
 */
@property (nonatomic, assign) BOOL candy;
/**
 *@description 拍号更改
 **/
- (void)resetBeatsType:(LxMcStaffBeatsType)beatsType;
/** 根据符头相对位置获取tag值 **/
- (NSInteger)miditagWithRealPoint:(CGPoint)point
                     dragNoteView:(LxMcNoteView *)noteView;
/** 清除附加线 **/
- (void)clearAdditionLineArrayWithNoteView:(LxMcNoteView *)noteView;
/** 重新布局音符的附加线 **/
- (void)relayoutAdditionLineWithNoteView:(LxMcNoteView *)noteView;
/** 去除所有高亮五线谱 **/
- (void)clearHighlightLine;
/** 上加、下加线处理 **/
- (void)checkAdditionLineAndRelayoutWithNoteView:(LxMcNoteView *)noteView realHeadPoint:(CGPoint )realHeadPoint;
/******************************************************播放相关***************************************************************/
/**
 *@description 获取所有音符，按播放序列排序
 **/
- (NSMutableArray <LxMcNoteView *>*)lx_getPlayQueueNoteArray;
/**
 *@description 开始播放
 **/
- (void)playNoteViewsWithQueueNoteS;
/**
 *@description 关闭音乐
 **/
- (void)stopPlay;

- (void)handlepanGesture:(UIPanGestureRecognizer *)gesture;
/**
 重新设置一个八音区的宽度
 */
- (void)resetOctave;
/**
 设置当前音符需要升或降音的值
 */
- (void)setAlter:(LxMcNoteView *)noteView withMeasureModel:(LxMcMeasureModel *)measuremodel;
/**
 设置默认音符的位置和显示
 */
- (void)setUpStaticNotes;

- (void)resetMidiPlay;

@end

