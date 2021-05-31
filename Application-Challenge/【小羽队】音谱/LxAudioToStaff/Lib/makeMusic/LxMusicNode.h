//
//  LxMusicNode.h
//  SmartPiano
//
//  Created by 李翔 on 2017/4/6.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LxMusicDefine.h"
@class LxMusicConfig,
       LxElementNoteNode,
       ASImageNode,
        ASTextNode;
@interface LxMusicNode : NSObject
#pragma mark - 弹奏记录和判断部分

/**
 *@description 音符当前弹奏分数等级
 **/
@property (assign, nonatomic) NotePlayGradeType playScoreGrade;
/**
 *音符当前弹奏状态（音准)
 **/
@property (assign, nonatomic) MusicNodeStatus playStatus;
/**
 *@description 是否统一为绿色显示，非黄色
 **/
@property (assign, nonatomic) BOOL forceGreeShown;
/**
 *@description 音符弹奏等级（节奏)
 **/
@property (assign, nonatomic) NotePlayDurationGrade playGrade;
/**
 *@description 音符弹奏力度等级(力度)
 **/
@property (assign, nonatomic) NotePlayDymicGrade playDymic;
/**
 *@description 音符弹奏时值准确度(时值)
 **/
@property (assign, nonatomic) NotePlayDurationGrade playDuration;
/**
 *@description 弹奏按下分数
 **/
@property (assign, nonatomic) NSInteger playScore;
/**
 *@description 弹奏按下力度分数
 **/
@property (assign, nonatomic) NSInteger playDymicScore;
/**
 *@description 弹奏时值分数
 **/
@property (assign, nonatomic) NSInteger playDurationScore;
/** Lx description   在跟停模式中记录是否谈错过，若谈错在谈对，则为红色显示  **/
@property (assign, nonatomic) BOOL errorPlayed;
#pragma mark - plist部分
/**
 *音符类型
 **/
@property (copy, nonatomic) NSString *type;
@property (assign, nonatomic) MusicNodeType nodeType;
/** Lx description   是否为跳音  **/
@property (assign, nonatomic) BOOL staccato;

/** Lx description   是否为保持音  **/
@property (assign, nonatomic) BOOL tenuto;
/**
 *音符时值
 **/
@property (assign, nonatomic) NSTimeInterval duration;
/** Lx description   同音连线音符时值  **/
@property (assign, nonatomic) NSTimeInterval all_duration;
/**
 *音符力度
 **/
@property (assign, nonatomic) NSInteger dynamics;
/**
 *音符step
 **/
@property (copy, nonatomic) NSString *step;
/**
 *@description 升降调
 **/
@property (assign, nonatomic) NSInteger alter;
/**
 *音阶
 **/
@property (assign, nonatomic) NSInteger octave;
/**
 *左右手 1,右手，，2，左手
 **/
@property (assign, nonatomic) NSInteger hand;
/**
 *@description 指法显示
 **/
@property (copy, nonatomic) NSString *fingering;
/** Lx description   指法是否必须显示指法  **/
@property (assign, nonatomic) BOOL isNecessaryFinger;
/**
 *@description 音阶identify
 **/
@property (copy, nonatomic) NSString * step_id;
/**
 *@description 指法identify
 **/
@property (copy, nonatomic) NSString *fingering_id;
/**
 *@description 歌词
 **/
@property (copy, nonatomic) NSString *lyrics;
#pragma mark - _pos.plist部分
/**
 *音符相对svg x位置
 */
@property (assign, nonatomic) CGFloat x;
/**
 *音符相对svg y位置
 **/
@property (assign, nonatomic) CGFloat y;


#pragma mark - 自定义部分
/**
 *音符唯一标识
 **/
@property (copy, nonatomic) NSString *identyfy;

/**
 *音符的弹奏时间,以毫秒为单位，记得/1000.f
 **/
@property (copy, nonatomic) NSString *position;
/**
 *@description 音符弹奏midi
 **/
@property (assign, nonatomic) NSInteger midiTag;
/**
 *@description 节奏模式彩条宽度(相对于svg)
 **/
@property (assign, nonatomic) CGFloat colorLayerWidth;
/**
 *@description 标志是否已经在键盘上显示
 **/
