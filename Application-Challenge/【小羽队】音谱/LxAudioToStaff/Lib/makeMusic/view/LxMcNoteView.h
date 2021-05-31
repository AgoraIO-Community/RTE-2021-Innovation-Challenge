//
//  LxMcNoteView.h
//  SmartPiano
//
//  Created by DavinLee on 2018/2/2.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger,LxMcNoteHeadDirection)
{
    LxMcNoteHead_center = 0,
    LxMcNoteHead_left_down,
    LxMcNoteHead_left_up,
    LxMcNoteHead_right_down,
    LxMcNoteHead_right_up,
};

typedef struct {
    
    /** 是否是固定音符 */
    BOOL fixed;
    /** 音符在小节的最后面还是最前面 */
    BOOL end;
    /** 当五线谱为几小节时显示 */
    NSInteger showInSectionsCount;
    
}FixedNotePosition;

@interface LxMcNoteView : UIImageView
/******************************************************五线谱信息相关***************************************************************/


/**
 *@description 是否音名音符
 **/
@property (assign, nonatomic) BOOL isABC;

/**
 *@description 显示点击音名
 **/
@property (assign, nonatomic) BOOL isTouchABC;

/**
 *@description 音名名称
 **/
@property (copy, nonatomic) NSString *ABCName;

/**
 *@description 是否休止符
 **/
@property (assign, nonatomic) BOOL isRest;
/**
 *@description 是否临时升降号类型
 **/
@property (assign, nonatomic) LxNoteTempMajorType tempMajorType;

/**
 *@description 是否附点音符
 **/
@property (assign, nonatomic) BOOL isDot;
/** Lx description   是否默认显示音符  **/
@property (assign, nonatomic) BOOL isDefault;
/**
 *@description 音符类型
 **/
@property (assign, nonatomic) MusicNodeType noteType;
/**
 *@description miditag
 **/
@property (assign, nonatomic) NSInteger miditag;
/**
 *@description 音符升降tag
 **/
@property (assign, nonatomic) NSInteger alter;
/**
 *@description 音符在小节中的eventDuration
 **/
@property (assign, nonatomic) CGFloat eventDuration;
/**
 *@description 音符在播放队列中的时间位置
 **/
@property (assign, nonatomic) NSTimeInterval playEventPosition;

/**
 默认音符信息
 */
@property (nonatomic, assign) FixedNotePosition fixedPosition;

/**
 强度标记
 */
@property (nonatomic, assign) NSInteger strength;

/**
 强度标记
 */
@property (nonatomic, assign) CGFloat addOringX;


/**
点击移动
 */
@property (nonatomic, assign) BOOL tapMove;


/******************************************************五线谱位置相关***************************************************************/
/**
 *@description 是否高音谱
 **/
@property (assign, nonatomic) BOOL isUpClef;
/**
 *@description 在整个五线谱中序列（高低音谱混合，赋值在lx_getPlayQueueNoteArray时执行和更新）
 **/
@property (assign, nonatomic) NSInteger staffIndex;
/**
 *@description 是否糖果状态
 **/
@property (assign, nonatomic) BOOL candy;

/**
 *@description 上加、下加线数量 :整数表示下加线数量，负数标识上加线数量
 **/
@property (assign, nonatomic) NSInteger additionStaffLineCount;
/**
 *@description 记录符头中心点在五线谱中位置(在搜寻tag值时确定Y坐标，在小节内音符结算时确定x坐标)
 **/
@property (assign, nonatomic) CGPoint rightHeadPosition;
/**日志说什么  图书馆
 *@description 符头是否处于线上，
 **/
@property (assign, nonatomic) BOOL isHeadOnLine;
/**
 *@description 是否已经成功放置在五线谱上
 **/
@property (assign, nonatomic) BOOL isLocatedStaff;
/**
 *@description 音符初始位置中心点(放之前为泡泡中心，放置后为上一次成功放置的位置中心)
 **/
@property (assign, nonatomic) CGPoint startPoint;
/**
 *@description 小节序列
 **/
@property (assign, nonatomic) NSInteger measureIndex;
/**
 *@description 小节内序列
 **/
@property (assign, nonatomic) NSInteger sectionIndex;

/******************************************************音符部件位置相关***************************************************************/
/**
 *@description 音符判断符头位置的标准位置
 **/
@property (assign, nonatomic, readonly) CGPoint judgePoint;
/**
 *@description 获取符头中心相对图形中心偏移
 **/
- (CGPoint)judgePointToCenter;
/**
 *@description 符头的朝向
 **/
@property (assign, nonatomic) LxMcNoteHeadDirection headDirection;

/**
 *@description 弱持有强弱记号
 **/
@property (weak, nonatomic) LxMcNoteView *strengthView;
/******************************************************音符部件***************************************************************/
/**
 *@description 符头layer
 **/
@property (strong, nonatomic) CALayer *layerNoteHead;
/**
 *@description 音符临时升降号
 **/
@property (strong, nonatomic) CALayer *layerTempMajor;
/**
 *@description 符杆附属UI（主要为八分、十六分音符等符杆上的尾巴)
 **/
@property (strong, nonatomic) CALayer *layerNoteBodyAuxiliary;
/**
 *@description 符杆UI
 **/
@property (strong, nonatomic) CALayer *layerNoteBody;
/**
 *@description 附点UI
 **/
@property (strong, nonatomic) CAShapeLayer *layerDot;
/**
 *@description playBottomStar
 **/
@property (strong, nonatomic) CALayer *playStarLayer;
/**
 *@description 在钢琴键输入
 **/
@property (assign, nonatomic) BOOL isKeyboardInput;

/******************************************************音符选中相关***************************************************************/
/** 音符是否选中状态 **/
@property (assign, nonatomic) BOOL selected;
/**
 *@description 选中UI
 **/
@property (strong, nonatomic) UIImageView *selectedBoarderView;
/**
 *@description 切换状态为五线谱音符
 **/
- (void)createStaffUI;

/**
 切换状态为糖果音符
 */
- (void)changeCandyUI:(BOOL)candy;
/**
 *@description  获取音符viewSize
 **/
+ (CGSize)defaultNoteViewSize;
/**
 *@description 清楚所有关于曲谱的UI
 **/
- (void)clearAllStaffUI;
/**
 *@description 检查音符方向并更改
 *@return 是否有方向修改
 **/
- (BOOL)checkDirection;
/**
 *@description 设置音符类型
 **/
- (void)setNodeType:(MusicNodeType)nodeType;
/**
 *@description 获取当前音符所在位置的正确上加。下加线数量,正数表示下加线数量，负数标识上加线数量
 **/
- (NSInteger)caculateRightAdditionStaffLineCount;
/**
 *@description 获取音符所占拍数，暂时只管4拍
 **/
- (CGFloat)durationUnit;
/**
 *@description 显示播放效果
 **/
- (void)showPlayState:(BOOL)playState candy:(BOOL)candy;
/** 区分1、2、3册名字 **/
- (NSString *)reSetImageName:(NSString *)noteName;
/**
 *@description 获取音符类型字符
 **/
- (NSString *)nodeTypeString;


@end