@property (assign, nonatomic) BOOL showInKeyBoard;
/** @Lx_description  弹奏时间    **/
@property (assign, nonatomic) NSTimeInterval pressTime;
/** @Lx_description  抬起时间    **/
@property (assign, nonatomic) NSTimeInterval putTime;
/** Lx description   同纵列下相同midi  **/
@property (weak, nonatomic) LxMusicNode *sameMidiNodeInEvent;
/** Lx description  同音连线附属UI  **/
@property (strong, nonatomic) NSString *auxiliaryId;
#pragma mark - 显示部分
/** 纵列index **/
@property (assign, nonatomic) NSInteger eventIndex;
/**
 *音符显示layer
 **/
@property (strong, nonatomic) CAShapeLayer *nodeLayer;
/**
 *@description 存放所有的附属UI（符干和附点)
 **/
@property (strong, nonatomic) NSMutableArray <CAShapeLayer *>*auxiliaryLayers;
/**
 *@description 音符显示音阶
 **/
@property (strong, nonatomic) CAShapeLayer *stepLayer;
/**
 *@description 音符显示指法
 **/
@property (strong, nonatomic) CAShapeLayer *fingerLayer;
/**
 *@description 色条
 **/
@property (strong, nonatomic) CAShapeLayer *colorLayer;
/**
 *@description 歌词UI
 **/
@property (strong, nonatomic) ASTextNode *lyricsNode;
/**
 *@description 存放所有layer对应的显示ImageNode;   layer.MemoryAddress - > LxElementNoteNode
 **/
@property (strong, nonatomic) NSMapTable *addToImageNodeTable;
/**
 *@description 获取UI类型
 *@param noteType ui类型字符
 **/
+ (NoteLayerType )getUItypeWithStr:(NSString *)noteType;

#pragma mark - 计算部分
/**
 *@description 获取实际非升降调前的miditag
 **/
- (NSInteger)realMiditag;
/**
 *@description 获取实际分数
 *@param grade 弹奏正确等级
 **/
+ (CGFloat)scoreTransPlayDownWithGrade:(NotePlayGradeType)grade;


/**
 *@description 获取octave的int值
 *@param midiTag midi值
 **/
+ (NSInteger)octaveWithMidiTag:(NSInteger)midiTag;
/**
 *@description 获取step的值
 *@param midiTag midi值
 **/
+ (NSString *)stepWithMiditag:(NSInteger)midiTag;

/**
 *@description 获取实际midi值
 *@param step 音步
 *@param octave 音阶
 **/
+ (NSInteger)miditagWithStep:(NSString *)step octave:(NSInteger)octave;

/**
 *@description 获取当前弹奏的评分
 **/
- (NotePlayGradeType)notePlayGradeWithContentOffsetX:(CGFloat)offset_x baseCheckWidth:(CGFloat)baseWidth;
/**
 *@description 获取按键抬起分数
 *@param  playScale 滚动速度比例
 **/
- (NSInteger)notePlayPutUpWIthStaffplayScale:(CGFloat)playScale;
/**
 *@description 获取弹奏力度的评分分数
 *@param dymicPress 按下力度
 *@return 弹奏力度对应分数
 **/
- (NSInteger)noteplayDimicWithValue:(NSInteger)dymicPress;
#pragma mark - 结果显示部分
/**
 *@description 获取弹奏等级评语
 **/
- (NSString *)playGradeString;
/**
 *@description 获取弹奏力度评语
 **/
- (NSString *)playDymicString;
/**
 *@description 获取抬起评语
 **/
- (NSString *)playDurationString;
#pragma mark - UI部分
/**
 *@description 获取对应imageNode并对应设置隐射关系
 **/
- (LxElementNoteNode *)imageNodeWithLayer:(CAShapeLayer *)layer;
/**
 *@description 设置所有UI透明度
 **/
- (void)setAllPartHide:(BOOL)hide;
/**
 *@description 根据配置显示所有的音符部件
 **/
- (void)AllPartShowWithConfig:(LxMusicConfig *)config;
/**
 *@description 重新设置
 **/
- (void)reset;
/** 根据音阶，音名设置miditag **/
- (void)lx_calculateMidiTag;
/** Lx description   对同一纵列下相同midi音符进行复制  **/
- (void)lx_resetStatusWithSameMidiNode:(LxMusicNode *)sameNode;
/** Lx description   区分是否必须显示指法  **/
- (void)lx_resetNessaryFinger;
@end
